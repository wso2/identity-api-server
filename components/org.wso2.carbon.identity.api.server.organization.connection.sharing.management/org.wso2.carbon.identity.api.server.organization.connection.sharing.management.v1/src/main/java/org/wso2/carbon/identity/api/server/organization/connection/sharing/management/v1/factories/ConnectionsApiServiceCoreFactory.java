/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.ConnectionSharingMgtServiceHolder;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.core.ConnectionsApiServiceCore;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.ConnectionSharingPolicyHandlerService;

/**
 * Factory class for ConnectionsApiService V1.
 */
public class ConnectionsApiServiceCoreFactory {

    private static final Log LOG = LogFactory.getLog(ConnectionsApiServiceCoreFactory.class);
    private static final ConnectionsApiServiceCore SERVICE;

    static {
        LOG.debug("Initializing ConnectionsApiServiceCoreFactory");
        ConnectionSharingPolicyHandlerService connectionSharingPolicyHandlerService =
                ConnectionSharingMgtServiceHolder.getConnectionSharingPolicyHandlerService();
        if (connectionSharingPolicyHandlerService == null) {
            LOG.error("Failed to initialize ConnectionsApiServiceCoreFactory: " +
                    "ConnectionSharingPolicyHandlerService is not available from OSGi context");
            throw new IllegalStateException(
                    "ConnectionSharingPolicyHandlerService is not available from the OSGi context.");
        }
        SERVICE = new ConnectionsApiServiceCore(connectionSharingPolicyHandlerService);
    }

    /**
     * Get ConnectionsApiServiceCore.
     *
     * @return ConnectionsApiServiceCore.
     */
    public static ConnectionsApiServiceCore getConnectionsApiServiceCore() {

        return SERVICE;
    }
}
