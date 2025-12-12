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
import org.wso2.carbon.identity.api.server.vc.config.management.v1.PaginationLink;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfiguration;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationCreationModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationList;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationListItem;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationUpdateModel;
import org.wso2.carbon.identity.openid4vc.config.management.VCCredentialConfigManager;
import org.wso2.carbon.identity.openid4vc.config.management.exception.VCConfigMgtClientException;
import org.wso2.carbon.identity.openid4vc.config.management.exception.VCConfigMgtException;
import org.wso2.carbon.identity.openid4vc.config.management.exception.VCConfigMgtServerException;
import org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfigSearchResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.vc.config.management.common.VCCredentialConfigManagementConstants.ASC_SORT_ORDER;
import static org.wso2.carbon.identity.api.server.vc.config.management.common.VCCredentialConfigManagementConstants.DEFAULT_LIMIT;
import static org.wso2.carbon.identity.api.server.vc.config.management.common.VCCredentialConfigManagementConstants.DESC_SORT_ORDER;
import static org.wso2.carbon.identity.api.server.vc.config.management.common.VCCredentialConfigManagementConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.vc.config.management.common.VCCredentialConfigManagementConstants.VC_CREDENTIAL_CONFIG_PATH_COMPONENT;

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
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding VC credential configuration for tenant: " + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration config =
                    toInternalModel(creationModel);
            org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration created =
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting VC credential configuration: " + configId + " for tenant: " + tenantDomain);
        }
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving VC credential configuration: " + configId + " for tenant: " + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration configuration =
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
    public VCCredentialConfigurationList listVCCredentialConfigurations(String before, String after, String filter,
                                                                        Integer limit, String attributes) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Listing VC credential configurations for tenant: " + tenantDomain);
        }

        VCCredentialConfigurationList result = new VCCredentialConfigurationList();

        try {
            // Set default values if the parameters are not set.
            limit = validatedLimit(limit);

            // Validate before and after parameters.
            if (StringUtils.isNotBlank(before) && StringUtils.isNotBlank(after)) {
                throw handleVCConfigException(
                        new VCConfigMgtClientException("60003",
                                "Both 'before' and 'after' parameters cannot be specified together"),
                        "Invalid pagination parameters", "before: " + before + ", after: " + after);
            }

            // Set the pagination sort order.
            String paginationSortOrder = StringUtils.isNotBlank(before) ? DESC_SORT_ORDER : ASC_SORT_ORDER;

            VCCredentialConfigSearchResult searchResult =
                    vcCredentialConfigManager.listWithPagination(after, before, limit + 1, filter,
                            paginationSortOrder, tenantDomain);

            List<org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration> configurations =
                    searchResult.getConfigurations();

            if (configurations != null && !configurations.isEmpty()) {
                boolean hasMoreItems = configurations.size() > limit;
                boolean needsReverse = StringUtils.isNotBlank(before);
                boolean isFirstPage = (StringUtils.isBlank(before) && StringUtils.isBlank(after)) ||
                        (StringUtils.isNotBlank(before) && !hasMoreItems);
                boolean isLastPage = !hasMoreItems && (StringUtils.isNotBlank(after) || StringUtils.isBlank(before));

                String url = "?limit=" + limit;

                if (StringUtils.isNotBlank(filter)) {
                    try {
                        url += "&filter=" + URLEncoder.encode(filter, StandardCharsets.UTF_8.name());
                    } catch (UnsupportedEncodingException e) {
                        LOG.error("Server encountered an error while building pagination URL for the response.", e);
                    }
                }

                if (hasMoreItems) {
                    configurations.remove(configurations.size() - 1);
                }
                if (needsReverse) {
                    Collections.reverse(configurations);
                }
                if (!isFirstPage) {
                    String encodedString = Base64.getEncoder().encodeToString(
                            configurations.get(0).getCursorKey().toString().getBytes(StandardCharsets.UTF_8));
                    result.addLinksItem(buildPaginationLink(url + "&before=" + encodedString, "previous"));
                }
                if (!isLastPage) {
                    String encodedString = Base64.getEncoder().encodeToString(configurations
                            .get(configurations.size() - 1).getCursorKey().toString().getBytes(StandardCharsets.UTF_8));
                    result.addLinksItem(buildPaginationLink(url + "&after=" + encodedString, "next"));
                }
            }
            if (configurations == null || configurations.isEmpty()) {
                result.setTotalResults(0);
                result.setVcCredentialConfigurations(new ArrayList<>());
                return result;
            }
            result.setTotalResults(searchResult.getTotalCount());
            result.setVcCredentialConfigurations(configurations.stream()
                    .filter(Objects::nonNull)
                    .map(this::toApiListItem)
                    .collect(Collectors.toList()));
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating VC credential configuration: " + configId + " for tenant: " + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration toUpdate =
                    toInternalModel(updateModel);
            org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration updated =
                    vcCredentialConfigManager.update(configId, toUpdate, tenantDomain);
            return toApiModel(updated);
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while updating VC credential configuration", configId);
        }
    }

    public VCCredentialConfiguration generateVCCredentialOffer(String configId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Generating VC credential offer for configuration: " + configId + " for tenant: "
                    + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration updated =
                    vcCredentialConfigManager.generateOffer(configId, tenantDomain);
            return toApiModel(updated);
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while updating VC credential configuration", configId);
        }
    }

    public VCCredentialConfiguration revokeVCCredentialOffer(String configId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Revoking VC credential offer for configuration: " + configId + " for tenant: "
                    + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration updated =
                    vcCredentialConfigManager.revokeOffer(configId, tenantDomain);
            return toApiModel(updated);
        } catch (VCConfigMgtException e) {
            throw handleVCConfigException(e, "Error while updating VC credential configuration", configId);
        }
    }

    private VCCredentialConfiguration toApiModel(
            org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration model) {

        if (model == null) {
            return null;
        }

        VCCredentialConfiguration apiModel = new VCCredentialConfiguration();
        if (StringUtils.isNotBlank(model.getId())) {
            apiModel.setId(model.getId());
        }
        apiModel.setIdentifier(model.getIdentifier());
        apiModel.setDisplayName(model.getDisplayName());
        apiModel.setFormat(model.getFormat());

        List<String> claims = model.getClaims();
        if (claims != null) {
            apiModel.setClaims(claims);
        } else {
            apiModel.setClaims(new ArrayList<>());
        }

        apiModel.setExpiresIn(model.getExpiresIn());
        if (StringUtils.isNotBlank(model.getOfferId())) {
            apiModel.setOfferId(model.getOfferId());
        }
        return apiModel;
    }

    private org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration toInternalModel(
            VCCredentialConfigurationCreationModel model) {

        org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration internalModel =
                new org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration();
        internalModel.setIdentifier(model.getIdentifier());
        internalModel.setDisplayName(model.getDisplayName());
        internalModel.setFormat(model.getFormat());
        if (model.getClaims() != null) {
            internalModel.setClaims(model.getClaims());
        }
        internalModel.setExpiresIn(model.getExpiresIn());
        return internalModel;
    }

    private org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration toInternalModel(
            VCCredentialConfigurationUpdateModel model) {

        org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration internalModel =
                new org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration();
        internalModel.setDisplayName(model.getDisplayName());
        internalModel.setFormat(model.getFormat());
        if (model.getClaims() != null) {
            internalModel.setClaims(model.getClaims());
        }
        internalModel.setExpiresIn(model.getExpiresIn());
        return internalModel;
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
            org.wso2.carbon.identity.openid4vc.config.management.model.VCCredentialConfiguration model) {

        if (model == null) {
            return null;
        }
        VCCredentialConfigurationListItem item = new VCCredentialConfigurationListItem();
        item.setId(model.getId());
        item.setIdentifier(model.getIdentifier());
        item.setDisplayName(model.getDisplayName());
        return item;
    }

    private Integer validatedLimit(Integer limit) {

        limit = limit == null ? DEFAULT_LIMIT : limit;
        if (limit <= 0) {
            throw handleVCConfigException(
                    new VCConfigMgtClientException("60002", "Limit must be a positive integer"),
                    "Invalid limit value", limit.toString());
        }
        return limit;
    }

    /**
     * Build pagination link.
     *
     * @param url URL with query parameters.
     * @param rel Relation type (previous or next).
     * @return Pagination link.
     */
    private PaginationLink buildPaginationLink(String url, String rel) {

        return new PaginationLink()
                .href(ContextLoader.buildURIForHeader(Constants.V1_API_PATH_COMPONENT + PATH_SEPARATOR +
                        VC_CREDENTIAL_CONFIG_PATH_COMPONENT + url).toString())
                .rel(rel);
    }
}
