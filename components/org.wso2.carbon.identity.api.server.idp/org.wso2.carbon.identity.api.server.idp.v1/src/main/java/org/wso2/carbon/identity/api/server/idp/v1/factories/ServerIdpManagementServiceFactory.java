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

package org.wso2.carbon.identity.api.server.idp.v1.factories;

import org.wso2.carbon.identity.api.server.idp.common.IdentityProviderServiceHolder;
import org.wso2.carbon.identity.api.server.idp.v1.core.ServerIdpManagementService;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.template.mgt.TemplateManager;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;

/**
 * Factory class for Server Idp Management Service.
 */
public class ServerIdpManagementServiceFactory {

    private ServerIdpManagementServiceFactory() {

    }

    private static class ServerIdpManagementServiceHolder {

        private static final ServerIdpManagementService SERVICE = createServiceInstance();
    }

    private static ServerIdpManagementService createServiceInstance() {

        IdentityProviderManager identityProviderManager = getIdentityProviderManager();
        ClaimMetadataManagementService claimMetadataManagementService = getClaimMetadataManagementService();
        TemplateManager templateManager = getTemplateManager();

        return new ServerIdpManagementService(identityProviderManager, templateManager, claimMetadataManagementService);
    }

    /**
     * Get ServerIdpManagementService.
     *
     * @return ServerIdpManagementService
     */
    public static ServerIdpManagementService getServerIdpManagementService() {

        return ServerIdpManagementServiceHolder.SERVICE;
    }

    private static IdentityProviderManager getIdentityProviderManager() {

        IdentityProviderManager service = IdentityProviderServiceHolder.getIdentityProviderManager();
        if (service == null) {
            throw new IllegalStateException("IdentityProviderManager is not available from OSGi context.");
        }

        return service;
    }

    private static ClaimMetadataManagementService getClaimMetadataManagementService() {

        ClaimMetadataManagementService service = IdentityProviderServiceHolder.getClaimMetadataManagementService();
        if (service == null) {
            throw new IllegalStateException("ClaimMetadataManagementService is not available from OSGi context.");
        }

        return service;
    }

    private static TemplateManager getTemplateManager() {

        TemplateManager service = IdentityProviderServiceHolder.getTemplateManager();
        if (service == null) {
            throw new IllegalStateException("TemplateManager is not available from OSGi context.");
        }

        return service;
    }
}
