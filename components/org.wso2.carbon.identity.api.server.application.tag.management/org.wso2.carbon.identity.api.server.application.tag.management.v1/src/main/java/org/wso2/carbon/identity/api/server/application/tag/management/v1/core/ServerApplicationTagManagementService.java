/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.tag.management.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.application.tag.management.common.ApplicationTagManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.constants.ApplicationTagMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.constants.ApplicationTagMgtEndpointConstants.ErrorMessage;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagCreationResponse;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagModel;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagResponse;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagsListItem;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagsListResponse;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.util.ApplicationTagMgtEndpointUtil;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.application.common.model.ApplicationTag;
import org.wso2.carbon.identity.application.common.model.ApplicationTag.ApplicationTagBuilder;
import org.wso2.carbon.identity.application.common.model.ApplicationTagsItem;
import org.wso2.carbon.identity.application.tag.mgt.ApplicationTagMgtException;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.application.tag.management.v1.constants.ApplicationTagMgtEndpointConstants.APPLICATION_TAG_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Server Application Tag Management Service.
 */
public class ServerApplicationTagManagementService {

    private static final int DEFAULT_OFFSET = 0;
    private static final ServerApplicationTagManagementService instance = new ServerApplicationTagManagementService();
    private static final Log LOG = LogFactory.getLog(ServerApplicationTagManagementService.class);

    private ServerApplicationTagManagementService() {

    }

    public static ServerApplicationTagManagementService getInstance() {

        return instance;
    }

