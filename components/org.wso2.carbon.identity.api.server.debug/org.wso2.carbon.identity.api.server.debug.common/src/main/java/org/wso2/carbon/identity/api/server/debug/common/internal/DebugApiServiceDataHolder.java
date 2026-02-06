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

package org.wso2.carbon.identity.api.server.debug.common.internal;

/**
 * Data holder for debug API services.
 * This class maintains references to debug framework services for the API layer.
 */
public class DebugApiServiceDataHolder {

    private static final DebugApiServiceDataHolder instance = new DebugApiServiceDataHolder();
    
    private Object debugService;

    private DebugApiServiceDataHolder() {
        // Private constructor for singleton pattern
    }

    /**
     * Get the singleton instance of DebugApiServiceDataHolder.
     *
     * @return DebugApiServiceDataHolder instance.
     */
    public static DebugApiServiceDataHolder getInstance() {
        return instance;
    }

    /**
     * Get the debug service.
     *
     * @return Debug service instance.
     */
    public Object getDebugService() {
        return debugService;
    }

    /**
     * Set the debug service.
     *
     * @param debugService Debug service instance.
     */
    public void setDebugService(Object debugService) {
        this.debugService = debugService;
    }
}
