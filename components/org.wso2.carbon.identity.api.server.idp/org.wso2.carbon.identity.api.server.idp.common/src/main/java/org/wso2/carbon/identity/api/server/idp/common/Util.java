/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;
import org.wso2.carbon.idp.mgt.IdpManager;

/**
 * Identity provider management util class.
 */
public class Util {

    /**
     * Get IdentityProviderManager osgi service.
     *
     * @return IdentityProviderManager
     */
    // TODO: 11/4/19 use Osgi service factory 
    public static IdentityProviderManager getIdentityProviderManager() {
        return (IdentityProviderManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(IdpManager.class, null);
    }

    /**
     * Get ClaimMetadataManagementService osgi service.
     *
     * @return ClaimMetadataManagementService
     */
    // TODO: 11/4/19 use Osgi service factory
    public static ClaimMetadataManagementService getClaimMetadataManagementService() {
        return (ClaimMetadataManagementService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(ClaimMetadataManagementService.class, null);
    }
}
