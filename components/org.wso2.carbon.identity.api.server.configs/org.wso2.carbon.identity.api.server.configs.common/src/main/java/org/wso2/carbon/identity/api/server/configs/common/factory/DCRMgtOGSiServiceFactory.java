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

package org.wso2.carbon.identity.api.server.configs.common.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth.dcr.DCRConfigurationMgtService;

/**
 * Since this factory produces JWTClientAuthenticatorMgtService connector service,  there is a possibility that said
 * connector not available in the distribution.
 * So rather than designing as Factory Beans this class designed as Singleton.
 */
public class DCRMgtOGSiServiceFactory {

    private static DCRConfigurationMgtService dcrConfigurationMgtService = null;
    private static final Log log = LogFactory.getLog(DCRMgtOGSiServiceFactory.class);


    /**
     * This method return  the instance if the OSGi service exists.
     * Else throw Null pointer Exception. We handle the exception gracefully.
     *
     * @return JWTClientAuthenticatorMgtService
     */
    public static DCRConfigurationMgtService getInstance() {

        if (dcrConfigurationMgtService == null) {
            /* Try catch statement is included due to a  NullPointerException which occurs at the server startup and
            runtime when the DCRMgtService is not available in the product. */

            try {
                // Call class for name to check the class is available in the run time.
                // This method will call only once at the first api call.
                Class.forName("org.wso2.carbon.identity.oauth.dcr.DCRConfigurationMgtService");
                DCRConfigurationMgtService taskOperationService
                        = (DCRConfigurationMgtService) PrivilegedCarbonContext.
                        getThreadLocalCarbonContext().getOSGiService
                                (DCRConfigurationMgtService.class, null);
                if (taskOperationService != null) {
                    dcrConfigurationMgtService = taskOperationService;
                }

            } catch (NullPointerException | ClassNotFoundException  e) {
                /* Catch block without implementation so that the DCRMgtService will be set to null
                   in-turn helps in validating the rest API requests. */
                if (log.isDebugEnabled()) {
                    log.debug("Unable to find the DCRMgtService. " +
                            "DCRMgtService is not available in the server.");
                }
            }
        }
        return dcrConfigurationMgtService;
    }
}
