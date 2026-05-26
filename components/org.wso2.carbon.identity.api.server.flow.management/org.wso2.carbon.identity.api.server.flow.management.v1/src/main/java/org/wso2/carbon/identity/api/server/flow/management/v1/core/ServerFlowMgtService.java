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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.action.management.api.exception.ActionMgtException;
import org.wso2.carbon.identity.action.management.api.model.Action;
import org.wso2.carbon.identity.action.management.api.service.ActionManagementService;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfigPatchModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionBasicResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionContextTreeResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowExtensionUpdateModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowMetaResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.Step;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.AbstractMetaResponseHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.AskPasswordFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.PasswordRecoveryFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.RegistrationFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.FlowExtensionContextTreeMapper;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.FlowExtensionMapper;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.flow.extension.metadata.FlowExtensionContextTreeMetadata;
import org.wso2.carbon.identity.flow.extension.metadata.FlowExtensionContextTreeService;
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

    private static final String FLOW_EXTENSION_ACTION_TYPE =
            Action.ActionTypes.FLOW_EXTENSION.getPathParam();

    private final FlowMgtService flowMgtService;
    private final ActionManagementService actionManagementService;

    public ServerFlowMgtService(FlowMgtService flowMgtService) {

        this(flowMgtService, null);
    }

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
     * Retrieve the controlled Flow Extension context tree for the given flow type. When
     * {@code flowType} is null/blank, the default tree is returned. The tree is filtered
     * server-side per the {@code [identity.flow_extension.context.*]} whitelist in
     * {@code deployment.toml} so the Console UI only offers paths the deployment allows.
     *
     * @param flowType optional flow type (null → default tree).
     * @return the tree response.
     */
    public FlowExtensionContextTreeResponse getFlowExtensionContextTree(String flowType) {

        String resolvedFlowType = StringUtils.isNotBlank(flowType) ? flowType : null;
        if (resolvedFlowType != null) {
            // Reuse the same flow-type validation used by the other endpoints.
            Utils.validateFlowType(resolvedFlowType);
        }
        // Goes through the engine's PUBLIC metadata service rather than reaching into the
        // engine's internal DataHolder — the latter lives in a Private-Package and isn't
        // visible to other OSGi bundles at runtime.
        FlowExtensionContextTreeMetadata metadata =
                FlowExtensionContextTreeService.getInstance().buildContextTree(resolvedFlowType);
        return FlowExtensionContextTreeMapper.toResponse(metadata);
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

    /**
     * Create a new Flow extension action.
     *
     * @param model the create request model.
     * @return the full response of the created extension.
     */
    public FlowExtensionResponse createFlowExtension(FlowExtensionModel model) {

        try {
            Action flowExtensionAction = FlowExtensionMapper.toFlowExtensionAction(model);
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            Action created = actionManagementService.addAction(
                    FLOW_EXTENSION_ACTION_TYPE, flowExtensionAction, tenantDomain);
            return FlowExtensionMapper.toFlowExtensionResponse(created);
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * List all Flow extension actions for the current tenant.
     *
     * @return list of basic response items.
     */
    public List<FlowExtensionBasicResponse> getFlowExtensions() {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            List<Action> actions = actionManagementService.getActionsByActionType(
                    FLOW_EXTENSION_ACTION_TYPE, tenantDomain);
            return actions.stream()
                    .map(FlowExtensionMapper::toFlowExtensionBasicResponse)
                    .collect(Collectors.toList());
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * Get a single Flow extension by its ID.
     *
     * @param extensionId the action ID.
     * @return the full response model.
     */
    public FlowExtensionResponse getFlowExtensionById(String extensionId) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            Action action = actionManagementService.getActionByActionId(
                    FLOW_EXTENSION_ACTION_TYPE, extensionId, tenantDomain);
            return FlowExtensionMapper.toFlowExtensionResponse(action);
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * Update (PATCH) an Flow extension.
     *
     * @param extensionId the action ID to update.
     * @param model       the update request model.
     * @return the full updated response model.
     */
    public FlowExtensionResponse updateFlowExtension(String extensionId,
                                                         FlowExtensionUpdateModel model) {

        try {
            Action flowExtensionAction = FlowExtensionMapper.toFlowExtensionAction(model);
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            Action updated = actionManagementService.updateAction(
                    FLOW_EXTENSION_ACTION_TYPE, extensionId, flowExtensionAction, tenantDomain);
            return FlowExtensionMapper.toFlowExtensionResponse(updated);
        } catch (ActionMgtException e) {
            throw Utils.handleActionMgtException(e);
        }
    }

    /**
     * Delete an Flow extension by its ID.
     *
     * @param extensionId the action ID to delete.
     */
    public void deleteFlowExtension(String extensionId) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            actionManagementService.deleteAction(
                    FLOW_EXTENSION_ACTION_TYPE, extensionId, tenantDomain);
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
