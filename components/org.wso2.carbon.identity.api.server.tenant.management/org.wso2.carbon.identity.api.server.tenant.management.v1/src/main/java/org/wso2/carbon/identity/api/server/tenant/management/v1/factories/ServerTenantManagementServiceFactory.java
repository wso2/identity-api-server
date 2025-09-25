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

package org.wso2.carbon.identity.api.server.tenant.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.tenant.management.common.TenantManagementServiceHolder;
import org.wso2.carbon.identity.api.server.tenant.management.v1.core.ServerTenantManagementService;
import org.wso2.carbon.tenant.mgt.services.TenantMgtService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Factory class for ServerTenantManagementService.
 */
public class ServerTenantManagementServiceFactory {

    private static final Log log = LogFactory.getLog(ServerTenantManagementServiceFactory.class);
    private static final ServerTenantManagementService SERVICE;

    static {
        if (log.isDebugEnabled()) {
            log.debug("Initializing ServerTenantManagementService factory");
        }
        TenantMgtService tenantMgtService = TenantManagementServiceHolder.getTenantMgtService();
        RealmService realmService = TenantManagementServiceHolder.getRealmService();

        if (tenantMgtService == null) {
            log.error("TenantMgtService is not available from OSGi context");
            throw new IllegalStateException("TenantMgtService is not available from OSGi context.");
        }
        if (realmService == null) {
            log.error("RealmService is not available from OSGi context");
            throw new IllegalStateException("RealmService is not available from OSGi context.");
        }

        SERVICE = new ServerTenantManagementService(tenantMgtService, realmService);
        log.info("ServerTenantManagementService initialized successfully");
    }

    /**
     * Get ServerTenantManagementService.
     *
     * @return ServerTenantManagementService.
     */
    public static ServerTenantManagementService getServerTenantManagementService() {

        return SERVICE;
    }
}
