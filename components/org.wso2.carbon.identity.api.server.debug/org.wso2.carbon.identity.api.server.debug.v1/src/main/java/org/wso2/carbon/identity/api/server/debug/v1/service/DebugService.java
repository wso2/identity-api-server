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

package org.wso2.carbon.identity.api.server.debug.v1.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.common.DebugServiceHolder;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkServerException;
import org.wso2.carbon.identity.debug.framework.model.DebugRequest;
import org.wso2.carbon.identity.debug.framework.model.DebugResponse;

import java.util.Map;

/**
 * Service layer for debug operations.
 * 
 * This service is a thin wrapper that delegates ALL core logic to the debug framework.
 * Responsibilities:
 * - Input validation (done in API impl layer)
 * - Delegation to framework coordinator
 * - Response formatting
 *
 * The service uses the {@link DebugRequestCoordinator} for centralized
 * request routing and protocol-specific handling.
 */
public class DebugService {

    private static final Log LOG = LogFactory.getLog(DebugService.class);

    /**
     * Constructor initializes debug framework service via service holder pattern.
     */
    public DebugService() {

        // Debug framework services are loaded on-demand
    }

    /**
     * Handles generic debug request for any resource type with properties.
     * Delegates to framework coordinator for all processing.
     *
     * @param connectionId   Connection ID to debug (can be null for some resource types).
     * @param resourceType   Type of resource (IDP, APPLICATION, CONNECTOR, etc.).
     * @param properties     Generic properties map for the debug request (optional).
     * @return Debug result containing session information and status.
     * @throws RuntimeException if debug request fails.
     * @throws DebugFrameworkClientException if the framework encounters a client error.
     * @throws DebugFrameworkServerException if the framework encounters a server error.
     */
    public Map<String, Object> handleGenericDebugRequest(String connectionId, String resourceType,
            Map<String, String> properties) throws DebugFrameworkClientException, DebugFrameworkServerException {

        // Build typed request for framework.
        DebugRequest debugRequest = new DebugRequest();
        debugRequest.setResourceType(resourceType);
        if (connectionId != null) {
            debugRequest.setConnectionId(connectionId);
        }
        if (properties != null) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                debugRequest.addContextProperty(entry.getKey(), entry.getValue());
            }
        }

        // Delegate to framework coordinator.
        DebugRequestCoordinator coordinator = getCoordinatorOrThrow();
        DebugResponse response = coordinator.handleResourceDebugRequest(debugRequest);

        // Convert framework response to Map for API layer.
        Map<String, Object> resultMap = response.getData();
        resultMap.put("timestamp", System.currentTimeMillis());
        resultMap.putIfAbsent("status", "SUCCESS");

        return resultMap;
    }

    /**
     * Retrieves the debug result for the given session ID.
     * Delegates directly to framework coordinator.
     *
     * @param sessionId The session ID to look up.
     * @return The debug result JSON string, or null if not found.
     */
    public String getDebugResult(String sessionId) throws DebugFrameworkClientException {

        try {
            DebugRequestCoordinator coordinator = getCoordinatorOrThrow();
            return coordinator.getDebugResult(sessionId);
        } catch (DebugFrameworkClientException e) {
            throw e;

        } catch (RuntimeException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Debug framework not available for result retrieval.", e);
            }
            throw new IllegalStateException("Debug framework not available for result retrieval.", e);
        } catch (Exception e) {
            LOG.error("Error retrieving debug result.", e);
            throw new RuntimeException("Error retrieving debug result.", e);
        }
    }

    /**
     * Gets the DebugRequestCoordinator or throws if unavailable.
     *
     * @return The coordinator instance.
     * @throws RuntimeException if coordinator is not available.
     */
    private DebugRequestCoordinator getCoordinatorOrThrow() {

        DebugRequestCoordinator coordinator = DebugServiceHolder.getDebugRequestCoordinator();
        if (coordinator == null) {
            throw new IllegalStateException("DebugRequestCoordinator not available");
        }

        return coordinator;
    }
}
