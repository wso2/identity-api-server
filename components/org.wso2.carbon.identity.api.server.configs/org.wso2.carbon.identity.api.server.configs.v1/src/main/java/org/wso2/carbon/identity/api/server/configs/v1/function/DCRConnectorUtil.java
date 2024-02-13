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
import org.wso2.carbon.identity.api.server.configs.common.factory.DCRMgtOGSiServiceFactory;
import org.wso2.carbon.identity.api.server.configs.v1.exception.DCRConfigClientException;
import org.wso2.carbon.identity.api.server.configs.v1.exception.DCRConfigException;
import org.wso2.carbon.identity.api.server.configs.v1.exception.DCRConfigServerException;
import org.wso2.carbon.identity.api.server.configs.v1.model.DCRConfig;
import org.wso2.carbon.identity.oauth.dcr.exception.DCRMException;
import org.wso2.carbon.identity.oauth.dcr.model.DCRConfiguration;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.configs.common.Constants.ErrorMessage.ERROR_DCR_CONFIG_SERVICE_NOT_FOUND;


/**
 * Util class for DCR connector
 */
public class DCRConnectorUtil {

    private static final Log log = LogFactory.getLog(DCRConnectorUtil.class);

    /**
     * Get the DCRConfig from the DCRConfiguration for said tenant domain.
     *
     * @param tenantDomain Tenant domain.
     * @return DCRConfig.
     * @throws DCRMException DCRMException.
     */
    public static DCRConfig getDCRConfig(String tenantDomain) throws DCRMException, DCRConfigException {

        if (DCRMgtOGSiServiceFactory.getInstance() != null) {
            DCRConfiguration dcrConfiguration = DCRMgtOGSiServiceFactory.getInstance()
                    .getDCRConfiguration(tenantDomain);
            return dcrConfigurationToDCRConfig(dcrConfiguration);
        } else {
            throw new DCRConfigException(ERROR_DCR_CONFIG_SERVICE_NOT_FOUND.message(),
                    ERROR_DCR_CONFIG_SERVICE_NOT_FOUND.code());
        }

    }

    public static void setDCRConfig(DCRConfig dcrConfig, String tenantDomain) throws
            DCRConfigException, DCRMException {

        if (DCRMgtOGSiServiceFactory.getInstance() != null) {
            DCRMgtOGSiServiceFactory.getInstance()
                    .setDCRConfiguration((getDCRConfigurationFromDCRConfig(dcrConfig)), tenantDomain);
        } else {
            throw new DCRConfigException(ERROR_DCR_CONFIG_SERVICE_NOT_FOUND.message(),
                    ERROR_DCR_CONFIG_SERVICE_NOT_FOUND.code());
        }
    }

    public static DCRConfig dcrConfigurationToDCRConfig(DCRConfiguration dcrConfiguration) {

        DCRConfig dcrConfig = new DCRConfig();
        dcrConfig.setClientAuthenticationRequired(dcrConfiguration.isClientAuthenticationRequired());
        dcrConfig.setEnableFapiEnforcement(dcrConfiguration.isFAPIEnforced());
        dcrConfig.setSsaJwks(dcrConfiguration.getSsaJwks());
        dcrConfig.setMandateSSA(dcrConfiguration.getMandateSSA());

        return dcrConfig;
    }

    public static APIError handleDCRConfigException(Exception e, Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof DCRConfigClientException) {
            DCRConfigClientException exception = (DCRConfigClientException) e;
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
        } else if (e instanceof DCRConfigServerException) {
            DCRConfigServerException exception = (DCRConfigServerException) e;
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

    /**
     * Get the DCRConfiguration from the DCRConfig.
     *
     * @param dcrConfig DCRConfig.
     * @return  DCRConfiguration.
     */
    public static DCRConfiguration getDCRConfigurationFromDCRConfig (DCRConfig dcrConfig) {

        DCRConfiguration dcrConfiguration = new DCRConfiguration();
        dcrConfiguration.setFAPIEnforced(dcrConfig.getEnableFapiEnforcement());
        dcrConfiguration.setSsaJwks(dcrConfig.getSsaJwks());
        dcrConfiguration.setClientAuthenticationRequired(dcrConfig.getClientAuthenticationRequired());
        return dcrConfiguration;
    }
}
