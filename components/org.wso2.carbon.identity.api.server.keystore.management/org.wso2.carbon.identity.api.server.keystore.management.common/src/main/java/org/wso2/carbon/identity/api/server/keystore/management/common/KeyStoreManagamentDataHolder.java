package org.wso2.carbon.identity.api.server.keystore.management.common;

import org.wso2.carbon.security.keystore.KeyStoreManagementService;

/**
 * Service holder class for keystore management.
 */
public class KeyStoreManagamentDataHolder {

    private static KeyStoreManagementService keyStoreManager;

    public static KeyStoreManagementService getKeyStoreManager() {
        return keyStoreManager;
    }

    public static void setKeyStoreManager(KeyStoreManagementService keyStoreManager) {
        KeyStoreManagamentDataHolder.keyStoreManager = keyStoreManager;
    }
}
