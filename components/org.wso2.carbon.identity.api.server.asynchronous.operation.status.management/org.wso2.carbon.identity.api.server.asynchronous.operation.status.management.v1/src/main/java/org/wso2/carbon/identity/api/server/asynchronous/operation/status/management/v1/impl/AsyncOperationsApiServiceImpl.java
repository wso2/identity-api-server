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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.AsyncOperationsApiService;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.core.AsyncOperationsApiServiceCore;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.factories.AsyncOperationsApiServiceCoreFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of the AsyncOperationsApiService.
 */
public class AsyncOperationsApiServiceImpl implements AsyncOperationsApiService {

    private static final Log LOG = LogFactory.getLog(AsyncOperationsApiServiceImpl.class);
    private AsyncOperationsApiServiceCore asyncOperationsApiServiceCore;

    public AsyncOperationsApiServiceImpl() {

        this.asyncOperationsApiServiceCore = AsyncOperationsApiServiceCoreFactory
                .getAsyncOperationsApiServiceCore();
        if (LOG.isDebugEnabled()) {
            LOG.debug("AsyncOperationsApiServiceImpl initialized successfully.");
        }
    }

    @Override
    public Response asyncOperationsGet(String after, String before, Integer limit, String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving async operations with parameters - after: %s, before: %s, " +
                    "limit: %s, filter: %s", after, before, limit, filter));
        }
        return asyncOperationsApiServiceCore.getOperations(after, before, limit, filter);
    }

    @Override
    public Response asyncOperationsOperationIdGet(String operationId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving async operation with ID: %s", operationId));
        }
        return asyncOperationsApiServiceCore.getOperation(operationId);
    }

    @Override
    public Response asyncOperationsOperationIdUnitOperationsGet(String operationId, String after, String before,
                                                                Integer limit, String filter) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving unit operations for operation ID: %s with parameters - " +
                    "after: %s, before: %s, limit: %s, filter: %s", operationId, after, before, limit, filter));
        }
        return asyncOperationsApiServiceCore.getUnitOperations(operationId, after, before, limit, filter);
    }

    @Override
    public Response asyncOperationsOperationIdUnitOperationsUnitOperationIdGet(String operationId,
                                                                               String unitOperationId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Retrieving unit operation with ID: %s for operation ID: %s",
                    unitOperationId, operationId));
        }
        return asyncOperationsApiServiceCore.getUnitOperation(unitOperationId);
    }
}
