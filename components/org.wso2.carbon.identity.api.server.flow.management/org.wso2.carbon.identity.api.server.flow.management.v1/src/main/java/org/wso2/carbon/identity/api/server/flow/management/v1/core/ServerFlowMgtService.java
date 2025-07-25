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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.flow.management.v1.BaseFlowMetaResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfig;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowConfigPatchModel;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRequest;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowResponse;
import org.wso2.carbon.identity.api.server.flow.management.v1.Step;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.AbstractMetaResponseHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.AskPasswordFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.PasswordRecoveryFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.response.handlers.RegistrationFlowMetaHandler;
import org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils;
import org.wso2.carbon.identity.flow.mgt.Constants;
import org.wso2.carbon.identity.flow.mgt.FlowMgtService;
import org.wso2.carbon.identity.flow.mgt.exception.FlowMgtFrameworkException;
import org.wso2.carbon.identity.flow.mgt.model.FlowConfigDTO;
import org.wso2.carbon.identity.flow.mgt.model.FlowDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils.collectFlowData;
import static org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils.validateExecutors;
import static org.wso2.carbon.identity.api.server.flow.management.v1.utils.Utils.validateIdentifiers;

/**
 * Service class for flow management.
 */
public class ServerFlowMgtService {

    private static final Log LOG = LogFactory.getLog(ServerFlowMgtService.class);
    private final FlowMgtService flowMgtService;

    public ServerFlowMgtService(FlowMgtService flowMgtService) {

        this.flowMgtService = flowMgtService;
        if (LOG.isDebugEnabled()) {
            LOG.debug("ServerFlowMgtService initialized with FlowMgtService.");
        }
    }

