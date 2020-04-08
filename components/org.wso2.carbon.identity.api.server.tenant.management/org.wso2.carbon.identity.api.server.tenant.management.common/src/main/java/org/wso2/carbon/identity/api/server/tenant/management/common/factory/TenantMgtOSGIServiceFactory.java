package org.wso2.carbon.identity.api.server.tenant.management.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.tenant.mgt.services.TenantMgtService;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the TenantMgtService type of object inside the container.
 */
public class TenantMgtOSGIServiceFactory extends AbstractFactoryBean<TenantMgtService> {

    private TenantMgtService tenantMgtService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected TenantMgtService createInstance() throws Exception {

        TenantMgtService tenantMgtService = null;
        if (this.tenantMgtService == null) {
            try {
                tenantMgtService = (TenantMgtService)
                        PrivilegedCarbonContext.getThreadLocalCarbonContext()
                                .getOSGiService(TenantMgtService.class, null);
            } catch (NullPointerException ex) {
                logger.error("erfefe");
            }
            if (tenantMgtService != null) {
                this.tenantMgtService = tenantMgtService;
            } else {
//                throw new Exception("Unable to retrieve TenantMgtService service.");
                logger.error("erfefe");
            }
        }
        return this.tenantMgtService;
    }
}
