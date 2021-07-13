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

import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;

/**
 * Service holder class for server configuration related services.
 */
public class AuthenticatorsServiceHolder {

    private static AuthenticatorsServiceHolder instance = new AuthenticatorsServiceHolder();

    private ApplicationManagementService applicationManagementService;
    private IdentityProviderManager identityProviderManager;

    private AuthenticatorsServiceHolder() {

    }

    public static AuthenticatorsServiceHolder getInstance() {

        return instance;
    }

    /**
     * Get ApplicationManagementService osgi service.
     *
     * @return ApplicationManagementService
     */
    public ApplicationManagementService getApplicationManagementService() {

        return AuthenticatorsServiceHolder.getInstance().applicationManagementService;
    }

    /**
     * Set ApplicationManagementService osgi service.
     *
     * @param applicationManagementService ApplicationManagementService.
     */
    public void setApplicationManagementService(ApplicationManagementService applicationManagementService) {

        AuthenticatorsServiceHolder.getInstance().applicationManagementService = applicationManagementService;
    }

    /**
     * Get IdentityProviderManager osgi service.
     *
     * @return IdentityProviderManager
     */
    public IdentityProviderManager getIdentityProviderManager() {

        return AuthenticatorsServiceHolder.getInstance().identityProviderManager;
    }

    /**
     * Set IdentityProviderManager osgi service.
     *
     * @param identityProviderManager IdentityProviderManager.
     */
    public void setIdentityProviderManager(IdentityProviderManager identityProviderManager) {

        AuthenticatorsServiceHolder.getInstance().identityProviderManager = identityProviderManager;
    }
}
