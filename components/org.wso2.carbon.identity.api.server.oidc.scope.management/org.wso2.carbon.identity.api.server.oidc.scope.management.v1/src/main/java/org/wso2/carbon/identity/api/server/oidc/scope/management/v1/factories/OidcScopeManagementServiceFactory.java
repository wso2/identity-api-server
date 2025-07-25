/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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
package org.wso2.carbon.identity.api.server.oidc.scope.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.oidc.scope.management.common.OIDCScopeManagementServiceHolder;
import org.wso2.carbon.identity.api.server.oidc.scope.management.v1.core.OidcScopeManagementService;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;

/**
 * Factory class for OidcScopeManagementService.
 */
public class OidcScopeManagementServiceFactory {
    private static final Log LOG = LogFactory.getLog(OidcScopeManagementServiceFactory.class);
    private static final OidcScopeManagementService SERVICE;

    static {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing OidcScopeManagementService");
        }
        OAuthAdminServiceImpl oAuthAdminService = OIDCScopeManagementServiceHolder
                .getOAuthAdminService();

        if (oAuthAdminService == null) {
            LOG.error("OAuthAdminServiceImpl is not available from OSGi context");
            throw new IllegalStateException("OAuthAdminServiceImpl is not available from OSGi context.");
        }

        SERVICE = new OidcScopeManagementService(oAuthAdminService);
        if (LOG.isDebugEnabled()) {
            LOG.debug("OidcScopeManagementService initialized successfully");
        }
    }

    /**
     * Get the OidcScopeManagementService instance.
     *
     * @return OidcScopeManagementService
     */
    public static OidcScopeManagementService getPermissionManagementService() {

        return SERVICE;
    }
}
