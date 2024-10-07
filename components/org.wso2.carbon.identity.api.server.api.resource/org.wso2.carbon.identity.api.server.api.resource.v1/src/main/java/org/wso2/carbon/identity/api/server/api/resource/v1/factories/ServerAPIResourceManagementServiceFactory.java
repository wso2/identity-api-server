package org.wso2.carbon.identity.api.server.api.resource.v1.factories;

import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.api.server.api.resource.common.APIResourceManagementServiceHolder;
import org.wso2.carbon.identity.api.server.api.resource.v1.core.ServerAPIResourceManagementService;

/**
 * Factory class for ServerAPIResourceManagementService.
 */
public class ServerAPIResourceManagementServiceFactory {

    private static final ServerAPIResourceManagementService SERVICE;

    static {
        APIResourceManager apiResourceManager = APIResourceManagementServiceHolder.getApiResourceManager();

        if (apiResourceManager == null) {
            throw new IllegalStateException("APIResourceManager is not available from OSGi context.");
        }

        SERVICE = new ServerAPIResourceManagementService(apiResourceManager);
    }

    /**
     * Get ServerAPIResourceManagementService instance.
     *
     * @return ServerAPIResourceManagementService.
     */
    public static ServerAPIResourceManagementService getServerAPIResourceManagementService() {
        return SERVICE;
    }

}
