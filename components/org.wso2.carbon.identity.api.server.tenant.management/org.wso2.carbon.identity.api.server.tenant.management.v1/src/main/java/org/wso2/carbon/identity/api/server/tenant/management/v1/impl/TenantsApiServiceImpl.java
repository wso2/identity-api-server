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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.tenant.management.common.
        TenantManagementConstants;
import org.wso2.carbon.identity.api.server.tenant.management.v1.
        TenantsApiService;
import org.wso2.carbon.identity.api.server.tenant.management.v1.core.
        ServerTenantManagementService;
import org.wso2.carbon.identity.api.server.tenant.management.v1.factories.
        ServerTenantManagementServiceFactory;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.
        OwnerPutModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.
        TenantModel;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.
        TenantPutModel;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * Implementation of TenantsApiService.
 */
public class TenantsApiServiceImpl implements TenantsApiService {

    /**
     * Logger for TenantsApiServiceImpl.
     */
    private static final Log LOG = LogFactory.getLog(
            TenantsApiServiceImpl.class);

    /**
     * Server tenant management service.
     */
    private final ServerTenantManagementService tenantManagementService;

    /**
     * Constructor for TenantsApiServiceImpl.
     */
    public TenantsApiServiceImpl() {
        try {
            this.tenantManagementService = ServerTenantManagementServiceFactory
                    .getServerTenantManagementService();
            if (LOG.isDebugEnabled()) {
                LOG.debug("TenantsApiServiceImpl initialized successfully.");
            }
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating "
                    + "ServerTenantManagementService.", e);
            throw new RuntimeException("Error occurred while initiating "
                    + "ServerTenantManagementService.", e);
        }
    }

    /**
     * Add a new tenant.
     *
     * @param tenantModel Tenant model containing tenant details
     * @return Response with created tenant location
     */
    @Override
    public final Response addTenant(final TenantModel tenantModel) {

        if (LOG.isDebugEnabled()) {
            String domain = null;
            if (tenantModel != null) {
                domain = tenantModel.getDomain();
            }
            LOG.debug("Adding tenant with domain: " + domain);
        }
        String resourceId = tenantManagementService.addTenant(tenantModel);
        LOG.info("Tenant created successfully with resource ID: "
                + resourceId);
        return Response.created(getResourceLocation(resourceId)).build();
    }

    /**
     * Delete tenant metadata.
     *
     * @param tenantId Tenant identifier
     * @return Response with no content
     */
    @Override
    public final Response deleteTenantMetadata(final String tenantId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting tenant metadata for tenant ID: "
                    + tenantId);
        }
        tenantManagementService.deleteTenantMetadata(tenantId);
        LOG.info("Tenant metadata deleted successfully for tenant ID: "
                + tenantId);
        return Response.noContent().build();
    }

    /**
     * Get tenant owner details.
     *
     * @param tenantId Tenant identifier
     * @param ownerId Owner identifier
     * @param additionalClaims Additional claims to retrieve
     * @return Response with owner details
     */
    @Override
    public final Response getOwner(final String tenantId,
                                   final String ownerId,
                                   final String additionalClaims) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving owner details for tenant ID: "
                    + tenantId + ", owner ID: " + ownerId);
        }
        return Response.ok().entity(tenantManagementService.getOwner(
                tenantId, ownerId, additionalClaims)).build();
    }

    /**
     * Get all tenant owners.
     *
     * @param tenantUniqueIdentifier Tenant unique identifier
     * @return Response with list of owners
     */
    @Override
    public final Response getOwners(final String tenantUniqueIdentifier) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving owners for tenant ID: "
                    + tenantUniqueIdentifier);
        }
        return Response.ok().entity(tenantManagementService.getOwners(
                tenantUniqueIdentifier)).build();
    }

    /**
     * Get tenant details.
     *
     * @param tenantId Tenant identifier
     * @return Response with tenant details
     */
    @Override
    public final Response getTenant(final String tenantId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving tenant details for tenant ID: "
                    + tenantId);
        }
        return Response.ok().entity(tenantManagementService.getTenant(
                tenantId)).build();
    }

    /**
     * Retrieve list of tenants.
     *
     * @param limit Maximum number of results to return
     * @param offset Starting index for results
     * @param sortOrder Sort order (ASC/DESC)
     * @param sortBy Field to sort by
     * @param filter Filter criteria
     * @return Response with tenant list
     */
    @Override
    public final Response retrieveTenants(final Integer limit,
                                          final Integer offset,
                                          final String sortOrder,
                                          final String sortBy,
                                          final String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving tenants list with limit: " + limit
                    + ", offset: " + offset);
        }
        return Response.ok().entity(tenantManagementService.listTenants(
                limit, offset, sortOrder, sortBy, filter)).build();
    }

    /**
     * Update tenant owner.
     *
     * @param tenantId Tenant identifier
     * @param ownerId Owner identifier
     * @param ownerPutModel Owner update model
     * @return Response indicating success
     */
    @Override
    public final Response updateOwner(final String tenantId,
                                      final String ownerId,
                                      final OwnerPutModel ownerPutModel) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating owner for tenant ID: " + tenantId
                    + ", owner ID: " + ownerId);
        }
        tenantManagementService.updateOwner(tenantId, ownerId,
                ownerPutModel);
        LOG.info("Owner updated successfully for tenant ID: " + tenantId);
        return Response.ok().build();
    }

    /**
     * Update tenant status.
     *
     * @param tenantId Tenant identifier
     * @param tenantPutModel Tenant status update model
     * @return Response with updated resource location
     */
    @Override
    public final Response updateTenantStatus(final String tenantId,
            final TenantPutModel tenantPutModel) {

        boolean activated = false;
        if (tenantPutModel != null && tenantPutModel.getActivated()) {
            activated = true;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating tenant status for tenant ID: " + tenantId
                    + ", activated: " + activated);
        }
        String resourceId = tenantManagementService.updateTenantStatus(
                tenantId, tenantPutModel);
        LOG.info("Tenant status updated successfully for tenant ID: "
                + tenantId + ", activated: " + activated);
        return Response.ok().entity(getResourceLocation(resourceId)).build();
    }

    /**
     * Build resource location URI.
     *
     * @param resourceId Resource identifier
     * @return URI for the resource location
     */
    private URI getResourceLocation(final String resourceId) {

        return ContextLoader.buildURIForHeader(Constants.V1_API_PATH_COMPONENT
                + TenantManagementConstants.TENANT_MANAGEMENT_PATH_COMPONENT
                + "/" + resourceId);
    }

    /**
     * Get tenant by domain.
     *
     * @param tenantDomain Tenant domain
     * @return Response with tenant details
     */
    @Override
    public final Response getTenantByDomain(final String tenantDomain) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving tenant by domain: " + tenantDomain);
        }
        return Response.ok().entity(tenantManagementService
                .getTenantByDomain(tenantDomain)).build();
    }

    /**
     * Check if domain exists.
     *
     * @param tenantDomain Tenant domain to check
     * @return Response indicating domain existence
     */
    @Override
    public final Response isDomainExist(final String tenantDomain) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Checking domain availability for: " + tenantDomain);
        }
        boolean isAvailable = tenantManagementService
                .isDomainAvailable(tenantDomain);
        if (isAvailable) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Domain is available: " + tenantDomain);
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Domain already exists: " + tenantDomain);
            }
            return Response.status(Response.Status.OK).build();
        }
    }
}
