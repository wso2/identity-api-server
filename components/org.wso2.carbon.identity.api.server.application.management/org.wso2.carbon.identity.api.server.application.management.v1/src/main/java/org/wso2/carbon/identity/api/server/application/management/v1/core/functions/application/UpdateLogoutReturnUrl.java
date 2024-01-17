package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application;

import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.ServiceProviderProperty;

import java.util.Arrays;

/**
 * Update logout return url.
 */
public class UpdateLogoutReturnUrl implements UpdateFunction<ServiceProvider, String> {

        @Override
        public void apply(ServiceProvider serviceProvider, String logoutReturnUrl) {

            ServiceProviderProperty[] spProperties = serviceProvider.getSpProperties();
            boolean logoutURLExists = false;

            if (spProperties == null) {
                spProperties = new ServiceProviderProperty[0];
            }
            for (ServiceProviderProperty spProperty : spProperties) {
                if (ApplicationManagementConstants.PROP_LOGOUT_RETURN_URL.equals(spProperty.getName())) {
                    spProperty.setValue(logoutReturnUrl);
                    logoutURLExists = true;
                }
            }
            if (!logoutURLExists) {
                ServiceProviderProperty logoutURLProperty = new ServiceProviderProperty();
                logoutURLProperty.setName(ApplicationManagementConstants.PROP_LOGOUT_RETURN_URL);
                logoutURLProperty.setValue(logoutReturnUrl);
                logoutURLProperty.setDisplayName(ApplicationManagementConstants.LOGOUT_RETURN_URL_DISPLAY_NAME);
                spProperties = Arrays.copyOf(spProperties, spProperties.length + 1);
                spProperties[spProperties.length - 1] = logoutURLProperty;
            }
            serviceProvider.setSpProperties(spProperties);
        }
}
