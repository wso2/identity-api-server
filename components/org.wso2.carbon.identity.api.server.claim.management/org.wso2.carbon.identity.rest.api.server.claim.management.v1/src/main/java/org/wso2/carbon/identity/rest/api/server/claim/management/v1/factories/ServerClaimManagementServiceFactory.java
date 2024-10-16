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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.factories;

import org.wso2.carbon.identity.api.server.claim.management.common.ClaimManagementDataHolder;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.core.ServerClaimManagementService;

/**
 * Factory class for ServerClaimManagementService.
 */
public class ServerClaimManagementServiceFactory {

    private ServerClaimManagementServiceFactory() {

    }

    private static class ServerClaimManagementServiceHolder {

        private static final ServerClaimManagementService SERVICE = createServiceInstance();
    }

    private static ServerClaimManagementService createServiceInstance() {

        ClaimMetadataManagementService claimMetadataManagementService = getClaimMetadataManagementService();
        OrganizationManager organizationManager = getOrganizationManager();

        return new ServerClaimManagementService(claimMetadataManagementService, organizationManager);
    }

    /**
     * Get ServerClaimManagementService.
     *
     * @return ServerClaimManagementService
     */
    public static ServerClaimManagementService getServerClaimManagementService() {

        return ServerClaimManagementServiceHolder.SERVICE;
    }

    private static ClaimMetadataManagementService getClaimMetadataManagementService() {

        ClaimMetadataManagementService service = ClaimManagementDataHolder.getClaimMetadataManagementService();
        if (service == null) {
            throw new IllegalStateException("ClaimMetadataManagementService is not available from OSGi context.");
        }

        return service;
    }

    private static OrganizationManager getOrganizationManager() {

        OrganizationManager organizationManager = ClaimManagementDataHolder.getOrganizationManager();
        if (organizationManager == null) {
            throw new IllegalStateException("OrganizationManager service is not available from OSGi context.");
        }

        return organizationManager;
    }
}
