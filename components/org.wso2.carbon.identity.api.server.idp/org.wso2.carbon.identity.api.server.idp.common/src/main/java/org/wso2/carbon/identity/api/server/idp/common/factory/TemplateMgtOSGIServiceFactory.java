package org.wso2.carbon.identity.api.server.idp.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.template.mgt.TemplateManager;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the TemplateManager type of object inside the container.
 */
public class TemplateMgtOSGIServiceFactory extends AbstractFactoryBean<TemplateManager> {

    private TemplateManager templateManager;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected TemplateManager createInstance() throws Exception {

        if (this.templateManager == null) {
            TemplateManager taskOperationService = (TemplateManager) PrivilegedCarbonContext.
                    getThreadLocalCarbonContext().getOSGiService(TemplateManager.class, null);
            if (taskOperationService != null) {
                this.templateManager = taskOperationService;
            } else {
                throw new Exception("Unable to retrieve TemplateManager service.");
            }
        }
        return this.templateManager;
    }
}
