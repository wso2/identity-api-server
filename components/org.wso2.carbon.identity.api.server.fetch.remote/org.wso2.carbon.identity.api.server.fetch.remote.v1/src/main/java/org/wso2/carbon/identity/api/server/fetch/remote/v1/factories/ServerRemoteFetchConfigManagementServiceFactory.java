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

package org.wso2.carbon.identity.api.server.fetch.remote.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.fetch.remote.common.RemoteFetchServiceHolder;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.core.ServerRemoteFetchConfigManagementService;
import org.wso2.carbon.identity.remotefetch.common.RemoteFetchConfigurationService;

/**
 * Remote Fetch Config Management Service Factory.
 */
public class ServerRemoteFetchConfigManagementServiceFactory {

    private static final Log log = LogFactory.getLog(ServerRemoteFetchConfigManagementServiceFactory.class);
    private static final ServerRemoteFetchConfigManagementService SERVICE;

    static {
        log.debug("Initializing ServerRemoteFetchConfigManagementService");
        RemoteFetchConfigurationService remoteFetchConfigurationService = RemoteFetchServiceHolder
                .getRemoteFetchConfigurationService();

        if (remoteFetchConfigurationService == null) {
            log.error("RemoteFetchConfigurationService is not available from OSGi context");
            throw new IllegalStateException("RemoteFetchConfigurationService is not available from OSGi context.");
        }

        SERVICE = new ServerRemoteFetchConfigManagementService(remoteFetchConfigurationService);
        log.info("ServerRemoteFetchConfigManagementService initialized successfully");
    }

    /**
     * Get RemoteFetchConfigurationService.
     *
     * @return RemoteFetchConfigurationService
     */
    public static ServerRemoteFetchConfigManagementService getServerRemoteFetchConfigManagementService() {

        return SERVICE;
    }
}
