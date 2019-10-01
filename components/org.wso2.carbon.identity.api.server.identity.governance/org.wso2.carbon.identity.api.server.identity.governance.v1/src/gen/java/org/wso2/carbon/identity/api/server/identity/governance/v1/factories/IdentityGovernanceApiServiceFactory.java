package org.wso2.carbon.identity.api.server.identity.governance.v1.factories;

import org.wso2.carbon.identity.api.server.identity.governance.v1.IdentityGovernanceApiService;
import org.wso2.carbon.identity.api.server.identity.governance.v1.impl.IdentityGovernanceApiServiceImpl;

public class IdentityGovernanceApiServiceFactory {

   private final static IdentityGovernanceApiService service = new IdentityGovernanceApiServiceImpl();

   public static IdentityGovernanceApiService getIdentityGovernanceApi()
   {
      return service;
   }
}
