/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound;

import org.apache.commons.lang.ArrayUtils;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocolListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.APPLICATION_MANAGEMENT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.INBOUND_PROTOCOLS_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Converts Inbound Authentication configuration of an application into the API model.
 */
public class InboundAuthConfigToApiModel implements Function<ServiceProvider, List<InboundProtocolListItem>> {

    @Override
    public List<InboundProtocolListItem> apply(ServiceProvider application) {

        String applicationId = application.getApplicationResourceId();
        InboundAuthenticationConfig inboundAuthConfig = application.getInboundAuthenticationConfig();

        if (inboundAuthConfig != null) {
            if (ArrayUtils.isNotEmpty(inboundAuthConfig.getInboundAuthenticationRequestConfigs())) {

                List<InboundProtocolListItem> inboundProtocolListItems = new ArrayList<>();
                Arrays.stream(inboundAuthConfig.getInboundAuthenticationRequestConfigs()).forEach(
                        inbound -> inboundProtocolListItems.add(buildInboundProtocolListItem(applicationId, inbound)));

                return inboundProtocolListItems;
            }
        }

        return Collections.emptyList();
    }

    private InboundProtocolListItem buildInboundProtocolListItem(String applicationId,
                                                                 InboundAuthenticationRequestConfig inbound) {

        return new InboundProtocolListItem()
                // TODO: add the friendly name from metadata service.
                .name(inbound.getFriendlyName())
                .type(inbound.getInboundAuthType())
                .self(buildInboundProtocolGetUrl(applicationId, inbound.getInboundAuthType()));
    }

    private String buildInboundProtocolGetUrl(String applicationId, String inboundAuthType) {

        String inboundPathComponent;
        switch (inboundAuthType) {
            case (FrameworkConstants.StandardInboundProtocols.SAML2):
                inboundPathComponent = ApplicationManagementConstants.INBOUND_PROTOCOL_SAML_PATH_COMPONENT;
                break;
            case (FrameworkConstants.StandardInboundProtocols.OAUTH2):
                inboundPathComponent = ApplicationManagementConstants.INBOUND_PROTOCOL_OAUTH2_PATH_COMPONENT;
                break;
            case (FrameworkConstants.StandardInboundProtocols.PASSIVE_STS):
                inboundPathComponent = ApplicationManagementConstants.INBOUND_PROTOCOL_PASSIVE_STS_PATH_COMPONENT;
                break;
            case (FrameworkConstants.StandardInboundProtocols.WS_TRUST):
                inboundPathComponent = ApplicationManagementConstants.INBOUND_PROTOCOL_WS_TRUST_PATH_COMPONENT;
                break;
            default:
                try {
                    inboundPathComponent = "/" + URLEncoder.encode(inboundAuthType, StandardCharsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    throw Utils.buildServerError("Error while building inbound protocol for " +
                            "inboundType: " + inboundAuthType, e);
                }
                break;
        }

        return ContextLoader.buildURIForBody(V1_API_PATH_COMPONENT + APPLICATION_MANAGEMENT_PATH_COMPONENT + "/"
                + applicationId + INBOUND_PROTOCOLS_PATH_COMPONENT + inboundPathComponent).toString();
    }
}
