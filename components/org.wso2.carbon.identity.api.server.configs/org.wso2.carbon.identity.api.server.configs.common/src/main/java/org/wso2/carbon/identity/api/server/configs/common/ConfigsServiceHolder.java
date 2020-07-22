/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.configs.common;

import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;

/**
 * Service holder class for server configuration related services.
 */
public class ConfigsServiceHolder {

    private static ConfigsServiceHolder instance = new ConfigsServiceHolder();

    private ApplicationManagementService applicationManagementService;
    private IdentityProviderManager identityProviderManager;
    private CORSManagementService corsManagementService;

    private ConfigsServiceHolder() {}

    public static ConfigsServiceHolder getInstance() {

        return instance;
    }

    /**
     * Get ApplicationManagementService osgi service.
     *
     * @return ApplicationManagementService
     */
    public ApplicationManagementService getApplicationManagementService() {

        return ConfigsServiceHolder.getInstance().applicationManagementService;
    }

    /**
     * Set ApplicationManagementService osgi service.
     *
     * @param applicationManagementService ApplicationManagementService.
     */
    public void setApplicationManagementService(ApplicationManagementService applicationManagementService) {

        ConfigsServiceHolder.getInstance().applicationManagementService = applicationManagementService;
    }

    /**
     * Get IdentityProviderManager osgi service.
     *
     * @return IdentityProviderManager
     */
    public IdentityProviderManager getIdentityProviderManager() {

        return ConfigsServiceHolder.getInstance().identityProviderManager;
    }

    /**
     * Set IdentityProviderManager osgi service.
     *
     * @param identityProviderManager IdentityProviderManager.
     */
    public void setIdentityProviderManager(IdentityProviderManager identityProviderManager) {

        ConfigsServiceHolder.getInstance().identityProviderManager = identityProviderManager;
    }

    /**
     * Get CORSManagementService osgi service.
     *
     * @return CORSManagementService
     */
    public CORSManagementService getCorsManagementService() {

        return ConfigsServiceHolder.getInstance().corsManagementService;
    }

    /**
     * Set CORSManagementService osgi service.
     *
     * @param corsManagementService CORSManagementService.
     */
    public void setCorsManagementService(CORSManagementService corsManagementService) {

        ConfigsServiceHolder.getInstance().corsManagementService = corsManagementService;
    }
}
