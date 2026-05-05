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

package org.wso2.carbon.identity.api.server.action.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.action.management.api.service.ActionManagementService;
import org.wso2.carbon.identity.api.server.action.management.common.ActionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.action.management.v1.core.ServerActionManagementService;

/**
 * Factory class for Action Management Service.
 */
public class ActionManagementServiceFactory {

    private static final Log LOG = LogFactory.getLog(ActionManagementServiceFactory.class);
    private static final ServerActionManagementService SERVICE;

    static {
        ActionManagementService actionManagementService = ActionManagementServiceHolder.getActionManagementService();

        if (actionManagementService == null) {
            LOG.error("ActionManagementService is not available from OSGi context.");
            throw new IllegalStateException("ActionManagementService is not available from OSGi context.");
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing ServerActionManagementService with ActionManagementService.");
        }
        SERVICE = new ServerActionManagementService(actionManagementService);
    }

    /**
     * Get Action Management Service.
     *
     * @return ServerActionManagementService.
     */
    public static ServerActionManagementService getActionManagementService() {

        return SERVICE;
    }
}
