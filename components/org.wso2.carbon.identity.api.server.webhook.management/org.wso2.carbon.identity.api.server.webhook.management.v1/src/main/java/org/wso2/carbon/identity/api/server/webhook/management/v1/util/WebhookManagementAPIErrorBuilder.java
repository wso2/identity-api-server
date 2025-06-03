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

package org.wso2.carbon.identity.api.server.webhook.management.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.webhook.management.api.exception.WebhookMgtClientException;
import org.wso2.carbon.identity.webhook.management.api.exception.WebhookMgtException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.webhook.management.api.constant.ErrorMessage.ERROR_CODE_WEBHOOK_NOT_FOUND;

/**
 * Class that handles exceptions and builds API error object.
 */
public class WebhookManagementAPIErrorBuilder {

    private static final Log LOG = LogFactory.getLog(WebhookManagementAPIErrorBuilder.class);
    private static final Set<String> NOT_FOUND_ERRORS = Collections.unmodifiableSet(new HashSet<>(
            Collections.singletonList(
                    ERROR_CODE_WEBHOOK_NOT_FOUND.getCode()
            )));

    private WebhookManagementAPIErrorBuilder() {

    }

    public static APIError buildAPIError(WebhookMgtException exception) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (exception instanceof WebhookMgtClientException) {
            LOG.debug(exception.getMessage(), exception);
            if (NOT_FOUND_ERRORS.contains(exception.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            LOG.error(exception.getMessage(), exception);
        }

        String errorCode = exception.getErrorCode();
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
