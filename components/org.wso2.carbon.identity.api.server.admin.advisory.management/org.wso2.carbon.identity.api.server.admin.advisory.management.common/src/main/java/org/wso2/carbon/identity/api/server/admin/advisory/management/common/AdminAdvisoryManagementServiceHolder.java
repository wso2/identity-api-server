/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.admin.advisory.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.advisory.mgt.service.AdminAdvisoryManagementService;
import org.wso2.carbon.context.PrivilegedCarbonContext;

/**
 * Service holder class for admin advisory management.
 */
public class AdminAdvisoryManagementServiceHolder {

    private static final Log log = LogFactory.getLog(AdminAdvisoryManagementServiceHolder.class);

    public AdminAdvisoryManagementServiceHolder() {}

    private static class AdminAdvisoryServiceHolder {

        static final AdminAdvisoryManagementService SERVICE = (AdminAdvisoryManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(AdminAdvisoryManagementService.class, null);
    }

    /**
     * Get admin advisory management service.
     */
    public static AdminAdvisoryManagementService getAdminAdvisoryManagementService() {

        AdminAdvisoryManagementService service = AdminAdvisoryServiceHolder.SERVICE;
        if (log.isDebugEnabled()) {
            log.debug("Retrieved AdminAdvisoryManagementService from OSGi service registry.");
        }
        if (service == null) {
            log.warn("AdminAdvisoryManagementService is not available in the OSGi service registry.");
        }
        return service;
    }
}
