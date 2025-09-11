/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *                                                                         
 * Licensed under the Apache License, Version 2.0 (the "License");         
 * you may not use this file except in compliance with the License.        
 * You may obtain a copy of the License at                                 
 *                                                                         
 * http://www.apache.org/licenses/LICENSE-2.0                              
 *                                                                         
 * Unless required by applicable law or agreed to in writing, software     
 * distributed under the License is distributed on an "AS IS" BASIS,       
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and     
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.authenticators.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.common.ApplicationAuthenticatorService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.idp.mgt.IdpManager;

/**
 * Service holder class for server configuration related services.
 */
public class AuthenticatorsServiceHolder {

    private static final Log log = LogFactory.getLog(AuthenticatorsServiceHolder.class);

    private AuthenticatorsServiceHolder() {

    }

    private static class ApplicationManagementServiceHolder {

        static final ApplicationManagementService SERVICE = (ApplicationManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ApplicationManagementService.class, null);
    }

    private static class IdentityProviderManagerHolder {

        static final IdpManager SERVICE = (IdpManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(IdpManager.class, null);
    }

    private static class ApplicationAuthenticatorServiceHolder {

        static final ApplicationAuthenticatorService SERVICE = (ApplicationAuthenticatorService)
                PrivilegedCarbonContext.getThreadLocalCarbonContext().getOSGiService(
                        ApplicationAuthenticatorService.class, null);
    }

    /**
     * Get ApplicationManagementService osgi service.
     *
     * @return ApplicationManagementService
     */
    public static ApplicationManagementService getApplicationManagementService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving ApplicationManagementService from OSGi service registry.");
        }
        if (ApplicationManagementServiceHolder.SERVICE == null) {
            log.warn("ApplicationManagementService is not available in OSGi service registry.");
        }
        return ApplicationManagementServiceHolder.SERVICE;
    }

    /**
     * Get IdpManager osgi service.
     *
     * @return IdpManager
     */
    public static IdpManager getIdentityProviderManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdpManager from OSGi service registry.");
        }
        if (IdentityProviderManagerHolder.SERVICE == null) {
            log.warn("IdpManager is not available in OSGi service registry.");
        }
        return IdentityProviderManagerHolder.SERVICE;
    }

    /**
     * Get ApplicationAuthenticatorService osgi service.
     *
     * @return ApplicationAuthenticatorService
     */
    public static ApplicationAuthenticatorService getApplicationAuthenticatorService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving ApplicationAuthenticatorService from OSGi service registry.");
        }
        if (ApplicationAuthenticatorServiceHolder.SERVICE == null) {
            log.warn("ApplicationAuthenticatorService is not available in OSGi service registry.");
        }
        return ApplicationAuthenticatorServiceHolder.SERVICE;
    }
}
