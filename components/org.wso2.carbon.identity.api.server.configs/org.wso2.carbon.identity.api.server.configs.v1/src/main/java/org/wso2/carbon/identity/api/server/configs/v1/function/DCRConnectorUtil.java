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

package org.wso2.carbon.identity.api.server.configs.v1.function;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.configs.common.Constants;
import org.wso2.carbon.identity.api.server.configs.v1.model.DCRConfig;
import org.wso2.carbon.identity.oauth.dcr.DCRConfigurationMgtService;
import org.wso2.carbon.identity.oauth.dcr.exception.DCRMClientException;
import org.wso2.carbon.identity.oauth.dcr.exception.DCRMException;
import org.wso2.carbon.identity.oauth.dcr.exception.DCRMServerException;
import org.wso2.carbon.identity.oauth.dcr.model.DCRConfiguration;

import javax.ws.rs.core.Response;

/**
 * Util class for DCR connector.
 */
public class DCRConnectorUtil {

    private static final Log LOG = LogFactory.getLog(DCRConnectorUtil.class);

    /**
     * Get the DCRConfig from the DCRConfiguration for said tenant domain.
     *
     * @return DCRConfig.
     * @throws DCRMException DCRMException.
     */
    public static DCRConfig getDCRConfig(DCRConfigurationMgtService dcrConfigurationMgtService) throws DCRMException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving DCR configuration.");
        }
        DCRConfiguration dcrConfiguration = dcrConfigurationMgtService.getDCRConfiguration();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved DCR configuration.");
        }
        return dcrConfigurationToDCRConfig(dcrConfiguration);

    }

    /**
     * Set the provided DCR Configurations for the tenant.
     * @param dcrConfig DCRConfig instance.
     * @throws DCRMException DCRMException.
     */
    public static void setDCRConfig(DCRConfig dcrConfig, DCRConfigurationMgtService dcrConfigurationMgtService)
            throws DCRMException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting DCR configuration.");
        }
        dcrConfigurationMgtService.setDCRConfiguration((getDCRConfigurationFromDCRConfig(dcrConfig)));
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully set DCR configuration.");
        }
    }

    private static DCRConfig dcrConfigurationToDCRConfig(DCRConfiguration dcrConfiguration) {

        DCRConfig dcrConfig = new DCRConfig();
        dcrConfig.setAuthenticationRequired(dcrConfiguration.getAuthenticationRequired());
        dcrConfig.setEnableFapiEnforcement(dcrConfiguration.getEnableFapiEnforcement());
        dcrConfig.setSsaJwks(dcrConfiguration.getSsaJwks());
        dcrConfig.setMandateSSA(dcrConfiguration.getMandateSSA());

        return dcrConfig;
    }

    /**
     * Handle DCR Config Exception and return corresponding APIError.
     * @param e Exception.
     * @param errorEnum Error Message enum.
     * @param data Extra data.
     * @return APIError.
     */
    public static APIError handleDCRConfigException(Exception e, Constants.ErrorMessage errorEnum, String data) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Handling DCR configuration exception: " + e.getClass().getSimpleName());
        }
        
        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof DCRMClientException) {
            DCRMClientException exception = (DCRMClientException) e;
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, exception.getMessage());
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
        } else if (e instanceof DCRMServerException) {
            DCRMServerException exception = (DCRMServerException) e;
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, exception, errorEnum.description());
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
            errorResponse = getErrorBuilder(errorEnum, data).build(LOG, e, errorEnum.description());
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

    /**
     * Get the DCRConfiguration from the DCRConfig.
     *
     * @param dcrConfig DCRConfig.
     * @return  DCRConfiguration.
     */
    private static DCRConfiguration getDCRConfigurationFromDCRConfig (DCRConfig dcrConfig) {

        DCRConfiguration dcrConfiguration = new DCRConfiguration();
        dcrConfiguration.setEnableFapiEnforcement(dcrConfig.getEnableFapiEnforcement());
        dcrConfiguration.setSsaJwks(dcrConfig.getSsaJwks());
        dcrConfiguration.setAuthenticationRequired(dcrConfig.getAuthenticationRequired());
        dcrConfiguration.setMandateSSA(dcrConfig.getMandateSSA());
        return dcrConfiguration;
    }
}
