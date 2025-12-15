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

package org.wso2.carbon.identity.api.server.vc.template.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.PaginationLink;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplate;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateCreationModel;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateList;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateListItem;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateUpdateModel;
import org.wso2.carbon.identity.openid4vc.template.management.VCTemplateManager;
import org.wso2.carbon.identity.openid4vc.template.management.exception.VCTemplateMgtClientException;
import org.wso2.carbon.identity.openid4vc.template.management.exception.VCTemplateMgtException;
import org.wso2.carbon.identity.openid4vc.template.management.exception.VCTemplateMgtServerException;
import org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplateSearchResult;

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

import static org.wso2.carbon.identity.api.server.vc.template.management.common.VCTemplateManagementConstants.ASC_SORT_ORDER;
import static org.wso2.carbon.identity.api.server.vc.template.management.common.VCTemplateManagementConstants.DEFAULT_LIMIT;
import static org.wso2.carbon.identity.api.server.vc.template.management.common.VCTemplateManagementConstants.DESC_SORT_ORDER;
import static org.wso2.carbon.identity.api.server.vc.template.management.common.VCTemplateManagementConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.vc.template.management.common.VCTemplateManagementConstants.VC_TEMPLATE_PATH_COMPONENT;

/**
 * Server Verifiable Credential Template management service.
 */
public class ServerVCTemplateManagementService {

    private static final Log LOG = LogFactory.getLog(ServerVCTemplateManagementService.class);
    private final VCTemplateManager vcTemplateManager;

    public ServerVCTemplateManagementService(VCTemplateManager vcTemplateManager) {

        this.vcTemplateManager = vcTemplateManager;
    }

