package org.wso2.carbon.identity.api.server.configs.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.configs.common.ConfigsServiceHolder;
import org.wso2.carbon.identity.api.server.configs.v1.core.ServerConfigManagementService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;
import org.wso2.carbon.identity.oauth.dcr.DCRConfigurationMgtService;
import org.wso2.carbon.identity.oauth2.impersonation.services.ImpersonationConfigMgtService;
import org.wso2.carbon.identity.oauth2.token.handler.clientauth.jwt.core.JWTClientAuthenticatorMgtService;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;
import org.wso2.carbon.logging.service.RemoteLoggingConfigService;

import static org.wso2.carbon.identity.api.server.configs.common.ConfigsServiceHolder.getIdentityProviderManager;

/**
 * Factory class for ServerConfigManagementService.
 */
public class ServerConfigManagementServiceFactory {

    private static final Log log = LogFactory.getLog(ServerConfigManagementServiceFactory.class);
    private static final ServerConfigManagementService SERVICE;

    static {
        log.info("Initializing ServerConfigManagementService factory.");
        
        ApplicationManagementService applicationManagementService = ConfigsServiceHolder
                .getApplicationManagementService();
        IdentityProviderManager identityProviderManager = getIdentityProviderManager();
        CORSManagementService corsManagementService = ConfigsServiceHolder.getCorsManagementService();
        RemoteLoggingConfigService remoteLoggingConfigService = ConfigsServiceHolder.getRemoteLoggingConfigService();
        ImpersonationConfigMgtService impersonationConfigMgtService = ConfigsServiceHolder
                .getImpersonationConfigMgtService();
        JWTClientAuthenticatorMgtService jwtClientAuthenticatorMgtService = ConfigsServiceHolder
                .getJWTClientAuthenticatorMgtService();
        DCRConfigurationMgtService dcrConfigurationMgtService = ConfigsServiceHolder.getDcrConfigurationMgtService();

        if (applicationManagementService == null) {
            log.error("ApplicationManagementService is not available from OSGi context.");
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }

        if (identityProviderManager == null) {
            log.error("IdentityProviderManager is not available from OSGi context.");
            throw new IllegalStateException("IdentityProviderManager is not available from OSGi context.");
        }

        if (corsManagementService == null) {
            log.error("CORSManagementService is not available from OSGi context.");
            throw new IllegalStateException("CORSManagementService is not available from OSGi context.");
        }

        if (remoteLoggingConfigService == null) {
            log.error("RemoteLoggingConfigService is not available from OSGi context.");
            throw new IllegalStateException("RemoteLoggingConfigService is not available from OSGi context.");
        }

        if (impersonationConfigMgtService == null) {
            log.error("ImpersonationConfigMgtService is not available from OSGi context.");
            throw new IllegalStateException("ImpersonationConfigMgtService is not available from OSGi context.");
        }

        if (jwtClientAuthenticatorMgtService == null) {
            log.error("JWTClientAuthenticatorMgtService is not available from OSGi context.");
            throw new IllegalStateException("JWTClientAuthenticatorMgtService is not available from OSGi context.");
        }

        if (dcrConfigurationMgtService == null) {
            log.error("DCRConfigurationMgtService is not available from OSGi context.");
            throw new IllegalStateException("DCRConfigurationMgtService is not available from OSGi context.");
        }

        SERVICE = new ServerConfigManagementService(applicationManagementService, identityProviderManager,
                corsManagementService,
                remoteLoggingConfigService,
                impersonationConfigMgtService,
                dcrConfigurationMgtService,
                jwtClientAuthenticatorMgtService);
        
        log.info("ServerConfigManagementService initialized successfully.");
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
