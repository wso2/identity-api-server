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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Identity Provider Management constant class.
 */
public class Constants {

    public static final String IDP_MANAGEMENT_PREFIX = "IDP-";
    public static final String IDP_PATH_COMPONENT = "/identity-providers";
    public static final String TRUSTED_TOKEN_ISSUER_PATH_COMPONENT = "/trusted-token-issuers";

    public static final String IDP_TEMPLATE_PATH_COMPONENT = "/templates";
    public static final String PATH_SEPERATOR = "/";
    public static final String JWKS_URI = "jwksUri";
    public static final String IDP_ISSUER_NAME = "idpIssuerName";
    public static final String TEMPLATE_ID = "templateId";
    public static final String META_DATA_SAML = "meta_data_saml";
    public static final String SELECT_MODE = "SelectMode";
    public static final String SELECT_MODE_METADATA = "Metadata File Configuration";
    public static final String TEMPLATE_MGT_ERROR_CODE_DELIMITER = "_";

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
    public static final String IDP_ISSUER_NAME_PATH = "/idpIssuerName";
    public static final String ALIAS_PATH = "/alias";
    public static final String CERTIFICATE_JWKSURI_PATH = "/certificate/jwksUri";
    public static final String CERTIFICATE_PATH_REGEX = "/certificate/certificates/[0-9]+";

    // IdP pagination constants.
    public static final String PAGINATION_LINK_FORMAT = "?offset=%d&limit=%d";
    public static final String PAGINATION_WITH_FILTER_LINK_FORMAT = "?offset=%d&limit=%d&filter=%s";
    public static final String PAGE_LINK_REL_NEXT = "next";
    public static final String PAGE_LINK_REL_PREVIOUS = "previous";

    // IdP attributes constants.
    public static final String IS_PRIMARY = "isPrimary";
    public static final String IS_FEDERATION_HUB = "isFederationHub";
    public static final String HOME_REALM_IDENTIFIER = "homeRealmIdentifier";
    public static final String CERTIFICATE = "certificate";
    public static final String ALIAS = "alias";
    public static final String CLAIMS = "claims";
    public static final String ROLES = "roles";
    public static final String GROUPS = "groups";
    public static final String FEDERATED_AUTHENTICATORS = "federatedAuthenticators";
    public static final String PROVISIONING = "provisioning";
    public static final String GOOGLE_PRIVATE_KEY = "google_prov_private_key";

    // IdP template property keys
    public static final String PROP_CATEGORY = "category";
    public static final String PROP_DISPLAY_ORDER = "displayOrder";
    public static final String PROP_SERVICES = "services";

    // IdP template services
    public static final String SERV_AUTHENTICATION = "authentication";
    public static final String SERV_PROVISIONING = "provisioning";

    // IdP Template Search.
    public static final List<String> SEARCH_KEYS = Collections.unmodifiableList(Arrays.asList("description", "image",
            "category", "displayOrder", "services"));
    public static final String SEARCH_KEY_NAME = "name";
    public static final String SEARCH_KEY_SERVICES = "services";
    public static final String SEARCH_KEY_NAME_INTERNAL = "resourceName";
    public static final String ATTR_KEY = "attributeKey";
    public static final String ATTR_VALUE = "attributeValue";
    public static final String TEMPLATE_TYPE_KEY = "resourceTypeName";
    public static final String TENANT_DOMAIN_KEY = "tenantDomain";
    public static final String SEARCH_VALUE_AUTHENTICATION_PROVISIONING = SERV_PROVISIONING + "," + SERV_AUTHENTICATION;

