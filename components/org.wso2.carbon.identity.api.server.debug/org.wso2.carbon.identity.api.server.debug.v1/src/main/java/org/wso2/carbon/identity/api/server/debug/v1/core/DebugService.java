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

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.debug.v1.constants.DebugConstants;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResult;
import org.wso2.carbon.identity.api.server.debug.v1.model.FrameworkResponseDTO;
import org.wso2.carbon.identity.api.server.debug.v1.utils.DebugExceptionHandler;
import org.wso2.carbon.identity.debug.framework.DebugFrameworkConstants;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkServerException;
import org.wso2.carbon.identity.debug.framework.model.DebugFrameworkRequest;
import org.wso2.carbon.identity.debug.framework.model.DebugFrameworkResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Core service for debug API request handling.
 */
public class DebugService {

    // Note: Add new resource types here so that validation is applied for each request based on the resource type.
    private static final Map<String, List<String>> REQUIRED_PROPERTIES_BY_RESOURCE_TYPE = Map.of(
            DebugConstants.ResourceType.IDP, List.of(DebugConstants.CONNECTION_ID));
    private final DebugRequestCoordinator coordinator;

    /**
     * Constructs a new {@link DebugService} with the specified coordinator.
     *
     * @param coordinator The {@link DebugRequestCoordinator} used to manage debug requests.
     */
    public DebugService(DebugRequestCoordinator coordinator) {

        this.coordinator = coordinator;
    }

    /**
     * Process a start debug session request and return a typed response DTO.
     * All framework exceptions are caught here and converted to APIError.
     *
     * @param resourceType Path parameter resource type.
     * @param requestBody Debug request payload.
     * @return DebugResponse DTO.
     */
    public DebugResponse processStartSession(String resourceType, Map<String, String> requestBody) {

        try {
            validateRequest(resourceType, requestBody);
            DebugFrameworkResponse frameworkResponse = startDebugSession(resourceType, requestBody);
            return buildDebugResponse(frameworkResponse);
        } catch (DebugFrameworkException e) {
            throw DebugExceptionHandler.handleDebugException(e);
        }
    }

    /**
     * Process a debug result retrieval request and return a typed response DTO.
     * All framework exceptions are caught here and converted to APIError.
     *
     * @param debugId Debug session id.
     * @return DebugResult DTO.
     */
    public DebugResult processGetResult(String debugId) {

        try {
            validateDebugId(debugId);
            DebugFrameworkResponse frameworkResponse = coordinator.getDebugResult(debugId);
            return buildDebugResult(frameworkResponse, debugId);
        } catch (DebugFrameworkException e) {
            throw DebugExceptionHandler.handleDebugException(e);
        }
    }

    /**
     * Builds and sends a debug request to the coordinator.
     *
     * @param resourceType Resource type from path parameter.
     * @param properties   Request properties map.
     * @return DebugFrameworkResponse from the coordinator.
     * @throws DebugFrameworkClientException if the framework encounters a client error.
     * @throws DebugFrameworkServerException if the framework encounters a server error.
     */
    private DebugFrameworkResponse startDebugSession(String resourceType, Map<String, String> properties)
            throws DebugFrameworkClientException, DebugFrameworkServerException {

        return coordinator.handleDebugRequest(buildDebugRequest(resourceType, properties));
    }

    private DebugFrameworkRequest buildDebugRequest(String resourceType, Map<String, String> properties) {

        DebugFrameworkRequest debugRequest = new DebugFrameworkRequest();
        debugRequest.setResourceType(resourceType);
        (properties != null ? properties : Collections.<String, String>emptyMap())
                .forEach(debugRequest::addContextProperty);
        return debugRequest;
    }

    /**
     * Builds a DebugResponse DTO from the typed framework response.
     * The framework is responsible for providing a structured response with
     * debugId, status and message as top-level fields, and metadata in getData().
     */
    private DebugResponse buildDebugResponse(DebugFrameworkResponse frameworkResponse)
            throws DebugFrameworkServerException {

        if (frameworkResponse.getDebugId() == null) {
            throw new DebugFrameworkServerException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getMessage(),
                    "Debug framework response does not contain debugId.");
        }

        FrameworkResponseDTO data = extractResponseData(frameworkResponse, null);
        DebugResponse response = new DebugResponse();
        response.setDebugId(data.getDebugId());

        DebugResponse.StatusEnum status = DebugStatusBuilder.resolveDebugStatus(data.getRawStatus());
        response.setStatus(status);
        response.setMessage(resolveDebugMessage(status, data.getRawMessage()));

