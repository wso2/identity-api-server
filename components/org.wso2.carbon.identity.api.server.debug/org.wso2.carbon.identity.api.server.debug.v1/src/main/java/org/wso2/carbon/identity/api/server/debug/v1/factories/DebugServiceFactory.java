/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.factories;

import org.wso2.carbon.identity.api.server.debug.common.DebugServiceHolder;
import org.wso2.carbon.identity.api.server.debug.v1.core.DebugService;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;

/**
 * Factory class for DebugService.
 */
public class DebugServiceFactory {

    private static final DebugService SERVICE;

    static {
        DebugRequestCoordinator coordinator = DebugServiceHolder.getDebugRequestCoordinator();
        if (coordinator == null) {
            throw new IllegalStateException(
                    "DebugRequestCoordinator is not available from OSGi context.");
        }
        SERVICE = new DebugService(coordinator);
    }

    private DebugServiceFactory() {

    }

    /**
     * Get DebugService instance.
     *
     * @return DebugService instance.
     */
    public static DebugService getDebugService() {

        return SERVICE;
    }
}
