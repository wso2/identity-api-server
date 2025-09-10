/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.cors.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.cors.common.Constants;
import org.wso2.carbon.identity.api.server.cors.v1.function.CORSApplicationToCORSApplicationObject;
import org.wso2.carbon.identity.api.server.cors.v1.function.CORSOriginToCORSOriginObject;
import org.wso2.carbon.identity.api.server.cors.v1.model.CORSApplicationObject;
import org.wso2.carbon.identity.api.server.cors.v1.model.CORSOriginObject;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;
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

    private final CORSManagementService corsManagementService;
    private static final Log log = LogFactory.getLog(CORSService.class);

    public CORSService(CORSManagementService corsManagementService) {

        this.corsManagementService = corsManagementService;
    }

    /**
     * Get a list of associated applications of a CORS origin.
     *
     * @return List of associated applications.
     */
    public List<CORSApplicationObject> getAssociatedAppsByCORSOrigin(String corsOriginId) {

        try {
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            if (log.isDebugEnabled()) {
                log.debug("Retrieving associated applications for CORS origin: " + corsOriginId + 
                          " in tenant: " + tenantDomain);
            }
            List<String> corsOriginIdList = corsManagementService.getTenantCORSOrigins(tenantDomain)
                    .stream().map(CORSOrigin::getId).collect(Collectors.toList());

            // Throw an exception if corsOriginId is not valid.
            if (!corsOriginIdList.contains(corsOriginId)) {
                if (log.isDebugEnabled()) {
                    log.debug("Invalid CORS origin ID provided: " + corsOriginId);
                }
                throw new CORSManagementServiceClientException(String.format(ERROR_CODE_INVALID_CORS_ORIGIN_ID
                        .description(), corsOriginId), ERROR_CODE_INVALID_CORS_ORIGIN_ID.code());
            }

            List<CORSApplication> applicationList = corsManagementService
                    .getCORSApplicationsByCORSOriginId(corsOriginId, tenantDomain);
            if (log.isDebugEnabled()) {
                log.debug("Found " + applicationList.size() + " applications associated with CORS origin: " + 
                          corsOriginId);
            }
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
            if (log.isDebugEnabled()) {
                log.debug("Retrieving CORS origins for tenant: " + tenantDomain);
            }
            List<CORSOrigin> corsOriginList = corsManagementService.getTenantCORSOrigins(tenantDomain);
            if (log.isDebugEnabled()) {
                log.debug("Found " + corsOriginList.size() + " CORS origins for tenant: " + tenantDomain);
            }
            return corsOriginList.stream().map(new CORSOriginToCORSOriginObject()).collect(Collectors.toList());
        } catch (CORSManagementServiceException e) {
            throw handleCORSException(e, Constants.ErrorMessage.ERROR_CODE_CORS_RETRIEVE, null);
        }
    }

    private APIError handleCORSException(CORSManagementServiceException e,
                                         Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof CORSManagementServiceClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
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
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
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
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
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
