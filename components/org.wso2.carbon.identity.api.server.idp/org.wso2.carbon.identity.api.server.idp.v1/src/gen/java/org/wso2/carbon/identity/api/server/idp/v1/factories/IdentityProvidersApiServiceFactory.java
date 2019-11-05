package org.wso2.carbon.identity.api.server.idp.v1.factories;

import org.wso2.carbon.identity.api.server.idp.v1.IdentityProvidersApiService;
import org.wso2.carbon.identity.api.server.idp.v1.impl.IdentityProvidersApiServiceImpl;

public class IdentityProvidersApiServiceFactory {

   private final static IdentityProvidersApiService service = new IdentityProvidersApiServiceImpl();

   public static IdentityProvidersApiService getIdentityProvidersApi()
   {
      return service;
   }
}
