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

package org.wso2.carbon.identity.api.server.application.tag.management.v1.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.constants.ApplicationTagMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.exception.ApplicationTagMgtEndpointException;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagModel;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.Error;
import org.wso2.carbon.identity.application.tag.mgt.ApplicationTagMgtClientException;
import org.wso2.carbon.identity.application.tag.mgt.ApplicationTagMgtException;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.application.tag.management.v1.constants.ApplicationTagMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_APPLICATION_TAG_COLOUR;
import static org.wso2.carbon.identity.api.server.application.tag.management.v1.constants.ApplicationTagMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_APPLICATION_TAG_NAME;
import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.application.tag.mgt.constant.ApplicationTagManagementConstants.ErrorMessages.ERROR_CODE_APP_TAG_ALREADY_EXISTS;

/**
 * Utility class for Application Tag management endpoint.
 */
public class ApplicationTagMgtEndpointUtil {

    private static final Log LOG = LogFactory.getLog(ApplicationTagMgtEndpointUtil.class);

    public static void validateApplicationTag(ApplicationTagModel applicationTagCreationModel) {

        if (StringUtils.isBlank(applicationTagCreationModel.getName())) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_APPLICATION_TAG_NAME);
        }
        if (StringUtils.isBlank(applicationTagCreationModel.getColour())) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_APPLICATION_TAG_COLOUR);
        }
    }

    public static ApplicationTagMgtEndpointException handleException(Response.Status status,
         ApplicationTagMgtEndpointConstants.ErrorMessage error) {

        return new ApplicationTagMgtEndpointException(status, getError(error.getCode(), error.getMessage(),
                error.getDescription()));
    }

    public static ApplicationTagMgtEndpointException handleException(Response.Status status,
         ApplicationTagMgtEndpointConstants.ErrorMessage error, String data) {

        return new ApplicationTagMgtEndpointException(status, getError(error.getCode(), error.getMessage(),
                String.format(error.getDescription(), data)));
    }

    public static ApplicationTagMgtEndpointException handleException(Response.Status status, String errorCode,
                                                                  String message, String description) {

        return new ApplicationTagMgtEndpointException(status, getError(errorCode, message, description));
    }

    public static ApplicationTagMgtEndpointException handleApplicationTagMgtException(ApplicationTagMgtException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof ApplicationTagMgtClientException) {
            LOG.debug(e.getMessage(), e);
            if (ERROR_CODE_APP_TAG_ALREADY_EXISTS.getCode().equals(e.getErrorCode())) {
                status = Response.Status.CONFLICT;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            LOG.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                ApplicationTagMgtEndpointConstants.APPLICATION_TAG_MANAGEMENT_PREFIX + errorCode;
        return handleException(status, errorCode, e.getMessage(), e.getDescription());
    }

    /**
     * Returns a generic error object.
     *
     * @param errorCode        Error code.
     * @param errorMessage     Error message.
     * @param errorDescription Error description.
     * @return A generic error with the specified details.
     */
    public static Error getError(String errorCode, String errorMessage, String errorDescription) {

        Error error = new Error();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }
}
