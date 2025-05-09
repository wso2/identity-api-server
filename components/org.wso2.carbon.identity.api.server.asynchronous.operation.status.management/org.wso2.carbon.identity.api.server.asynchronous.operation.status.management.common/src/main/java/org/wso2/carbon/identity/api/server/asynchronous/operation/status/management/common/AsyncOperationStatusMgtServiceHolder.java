package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.service.AsyncOperationStatusMgtService;

/**
 * Holds the services which the async operation status management API component is using.
 */
public class AsyncOperationStatusMgtServiceHolder {

    private AsyncOperationStatusMgtServiceHolder() {

    }

    private static class ServiceHolder {

        private static final AsyncOperationStatusMgtService SERVICE =
                (AsyncOperationStatusMgtService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(AsyncOperationStatusMgtService.class, null);
    }

    /**
     * Get AsyncOperationStatusMgtService service.
     *
     * @return AsyncOperationStatusMgtService.
     */
    public static AsyncOperationStatusMgtService getAsyncOperationStatusMgtService() {

        return ServiceHolder.SERVICE;
    }
}
