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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds constants related to Application Management components.
 */
public class ApplicationManagementConstants {

    private ApplicationManagementConstants() {

    }

    private static final String APPLICATION_MANAGEMENT_PREFIX = "APP-";
    public static final String APPLICATION_MANAGEMENT_PATH_COMPONENT = "/applications";
    public static final String INBOUND_PROTOCOLS_PATH_COMPONENT = "/inbound-protocols";
    public static final String INBOUND_PROTOCOL_OAUTH2_PATH_COMPONENT = "/oidc";
    public static final String INBOUND_PROTOCOL_SAML_PATH_COMPONENT = "/saml";
    public static final String INBOUND_PROTOCOL_PASSIVE_STS_PATH_COMPONENT = "/passive-sts";
    public static final String INBOUND_PROTOCOL_WS_TRUST_PATH_COMPONENT = "/ws-trust";

    private static final Map<String, String> OAUTH_GRANT_TYPE_NAMES = new LinkedHashMap<>();
    public static final String DEFAULT_NAME_ID_FORMAT = "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress";
    public static final String DEFAULT_CERTIFICATE_ALIAS = "wso2carbon";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Client errors with codes defined at Application Management Layer.
        UNSUPPORTED_FILTER_ATTRIBUTE("60004", "Filtering using the attempted attribute is not supported.",
                "Filtering cannot be done with the '%s' attribute. " +
                        "Filtering is only supported with the 'name' attribute."),
        INVALID_FILTER_FORMAT("60004",
                "Invalid format user for filtering.",
                "Filter needs to be in the format <attribute>+<operation>+<value>. Eg: name+eq+john"),
        INVALID_FILTER_OPERATION("60004",
                "Attempted filtering operation is not invalid.",
                "Attempted filtering operation '%s' is invalid. " +
                        "Please use one of the supported filtering operations such as 'eq', 'co', 'sw' or 'ew'."),
        APPLICATION_NOT_FOUND("60006",
                "Application not found.",
                "Application cannot be found for the provided id: %s in the tenantDomain: %s."),

        // Client errors defined at API level.
        INVALID_INBOUND_PROTOCOL("60501",
                "Inbound protocol not found.",
                "Inbound protocol cannot be found for the provided id: %s"),
        INBOUND_NOT_CONFIGURED("60502",
                "Inbound protocol not configured.",
                "Inbound protocol: %s not configured for application id: %s."),

        // Server Errors.
        ERROR_RETRIEVING_SAML_METADATA("65001",
                "Error occurred while retrieving SAML Metadata.",
                "Unexpected error occurred while retrieving SAML Metadata."),
        ERROR_RETRIEVING_WS_TRUST_METADATA("65001",
                "Error occurred while retrieving WS Trust Metadata.",
                "Unexpected error occurred while retrieving WS Trust Metadata."),
        SORTING_NOT_IMPLEMENTED("65002",
                "Sorting not supported.",
                "Sorting capability is not supported in this version of the API."),
        ATTRIBUTE_FILTERING_NOT_IMPLEMENTED("65003",
                "Attribute filtering not supported.",
                "Attribute filtering capability is not supported in this version of the API."),

        // Server errors defined at REST API layer.
        APPLICATION_CREATION_WITH_TEMPLATES_NOT_IMPLEMENTED("65501",
                "Unsupported Operation.",
                "Application creation with templates is not supported in this version of the API.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
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
