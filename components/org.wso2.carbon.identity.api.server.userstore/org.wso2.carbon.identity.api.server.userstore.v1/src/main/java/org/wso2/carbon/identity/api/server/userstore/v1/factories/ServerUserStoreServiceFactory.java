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

package org.wso2.carbon.identity.api.server.userstore.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.userstore.common.UserStoreConfigServiceHolder;
import org.wso2.carbon.identity.api.server.userstore.v1.core.ServerUserStoreService;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.user.store.configuration.UserStoreConfigService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Factory class for ServerUserStoreService.
 */
public class ServerUserStoreServiceFactory {

    private static final Log LOG = LogFactory.getLog(ServerUserStoreServiceFactory.class);
    private static final ServerUserStoreService SERVICE;

    static {
        UserStoreConfigService userStoreConfigService = UserStoreConfigServiceHolder.getUserStoreConfigService();
        RealmService realmService = UserStoreConfigServiceHolder.getRealmService();
        ClaimMetadataManagementService claimMetadataManagementService = UserStoreConfigServiceHolder
                .getClaimMetadataManagementService();

        if (userStoreConfigService == null) {
            LOG.error("UserStoreConfigService is not available from OSGi context");
            throw new IllegalStateException("UserStoreConfigService is not available from OSGi context.");
        }

        if (realmService == null) {
            LOG.error("RealmService is not available from OSGi context");
            throw new IllegalStateException("RealmService is not available from OSGi context.");
        }

        if (claimMetadataManagementService == null) {
            LOG.error("ClaimMetadataManagementService is not available from OSGi context");
            throw new IllegalStateException("ClaimMetadataManagementService is not available from OSGi context.");
        }

        SERVICE = new ServerUserStoreService(userStoreConfigService, realmService, claimMetadataManagementService);
        if (LOG.isDebugEnabled()) {
            LOG.debug("ServerUserStoreService initialized successfully");
        }
    }

    /**
     * Get ServerUserStoreService.
     *
     * @return ServerUserStoreService.
     */
    public static ServerUserStoreService getServerUserStoreService() {

        return SERVICE;
    }

}