    /**
     * Add Application Tag.
     *
     * @param applicationTagCreationModel Application Tag creation model.
     * @return Created Application Tag ID.
     */
    public ApplicationTagCreationResponse addApplicationTag(ApplicationTagModel applicationTagCreationModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding Application Tag: " + applicationTagCreationModel.getName());
        }

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        ApplicationTagsItem createdTag = null;
        try {
            ApplicationTag applicationTag = createApplicationTag(applicationTagCreationModel);
            createdTag = ApplicationTagManagementServiceHolder.getApplicationTagManager()
                    .createApplicationTag(applicationTag, tenantDomain);
            if (createdTag == null) {
                LOG.error(ErrorMessage.ERROR_CODE_ADD_APPLICATION_TAG.getDescription());
                throw ApplicationTagMgtEndpointUtil.handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        ErrorMessage.ERROR_CODE_ADD_APPLICATION_TAG);
            }
            return buildApplicationTagCreationResponse(createdTag);
        } catch (ApplicationTagMgtException e) {
            throw ApplicationTagMgtEndpointUtil.handleApplicationTagMgtException(e);
        }
    }

    /**
     * Delete Application Tag.
     *
     * @param tagId     Application Tag ID.
     */
    public void deleteApplicationTag(String tagId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting Application Tag with ID: " + tagId);
        }
        try {
            ApplicationTagManagementServiceHolder.getApplicationTagManager().deleteApplicationTagById(tagId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (ApplicationTagMgtException e) {
            throw ApplicationTagMgtEndpointUtil.handleApplicationTagMgtException(e);
        }
    }

    /**
     * Get all Application Tags.
     *
     * @param offset    Offset for pagination.
     * @param limit     Limit for pagination.
     * @param filter    Filter String.
     * @return ApplicationTagsListResponse.
     */
    public ApplicationTagsListResponse getAllApplicationTags(Integer offset, Integer limit, String filter) {

        limit = validateAndGetLimit(limit);
        offset = validateAndGetOffset(offset);
        String tenantDomain = CarbonContext.getThreadLocalCarbonContext().getTenantDomain();

        try {
            int totalResults = ApplicationTagManagementServiceHolder.getApplicationTagManager()
                    .getCountOfApplicationTags(filter, tenantDomain);

            List<org.wso2.carbon.identity.application.common.model.ApplicationTagsListItem> filteredApplicationTags =
                    ApplicationTagManagementServiceHolder.getApplicationTagManager()
                    .getAllApplicationTags(tenantDomain, offset, limit, filter);
            int resultsInCurrentPage = filteredApplicationTags.size();

            return new ApplicationTagsListResponse()
                    .totalResults(totalResults)
                    .startIndex(offset + 1)
                    .count(resultsInCurrentPage)
                    .applicationTags(buildApplicationTagsList(filteredApplicationTags))
                    .links(Util.buildPaginationLinks(limit, offset, totalResults,
                                    APPLICATION_TAG_PATH_COMPONENT)
                            .entrySet()
                            .stream()
                            .map(link -> new Link().rel(link.getKey()).href(link.getValue()))
                            .collect(Collectors.toList()));
        } catch (ApplicationTagMgtException e) {
            throw ApplicationTagMgtEndpointUtil.handleApplicationTagMgtException(e);
        }
    }

    /**
     * Get an Application Tag.
     *
     * @param tagId    Application Tag ID.
     * @return ApplicationTagResponse.
     */
    public ApplicationTagResponse getApplicationTagById(String tagId) {

        try {
            ApplicationTagsItem applicationTag = ApplicationTagManagementServiceHolder.getApplicationTagManager()
                    .getApplicationTagById(tagId, CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
            if (applicationTag == null) {
                throw ApplicationTagMgtEndpointUtil.handleException(Response.Status.NOT_FOUND,
                        ApplicationTagMgtEndpointConstants.ErrorMessage.ERROR_CODE_APPLICATION_TAG_NOT_FOUND, tagId);
            }
            return buildApplicationTagResponse(applicationTag);
        } catch (ApplicationTagMgtException e) {
            throw ApplicationTagMgtEndpointUtil.handleApplicationTagMgtException(e);
        }
    }

    /**
     * Update an existing Application Tag.
     *
     * @param tagId                     Application Tag ID.
     * @param applicationTagPatch       Application Tag Patch Model.
     */
    public void patchApplicationTag(String tagId, ApplicationTagModel applicationTagPatch) {

        try {
            ApplicationTagResponse currentApplicationTag = getApplicationTagById(tagId);

            String tagName = applicationTagPatch.getName() == null ? currentApplicationTag.getName() :
                    applicationTagPatch.getName();
            String tagColour = applicationTagPatch.getColour() == null ? currentApplicationTag.getColour() :
                    applicationTagPatch.getColour();

            ApplicationTagBuilder appTagBuilder = new ApplicationTagBuilder()
                    .name(tagName)
                    .colour(tagColour);
            ApplicationTag applicationTag = appTagBuilder.build();
            ApplicationTagManagementServiceHolder.getApplicationTagManager().updateApplicationTag(applicationTag, tagId,
                    CarbonContext.getThreadLocalCarbonContext().getTenantDomain());
        } catch (ApplicationTagMgtException e) {
            throw ApplicationTagMgtEndpointUtil.handleApplicationTagMgtException(e);
        }
    }

    /**
     * Create Application Tag from the ApplicationTag.
     *
     * @param applicationTag Application Tag creation model.
     * @return Application Tag.
     */
    private ApplicationTag createApplicationTag(ApplicationTagModel applicationTag) {

        ApplicationTagMgtEndpointUtil.validateApplicationTag(applicationTag);
        ApplicationTagBuilder applicationTagBuilder = new ApplicationTagBuilder()
                .name(applicationTag.getName())
                .colour(applicationTag.getColour());
        return applicationTagBuilder.build();
    }

    /**
     * Build ApplicationTagsListItem from the ApplicationTagsItem.
     *
     * @param applicationTag Application Tag Item.
     * @return ApplicationTagsListItem.
     */
    private ApplicationTagResponse buildApplicationTagResponse(ApplicationTagsItem applicationTag) {

       return new ApplicationTagResponse()
                    .id(applicationTag.getId())
                    .name(applicationTag.getName())
                    .colour(applicationTag.getColour())
                    .associatedAppsCount(0);
    }

    /**
     * Build Application Tags List from the List<ApplicationTagsItem>.
     *
     * @param applicationTags Application Tags List.
     * @return List<ApplicationTagsListItem>.
     */
    private List<ApplicationTagsListItem> buildApplicationTagsList(
            List<org.wso2.carbon.identity.application.common.model.ApplicationTagsListItem> applicationTags) {

        List<ApplicationTagsListItem> applicationTagsList = new ArrayList<>();
        for (org.wso2.carbon.identity.application.common.model.ApplicationTagsListItem appTag: applicationTags) {
            applicationTagsList.add(new ApplicationTagsListItem()
                    .id(appTag.getId())
                    .name(appTag.getName())
                    .colour(appTag.getColour())
                    .associatedAppsCount(appTag.getAssociatedAppsCount())
                    .self(ContextLoader.buildURIForBody(V1_API_PATH_COMPONENT + APPLICATION_TAG_PATH_COMPONENT
                            + "/" + appTag.getId()).toString()));
        }
        return applicationTagsList;
    }

    /**
     * Build ApplicationTag Response from the ApplicationTagsItem.
     *
     * @param createdTag Created Application Tag.
     * @return ApplicationTagResponse.
     */
    private ApplicationTagCreationResponse buildApplicationTagCreationResponse(ApplicationTagsItem createdTag) {

        return new ApplicationTagCreationResponse()
                .id(createdTag.getId())
                .name(createdTag.getName())
                .colour(createdTag.getColour());
    }

    /**
     * Validate the value of the limit.
     *
     * @param limit Value of the limit.
     * @return Limit value after validation.
     */
    private int validateAndGetLimit(Integer limit) {

        final int maximumItemPerPage = IdentityUtil.getMaximumItemPerPage();
        if (limit != null && limit > 0 && limit <= maximumItemPerPage) {
            return limit;
        } else if (limit != null && limit > maximumItemPerPage) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Given limit exceeds the maximum limit. Therefore the configured default limit: "
                        + maximumItemPerPage + " is set as the limit.");
            }
            return maximumItemPerPage;
        } else {
            return IdentityUtil.getDefaultItemsPerPage();
        }
    }

    /**
     * Validate the value of the offset.
     *
     * @param offset Value of the offset.
     * @return Offset value after validation.
     */
    private int validateAndGetOffset(Integer offset) {

        if (offset != null && offset >= 0) {
            return offset;
        } else {
            return DEFAULT_OFFSET;
        }
    }
}
