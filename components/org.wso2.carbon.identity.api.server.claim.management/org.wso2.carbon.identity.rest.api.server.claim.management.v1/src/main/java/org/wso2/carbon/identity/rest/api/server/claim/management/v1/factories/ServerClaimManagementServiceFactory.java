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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.claim.management.common.ClaimManagementDataHolder;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.core.ServerClaimManagementService;

/**
 * Factory class for ServerClaimManagementService.
 */
public class ServerClaimManagementServiceFactory {

    private static final Log LOG = LogFactory.getLog(ServerClaimManagementServiceFactory.class);
    private static final ServerClaimManagementService SERVICE;

    static {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing ServerClaimManagementService factory");
        }
        ClaimMetadataManagementService claimMetadataManagementService = ClaimManagementDataHolder
                .getClaimMetadataManagementService();
        OrganizationManager organizationManager = ClaimManagementDataHolder.getOrganizationManager();

        if (claimMetadataManagementService == null) {
            LOG.error("ClaimMetadataManagementService is not available from OSGi context");
            throw new IllegalStateException("ClaimMetadataManagementService is not available from OSGi context.");
        }

        if (organizationManager == null) {
            LOG.error("OrganizationManager is not available from OSGi context");
            throw new IllegalStateException("OrganizationManager is not available from OSGi context.");
        }

        SERVICE = new ServerClaimManagementService(claimMetadataManagementService, organizationManager);
        LOG.info("ServerClaimManagementService factory initialized successfully");
    }

    /**
     * Get ServerClaimManagementService.
     *
     * @return ServerClaimManagementService
     */
    public static ServerClaimManagementService getServerClaimManagementService() {

        return SERVICE;
    }
}
