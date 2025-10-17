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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.framework.async.operation.status.mgt.api.service.AsyncOperationStatusMgtService;

/**
 * Service holder for the async operation status management API component.
 */
public class AsyncOperationStatusMgtServiceHolder {

    private static final Log log = LogFactory.getLog(AsyncOperationStatusMgtServiceHolder.class);

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

        if (log.isDebugEnabled()) {
            log.debug("Retrieving AsyncOperationStatusMgtService from service holder.");
        }
        
        AsyncOperationStatusMgtService service = ServiceHolder.SERVICE;
        if (service == null) {
            log.warn("AsyncOperationStatusMgtService is not available.");
        }
        
        return service;
    }
}
