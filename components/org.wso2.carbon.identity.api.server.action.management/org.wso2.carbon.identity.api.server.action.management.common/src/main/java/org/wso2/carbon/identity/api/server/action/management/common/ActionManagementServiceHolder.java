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

package org.wso2.carbon.identity.api.server.action.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.action.management.api.service.ActionManagementService;

/**
 * Service holder class for action management.
 */
public class ActionManagementServiceHolder {

    private static final Log log = LogFactory.getLog(ActionManagementServiceHolder.class);

    private ActionManagementServiceHolder() {}

    private static class ServiceHolder {

        static final ActionManagementService SERVICE = (ActionManagementService) PrivilegedCarbonContext
                        .getThreadLocalCarbonContext().getOSGiService(ActionManagementService.class, null);
    }

    /**
     * Get ActionManagementService osgi service.
     *
     * @return ActionManagementService.
     */
    public static ActionManagementService getActionManagementService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving ActionManagementService from OSGi service registry.");
        }
        ActionManagementService service = ServiceHolder.SERVICE;
        if (service == null) {
            log.warn("ActionManagementService is not available from OSGi service registry.");
        } else if (log.isDebugEnabled()) {
            log.debug("ActionManagementService retrieved successfully from OSGi service registry.");
        }
        return service;
    }
}
