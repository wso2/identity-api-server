/*
 * Copyright (c) 2023, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.common.error.bulk;

import org.apache.commons.logging.Log;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;

import java.util.List;

import static org.wso2.carbon.identity.api.server.common.Util.getCorrelation;
import static org.wso2.carbon.identity.api.server.common.Util.isCorrelationIDPresent;

/**
 * Common BulkErrorResponse Object for all the server bulk API related errors.
 */
public class BulkErrorResponse extends BulkErrorDTO {

    /**
     * BulkErrorResponse Builder.
     */
    public static class Builder {
        private String code;
        private String message;
        private String description;
        private List<? extends ErrorDTO> failedOperations;

        public Builder() {
            // The constructor is intentionally left empty as there are no initializations required.
        }

        public BulkErrorResponse.Builder withCode(String code) {

            this.code = code;
            return this;
        }

        public BulkErrorResponse.Builder withMessage(String message) {

            this.message = message;
            return this;
        }

        public BulkErrorResponse.Builder withDescription(String description) {

            this.description = description;
            return this;
        }

        public BulkErrorResponse.Builder withFailedOperations(List<? extends ErrorDTO> failedOperations) {

            this.failedOperations = failedOperations;
            return this;
        }

        public BulkErrorResponse build() {

            BulkErrorResponse error = new BulkErrorResponse();
            error.setCode(this.code);
            error.setMessage(this.message);
            error.setDescription(this.description);
            error.setRef(getCorrelation());
            error.setFailedOperations(this.failedOperations);
            return error;
        }

        public BulkErrorResponse build(Log log, Exception e, String message) {

            BulkErrorResponse error = build();
            String errorMessageFormat = "errorCode: %s | message: %s";
            String errorMsg = String.format(errorMessageFormat, error.getCode(), message);
            if (!isCorrelationIDPresent()) {
                errorMsg = String.format("correlationID: %s | %s", error.getRef(), errorMsg);
            }
            log.error(errorMsg, e);
            return error;
        }

        /**
         * Build error response builder for bad requests without exceptions.
         *
         * @param log Logger.
         * @param message Error message.
         * @return BulkErrorResponse object.
         */
        public BulkErrorResponse build(Log log, String message) {

            BulkErrorResponse error = build();
            String errorMessageFormat = "errorCode: %s | message: %s";
            String errorMsg = String.format(errorMessageFormat, error.getCode(), message);
            if (!isCorrelationIDPresent()) {
                errorMsg = String.format("correlationID: %s | %s", error.getRef(), errorMsg);
            }

            if (log.isDebugEnabled()) {
                log.debug(errorMsg);
            }
            return error;
        }
    }
}
