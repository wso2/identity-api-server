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

package org.wso2.carbon.identity.api.server.webhook.metadata.v1.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.constants.WebhookMetadataEndpointConstants;
import org.wso2.carbon.identity.webhook.metadata.api.exception.WebhookMetadataClientException;
import org.wso2.carbon.identity.webhook.metadata.api.exception.WebhookMetadataException;

import javax.ws.rs.core.Response;

/**
 * Class that handles exceptions and builds API error object.
 */
public class WebhookMetadataAPIErrorBuilder {

    private static final Log LOG = LogFactory.getLog(WebhookMetadataAPIErrorBuilder.class);

    public static APIError buildAPIError(WebhookMetadataException exception) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (exception instanceof WebhookMetadataClientException) {
            LOG.debug(exception.getMessage(), exception);
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error(exception.getMessage(), exception);
        }

        String errorCode = exception.getErrorCode();
        return buildAPIError(status, errorCode, exception.getMessage(), exception.getDescription());
    }

    public static APIError buildAPIError(Response.Status status, WebhookMetadataEndpointConstants.ErrorMessage error,
                                         String... data) {

        String description = error.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, (Object[]) data);
        }
        return buildAPIError(status, error.getCode(), error.getMessage(), description);
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