    public static final String MAX_FEDERATED_AUTHENTICATORS_PROPERTY_LIMIT =
            "MaxFederatedAuthenticatorPropertiesPerIdP";

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors starting from 600xx.
        ERROR_CODE_IDP_NOT_FOUND("60002", "Resource not found.", "Unable to find a resource matching the provided " +
                "identity provider identifier %s."),
        ERROR_CODE_ERROR_LISTING_IDPS("60021",
                "Unable to list existing identity providers.",
                "Server encountered an error while listing the identity providers."),
        ERROR_CODE_AUTHENTICATOR_NOT_FOUND_FOR_IDP("60022",
                "Resource not found.",
                "Unable to find federated authenticator with identifier %s."),
        ERROR_CODE_CONNECTOR_NOT_FOUND_FOR_IDP("60023", "Resource not found.", "Unable to find outbound provisioning " +
                "connector identifier %s."),
        ERROR_CODE_INVALID_LOCAL_CLAIM_ID("60024",
                "Invalid claim uri.",
                "Invalid claim uri %s provided in claim config."),
        ERROR_CODE_INVALID_INPUT("60025", "Invalid input.", "One of the given inputs is invalid.%s"),
        ERROR_CODE_INVALID_SAML_METADATA("60026", "Invalid SAML metadata.", "SAML metadata is invalid/empty."),
        ERROR_CODE_INVALID_DEFAULT_AUTHENTICATOR("60027", "Invalid default authenticator ID",
                "Provided value for the default authenticator ID is invalid."),
        ERROR_CODE_INVALID_DEFAULT_OUTBOUND_CONNECTOR("60028", "Invalid default outbound connector ID",
                "Provided value for the default outbound connector ID is invalid."),
        ERROR_CODE_OUTBOUND_PROVISIONING_CONFIG_NOT_FOUND("60029", "Unable to update Outbound " +
                "Provisioning Connector", "Outbound Provisioning Connector properties have not specified " +
                "for connector : %s"),
        ERROR_CODE_INVALID_USER_CLAIM_URI("60030", "Invalid user ID claim URI",
                "User ID claim URI: %s does not match with the claim mappings"),
        ERROR_CODE_INVALID_ROLE_CLAIM_URI("60031", "Invalid role claim URI",
                "Role claim URI: %s does not match with the claim mappings"),
        ERROR_CODE_NOT_EXISTING_CLAIM_URI("60032", "Invalid claim URI",
                "One or more local claim URIs do not exist"),
        ERROR_CODE_NOT_EXISTING_USER_CLAIM_URI("60033", "Invalid user ID claim URI",
                "User ID claim URI is not a local claim for tenant: %s"),
        ERROR_CODE_NOT_EXISTING_ROLE_CLAIM_URI("60034", "Invalid role claim URI",
                "Role claim URI is not a local claim for tenant: %s"),
        ERROR_CODE_IDP_LIMIT_REACHED("60035",
                "Unable to create an identity provider.",
                "Maximum number of allowed identity providers have been reached."),
        ERROR_CODE_INVALID_CERTIFICATE_FORMAT("60036", "Invalid IDP certificate format.",
                "One or more IDP certificate formats are invalid"),
        ERROR_CODE_DUPLICATE_OIDC_SCOPES("60037", "Duplicate OIDC Scopes.",
                "Cannot set scopes in both Scopes and Additional Query Parameters." +
                        " Recommend to use Scopes field."),
        ERROR_CODE_INVALID_OIDC_SCOPES("60038", "Invalid OIDC Scopes.",
                "Scopes must contain 'openid'."),
        ERROR_CODE_ERROR_LISTING_TRUSTED_TOKEN_ISSUERS("60021",
                "Unable to list existing trusted token issuers.",
                "Server encountered an error while listing the trusted token issuers."),
        ERROR_CODE_ENDPOINT_PROVIDED_FOR_SYSTEM_AUTH("60039", "No endpoint configuration is allowed.",
                "No endpoint configuration must be " +
                "provided for the system defined federated authenticators %s."),
        ERROR_CODE_PROPERTIES_PROVIDED_FOR_USER_AUTH("60040", "No properties are allowed.",
                "No properties must be provided for the user defined " +
                "federated authenticators %s."),
        ERROR_CODE_NO_ENDPOINT_PROVIDED("60041", "No endpoint provided.", "Endpoint " +
                "configuration must be provided for the user defined federated authenticators %s."),
        ERROR_CODE_NON_DECODABLE_AUTH_ID("60042", "Non-decodable authenticator ID.",
                "Unable to decode the provided authenticator ID %s."),
        ERROR_CODE_NO_SYSTEM_AUTHENTICATOR_FOUND("60043", "No system authenticator found.",
                "No system authenticator found for the provided authenticator Id %s."),
        ERROR_COED_MULTIPLE_USER_DEFINED_AUTHENTICATORS_FOUND("60044", "Invalid federated " +
                "authenticators combination.", "Allow to have only one user defined " +
                "federated authenticator and no system authenticators."),
        ERROR_CODE_MAX_FEDERATED_AUTHENTICATOR_PROPERTY_EXCEEDED("60045",
                "Maximum number of authenticator properties exceeded.",
                "Maximum number of allowed properties for a federated authenticator have been exceeded. " +
                        "Max allowed: %s."),

