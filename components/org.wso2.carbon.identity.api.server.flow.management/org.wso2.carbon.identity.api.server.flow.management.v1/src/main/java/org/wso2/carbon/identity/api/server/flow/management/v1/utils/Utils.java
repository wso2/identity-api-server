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

package org.wso2.carbon.identity.api.server.flow.management.v1.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.api.server.flow.management.common.FlowMgtServiceHolder;
import org.wso2.carbon.identity.api.server.flow.management.v1.Action;
import org.wso2.carbon.identity.api.server.flow.management.v1.Component;
import org.wso2.carbon.identity.api.server.flow.management.v1.Data;
import org.wso2.carbon.identity.api.server.flow.management.v1.Executor;
import org.wso2.carbon.identity.api.server.flow.management.v1.Position;
import org.wso2.carbon.identity.api.server.flow.management.v1.Size;
import org.wso2.carbon.identity.api.server.flow.management.v1.Step;
import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.AbstractMetaResponseHandler;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtClientException;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtFrameworkException;
import org.wso2.carbon.identity.flow.mgt.model.ActionDTO;
import org.wso2.carbon.identity.flow.mgt.model.ComponentDTO;
import org.wso2.carbon.identity.flow.mgt.model.DataDTO;
import org.wso2.carbon.identity.flow.mgt.model.ExecutorDTO;
import org.wso2.carbon.identity.flow.mgt.model.StepDTO;
import org.wso2.carbon.identity.governance.IdentityGovernanceException;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_DUPLICATE_COMPONENT_ID;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_GET_GOVERNANCE_CONFIG;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_GET_LOCAL_AUTHENTICATORS;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.Schema.IDP_NAME;

/**
 * Utility class for flow management.
 */
public class Utils {

    private static final Log LOG = LogFactory.getLog(Utils.class);

