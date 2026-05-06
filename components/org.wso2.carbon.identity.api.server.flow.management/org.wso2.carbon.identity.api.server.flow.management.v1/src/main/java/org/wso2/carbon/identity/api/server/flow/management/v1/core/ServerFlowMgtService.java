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

package org.wso2.carbon.identity.api.server.flow.management.v1.core;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.action.management.api.service.ActionManagementService;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfigPatchModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionBasicResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionContextTreeResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionNameCheckRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionNameCheckResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionUpdateModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.Step;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.AbstractMetaResponseHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.AskPasswordFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.PasswordRecoveryFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.RegistrationFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.InFlowExtensionContextTreeMapper;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.InFlowExtensionMapper;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.flow.execution.engine.inflow.extension.metadata.InFlowExtensionContextTreeMetadata;
import org.wso2.carbon.identity.flow.execution.engine.inflow.extension.metadata.InFlowExtensionContextTreeService;
import org.wso2.carbon.identity.flow.mgt.Constants;
import org.wso2.carbon.identity.flow.mgt.FlowMgtService;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtFrameworkException;
import org.wso2.carbon.identity.flow.mgt.model.FlowConfigDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils.collectFlowData;
import static org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils.validateExecutors;
import static org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils.validateIdentifiers;
import static org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils.validateNodeConnectivity;

/**
 * Service class for flow management.
 */
public class ServerFlowMgtService {

    private static final String IN_FLOW_EXTENSION_ACTION_TYPE =
            Action.ActionTypes.IN_FLOW_EXTENSION.getPathParam();

    private final FlowMgtService flowMgtService;
    private final ActionManagementService actionManagementService;

    public ServerFlowMgtService(FlowMgtService flowMgtService,
                                ActionManagementService actionManagementService) {

        this.flowMgtService = flowMgtService;
        this.actionManagementService = actionManagementService;
    }

