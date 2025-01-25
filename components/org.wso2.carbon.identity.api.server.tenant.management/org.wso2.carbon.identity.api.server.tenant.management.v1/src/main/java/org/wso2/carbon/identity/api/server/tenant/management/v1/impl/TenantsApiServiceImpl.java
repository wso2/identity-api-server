/*
 * Copyright (c) 2020-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.tenant.management.v1.impl;

import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementConstants;
import org.wso2.carbon.identity.api.server.tenant.management.v1.TenantsApiService;
import org.wso2.carbon.identity.api.server.tenant.management.v1.core.ServerTenantManagementService;
import org.wso2.carbon.identity.api.server.tenant.management.v1.factories.ServerTenantManagementServiceFactory;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerPutModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.TenantPutModel;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * Implementation of TenantsApiService.
 */
public class TenantsApiServiceImpl implements TenantsApiService {

    private final ServerTenantManagementService tenantManagementService;

    public TenantsApiServiceImpl() {
        try {
            this.tenantManagementService = ServerTenantManagementServiceFactory.getServerTenantManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating ServerTenantManagementService.", e);
        }
    }

    @Override
    public Response addTenant(TenantModel tenantModel) {

        String resourceId = tenantManagementService.addTenant(tenantModel);
        return Response.created(getResourceLocation(resourceId)).build();
    }

    @Override
    public Response deleteTenantMetadata(String tenantId) {

        tenantManagementService.deleteTenantMetadata(tenantId);
        return Response.noContent().build();
    }

    @Override
    public Response getOwner(String tenantId, String ownerId, String additionalClaims) {

        return Response.ok().entity(tenantManagementService.getOwner(tenantId, ownerId, additionalClaims)).build();
    }

    @Override
    public Response getOwners(String tenantUniqueIdentifier) {

        return Response.ok().entity(tenantManagementService.getOwners(tenantUniqueIdentifier)).build();
    }

    @Override
    public Response getTenant(String tenantId) {

        return Response.ok().entity(tenantManagementService.getTenant(tenantId)).build();
    }

    @Override
    public Response retrieveTenants(Integer limit, Integer offset, String sortOrder, String sortBy, String filter) {

        return Response.ok().entity(tenantManagementService.listTenants(limit, offset, sortOrder, sortBy, filter)).
                build();
    }

    @Override
    public Response updateOwner(String tenantId, String ownerId, OwnerPutModel ownerPutModel) {

        tenantManagementService.updateOwner(tenantId, ownerId, ownerPutModel);
        return Response.ok().build();
    }

    @Override
    public Response updateTenantStatus(String tenantId, TenantPutModel tenantPutModel) {

        String resourceId = tenantManagementService.updateTenantStatus(tenantId, tenantPutModel);
        return Response.ok().entity(getResourceLocation(resourceId)).build();
    }

    private URI getResourceLocation(String resourceId) {

        return ContextLoader.buildURIForHeader(Constants.V1_API_PATH_COMPONENT +
                TenantManagementConstants.TENANT_MANAGEMENT_PATH_COMPONENT + "/" + resourceId);
    }

    @Override
    public Response getTenantByDomain(String tenantDomain) {

        return Response.ok().entity(tenantManagementService.getTenantByDomain(tenantDomain)).build();
    }

    @Override
    public Response isDomainExist(String tenantDomain) {

        if (tenantManagementService.isDomainAvailable(tenantDomain)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.status(Response.Status.OK).build();
        }
    }
}