        if (data.getMetadata() != null) {
            response.setMetadata(data.getMetadata());
        }
        return response;
    }

    /**
     * Builds a DebugResult DTO from the typed framework response.
     * The framework is responsible for providing a structured response with
     * debugId, status and message as top-level fields, and metadata in getData().
     *
     * @param frameworkResponse Framework response.
     * @param requestedDebugId  The originally requested debug ID (used as fallback if absent from response).
     * @return DebugResult DTO.
     */
    private DebugResult buildDebugResult(DebugFrameworkResponse frameworkResponse, String requestedDebugId) {

        FrameworkResponseDTO data = extractResponseData(frameworkResponse, requestedDebugId);
        if (data.getDebugId() == null) {
            throw DebugExceptionHandler.handleException(
                    Response.Status.NOT_FOUND,
                    DebugConstants.ErrorMessage.ERROR_CODE_RESULT_NOT_FOUND);
        }

        DebugResult response = new DebugResult();
        response.setDebugId(data.getDebugId());

        DebugResult.StatusEnum status = DebugStatusBuilder.resolveResultStatus(data.getRawStatus());
        response.setStatus(status);
        response.setMessage(resolveResultMessage(status, data.getRawMessage()));

        if (data.getMetadata() != null) {
            response.setMetadata(data.getMetadata());
        }
        return response;
    }

    /**
     * Extracts common fields from a framework response into a FrameworkResponseDTO,
     * using fallbackDebugId when the response does not carry its own debugId.
     *
     * @param frameworkResponse Framework response.
     * @param fallbackDebugId   Fallback debug ID (may be null).
     * @return FrameworkResponseDTO holding the extracted fields.
     */
    private FrameworkResponseDTO extractResponseData(DebugFrameworkResponse frameworkResponse,
            String fallbackDebugId) {

        String debugId = frameworkResponse.getDebugId() != null
                ? frameworkResponse.getDebugId() : fallbackDebugId;
        Map<String, Object> data = frameworkResponse.getData();
        Map<String, Object> metadata = (data != null && !data.isEmpty()) ? data : null;
        return new FrameworkResponseDTO(debugId, frameworkResponse.getStatus(),
                frameworkResponse.getMessage(), metadata);
    }

    /**
     * Resolves the response message for a debug session start.
     *
     * @param status          Resolved status enum.
     * @param frameworkMessage Raw message from framework.
     * @return Message string.
     */
    private String resolveDebugMessage(DebugResponse.StatusEnum status, String frameworkMessage) {

        String normalizedMessage = StringUtils.trimToNull(frameworkMessage);
        if (normalizedMessage != null) {
            return normalizedMessage;
        }
        switch (status) {
            case FAILURE:
                return "Failed to execute the debug session.";
            case SUCCESS_COMPLETE:
                return "The debug session has completed successfully.";
            case SUCCESS_INCOMPLETE:
                return "The debug session has partially completed.";
            default:
                return "Debug session executed successfully.";
        }
    }

    /**
     * Resolves the response message for a debug result retrieval.
     *
     * @param status          Resolved status enum.
     * @param frameworkMessage Raw message from framework.
     * @return Message string.
     */
    private String resolveResultMessage(DebugResult.StatusEnum status, String frameworkMessage) {

        String normalizedMessage = StringUtils.trimToNull(frameworkMessage);
        if (normalizedMessage != null) {
            return normalizedMessage;
        }
        switch (status) {
            case SUCCESS_INCOMPLETE:
                return "The debug session is still in progress.";
            case SUCCESS_COMPLETE:
                return "Debug session results have been retrieved successfully.";
            case FAILURE:
            default:
                return "Failed to retrieve debug results.";
        }
    }

    /**
     * Validates that the given debug ID is non-blank.
     *
     * @param debugId Debug session ID to validate.
     * @throws DebugFrameworkClientException if the debug ID is blank.
     */
    private void validateDebugId(String debugId) throws DebugFrameworkClientException {

        if (StringUtils.isBlank(debugId)) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                    "Debug ID cannot be null or empty.");
        }
    }

    /**
     * Validates the debug start session request.
     *
     * @param resourceType Resource type.
     * @param requestBody Request body.
     * @throws DebugFrameworkClientException if validation fails.
     */
    private void validateRequest(String resourceType, Map<String, String> requestBody)
            throws DebugFrameworkClientException {

        if (!REQUIRED_PROPERTIES_BY_RESOURCE_TYPE.containsKey(resourceType)) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                    String.format("Unsupported resource type: %s.", resourceType));
        }

        List<String> requiredProperties = REQUIRED_PROPERTIES_BY_RESOURCE_TYPE.get(resourceType);
        if (requiredProperties.isEmpty()) {
            return;
        }

        if (requestBody == null) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                    String.format("Debug request body cannot be null for resource type: %s.", resourceType));
        }

        for (String requiredProperty : requiredProperties) {
            if (!requestBody.containsKey(requiredProperty)) {
                throw new DebugFrameworkClientException(
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                        String.format("Missing required property: %s.", requiredProperty));
            }
        }
    }
}
