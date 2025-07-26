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

package org.wso2.carbon.identity.api.server.common.error;

import org.apache.commons.logging.Log;

import static org.wso2.carbon.identity.api.server.common.Util.getCorrelation;
import static org.wso2.carbon.identity.api.server.common.Util.isCorrelationIDPresent;

/**
 * Common ErrorResponse Object for all the server API related errors.
 */
public class ErrorResponse extends ErrorDTO {

    private static final long serialVersionUID = -3502358623560083025L;

    /**
     * ErrorResponse Builder.
     */
    public static class Builder {
        private String code;
        private String message;
        private String description;

        public Builder() {

        }

        public Builder withCode(String code) {
            this.code = code;
            return this;

        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;

        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ErrorResponse build() {
            ErrorResponse error = new ErrorResponse();
            error.setCode(this.code);
            error.setMessage(this.message);
            error.setDescription(this.description);
            error.setRef(getCorrelation());
            return error;
        }

        public ErrorResponse build(Log log, Exception e, String message) {
            ErrorResponse error = build();
            String errorMessageFormat = "errorCode: %s | message: %s";
            String errorMsg = String.format(errorMessageFormat, error.getCode(), message);
            if (!isCorrelationIDPresent()) {
                errorMsg = String.format("correlationID: %s | " + errorMsg, error.getRef());
            }
            log.error(errorMsg, e);
            return error;
        }

        /**
         * Error response builder for bad requests without exceptions.
         *
         * @param log Logger.
         * @param message Error message.
         * @return ErrorResponse object.
         */
        public ErrorResponse build(Log log, String message) {

            ErrorResponse error = build();
            String errorMessageFormat = "errorCode: %s | message: %s";
            String errorMsg = String.format(errorMessageFormat, error.getCode(), message);
            if (!isCorrelationIDPresent()) {
                errorMsg = String.format("correlationID: %s | " + errorMsg, error.getRef());
            }

            if (log.isDebugEnabled()) {
                log.debug(errorMsg);
            }
            return error;
        }
    }
}
