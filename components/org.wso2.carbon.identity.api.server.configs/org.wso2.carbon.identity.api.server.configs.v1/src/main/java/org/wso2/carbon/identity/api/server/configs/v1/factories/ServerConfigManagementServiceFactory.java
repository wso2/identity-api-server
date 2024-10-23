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

package org.wso2.carbon.identity.api.server.configs.v1.factories;

import org.wso2.carbon.identity.api.server.configs.common.ConfigsServiceHolder;
import org.wso2.carbon.identity.api.server.configs.v1.core.ServerConfigManagementService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;
import org.wso2.carbon.identity.oauth.dcr.DCRConfigurationMgtService;
import org.wso2.carbon.identity.oauth2.impersonation.services.ImpersonationConfigMgtService;
import org.wso2.carbon.identity.oauth2.token.handler.clientauth.jwt.core.JWTClientAuthenticatorMgtService;
import org.wso2.carbon.idp.mgt.IdpManager;
import org.wso2.carbon.logging.service.RemoteLoggingConfigService;

/**
 * Factory class for ServerConfigManagementService.
 */
public class ServerConfigManagementServiceFactory {

    private ServerConfigManagementServiceFactory() {

    }

    private static class ServerConfigManagementServiceHolder {

        private static final ServerConfigManagementService SERVICE = createServiceInstance();
    }

    private static ServerConfigManagementService createServiceInstance() {

        ApplicationManagementService applicationManagementService = getApplicationManagementService();
        IdpManager idpManager = getIdpManager();
        CORSManagementService corsManagementService = getCorsManagementService();
        RemoteLoggingConfigService remoteLoggingConfigService = getRemoteLoggingConfigService();
        ImpersonationConfigMgtService impersonationConfigMgtService = getImpersonationConfigMgtService();
        JWTClientAuthenticatorMgtService jwtClientAuthenticatorMgtService = getJWTClientAuthenticatorMgtService();
        DCRConfigurationMgtService dcrConfigurationMgtService = getDcrConfigurationMgtService();

        return new ServerConfigManagementService(applicationManagementService, idpManager,
                corsManagementService, remoteLoggingConfigService, impersonationConfigMgtService,
                dcrConfigurationMgtService, jwtClientAuthenticatorMgtService);
    }

    /**
     * Get ServerConfigManagementService instance.
     *
     * @return ServerConfigManagementService.
     */
    public static ServerConfigManagementService getServerConfigManagementService() {

        return ServerConfigManagementServiceHolder.SERVICE;
    }

    private static ApplicationManagementService getApplicationManagementService() {

        ApplicationManagementService service = ConfigsServiceHolder.getApplicationManagementService();
        if (service == null) {
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }

        return service;
    }

    private static IdpManager getIdpManager() {

        IdpManager service = ConfigsServiceHolder.getIdpManager();
        if (service == null) {
            throw new IllegalStateException("IdentityProviderManager is not available from OSGi context.");
        }

        return service;
    }

    private static CORSManagementService getCorsManagementService() {

        CORSManagementService service = ConfigsServiceHolder.getCorsManagementService();
        if (service == null) {
            throw new IllegalStateException("CORSManagementService is not available from OSGi context.");
        }

        return service;
    }

    private static RemoteLoggingConfigService getRemoteLoggingConfigService() {

        RemoteLoggingConfigService service = ConfigsServiceHolder.getRemoteLoggingConfigService();
        if (service == null) {
            throw new IllegalStateException("RemoteLoggingConfigService is not available from OSGi context.");
        }

        return service;
    }

    private static ImpersonationConfigMgtService getImpersonationConfigMgtService() {

        ImpersonationConfigMgtService service = ConfigsServiceHolder.getImpersonationConfigMgtService();
        if (service == null) {
            throw new IllegalStateException("ImpersonationConfigMgtService is not available from OSGi context.");
        }

        return service;
    }

    private static JWTClientAuthenticatorMgtService getJWTClientAuthenticatorMgtService() {

        JWTClientAuthenticatorMgtService service = ConfigsServiceHolder.getJWTClientAuthenticatorMgtService();
        if (service == null) {
            throw new IllegalStateException("JWTClientAuthenticatorMgtService is not available from OSGi context.");
        }

        return service;
    }

    private static DCRConfigurationMgtService getDcrConfigurationMgtService() {

        DCRConfigurationMgtService service = ConfigsServiceHolder.getDcrConfigurationMgtService();
        if (service == null) {
            throw new IllegalStateException("DCRConfigurationMgtService is not available from OSGi context.");
        }

        return service;
    }
}