    /**
     * Retrieve the flow.
     *
     * @return FlowResponse.
     */
    public FlowResponse getFlow(String flowType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving flow for type: " + flowType);
        }
        FlowDTO flowDTO;
        try {
            Utils.validateFlowType(flowType);
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            flowDTO = flowMgtService.getFlow(flowType, tenantId);
            FlowResponse flowResponse = new FlowResponse();
            if (flowDTO == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No flow found for type: " + flowType + " in tenant: " + tenantId);
                }
                return flowResponse;
            }
            flowResponse.steps(flowDTO.getSteps().stream().map(Utils::convertToStep)
                    .collect(Collectors.toList()));
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved flow for type: " + flowType + " with " +
                        (flowDTO.getSteps() != null ? flowDTO.getSteps().size() : 0) + " steps.");
            }
            return flowResponse;
        } catch (FlowMgtFrameworkException e) {
            LOG.error("Error retrieving flow for type: " + flowType, e);
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Retrieve flow metadata based on the flow type.
     *
     * @param flowType Type of the flow.
     * @return BaseFlowMetaResponse containing metadata.
     */
    public BaseFlowMetaResponse getFlowMeta(String flowType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving flow meta for type: " + flowType);
        }
        Utils.validateFlowType(flowType);
        AbstractMetaResponseHandler metaResponseHandler = resolveHandler(flowType);
        BaseFlowMetaResponse response = metaResponseHandler.createResponse();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved flow meta for type: " + flowType);
        }
        return response;
    }

    /**
     * Update the flow.
     *
     * @param flowRequest FlowRequest.
     */
    public void updateFlow(FlowRequest flowRequest) {

        String flowType = flowRequest != null ? flowRequest.getFlowType() : null;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating flow for type: " + flowType);
        }
        try {
            Utils.validateFlowType(flowRequest.getFlowType());
            validateFlow(flowRequest.getFlowType(), flowRequest.getSteps());
            FlowDTO flowDTO = new FlowDTO();
            flowDTO.setSteps(flowRequest.getSteps().stream().map(Utils::convertToStepDTO)
                    .collect(Collectors.toList()));
            flowDTO.setFlowType(flowRequest.getFlowType());
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            flowMgtService.updateFlow(flowDTO, tenantId);
            LOG.info("Flow updated successfully for type: " + flowType + " in tenant: " + tenantId);
        } catch (FlowMgtFrameworkException e) {
            LOG.error("Error updating flow for type: " + flowType, e);
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Retrieve the flow configurations.
     *
     * @return List of FlowConfig.
     */
    public List<FlowConfig> getFlowConfigs() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving all flow configs.");
        }
        try {
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            List<FlowConfigDTO> flowMgtConfigs = flowMgtService.getFlowConfigs(tenantId);
            List<FlowConfig> result = flowMgtConfigs.stream()
                    .map(Utils::convertToFlowConfig)
                    .collect(Collectors.toList());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved " + result.size() + " flow configs for tenant: " + tenantId);
            }
            return result;
        } catch (FlowMgtFrameworkException e) {
            LOG.error("Error retrieving flow configs.", e);
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

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving flow config for type: " + flowType);
        }
        try {
            Utils.validateFlowType(flowType);
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            FlowConfigDTO flowConfig = flowMgtService.getFlowConfig(flowType, tenantId);
            FlowConfig result = Utils.convertToFlowConfig(flowConfig);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved flow config for type: " + flowType + " in tenant: " + tenantId);
            }
            return result;
        } catch (FlowMgtFrameworkException e) {
            LOG.error("Error retrieving flow config for type: " + flowType, e);
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Update the flow configuration for a specific flow type.
     *
     * @param flowConfigPatchModel FlowConfigPatchModel containing the updated configuration.
     */
    public FlowConfig updateFlowConfig(FlowConfigPatchModel flowConfigPatchModel) {

        String flowType = flowConfigPatchModel != null ? flowConfigPatchModel.getFlowType() : null;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating flow config for type: " + flowType);
        }
        try {
            Utils.validateFlowType(flowConfigPatchModel.getFlowType());
            int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
            FlowConfigDTO existingFlowConfig = flowMgtService.getFlowConfig(flowConfigPatchModel.getFlowType(),
                    tenantId);
            // If the patch model does not contain values for isEnabled or isAutoLoginEnabled,
            // retain the existing values from the flow configuration.
            if (existingFlowConfig != null) {
                if (flowConfigPatchModel.getIsEnabled() == null) {
                    flowConfigPatchModel.setIsEnabled(existingFlowConfig.getIsEnabled());
                }
                if (flowConfigPatchModel.getIsAutoLoginEnabled() == null) {
                    flowConfigPatchModel.setIsAutoLoginEnabled(existingFlowConfig.getIsAutoLoginEnabled());
                }
            }
            FlowConfigDTO updatedFlowConfig =
                    flowMgtService.updateFlowConfig(Utils.convertToFlowConfigDTO(flowConfigPatchModel), tenantId);
            LOG.info("Flow config updated successfully for type: " + flowType + " in tenant: " + tenantId);
            return Utils.convertToFlowConfig(updatedFlowConfig);
        } catch (FlowMgtFrameworkException e) {
            LOG.error("Error updating flow config for type: " + flowType, e);
            throw Utils.handleFlowMgtException(e);
        }
    }

    /**
     * Validate the flow type and steps.
     *
     * @param flowType  Type of the flow.
     * @param flowSteps List of steps in the flow.
     */
    private void validateFlow(String flowType, List<Step> flowSteps) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Validating flow for type: " + flowType + " with " +
                    (flowSteps != null ? flowSteps.size() : 0) + " steps.");
        }
        AbstractMetaResponseHandler metaResponseHandler = resolveHandler(flowType);
        Set<String> flowExecutorNames = new HashSet<>();
        Set<String> flowFieldIdentifiers = new HashSet<>();
        Set<String> flowComponentIds = new HashSet<>();
        collectFlowData(flowSteps, flowExecutorNames, flowFieldIdentifiers, flowComponentIds);
        validateExecutors(metaResponseHandler, flowExecutorNames);
        validateIdentifiers(metaResponseHandler, flowFieldIdentifiers);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Flow validation completed successfully for type: " + flowType);
        }
    }

    /**
     * Resolve the appropriate handler based on the flow type.
     *
     * @param flowType Type of the flow.
     * @return An instance of AbstractMetaResponseHandler.
     */
    private AbstractMetaResponseHandler resolveHandler(String flowType) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolving handler for flow type: " + flowType);
        }
        AbstractMetaResponseHandler handler;
        switch (Constants.FlowTypes.valueOf(flowType)) {
            case REGISTRATION:
                handler = new RegistrationFlowMetaHandler();
                break;
            case PASSWORD_RECOVERY:
                handler = new PasswordRecoveryFlowMetaHandler();
                break;
            case INVITED_USER_REGISTRATION:
                handler = new AskPasswordFlowMetaHandler();
                break;
            default:
                LOG.error("Unhandled flow type: " + flowType);
                throw new IllegalStateException("Unhandled flow type: " + flowType);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Resolved handler: " + handler.getClass().getSimpleName() + " for flow type: " + flowType);
        }
        return handler;
    }
}
