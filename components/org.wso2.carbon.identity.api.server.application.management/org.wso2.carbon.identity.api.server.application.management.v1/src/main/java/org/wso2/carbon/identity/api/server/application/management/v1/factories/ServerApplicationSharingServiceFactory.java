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

package org.wso2.carbon.identity.api.server.application.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationSharingService;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;

/**
 * Factory class for ServerApplicationSharingService.
 */
public class ServerApplicationSharingServiceFactory {

    private static final Log log = LogFactory.getLog(ServerApplicationSharingServiceFactory.class);
    private static final ServerApplicationSharingService SERVICE;

    static {
        if (log.isDebugEnabled()) {
            log.debug("Initializing ServerApplicationSharingService from OSGi context.");
        }
        OrgApplicationManager orgApplicationManager = ApplicationManagementServiceHolder.getOrgApplicationManager();

        if (orgApplicationManager == null) {
            log.error("OrgApplicationManager is not available from OSGi context.");
            throw new IllegalStateException("OrgApplicationManager is not available from OSGi context.");
        }

        SERVICE = new ServerApplicationSharingService(orgApplicationManager);
        if (log.isDebugEnabled()) {
            log.debug("Successfully initialized ServerApplicationSharingService.");
        }
    }

    /**
     * Get ServerAPIResourceManagementService instance.
     *
     * @return ServerAPIResourceManagementService.
     */
    public static ServerApplicationSharingService getServerApplicationSharingService() {

        return SERVICE;
    }

}
