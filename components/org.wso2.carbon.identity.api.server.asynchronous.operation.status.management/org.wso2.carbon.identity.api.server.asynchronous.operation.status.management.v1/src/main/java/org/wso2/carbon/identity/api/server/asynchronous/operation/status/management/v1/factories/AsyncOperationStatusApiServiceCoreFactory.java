package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.factories;

import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.common.AsyncOperationStatusMgtServiceHolder;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.core.AsyncOperationStatusApiServiceCore;
import org.wso2.carbon.identity.framework.async.status.mgt.api.service.AsyncStatusMgtService;

/**
 * Factory class for AsyncOperationStatusApiService.
 */
public class AsyncOperationStatusApiServiceCoreFactory {

    private static final AsyncOperationStatusApiServiceCore SERVICE;

    static {
        AsyncStatusMgtService asyncStatusMgtService =
                AsyncOperationStatusMgtServiceHolder.getAsyncStatusMgtService();
        if (asyncStatusMgtService == null) {
            throw new IllegalStateException("AsyncStatusMgtService is not available from the OSGi context.");
        }
        SERVICE = new AsyncOperationStatusApiServiceCore(asyncStatusMgtService);
    }

    /**
     * Get UsersApiServiceCore.
     *
     * @return UsersApiServiceCore.
     */
    public static AsyncOperationStatusApiServiceCore getAsyncOperationStatusApiServiceCore() {

        return SERVICE;
    }
}
