package org.wso2.carbon.identity.api.server.jwt.issuers.common;

import org.wso2.carbon.idp.mgt.IdentityProviderManager;

/**
 * Service holder class for JWT Issuer.
 */
public class JWTIssuerServiceHolder {

    private static IdentityProviderManager identityProviderManager;

    /**
     * Get IdentityProviderManager osgi service.
     *
     * @return IdentityProviderManager
     */
    public static IdentityProviderManager getIdentityProviderManager() {

        return identityProviderManager;
    }

    /**
     * Set IdentityProviderManager osgi service.
     *
     * @param identityProviderManager IdentityProviderManager.
     */
    public static void setIdentityProviderManager(IdentityProviderManager identityProviderManager) {

        JWTIssuerServiceHolder.identityProviderManager = identityProviderManager;
    }
}
