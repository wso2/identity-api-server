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
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfigPatchModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.Position;
import org.wso2.carbon.identity.api.server.flow.management.v1.Size;
import org.wso2.carbon.identity.api.server.flow.management.v1.Step;
import org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.AbstractMetaResponseHandler;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.core.util.LambdaExceptionUtils;
import org.wso2.carbon.identity.flow.mgt.Constants;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtClientException;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtFrameworkException;
import org.wso2.carbon.identity.flow.mgt.model.ActionDTO;
import org.wso2.carbon.identity.flow.mgt.model.ComponentDTO;
import org.wso2.carbon.identity.flow.mgt.model.DataDTO;
import org.wso2.carbon.identity.flow.mgt.model.ExecutorDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowConfigDTO;
import org.wso2.carbon.identity.flow.mgt.model.StepDTO;
import org.wso2.carbon.identity.governance.IdentityGovernanceException;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.multi.attribute.login.constants.MultiAttributeLoginConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_DUPLICATE_COMPONENT_ID;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_GET_GOVERNANCE_CONFIG;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_EXECUTOR;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowGeneration.EMPTY;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowGeneration.END;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowGeneration.FORM;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowGeneration.NEXT;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.FlowGeneration.NULL;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.USERNAME_IDENTIFIER;
import static org.wso2.carbon.identity.api.server.flow.management.v1.constants.FlowEndpointConstants.USER_IDENTIFIER;

/**
 * Utility class for flow management.
 */
public class Utils {

    private static final Log LOG = LogFactory.getLog(Utils.class);

    private Utils() {

    }

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
     * Handles exceptions and returns an APIError object.
     *
     * @param e FlowMgtFrameworkException object.
     * @param data Data to be added to the description.
     * @return APIError object.
     */
    public static APIError handleFlowMgtException(FlowMgtFrameworkException e, Object ... data) {

        String description = e.getDescription();
        if (ArrayUtils.isNotEmpty(data)) {
            description = String.format(description, data);
        }
        e.setDescription(description);
        return handleFlowMgtException(e);
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

        return new Executor()
                .name(executorDTO.getName())
                .meta(convertToMap(executorDTO.getMetadata()));
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
        if (meta != null && !meta.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                meta.forEach(LambdaExceptionUtils.rethrowBiConsumer((name, value) -> {
                    executorDTO.addMetadata(name, value instanceof String
                            ? (String) value
                            : objectMapper.writeValueAsString(value));
                }));
            } catch (Exception e) {
                throw handleFlowMgtException(new FlowMgtClientException(
                        FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_METADATA.getCode(),
                        FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_METADATA.getMessage(),
                        FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_METADATA.getDescription(), e));
            }
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
    public static boolean getGovernanceConfig(String tenantDomain, String connectorConfig) {

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
            throw handleFlowMgtException(new FlowMgtClientException(
                    ERROR_CODE_GET_GOVERNANCE_CONFIG.getCode(),
                    ERROR_CODE_GET_GOVERNANCE_CONFIG.getMessage(),
                    ERROR_CODE_GET_GOVERNANCE_CONFIG.getDescription(), e));
        }
    }

    /**
     * Collects flow data from the provided steps.
     *
     * @param steps       List of steps to collect data from.
     * @param executors   Set to collect executor names.
     * @param identifiers Set to collect identifiers.
     * @param ids         Set to collect component IDs.
     */
    public static void collectFlowData(List<Step> steps, Set<String> executors, Set<String> identifiers,
                                       Set<String> ids) {

        for (Step step : steps) {
            if (step.getId() != null && !step.getId().isEmpty()) {
                ids.add(step.getId());
            }
            if (step.getData() != null && step.getData().getComponents() != null) {
                traverseComponents(step.getData().getComponents(), executors, identifiers, ids);
            }
        }
    }

