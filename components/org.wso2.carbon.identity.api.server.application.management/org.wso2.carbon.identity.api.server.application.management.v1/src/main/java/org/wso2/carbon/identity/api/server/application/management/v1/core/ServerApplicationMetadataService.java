/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthProtocolMetadata;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolProperty;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.mgt.AbstractInboundAuthenticatorConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Util.base64URLDecode;

/**
 * Calls internal osgi services to get required application metadata.
 */
public class ServerApplicationMetadataService {

    private static final Log LOG = LogFactory.getLog(ServerApplicationMetadataService.class);

    public List<AuthProtocolMetadata> getInboundProtocols(Boolean customOnly) {

        List<AuthProtocolMetadata> authProtocolMetadataList = new ArrayList<>();

        // Add custom inbound protocols
        Map<String, AbstractInboundAuthenticatorConfig> allCustomAuthenticators =
                ApplicationManagementServiceHolder.getApplicationManagementService().getAllInboundAuthenticatorConfig();

        for (Map.Entry<String, AbstractInboundAuthenticatorConfig> entry : allCustomAuthenticators
                .entrySet()) {
            AuthProtocolMetadata protocol = new AuthProtocolMetadata();
            protocol.setName(entry.getValue().getName());
            protocol.setDisplayName(entry.getValue().getFriendlyName());
            authProtocolMetadataList.add(protocol);
        }

        if (customOnly == null || !customOnly) {
            // Add default inbound protocols
            authProtocolMetadataList.add(getInboundProtocolMetadata("saml", "SAML2 Web SSO Configuration"));
            authProtocolMetadataList.add(getInboundProtocolMetadata("oidc", "OAuth/OpenID Connect Configuration"));
            authProtocolMetadataList.add(getInboundProtocolMetadata("ws-federation",
                    "WS-Federation (Passive) Configuration"));
            authProtocolMetadataList.add(getInboundProtocolMetadata("ws-trust",
                    "WS-Trust Security Token Service Configuration"));
        }

        return authProtocolMetadataList;
    }

    private AuthProtocolMetadata getInboundProtocolMetadata(String name, String displayName) {

        AuthProtocolMetadata metadata = new AuthProtocolMetadata();
        metadata.setName(name);
        metadata.setDisplayName(displayName);
        return metadata;
    }

    public CustomInboundProtocolMetaData getCustomProtocolMetadata(String inboundProtocolId) {

        String protocolName = base64URLDecode(inboundProtocolId);
        Map<String, AbstractInboundAuthenticatorConfig> allCustomAuthenticators =
                ApplicationManagementServiceHolder.getApplicationManagementService().getAllInboundAuthenticatorConfig();

        // Loop through all custom inbound protocols and match the name.
        for (Map.Entry<String, AbstractInboundAuthenticatorConfig> entry : allCustomAuthenticators
                .entrySet()) {
            if (entry.getValue().getName().equals(protocolName)) {
                CustomInboundProtocolMetaData metaData = new CustomInboundProtocolMetaData();
                metaData.setDisplayName(entry.getValue().getFriendlyName());

                // set properties
                metaData.setProperties(getCustomInboundProtocolProperties(
                        entry.getValue().getConfigurationProperties()));
                return metaData;
            }
        }

        // Throw 404 error if the protocol not found
        throw buildApiError(ApplicationManagementConstants.ErrorMessage.ERROR_INBOUND_PROTOCOL_NOT_FOUND);
    }

    private List<CustomInboundProtocolProperty> getCustomInboundProtocolProperties(Property[] properties) {

        List<CustomInboundProtocolProperty> protocolProperties = new ArrayList<>();
        for (Property property : properties) {
            CustomInboundProtocolProperty protocolProperty = new CustomInboundProtocolProperty();

            if (StringUtils.isNotBlank(property.getName())) {
                protocolProperty.setName(property.getName());
            }
            if (StringUtils.isNotBlank(property.getDisplayName())) {
                protocolProperty.setDisplayName(property.getDisplayName());
            }
            if (StringUtils.isNotBlank(property.getType())) {
                protocolProperty.setType(CustomInboundProtocolProperty.TypeEnum.valueOf(
                        property.getType().toUpperCase(Locale.ENGLISH)));
            } else {
                protocolProperty.setType(CustomInboundProtocolProperty.TypeEnum.STRING);
            }
            protocolProperty.setRequired(property.isRequired());
            if (property.getOptions() != null) {
                protocolProperty.setAvailableValues(Arrays.asList(property.getOptions()));
            }
            if (StringUtils.isNotBlank(property.getDefaultValue())) {
                protocolProperty.setDefaultValue(property.getDefaultValue());
            }
            if (StringUtils.isNotBlank(property.getRegex())) {
                protocolProperty.setValidationRegex(property.getRegex());
            }
            protocolProperty.setDisplayOrder(property.getDisplayOrder());
            protocolProperty.setIsConfidential(property.isConfidential());

            protocolProperties.add(protocolProperty);
        }
        return protocolProperties;
    }

    public Response getOIDCMetadata() {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    public Response getSAMLMetadata() {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    public Response getWSTrustMetadata() {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    private APIError buildApiError(ApplicationManagementConstants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse = buildErrorResponse(errorEnum);
        return new APIError(errorEnum.getHttpStatusCode(), errorResponse);
    }

    private ErrorResponse buildErrorResponse(ApplicationManagementConstants.ErrorMessage errorEnum) {

        return new ErrorResponse.Builder()
                .withCode(errorEnum.getCode())
                .withDescription(errorEnum.getDescription())
                .withMessage(errorEnum.getMessage())
                .build(LOG, errorEnum.getDescription());
    }
}