    /**
     * Add a new VC template.
     *
     * @param creationModel API request payload.
     * @return Created template.
     */
    public VCTemplate addVCTemplate(VCTemplateCreationModel creationModel) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding VC template for tenant: " + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate template =
                    toInternalModel(creationModel);
            org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate created =
                    vcTemplateManager.add(template, tenantDomain);
            return toApiModel(created);
        } catch (VCTemplateMgtException e) {
            throw handleVCTemplateException(e, "Error while adding VC template", null);
        }
    }

    /**
     * Delete a VC template by identifier.
     *
     * @param templateId Template id.
     */
    public void deleteVCTemplate(String templateId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting VC template: " + templateId + " for tenant: " + tenantDomain);
        }
        try {
            vcTemplateManager.delete(templateId, tenantDomain);
        } catch (VCTemplateMgtException e) {
            throw handleVCTemplateException(e, "Error while deleting VC template", templateId);
        }
    }

    /**
     * Retrieve a VC template.
     *
     * @param templateId template identifier.
     * @return VC template.
     */
    public VCTemplate getVCTemplate(String templateId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving VC template: " + templateId + " for tenant: " + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate template =
                    vcTemplateManager.get(templateId, tenantDomain);
            if (template == null) {
                throw notFound(templateId);
            }
            return toApiModel(template);
        } catch (VCTemplateMgtException e) {
             throw handleVCTemplateException(e, "Error while retrieving VC template", templateId);
         }
     }

    /**
     * List VC templates for the tenant.
     *
     * @return List of templates.
     */
    public VCTemplateList listVCTemplates(String before, String after, String filter,
                                                                        Integer limit, String attributes) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Listing VC templates for tenant: " + tenantDomain);
        }

        VCTemplateList result = new VCTemplateList();

        try {
            // Set default values if the parameters are not set.
            limit = validatedLimit(limit);

            // Validate before and after parameters.
            if (StringUtils.isNotBlank(before) && StringUtils.isNotBlank(after)) {
                throw handleVCTemplateException(
                        new VCTemplateMgtClientException("60003",
                                "Both 'before' and 'after' parameters cannot be specified together"),
                        "Invalid pagination parameters", "before: " + before + ", after: " + after);
            }

            // Set the pagination sort order.
            String paginationSortOrder = StringUtils.isNotBlank(before) ? DESC_SORT_ORDER : ASC_SORT_ORDER;

            VCTemplateSearchResult searchResult =
                    vcTemplateManager.listWithPagination(after, before, limit + 1, filter,
                            paginationSortOrder, tenantDomain);

            List<org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate> templates =
                    searchResult.getTemplates();

            if (templates != null && !templates.isEmpty()) {
                boolean hasMoreItems = templates.size() > limit;
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
                    templates.remove(templates.size() - 1);
                }
                if (needsReverse) {
                    Collections.reverse(templates);
                }
                if (!isFirstPage) {
                    String encodedString = Base64.getEncoder().encodeToString(
                            templates.get(0).getCursorKey().toString().getBytes(StandardCharsets.UTF_8));
                    result.addLinksItem(buildPaginationLink(url + "&before=" + encodedString, "previous"));
                }
                if (!isLastPage) {
                    String encodedString = Base64.getEncoder().encodeToString(templates
                            .get(templates.size() - 1).getCursorKey().toString().getBytes(StandardCharsets.UTF_8));
                    result.addLinksItem(buildPaginationLink(url + "&after=" + encodedString, "next"));
                }
            }
            if (templates == null || templates.isEmpty()) {
                result.setTotalResults(0);
                result.setVcTemplates(new ArrayList<>());
                return result;
            }
            result.setTotalResults(searchResult.getTotalCount());
            result.setVcTemplates(templates.stream()
                    .filter(Objects::nonNull)
                    .map(this::toApiListItem)
                    .collect(Collectors.toList()));
            return result;
        } catch (VCTemplateMgtException e) {
            throw handleVCTemplateException(e, "Error while listing VC templates", null);
        }
    }

    /**
     * Update an existing VC template.
     *
     * @param templateId     Template ID.
     * @param updateModel  Update payload.
     * @return Updated template.
     */
    public VCTemplate updateVCTemplate(String templateId, VCTemplateUpdateModel updateModel) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating VC template: " + templateId + " for tenant: " + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate toUpdate =
                    toInternalModel(updateModel);
            org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate updated =
                    vcTemplateManager.update(templateId, toUpdate, tenantDomain);
            return toApiModel(updated);
        } catch (VCTemplateMgtException e) {
            throw handleVCTemplateException(e, "Error while updating VC template", templateId);
        }
    }

    public VCTemplate generateOffer(String templateId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Generating VC credential offer for template: " + templateId + " for tenant: "
                    + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate updated =
                    vcTemplateManager.generateOffer(templateId, tenantDomain);
            return toApiModel(updated);
        } catch (VCTemplateMgtException e) {
            throw handleVCTemplateException(e, "Error while updating VC template", templateId);
        }
    }

    public VCTemplate revokeOffer(String templateId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Revoking VC credential offer for template: " + templateId + " for tenant: "
                    + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate updated =
                    vcTemplateManager.revokeOffer(templateId, tenantDomain);
            return toApiModel(updated);
        } catch (VCTemplateMgtException e) {
            throw handleVCTemplateException(e, "Error while updating VC template", templateId);
        }
    }

    private VCTemplate toApiModel(
            org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate model) {

        if (model == null) {
            return null;
        }

        VCTemplate apiModel = new VCTemplate();
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

    private org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate toInternalModel(
            VCTemplateCreationModel model) {

        org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate internalModel =
                new org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate();
        internalModel.setIdentifier(model.getIdentifier());
        internalModel.setDisplayName(model.getDisplayName());
        internalModel.setFormat(model.getFormat());
        if (model.getClaims() != null) {
            internalModel.setClaims(model.getClaims());
        }
        internalModel.setExpiresIn(model.getExpiresIn());
        return internalModel;
    }

    private org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate toInternalModel(
            VCTemplateUpdateModel model) {

        org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate internalModel =
                new org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate();
        internalModel.setDisplayName(model.getDisplayName());
        internalModel.setFormat(model.getFormat());
        if (model.getClaims() != null) {
            internalModel.setClaims(model.getClaims());
        }
        internalModel.setExpiresIn(model.getExpiresIn());
        return internalModel;
    }

    private APIError notFound(String data) {

        String message = "VC template not found";
        ErrorResponse error = new ErrorResponse.Builder()
                .withCode("VC-60001")
                .withMessage("Resource not found")
                .withDescription(includeData(message, data))
                .build(LOG, message);
        return new APIError(Response.Status.NOT_FOUND, error);
    }

    private APIError handleVCTemplateException(VCTemplateMgtException exception, String defaultDescription,
                                               String data) {

        ErrorResponse errorResponse;
        Response.Status status;
        String description = StringUtils.isNotBlank(exception.getMessage()) ?
                exception.getMessage() : defaultDescription;

        if (exception instanceof VCTemplateMgtClientException) {
            errorResponse = new ErrorResponse.Builder()
                    .withCode(prefixCode(exception.getCode()))
                    .withMessage("Invalid request or data")
                    .withDescription(includeData(description, data))
                    .build(LOG, exception.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (exception instanceof VCTemplateMgtServerException) {
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

    private VCTemplateListItem toApiListItem(
            org.wso2.carbon.identity.openid4vc.template.management.model.VCTemplate model) {

        if (model == null) {
            return null;
        }
        VCTemplateListItem item = new VCTemplateListItem();
        item.setId(model.getId());
        item.setIdentifier(model.getIdentifier());
        item.setDisplayName(model.getDisplayName());
        return item;
    }

    private Integer validatedLimit(Integer limit) {

        limit = limit == null ? DEFAULT_LIMIT : limit;
        if (limit <= 0) {
            throw handleVCTemplateException(
                    new VCTemplateMgtClientException("60002", "Limit must be a positive integer"),
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
                        VC_TEMPLATE_PATH_COMPONENT + url).toString())
                .rel(rel);
    }
}
