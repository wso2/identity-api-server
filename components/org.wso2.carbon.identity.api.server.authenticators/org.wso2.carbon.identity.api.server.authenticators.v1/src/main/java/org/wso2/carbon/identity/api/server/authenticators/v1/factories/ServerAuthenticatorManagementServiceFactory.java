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

package org.wso2.carbon.identity.api.server.authenticators.v1.factories;

import org.wso2.carbon.identity.api.server.authenticators.common.AuthenticatorsServiceHolder;
import org.wso2.carbon.identity.api.server.authenticators.v1.core.ServerAuthenticatorManagementService;
import org.wso2.carbon.identity.application.common.ApplicationAuthenticatorService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.idp.mgt.IdpManager;

/**
 * Factory class for ServerAuthenticatorManagementService.
 */
public class ServerAuthenticatorManagementServiceFactory {

    private static final ServerAuthenticatorManagementService SERVICE;

    static {
        ApplicationManagementService applicationManagementService = AuthenticatorsServiceHolder
                .getApplicationManagementService();
        IdpManager idpManager = AuthenticatorsServiceHolder.getIdentityProviderManager();
        ApplicationAuthenticatorService applicationAuthenticatorService = AuthenticatorsServiceHolder
                .getApplicationAuthenticatorService();

        if (applicationManagementService == null) {
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }

        if (idpManager == null) {
            throw new IllegalStateException("IdpManager is not available from OSGi context.");
        }

        SERVICE = new ServerAuthenticatorManagementService(applicationManagementService, idpManager,
                applicationAuthenticatorService);
    }

    /**
     * Get ServerAuthenticatorManagementService.
     *
     * @return ServerAuthenticatorManagementService.
     */
    public static ServerAuthenticatorManagementService getServerAuthenticatorManagementService() {

        return SERVICE;
    }
}
