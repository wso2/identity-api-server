/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.webhook.management.v2.constants;

/**
 * Constants related to Webhook Management.
 */
public class WebhookMgtEndpointConstants {

    public static final int DEFAULT_LIMIT = 10;
    public static final int DEFAULT_OFFSET = 0;

    public static final String WEBHOOK_PATH_COMPONENT = "/webhooks";
    public static final String CHANNEL_PATH_COMPONENT = "/channels";
    public static final String ORGANIZATION_PATH_COMPONENT = "/organizations";

    public static final String PAGE_LINK_REL_NEXT = "next";
    public static final String PAGE_LINK_REL_PREVIOUS = "previous";

    private WebhookMgtEndpointConstants() {

    }

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        // Client errors.
        ERROR_NO_WEBHOOK_FOUND_ON_GIVEN_ID("WEBHOOKMGT-60010",
                "Webhook is not found.",
                "No webhook is found for given webhook id: %s"),
        ERROR_STATUS_NOT_MODIFIABLE("WEBHOOKMGT-60012",
                "Webhook status is not modifiable.",
                "The status of a webhook cannot be changed through this operation. Use the activate or " +
                        "deactivate operation instead."),
        ERROR_EMPTY_PATCH_REQUEST("WEBHOOKMGT-60013",
                "Invalid request.",
                "The patch request body must contain at least one property to modify."),
        ERROR_NULL_MEMBER_IN_PATCH_REQUEST("WEBHOOKMGT-60014",
                "Invalid request.",
                "The property %s cannot be null. No webhook property can be removed, so null is not accepted " +
                        "as a removal directive.");

        private final String code;
        private final String message;
        private final String description;

        ErrorMessage(String code, String message, String description) {

            this.code = code;
            this.message = message;
            this.description = description;
        }

        public String getCode() {

            return code;
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
