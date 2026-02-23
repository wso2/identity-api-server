/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.core;

import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.debug.v1.service.DebugService;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkServerException;

import java.util.Map;

/**
 * Core service for debug API request handling.
 */
public class DebugServiceCore {

    private static final String CONNECTION_ID = "connectionId";

    private final DebugService debugService;

    public DebugServiceCore(DebugService debugService) {

        this.debugService = debugService;
    }

    /**
     * Process a start debug session request.
     *
     * @param debugConnectionRequest Debug request payload.
     * @return Debug result map.
     * @throws DebugFrameworkClientException Client exceptions from framework.
     * @throws DebugFrameworkServerException Server exceptions from framework.
     */
    public Map<String, Object> processStartSession(DebugConnectionRequest debugConnectionRequest)
            throws DebugFrameworkClientException, DebugFrameworkServerException {

        Map<String, String> properties = debugConnectionRequest != null
                ? debugConnectionRequest.getProperties() : null;
        String resourceType = debugConnectionRequest != null
                ? debugConnectionRequest.getResourceType() : null;
        String connectionId = properties != null ? properties.get(CONNECTION_ID) : null;

        return debugService.handleGenericDebugRequest(connectionId, resourceType, properties);
    }

    /**
     * Process a debug result retrieval request.
     *
     * @param sessionId Debug session id.
     * @return Debug result json.
     */
    public String processGetResult(String sessionId) {

        return debugService.getDebugResult(sessionId);
    }
}
