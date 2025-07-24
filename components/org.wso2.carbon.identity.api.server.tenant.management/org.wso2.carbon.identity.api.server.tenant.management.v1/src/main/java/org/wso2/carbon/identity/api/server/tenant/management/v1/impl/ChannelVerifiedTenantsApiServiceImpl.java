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

package org.wso2.carbon.identity.api.server.tenant.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.tenant.management.common.
        TenantManagementConstants;
import org.wso2.carbon.identity.api.server.tenant.management.v1.
        ChannelVerifiedTenantsApiService;
import org.wso2.carbon.identity.api.server.tenant.management.v1.core.
        ServerTenantManagementService;
import org.wso2.carbon.identity.api.server.tenant.management.v1.factories.
        ServerTenantManagementServiceFactory;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.
        ChannelVerifiedTenantModel;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * API implementation for channel verified lite users to create tenants.
 */
public class ChannelVerifiedTenantsApiServiceImpl
        implements ChannelVerifiedTenantsApiService {

    /**
     * Logger for ChannelVerifiedTenantsApiServiceImpl.
     */
    private static final Log LOG = LogFactory.getLog(
            ChannelVerifiedTenantsApiServiceImpl.class);

    /**
     * Server tenant management service.
     */
    private final ServerTenantManagementService tenantManagementService;

    /**
     * Constructor for ChannelVerifiedTenantsApiServiceImpl.
     */
    public ChannelVerifiedTenantsApiServiceImpl() {
        try {
            this.tenantManagementService = ServerTenantManagementServiceFactory
                    .getServerTenantManagementService();
            if (LOG.isDebugEnabled()) {
                LOG.debug("ChannelVerifiedTenantsApiServiceImpl "
                        + "initialized successfully.");
            }
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating "
                    + "ServerTenantManagementService.", e);
            throw new RuntimeException("Error occurred while initiating "
                    + "ServerTenantManagementService.", e);
        }
    }

    /**
     * Add a channel verified tenant.
     *
     * @param channelVerifiedTenantModel Channel verified tenant model
     * @return Response with created tenant location
     */
    @Override
    public final Response addChannelVerifiedTenant(
            final ChannelVerifiedTenantModel channelVerifiedTenantModel) {

        if (LOG.isDebugEnabled()) {
            String domain = null;
            if (channelVerifiedTenantModel != null) {
                domain = channelVerifiedTenantModel.getDomain();
            }
            LOG.debug("Adding channel verified tenant with domain: "
                    + domain);
        }
        String resourceId = tenantManagementService.addTenant(
                channelVerifiedTenantModel);
        LOG.info("Channel verified tenant created successfully "
                + "with resource ID: " + resourceId);
        return Response.created(getResourceLocation(resourceId)).build();
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
}