    private static void traverseComponents(List<Component> components, Set<String> executors, Set<String> identifiers,
                                           Set<String> ids) {

        for (Component component : components) {
            if (ids != null && component.getId() != null && !component.getId().isEmpty()) {
                if (!ids.add(component.getId())) {
                    throw handleFlowMgtException(new FlowMgtClientException(
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

    /**
     * Validates the identifiers provided in the flow.
     *
     * @param metaResponseHandler The handler for the flow metadata response.
     * @param identifiers         Set of identifiers to validate.
     */
    public static void validateIdentifiers(AbstractMetaResponseHandler metaResponseHandler, Set<String> identifiers) {

        List<String> required = metaResponseHandler.getRequiredInputFields();
        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        boolean alternativeLoginStatus = Utils.getGovernanceConfig(tenantDomain,
                MultiAttributeLoginConstants.MULTI_ATTRIBUTE_LOGIN_PROPERTY);

        // Determine which identifiers are acceptable for "identity"
        List<String> identityIdentifiers = alternativeLoginStatus ?
                Arrays.asList(USER_IDENTIFIER, USERNAME_IDENTIFIER) :
                Collections.singletonList(USERNAME_IDENTIFIER);

        // Check if at least one required identity identifier is present
        boolean needsIdentity = required.stream().anyMatch(identityIdentifiers::contains);
        boolean hasIdentity = identifiers.stream().anyMatch(identityIdentifiers::contains);

        if (needsIdentity && !hasIdentity) {
            throw handleFlowMgtException(new FlowMgtClientException(
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_MISSING_IDENTIFIER.getCode(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_MISSING_IDENTIFIER.getMessage(),
                    alternativeLoginStatus
                            ? "Either user identifier or username identifier must be provided."
                            : "Username identifier must be provided."));
        }

        // Check remaining required identifiers (excluding identity identifiers)
        List<String> remainingRequired = new ArrayList<>(required);
        remainingRequired.removeAll(identityIdentifiers);

        if (!identifiers.containsAll(remainingRequired)) {
            throw handleFlowMgtException(new FlowMgtClientException(
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_MISSING_IDENTIFIER.getCode(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_MISSING_IDENTIFIER.getMessage(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_MISSING_IDENTIFIER.getDescription()));
        }
    }

    /**
     * Validates the executors provided in the flow.
     *
     * @param metaResponseHandler The handler for the flow metadata response.
     * @param executors           Set of executors to validate.
     */
    public static void validateExecutors(AbstractMetaResponseHandler metaResponseHandler, Set<String> executors) {

        if (!new HashSet<>(metaResponseHandler.getSupportedExecutors()).containsAll(executors)) {
            throw handleFlowMgtException(new FlowMgtClientException(
                    ERROR_CODE_UNSUPPORTED_EXECUTOR.getCode(),
                    ERROR_CODE_UNSUPPORTED_EXECUTOR.getMessage(),
                    ERROR_CODE_UNSUPPORTED_EXECUTOR.getDescription()));
        }
    }

    public static FlowConfig convertToFlowConfig(FlowConfigDTO flowConfig) {

        FlowConfig config = new FlowConfig();
        config.setFlowType(flowConfig.getFlowType());
        config.setIsEnabled(flowConfig.getIsEnabled() != null && flowConfig.getIsEnabled());
        config.setFlowCompletionConfigs(flowConfig.getAllFlowCompletionConfigs());
        return config;
    }

    public static FlowConfigDTO convertToFlowConfigDTO(FlowConfigPatchModel flowConfig) {

        FlowConfigDTO config = new FlowConfigDTO();
        config.setFlowType(flowConfig.getFlowType());
        config.setIsEnabled(flowConfig.getIsEnabled() != null && flowConfig.getIsEnabled());
        config.addAllFlowCompletionConfigs(flowConfig.getFlowCompletionConfigs());
        return config;
    }

    public static void validateFlowType(String value) {

        if (StringUtils.isBlank(value) || Arrays.stream(Constants.FlowTypes.values())
                .noneMatch(type -> type.name().equals(value))) {
            throw Utils.handleFlowMgtException(new FlowMgtClientException(
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_FLOW_TYPE.getCode(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_FLOW_TYPE.getMessage(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_FLOW_TYPE.getDescription()));
        }
    }

    /**
     * Get the list of supported properties for a given flow type.
     *
     * @param flowType The flow type.
     * @return List of supported properties.
     */
    public static List<String> getSupportedFlowCompletionConfig(String flowType) {

        Constants.FlowTypes requestedFlowType  = Constants.FlowTypes.valueOf(flowType);
        List<Constants.FlowCompletionConfig> supportedFlags  = requestedFlowType.getSupportedFlowCompletionConfigs();
        return supportedFlags.stream().map(Constants.FlowCompletionConfig::getConfig).collect(Collectors.toList());
    }

    /**
     * Validate the flow completion config and its value.
     *
     * @param flowCompletionConfig The flow completion config to validate.
     * @param value                The value of the flow completion config.
     * @param supportedProperties  List of supported properties for the flow type.
     * @param flowType             The flow type.
     * @throws FlowMgtClientException If the flow completion config or its value is invalid.
     */
    public static void validateFlowCompletionConfig(String flowCompletionConfig, String value ,
                                                    List<String> supportedProperties, String flowType)
            throws FlowMgtClientException {

        if (StringUtils.isBlank(flowCompletionConfig) || StringUtils.isBlank(value)) {

            throw Utils.handleFlowMgtException(new FlowMgtClientException(
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_FLOW_COMPLETION_CONFIG.getCode(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_FLOW_COMPLETION_CONFIG.getMessage(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_FLOW_COMPLETION_CONFIG.getDescription()),
                    flowCompletionConfig
            );
        }
        if (!supportedProperties.contains(flowCompletionConfig)) {
            throw Utils.handleFlowMgtException(new FlowMgtClientException(
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_FLOW_COMPLETION_CONFIG.getCode(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_FLOW_COMPLETION_CONFIG.getMessage(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_FLOW_COMPLETION_CONFIG.getDescription()),
                    flowCompletionConfig, flowType
            );
        }
    }

    /**
     * Validate the connectivity of nodes in the flow graph.
     *
     * @param steps List of steps to validate.
     */
    public static void validateNodeConnectivity(List<Step> steps) {

        if (CollectionUtils.isEmpty(steps)) {
            throw Utils.handleFlowMgtException(new FlowMgtClientException(
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_EMPTY_STEPS.getCode(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_EMPTY_STEPS.getMessage(),
                    FlowEndpointConstants.ErrorMessages.ERROR_CODE_EMPTY_STEPS.getDescription()));
        }
        for (Step step : steps) {
            validateNextNodeReference(step);
        }
    }

    /**
     * Validate the next node reference of a step.
     *
     * @param step Step to validate.
     */
    public static void validateNextNodeReference(Step step) {

        //If the step is not END, and contains action, then the next must not be null.
        if (!END.equals(step.getType())) {
            if (step.getData() != null && step.getData().getAction() != null &&
                    StringUtils.isBlank(step.getData().getAction().getNext())) {
                throw Utils.handleFlowMgtException(new FlowMgtClientException(
                                FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_PROPERTY.getCode(),
                                FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_PROPERTY.getMessage(),
                                FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_PROPERTY.getDescription()),
                        NEXT, step.getData().getAction().getNext() == null ? NULL : EMPTY);
            }
            validateNextNodeReference(step.getData().getComponents());
        }
    }

    /**
     * Validate the next node reference of components recursively.
     *
     * @param components List of components to validate.
     */
    public static void validateNextNodeReference(List<Component> components) {

        if (!CollectionUtils.isEmpty(components)) {
            for (Component component : components) {
                validateNextNodeReference(component);
            }
        }
    }

    /**
     * Validate the next node reference of a component recursively.
     *
     * @param component Component to validate.
     */
    public static void validateNextNodeReference(Component component) {

        if (component.getAction() != null && StringUtils.isBlank(component.getAction().getNext())) {
            throw Utils.handleFlowMgtException(new FlowMgtClientException(
                            FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_PROPERTY.getCode(),
                            FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_PROPERTY.getMessage(),
                            FlowEndpointConstants.ErrorMessages.ERROR_CODE_UNSUPPORTED_PROPERTY.getDescription()),
                    NEXT, component.getAction().getNext() == null ? NULL : EMPTY);
        }

        // If the component is of type FORM, has its child components.
        if (FORM.equals(component.getType())) {
            validateNextNodeReference(component.getComponents());
        }
    }
}
