/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 LLC. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.carbon.identity.api.server.organization.management.common;

import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;

/**
 * Service holder class for organization management related services.
 */
public class OrganizationManagementServiceHolder {

    private static OrganizationManagementServiceHolder instance = new OrganizationManagementServiceHolder();

    private OrgApplicationManager orgApplicationManager;
    private OrganizationManager organizationManager;

    private OrganizationManagementServiceHolder() {

    }

    public static OrganizationManagementServiceHolder getInstance() {

        return instance;
    }

    /**
     * Get OrgApplicationManager OSGi service.
     *
     * @return OrgApplicationManager
     */
    public OrgApplicationManager getOrgApplicationManager() {

        return OrganizationManagementServiceHolder.getInstance().orgApplicationManager;
    }

    /**
     * Set OrgApplicationManager OSGi service.
     *
     * @param orgApplicationManager OrgApplicationManager.
     */
    public void setOrgApplicationManager(OrgApplicationManager orgApplicationManager) {

        OrganizationManagementServiceHolder.getInstance().orgApplicationManager = orgApplicationManager;
    }

    /**
     * Get OrganizationManager OSGi service.
     *
     * @return OrganizationManager
     */
    public OrganizationManager getOrganizationManager() {

        return OrganizationManagementServiceHolder.getInstance().organizationManager;
    }

    /**
     * Set OrganizationManager OSGi service.
     *
     * @param organizationManager IdentityProviderManager.
     */
    public void setOrganizationManager(OrganizationManager organizationManager) {

        OrganizationManagementServiceHolder.getInstance().organizationManager = organizationManager;
    }
}
