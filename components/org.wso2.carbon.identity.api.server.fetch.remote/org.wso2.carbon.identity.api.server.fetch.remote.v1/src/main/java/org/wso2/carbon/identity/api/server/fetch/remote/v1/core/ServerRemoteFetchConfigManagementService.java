/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.fetch.remote.v1.core;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.ActionListenerAttributes;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequestCommits;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationGetResponse;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationListItem;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationListResponse;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationPOSTRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationPatchRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RepositoryManagerAttributes;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.StatusListItem;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.StatusListResponse;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.remotefetch.common.BasicRemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.DeploymentRevision;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfiguration;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfigurationService;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchClientException;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchCoreException;
import org.wso2.carbon.identity.remotefetch.common.exceptions.RemoteFetchServerException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import javax.ws.rs.core.Response;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.ACCESS_TOKEN;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.ACTION_LISTENER;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.ACTION_LISTENER_ATTRIBUTES;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.BRANCH;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.CONFIGURATION_DEPLOYER;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.CONFIGURATION_DEPLOYER_ATTRIBUTES;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.DIRECTORY;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.FREQUENCY;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.IS_ENABLED;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.REMOTE_FETCH_NAME;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.REPOSITORY_MANAGER;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.REPOSITORY_MANAGER_ATTRIBUTES;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.URI;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.USER_NAME;
import static org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchConfigurationConstants.WEBHOOK_REQUEST;
import static org.wso2.carbon.identity.api.server.fetch.remote.v1.core.RemoteFetchUtils.convertDateToStringIfNotNull;
import static org.wso2.carbon.identity.api.server.fetch.remote.v1.core.RemoteFetchUtils.setIfNotNull;

/**
 * Call internal osgi services to perform server remote fetch configuration related operations.
 */
public class ServerRemoteFetchConfigManagementService {

    private final RemoteFetchConfigurationService remoteFetchConfigurationService;
    private static final Log log = LogFactory.getLog(ServerRemoteFetchConfigManagementService.class);

    public ServerRemoteFetchConfigManagementService(RemoteFetchConfigurationService remoteFetchConfigurationService) {

        this.remoteFetchConfigurationService = remoteFetchConfigurationService;
    }

