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

package org.wso2.carbon.identity.api.server.organization.management.v1.impl;

import org.wso2.carbon.identity.api.server.organization.management.v1.OrganizationsApiService;
import org.wso2.carbon.identity.api.server.organization.management.v1.factories.OrganizationManagementServiceFactory;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryAttributes;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryCheckPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryPostRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationNameCheckPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPUTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPatchRequestItem;
import org.wso2.carbon.identity.api.server.organization.management.v1.service.OrganizationManagementService;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Service implementation of organization management API.
 */
public class OrganizationsApiServiceImpl implements OrganizationsApiService {

    private final OrganizationManagementService organizationManagementService;

    public OrganizationsApiServiceImpl() {

        try {
            this.organizationManagementService = OrganizationManagementServiceFactory
                    .getOrganizationManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating organization management service.", e);
        }
    }

    @Override
    public Response organizationsGet(String filter, Integer limit, String after, String before, Boolean recursive) {

        return organizationManagementService.getOrganizations(filter, limit, after, before, recursive);
    }

    @Override
    public Response organizationsOrganizationIdDelete(String organizationId) {

        return organizationManagementService.deleteOrganization(organizationId);
    }

    @Override
    public Response organizationsOrganizationIdDiscoveryDelete(String organizationId) {

        return organizationManagementService.deleteOrganizationDiscoveryAttributes(organizationId);
    }

    @Override
    public Response organizationsOrganizationIdDiscoveryPut(String organizationId, OrganizationDiscoveryAttributes
            organizationDiscoveryAttributes) {

        return organizationManagementService.updateOrganizationDiscoveryAttributes(organizationId,
                organizationDiscoveryAttributes);
    }

    @Override
    public Response organizationsOrganizationIdGet(String organizationId, Boolean includePermissions) {

        return organizationManagementService.getOrganization(organizationId, includePermissions);
    }

    @Override
    public Response organizationsOrganizationIdPatch(String organizationId, List<OrganizationPatchRequestItem>
            organizationPatchRequestItem) {

        return organizationManagementService.patchOrganization(organizationId, organizationPatchRequestItem);
    }

    @Override
    public Response organizationsOrganizationIdPut(String organizationId, OrganizationPUTRequest
            organizationPUTRequest) {

        return organizationManagementService.updateOrganization(organizationId, organizationPUTRequest);
    }

    @Override
    public Response organizationCheckDiscovery(
            OrganizationDiscoveryCheckPOSTRequest organizationDiscoveryCheckPOSTRequest) {

        return organizationManagementService.isDiscoveryAttributeAvailable(organizationDiscoveryCheckPOSTRequest);
    }

    @Override
    public Response organizationDiscoveryGet(String organizationId) {

        return organizationManagementService.getOrganizationDiscoveryAttributes(organizationId);
    }

    @Override
    public Response organizationDiscoveryPost(OrganizationDiscoveryPostRequest organizationDiscoveryPostRequest) {

        return organizationManagementService.addOrganizationDiscoveryAttributes(organizationDiscoveryPostRequest);
    }

    @Override
    public Response organizationMetadataGet() {

        return organizationManagementService.getOrganizationMetadata();
    }

    @Override
    public Response organizationsMetaAttributesGet(String filter, Integer limit, String after, String before,
                                                   Boolean recursive) {

        return organizationManagementService.getOrganizationsMetaAttributes(filter, limit, after, before, recursive);
    }

    @Override
    public Response organizationPost(OrganizationPOSTRequest organizationPOSTRequest) {

        return organizationManagementService.addOrganization(organizationPOSTRequest);
    }

    @Override
    public Response shareOrgApplication(String organizationId, String applicationId,
                                        ApplicationSharePOSTRequest applicationSharePOSTRequest) {

        return organizationManagementService.shareOrganizationApplication(organizationId, applicationId,
                applicationSharePOSTRequest);
    }

    @Deprecated
    @Override
    public Response shareOrgApplicationDelete(String organizationId, String applicationId,
                                              String sharedOrganizationId) {

        return organizationManagementService.deleteSharedApplication(organizationId, applicationId,
                sharedOrganizationId);
    }

    @Deprecated
    @Override
    public Response shareOrgApplicationGet(String organizationId, String applicationId) {

        return organizationManagementService.getApplicationSharedOrganizations(organizationId, applicationId);
    }

    @Deprecated
    @Override
    public Response sharedApplicationsAllDelete(String organizationId, String applicationId) {

        return organizationManagementService.deleteAllSharedApplications(organizationId, applicationId);
    }

    @Deprecated
    @Override
    public Response shareOrgApplicationDeleteAll(String organizationId, String applicationId) {

        return organizationManagementService.deleteAllSharedApplications(organizationId, applicationId);
    }

    @Deprecated
    @Override
    public Response sharedApplicationsGet(String organizationId, String applicationId) {

        return organizationManagementService.getSharedApplications(organizationId, applicationId);
    }

    @Override
    public Response organizationsCheckNamePost(OrganizationNameCheckPOSTRequest organizationNameCheckPOSTRequest) {

        return organizationManagementService.checkOrganizationName(organizationNameCheckPOSTRequest.getName());
    }

    @Override
    public Response organizationsDiscoveryGet(String filter, Integer offset, Integer limit) {

        return organizationManagementService.getOrganizationsDiscoveryAttributes(filter, offset, limit);
    }
}
