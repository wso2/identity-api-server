/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application;

import org.apache.commons.lang.StringUtils;
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

            if (StringUtils.isBlank(logoutReturnUrl)) {
                logoutReturnUrl = ApplicationManagementConstants.DEFAULT_LOGOUT_RETURN_URL_VALUE;
            }
            ServiceProviderProperty[] spProperties = serviceProvider.getSpProperties();
            boolean logoutUrlExists = false;

            if (spProperties == null) {
                spProperties = new ServiceProviderProperty[0];
            }
            for (ServiceProviderProperty spProperty : spProperties) {
                if (ApplicationManagementConstants.PROP_LOGOUT_RETURN_URL.equals(spProperty.getName())) {
                    spProperty.setValue(logoutReturnUrl);
                    logoutUrlExists = true;
                }
            }
            if (!logoutUrlExists) {
                spProperties = Arrays.copyOf(spProperties, spProperties.length + 1);
                spProperties[spProperties.length - 1] = buildLogoutReturnUrlSpProperty(logoutReturnUrl);
            }
            serviceProvider.setSpProperties(spProperties);
        }

        private ServiceProviderProperty buildLogoutReturnUrlSpProperty(String logoutReturnUrl) {

            ServiceProviderProperty logoutReturnUrlProperty = new ServiceProviderProperty();
            logoutReturnUrlProperty.setName(ApplicationManagementConstants.PROP_LOGOUT_RETURN_URL);
            logoutReturnUrlProperty.setValue(logoutReturnUrl);
            logoutReturnUrlProperty.setDisplayName(ApplicationManagementConstants.LOGOUT_RETURN_URL_DISPLAY_NAME);
            return logoutReturnUrlProperty;
        }
}
