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

package org.wso2.carbon.identity.api.server.idv.provider.common;

/**
 * Constants related to identity verification service.
 */
public class Constants {

    public static final String IDV_API_PATH_COMPONENT = "/api/server/v1/idv-providers/";
    public static final String ERROR_PREFIX = "IDVP-";

    /**
     * Enum for identity verification related errors.
     * Error Code - code to identify the error.
     * Error Message - What went wrong.
     * Error Description - Why it went wrong.
     */
    public enum ErrorMessage {

        // Client errors - IDVP server APIs.
        ERROR_CODE_IDVP_NOT_FOUND("60000",
                "Invalid identity verification provider id.",
                "Could not find an identity verification provider with given id %s."),
        ERROR_CODE_IDVP_EXISTS("60001",
                "Identity Verification Provider already exists with the given name.",
                "Identity Verification Provider: %s already exists with the given name."),

        // Server errors - IDVP server APIs.
        ERROR_ADDING_IDVP("65000",
                "Unable to add identity verification provider.",
                "Server encountered an error while adding the identity verification provider: %s."),
        ERROR_UPDATING_IDVP("65001",
                "Unable to update identity verification provider.",
                "Server encountered an error while updating the identity verification provider."),
        ERROR_RETRIEVING_IDVP("65002",
                "Unable to retrieve identity verification provider.",
                "Server encountered an error while retrieving the identity verification provider: %s."),
        ERROR_DELETING_IDVP("65003",
                "Unable to delete identity verification provider.",
                "Server encountered an error while deleting the identity verification provider."),
        ERROR_RETRIEVING_TENANT("65004",
                "Error retrieving tenant.",
                "Error occurred while retrieving tenant id for the tenant domain: %s."),
        ERROR_RETRIEVING_IDVPS("6505",
                "Unable to retrieve identity verification providers.",
                "Server encountered an error while retrieving the identity verification providers in " +
                        "the tenant: %s."),

        // Client errors - IDVP user APIs.
        ERROR_CODE_IDV_PROVIDER_NOT_FOUND("10000", "Identity Provider not found.",
                "Provided Identity Verification Provider: %s is not found.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return ERROR_PREFIX + code;
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