    /**
     * Handles exceptions and returns an APIError object.
     *
     * @param status      Response status.
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
     * Handles exceptions and returns an APIError object.
     *
     * @param e FlowMgtFrameworkException object.
     * @return APIError object.
     */
    public static APIError handleFlowMgtException(FlowMgtFrameworkException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof FlowMgtClientException) {
            LOG.debug(e.getMessage(), e);
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                FlowEndpointConstants.FLOW_PREFIX + errorCode;
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
     * Converts a StepDTO object to a Step object.
     *
     * @param stepDTO StepDTO object.
     * @return Step object.
     */
    public static Step convertToStep(StepDTO stepDTO) {

        return new Step()
                .id(stepDTO.getId())
                .type(stepDTO.getType())
                .size(new Size()
                        .height(BigDecimal.valueOf(stepDTO.getHeight()))
                        .width(BigDecimal.valueOf(stepDTO.getWidth()))
                )
                .position(new Position()
                        .x(BigDecimal.valueOf(stepDTO.getCoordinateX()))
                        .y(BigDecimal.valueOf(stepDTO.getCoordinateY()))
                ).data(convertToData(stepDTO.getData())
                );
    }

    private static Data convertToData(DataDTO dataDTO) {

        return new Data()
                .components(CollectionUtils.isEmpty(dataDTO.getComponents()) ? null :
                        dataDTO.getComponents().stream()
                                .map(Utils::convertToComponent)
                                .collect(Collectors.toList()))
                .action(convertToAction(dataDTO.getAction()));
    }

    private static Component convertToComponent(ComponentDTO componentDTO) {

        if (componentDTO == null) {
            return null;
        }

        return new Component()
                .id(componentDTO.getId())
                .category(componentDTO.getCategory())
                .type(componentDTO.getType())
                .variant(componentDTO.getVariant())
                .config(convertToMap(componentDTO.getConfigs()))
                .action(convertToAction(componentDTO.getAction()))
                .components(componentDTO.getComponents() != null ? componentDTO.getComponents().stream()
                        .map(Utils::convertToComponent)
                        .collect(Collectors.toList()) : null);
    }

    private static Action convertToAction(ActionDTO actionDTO) {

        if (actionDTO == null) {
            return null;
        }
        return new Action()
                .type(actionDTO.getType())
                .next(actionDTO.getNextId())
                .executor(convertToExecutor(actionDTO.getExecutor()));
    }

    private static Executor convertToExecutor(ExecutorDTO executorDTO) {

        if (executorDTO == null) {
            return null;
        }

        Executor executor = new Executor()
                .name(executorDTO.getName());

        if (executorDTO.getIdpName() != null) {
            Map<String, String> meta = new java.util.HashMap<>();
            meta.put(IDP_NAME, executorDTO.getIdpName());
            executor.meta(meta);
        }
        return executor;
    }

    /**
     * Converts a Step object to a StepDTO object.
     *
     * @param step Step object.
     * @return StepDTO object.
     */
    public static StepDTO convertToStepDTO(Step step) {

        return new StepDTO.Builder()
                .id(step.getId())
                .type(step.getType())
                .coordinateX(step.getPosition().getX().doubleValue())
                .coordinateY(step.getPosition().getY().doubleValue())
                .width(step.getSize().getWidth().doubleValue())
                .height(step.getSize().getHeight().doubleValue())
                .data(convertToDataDTO(step.getData()))
                .build();
    }

    private static DataDTO convertToDataDTO(Data data) {

        DataDTO.Builder dataDTOBuilder = new DataDTO.Builder();
        if (data.getComponents() != null && !data.getComponents().isEmpty()) {
            dataDTOBuilder.components(data.getComponents().stream()
                    .map(Utils::convertToComponentDTO)
                    .collect(Collectors.toList()));
        }
        if (data.getAction() != null) {
            dataDTOBuilder.action(convertToActionDTO(data.getAction()));
        }
        return dataDTOBuilder.build();
    }

    private static ComponentDTO convertToComponentDTO(Component component) {

        if (component == null) {
            return null;
        }

        return new ComponentDTO.Builder()
                .id(component.getId())
                .category(component.getCategory())
                .type(component.getType())
                .variant(component.getVariant())
                .configs(convertToMap(component.getConfig()))
                .action(convertToActionDTO(component.getAction()))
                .components(component.getComponents() != null ? component.getComponents().stream()
                        .map(Utils::convertToComponentDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    private static ActionDTO convertToActionDTO(Action action) {

        if (action == null) {
            return null;
        }

        return new ActionDTO.Builder()
                .type(action.getType())
                .nextId(action.getNext())
                .executor(convertToExecutorDTO(action.getExecutor()))
                .build();
    }

    private static ExecutorDTO convertToExecutorDTO(Executor executor) {

        if (executor == null) {
            return null;
        }

        ExecutorDTO executorDTO = new ExecutorDTO.Builder().name(executor.getName()).build();
        Map<String, Object> meta = convertToMap(executor.getMeta());
        if (meta != null && !meta.isEmpty() && meta.containsKey(IDP_NAME)) {
            executorDTO.setIdpName(String.valueOf(meta.get(IDP_NAME)));
        }
        return executorDTO;
    }

    private static Map<String, Object> convertToMap(Object map) {

        if (map == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * Checks whether a connector config is enabled.
     *
     * @param tenantDomain Tenant domain.
     */
    public boolean isFlowConfigEnabled(String tenantDomain, String connectorConfig) {

        try {
            IdentityGovernanceService identityGovernanceService =
                    FlowMgtServiceHolder.getIdentityGovernanceService();
            Property[] connectorConfigs = identityGovernanceService.getConfiguration(
                    new String[]{connectorConfig}, tenantDomain);

            if (connectorConfigs == null || connectorConfigs.length == 0) {
                return false;
            }

            return Boolean.parseBoolean(connectorConfigs[0].getValue());

        } catch (IdentityGovernanceException e) {
            throw handleFlowMgtException(new FlowMgtFrameworkException(
                    ERROR_CODE_GET_GOVERNANCE_CONFIG.getCode(),
                    ERROR_CODE_GET_GOVERNANCE_CONFIG.getMessage(),
                    ERROR_CODE_GET_GOVERNANCE_CONFIG.getDescription(), e));
        }
    }

    public LocalAuthenticatorConfig[] getConnections() {

        try {

            ApplicationManagementService applicationManagementServiceHolder =
                    FlowMgtServiceHolder.getApplicationManagementService();
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            return applicationManagementServiceHolder.getAllLocalAuthenticators(tenantDomain);

        } catch (IdentityApplicationManagementException e) {
            throw handleFlowMgtException(new FlowMgtFrameworkException(
                    ERROR_CODE_GET_LOCAL_AUTHENTICATORS.getCode(),
                    ERROR_CODE_GET_LOCAL_AUTHENTICATORS.getMessage(),
                    ERROR_CODE_GET_LOCAL_AUTHENTICATORS.getDescription(), e));
        }
    }

    public static void collectFlowData(
            List<Step> steps,
            Set<String> executors,
            Set<String> identifiers,
            Set<String> ids
    ) {
        for (Step step : steps) {
            if (step.getId() != null && !step.getId().isEmpty()) {
                ids.add(step.getId());
            }
            if (step.getData() != null && step.getData().getComponents() != null) {
                traverseComponents(step.getData().getComponents(), executors, identifiers, ids);
            }
        }
    }

    private static void traverseComponents(
            List<Component> components,
            Set<String> executors,
            Set<String> identifiers,
            Set<String> ids
    ) {
        for (Component component : components) {
            if (ids != null && component.getId() != null && !component.getId().isEmpty()) {
                if (!ids.add(component.getId())) {
                    throw handleFlowMgtException(new FlowMgtFrameworkException(
                            ERROR_CODE_DUPLICATE_COMPONENT_ID.getCode(),
                            ERROR_CODE_DUPLICATE_COMPONENT_ID.getMessage(),
                            ERROR_CODE_DUPLICATE_COMPONENT_ID.getDescription()));
                }
            }

            if (executors != null && component.getAction() != null
                    && "EXECUTOR".equals(component.getAction().getType())) {
                Executor executor = component.getAction().getExecutor();
                if (executor != null && executor.getName() != null) {
                    executors.add(executor.getName());
                }
            }

            if (identifiers != null && "FIELD".equals(component.getCategory())
                    && "INPUT".equals(component.getType())) {
                if (component.getConfig() instanceof Map) {
                    Map<String, Object> configMap = (Map<String, Object>) component.getConfig();
                    Object identifierObj = configMap.get("identifier");
                    if (identifierObj != null) {
                        String identifier = identifierObj.toString();
                        if (!identifier.isEmpty()) {
                            identifiers.add(identifier);
                        }
                    }
                }
            }

            if (component.getComponents() != null && !component.getComponents().isEmpty()) {
                traverseComponents(component.getComponents(), executors, identifiers, ids);
            }
        }
    }

    public static void validateIdentifiers(AbstractMetaResponseHandler metaResponseHandler, Set<String> identifiers) {

        String flowType = metaResponseHandler.getFlowType();
        Set<String> required = (Set<String>) metaResponseHandler.getRequiredInputFields(flowType);

        if (!identifiers.containsAll(required)) {
            throw handleFlowMgtException(new FlowMgtFrameworkException(
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_MISSING_IDENTIFIER.getCode(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_MISSING_IDENTIFIER.getMessage(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_MISSING_IDENTIFIER.getDescription()));
        }
    }

    public static void validateExecutors(AbstractMetaResponseHandler metaResponseHandler, Set<String> executors) {

        if (!new HashSet<>(metaResponseHandler.getSupportedExecutors()).containsAll(executors)) {
            throw handleFlowMgtException(new FlowMgtFrameworkException(
                    ERROR_CODE_UNSUPPORTED_EXECUTOR.getCode(),
                    ERROR_CODE_UNSUPPORTED_EXECUTOR.getMessage(),
                    ERROR_CODE_UNSUPPORTED_EXECUTOR.getDescription()));
        }
    }
}
