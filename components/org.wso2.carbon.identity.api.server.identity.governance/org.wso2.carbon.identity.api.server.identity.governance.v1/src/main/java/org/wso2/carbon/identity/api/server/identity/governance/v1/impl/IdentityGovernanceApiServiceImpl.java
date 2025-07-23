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

package org.wso2.carbon.identity.api.server.identity.governance.v1.impl;

import org.wso2.carbon.identity.api.server.identity.governance.v1.IdentityGovernanceApiService;
import org.wso2.carbon.identity.api.server.identity.governance.v1.core.ServerIdentityGovernanceService;
import org.wso2.carbon.identity.api.server.identity.governance.v1.factories.ServerIdentityGovernanceServiceFactory;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.MultipleConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PreferenceSearchAttribute;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PropertyRevertReq;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Identity Governance api service implementation.
 */
public class IdentityGovernanceApiServiceImpl implements IdentityGovernanceApiService {

    private final ServerIdentityGovernanceService identityGovernanceService;

    public IdentityGovernanceApiServiceImpl() {

        try {
            this.identityGovernanceService = ServerIdentityGovernanceServiceFactory
                    .getServerIdentityGovernanceService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating identity governance service.", e);
        }
    }

    @Override
    public Response getCategories(Integer limit, Integer offset, String filter, String sort) {

        return Response.ok().entity(identityGovernanceService.getGovernanceConnectors(limit, offset, filter, sort))
                .build();
    }

    @Override
    public Response getConnectorCategory(String categoryId) {

        return Response.ok().entity(identityGovernanceService.getGovernanceConnectorCategory(categoryId)).build();
    }

    @Override
    public Response getConnectorsOfCategory(String categoryId) {

        return Response.ok().entity(identityGovernanceService.getGovernanceConnectorsByCategory(categoryId)).build();
    }

    @Override
    public Response getConnector(String categoryId, String connectorId) {

        return Response.ok().entity(identityGovernanceService.getGovernanceConnector(categoryId, connectorId)).build();
    }

    @Override
    public Response patchConnector(String categoryId, String connectorId, ConnectorsPatchReq governanceConnector) {

        identityGovernanceService.updateGovernanceConnectorProperty(categoryId, connectorId, governanceConnector);
        return Response.ok().build();
    }

    @Override
    public Response patchConnectorsOfCategory(String categoryId,
                                              MultipleConnectorsPatchReq multipleConnectorsPatchReq) {

        identityGovernanceService.updateGovernanceConnectorProperties(categoryId, multipleConnectorsPatchReq);
        return Response.ok().build();
    }

    @Override
    public Response getPreferenceByPost(List<PreferenceSearchAttribute> preferenceSearchAttribute) {

        return Response.ok().entity(identityGovernanceService.getConfigPreference(preferenceSearchAttribute)).build();
    }

    @Override
    public Response revertConnectorProperties(String categoryId, String connectorId,
                                              PropertyRevertReq propertyRevertReq) {

        identityGovernanceService.revertGovernanceConnectorProperties(categoryId, connectorId, propertyRevertReq);
        return Response.ok().build();
    }
}
