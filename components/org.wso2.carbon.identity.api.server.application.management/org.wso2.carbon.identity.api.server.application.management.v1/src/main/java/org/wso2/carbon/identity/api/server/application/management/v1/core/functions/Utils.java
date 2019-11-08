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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.security.SecurityConfigException;

import java.util.List;
import java.util.function.Consumer;
import javax.ws.rs.core.Response;

/**
 * Utility functions.
 */
public class Utils {

    private static final Log log = LogFactory.getLog(Utils.class);

    public static boolean getBooleanValue(Boolean aBoolean) {

        return aBoolean != null && aBoolean;
    }

    public static void setIfNotNull(String value, Consumer<String> consumer) {

        if (value != null) {
            consumer.accept(value);
        }
    }

    public static void setIfNotNull(Boolean value, Consumer<Boolean> consumer) {

        if (value != null) {
            consumer.accept(value);
        }
    }

    public static APIError buildClientError(String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Invalid Request.")
                .withDescription(message)
                .build(log, message);

        Response.Status status = Response.Status.BAD_REQUEST;
        return new APIError(status, errorResponse);
    }

    public static APIError buildServerErrorResponse(Exception e, String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Server error while trying the attempted operation.")
                .withDescription(message)
                .build(log, e, message);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }

    public static APIError buildServerErrorResponse(String message) {

        return buildServerErrorResponse(null, message);
    }

    public static APIError buildNotImplementedErrorResponse(String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder
                .withMessage("Server error while trying the attempted operation.")
                .withDescription(message)
                .build(log, message);

        Response.Status status = Response.Status.NOT_IMPLEMENTED;
        return new APIError(status, errorResponse);
    }

    public static void rollbackInbounds(List<InboundAuthenticationRequestConfig> currentlyAddedInbounds) {

        for (InboundAuthenticationRequestConfig inbound : currentlyAddedInbounds) {
            switch (inbound.getInboundAuthType()) {
                case FrameworkConstants.StandardInboundProtocols.SAML2:
                    rollbackSAMLServiceProvider(inbound);
                    break;
                case FrameworkConstants.StandardInboundProtocols.OAUTH2:
                    rollbackOAuth2ConsumerApp(inbound);
                    break;
                case FrameworkConstants.StandardInboundProtocols.WS_TRUST:
                    rollbackWsTrustService(inbound);
                    break;
                default:
                    // No rollbacks required for other inbounds.
                    break;
            }
        }
    }

    private static void rollbackWsTrustService(InboundAuthenticationRequestConfig inbound) {

        try {
            String trustedServiceAudience = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getStsAdminService().removeTrustedService(trustedServiceAudience);
        } catch (SecurityConfigException e) {
            throw Utils.buildServerErrorResponse(e, "Error while trying to rollback wsTrust configuration. "
                    + e.getMessage());
        }
    }

    private static void rollbackOAuth2ConsumerApp(InboundAuthenticationRequestConfig inbound) {

        try {
            String consumerKey = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getOAuthAdminService().removeOAuthApplicationData(consumerKey);
        } catch (IdentityOAuthAdminException e) {
            throw Utils.buildServerErrorResponse(e, "Error while trying to rollback OAuth2/OpenIDConnect " +
                    "configuration." + e.getMessage());
        }
    }

    private static void rollbackSAMLServiceProvider(InboundAuthenticationRequestConfig inbound) {

        try {
            String issuer = inbound.getInboundAuthKey();
            ApplicationManagementServiceHolder.getSamlssoConfigService().removeServiceProvider(issuer);
        } catch (IdentityException e) {
            throw Utils.buildServerErrorResponse(e, "Error while trying to rollback SAML2 configuration."
                    + e.getMessage());
        }
    }

}
