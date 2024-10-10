package org.wso2.carbon.identity.api.server.identity.governance.v1.factories;

import org.wso2.carbon.identity.api.server.identity.governance.common.GovernanceDataHolder;
import org.wso2.carbon.identity.api.server.identity.governance.v1.core.ServerIdentityGovernanceService;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Factory class for Server Identity Governance Service.
 */
public class ServerIdentityGovernanceServiceFactory {

    private static final ServerIdentityGovernanceService SERVICE;

    static {
        IdentityGovernanceService identityGovernanceService = GovernanceDataHolder.getIdentityGovernanceService();

        if (identityGovernanceService == null) {
            throw new IllegalStateException("RolePermissionManagementService is not available from OSGi context.");
        }

        SERVICE = new ServerIdentityGovernanceService(identityGovernanceService);
    }

    /**
     * Get IdentityGovernanceService.
     *
     * @return IdentityGovernanceService
     */
    public static ServerIdentityGovernanceService getServerIdentityGovernanceService() {
        return SERVICE;
    }
}
