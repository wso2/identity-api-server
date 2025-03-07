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

package org.wso2.carbon.identity.api.server.registration.execution.v1.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.api.server.registration.execution.common.RegistrationExecutionServiceHolder;
import org.wso2.carbon.identity.api.server.registration.execution.v1.Component;
import org.wso2.carbon.identity.api.server.registration.execution.v1.Data;
import org.wso2.carbon.identity.api.server.registration.execution.v1.constants.RegistrationExecutionEndpointConstants;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.governance.IdentityGovernanceException;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.user.registration.engine.exception.RegistrationEngineClientException;
import org.wso2.carbon.identity.user.registration.engine.exception.RegistrationEngineException;
import org.wso2.carbon.identity.user.registration.engine.exception.RegistrationEngineServerException;
import org.wso2.carbon.identity.user.registration.mgt.Constants;
import org.wso2.carbon.identity.user.registration.mgt.model.ComponentDTO;
import org.wso2.carbon.identity.user.registration.mgt.model.DataDTO;

import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.registration.execution.v1.constants.RegistrationExecutionEndpointConstants.DYNAMIC_REGISTRATION_PORTAL_ENABLED;
import static org.wso2.carbon.identity.api.server.registration.execution.v1.constants.RegistrationExecutionEndpointConstants.ErrorMessage.ERROR_CODE_DYNAMIC_REGISTRATION_PORTAL_DISABLED;
import static org.wso2.carbon.identity.api.server.registration.execution.v1.constants.RegistrationExecutionEndpointConstants.ErrorMessage.ERROR_CODE_GET_GOVERNANCE_CONFIG;

/**
 * Utility class for registration execution API.
 */
public class Utils {

    private static final Log LOG = LogFactory.getLog(Utils.class);

    private Utils() {

    }

    /**
     * Handles exceptions and returns an APIError object.
     *
     * @param status      HTTP status.
     * @param errorCode   Error code.
     * @param message     Error message.
     * @param description Error description.
     * @return APIError object.
     */
    public static APIError handleException(Response.Status status, String errorCode,
                                           String message, String description) {

        return new APIError(status, getError(errorCode, message, description));
    }

    /**
     * Handles RegistrationFrameworkException and returns an APIError object.
     *
     * @param e RegistrationFrameworkException.
     * @return APIError object.
     */
    public static APIError handleRegistrationException(RegistrationEngineException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof RegistrationEngineClientException) {
            LOG.debug(e.getMessage(), e);
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                RegistrationExecutionEndpointConstants.REGISTRATION_FLOW_PREFIX + errorCode;
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
    public static ErrorDTO getError(String errorCode, String errorMessage, String errorDescription) {

        ErrorDTO error = new ErrorDTO();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }

    /**
     * Checks whether the dynamic registration portal is enabled.
     *
     * @param tenantDomain Tenant domain.
     */
    public static void isDynamicRegistrationPortalEnabled(String tenantDomain) {

        try {
            IdentityGovernanceService identityGovernanceService =
                    RegistrationExecutionServiceHolder.getIdentityGovernanceService();
            Property[] connectorConfigs = identityGovernanceService.getConfiguration(
                    new String[]{DYNAMIC_REGISTRATION_PORTAL_ENABLED}, tenantDomain);
            if (!Boolean.parseBoolean(connectorConfigs[0].getValue())) {
                throw handleRegistrationException(new RegistrationEngineClientException(
                        ERROR_CODE_DYNAMIC_REGISTRATION_PORTAL_DISABLED.getCode(),
                        ERROR_CODE_DYNAMIC_REGISTRATION_PORTAL_DISABLED.getMessage(),
                        ERROR_CODE_DYNAMIC_REGISTRATION_PORTAL_DISABLED.getDescription()));
            }
        } catch (IdentityGovernanceException e) {
            throw handleRegistrationException(new RegistrationEngineServerException(
                    ERROR_CODE_GET_GOVERNANCE_CONFIG.getCode(),
                    ERROR_CODE_GET_GOVERNANCE_CONFIG.getMessage(),
                    ERROR_CODE_GET_GOVERNANCE_CONFIG.getDescription(), e));
        }
    }

    /**
     * Converts DataDTO to Data.
     *
     * @param dataDTO DataDTO object.
     * @param type    Type of the step.
     * @return Data object.
     */
    public static Data convertToData(DataDTO dataDTO, String type) {

        if (dataDTO == null) {
            return null;
        }

        Data data = new Data();

        if (dataDTO.getAdditionalData() != null) {
            dataDTO.getAdditionalData().forEach(data::putAdditionalDataItem);
        }

        if (dataDTO.getRequiredParams() != null) {
            dataDTO.getRequiredParams().forEach(data::addRequiredParamsItem);
        }

        switch (type) {
            case Constants.StepTypes.VIEW:
                return data.components(dataDTO.getComponents().stream()
                        .map(Utils::convertToComponent)
                        .collect(Collectors.toList()));
            case Constants.StepTypes.REDIRECTION:
                return data.redirectURL(dataDTO.getUrl());
            default:
                return data;
        }
    }

    private static Component convertToComponent(ComponentDTO componentDTO) {

        if (componentDTO == null) {
            return null;
        }

        return new Component()
                .id(componentDTO.getId())
                .type(componentDTO.getType())
                .variant(componentDTO.getVariant())
                .config(convertToMap(componentDTO.getConfigs()))
                .components(componentDTO.getComponents() != null ? componentDTO.getComponents().stream()
                        .map(Utils::convertToComponent)
                        .collect(Collectors.toList()) : null);
    }

    private static Map<String, Object> convertToMap(Object map) {

        if (map == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, new TypeReference<Map<String, Object>>() {
        });
    }
}
