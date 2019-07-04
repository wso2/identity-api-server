package org.wso2.carbon.identity.rest.api.server.challenge.v1.factories;

import org.wso2.carbon.identity.rest.api.server.challenge.v1.ChallengesApiService;
import org.wso2.carbon.identity.rest.api.server.challenge.v1.impl.ChallengesApiServiceImpl;

public class ChallengesApiServiceFactory {

   private final static ChallengesApiService service = new ChallengesApiServiceImpl();

   public static ChallengesApiService getChallengesApi()
   {
      return service;
   }
}
