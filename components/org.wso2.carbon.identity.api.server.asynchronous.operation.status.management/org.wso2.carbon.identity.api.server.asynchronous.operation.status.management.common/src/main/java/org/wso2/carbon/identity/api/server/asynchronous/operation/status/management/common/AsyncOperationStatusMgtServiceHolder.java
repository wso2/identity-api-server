package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.framework.async.status.mgt.api.service.AsyncStatusMgtService;

/**
 * Holds the services which the async operation status management API component is using.
 */
public class AsyncOperationStatusMgtServiceHolder {

    private AsyncOperationStatusMgtServiceHolder() {

    }

    private static class AsyncStatusMgtServiceHolder {

        private static final AsyncStatusMgtService SERVICE =
                (AsyncStatusMgtService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(AsyncStatusMgtService.class, null);
    }

    /**
     * Get AsyncStatusMgtService service.
     *
     * @return AsyncStatusMgtService.
     */
    public static AsyncStatusMgtService getAsyncStatusMgtService() {

        return AsyncOperationStatusMgtServiceHolder.AsyncStatusMgtServiceHolder.SERVICE;
    }
}
