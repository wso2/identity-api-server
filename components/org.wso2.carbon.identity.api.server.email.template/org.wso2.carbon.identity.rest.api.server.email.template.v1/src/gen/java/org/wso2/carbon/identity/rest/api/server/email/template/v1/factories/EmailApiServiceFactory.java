package org.wso2.carbon.identity.rest.api.server.email.template.v1.factories;

import org.wso2.carbon.identity.rest.api.server.email.template.v1.EmailApiService;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.impl.EmailApiServiceImpl;

public class EmailApiServiceFactory {

   private final static EmailApiService service = new EmailApiServiceImpl();

   public static EmailApiService getEmailApi()
   {
      return service;
   }
}
