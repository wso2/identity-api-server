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

package org.wso2.carbon.identity.api.server.configs.v1.function;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.configs.common.Constants;
import org.wso2.carbon.identity.api.server.configs.v1.model.JWTValidatorConfig;
import org.wso2.carbon.identity.oauth2.token.handler.clientauth.jwt.core.JWTClientAuthenticatorMgtService;
import org.wso2.carbon.identity.oauth2.token.handler.clientauth.jwt.core.exception.JWTClientAuthenticatorServiceClientException;
import org.wso2.carbon.identity.oauth2.token.handler.clientauth.jwt.core.exception.JWTClientAuthenticatorServiceServerException;
import org.wso2.carbon.identity.oauth2.token.handler.clientauth.jwt.core.model.JWTClientAuthenticatorConfig;

import javax.ws.rs.core.Response;

/**
 * Util class for JWT Connector.
 * These methods are used to convert JWTValidatorConfig to JWTClientAuthenticatorConfig and vice versa.
 * These methods are used to handle exceptions.
 * These objects are from JWTClientAuthenticatorService connector.
 */
public class JWTConnectorUtil {

    private static final Log log = LogFactory.getLog(JWTConnectorUtil.class);


    /**
     * Get the JWTValidatorConfig from the JWTClientAuthenticatorConfig for said tenant domain.
     *
     * @param tenantDomain Tenant domain.
     * @return JWTValidatorConfig.
     * @throws Exception Exception.
     */
    public static JWTValidatorConfig getJWTValidatorConfig(String tenantDomain,
                                     JWTClientAuthenticatorMgtService jwtClientAuthenticatorMgtService)
            throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving JWT validator configuration for tenant: " + tenantDomain);
        }
        JWTValidatorConfig config = new JWTValidatorConfig().enableTokenReuse(jwtClientAuthenticatorMgtService.
                getPrivateKeyJWTClientAuthenticatorConfiguration(tenantDomain).isEnableTokenReuse());
        if (log.isDebugEnabled()) {
            log.debug("Successfully retrieved JWT validator configuration for tenant: " + tenantDomain);
        }
        return config;
    }

    /**
     * Get the JWTClientAuthenticatorConfig from the JWTValidatorConfig.
     *
     * @param jwtValidatorConfig JWTValidatorConfig.
     * @return  JWTClientAuthenticatorConfig.
     */
    public static JWTClientAuthenticatorConfig getJWTDaoConfig(JWTValidatorConfig jwtValidatorConfig) {

        JWTClientAuthenticatorConfig jwtClientAuthenticatorConfig = new JWTClientAuthenticatorConfig();
        jwtClientAuthenticatorConfig.setEnableTokenReuse(jwtValidatorConfig.getEnableTokenReuse());
        return jwtClientAuthenticatorConfig;
    }

    /**
     * Handle exceptions for JWT Rest API.
     *
     * @param e Exception.
     * @param errorEnum Error Message information.
     * @param data Context data.
     * @return APIError.
     */
    public static APIError handlePrivateKeyJWTValidationException(Exception e,
                                                                  Constants.ErrorMessage errorEnum, String data) {

        if (log.isDebugEnabled()) {
            log.debug("Handling JWT client authenticator exception: " + e.getClass().getSimpleName());
        }
        
        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof JWTClientAuthenticatorServiceClientException) {
            JWTClientAuthenticatorServiceClientException exception = (JWTClientAuthenticatorServiceClientException) e;
            errorResponse = getErrorBuilder(errorEnum, data).build(log, exception.getMessage());
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode =
                        errorCode.contains
                                (org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(exception.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof JWTClientAuthenticatorServiceServerException) {
            JWTClientAuthenticatorServiceServerException exception = (JWTClientAuthenticatorServiceServerException) e;
            errorResponse = getErrorBuilder(errorEnum, data).build(log, exception, errorEnum.description());
            if (exception.getErrorCode() != null) {
                String errorCode = exception.getErrorCode();
                errorCode =
                        errorCode.contains
                                (org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(exception.getMessage());
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
    private static ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

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
