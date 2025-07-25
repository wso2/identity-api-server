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

package org.wso2.carbon.identity.api.server.keystore.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreManagamentDataHolder;
import org.wso2.carbon.identity.api.server.keystore.management.v1.core.KeyStoreService;
import org.wso2.carbon.security.keystore.KeyStoreManagementService;

/**
 * Factory class for KeyStoreService.
 */
public class KeyStoreServiceFactory {

    private static final Log LOG = LogFactory.getLog(KeyStoreServiceFactory.class);
    private static final KeyStoreService SERVICE;

    static {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing KeyStoreService factory.");
        }
        KeyStoreManagementService keyStoreManagementService = KeyStoreManagamentDataHolder.getKeyStoreManager();

        if (keyStoreManagementService == null) {
            LOG.error("KeyStoreManagementService is not available from OSGi context.");
            throw new IllegalStateException("KeyStoreManagementService is not available from OSGi context.");
        }

        SERVICE = new KeyStoreService(keyStoreManagementService);
        LOG.info("KeyStoreService factory initialized successfully.");
    }

    /**
     * Get KeyStoreService service.
     *
     * @return KeyStoreService service.
     */
    public static KeyStoreService getKeyStoreService() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving KeyStoreService instance.");
        }
        return SERVICE;
    }
}
