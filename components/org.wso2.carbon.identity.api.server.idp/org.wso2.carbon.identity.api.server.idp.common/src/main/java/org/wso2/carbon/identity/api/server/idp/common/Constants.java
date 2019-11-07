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

package org.wso2.carbon.identity.api.server.idp.common;

/**
 * Identity Provider Management constant class.
 */
public class Constants {

    public static final String IDP_MANAGEMENT_PREFIX = "IDP-";
    public static final String IDP_PATH_COMPONENT = "/identity-providers";
    public static final String JWKS_URI = "jwksUri";
    public static final String META_DATA_SAML = "meta_data_saml";
    public static final String SELECT_MODE = "SelectMode";
    public static final String SELECT_MODE_METADATA = "Metadata File Configuration";

    // IdP property keys.
    public static final String PROP_DISPLAY_NAME = "DisplayName";

    // Patch operation paths.
    public static final String NAME_PATH = "/name";
    public static final String DESCRIPTION_PATH = "/description";
    public static final String IMAGE_PATH = "/image";
    public static final String IS_PRIMARY_PATH = "/isPrimary";
    public static final String IS_ENABLED_PATH = "/isEnabled";
    public static final String IS_FEDERATION_HUB_PATH = "/isFederationHub";
    public static final String HOME_REALM_PATH = "/homeRealmIdentifier";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_LISTING_IDPS("42021",
                "Unable to list existing identity providers.",
                "Server encountered an error while listing the identity providers."),
        ERROR_CODE_ERROR_ADDING_IDP("52002",
                "Unable to add identity provider.",
                "Server encountered an error while adding the identity provider."),
        ERROR_CODE_ERROR_DELETING_IDP("52004",
                "Unable to delete identity provider.",
                "Server encountered an error while deleting the identity provider for the identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP("52003",
                "Unable to retrieve identity provider.",
                "Server encountered an error while retrieving the identity provider for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_META_AUTHENTICATORS("52021",
                "Unable to retrieve meta federated authenticator list.",
                "Server encountered an error while retrieving the meta federated authenticators."),
        ERROR_CODE_ERROR_RETRIEVING_META_CONNECTORS("52022",
                "Unable to retrieve meta outbound connector list.",
                "Server encountered an error while retrieving the meta outbound connector list."),
        ERROR_CODE_ERROR_RETRIEVING_META_AUTHENTICATOR("52023",
                "Unable to retrieve meta federated authenticator.",
                "Server encountered an error while retrieving the meta federated authenticator with identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_META_CONNECTOR("52024",
                "Unable to retrieve meta outbound connector.",
                "Server encountered an error while retrieving the meta outbound connector with identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_AUTHENTICATORS("52025",
                "Unable to retrieve identity provider's federated authenticator list.",
                "Server encountered an error while retrieving the federated authenticators of " +
                        "identity provider %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTORS("52026",
                "Unable to retrieve identity provider's outbound connector list.",
                "Server encountered an error while retrieving the outbound connectors of " +
                        "identity provider %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_AUTHENTICATOR("52027",
                "Unable to retrieve identity provider's federated authenticator.",
                "Server encountered an error while retrieving the federated authenticator with identifier %s of " +
                        "identity provider %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTOR("52028",
                "Unable to retrieve identity provider's outbound connector.",
                "Server encountered an error while retrieving the outbound connector with identifier %s of " +
                        "identity provider %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_PROVISIONING("52029",
                "Unable to retrieve identity provider's provisioning config.",
                "Server encountered an error while retrieving the provisioning config of identity provider %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_CLAIMS("52030",
                "Unable to retrieve identity provider claim config.",
                "Server encountered an error while retrieving the identity provider claim config for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_ROLES("52031",
                "Unable to retrieve identity provider role config.",
                "Server encountered an error while retrieving the identity provider role config for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_JIT("52032",
                "Unable to retrieve identity provider JIT config.",
                "Server encountered an error while retrieving the identity provider JIT config for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP("52005",
                "Unable to update identity provider.",
                "Server encountered an error while updating the identity provider for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_AUTHENTICATOR("52033",
                "Unable to update identity provider federated authenticator.",
                "Server encountered an error while updating the identity provider federated authenticator for " +
                        "identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_CONNECTOR("52034",
                "Unable to update identity provider outbound connector.",
                "Server encountered an error while updating the identity provider outbound connector for " +
                        "identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_CLAIMS("52035",
                "Unable to update identity provider claims.",
                "Server encountered an error while updating the identity provider claim config for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_ROLES("52036",
                "Unable to update identity provider roles.",
                "Server encountered an error while updating the identity provider role config for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_JIT("52037",
                "Unable to update identity provider Just-In-Time provisioning.",
                "Server encountered an error while updating the identity provider Just-In-Time provisioning config " +
                        "for identifier %s."),
        ERROR_CODE_IDP_NOT_FOUND("42002", "Resource not found.", "Unable to find a resource matching the provided " +
                "identity provider identifier %s."),
        ERROR_CODE_AUTHENTICATOR_NOT_FOUND_FOR_IDP("42022",
                "Resource not found.",
                "Unable to find any claims matching the provided claim dialect identifier %s."),
        ERROR_CODE_CONNECTOR_NOT_FOUND_FOR_IDP("42023", "Resource not found.", "Unable to find a resource matching " +
                "the provided federated authenticator identifier %s of identity " +
                "provider identifier %s."),
        ERROR_CODE_PAGINATION_NOT_IMPLEMENTED("52038",
                "Pagination not supported.",
                "Pagination capabilities are not supported in this version of the API."),
        ERROR_CODE_FILTERING_NOT_IMPLEMENTED("52039",
                "Filtering not supported.",
                "Filtering capability is not supported in this version of the API."),
        ERROR_CODE_SORTING_NOT_IMPLEMENTED("52040",
                "Sorting not supported.",
                "Sorting capability is not supported in this version of the API."),
        ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED("52041",
                "Attribute filtering not supported.",
                "Attribute filtering capability is not supported in this version of the API."),
        ERROR_CODE_INVALID_LOCAL_CLAIM_ID("52042",
                "Invalid claim identifier.",
                "Invalid claim identifier %s provided in claim config."),
        ERROR_CODE_INVALID_INPUT("52043", "Invalid input.", "One of the given inputs is invalid."),
        ERROR_CODE_INVALID_SAML_METADATA("52044", "Invalid SAML metadata.", "SAML metadata is invalid/empty.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return IDP_MANAGEMENT_PREFIX + code;
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
