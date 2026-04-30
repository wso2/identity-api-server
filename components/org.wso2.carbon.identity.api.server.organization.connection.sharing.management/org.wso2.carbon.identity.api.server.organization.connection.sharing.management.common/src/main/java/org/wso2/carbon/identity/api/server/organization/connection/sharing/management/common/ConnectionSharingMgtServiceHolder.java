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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.organization.management.organization.connection.sharing.ConnectionSharingPolicyHandlerService;

/**
 * Holds the services which the connection sharing management API component is using.
 */
public class ConnectionSharingMgtServiceHolder {

    private ConnectionSharingMgtServiceHolder() {

    }

    private static class ConnectionSharingPolicyHandlerServiceHolder {

        private static final ConnectionSharingPolicyHandlerService SERVICE =
                (ConnectionSharingPolicyHandlerService) PrivilegedCarbonContext
                        .getThreadLocalCarbonContext()
                        .getOSGiService(ConnectionSharingPolicyHandlerService.class, null);
    }

    /**
     * Get ConnectionSharingPolicyHandlerService.
     *
     * @return ConnectionSharingPolicyHandlerService.
     */
    public static ConnectionSharingPolicyHandlerService getConnectionSharingPolicyHandlerService() {

        return ConnectionSharingMgtServiceHolder.ConnectionSharingPolicyHandlerServiceHolder.SERVICE;
    }
}
