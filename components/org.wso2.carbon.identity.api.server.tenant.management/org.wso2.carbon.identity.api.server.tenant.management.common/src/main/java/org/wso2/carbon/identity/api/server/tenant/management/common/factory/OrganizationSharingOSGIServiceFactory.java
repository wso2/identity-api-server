package org.wso2.carbon.identity.api.server.tenant.management.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.OrganizationUserSharingService;

/**
 *
 */
public class OrganizationSharingOSGIServiceFactory extends AbstractFactoryBean<OrganizationUserSharingService> {

    private OrganizationUserSharingService organizationUserSharingService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected OrganizationUserSharingService createInstance() throws Exception {

        OrganizationUserSharingService organizationUserSharingService = null;
        if (this.organizationUserSharingService == null) {
            organizationUserSharingService = (OrganizationUserSharingService)
                    PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getOSGiService(OrganizationUserSharingService.class,
                                    null);
            if (organizationUserSharingService != null) {
                this.organizationUserSharingService = organizationUserSharingService;
            } else {
                throw new Exception("Unable to get the OrganizationUserSharingService");
            }
        }
        return this.organizationUserSharingService;
    }
}
