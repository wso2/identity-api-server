/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.keystore.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.security.keystore.KeyStoreManagementService;

/**
 * Service holder class for keystore management.
 */
public class KeyStoreManagamentDataHolder {

    private static final Log log = LogFactory.getLog(KeyStoreManagamentDataHolder.class);

    public KeyStoreManagamentDataHolder() {}

    private static class KeyStoreManagementServiceHolder {

        static final KeyStoreManagementService SERVICE = (KeyStoreManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(KeyStoreManagementService.class, null);
    }

    /**
     * Get KeyStoreManagementService OSGi service.
     *
     * @return KeyStoreManagementService.
     */
    public static KeyStoreManagementService getKeyStoreManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving KeyStoreManagementService from OSGi context.");
        }
        KeyStoreManagementService service = KeyStoreManagementServiceHolder.SERVICE;
        if (service == null && log.isWarnEnabled()) {
            log.warn("KeyStoreManagementService is not available in OSGi context.");
        }
        return service;
    }
}
