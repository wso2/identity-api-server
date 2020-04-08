/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.idp.common;

import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.template.mgt.TemplateManager;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;

/**
 * Service holder class for identity providers.
 */
public class IdentityProviderServiceHolder {

    private static IdentityProviderManager identityProviderManager;
    private static ClaimMetadataManagementService claimMetadataManagementService;
    private static TemplateManager templateManager;

    /**
     * Get IdentityProviderManager osgi service.
     *
     * @return IdentityProviderManager
     */
    public static IdentityProviderManager getIdentityProviderManager() {

        return identityProviderManager;
    }

    /**
     * Set IdentityProviderManager osgi service.
     *
     * @param identityProviderManager IdentityProviderManager.
     */
    public static void setIdentityProviderManager(IdentityProviderManager identityProviderManager) {

        IdentityProviderServiceHolder.identityProviderManager = identityProviderManager;
    }

    /**
     * Get ClaimMetadataManagementService osgi service.
     *
     * @return ClaimMetadataManagementService.
     */
    public static ClaimMetadataManagementService getClaimMetadataManagementService() {

        return claimMetadataManagementService;
    }

    /**
     * Set ClaimMetadataManagementService osgi service.
     *
     * @param claimMetadataManagementService ClaimMetadataManagementService.
     */
    public static void setClaimMetadataManagementService(
            ClaimMetadataManagementService claimMetadataManagementService) {

        IdentityProviderServiceHolder.claimMetadataManagementService = claimMetadataManagementService;
    }

    /**
     * Set TemplateManager osgi service.
     *
     * @param templateManager TemplateManager service
     */
    public static void setTemplateManager(TemplateManager templateManager) {

        IdentityProviderServiceHolder.templateManager = templateManager;
    }

    /**
     * Get TemplateManager osgi service.
     *
     * @return TemplateManager
     */
    public static TemplateManager getTemplateManager() {

        return templateManager;
    }
}
