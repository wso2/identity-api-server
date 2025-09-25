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

package org.wso2.carbon.identity.api.server.credential.management.v1.factories;

import org.wso2.carbon.identity.api.server.credential.management.common.impl.CredentialManagementServiceImpl;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementService;
import org.wso2.carbon.identity.api.server.credential.management.v1.core.ServerCredentialManagementService;

/**
 * Factory class for ServerCredentialManagementService.
 */
public class ServerCredentialManagementServiceFactory {

    private static final ServerCredentialManagementService SERVICE;

    private ServerCredentialManagementServiceFactory() {

    }

    static {

        CredentialManagementService credentialManagementService = new CredentialManagementServiceImpl();
        SERVICE = new ServerCredentialManagementService(credentialManagementService);
    }

    /**
     * Get ServerCredentialManagementService instance.
     *
     * @return ServerCredentialManagementService.
     */
    public static ServerCredentialManagementService getServerCredentialManagementService() {

        return SERVICE;
    }
}
