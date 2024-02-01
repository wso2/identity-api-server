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

package org.wso2.carbon.identity.api.server.organization.selfservice.v1.util;

/**
 * Constants of Self service Mgt Endpoint module.
 */
public class SelfServiceMgtConstants {

    private SelfServiceMgtConstants () {

    }

    // Error handling related constants.
    private static final String SELF_SERVICE_MANAGEMENT_PREFIX = "SSM-";
    public static final String USER_STORE_ALREADY_EXIST_ERROR_CODE = "SUS-60002";
    public static final String USER_STORE_ALREADY_EXIST_ERROR = "Cannot add user store. Domain name";
    public static final String CONFIGS_NOT_FOUND_ERROR = "Self service configs could not be found.";

    // System application creation related constants.
    public static final String PROPERTY_APP_NAME = "name";
    public static final String PROPERTY_TOKEN_EXPIRY = "applicationAccessTokenExpiryInSeconds";
    public static final String PROPERTY_INBOUND_PROTOCOL = "inboundProtocolConfiguration";
    public static final String PROPERTY_OIDC_PROTOCOL = "oidc";
    public static final String PROPERTY_ACCESS_TOKEN = "accessToken";
    public static final String SELF_SERVICE_DEFAULT_APP_NAME = "SelfService.SystemDefaultAppName";
    public static final String SELF_SERVICE_DEFAULT_TOKEN_EXPIRY_TIME = "SelfService.SystemDefaultTokenExpiryTime";

    // Lite user-store related constants.
    public static final String LITE_USER_CONNECTION_URL = "SelfService.LiteUser.ConnectionURL";
    public static final String LITE_USER_USER_STORE_NAME = "SelfService.LiteUser.UserStoreName";
    public static final String LITE_USER_CONNECTION_PASSWORD = "SelfService.LiteUser.ConnectionPassword";
    public static final String LITE_USER_CONNECTION_USERNAME = "SelfService.LiteUser.ConnectionUsername";
    public static final String LITE_USER_CONNECTION_DRIVER_NAME = "SelfService.LiteUser.ConnectionDriverName";
    public static final String PROPERTY_USER_STORE_NAME = "name";
    public static final String PROPERTY_USER_STORE_PROPERTIES = "properties";
    public static final String PROPERTY_USER_STORE_CONNECTION_URL = "url";
    public static final String PROPERTY_USER_STORE_CONNECTION_USERNAME = "userName";
    public static final String PROPERTY_USER_STORE_CONNECTION_PASSWORD = "password";
    public static final String PROPERTY_USER_STORE_CONNECTION_DRIVER = "driverName";

    // Organization mgt governance configs related constants.
    public static final String SELF_SERVICE_ADMIN_EMAIL_VERIFICATION_PROPERTY_NAME =
            "Organization.SelfService.AdminEmailVerification";
    public static final String SELF_SERVICE_ONBOARD_ADMIN_TO_SUB_ORG_PROPERTY_NAME =
            "Organization.SelfService.OnboardAdminToSubOrg";
    public static final String SELF_SERVICE_ENABLE_PROPERTY_NAME = "Organization.SelfService.Enable";
    public static final String SELF_SERVICE_GOVERNANCE_CONNECTOR = "organization-self-service";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_VALUE = "value";

    // Resource path constants.
    public static final String CREATE_LITE_USER_STORE_REQUEST_JSON = "classpath:lite-userstore-request.json";
    public static final String ENABLE_LITE_USER_REQUEST_JSON = "classpath:enable-lite-user.json";
    public static final String DISABLE_LITE_USER_REQUEST_JSON = "classpath:disable-lite-user.json";
    public static final String CREATE_SELF_SERVICE_APP_REQUEST_JSON = "classpath:create-app-request.json";

    /**
     * Enums for error messages.
     */
    public enum ErrorMessage {

        ERROR_ONBOARDING_LITE_USER_STORE("65001",
                "Error occurred while onboarding lite user-store",
                "Unexpected error occurred while onboarding the lite user-store for self service mgt."),
        ERROR_UPDATING_GOVERNANCE_CONFIG("65002",
                "Error occurred while updating governance configurations",
                "Unexpected error occurred while updating lite user-store governance configurations " +
                        "for self service mgt."),
        ERROR_CREATING_SYSTEM_APP("65003",
                   "Error occurred while creating system application",
                   "Unexpected error occurred while creating system application for self service mgt."),
        ERROR_DELETING_SYSTEM_APP("65004",
                "Error occurred while deleting system application",
                "Unexpected error occurred while deleting system application for self service mgt."),
        ERROR_RETRIEVING_SELF_SERVICE_CONFIG("65006",
                "Error occurred while retrieving self service governance configurations",
                "Unexpected error occurred while retrieving self service governance configurations"),
        ERROR_UPDATING_SELF_SERVICE_CONFIG("65007",
                "Error occurred while updating self service governance configurations",
                "Unexpected error occurred while updating self service governance configurations");


        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            if (code.contains(SELF_SERVICE_MANAGEMENT_PREFIX)) {
                return code;
            }
            return SELF_SERVICE_MANAGEMENT_PREFIX + code;
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
