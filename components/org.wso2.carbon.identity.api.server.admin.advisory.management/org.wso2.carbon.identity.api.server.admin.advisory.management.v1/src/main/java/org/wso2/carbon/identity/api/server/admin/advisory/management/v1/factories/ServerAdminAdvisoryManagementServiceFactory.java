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

package org.wso2.carbon.identity.api.server.admin.advisory.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.advisory.mgt.service.AdminAdvisoryManagementService;
import org.wso2.carbon.identity.api.server.admin.advisory.management.common.AdminAdvisoryManagementServiceHolder;
import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.core.ServerAdminAdvisoryManagementService;

/**
 * Service factory class for admin advisory management API.
 */
public class ServerAdminAdvisoryManagementServiceFactory {

    private static final Log LOG = LogFactory.getLog(ServerAdminAdvisoryManagementServiceFactory.class);
    private static final ServerAdminAdvisoryManagementService SERVICE;

    static {
        LOG.debug("Initializing ServerAdminAdvisoryManagementServiceFactory.");
        AdminAdvisoryManagementService adminAdvisoryManagementService = AdminAdvisoryManagementServiceHolder
                .getAdminAdvisoryManagementService();

        if (adminAdvisoryManagementService == null) {
            LOG.error("AdminAdvisoryManagementService is not available from OSGi context.");
            throw new IllegalStateException("AdminAdvisoryManagementService is not available from OSGi context.");
        }

        SERVICE = new ServerAdminAdvisoryManagementService(adminAdvisoryManagementService);
        LOG.debug("ServerAdminAdvisoryManagementServiceFactory initialized successfully.");
    }

    /**
     * Get admin advisory management service.
     *
     * @return ServerAdminAdvisoryManagementService
     */
    public static ServerAdminAdvisoryManagementService getServerAdminAdvisoryManagementService() {

        LOG.debug("Returning ServerAdminAdvisoryManagementService instance.");
        return SERVICE;
    }
}
