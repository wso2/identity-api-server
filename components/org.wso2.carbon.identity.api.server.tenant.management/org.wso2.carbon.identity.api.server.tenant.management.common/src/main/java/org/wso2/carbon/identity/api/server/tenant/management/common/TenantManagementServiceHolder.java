/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.tenant.management.common;

import org.wso2.carbon.identity.organization.management.organization.user.sharing.OrganizationUserSharingService;
import org.wso2.carbon.tenant.mgt.services.TenantMgtService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * Service holder class for tenant management.
 */
public class TenantManagementServiceHolder {

    private static TenantMgtService tenantMgtService;
    private static OrganizationUserSharingService organizationUserSharingService;
    private static RealmService realmService;

    /**
     * Get TenantMgtService osgi service.
     *
     * @return TenantMgtService
     */
    public static TenantMgtService getTenantMgtService() {

        return tenantMgtService;
    }

    /**
     * Set TenantMgtService osgi service.
     *
     * @param tenantMgtService TenantMgtService.
     */
    public static void setTenantMgtService(TenantMgtService tenantMgtService) {

        TenantManagementServiceHolder.tenantMgtService = tenantMgtService;
    }

    /**
     * Get OrganizationUserSharingService osgi service.
     *
     * @return OrganizationUserSharingService
     */
    public static OrganizationUserSharingService getOrganizationUserSharingService() {

        return organizationUserSharingService;
    }

    /**
     * Set OrganizationUserSharingService osgi service.
     *
     * @param organizationUserSharingService OrganizationUserSharingService.
     */
    public static void setOrganizationUserSharingService(
            OrganizationUserSharingService organizationUserSharingService) {

        TenantManagementServiceHolder.organizationUserSharingService = organizationUserSharingService;
    }

    public static RealmService getRealmService() {

        return realmService;
    }

    public static void setRealmService(RealmService realmService) {

        TenantManagementServiceHolder.realmService = realmService;
    }
}
