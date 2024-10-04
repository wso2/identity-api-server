package org.wso2.carbon.identity.api.server.permission.management.v1.factories;

import org.wso2.carbon.identity.api.server.permission.management.v1.PermissionManagementApiService;
import org.wso2.carbon.identity.api.server.permission.management.v1.impl.PermissionManagementApiServiceImpl;

public class PermissionManagementApiServiceFactory {

   private final static PermissionManagementApiService SERVICE = new PermissionManagementApiServiceImpl();

   public static PermissionManagementApiService getPermissionManagementApi()
   {
      return SERVICE;
   }
}
