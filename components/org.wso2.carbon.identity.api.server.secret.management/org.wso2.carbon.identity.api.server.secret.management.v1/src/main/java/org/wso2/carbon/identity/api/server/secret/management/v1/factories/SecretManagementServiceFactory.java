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

package org.wso2.carbon.identity.api.server.secret.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.secret.management.v1.core.SecretManagementService;
import org.wso2.carbon.identity.secret.mgt.core.SecretManager;

/**
 * Factory class for SecretManagementService.
 */
public class SecretManagementServiceFactory {

    private static final Log log = LogFactory.getLog(SecretManagementServiceFactory.class);
    private static final SecretManagementService SERVICE;

    static {
        SecretManager secretManager = (SecretManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(SecretManager.class, null);

        if (secretManager == null) {
            log.error("SecretManager is not available from OSGi context.");
            throw new IllegalStateException("SecretManager is not available from OSGi context.");
        }

        SERVICE = new SecretManagementService(secretManager);
        if (log.isDebugEnabled()) {
            log.debug("SecretManagementService factory initialized successfully.");
        }
    }

    /**
     * Get SecretManagementService.
     *
     * @return SecretManagementService.
     */
    public static SecretManagementService getSecretManagementService() {

        return SERVICE;
    }
}
