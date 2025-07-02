/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.api.resource.v1.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Constants related to API resource management.
 */
public class APIResourceMgtEndpointConstants {

    private APIResourceMgtEndpointConstants() {
    }

    public static final String BUSINESS_API_RESOURCE_TYPE = "BUSINESS";
    public static final String MCP_SERVER_RESOURCE_TYPE = "MCP";
    public static final String API_RESOURCE_MANAGEMENT_PREFIX = "API-RESOURCE-";
    public static final String API_RESOURCE_PATH_COMPONENT = "/api-resources";
    public static final String API_RESOURCE_COLLECTION_PATH_COMPONENT = "/api-resource-collections";
    public static final String ENABLE_SCIM2_ROLES_V3_API = "SCIM2.EnableScim2RolesV3Api";
    public static final String SCIM2_ROLES_V3_API_PATH = "/scim2/v3/Roles";
    public static final String SCIM2_ROLES_V3_ORG_API = "/o/scim2/v3/Roles";
    public static final List<String> ALLOWED_API_RESOURCE_TYPES = Collections.unmodifiableList(
            Arrays.asList(
                    BUSINESS_API_RESOURCE_TYPE,
                    MCP_SERVER_RESOURCE_TYPE
            )
    );
    private static final List<String> allowedAttributeList = new ArrayList<>();
    public static final List<String> ALLOWED_SEARCH_ATTRIBUTES = Collections.unmodifiableList(allowedAttributeList);

    private static final List<String> supportedRequiredAttributeList = new ArrayList<>();
    public static final List<String> SUPPORTED_REQUIRED_ATTRIBUTES = Collections
            .unmodifiableList(supportedRequiredAttributeList);

    private static final List<String> supportedRequiredAttributeListCollectionsAPI = new ArrayList<>();
    public static final List<String> SUPPORTED_REQUIRED_ATTRIBUTES_COLLECTIONS_API = Collections
            .unmodifiableList(supportedRequiredAttributeListCollectionsAPI);

    public static final String RESTRICTED_OAUTH2_SCOPES = "OAuth.RestrictedScopes.RestrictedScope";
    public static final Integer DEFAULT_LIMIT = 10;
    public static final String ASC_SORT_ORDER = "ASC";
    public static final String DESC_SORT_ORDER = "DESC";
    public static final String ATTRIBUTES_DELIMITER = ",";

    static {
        allowedAttributeList.add("description");
        allowedAttributeList.add("type");
        allowedAttributeList.add("requires_authorization");
        allowedAttributeList.add("scopes");

        supportedRequiredAttributeList.add("properties");
        supportedRequiredAttributeListCollectionsAPI.add("apiResources");
    }

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_CODE_API_RESOURCE_LIMIT_REACHED("60001",
                "Unable to create an API resource.",
                "Maximum number of allowed API resources have been reached."),
        ERROR_CODE_API_RESOURCE_NOT_FOUND("60002",
                "Unable to find the API resource.",
                "Unable to find the API resource with the id: %s in the tenant domain."),
        ERROR_CODE_INVALID_API_RESOURCE_NAME("60003",
                "Invalid API resource name provided.", "API resource name is required."),
        ERROR_CODE_INVALID_API_RESOURCE_IDENTIFIER("60004",
                "Invalid API resource identifier provided.", "API resource identifier is required."),
        ERROR_CODE_INVALID_SCOPE_NAME("60005",
                "Invalid scope name provided.", "Scope name is required."),
        ERROR_CODE_REMOVED_SCOPES_PATCH_NOT_SUPPORTED("60006",
                "Removed scopes patching is not supported yet.",
                "Removed scopes patching is not supported yet for API resources."),
        ERROR_CODE_INVALID_SEARCH_ATTRIBUTE("60007",
                "Invalid search attribute.",
                "Invalid search attribute: %s."),
        ERROR_CODE_RESTRICTED_SCOPE_NAME("60008",
                "Restricted scope name provided.", "Scope name is restricted."),
        ERROR_CODE_RESTRICTED_OIDC_SCOPES("60009", "Existing OIDC scope name provided.",
                "Provided scopes name is already exists in the system as an OIDC scope."),
        ERROR_CODE_INVALID_LIMIT("60010", "Invalid limit provided.",
                "Limit should be a positive integer."),
        ERROR_CODE_BOTH_BEFORE_AFTER_PROVIDED("60011", "Invalid before/after provided.",
                "Both before and after parameters cannot be provided at the same time."),
        ERROR_CODE_SYSTEM_API_RESOURCE_NOT_MODIFIABLE("60012", "Cannot modify or delete System APIs.",
                "Cannot modify or delete the read-only System APIs."),
        ERROR_CODE_INVALID_REQ_ATTRIBUTES("60013", "Invalid attribute name.",
                "Invalid attribute name provided as required attribute."),
        ERROR_CODE_API_RESOURCE_COLLECTION_NOT_FOUND("60014",
                "Unable to find the API resource collection.",
                "Unable to find the API resource collection with the id: %s in the tenant domain."),
        ERROR_CODE_AUTHORIZATION_DETAILS_TYPE_NOT_FOUND("60015",
                "Unable to find the authorization details type.",
                "Authorization details types for resource id '%s' could not be found in the tenant domain."),
        ERROR_CODE_INVALID_AUTHORIZATION_DETAILS_SCHEMA("60016",
                "Authorization details schema contains errors.",
                "The provided input schema does not comply with the 2020-12 JSON Schema specification: %s"),
        // Server errors.
        ERROR_CODE_ADD_API_RESOURCE("65001", "Error while adding api resource.", "Server encountered an error while " +
                "adding the api resource."),

        ERROR_CODE_VALIDATE_SCOPES("65002", "Error while validating scopes.", "Server encountered an error while " +
                "validating the scopes."),
        ;
        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return API_RESOURCE_MANAGEMENT_PREFIX + code;
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
}
