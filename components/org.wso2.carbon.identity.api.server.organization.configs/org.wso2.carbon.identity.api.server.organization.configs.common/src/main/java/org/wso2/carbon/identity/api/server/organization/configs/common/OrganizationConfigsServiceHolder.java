/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.configs.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.organization.config.service.OrganizationConfigManager;

/**
 * Service holder class for organization configuration management.
 */
public class OrganizationConfigsServiceHolder {

    private static final Log LOG = LogFactory.getLog(OrganizationConfigsServiceHolder.class);

    public OrganizationConfigsServiceHolder() {}

    private static class OrganizationConfigManagerHolder {

        static final OrganizationConfigManager SERVICE = (OrganizationConfigManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OrganizationConfigManager.class, null);
    }

    /**
     * Get OrganizationConfigManager OSGi service.
     *
     * @return OrganizationConfigManager.
     */
    public static OrganizationConfigManager getOrganizationConfigManager() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving OrganizationConfigManager service.");
        }
        
        OrganizationConfigManager service = OrganizationConfigManagerHolder.SERVICE;
        if (service == null) {
            LOG.warn("OrganizationConfigManager service is not available.");
        }
        
        return service;
    }
}
