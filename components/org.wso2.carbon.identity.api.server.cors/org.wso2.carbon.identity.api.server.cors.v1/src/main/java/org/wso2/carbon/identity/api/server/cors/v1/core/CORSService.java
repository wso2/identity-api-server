/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.cors.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.cors.common.CORSServiceHolder;
import org.wso2.carbon.identity.api.server.cors.common.Constants;
import org.wso2.carbon.identity.api.server.cors.v1.function.CORSApplicationToCORSApplicationObject;
import org.wso2.carbon.identity.api.server.cors.v1.function.CORSOriginToCORSOriginObject;
import org.wso2.carbon.identity.api.server.cors.v1.model.CORSApplicationObject;
import org.wso2.carbon.identity.api.server.cors.v1.model.CORSOriginObject;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceClientException;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceException;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceServerException;
import org.wso2.carbon.identity.cors.mgt.core.model.CORSApplication;
import org.wso2.carbon.identity.cors.mgt.core.model.CORSOrigin;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.cors.common.Constants.ErrorMessage.ERROR_CODE_INVALID_CORS_ORIGIN_ID;

/**
 * Call internal OSGi services to perform server CORS management.
 */
public class CORSService {

    private static final Log log = LogFactory.getLog(CORSService.class);

    /**
     * Get a list of associated applications of a CORS origin.
     *
     * @return List of associated applications.
     */
    public List<CORSApplicationObject> getAssociatedAppsByCORSOrigin(String corsOriginId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            List<String> corsOriginIdList = CORSServiceHolder.getInstance().getCorsManagementService()
                    .getTenantCORSOrigins(tenantDomain).stream().map(CORSOrigin::getId).collect(Collectors.toList());

            // Throw an exception if corsOriginId is not valid.
            if (!corsOriginIdList.contains(corsOriginId)) {
                throw new CORSManagementServiceClientException(String.format(ERROR_CODE_INVALID_CORS_ORIGIN_ID
                        .description(), corsOriginId), ERROR_CODE_INVALID_CORS_ORIGIN_ID.code());
            }

            List<CORSApplication> applicationList = CORSServiceHolder.getInstance().getCorsManagementService()
                    .getCORSApplicationsByCORSOriginId(corsOriginId, tenantDomain);
            return applicationList.stream().map(new CORSApplicationToCORSApplicationObject())
                    .collect(Collectors.toList());
        } catch (CORSManagementServiceException e) {
            throw handleCORSException(e, Constants.ErrorMessage.ERROR_CODE_CORS_RETRIEVE, null);
        }
    }

    /**
     * Get a list of CORS origins allowed by the tenant.
     *
     * @return List of CORSOriginGetObject.
     */
    public List<CORSOriginObject> getCORSOrigins() {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            List<CORSOrigin> corsOriginList = CORSServiceHolder.getInstance().getCorsManagementService()
                    .getTenantCORSOrigins(tenantDomain);
            return corsOriginList.stream().map(new CORSOriginToCORSOriginObject()).collect(Collectors.toList());
        } catch (CORSManagementServiceException e) {
            throw handleCORSException(e, Constants.ErrorMessage.ERROR_CODE_CORS_RETRIEVE, null);
        }
    }

    private APIError handleCORSException(CORSManagementServiceException e,
                                         Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());

        Response.Status status;

        if (e instanceof CORSManagementServiceClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CORS_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof CORSManagementServiceServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CORS_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, Constants.ErrorMessage error, String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.code()).withMessage(errorMsg.message())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Constant.ErrorMessage.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(Constants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.description(), data);
        } else {
            message = error.description();
        }
        return message;
    }
}
