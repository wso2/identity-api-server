/*
 * Copyright (c) 2019, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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
import org.wso2.carbon.identity.oauth2.device.constants.Constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds constants related to Application Management components.
 */
public class ApplicationManagementConstants {

    private ApplicationManagementConstants() {

    }

    private static final String APPLICATION_MANAGEMENT_PREFIX = "APP-";
    public static final String APPLICATION_MANAGEMENT_PATH_COMPONENT = "/applications";
    public static final String APPLICATION_TEMPLATE_MANAGEMENT_PATH_COMPONENT = "/templates";
    public static final String INBOUND_PROTOCOLS_PATH_COMPONENT = "/inbound-protocols";
    public static final String INBOUND_PROTOCOL_OAUTH2_PATH_COMPONENT = "/oidc";
    public static final String INBOUND_PROTOCOL_SAML_PATH_COMPONENT = "/saml";
    public static final String INBOUND_PROTOCOL_PASSIVE_STS_PATH_COMPONENT = "/passive-sts";
    public static final String INBOUND_PROTOCOL_WS_TRUST_PATH_COMPONENT = "/ws-trust";

    private static final Map<String, String> OAUTH_GRANT_TYPE_NAMES = new LinkedHashMap<>();
    public static final String DEFAULT_NAME_ID_FORMAT = "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified";
    public static final String DEFAULT_CERTIFICATE_ALIAS = "wso2carbon";
    public static final String ADVANCED_CONFIGURATIONS = "advancedConfigurations";
    public static final String TEMPLATE_ID = "templateId";
    public static final String NAME = "name";
    public static final String CLIENT_ID = "clientId";
    public static final String ISSUER = "issuer";

    public static final String NON_EXISTING_USER_CODE = "30007 - ";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        // Client errors with codes defined at Application Management Layer.
        UNSUPPORTED_FILTER_ATTRIBUTE("60004",
                "Filtering using the attempted attribute is not supported.",
                "Filtering cannot be done with the '%s' attribute. " +
                        "Filtering is only supported with 'name', 'clientId', and 'issuer' attributes."),
        INVALID_FILTER_FORMAT("60004",
                "Invalid filter query format.",
                "Filter needs to be in the format <attribute>+<operation>+<value>. Eg: name+eq+john"),
        INVALID_FILTER_OPERATION("60004",
                "Attempted filtering operation is invalid.",
                "Attempted filtering operation '%s' is invalid. Please use one of the supported " +
                        "filtering operations such as 'eq', 'co', 'sw', 'ew', 'and', 'or'."),
        APPLICATION_NOT_FOUND("60006",
                "Application not found.",
                "Application cannot be found for the provided id: %s in the tenantDomain: %s."),
        DISABLE_REDIRECT_OR_POST_BINDINGS("APP-60007",
                "Disabling HTTP_POST or HTTP_REDIRECT is not allowed",
                "HTTP_POST or HTTP_REDIRECT cannot be disabled"),

        // Client errors defined at API level.
        INVALID_INBOUND_PROTOCOL("60501",
                "Inbound protocol not found.",
                "Inbound protocol cannot be found for the provided id: %s"),
        INBOUND_NOT_CONFIGURED("60502",
                "Inbound protocol not configured.",
                "Inbound protocol: %s not configured for application id: %s."),
        ERROR_WS_TRUST_METADATA_SERVICE_NOT_FOUND("60504",
                "WS-Trust protocol is not supported.",
                "STS admin service is unavailable at the moment."),
        ERROR_APPLICATION_LIMIT_REACHED("60503",
                "Unable to create an application.",
                "Maximum number of allowed applications have been reached."),
        NON_EXISTING_USER_ID("60504", "User not found",
                "Non exiting user for the given userid: %s."),
        NON_EXISTING_REQ_ATTRIBUTES("60505", "Invalid attribute name.",
                "Invalid attribute name provided as required attribute."),

        ADDITIONAL_SP_PROP_NOT_SUPPORTED("60506",
                "Unsupported application property.",
                "'additionalSpProperties' is not yet supported in this version of the API."),

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
        PAGINATED_LISTING_NOT_IMPLEMENTED("65004",
                "Paginated listing not supported.",
                "Paginated listing capability is not supported in this version of the API."),
        ERROR_RESOLVING_APPLICATION_TEMPLATE("65005",
                "Error occurred while retrieving application template.",
                "Unexpected error occurred while retrieving application template."),
        ERROR_PROCESSING_REQUEST("65006",
                "Unexpected Processing Error.",
                "Server encountered an unexpected error when creating the application."),

        // Server errors defined at REST API layer.
        APPLICATION_CREATION_WITH_TEMPLATES_NOT_IMPLEMENTED("65501",
                "Unsupported Operation.",
                "Application creation with templates is not supported in this version of the API."),
        ERROR_CODE_ERROR_INVALID_SEARCH_FILTER("65502", "Search request validation failed.", "Invalid search filter."),
        ERROR_RETRIEVING_USER_BY_ID("65503", "Error occurred while retrieving user",
                "Error occurred while retrieving user by userid: %s."),
        ERROR_RETRIEVING_USERSTORE_MANAGER("65504", "Error retrieving userstore manager.",
                "Error occurred while retrieving userstore manager.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            if (code.contains(APPLICATION_MANAGEMENT_PREFIX)) {
                return code;
            }
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
        OAUTH_GRANT_TYPE_NAMES.put("organization_switch", "Organization Switch");
        OAUTH_GRANT_TYPE_NAMES.put(Constants.DEVICE_FLOW_GRANT_TYPE, "Device Code");
    }

    public static Map<String, String> getOAuthGrantTypeNames() {

        return OAUTH_GRANT_TYPE_NAMES;
    }

    /**
     * This class contains the constant values related to the Application Template properties.
     */
    public static class TemplateProperties {

        public static final String INBOUND_PROTOCOL = "protocol";
        public static final String TYPES = "types";
        public static final String CATEGORY = "category";
        public static final String DISPLAY_ORDER = "display-order";
        public static final String TEMPLATE_GROUP = "template-group";

        // Application Template Search.
        public static final List<String> SEARCH_KEYS = Collections.unmodifiableList(Arrays.asList("description",
                "image", "category", "displayOrder", "authenticationProtocol", "templateGroup"));
        public static final String SEARCH_KEY_NAME = "name";
        public static final String SEARCH_KEY_NAME_INTERNAL = "resourceName";
        public static final String ATTR_KEY = "attributeKey";
        public static final String ATTR_VALUE = "attributeValue";
        public static final String TEMPLATE_TYPE_KEY = "resourceTypeName";
        public static final String TENANT_DOMAIN_KEY = "tenantDomain";

        private TemplateProperties() {

        }
    }
}