        // Server Error starting from 650xx.
        ERROR_CODE_ERROR_ADDING_IDP("65002",
                "Unable to add identity provider.",
                "Server encountered an error while adding the identity provider."),
        ERROR_CODE_ERROR_DELETING_IDP("65004",
                "Unable to delete identity provider.",
                "Server encountered an error while deleting the identity provider for the identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP("65003",
                "Unable to retrieve identity provider.",
                "Server encountered an error while retrieving the identity provider for identifier %s."),
        ERROR_CODE_ERROR_EXPORTING_IDP("65006",
                "Unable to export identity provider.",
                "Server encountered an error while exporting the identity provider for identifier %s."),
        ERROR_CODE_ERROR_IMPORTING_IDP("65007",
                "Unable to import identity provider.",
                "Server encountered an error while importing the identity provider."),
        ERROR_CODE_ERROR_RETRIEVING_META_AUTHENTICATORS("65021",
                "Unable to retrieve meta federated authenticator list.",
                "Server encountered an error while retrieving the meta federated authenticators."),
        ERROR_CODE_ERROR_RETRIEVING_META_CONNECTORS("65022",
                "Unable to retrieve meta outbound connector list.",
                "Server encountered an error while retrieving the meta outbound connector list."),
        ERROR_CODE_ERROR_RETRIEVING_META_AUTHENTICATOR("65023",
                "Unable to retrieve meta federated authenticator.",
                "Server encountered an error while retrieving the meta federated authenticator with identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_META_CONNECTOR("65024",
                "Unable to retrieve meta outbound connector.",
                "Server encountered an error while retrieving the meta outbound connector with identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_AUTHENTICATORS("65025",
                "Unable to retrieve identity provider's federated authenticator list.",
                "Server encountered an error while retrieving the federated authenticators of " +
                        "identity provider %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTORS("65026",
                "Unable to retrieve identity provider's outbound connector list.",
                "Server encountered an error while retrieving the outbound connectors of " +
                        "identity provider %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_AUTHENTICATOR("65027",
                "Unable to retrieve identity provider's federated authenticator.",
                "Server encountered an error while retrieving the federated authenticator with identifier %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTOR("65028",
                "Unable to retrieve identity provider's outbound connector.",
                "Server encountered an error while retrieving the outbound connector with identifier %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_PROVISIONING("65029",
                "Unable to retrieve identity provider's provisioning config.",
                "Server encountered an error while retrieving the provisioning config of identity provider %s"),
        ERROR_CODE_ERROR_RETRIEVING_IDP_CLAIMS("65030",
                "Unable to retrieve identity provider claim config.",
                "Server encountered an error while retrieving the identity provider claim config for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_ROLES("65031",
                "Unable to retrieve identity provider role config.",
                "Server encountered an error while retrieving the identity provider role config for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_JIT("65032",
                "Unable to retrieve identity provider JIT config.",
                "Server encountered an error while retrieving the identity provider JIT config for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_GROUPS("65033", "Unable to retrieve identity provider group config.",
                "Server encountered an error while retrieving the identity provider group config for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_ASSOCIATION("65034", "Unable to retrieve identity provider federated " +
                "association config.", "Server encountered an error while retrieving the identity provider " +
                "federated association config for identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_CONNECTED_APPS("65042",
                "Unable to retrieve identity provider connected applications.",
                "Server encountered an error while retrieving the identity provider connected applications %s."),
        ERROR_CODE_ERROR_UPDATING_IDP("65005",
                "Unable to update identity provider.",
                "Server encountered an error while updating the identity provider for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_AUTHENTICATOR("65033",
                "Unable to update identity provider federated authenticator.",
                "Server encountered an error while updating the identity provider federated authenticator for " +
                        "identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_CONNECTOR("65034",
                "Unable to update identity provider outbound connector.",
                "Server encountered an error while updating the identity provider outbound connector for " +
                        "identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_CLAIMS("65035",
                "Unable to update identity provider claims.",
                "Server encountered an error while updating the identity provider claim config for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_ROLES("65036",
                "Unable to update identity provider roles.",
                "Server encountered an error while updating the identity provider role config for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_JIT("65037",
                "Unable to update identity provider Just-In-Time provisioning.",
                "Server encountered an error while updating the identity provider Just-In-Time provisioning config " +
                        "for identifier %s."),
        ERROR_CODE_PAGINATION_NOT_IMPLEMENTED("65038",
                "Pagination not supported.",
                "Pagination capabilities are not supported in this version of the API."),
        ERROR_CODE_FILTERING_NOT_IMPLEMENTED("65039",
                "Filtering not supported.",
                "Filtering capability is not supported in this version of the API."),
        ERROR_CODE_SORTING_NOT_IMPLEMENTED("65040",
                "Sorting not supported.",
                "Sorting capability is not supported in this version of the API."),
        ERROR_CODE_ATTRIBUTE_FILTERING_NOT_IMPLEMENTED("65041",
                "Attribute filtering not supported.",
                "Attribute filtering capability is not supported in this version of the API."),
        ERROR_CODE_BUILDING_LINKS("65042", "Error building page links", "Error occurred during building page links. " +
                "%s"),
        ERROR_CODE_ERROR_UPDATING_IDP_GROUPS("65036",
                "Unable to update identity provider groups.",
                "Server encountered an error while updating the identity provider " +
                        "group config for identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_ASSOCIATION("65037", "Unable to update identity provider federated " +
                "association config.", "Server encountered an error while updating the identity provider " +
                "federated association config for identifier %s."),
        ERROR_CODE_ERROR_LISTING_IDP_TEMPLATES("65050", "Unable to list existing identity provider " +
                "templates.", "Error occured while listing identity provider templates."),
        ERROR_CODE_ERROR_ADDING_IDP_TEMPLATE("65051", "Unable to add IDP template.",
                "Error occurred while trying to add the IDP template."),
        ERROR_CODE_ERROR_DELETING_IDP_TEMPLATE("65052", "Unable to delete IDP template.",
                "Error occurred while trying to delete the IDP template with identifier %s."),
        ERROR_CODE_ERROR_UPDATING_IDP_TEMPLATE("65053", "Unable to update IDP template.",
                "Error occurred while updating the IDP template with identifier %s."),
        ERROR_CODE_ERROR_RETRIEVING_IDP_TEMPLATE("65054", "Unable to retrieve IDP template.",
                "Error occurred while retrieving the IDP template with identifier %s"),
        ERROR_CODE_ERROR_INVALID_SEARCH_FILTER("65055", "Search request validation failed.",
                "Invalid search filter."),
        ERROR_CODE_VALIDATING_LOCAL_CLAIM_URIS("65056", "Error while validation local claim URIs",
                "Error while validating claim URIs against local claims");

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
