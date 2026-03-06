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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.common.DebugServiceHolder;
import org.wso2.carbon.identity.api.server.debug.v1.constants.DebugConstants;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.debug.framework.DebugFrameworkConstants;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkServerException;
import org.wso2.carbon.identity.debug.framework.model.DebugRequest;
import org.wso2.carbon.identity.debug.framework.model.DebugResponse;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Core service for debug API request handling.
 */
public class DebugService {

    private static final Log LOG = LogFactory.getLog(DebugService.class);

    /**
     * Process a start debug session request.
     *
     * @param resourceType Path parameter resource type.
     * @param debugConnectionRequest Debug request payload.
     * @return Debug result map.
     * @throws DebugFrameworkClientException Client exceptions from framework.
     * @throws DebugFrameworkServerException Server exceptions from framework.
     */
    public Map<String, Object> processStartSession(String resourceType,
                                                   DebugConnectionRequest debugConnectionRequest)
            throws DebugFrameworkClientException, DebugFrameworkServerException {

        String normalizedResourceType = normalizeResourceType(resourceType);
        validateRequest(normalizedResourceType, debugConnectionRequest);
        Map<String, String> properties = new HashMap<>();
        if (debugConnectionRequest.getConnectionId() != null
                && !debugConnectionRequest.getConnectionId().trim().isEmpty()) {
            properties.put(DebugConstants.RequestKeys.CONNECTION_ID, debugConnectionRequest.getConnectionId());
        }

        return handleGenericDebugRequest(normalizedResourceType, properties);
    }

    /**
     * Process a debug result retrieval request.
     *
     * @param debugId Debug session id.
     * @return Debug result map.
     * @throws DebugFrameworkClientException if request is invalid.
     */
    public Map<String, Object> processGetResult(String debugId)
            throws DebugFrameworkClientException, DebugFrameworkServerException {

        return getDebugResult(debugId);
    }

    /**
     * Handles a generic debug request using request properties and path resourceType.
     *
     * @param resourceType Resource type from path parameter.
     * @param properties Generic properties map for the debug request.
     * @return Debug result containing session information and status.
     * @throws DebugFrameworkClientException if the framework encounters a client error.
     * @throws DebugFrameworkServerException if the framework encounters a server error.
     */
    private Map<String, Object> handleGenericDebugRequest(String resourceType,
                                                          Map<String, String> properties)
            throws DebugFrameworkClientException, DebugFrameworkServerException {

        DebugRequest debugRequest = new DebugRequest();
        debugRequest.setResourceType(resourceType);

        if (properties != null) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    debugRequest.addContextProperty(entry.getKey(), entry.getValue());
                }
            }
            debugRequest.setConnectionId(properties.get(DebugConstants.RequestKeys.CONNECTION_ID));
        }

        DebugRequestCoordinator coordinator = getCoordinatorOrThrow();
        DebugResponse response = executeWithServerErrorHandling(
                () -> coordinator.handleResourceDebugRequest(debugRequest),
                "Error processing start debug session request.",
                "Error occurred while processing debug request.");

        Map<String, Object> resultMap = response.getData() != null ?
                new HashMap<>(response.getData()) : new HashMap<>();
        resultMap.put(DebugConstants.ResponseKeys.TIMESTAMP, System.currentTimeMillis());
        resultMap.putIfAbsent(DebugConstants.ResponseKeys.STATUS, deriveStatus(resultMap));

        return resultMap;
    }

    /**
     * Derives status when the framework omits the explicit status field.
     *
     * @param responseMap Framework response map.
     * @return SUCCESS or FAILURE.
     */
    private String deriveStatus(Map<String, Object> responseMap) {

        Object success = responseMap.get(DebugConstants.ResponseKeys.SUCCESS);
        if (success instanceof Boolean) {
            return Boolean.TRUE.equals(success) ? DebugConstants.Status.SUCCESS : DebugConstants.Status.FAILURE;
        }
        return DebugConstants.Status.SUCCESS;
    }

    /**
     * Retrieves the debug result for the given session ID.
     *
     * @param debugId The debug session ID to look up.
     * @return The debug result map, or null if not found.
     * @throws DebugFrameworkClientException if request is invalid.
     */
    private Map<String, Object> getDebugResult(String debugId)
            throws DebugFrameworkClientException, DebugFrameworkServerException {

        DebugRequestCoordinator coordinator = getCoordinatorOrThrow();
        return executeWithServerErrorHandling(() -> coordinator.getDebugResult(debugId),
                "Error retrieving debug result.",
                "Error occurred while retrieving debug result.");
    }

    /**
     * Gets the DebugRequestCoordinator or throws if unavailable.
     *
     * @return The coordinator instance.
     */
    private DebugRequestCoordinator getCoordinatorOrThrow() throws DebugFrameworkServerException {

        DebugRequestCoordinator coordinator = DebugServiceHolder.getDebugRequestCoordinator();
        if (coordinator == null) {
            throw new DebugFrameworkServerException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getMessage(),
                    "Debug request coordinator not available.");
        }

        return coordinator;
    }

    private void validateRequest(String resourceType, DebugConnectionRequest debugConnectionRequest)
            throws DebugFrameworkClientException {

        if (debugConnectionRequest == null) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                    "Debug request body cannot be null.");
        }

        if (resourceType == null || resourceType.trim().isEmpty()) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_MISSING_RESOURCE_TYPE.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_MISSING_RESOURCE_TYPE.getMessage(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_MISSING_RESOURCE_TYPE.getDescription());
        }

        if (!DebugConstants.ResourceType.IDP.equals(resourceType)
                && !DebugConstants.ResourceType.FRAUD_DETECTION.equals(resourceType)) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                    "Invalid resource type. Supported values are IDP and FRAUD_DETECTION.");
        }
        if (DebugConstants.ResourceType.IDP.equals(resourceType)
                && (debugConnectionRequest.getConnectionId() == null
                || debugConnectionRequest.getConnectionId().trim().isEmpty())) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_MISSING_CONNECTION_ID.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_MISSING_CONNECTION_ID.getMessage(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_MISSING_CONNECTION_ID.getDescription());
        }
    }

    private String normalizeResourceType(String resourceType) {

        if (resourceType == null) {
            return null;
        }
        return resourceType.trim().toUpperCase(Locale.ROOT);
    }

    private <T> T executeWithServerErrorHandling(DebugOperation<T> operation, String logMessage,
                                                 String description)
            throws DebugFrameworkClientException, DebugFrameworkServerException {

        try {
            return operation.execute();
        } catch (RuntimeException e) {
            LOG.error(logMessage, e);
            throw new DebugFrameworkServerException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getMessage(),
                    description, e);
        }
    }

    @FunctionalInterface
    private interface DebugOperation<T> {

        T execute() throws DebugFrameworkClientException, DebugFrameworkServerException;
    }
}
