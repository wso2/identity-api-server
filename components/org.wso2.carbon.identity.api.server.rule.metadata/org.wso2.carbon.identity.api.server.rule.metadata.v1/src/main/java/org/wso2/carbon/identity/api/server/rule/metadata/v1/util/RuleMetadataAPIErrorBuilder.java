/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.rule.metadata.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.rule.metadata.api.exception.RuleMetadataClientException;
import org.wso2.carbon.identity.rule.metadata.api.exception.RuleMetadataException;

import javax.ws.rs.core.Response;

/**
 * Class that handles exceptions and builds API error object.
 */
public class RuleMetadataAPIErrorBuilder {

    private static final String ERROR_CODE_DELIMITER = "-";
    private static final String ERROR_CODE_PREFIX = "RULEMETA" + ERROR_CODE_DELIMITER;
    private static final Log LOG = LogFactory.getLog(RuleMetadataAPIErrorBuilder.class);

    private RuleMetadataAPIErrorBuilder() {

    }

    public static APIError buildAPIError(RuleMetadataException exception) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (exception instanceof RuleMetadataClientException) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Client error in rule metadata operation: " + exception.getMessage(), exception);
            }
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error("Server error in rule metadata operation: " + exception.getMessage(), exception);
        }

        String errorCode = exception.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                ERROR_CODE_PREFIX + errorCode;
        return buildAPIError(status, errorCode, exception.getMessage(), exception.getDescription());
    }

    private static APIError buildAPIError(Response.Status status, String errorCode,
                                          String message, String description) {

        return new APIError(status, getError(errorCode, message, description));
    }

    private static ErrorDTO getError(String errorCode, String errorMessage, String errorDescription) {

        ErrorDTO error = new ErrorDTO();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }
}
