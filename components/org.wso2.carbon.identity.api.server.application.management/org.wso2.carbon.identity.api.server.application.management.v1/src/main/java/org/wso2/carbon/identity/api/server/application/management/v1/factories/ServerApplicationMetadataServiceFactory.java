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

package org.wso2.carbon.identity.api.server.application.management.v1.factories;

import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationMetadataService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth.ciba.api.CibaAuthServiceImpl;
import org.wso2.carbon.identity.sso.saml.SAMLSSOConfigServiceImpl;
import org.wso2.carbon.security.sts.service.STSAdminServiceInterface;

/**
 * Factory class for ServerApplicationMetadataService.
 */
public class ServerApplicationMetadataServiceFactory {

    private static final ServerApplicationMetadataService SERVICE;

    static {
        ApplicationManagementService applicationManagementService = ApplicationManagementServiceHolder
                .getApplicationManagementService();
        SAMLSSOConfigServiceImpl samlSSOConfigService = ApplicationManagementServiceHolder.getSamlssoConfigService();
        OAuthAdminServiceImpl oAuthAdminService = ApplicationManagementServiceHolder.getOAuthAdminService();
        STSAdminServiceInterface sTSAdminServiceInterface = ApplicationManagementServiceHolder.getStsAdminService();
        CibaAuthServiceImpl cibaAuthService = ApplicationManagementServiceHolder.getCibaAuthService();

        if (applicationManagementService == null) {
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }

        if (samlSSOConfigService == null) {
            throw new IllegalStateException("SAMLSSOConfigServiceImpl is not available from OSGi context.");
        }

        if (oAuthAdminService == null) {
            throw new IllegalStateException("OAuthAdminServiceImpl is not available from OSGi context.");
        }

        // Null check for STSAdminServiceInterface is not mandatory as per the previous implementation.

        SERVICE = new ServerApplicationMetadataService(applicationManagementService, samlSSOConfigService,
                oAuthAdminService, sTSAdminServiceInterface, cibaAuthService);
    }

    /**
     * Get ServerAPIResourceManagementService instance.
     *
     * @return ServerAPIResourceManagementService.
     */
    public static ServerApplicationMetadataService getServerApplicationMetadataService() {

        return SERVICE;
    }

}
