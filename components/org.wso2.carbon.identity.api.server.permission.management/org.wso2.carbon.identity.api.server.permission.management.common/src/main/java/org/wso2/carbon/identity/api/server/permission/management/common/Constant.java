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

package org.wso2.carbon.identity.api.server.permission.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Permission Management constant class.
 */
public class Constant {

    /**
     * Enum for error messages.
     */
    public enum ErrorMessage {

        ERROR_CODE_ERROR_GETTING_PERMISSIONS("500025",
                "Unable to get all permissions.",
                "Server encounted a error when getting permissions"),
        ERROR_CODE_INVALID_INPUT("50037",
                "Invalid input.",
                "One of the given inputs is invalid.")
        ;

        private static final Log log = LogFactory.getLog(ErrorMessage.class);
        private final String code;
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

        /**
         * Get the proper error message mapped to the server side error.
         *
         * @param serverCode Error code from the server.
         * @return Error message.
         */
        public static ErrorMessage getMappedErrorMessage(String serverCode) {

            try {
                if (log.isDebugEnabled()) {
                    log.debug("Retrieving error message mapping for server code: " + serverCode);
                }
                String errorCode = resourceBundle.getString(serverCode);
                return messageIndex.get(errorCode);
            } catch (Throwable e) {
                if (log.isDebugEnabled()) {
                    log.debug("Error mapping not found for server code: " + serverCode + 
                            ". Using default error message.", e);
                }
            }
            return ErrorMessage.ERROR_CODE_INVALID_INPUT;
        }

        @Override
        public String toString() {
            return code + " | " + message;
        }
    }
}
