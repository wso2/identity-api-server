package org.wso2.carbon.identity.api.server.idle.account.identification.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.idle.account.identification.services.IdleAccountIdentificationService;

/**
 * Factory Beans serve as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the IdleAccountIdentificationService type of object inside the container.
 */
public class IdleAccountIdentificationOSGIServiceFactory extends AbstractFactoryBean<IdleAccountIdentificationService> {

    private IdleAccountIdentificationService idleAccountIdentificationService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected IdleAccountIdentificationService createInstance() throws Exception {

        if (this.idleAccountIdentificationService == null) {
            IdleAccountIdentificationService taskOperationService =
                    (IdleAccountIdentificationService) PrivilegedCarbonContext.
                    getThreadLocalCarbonContext().getOSGiService(IdleAccountIdentificationService.class, null);

            if (taskOperationService == null) {
                throw new Exception("Unable to retrieve Idle account identification service.");
            }
            this.idleAccountIdentificationService = taskOperationService;
        }
        return this.idleAccountIdentificationService;
    }
}