    /**
     * Retrieve the flow.
     *
     * @return FlowResponse.
     */
    public FlowResponse getFlow(String flowType) {

        FlowDTO flowDTO;
        try {
            Utils.validateFlowType(flowType);
            flowDTO = flowMgtService
                    .getFlow(flowType, PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
            FlowResponse flowResponse = new FlowResponse();
            if (flowDTO == null) {
                return flowResponse;
            }
            flowResponse.steps(flowDTO.getSteps().stream().map(Utils::convertToStep)
                    .collect(Collectors.toList()));
            return flowResponse;
        } catch (FlowMgtFrameworkException e) {
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Retrieve flow metadata based on the flow type.
     *
     * @param flowType Type of the flow.
     * @return FlowMetaResponse containing metadata.
     */
    public FlowMetaResponse getFlowMeta(String flowType) {

        Utils.validateFlowType(flowType);
        AbstractMetaResponseHandler metaResponseHandler = resolveHandler(flowType);
        return metaResponseHandler.createResponse();
    }

    /**
     * Retrieve the controlled In-Flow Extension context tree for the given flow type. When
     * {@code flowType} is null/blank, the default tree is returned. The tree is filtered
     * server-side per the {@code [identity.in_flow_extension.context.*]} whitelist in
     * {@code deployment.toml} so the Console UI only offers paths the deployment allows.
     *
     * @param flowType optional flow type (null → default tree).
     * @return the tree response.
     */
    public InFlowExtensionContextTreeResponse getInFlowExtensionContextTree(String flowType) {

        String resolvedFlowType = (flowType != null && !flowType.trim().isEmpty()) ? flowType : null;
        if (resolvedFlowType != null) {
            // Reuse the same flow-type validation used by the other endpoints.
            Utils.validateFlowType(resolvedFlowType);
        }
        // Goes through the engine's PUBLIC metadata service rather than reaching into the
        // engine's internal DataHolder — the latter lives in a Private-Package and isn't
        // visible to other OSGi bundles at runtime.
        InFlowExtensionContextTreeMetadata metadata =
                InFlowExtensionContextTreeService.getInstance().buildContextTree(resolvedFlowType);
        return InFlowExtensionContextTreeMapper.toResponse(metadata);
    }

    /**
     * Update the flow.
     *
     * @param flowRequest FlowRequest.
     */
    public void updateFlow(FlowRequest flowRequest) {

        try {
            Utils.validateFlowType(flowRequest.getFlowType());
            validateFlow(flowRequest.getFlowType(), flowRequest.getSteps());
            FlowDTO flowDTO = new FlowDTO();
            flowDTO.setSteps(flowRequest.getSteps().stream().map(Utils::convertToStepDTO)
                    .collect(Collectors.toList()));
            flowDTO.setFlowType(flowRequest.getFlowType());
            flowMgtService.updateFlow(flowDTO,
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
        } catch (FlowMgtFrameworkException e) {
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Delete the flow for a specific flow type.
     *
     * @param flowType Type of the flow.
     */
    public void deleteFlow(String flowType) {

        try {
            Utils.validateFlowType(flowType);
            flowMgtService.deleteFlow(flowType,
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
        } catch (FlowMgtFrameworkException e) {
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Retrieve the flow configurations.
     *
     * @return List of FlowConfig.
     */
    public List<FlowConfig> getFlowConfigs() {

        try {
            List<FlowConfigDTO> flowMgtConfigs = flowMgtService.getFlowConfigs(
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
            return flowMgtConfigs.stream()
                    .map(Utils::convertToFlowConfig)
                    .collect(Collectors.toList());
        } catch (FlowMgtFrameworkException e) {
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Retrieve the flow configuration for a specific flow type.
     *
     * @param flowType Type of the flow.
     * @return FlowConfig for the specified flow type.
     */
    public FlowConfig getFlowConfigForFlow(String flowType) {

        try {
            Utils.validateFlowType(flowType);
            FlowConfigDTO flowConfig = flowMgtService.getFlowConfig(flowType,
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
            return Utils.convertToFlowConfig(flowConfig);
        } catch (FlowMgtFrameworkException e) {
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Update the flow configuration for a specific flow type.
     *
     * @param flowConfigPatchModel FlowConfigPatchModel containing the updated configuration.
     */
    public FlowConfig updateFlowConfig(FlowConfigPatchModel flowConfigPatchModel) {

        try {
            Utils.validateFlowType(flowConfigPatchModel.getFlowType());
            FlowConfigDTO existingFlowConfig = flowMgtService.getFlowConfig(
                    flowConfigPatchModel.getFlowType(),
                    PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
            // If the patch model does not contain values for isEnabled or any properties,
            // retain the existing values from the flow configuration.
            if (existingFlowConfig != null) {
                if (flowConfigPatchModel.getIsEnabled() == null) {
                    flowConfigPatchModel.setIsEnabled(existingFlowConfig.getIsEnabled());
                }

                Map<String, String> existingFlowCompletionConfigs = existingFlowConfig.getAllFlowCompletionConfigs();
                Map<String, String> patchFlowCompletionConfigs = flowConfigPatchModel.getFlowCompletionConfigs();
                List<String> supportedFlowCompletionConfigs = Utils.getSupportedFlowCompletionConfig(
                        flowConfigPatchModel.getFlowType());
                // Validate the configs provided in the patch model.
                if (patchFlowCompletionConfigs != null) {
                    for (Map.Entry<String, String> entry : patchFlowCompletionConfigs.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        Utils.validateFlowCompletionConfig(key, value,
                                supportedFlowCompletionConfigs, flowConfigPatchModel.getFlowType());
                    }
                }
                // Iterate over existing configs and add those which are not present in the patch model.
                for (Map.Entry<String, String> entry : existingFlowCompletionConfigs.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (patchFlowCompletionConfigs == null || !patchFlowCompletionConfigs.containsKey(key)) {
                        flowConfigPatchModel.putFlowCompletionConfigsItem(key, value);
                    }
                }
            }
            FlowConfigDTO updatedFlowConfig =
                    flowMgtService.updateFlowConfig(Utils.convertToFlowConfigDTO(flowConfigPatchModel),
                            PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId());
            return Utils.convertToFlowConfig(updatedFlowConfig);
        } catch (FlowMgtFrameworkException e) {
            throw Utils.handleFlowMgtException(e);
        }
    }

    // =========================================================================
    // InFlow Extension CRUD
    // =========================================================================

    /**
     * Create a new InFlow extension action.
     *
     * @param model the create request model.
     * @return the full response of the created extension.
     */
    public InFlowExtensionResponse createInFlowExtension(InFlowExtensionModel model) {

        try {
            Action domainAction = InFlowExtensionMapper.toInFlowExtensionAction(model);
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            Action created = actionManagementService.addAction(
                    IN_FLOW_EXTENSION_ACTION_TYPE, domainAction, tenantDomain);
            return InFlowExtensionMapper.toInFlowExtensionResponse(created);
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * List all InFlow extension actions for the current tenant.
     *
     * @return list of basic response items.
     */
    public List<InFlowExtensionBasicResponse> getInFlowExtensions() {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            List<Action> actions = actionManagementService.getActionsByActionType(
                    IN_FLOW_EXTENSION_ACTION_TYPE, tenantDomain);
            return actions.stream()
                    .map(InFlowExtensionMapper::toInFlowExtensionBasicResponse)
                    .collect(Collectors.toList());
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * Get a single InFlow extension by its ID.
     *
     * @param extensionId the action ID.
     * @return the full response model.
     */
    public InFlowExtensionResponse getInFlowExtensionById(String extensionId) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            Action action = actionManagementService.getActionByActionId(
                    IN_FLOW_EXTENSION_ACTION_TYPE, extensionId, tenantDomain);
            return InFlowExtensionMapper.toInFlowExtensionResponse(action);
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * Update (PATCH) an InFlow extension.
     *
     * @param extensionId the action ID to update.
     * @param model       the update request model.
     * @return the full updated response model.
     */
    public InFlowExtensionResponse updateInFlowExtension(String extensionId,
                                                         InFlowExtensionUpdateModel model) {

        try {
            Action domainAction = InFlowExtensionMapper.toInFlowExtensionAction(model);
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            Action updated = actionManagementService.updateAction(
                    IN_FLOW_EXTENSION_ACTION_TYPE, extensionId, domainAction, tenantDomain);
            return InFlowExtensionMapper.toInFlowExtensionResponse(updated);
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * Delete an InFlow extension by its ID.
     *
     * @param extensionId the action ID to delete.
     */
    public void deleteInFlowExtension(String extensionId) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            actionManagementService.deleteAction(
                    IN_FLOW_EXTENSION_ACTION_TYPE, extensionId, tenantDomain);
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * Check whether the given InFlow extension name is available (unique) for the current tenant.
     * When {@code request.getExcludeId()} is non-null the check excludes that action ID (update
     * scenario).
     *
     * @param request the name-check request model.
     * @return a response model with {@code available = true/false}.
     */
    public InFlowExtensionNameCheckResponse checkInFlowExtensionName(
            InFlowExtensionNameCheckRequest request) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            boolean available;
            if (request.getExcludeId() != null && !request.getExcludeId().isEmpty()) {
                available = actionManagementService.isActionNameAvailable(
                        IN_FLOW_EXTENSION_ACTION_TYPE, request.getName(),
                        request.getExcludeId(), tenantDomain);
            } else {
                available = actionManagementService.isActionNameAvailable(
                        IN_FLOW_EXTENSION_ACTION_TYPE, request.getName(), tenantDomain);
            }
            return new InFlowExtensionNameCheckResponse().available(available);
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * Validate the flow type and steps.
     *
     * @param flowType  Type of the flow.
     * @param flowSteps List of steps in the flow.
     */
    private void validateFlow(String flowType, List<Step> flowSteps) {

        AbstractMetaResponseHandler metaResponseHandler = resolveHandler(flowType);
        Set<String> flowExecutorNames = new HashSet<>();
        Set<String> flowFieldIdentifiers = new HashSet<>();
        Set<String> flowComponentIds = new HashSet<>();
        collectFlowData(flowSteps, flowExecutorNames, flowFieldIdentifiers, flowComponentIds);
        validateNodeConnectivity(flowSteps);
        validateExecutors(metaResponseHandler, flowExecutorNames);
        validateIdentifiers(metaResponseHandler, flowFieldIdentifiers);
    }

    /**
     * Resolve the appropriate handler based on the flow type.
     *
     * @param flowType Type of the flow.
     * @return An instance of AbstractMetaResponseHandler.
     */
    private AbstractMetaResponseHandler resolveHandler(String flowType) {

        switch (Constants.FlowTypes.valueOf(flowType)) {
            case REGISTRATION:
                return new RegistrationFlowMetaHandler();
            case PASSWORD_RECOVERY:
                return new PasswordRecoveryFlowMetaHandler();
            case INVITED_USER_REGISTRATION:
                return new AskPasswordFlowMetaHandler();
            default:
                throw new IllegalStateException("Unhandled flow type: " + flowType);
        }
    }
}
