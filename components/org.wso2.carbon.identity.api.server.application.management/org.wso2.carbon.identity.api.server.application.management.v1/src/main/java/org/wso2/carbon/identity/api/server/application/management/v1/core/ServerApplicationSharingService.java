/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.apache.commons.collections.CollectionUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.BasicOrganizationResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedApplicationResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedApplicationsResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.organization.management.application.model.SharedApplication;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementClientException;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementServerException;
import org.wso2.carbon.identity.organization.management.service.model.BasicOrganization;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForBody;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_SHARE_APPLICATION_EMPTY_REQUEST_BODY;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_SHARE_APPLICATION_REQUEST_BODY;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ORGANIZATION_PATH;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.getOrganizationId;
import static org.wso2.carbon.identity.organization.management.service.util.Utils.handleClientException;

/**
 * Calls internal osgi services to perform server application management related operations.
 */
public class ServerApplicationSharingService {

    private final OrgApplicationManager orgApplicationManager;

    public ServerApplicationSharingService(OrgApplicationManager orgApplicationManager) {

        this.orgApplicationManager = orgApplicationManager;
    }

    /**
     * Returns the shared applications list of a given primary application, along with their organizations.
     *
     * @param applicationId ID of the primary application.
     * @return shared applications list of a given primary application.
     */
    public Response getSharedApplications(String applicationId) {

        try {
            List<SharedApplication> sharedApplications = orgApplicationManager
                    .getSharedApplications(getOrganizationId(), applicationId);
            return Response.ok(createSharedApplicationsResponse(sharedApplications)).build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Stop application sharing to an organization by removing the fragment application from the given organization.
     *
     * @param applicationId        primary application ID.
     * @param sharedOrganizationId ID of the organization owning the fragment application.
     * @return the stop application sharing response.
     */
    public Response deleteSharedApplication(String applicationId, String sharedOrganizationId) {

        try {
            orgApplicationManager.deleteSharedApplication(getOrganizationId(), applicationId,
                    sharedOrganizationId);
            return Response.noContent().build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Returns the list of organization with whom the primary application is shared.
     *
     * @param applicationId ID of the primary application.
     * @return list of organization having the fragment applications.
     */
    public Response getApplicationSharedOrganizations(String applicationId) {

        try {
            List<BasicOrganization> basicOrganizations = orgApplicationManager
                    .getApplicationSharedOrganizations(getOrganizationId(), applicationId);
            return Response.ok(createSharedOrgResponse(basicOrganizations)).build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Share an application to child organizations.
     *
     * @param applicationId Application identifier.
     * @param requestBody   Request body of the share request.
     * @return The status of the operation.
     */
    public Response shareOrganizationApplication(String applicationId,
                                                 ApplicationSharePOSTRequest requestBody) {

        try {
            validateApplicationSharePostRequestBody(requestBody);
            boolean shareWithAllChildren = (requestBody.getShareWithAllChildren() != null)
                    ? requestBody.getShareWithAllChildren() : false;
            orgApplicationManager.shareOrganizationApplication(getOrganizationId(), applicationId,
                    shareWithAllChildren, requestBody.getSharedOrganizations());
            return Response.ok().build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    /**
     * Stop application sharing to all organizations by removing the fragment applications from the given organization.
     *
     * @param applicationId Main application ID.
     * @return The application unsharing response.
     */
    public Response deleteAllSharedApplications(String applicationId) {

        try {
            orgApplicationManager.deleteSharedApplication(getOrganizationId(), applicationId, null);
            return Response.noContent().build();
        } catch (OrganizationManagementClientException e) {
            throw Utils.buildClientError(e.getErrorCode(), e.getMessage(), e.getDescription());
        } catch (OrganizationManagementException e) {
            throw Utils.buildServerError(e.getErrorCode(), e.getMessage(), e.getDescription(), e);
        }
    }

    private SharedApplicationsResponse createSharedApplicationsResponse(List<SharedApplication> sharedApplications)
            throws OrganizationManagementServerException {

        SharedApplicationsResponse response = new SharedApplicationsResponse();
        for (SharedApplication sharedApp : sharedApplications) {
            SharedApplicationResponse sharedApplicationResponse =
                    new SharedApplicationResponse().applicationId(sharedApp.getSharedApplicationId())
                            .organizationId(sharedApp.getOrganizationId());
            response.addSharedApplicationsItem(sharedApplicationResponse);
        }
        return response;
    }

    private SharedOrganizationsResponse createSharedOrgResponse(List<BasicOrganization> organizations)
            throws OrganizationManagementServerException {

        SharedOrganizationsResponse response = new SharedOrganizationsResponse();
        for (BasicOrganization org : organizations) {
            BasicOrganizationResponse basicOrganizationResponse =
                    new BasicOrganizationResponse().id(org.getId()).name(org.getName())
                            .ref(buildOrganizationURL(org.getId()).toString());
            response.addOrganizationsItem(basicOrganizationResponse);
        }
        return response;
    }

    private void validateApplicationSharePostRequestBody(ApplicationSharePOSTRequest requestBody)
            throws OrganizationManagementClientException {

        if (requestBody == null) {
            throw handleClientException(ERROR_CODE_INVALID_SHARE_APPLICATION_EMPTY_REQUEST_BODY);
        }
        if (requestBody.getShareWithAllChildren() == null && requestBody.getSharedOrganizations() == null) {
            throw handleClientException(ERROR_CODE_INVALID_SHARE_APPLICATION_EMPTY_REQUEST_BODY);
        }
        if (requestBody.getShareWithAllChildren() != null
                && requestBody.getShareWithAllChildren()
                && CollectionUtils.isNotEmpty(requestBody.getSharedOrganizations())) {
            throw handleClientException(ERROR_CODE_INVALID_SHARE_APPLICATION_REQUEST_BODY);
        }
    }

    private static URI buildOrganizationURL(String organizationId) {

        return buildURIForBody(PATH_SEPARATOR + V1_API_PATH_COMPONENT + PATH_SEPARATOR + ORGANIZATION_PATH +
                PATH_SEPARATOR + organizationId);
    }
}
