package org.wso2.carbon.identity.api.server.api.resource.v1.factories;

import org.wso2.carbon.identity.api.resource.collection.mgt.APIResourceCollectionManager;
import org.wso2.carbon.identity.api.server.api.resource.common.APIResourceManagementServiceHolder;
import org.wso2.carbon.identity.api.server.api.resource.v1.core.ServerAPIResourceCollectionManagementService;

/**
 * Factory class for ServerAPIResourceCollectionManagementService.
 */
public class ServerAPIResourceCollectionManagementServiceFactory {

    private static final ServerAPIResourceCollectionManagementService SERVICE;

    static {
        APIResourceCollectionManager apiResourceCollectionManager = APIResourceManagementServiceHolder
                .getApiResourceCollectionManager();

        if (apiResourceCollectionManager == null) {
            throw new IllegalStateException("APIResourceCollectionManager is not available from OSGi context.");
        }

        SERVICE = new ServerAPIResourceCollectionManagementService(apiResourceCollectionManager);
    }

    /**
     * Get ServerAPIResourceCollectionManagementService instance.
     *
     * @return ServerAPIResourceCollectionManagementService.
     */
    public static ServerAPIResourceCollectionManagementService getServerAPIResourceCollectionManagementService() {
        return SERVICE;
    }
}
