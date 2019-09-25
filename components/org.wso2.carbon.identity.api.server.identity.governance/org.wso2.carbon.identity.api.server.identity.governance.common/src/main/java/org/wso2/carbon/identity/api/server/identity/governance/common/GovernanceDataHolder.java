package org.wso2.carbon.identity.api.server.identity.governance.common;

import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Service holder class for identity governance.
 */
public class GovernanceDataHolder {

    private static IdentityGovernanceService identityGovernanceService;

    public static IdentityGovernanceService getIdentityGovernanceService() {

        return identityGovernanceService;
    }

    public static void setIdentityGovernanceService(IdentityGovernanceService identityGovernanceService) {

        GovernanceDataHolder.identityGovernanceService = identityGovernanceService;
    }

}
