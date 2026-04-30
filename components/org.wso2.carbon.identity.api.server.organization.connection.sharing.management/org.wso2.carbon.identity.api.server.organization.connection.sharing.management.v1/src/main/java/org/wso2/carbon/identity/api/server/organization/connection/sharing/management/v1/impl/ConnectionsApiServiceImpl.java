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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.ConnectionsApiService;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.core.ConnectionsApiServiceCore;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.factories.ConnectionsApiServiceCoreFactory;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionUnshareWithAllRequestBody;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.organization.connection.sharing.management.common.constants.ConnectionSharingMgtConstants.ErrorMessage.ERROR_INITIATING_CONNECTIONS_API_SERVICE;

/**
 * Implementation of the connection sharing management APIs V1.
 */
public class ConnectionsApiServiceImpl implements ConnectionsApiService {

    private static final Log LOG = LogFactory.getLog(ConnectionsApiServiceImpl.class);
    private final ConnectionsApiServiceCore connectionsApiServiceCore;

    public ConnectionsApiServiceImpl() {

        try {
            this.connectionsApiServiceCore = ConnectionsApiServiceCoreFactory.getConnectionsApiServiceCore();
            LOG.debug("ConnectionsApiServiceImpl V1 initialized successfully.");
        } catch (IllegalStateException e) {
            LOG.error("Failed to initialize ConnectionsApiServiceImpl.", e);
            throw new RuntimeException(ERROR_INITIATING_CONNECTIONS_API_SERVICE.getMessage(), e);
        }
    }

    @Override
    public Response shareConnectionsWithSelected(
            ConnectionShareSelectedRequestBody connectionShareSelectedRequestBody) {

        return connectionsApiServiceCore.shareConnectionsWithSelectedOrgs(connectionShareSelectedRequestBody);
    }

    @Override
    public Response shareConnectionsWithAll(ConnectionShareWithAllRequestBody connectionShareWithAllRequestBody) {

        return connectionsApiServiceCore.shareConnectionsWithAllOrgs(connectionShareWithAllRequestBody);
    }

    @Override
    public Response unshareConnectionsFromSelected(
            ConnectionUnshareSelectedRequestBody connectionUnshareSelectedRequestBody) {

        return connectionsApiServiceCore.unshareConnectionsFromSelectedOrgs(connectionUnshareSelectedRequestBody);
    }

    @Override
    public Response unshareConnectionsFromAll(
            ConnectionUnshareWithAllRequestBody connectionUnshareWithAllRequestBody) {

        return connectionsApiServiceCore.unshareConnectionsFromAllOrgs(connectionUnshareWithAllRequestBody);
    }

    @Override
    public Response getConnectionSharedOrganizations(String connectionId, String before, String after, String filter,
                                                     Integer limit, Boolean recursive, String attributes) {

        return connectionsApiServiceCore.getConnectionSharedOrganizations(connectionId, before, after, filter, limit,
                recursive, attributes);
    }
}
