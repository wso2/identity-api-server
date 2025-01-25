/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.secret.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.secret.mgt.core.SecretManager;

/**
 * Service holder class for secret management.
 */
public class SecretManagementServiceHolder {

    private SecretManagementServiceHolder() {

    }

    private static class SecretManagerServiceHolder {

        static final SecretManager SERVICE = (SecretManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(SecretManager.class, null);
    }

    /**
     * Get SecretManager OSGi service.
     *
     * @return secretConfig manager.
     */
    public static SecretManager getSecretConfigManager() {

        return SecretManagerServiceHolder.SERVICE;
    }
}
