package org.wso2.carbon.identity.api.server.identity.governance.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the UserSessionManagementService type of object inside the container.
 */
public class OSGIServiceFactory extends AbstractFactoryBean<IdentityGovernanceService> {

    private IdentityGovernanceService identityGovernanceService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected IdentityGovernanceService createInstance() throws Exception {

        if (this.identityGovernanceService == null) {
            IdentityGovernanceService identityGovernanceService = (IdentityGovernanceService)
                    PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getOSGiService(IdentityGovernanceService.class, null);
            if (identityGovernanceService != null) {
                this.identityGovernanceService = identityGovernanceService;
            } else {
                throw new Exception("Unable to retrieve IdentityGovernanceService service.");
            }
        }
        return this.identityGovernanceService;
    }

}
