package org.wso2.carbon.identity.api.server.configs.v1.factories;

import org.wso2.carbon.identity.api.server.configs.common.ConfigsServiceHolder;
import org.wso2.carbon.identity.api.server.configs.v1.core.ServerConfigManagementService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;
import org.wso2.carbon.identity.oauth.dcr.DCRConfigurationMgtService;
import org.wso2.carbon.identity.oauth2.finegrainedauthz.services.FineGrainedAuthzConfigMgtService;
import org.wso2.carbon.identity.oauth2.impersonation.services.ImpersonationConfigMgtService;
import org.wso2.carbon.identity.oauth2.token.handler.clientauth.jwt.core.JWTClientAuthenticatorMgtService;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;
import org.wso2.carbon.logging.service.RemoteLoggingConfigService;

import static org.wso2.carbon.identity.api.server.configs.common.ConfigsServiceHolder.getIdentityProviderManager;

/**
 * Factory class for ServerConfigManagementService.
 */
public class ServerConfigManagementServiceFactory {

    private static final ServerConfigManagementService SERVICE;

    static {
        ApplicationManagementService applicationManagementService = ConfigsServiceHolder
                .getApplicationManagementService();
        IdentityProviderManager identityProviderManager = getIdentityProviderManager();
        CORSManagementService corsManagementService = ConfigsServiceHolder.getCorsManagementService();
        RemoteLoggingConfigService remoteLoggingConfigService = ConfigsServiceHolder.getRemoteLoggingConfigService();
        ImpersonationConfigMgtService impersonationConfigMgtService = ConfigsServiceHolder
                .getImpersonationConfigMgtService();
        JWTClientAuthenticatorMgtService jwtClientAuthenticatorMgtService = ConfigsServiceHolder
                .getJWTClientAuthenticatorMgtService();
        FineGrainedAuthzConfigMgtService fineGrainedAuthzConfigMgtService = ConfigsServiceHolder
                .getFineGrainedAuthzConfigMgtService();
        DCRConfigurationMgtService dcrConfigurationMgtService = ConfigsServiceHolder.getDcrConfigurationMgtService();

        if (applicationManagementService == null) {
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }

        if (identityProviderManager == null) {
            throw new IllegalStateException("IdentityProviderManager is not available from OSGi context.");
        }

        if (corsManagementService == null) {
            throw new IllegalStateException("CORSManagementService is not available from OSGi context.");
        }

        if (remoteLoggingConfigService == null) {
            throw new IllegalStateException("RemoteLoggingConfigService is not available from OSGi context.");
        }

        if (impersonationConfigMgtService == null) {
            throw new IllegalStateException("ImpersonationConfigMgtService is not available from OSGi context.");
        }

        if (jwtClientAuthenticatorMgtService == null) {
            throw new IllegalStateException("JWTClientAuthenticatorMgtService is not available from OSGi context.");
        }

        if (dcrConfigurationMgtService == null) {
            throw new IllegalStateException("DCRConfigurationMgtService is not available from OSGi context.");
        }

        if (fineGrainedAuthzConfigMgtService == null) {
            throw new IllegalStateException("FineGrainedAuthzConfigMgtService is not available from OSGi context.");
        }

        SERVICE = new ServerConfigManagementService(applicationManagementService, identityProviderManager,
                corsManagementService,
                remoteLoggingConfigService,
                impersonationConfigMgtService,
                fineGrainedAuthzConfigMgtService,
                dcrConfigurationMgtService,
                jwtClientAuthenticatorMgtService);
    }

    /**
     * Get ServerConfigManagementService instance.
     *
     * @return ServerConfigManagementService.
     */
    public static ServerConfigManagementService getServerConfigManagementService() {

        return SERVICE;
    }
}
