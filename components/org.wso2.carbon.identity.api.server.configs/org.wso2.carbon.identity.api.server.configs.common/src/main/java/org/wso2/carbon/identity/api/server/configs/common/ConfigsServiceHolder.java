/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.configs.common;

import org.wso2.carbon.base.api.ServerConfigurationService;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;
import org.wso2.carbon.identity.oauth.dcr.DCRConfigurationMgtService;
import org.wso2.carbon.identity.oauth2.impersonation.services.ImpersonationConfigMgtService;
import org.wso2.carbon.identity.oauth2.token.handler.clientauth.jwt.core.JWTClientAuthenticatorMgtService;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;
import org.wso2.carbon.idp.mgt.IdpManager;
import org.wso2.carbon.logging.service.RemoteLoggingConfigService;

/**
 * Service holder class for server configuration related services.
 */
public class ConfigsServiceHolder {

    private ConfigsServiceHolder() {}

    private static class ApplicationManagementServiceHolder {

        static final ApplicationManagementService SERVICE = (ApplicationManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ApplicationManagementService.class, null);
    }

    private static class IdentityProviderManagerHolder {

        static final IdentityProviderManager SERVICE = (IdentityProviderManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(IdpManager.class, null);
    }

    private static class CORSManagementServiceHolder {

        static final CORSManagementService SERVICE = (CORSManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(CORSManagementService.class, null);
    }

    private static class RemoteLoggingConfigServiceHolder {

        static final RemoteLoggingConfigService SERVICE = (RemoteLoggingConfigService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(RemoteLoggingConfigService.class, null);
    }

    private static class ImpersonationConfigMgtServiceHolder {

        static final ImpersonationConfigMgtService SERVICE = (ImpersonationConfigMgtService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ImpersonationConfigMgtService.class, null);
    }

    private static class DCRConfigurationMgtServiceHolder {

        static final DCRConfigurationMgtService SERVICE = (DCRConfigurationMgtService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(DCRConfigurationMgtService.class, null);
    }

    private static class JWTClientAuthenticatorMgtServiceHolder {

        static final JWTClientAuthenticatorMgtService SERVICE =
                (JWTClientAuthenticatorMgtService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(JWTClientAuthenticatorMgtService.class, null);
    }

    private static class ServerConfigurationServiceHolder {

        static final ServerConfigurationService SERVICE =
                (ServerConfigurationService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(ServerConfigurationService.class, null);
    }

    /**
     * Get ApplicationManagementService osgi service.
     *
     * @return ApplicationManagementService
     */
    public static ApplicationManagementService getApplicationManagementService() {

        return ApplicationManagementServiceHolder.SERVICE;
    }

    /**
     * Get IdentityProviderManager osgi service.
     *
     * @return IdentityProviderManager
     */
    public static IdentityProviderManager getIdentityProviderManager() {

        return IdentityProviderManagerHolder.SERVICE;
    }

    /**
     * Get CORSManagementService osgi service.
     *
     * @return CORSManagementService
     */
    public static CORSManagementService getCorsManagementService() {

        return CORSManagementServiceHolder.SERVICE;
    }

    /**
     * Get RemoteLoggingConfigService osgi service.
     *
     * @return RemoteLoggingConfigService
     */
    public static RemoteLoggingConfigService getRemoteLoggingConfigService() {

        return RemoteLoggingConfigServiceHolder.SERVICE;
    }

    /**
     * Get Impersonation Config Mgt osgi service.
     *
     * @return RemoteLoggingConfigService
     */
    public static ImpersonationConfigMgtService getImpersonationConfigMgtService() {

        return ImpersonationConfigMgtServiceHolder.SERVICE;
    }

    /**
     * Get DCRConfigurationMgtService osgi service.
     *
     * @return DCRConfigurationMgtService
     */
    public static DCRConfigurationMgtService getDcrConfigurationMgtService() {

        return DCRConfigurationMgtServiceHolder.SERVICE;
    }

    /**
     * Get JWTClientAuthenticatorMgtService osgi service.
     *
     * @return JWTClientAuthenticatorMgtService
     */
    public static JWTClientAuthenticatorMgtService getJWTClientAuthenticatorMgtService() {

        return JWTClientAuthenticatorMgtServiceHolder.SERVICE;
    }

    /**
     * Get ServerConfigurationService osgi service.
     *
     * @return ServerConfigurationService
     */
    public static ServerConfigurationService getServerConfigurationService() {

        return ServerConfigurationServiceHolder.SERVICE;
    }
}