    /**
     * Get list of remote fetch configurations.
     *
     * @return RemoteFetchConfigurationListResponse.
     */
    public RemoteFetchConfigurationListResponse getRemoteFetchConfigs() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all remote fetch configurations");
        }
        OptionalInt optionalIntLimit = OptionalInt.empty();
        OptionalInt optionalIntOffset = OptionalInt.empty();

        try {
            RemoteFetchConfigurationListResponse response = createRemoteFetchConfigurationListResponse(
                    remoteFetchConfigurationService.getBasicRemoteFetchConfigurationList(optionalIntLimit, 
                    optionalIntOffset));
            log.info("Successfully retrieved " + response.getCount() + " remote fetch configurations");
            return response;
        } catch (RemoteFetchCoreException e) {
            log.error("Failed to retrieve remote fetch configurations", e);
            throw handleRemoteFetchConfigurationException(e, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_ERROR_LISTING_RF_CONFIGS, null);
        }
    }

    /**
     * Delete an Remote fetch configuration.
     *
     * @param remoteFetchConfigurationId Identity Provider resource ID.
     */
    public void deleteRemoteFetchConfig(String remoteFetchConfigurationId) {

        if (log.isDebugEnabled()) {
            log.debug("Deleting remote fetch configuration with ID: " + remoteFetchConfigurationId);
        }
        try {
            remoteFetchConfigurationService.deleteRemoteFetchConfiguration(remoteFetchConfigurationId);
            log.info("Successfully deleted remote fetch configuration with ID: " + remoteFetchConfigurationId);
        } catch (RemoteFetchCoreException e) {
            log.error("Failed to delete remote fetch configuration with ID: " + remoteFetchConfigurationId, e);
            throw handleRemoteFetchConfigurationException(e, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_ERROR_DELETING_RF_CONFIGS, remoteFetchConfigurationId);
        }
    }

    /**
     * Get remote fetch configuration by resource Id.
     *
     * @param remoteFetchConfigurationId resource Id.
     * @return RemoteFetchConfigurationListResponse.
     */
    public RemoteFetchConfigurationGetResponse getRemoteFetchConfig(String remoteFetchConfigurationId) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving remote fetch configuration with ID: " + remoteFetchConfigurationId);
        }
        try {
            RemoteFetchConfiguration remoteFetchConfiguration = remoteFetchConfigurationService
                            .getRemoteFetchConfiguration(remoteFetchConfigurationId);
            if (remoteFetchConfiguration == null) {
                log.warn("Remote fetch configuration not found with ID: " + remoteFetchConfigurationId);
                throw handleException(Response.Status.NOT_FOUND, RemoteFetchConfigurationConstants.
                                ErrorMessage.ERROR_CODE_RE_CONFIG_NOT_FOUND,
                        remoteFetchConfigurationId);
            }
            if (log.isDebugEnabled()) {
                log.debug("Successfully retrieved remote fetch configuration: " + 
                        remoteFetchConfiguration.getRemoteFetchName());
            }
            return createRemoteFetchConfigurationResponse(remoteFetchConfiguration);
        } catch (RemoteFetchCoreException e) {
            log.error("Failed to retrieve remote fetch configuration with ID: " + remoteFetchConfigurationId, e);
            throw handleRemoteFetchConfigurationException(e, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_RF_CONFIG, null);
        }
    }

    /**
     * Update remote fetch configuration by resource id.
     *
     * @param id                                   Id.
     * @param remoteFetchConfigurationPatchRequest RemoteFetchConfigurationPatchRequest.
     */
    public void updateRemoteFetchConfig(String id,
                                        RemoteFetchConfigurationPatchRequest remoteFetchConfigurationPatchRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating remote fetch configuration with ID: " + id);
        }
        try {
            RemoteFetchConfiguration remoteFetchConfiguration = remoteFetchConfigurationService
                    .getRemoteFetchConfiguration(id);
            if (remoteFetchConfiguration == null) {
                log.warn("Remote fetch configuration not found for update with ID: " + id);
                throw handleException(Response.Status.NOT_FOUND, RemoteFetchConfigurationConstants.
                        ErrorMessage.ERROR_CODE_RE_CONFIG_NOT_FOUND, id);
            }
            RemoteFetchConfiguration remoteFetchConfigurationToUpdate = deepCopyRemoteFetchConfiguration
                    (id, remoteFetchConfiguration);

            setIfNotNull(remoteFetchConfigurationPatchRequest.getIsEnabled(),
                    remoteFetchConfigurationToUpdate::setEnabled);

            setIfNotNull(remoteFetchConfigurationPatchRequest.getRemoteFetchName(),
                    remoteFetchConfigurationToUpdate::setRemoteFetchName);

            remoteFetchConfigurationService.updateRemoteFetchConfiguration(remoteFetchConfigurationToUpdate);
            log.info("Successfully updated remote fetch configuration with ID: " + id);
        } catch (RemoteFetchCoreException e) {
            log.error("Failed to update remote fetch configuration with ID: " + id, e);
            throw handleRemoteFetchConfigurationException(e, RemoteFetchConfigurationConstants.ErrorMessage.
                    ERROR_CODE_ERROR_UPDATING_RF_CONFIG, null);
        }
    }

    /**
     * Trigger remote fetch.
     *
     * @param remoteFetchConfigurationId remoteFetchConfigurationId.
     */
    public void triggerRemoteFetch(String remoteFetchConfigurationId) {

        log.info("Triggering remote fetch for configuration ID: " + remoteFetchConfigurationId);
        try {
            RemoteFetchConfiguration remoteFetchConfiguration = remoteFetchConfigurationService
                    .getRemoteFetchConfiguration(remoteFetchConfigurationId);
            if (remoteFetchConfiguration != null) {
                remoteFetchConfigurationService.triggerRemoteFetch(remoteFetchConfiguration);
                log.info("Successfully triggered remote fetch for configuration ID: " + remoteFetchConfigurationId);
            } else {
                log.warn("Remote fetch configuration not found for trigger with ID: " + remoteFetchConfigurationId);
                throw handleException(Response.Status.NOT_FOUND, RemoteFetchConfigurationConstants.
                        ErrorMessage.ERROR_CODE_RE_CONFIG_NOT_FOUND, remoteFetchConfigurationId);
            }
        } catch (RemoteFetchCoreException e) {
            log.error("Failed to trigger remote fetch for configuration ID: " + remoteFetchConfigurationId, e);
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, RemoteFetchConfigurationConstants.ErrorMessage
                    .ERROR_CODE_ERROR_TRIGGER_REMOTE_FETCH, remoteFetchConfigurationId);
        }
    }

    /**
     * Add remote fetch configuration.
     *
     * @param remoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest
     * @return resource id.
     */
    public String addRemoteFetchConfiguration(RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Adding new remote fetch configuration: " + 
                    remoteFetchConfigurationPOSTRequest.getRemoteFetchName());
        }
        try {
            validatePOSTRequest(remoteFetchConfigurationPOSTRequest);
            String configId = remoteFetchConfigurationService.addRemoteFetchConfiguration(
                    createRemoteFetchConfiguration(remoteFetchConfigurationPOSTRequest)).getId();
            log.info("Successfully added remote fetch configuration with ID: " + configId);
            return configId;
        } catch (RemoteFetchCoreException e) {
            log.error("Failed to add remote fetch configuration: " + 
                    remoteFetchConfigurationPOSTRequest.getRemoteFetchName(), e);
            throw handleRemoteFetchConfigurationException(e, RemoteFetchConfigurationConstants.ErrorMessage.
                    ERROR_CODE_ERROR_ADDING_RF_CONFIG, null);
        }
    }

    private void validatePOSTRequest(RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Validating remote fetch configuration POST request");
        }
        if (remoteFetchConfigurationPOSTRequest.getIsEnabled() == null) {
            log.warn("Validation failed: isEnabled field is null");
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, IS_ENABLED);
        }
        if (StringUtils.isBlank(remoteFetchConfigurationPOSTRequest.getRemoteFetchName())) {
            log.warn("Validation failed: remoteFetchName is blank");
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, REMOTE_FETCH_NAME);
        }
        if (remoteFetchConfigurationPOSTRequest.getActionListener() == null) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, ACTION_LISTENER);
        }
        if (remoteFetchConfigurationPOSTRequest.getActionListener().getType() == null) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, ACTION_LISTENER);
        }
        if (remoteFetchConfigurationPOSTRequest.getActionListener().getAttributes() == null) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, ACTION_LISTENER_ATTRIBUTES);
        }
        if (remoteFetchConfigurationPOSTRequest.getRepositoryManager() == null) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, REPOSITORY_MANAGER);
        }
        if (remoteFetchConfigurationPOSTRequest.getRepositoryManager().getType() == null) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, REPOSITORY_MANAGER);
        }
        if (remoteFetchConfigurationPOSTRequest.getRepositoryManager().getAttributes() == null) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, REPOSITORY_MANAGER_ATTRIBUTES);
        }
        if (remoteFetchConfigurationPOSTRequest.getConfigurationDeployer() == null) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, CONFIGURATION_DEPLOYER);
        }
        if (remoteFetchConfigurationPOSTRequest.getConfigurationDeployer().getType() == null) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, CONFIGURATION_DEPLOYER);
        }
        if (remoteFetchConfigurationPOSTRequest.getConfigurationDeployer().getAttributes() == null) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, CONFIGURATION_DEPLOYER_ATTRIBUTES);
        }
    }

    /**
     * Get status of remote fetch.
     *
     * @param remoteFetchConfigurationId RemoteFetchConfigurationId.
     */
    public StatusListResponse getStatus(String remoteFetchConfigurationId) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving status for remote fetch configuration ID: " + remoteFetchConfigurationId);
        }
        try {
            RemoteFetchConfiguration remoteFetchConfiguration = remoteFetchConfigurationService
                    .getRemoteFetchConfiguration(remoteFetchConfigurationId);

            if (remoteFetchConfiguration != null) {
                StatusListResponse response = createStatusListResponse(remoteFetchConfigurationService
                        .getDeploymentRevisions(remoteFetchConfigurationId));
                if (log.isDebugEnabled()) {
                    log.debug("Retrieved status with " + response.getCount() + " deployment revisions for ID: " + 
                            remoteFetchConfigurationId);
                }
                return response;
            } else {
                log.warn("Remote fetch configuration not found for status retrieval with ID: " + 
                        remoteFetchConfigurationId);
                throw handleException(Response.Status.NOT_FOUND, RemoteFetchConfigurationConstants.
                        ErrorMessage.ERROR_CODE_RE_CONFIG_NOT_FOUND, remoteFetchConfigurationId);
            }
        } catch (RemoteFetchCoreException e) {
            log.error("Failed to retrieve status for remote fetch configuration ID: " + 
                    remoteFetchConfigurationId, e);
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, RemoteFetchConfigurationConstants.ErrorMessage
                    .ERROR_CODE_ERROR_STATUS_REMOTE_FETCH, remoteFetchConfigurationId);
        }
    }

    /**
     * This method is used to create status list from list of deployment revisions.
     * This method uses java stream api to count successful deployments and failed deployments.
     *
     * @param deploymentRevisions List of deployment revisions.
     * @return StatusListResponse.
     */
    private StatusListResponse createStatusListResponse(List<DeploymentRevision> deploymentRevisions) {

        StatusListResponse statusListResponse = new StatusListResponse();
        if (CollectionUtils.isNotEmpty(deploymentRevisions)) {
            List<StatusListItem> statusListItemList = new ArrayList<>();
            for (DeploymentRevision deploymentRevision : deploymentRevisions) {
                StatusListItem statusListItem = populateDeploymentRevision(deploymentRevision);
                statusListItemList.add(statusListItem);
            }
            statusListResponse.setRemoteFetchRevisionStatuses(statusListItemList);
            statusListResponse.setSuccessfulDeployments(
                    (int) deploymentRevisions.stream()
                            .filter(Objects::nonNull)
                            .filter(deploymentRevision ->
                                    deploymentRevision.getDeploymentStatus().name()
                                            .equals(RemoteFetchConfigurationConstants.SUCCESS)).count());
            statusListResponse.setFailedDeployments(
                    (int) deploymentRevisions.stream()
                            .filter(Objects::nonNull)
                            .filter(deploymentRevision ->
                                    deploymentRevision.getDeploymentStatus().name()
                                            .equals(RemoteFetchConfigurationConstants.FAIL)).count());
            Date date = deploymentRevisions.stream()
                    .map(DeploymentRevision::getLastSynchronizedDate)
                    .max(Date::compareTo)
                    .orElse(null);

            convertDateToStringIfNotNull(date, statusListResponse::setLastSynchronizedTime);
            return statusListResponse;
        } else {
            statusListResponse.setCount(0);
        }
        return statusListResponse;
    }

    /**
     * Populate deployment revision to get status list item.
     *
     * @param deploymentRevision DeploymentRevision.
     * @return StatusListItem.
     */
    private StatusListItem populateDeploymentRevision(DeploymentRevision deploymentRevision) {

        StatusListItem statusListItem = new StatusListItem();
        statusListItem.setDeployedStatus(deploymentRevision.getDeploymentStatus().name());
        convertDateToStringIfNotNull(deploymentRevision.getDeployedDate(), statusListItem::setDeployedTime);
        statusListItem.setItemName(deploymentRevision.getItemName());
        statusListItem.setDeploymentErrorReport(deploymentRevision.getErrorMessage());
        return statusListItem;
    }

    /**
     * This method is used to create remote fetch configuration object from POST request.
     * First it creates three hash map from post request.
     * Then it set primitive attributes from post request remote fetch configuration.
     *
     * @param remoteFetchConfigurationPOSTRequest POST request.
     * @return RemoteFetchConfiguration.
     */
    private RemoteFetchConfiguration createRemoteFetchConfiguration
    (RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest) {

        RemoteFetchConfiguration remoteFetchConfiguration = new RemoteFetchConfiguration();
        Map<String, String> repositoryManagerAttributes =
                createRepositoryManagerProperties(remoteFetchConfigurationPOSTRequest);
        Map<String, String> actionListenerAttributes =
                createActionListenerProperties(remoteFetchConfigurationPOSTRequest);
        Map<String, String> configurationDeployerAttributes = Collections.emptyMap();

        setIfNotNull(remoteFetchConfigurationPOSTRequest.getIsEnabled(), remoteFetchConfiguration::setEnabled);
        setIfNotNull(remoteFetchConfigurationPOSTRequest.getRemoteFetchName(),
                remoteFetchConfiguration::setRemoteFetchName);

        remoteFetchConfiguration.setTenantId(IdentityTenantUtil
                .getTenantId(ContextLoader.getTenantDomainFromContext()));

        remoteFetchConfiguration.setConfigurationDeployerType(remoteFetchConfigurationPOSTRequest
                .getConfigurationDeployer().getType().name());
        remoteFetchConfiguration.setActionListenerType(remoteFetchConfigurationPOSTRequest
                .getActionListener().getType().name());
        remoteFetchConfiguration.setRepositoryManagerType(remoteFetchConfigurationPOSTRequest
                .getRepositoryManager().getType().name());
        remoteFetchConfiguration.setActionListenerAttributes(actionListenerAttributes);
        remoteFetchConfiguration.setRepositoryManagerAttributes(repositoryManagerAttributes);
        remoteFetchConfiguration.setConfigurationDeployerAttributes(configurationDeployerAttributes);
        return remoteFetchConfiguration;
    }

    /**
     * This method is used to create action listener hash map from POST request.
     *
     * @param remoteFetchConfigurationPOSTRequest RemoteFetchConfigurationPOSTRequest/
     * @return Properties.
     */
    private Map<String, String> createActionListenerProperties
    (RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest) {

        Map<String, String> properties = new HashMap<>();
        if (remoteFetchConfigurationPOSTRequest.getActionListener() != null) {
            ActionListenerAttributes actionListenerAttributes =
                    remoteFetchConfigurationPOSTRequest.getActionListener().getAttributes();
            if (!StringUtils.isEmpty(actionListenerAttributes.getFrequency())) {
                properties.put(FREQUENCY, actionListenerAttributes.getFrequency());
            }
        }
        return properties;
    }

    /**
     * This method is used to create Repository manager hash map from post request.
     *
     * @param remoteFetchConfigurationPOSTRequest POST request.
     * @return Properties.
     */
    private Map<String, String> createRepositoryManagerProperties
    (RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest) {

        Map<String, String> properties = new HashMap<>();
        if (remoteFetchConfigurationPOSTRequest.getRepositoryManager() != null) {
            RepositoryManagerAttributes repositoryManagerAttributes =
                    remoteFetchConfigurationPOSTRequest.getRepositoryManager().getAttributes();
            if (!StringUtils.isEmpty(repositoryManagerAttributes.getAccessToken())) {
                properties.put(ACCESS_TOKEN,
                        repositoryManagerAttributes.getAccessToken());
            }
            if (!StringUtils.isEmpty(repositoryManagerAttributes.getBranch())) {
                properties.put(BRANCH, repositoryManagerAttributes.getBranch());
            }
            if (!StringUtils.isEmpty(repositoryManagerAttributes.getDirectory())) {
                properties.put(DIRECTORY, repositoryManagerAttributes.getDirectory());
            }
            if (!StringUtils.isEmpty(repositoryManagerAttributes.getUri())) {
                properties.put(URI, repositoryManagerAttributes.getUri());
            }
            if (!StringUtils.isEmpty(repositoryManagerAttributes.getUsername())) {
                properties.put(USER_NAME, repositoryManagerAttributes.getUsername());
            }
        }
        return properties;
    }

    /**
     * This method is used to create GET response from remote fetch configuration id.
     *
     * @param remoteFetchConfiguration remote fetch configuration domain object.
     * @return RemoteFetchConfigurationGetResponse.
     * @throws RemoteFetchCoreException RemoteFetchCoreException
     */
    private RemoteFetchConfigurationGetResponse createRemoteFetchConfigurationResponse
    (RemoteFetchConfiguration remoteFetchConfiguration) throws RemoteFetchCoreException {

        RemoteFetchConfigurationGetResponse remoteFetchConfigurationGetResponse
                = new RemoteFetchConfigurationGetResponse();
        ActionListenerAttributes actionListenerAttributes = createActionListenerAttributeProperties
                (remoteFetchConfiguration);
        RepositoryManagerAttributes repositoryManagerAttributes = createRepositoryManagerAttributeProperties
                (remoteFetchConfiguration);
        remoteFetchConfigurationGetResponse.setActionListenerAttributes(actionListenerAttributes);
        remoteFetchConfigurationGetResponse.setRepositoryManagerAttributes(repositoryManagerAttributes);
        remoteFetchConfigurationGetResponse.setConfigurationDeployerAttributes(null);
        setIfNotNull(remoteFetchConfiguration.getRemoteFetchConfigurationId(),
                remoteFetchConfigurationGetResponse::setId);
        setIfNotNull(remoteFetchConfiguration.isEnabled(),
                remoteFetchConfigurationGetResponse::setIsEnabled);
        setIfNotNull(remoteFetchConfiguration.getRemoteFetchName(),
                remoteFetchConfigurationGetResponse::setRemoteFetchName);
        setIfNotNull(remoteFetchConfiguration.getRepositoryManagerType(),
                remoteFetchConfigurationGetResponse::setRepositoryManagerType);
        setIfNotNull(remoteFetchConfiguration.getConfigurationDeployerType(),
                remoteFetchConfigurationGetResponse::setConfigurationDeployerType);
        setIfNotNull(remoteFetchConfiguration.getRepositoryManagerType(),
                remoteFetchConfigurationGetResponse::setRepositoryManagerType);
        StatusListResponse statusListResponse = this.createStatusListResponse(remoteFetchConfigurationService
                .getDeploymentRevisions(remoteFetchConfiguration.getRemoteFetchConfigurationId()));
        remoteFetchConfigurationGetResponse.setStatus(statusListResponse);
        return remoteFetchConfigurationGetResponse;
    }

    /**
     * This method used to create Action Listener attributes from domain object.
     *
     * @param remoteFetchConfiguration RemoteFetchConfiguration.
     * @return ActionListenerAttributes.
     */
    private ActionListenerAttributes createActionListenerAttributeProperties
    (RemoteFetchConfiguration remoteFetchConfiguration) {

        ActionListenerAttributes actionListenerAttributes = new ActionListenerAttributes();
        setIfNotNull(remoteFetchConfiguration
                        .getActionListenerAttributes().get(FREQUENCY),
                actionListenerAttributes::setFrequency);
        return actionListenerAttributes;
    }

    /**
     * This method is used to create Repository Manager Attributes from domain object.
     *
     * @param remoteFetchConfiguration RemoteFetchConfiguration.
     * @return RepositoryManagerAttributes
     */
    private RepositoryManagerAttributes createRepositoryManagerAttributeProperties
    (RemoteFetchConfiguration remoteFetchConfiguration) {

        RepositoryManagerAttributes repositoryManagerAttributes = new RepositoryManagerAttributes();
        setIfNotNull((remoteFetchConfiguration
                        .getRepositoryManagerAttributes().get(ACCESS_TOKEN)),
                repositoryManagerAttributes::setAccessToken);
        setIfNotNull(remoteFetchConfiguration
                        .getRepositoryManagerAttributes().get(BRANCH),
                repositoryManagerAttributes::setBranch);
        setIfNotNull(remoteFetchConfiguration
                        .getRepositoryManagerAttributes().get(RemoteFetchConfigurationConstants.DIRECTORY),
                repositoryManagerAttributes::setDirectory);
        setIfNotNull(remoteFetchConfiguration
                        .getRepositoryManagerAttributes().get(URI),
                repositoryManagerAttributes::setUri);
        setIfNotNull(remoteFetchConfiguration
                        .getRepositoryManagerAttributes().get(USER_NAME),
                repositoryManagerAttributes::setUsername);
        return repositoryManagerAttributes;
    }

    /**
     * This method is used to create list response from basic remote fetch configuration.
     * This method is create list item from basic remote fetch configuration list items.
     *
     * @param basicRemoteFetchConfigurationList List response.
     * @return RemoteFetchConfigurationListResponse.
     */
    private RemoteFetchConfigurationListResponse createRemoteFetchConfigurationListResponse
    (List<BasicRemoteFetchConfiguration> basicRemoteFetchConfigurationList) {

        RemoteFetchConfigurationListResponse remoteFetchConfigurationListResponse =
                new RemoteFetchConfigurationListResponse();
        if (CollectionUtils.isNotEmpty(basicRemoteFetchConfigurationList)) {
            List<RemoteFetchConfigurationListItem> remoteFetchConfigurations = new ArrayList<>();
            for (BasicRemoteFetchConfiguration basicRemoteFetchConfiguration : basicRemoteFetchConfigurationList) {
                RemoteFetchConfigurationListItem remoteFetchConfigurationListItem =
                        populateRemoteFetchConfigurationListResponse(basicRemoteFetchConfiguration);
                remoteFetchConfigurations.add(remoteFetchConfigurationListItem);
            }
            remoteFetchConfigurationListResponse.setRemotefetchConfigurations(remoteFetchConfigurations);
            remoteFetchConfigurationListResponse.setCount(remoteFetchConfigurations.size());
        } else {
            remoteFetchConfigurationListResponse.setCount(0);
        }
        return remoteFetchConfigurationListResponse;
    }

    /**
     * This method is used to populate remote fetch configuration list item from basic remote fetch configuration.
     *
     * @param basicRemoteFetchConfiguration basic remote fetch configuration.
     * @return RemoteFetchConfigurationListItem.
     */
    private RemoteFetchConfigurationListItem populateRemoteFetchConfigurationListResponse
    (BasicRemoteFetchConfiguration basicRemoteFetchConfiguration) {

        RemoteFetchConfigurationListItem remoteFetchConfigurationListItem = new RemoteFetchConfigurationListItem();
        setIfNotNull(basicRemoteFetchConfiguration.getId(), remoteFetchConfigurationListItem::setId);
        setIfNotNull(basicRemoteFetchConfiguration.isEnabled(), remoteFetchConfigurationListItem::setIsEnabled);
        setIfNotNull(basicRemoteFetchConfiguration.getActionListenerType(),
                remoteFetchConfigurationListItem::setActionListenerType);
        setIfNotNull(basicRemoteFetchConfiguration.getConfigurationDeployerType(),
                remoteFetchConfigurationListItem::setConfigurationDeployerType);
        setIfNotNull(basicRemoteFetchConfiguration.getRepositoryManagerType(),
                remoteFetchConfigurationListItem::setRepositoryManagerType);
        setIfNotNull(basicRemoteFetchConfiguration.getRemoteFetchName(),
                remoteFetchConfigurationListItem::setName);
        if (basicRemoteFetchConfiguration.getLastDeployed() == null) {
            remoteFetchConfigurationListItem.setLastDeployed(null);
        } else {
            convertDateToStringIfNotNull(basicRemoteFetchConfiguration.getLastDeployed(),
                    remoteFetchConfigurationListItem::setLastDeployed);
        }
        remoteFetchConfigurationListItem.setFailedDeployments(basicRemoteFetchConfiguration.getFailedDeployments());
        remoteFetchConfigurationListItem.setSuccessfulDeployments(basicRemoteFetchConfiguration.
                getSuccessfulDeployments());
        return remoteFetchConfigurationListItem;
    }

    /**
     * This method is used to handle web hook.
     *
     * @param pushEventWebHookPOSTRequest WebHook Post request
     */
    public void handleWebHook(PushEventWebHookPOSTRequest pushEventWebHookPOSTRequest) {

        if (log.isDebugEnabled()) {
            String repoUrl = (pushEventWebHookPOSTRequest.getRepository() != null ? 
                    pushEventWebHookPOSTRequest.getRepository().getCloneUrl() : "null");
            log.debug("Handling webhook request for repository: " + repoUrl);
        }
        try {
            validateWebHookRequest(pushEventWebHookPOSTRequest);
            String cloneURL = pushEventWebHookPOSTRequest.getRepository().getCloneUrl();
            String branch = populateBranch(pushEventWebHookPOSTRequest.getRef());
            List<String> modifiedFiles = extractAddedAndModifiedFiles(pushEventWebHookPOSTRequest.getCommits());
            if (log.isDebugEnabled()) {
                log.debug("Processing webhook for repository: " + cloneURL + ", branch: " + branch + 
                        ", modified files count: " + modifiedFiles.size());
            }
            remoteFetchConfigurationService.handleWebHook(cloneURL, branch, modifiedFiles);
            log.info("Successfully processed webhook for repository: " + cloneURL + ", branch: " + branch);
        } catch (RemoteFetchCoreException e) {
            log.error("Failed to handle webhook request", e);
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, RemoteFetchConfigurationConstants.ErrorMessage
                    .ERROR_CODE_ERROR_WEB_HOOK_REMOTE_FETCH, null);
        }
    }

    /**
     * Method used to validate GitHub Web hook request.
     *
     * @param pushEventWebHookPOSTRequest pushEventWebHookPOSTRequest.
     */
    private void validateWebHookRequest(PushEventWebHookPOSTRequest pushEventWebHookPOSTRequest) {

        if ((pushEventWebHookPOSTRequest.getRef() == null) || (pushEventWebHookPOSTRequest.getRepository() == null) ||
                (pushEventWebHookPOSTRequest.getCommits() == null)) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, WEBHOOK_REQUEST);
        }
        if (StringUtils.isBlank(pushEventWebHookPOSTRequest.getRepository().getCloneUrl())) {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_INVALID_RE_CONFIG_INPUT, WEBHOOK_REQUEST);
        }
    }

    private String populateBranch(String ref) {

        String[] splitCredentials = ref.split("/", 3);
        return splitCredentials[2];
    }

    /**
     * This method is used to extract added or modified file names from the commit objects.
     *
     * @param commits List of commits
     * @return List of FIle names
     */
    private List<String> extractAddedAndModifiedFiles(List<PushEventWebHookPOSTRequestCommits> commits) {

        List<String> fileNames = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(commits)) {
            for (PushEventWebHookPOSTRequestCommits commit : commits) {
                fileNames.addAll(commit.getAdded());
                fileNames.addAll(commit.getModified());
            }
            return fileNames;
        } else {
            throw handleException(Response.Status.BAD_REQUEST, RemoteFetchConfigurationConstants.
                    ErrorMessage.ERROR_CODE_COMMIT_NOT_FOUND, null);
        }
    }

    /**
     * This method is used to handle remote fetch core exception and create API error wit suitable response code and
     * status by checking its instance type.
     *
     * @param e         RemoteFetchCoreException.
     * @param errorEnum RemoteFetchConfigurationConstants.ErrorMessage
     * @param data      data
     * @return APIError
     */
    private APIError handleRemoteFetchConfigurationException(RemoteFetchCoreException e,
                                                             RemoteFetchConfigurationConstants.ErrorMessage errorEnum,
                                                             String data) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (e instanceof RemoteFetchClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(RemoteFetchConfigurationConstants.ERROR_CODE_DELIMITER) ?
                                errorCode : RemoteFetchConfigurationConstants.
                                REMOTE_FETCH_CONFIGURATION_MANAGEMENT_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof RemoteFetchServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(RemoteFetchConfigurationConstants.ERROR_CODE_DELIMITER) ?
                                errorCode : RemoteFetchConfigurationConstants.
                                REMOTE_FETCH_CONFIGURATION_MANAGEMENT_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * From the error message and the data this method returns error builder.
     *
     * @param errorMsg error message.
     * @param data     data.
     * @return ErrorResponse.Builder
     */
    private ErrorResponse.Builder getErrorBuilder(RemoteFetchConfigurationConstants.ErrorMessage errorMsg,
                                                  String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Constant.ErrorMessage.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(RemoteFetchConfigurationConstants.ErrorMessage error,
                                      String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getDescription(), data);
        } else {
            message = error.getDescription();
        }
        return message;
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, RemoteFetchConfigurationConstants.ErrorMessage error,
                                     String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Clone given remote fetch configuration object.
     *
     * @param id                       remote fetch configuration id.
     * @param remoteFetchConfiguration remote fetch configuration object which need to be cloned.
     * @return RemoteFetchConfiguration.
     */
    private RemoteFetchConfiguration deepCopyRemoteFetchConfiguration
    (String id, RemoteFetchConfiguration remoteFetchConfiguration) {

        try {
            return (RemoteFetchConfiguration) BeanUtils.cloneBean(remoteFetchConfiguration);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
                e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, RemoteFetchConfigurationConstants.ErrorMessage
                    .ERROR_CODE_ERROR_UPDATING_RF_CONFIG, id);
        }
    }
}
