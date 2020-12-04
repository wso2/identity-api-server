/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.identity.governance.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.identity.governance.v1.IdentityGovernanceApiService;
import org.wso2.carbon.identity.api.server.identity.governance.v1.core.ServerIdentityGovernanceService;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PreferenceSearchAttribute;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Identity Governance api service implementation.
 */
public class IdentityGovernanceApiServiceImpl implements IdentityGovernanceApiService {

    @Autowired
    private ServerIdentityGovernanceService identityGovernanceService;

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
    public Response getPreferenceByPost(List<PreferenceSearchAttribute> preferenceSearchAttribute) {

        return Response.ok().entity(identityGovernanceService.getConfigPreference(preferenceSearchAttribute)).build();
    }
}
