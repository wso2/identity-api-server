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

package org.wso2.carbon.identity.api.server.flow.execution.v1.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.flow.execution.common.FlowExecutionServiceHolder;
import org.wso2.carbon.identity.api.server.flow.execution.v1.Component;
import org.wso2.carbon.identity.api.server.flow.execution.v1.Data;
import org.wso2.carbon.identity.api.server.flow.execution.v1.FlowExecutionRequest;
import org.wso2.carbon.identity.api.server.flow.execution.v1.constants.FlowExecutionEndpointConstants;
import org.wso2.carbon.identity.api.server.flow.execution.v1.model.FlowExecutionErrorDTO;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.flow.execution.engine.exception.FlowEngineClientException;
import org.wso2.carbon.identity.flow.execution.engine.exception.FlowEngineException;
import org.wso2.carbon.identity.flow.execution.engine.exception.FlowEngineServerException;
import org.wso2.carbon.identity.flow.mgt.Constants;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtFrameworkException;
import org.wso2.carbon.identity.flow.mgt.model.ComponentDTO;
import org.wso2.carbon.identity.flow.mgt.model.DataDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowConfigDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.flow.execution.v1.constants.FlowExecutionEndpointConstants.ErrorMessage.ERROR_CODE_FLOW_DISABLED;
import static org.wso2.carbon.identity.api.server.flow.execution.v1.constants.FlowExecutionEndpointConstants.ErrorMessage.ERROR_CODE_GET_FLOW_CONFIG;
import static org.wso2.carbon.identity.api.server.flow.execution.v1.constants.FlowExecutionEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_FLOW_TYPE;

/**
 * Utility class for flow execution API.
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
                                           String message, String description, String flowType) {

        return new APIError(status, getError(errorCode, message, description, flowType));
    }

    /**
     * Handles RegistrationFrameworkException and returns an APIError object.
     *
     * @param e RegistrationFrameworkException.
     * @return APIError object.
     */
    public static APIError handleFlowException(FlowEngineException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof FlowEngineClientException) {
            LOG.debug(e.getMessage(), e);
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                FlowExecutionEndpointConstants.REGISTRATION_FLOW_PREFIX + errorCode;
        return handleException(status, errorCode, e.getMessage(), e.getDescription(), e.getFlowType());
    }

    /**
     * Handles RegistrationFrameworkException and returns an APIError object.
     *
     * @param e            RegistrationFrameworkException.
     * @param tenantDomain Tenant domain.
     * @return APIError object.
     */
    public static APIError handleFlowException(FlowEngineException e, String tenantDomain) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        String errorCode = e.getErrorCode();
        if (e instanceof FlowEngineClientException) {
            LOG.debug(e.getMessage(), e);
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error(e.getMessage(), e);
        }
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                FlowExecutionEndpointConstants.REGISTRATION_FLOW_PREFIX + errorCode;
        return handleException(status, errorCode, e.getMessage(), e.getDescription(), e.getFlowType());
    }

    /**
     * Returns a generic error object.
     *
     * @param errorCode        Error code.
     * @param errorMessage     Error message.
     * @param errorDescription Error description.
     * @return A generic error with the specified details.
     */
    public static FlowExecutionErrorDTO getError(String errorCode, String errorMessage, String errorDescription,
                                                 String flowType) {

        FlowExecutionErrorDTO error = new FlowExecutionErrorDTO();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        error.setFlowType(flowType);
        return error;
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
                if (dataDTO.getComponents() != null && !dataDTO.getComponents().isEmpty()) {
                    return data.components(dataDTO.getComponents().stream()
                            .map(Utils::convertToComponent)
                            .collect(Collectors.toList()));
                }
                return data.components(new ArrayList<>());
            case Constants.StepTypes.REDIRECTION:
                if (dataDTO.getComponents() != null && !dataDTO.getComponents().isEmpty()) {
                    data.components(dataDTO.getComponents().stream()
                            .map(Utils::convertToComponent)
                            .collect(Collectors.toList()));
                }
                return data.redirectURL(dataDTO.getRedirectURL());
            case Constants.StepTypes.WEBAUTHN:
                return data.webAuthnData(dataDTO.getWebAuthnData());
            default:
                return data;
        }
    }

    /**
     * Validates the flow initiation request.
     *
     * @param flowExecutionRequest FlowExecutionRequest object.
     */
    public static void validateFlowInitiation(FlowExecutionRequest flowExecutionRequest) {

        String flowType = flowExecutionRequest.getFlowType();
        if (StringUtils.isBlank(flowType) || Arrays.stream(Constants.FlowTypes.values())
                .noneMatch(type -> type.name().equals(flowType))) {
            throw Utils.handleFlowException(new FlowEngineClientException(
                    ERROR_CODE_INVALID_FLOW_TYPE.getCode(),
                    ERROR_CODE_INVALID_FLOW_TYPE.getMessage(),
                    ERROR_CODE_INVALID_FLOW_TYPE.getDescription()));
        }

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        if (!Utils.isOrchestrationEnabled(flowType, tenantDomain)) {
            throw Utils.handleFlowException(new FlowEngineClientException(
                    ERROR_CODE_FLOW_DISABLED.getCode(),
                    ERROR_CODE_FLOW_DISABLED.getMessage(),
                    String.format(ERROR_CODE_FLOW_DISABLED.getDescription(), flowType)));
        }

    }

    /**
     * Checks whether orchestration is enabled for the given flow type.
     *
     * @param flowType     Type of the flow.
     * @param tenantDomain Tenant domain.
     * @return true if orchestration is enabled, false otherwise.
     */
    public static boolean isOrchestrationEnabled(String flowType, String tenantDomain) {

        try {
            FlowConfigDTO flowConfig = FlowExecutionServiceHolder.getFlowMgtService().getFlowConfig(
                    flowType, IdentityTenantUtil.getTenantId(tenantDomain));
            return flowConfig.getIsEnabled();
        } catch (FlowMgtFrameworkException e) {
            throw handleFlowException(new FlowEngineServerException(
                    ERROR_CODE_GET_FLOW_CONFIG.getCode(),
                    ERROR_CODE_GET_FLOW_CONFIG.getMessage(),
                    ERROR_CODE_GET_FLOW_CONFIG.getDescription(), e));
        }
    }

    private static Component convertToComponent(ComponentDTO componentDTO) {

        if (componentDTO == null) {
            return null;
        }

        Component component = new Component()
                .id(componentDTO.getId())
                .type(componentDTO.getType())
                .variant(componentDTO.getVariant())
                .config(convertToMap(componentDTO.getConfigs()))
                .components(componentDTO.getComponents() != null ? componentDTO.getComponents().stream()
                        .map(Utils::convertToComponent)
                        .collect(Collectors.toList()) : null);
        if (Constants.ComponentTypes.BUTTON.equals(componentDTO.getType())) {
            component.actionId(componentDTO.getId());
        }
        return component;
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
