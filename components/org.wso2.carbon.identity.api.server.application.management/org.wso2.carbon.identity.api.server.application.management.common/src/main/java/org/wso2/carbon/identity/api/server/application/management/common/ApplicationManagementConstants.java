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
package org.wso2.carbon.identity.api.server.application.management.common;

import org.wso2.carbon.identity.oauth.common.GrantType;
import org.wso2.carbon.identity.oauth.common.OAuthConstants;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ws.rs.core.Response;

/**
 * Holds constants related to Application Management components.
 */
public class ApplicationManagementConstants {

    private ApplicationManagementConstants() {

    }

    private static final String APPLICATION_MANAGEMENT_PREFIX = "APP-";
    public static final String APPLICATION_MANAGEMENT_PATH_COMPONENT = "/applications";
    private static final Map<String, String> OAUTH_GRANT_TYPE_NAMES = new LinkedHashMap<>();

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_CODE_INVALID_INPUT("50001",
                Response.Status.BAD_REQUEST,
                "Invalid input.",
                "One of the given inputs is invalid."),
        ERROR_CODE_UNSUPPORTED_FILTER_ATTRIBUTE("50002",
                Response.Status.BAD_REQUEST,
                "Filtering using the attempted attribute is not supported.",
                "Filtering cannot be done with the '%s' attribute. " +
                        "Filtering is only supported with the 'name' attribute."),
        ERROR_CODE_INVALID_FILTER_FORMAT("50003",
                Response.Status.BAD_REQUEST,
                "Invalid format user for filtering.",
                "Filter needs to be in the format <attribute>+<operation>+<value>. Eg: name+eq+john"),
        ERROR_CODE_INVALID_FILTER_OPERATION("55004",
                Response.Status.BAD_REQUEST,
                "Attempted filtering operation is not invalid.",
                "Attempted filtering operation '%s' is invalid. " +
                        "Please use one of the supported filtering operations such as 'eq', 'co', 'sw' or 'ew'."),
        ERROR_CODE_APPLICATION_NOT_FOUND("50005",
                Response.Status.NOT_FOUND,
                "Application not found.",
                "Application cannot be found for the provided id: %s in the tenantDomain: %s."),
        ERROR_INBOUND_PROTOCOL_NOT_FOUND("50006", Response.Status.NOT_FOUND,
                "Inbound protocol not found.",
                "Inbound protocol cannot be found for the provided id."),
        // Server Errors.
        ERROR_CODE_SORTING_NOT_IMPLEMENTED("55001",
                Response.Status.NOT_IMPLEMENTED,
                "Sorting not supported.",
                "Sorting capability is not supported in this version of the API."),
        ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED("55002",
                Response.Status.NOT_IMPLEMENTED,
                "Attribute filtering not supported.",
                "Attribute filtering capability is not supported in this version of the API.");

        private final String code;
        private Response.Status httpStatusCode;
        private final String message;
        private final String description;

        private static final Map<String, ErrorMessage> messageIndex = new HashMap<>(ErrorMessage.values().length);
        static final String BUNDLE = "ServerClientErrorMappings";
        static ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE);

        static {
            for (ErrorMessage em : ErrorMessage.values()) {
                messageIndex.put(em.code, em);
            }
        }

        ErrorMessage(String code, Response.Status statusCode, String message, String description) {

            this.code = code;
            this.httpStatusCode = statusCode;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return APPLICATION_MANAGEMENT_PREFIX + code;
        }

        public String getMessage() {

            return message;
        }

        public String getDescription() {

            return description;
        }

        public Response.Status getHttpStatusCode() {

            return httpStatusCode;
        }

        /**
         * Get the proper error message mapped to the server side error.
         *
         * @param serverCode Error code from the server.
         * @return Error message.
         */
        public static ErrorMessage getMappedErrorMessage(String serverCode) {

            try {
                String errorCode = resourceBundle.getString(serverCode);
                return messageIndex.get(errorCode);
            } catch (Throwable e) {
                // Ignore if error mapping has invalid input.
            }
            return ErrorMessage.ERROR_CODE_INVALID_INPUT;
        }

        @Override
        public String toString() {

            return code + " | " + message;
        }
    }

    static {
        OAUTH_GRANT_TYPE_NAMES.put(OAuthConstants.GrantTypes.AUTHORIZATION_CODE, "Code");
        OAUTH_GRANT_TYPE_NAMES.put(OAuthConstants.GrantTypes.IMPLICIT, "Implicit");
        OAUTH_GRANT_TYPE_NAMES.put(OAuthConstants.GrantTypes.PASSWORD, "Password");
        OAUTH_GRANT_TYPE_NAMES.put(OAuthConstants.GrantTypes.CLIENT_CREDENTIALS, "Client Credential");
        OAUTH_GRANT_TYPE_NAMES.put(OAuthConstants.GrantTypes.REFRESH_TOKEN, "Refresh Token");
        OAUTH_GRANT_TYPE_NAMES.put("urn:ietf:params:oauth:grant-type:saml1-bearer", "SAML1");
        OAUTH_GRANT_TYPE_NAMES.put(GrantType.SAML20_BEARER.toString(), "SAML2");
        OAUTH_GRANT_TYPE_NAMES.put(OAuthConstants.GrantTypes.IWA_NTLM, "IWA-NTLM");
    }

    public static Map<String, String> getOAuthGrantTypeNames() {
        return OAUTH_GRANT_TYPE_NAMES;
    }
}
