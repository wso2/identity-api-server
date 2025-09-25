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

package org.wso2.carbon.identity.api.server.identity.governance.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.identity.governance.common.GovernanceDataHolder;
import org.wso2.carbon.identity.api.server.identity.governance.v1.core.ServerIdentityGovernanceService;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Factory class for Server Identity Governance Service.
 */
public class ServerIdentityGovernanceServiceFactory {

    private static final Log LOG = LogFactory.getLog(ServerIdentityGovernanceServiceFactory.class);
    private static final ServerIdentityGovernanceService SERVICE;

    static {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing ServerIdentityGovernanceServiceFactory.");
        }
        IdentityGovernanceService identityGovernanceService = GovernanceDataHolder.getIdentityGovernanceService();

        if (identityGovernanceService == null) {
            LOG.error("IdentityGovernanceService is not available from OSGi context.");
            throw new IllegalStateException("RolePermissionManagementService is not available from OSGi context.");
        }

        SERVICE = new ServerIdentityGovernanceService(identityGovernanceService);
        if (LOG.isDebugEnabled()) {
            LOG.debug("ServerIdentityGovernanceServiceFactory initialized successfully.");
        }
    }

    /**
     * Get IdentityGovernanceService.
     *
     * @return IdentityGovernanceService
     */
    public static ServerIdentityGovernanceService getServerIdentityGovernanceService() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Returning ServerIdentityGovernanceService instance.");
        }
        return SERVICE;
    }
}
