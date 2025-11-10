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

package org.wso2.carbon.identity.api.server.vc.config.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.Metadata;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfiguration;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationCreationModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationList;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationListItem;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationUpdateModel;
import org.wso2.carbon.identity.vc.config.management.VCCredentialConfigManager;
import org.wso2.carbon.identity.vc.config.management.exception.VCConfigMgtClientException;
import org.wso2.carbon.identity.vc.config.management.exception.VCConfigMgtException;
import org.wso2.carbon.identity.vc.config.management.exception.VCConfigMgtServerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

/**
 * Server Verifiable Credential Configuration management service.
 */
public class ServerVCCredentialConfigManagementService {

    private static final Log LOG = LogFactory.getLog(ServerVCCredentialConfigManagementService.class);
    private final VCCredentialConfigManager vcCredentialConfigManager;

    public ServerVCCredentialConfigManagementService(VCCredentialConfigManager vcCredentialConfigManager) {

        this.vcCredentialConfigManager = vcCredentialConfigManager;
    }

    /**
     * Add a new VC credential configuration.
     *
     * @param creationModel API request payload.
     * @return Created credential configuration.
     */
    public VCCredentialConfiguration addVCCredentialConfiguration(
            VCCredentialConfigurationCreationModel creationModel) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration config =
                    toInternalModel(creationModel);
            org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration created =
                    vcCredentialConfigManager.add(config, tenantDomain);
            return toApiModel(created);
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while adding VC credential configuration", null);
        }
    }

    /**
     * Delete a VC credential configuration by identifier.
     *
     * @param configId Configuration identifier.
     */
    public void deleteVCCredentialConfiguration(String configId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            vcCredentialConfigManager.delete(configId, tenantDomain);
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while deleting VC credential configuration", configId);
        }
    }

    /**
     * Retrieve a VC credential configuration.
     *
     * @param configId Configuration identifier.
     * @return Credential configuration.
     */
    public VCCredentialConfiguration getVCCredentialConfiguration(String configId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration configuration =
                    vcCredentialConfigManager.get(configId, tenantDomain);
            if (configuration == null) {
                throw notFound(configId);
            }
            return toApiModel(configuration);
        } catch (VCConfigMgtException e) {
             throw handleVCConfigException(e, "Error while retrieving VC credential configuration", configId);
         }
     }

    /**
     * List VC credential configurations for the logged-in tenant.
     *
     * @return List of credential configurations.
     */
    public VCCredentialConfigurationList listVCCredentialConfigurations() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            List<org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration> configurations =
                    vcCredentialConfigManager.list(tenantDomain);

            VCCredentialConfigurationList result = new VCCredentialConfigurationList();
            if (configurations == null || configurations.isEmpty()) {
                result.setTotalResults(0);
                result.setVcCredentialConfigurations(new ArrayList<>());
                return result;
            }

            List<VCCredentialConfigurationListItem> items = configurations.stream()
                    .filter(Objects::nonNull)
                    .map(this::toApiListItem)
                    .collect(Collectors.toList());

            result.setTotalResults(items.size());
            result.setVcCredentialConfigurations(items);
            return result;
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while listing VC credential configurations", null);
        }
    }

    /**
     * Update an existing VC credential configuration.
     *
     * @param configId     Configuration identifier.
     * @param updateModel  Update payload.
     * @return Updated credential configuration.
     */
    public VCCredentialConfiguration updateVCCredentialConfiguration(String configId,
                                                                     VCCredentialConfigurationUpdateModel updateModel) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration toUpdate =
                    toInternalModel(updateModel);
            org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration updated =
                    vcCredentialConfigManager.update(configId, toUpdate, tenantDomain);
            return toApiModel(updated);
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while updating VC credential configuration", configId);
        }
    }

    private VCCredentialConfiguration toApiModel(
            org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration model) {

        if (model == null) {
            return null;
        }

        VCCredentialConfiguration apiModel = new VCCredentialConfiguration();
        if (StringUtils.isNotBlank(model.getId())) {
            // The generated API model expects id as String.
            apiModel.setId(model.getId());
        }
        apiModel.setIdentifier(model.getIdentifier());
        apiModel.setDisplayName(model.getDisplayName());
        apiModel.setScope(model.getScope());
        apiModel.setFormat(model.getFormat());
        apiModel.setType(model.getType());
        apiModel.setMetadata(toApiCredentialMetadata(model.getMetadata()));

        List<String> claims = model.getClaims();
        if (claims != null) {
            apiModel.setClaims(claims);
        } else {
            apiModel.setClaims(new ArrayList<>());
        }

        apiModel.setExpiresIn(model.getExpiresIn());
        return apiModel;
    }

    private Metadata toApiCredentialMetadata(
            org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration.Metadata metadata) {

        if (metadata == null) {
            return null;
        }
        Metadata apiMetadata = new Metadata();
        apiMetadata.setDisplay(metadata.getDisplay());
        return apiMetadata;
    }

    private org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration toInternalModel(
            VCCredentialConfigurationCreationModel model) {

        org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration internalModel =
                new org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration();
        internalModel.setIdentifier(model.getIdentifier());
        internalModel.setDisplayName(model.getDisplayName());
        internalModel.setScope(model.getScope());
        internalModel.setFormat(model.getFormat());
        internalModel.setType(model.getType());
        internalModel.setMetadata(toInternalCredentialMetadata(model.getMetadata()));
        if (model.getClaims() != null) {
            internalModel.setClaims(model.getClaims());
        }
        internalModel.setExpiresIn(model.getExpiresIn());
        return internalModel;
    }

    private org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration toInternalModel(
            VCCredentialConfigurationUpdateModel model) {

        return toInternalModel((VCCredentialConfigurationCreationModel) model);
    }

    private org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration.Metadata
    toInternalCredentialMetadata(Metadata metadata) {

        if (metadata == null) {
            return null;
        }
        org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration.Metadata
                internalMetadata =
                new org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration.Metadata();
        internalMetadata.setDisplay(metadata.getDisplay());
        return internalMetadata;
    }

    private APIError notFound(String data) {

        String message = "VC credential configuration not found";
        ErrorResponse error = new ErrorResponse.Builder()
                .withCode("VC-60001")
                .withMessage("Resource not found")
                .withDescription(includeData(message, data))
                .build(LOG, message);
        return new APIError(Response.Status.NOT_FOUND, error);
    }

    private APIError handleVCConfigException(VCConfigMgtException exception, String defaultDescription, String data) {

        ErrorResponse errorResponse;
        Response.Status status;
        String description = StringUtils.isNotBlank(exception.getMessage()) ?
                exception.getMessage() : defaultDescription;

        if (exception instanceof VCConfigMgtClientException) {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(prefixCode(exception.getCode()))
                    .withMessage("Invalid request or data")
                    .withDescription(includeData(description, data))
                    .build(LOG, exception.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (exception instanceof VCConfigMgtServerException) {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(prefixCode(exception.getCode()))
                    .withMessage("Server error")
                    .withDescription(includeData(description, data))
                    .build(LOG, exception, description);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = new ErrorResponse.Builder()
                    .withCode("VC-65000")
                    .withMessage("Unexpected error")
                    .withDescription(includeData(defaultDescription, data))
                    .build(LOG, exception, defaultDescription);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private String prefixCode(String code) {

        if (StringUtils.isBlank(code)) {
            return "VC-00000";
        }
        if (code.contains(Constants.ERROR_CODE_DELIMITER)) {
            return code;
        }
        return "VC-" + code;
    }

    private String includeData(String message, String data) {

        if (StringUtils.isNotBlank(data)) {
            return message + ": " + data;
        }
        return message;
    }

    private VCCredentialConfigurationListItem toApiListItem(
            org.wso2.carbon.identity.vc.config.management.model.VCCredentialConfiguration model) {

        if (model == null) {
            return null;
        }
        VCCredentialConfigurationListItem item = new VCCredentialConfigurationListItem();
        item.setId(model.getId());
        item.setIdentifier(model.getIdentifier());
        item.setDisplayName(model.getDisplayName());
        item.setScope(model.getScope());
        return item;
    }
}
