package org.wso2.carbon.identity.api.server.identity.governance.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.identity.governance.v1.IdentityGovernanceApiService;
import org.wso2.carbon.identity.api.server.identity.governance.v1.core.ServerIdentityGovernanceService;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorsPatchReq;

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
}
