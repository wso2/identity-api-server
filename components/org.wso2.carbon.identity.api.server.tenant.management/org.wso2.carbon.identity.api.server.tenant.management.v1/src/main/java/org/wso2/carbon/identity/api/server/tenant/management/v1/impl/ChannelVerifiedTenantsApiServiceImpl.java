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

import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementConstants;
import org.wso2.carbon.identity.api.server.tenant.management.v1.ChannelVerifiedTenantsApiService;
import org.wso2.carbon.identity.api.server.tenant.management.v1.core.ServerTenantManagementService;
import org.wso2.carbon.identity.api.server.tenant.management.v1.factories.ServerTenantManagementServiceFactory;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.ChannelVerifiedTenantModel;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * API implementation for channel verified lite users to create tenants.
 */
public class ChannelVerifiedTenantsApiServiceImpl implements ChannelVerifiedTenantsApiService {

    private final ServerTenantManagementService tenantManagementService;

    public ChannelVerifiedTenantsApiServiceImpl() {
        try {
            this.tenantManagementService = ServerTenantManagementServiceFactory.getServerTenantManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating ServerTenantManagementService.", e);
        }
    }

    @Override
    public Response addChannelVerifiedTenant(ChannelVerifiedTenantModel channelVerifiedTenantModel) {

        String resourceId = tenantManagementService.addTenant(channelVerifiedTenantModel);
        return Response.created(getResourceLocation(resourceId)).build();
    }

    private URI getResourceLocation(String resourceId) {

        return ContextLoader.buildURIForHeader(Constants.V1_API_PATH_COMPONENT +
                TenantManagementConstants.TENANT_MANAGEMENT_PATH_COMPONENT + "/" + resourceId);
    }
}
