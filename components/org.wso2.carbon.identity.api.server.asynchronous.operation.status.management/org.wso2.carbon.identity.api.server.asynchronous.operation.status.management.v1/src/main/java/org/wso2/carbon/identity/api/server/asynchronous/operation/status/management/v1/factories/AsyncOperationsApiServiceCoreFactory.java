/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.common.AsyncOperationStatusMgtServiceHolder;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.core.AsyncOperationsApiServiceCore;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.service.AsyncOperationStatusMgtService;

/**
 * Factory class for AsyncOperationsApiService.
 */
public class AsyncOperationsApiServiceCoreFactory {

    private static final Log log = LogFactory.getLog(AsyncOperationsApiServiceCoreFactory.class);
    private static final AsyncOperationsApiServiceCore SERVICE;

    static {
        if (log.isDebugEnabled()) {
            log.debug("Initializing AsyncOperationsApiServiceCoreFactory.");
        }
        AsyncOperationStatusMgtService asyncOperationStatusMgtService =
                AsyncOperationStatusMgtServiceHolder.getAsyncOperationStatusMgtService();
        if (asyncOperationStatusMgtService == null) {
            log.error("AsyncOperationStatusMgtService is not available from the OSGi context.");
            throw new IllegalStateException("AsyncOperationStatusMgtService is not available from the OSGi context.");
        }
        SERVICE = new AsyncOperationsApiServiceCore(asyncOperationStatusMgtService);
        if (log.isDebugEnabled()) {
            log.debug("AsyncOperationsApiServiceCoreFactory initialized successfully.");
        }
    }

    /**
     * Get AsyncOperationsApiServiceCore.
     *
     * @return AsyncOperationsApiServiceCore.
     */
    public static AsyncOperationsApiServiceCore getAsyncOperationsApiServiceCore() {

        if (log.isDebugEnabled()) {
            log.debug("Returning AsyncOperationsApiServiceCore instance.");
        }
        return SERVICE;
    }
}
