/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idp.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.claim.metadata.mgt.ClaimMetadataManagementService;
import org.wso2.carbon.identity.template.mgt.TemplateManager;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;

/**
 * Service holder class for identity providers.
 */
public class IdentityProviderServiceHolder {

    private static final Log log = LogFactory.getLog(IdentityProviderServiceHolder.class);

    private IdentityProviderServiceHolder() {

    }

    private static class IdentityProviderManagerHolder {

        static final IdentityProviderManager SERVICE = IdentityProviderManager.getInstance();
    }

    private static class ClaimMetadataManagementServiceHolder {

        static final ClaimMetadataManagementService SERVICE = (ClaimMetadataManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ClaimMetadataManagementService.class, null);
    }

    private static class TemplateManagerHolder {

        static final TemplateManager SERVICE = (TemplateManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(TemplateManager.class, null);
    }

    /**
     * Get IdentityProviderManager osgi service.
     *
     * @return IdentityProviderManager.
     */
    public static IdentityProviderManager getIdentityProviderManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdentityProviderManager service instance.");
        }
        if (IdentityProviderManagerHolder.SERVICE == null) {
            log.warn("IdentityProviderManager service is not available.");
        }
        return IdentityProviderManagerHolder.SERVICE;
    }

    /**
     * Get ClaimMetadataManagementService osgi service.
     *
     * @return ClaimMetadataManagementService.
     */
    public static ClaimMetadataManagementService getClaimMetadataManagementService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving ClaimMetadataManagementService service instance.");
        }
        if (ClaimMetadataManagementServiceHolder.SERVICE == null) {
            log.warn("ClaimMetadataManagementService service is not available.");
        }
        return ClaimMetadataManagementServiceHolder.SERVICE;
    }

    /**
     * Get TemplateManager osgi service.
     *
     * @return TemplateManager
     */
    public static TemplateManager getTemplateManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving TemplateManager service instance.");
        }
        if (TemplateManagerHolder.SERVICE == null) {
            log.warn("TemplateManager service is not available.");
        }
        return TemplateManagerHolder.SERVICE;
    }
}
