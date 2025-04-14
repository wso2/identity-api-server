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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.impl;

import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.AsyncOperationStatusApiService;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.core.AsyncOperationStatusApiServiceCore;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.factories.AsyncOperationStatusApiServiceCoreFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of AsyncOperationStatusApiService.
 */
public class AsyncOperationStatusApiServiceImpl implements AsyncOperationStatusApiService {

    private AsyncOperationStatusApiServiceCore asyncOperationStatusApiServiceCore;

    public AsyncOperationStatusApiServiceImpl() {
        this.asyncOperationStatusApiServiceCore = AsyncOperationStatusApiServiceCoreFactory
                .getAsyncOperationStatusApiServiceCore();
    }

    @Override
    public Response asyncOperationStatusGet(String after, String before, Integer limit, String filter) {

        return asyncOperationStatusApiServiceCore.getOperations(after, before, limit, filter);
    }

    @Override
    public Response asyncOperationStatusOperationIdGet(String operationId) {

        return asyncOperationStatusApiServiceCore.getOperation(operationId);
    }

    @Override
    public Response asyncOperationStatusOperationIdUnitOperationsGet(String operationId, String after, String before,
                                                                     Integer limit, String filter) {

        return asyncOperationStatusApiServiceCore.getUnitOperations(operationId, after, before, limit, filter);
    }

    @Override
    public Response asyncOperationStatusOperationIdUnitOperationsUnitOperationIdGet(String operationId,
                                                                                    String unitOperationId) {

        return asyncOperationStatusApiServiceCore.getUnitOperation(unitOperationId);
    }
}
