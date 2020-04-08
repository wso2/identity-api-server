package org.wso2.carbon.identity.api.server.tenant.management.common;

import org.wso2.carbon.tenant.mgt.services.TenantMgtService;

/**
 * Service holder class for tenant management.
 */
public class TenantManagementServiceHolder {

    private static TenantMgtService tenantMgtService;

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
}
