/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.registration.execution.v1.constants;

/**
 * Constants related to registration flow endpoint.
 */
public class RegistrationExecutionEndpointConstants {

    private RegistrationExecutionEndpointConstants() {
    }

    public static final String REGISTRATION_FLOW_PREFIX = "RFM-";
    public static final String DYNAMIC_REGISTRATION_PORTAL_ENABLED = "SelfRegistration.EnableDynamicPortal";

    /**
     * Error messages related to registration flow management.
     */
    public enum ErrorMessage {

        ERROR_CODE_DYNAMIC_REGISTRATION_PORTAL_DISABLED("60101",
                "Dynamic registration portal is not enabled.",
                "Dynamic registration portal is not enabled. Please contact your administrator."
        ),

        ERROR_CODE_GET_GOVERNANCE_CONFIG("65101",
                "Error occurred while retrieving the governance configuration.",
                "Server encountered an error while retrieving the governance configuration."
        );

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return REGISTRATION_FLOW_PREFIX + code;
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
