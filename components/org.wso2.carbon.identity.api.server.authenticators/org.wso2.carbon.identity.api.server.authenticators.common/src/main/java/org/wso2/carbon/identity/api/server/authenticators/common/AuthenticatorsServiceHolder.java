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

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.common.ApplicationAuthenticatorService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.idp.mgt.IdpManager;

/**
 * Service holder class for server configuration related services.
 */
public class AuthenticatorsServiceHolder {

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

        return ApplicationManagementServiceHolder.SERVICE;
    }

    /**
     * Get IdpManager osgi service.
     *
     * @return IdpManager
     */
    public static IdpManager getIdentityProviderManager() {

        return IdentityProviderManagerHolder.SERVICE;
    }

    /**
     * Get ApplicationAuthenticatorService osgi service.
     *
     * @return ApplicationAuthenticatorService
     */
    public static ApplicationAuthenticatorService getApplicationAuthenticatorService() {

        return ApplicationAuthenticatorServiceHolder.SERVICE;
    }
}
