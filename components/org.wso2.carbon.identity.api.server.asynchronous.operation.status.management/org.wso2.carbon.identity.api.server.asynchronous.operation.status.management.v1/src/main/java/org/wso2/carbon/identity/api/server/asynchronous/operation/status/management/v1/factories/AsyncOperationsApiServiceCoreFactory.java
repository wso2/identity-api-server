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

import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.common.AsyncOperationStatusMgtServiceHolder;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.core.AsyncOperationsApiServiceCore;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.service.AsyncOperationStatusMgtService;

/**
 * Factory class for AsyncOperationsApiService.
 */
public class AsyncOperationsApiServiceCoreFactory {

    private static final AsyncOperationsApiServiceCore SERVICE;

    static {
        AsyncOperationStatusMgtService asyncOperationStatusMgtService =
                AsyncOperationStatusMgtServiceHolder.getAsyncOperationStatusMgtService();
        if (asyncOperationStatusMgtService == null) {
            throw new IllegalStateException("AsyncOperationStatusMgtService is not available from the OSGi context.");
        }
        SERVICE = new AsyncOperationsApiServiceCore(asyncOperationStatusMgtService);
    }

    /**
     * Get AsyncOperationsApiServiceCore.
     *
     * @return AsyncOperationsApiServiceCore.
     */
    public static AsyncOperationsApiServiceCore getAsyncOperationsApiServiceCore() {

        return SERVICE;
    }
}
