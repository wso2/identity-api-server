/*
 * Copyright (c) 2020-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.tenant.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.tenant.mgt.services.TenantMgtService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Service holder class for tenant management.
 */
public class TenantManagementServiceHolder {

    private TenantManagementServiceHolder() {

    }

    private static class TenantMgtServiceHolder {

        static final TenantMgtService SERVICE = (TenantMgtService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(TenantMgtService.class, null);
    }

    private static class RealmServiceHolder {

        static final RealmService SERVICE = (RealmService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(RealmService.class, null);
    }

    /**
     * Get TenantMgtService osgi service.
     *
     * @return TenantMgtService.
     */
    public static TenantMgtService getTenantMgtService() {

        return TenantMgtServiceHolder.SERVICE;
    }

    /**
     * Get RealmService osgi service.
     *
     * @return RealmService.
     */
    public static RealmService getRealmService() {

        return RealmServiceHolder.SERVICE;
    }
}
