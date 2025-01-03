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

package org.wso2.carbon.identity.api.server.organization.configs.v1.factories;

import org.wso2.carbon.identity.api.server.organization.configs.common.OrganizationConfigsServiceHolder;
import org.wso2.carbon.identity.api.server.organization.configs.v1.core.OrganizationConfigsService;
import org.wso2.carbon.identity.organization.config.service.OrganizationConfigManager;

/**
 * Factory class for OrganizationConfigsService.
 */
public class OrganizationConfigsServiceFactory {

    private static final OrganizationConfigsService SERVICE;

    static {
        OrganizationConfigManager organizationConfigManager = OrganizationConfigsServiceHolder
                .getOrganizationConfigManager();

        if (organizationConfigManager == null) {
            throw new IllegalStateException("OrganizationConfigManager service is not available from OSGi context.");
        }

        SERVICE = new OrganizationConfigsService(organizationConfigManager);
    }

    /**
     * Get OrganizationConfigsService.
     *
     * @return OrganizationConfigsService.
     */
    public static OrganizationConfigsService getOrganizationConfigsService() {

        return SERVICE;
    }
}
