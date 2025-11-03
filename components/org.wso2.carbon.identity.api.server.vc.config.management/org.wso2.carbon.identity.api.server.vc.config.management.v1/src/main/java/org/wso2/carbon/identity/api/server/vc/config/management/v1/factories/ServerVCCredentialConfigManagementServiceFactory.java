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

package org.wso2.carbon.identity.api.server.vc.config.management.v1.factories;

import org.wso2.carbon.identity.api.server.vc.config.management.common.VCCredentialConfigManagementServiceHolder;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.core.ServerVCCredentialConfigManagementService;
import org.wso2.carbon.identity.vc.config.management.VCCredentialConfigManager;

/**
 * Factory class for {@link ServerVCCredentialConfigManagementService}.
 */
public final class ServerVCCredentialConfigManagementServiceFactory {

    private static final ServerVCCredentialConfigManagementService SERVICE;

    static {
        VCCredentialConfigManager vcCredentialConfigManager = VCCredentialConfigManagementServiceHolder
                .getVCCredentialConfigManager();

        if (vcCredentialConfigManager == null) {
            throw new IllegalStateException("VCCredentialConfigManager is not available from OSGi context.");
        }

        SERVICE = new ServerVCCredentialConfigManagementService(vcCredentialConfigManager);
    }

    /**
     * Get VCCredentialConfig Management Service.
     *
     * @return ServerVCCredentialConfigManagementService.
     */
    public static ServerVCCredentialConfigManagementService getServerVCCredentialConfigManagementService() {

        return SERVICE;
    }
}
