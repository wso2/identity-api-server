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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.v1.constants.DebugConstants;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResult;
import org.wso2.carbon.identity.api.server.debug.v1.utils.Utils;
import org.wso2.carbon.identity.debug.framework.DebugFrameworkConstants;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkServerException;
import org.wso2.carbon.identity.debug.framework.model.DebugRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Core service for debug API request handling.
 */
public class DebugService {

    private static final Log LOG = LogFactory.getLog(DebugService.class);
    private static final Map<String, List<String>> REQUIRED_PROPERTIES_BY_RESOURCE_TYPE =
            createRequiredPropertiesByResourceType();
    private static final DebugStatusBuilder STATUS_BUILDER = new DebugStatusBuilder(LOG);
    private final DebugRequestCoordinator coordinator;

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
    public DebugResponse processStartSession(
            String resourceType, Map<String, String> requestBody) {

        try {
            validateRequest(resourceType, requestBody);
            Map<String, Object> responseData = handleDebugRequest(resourceType, requestBody);
            return buildDebugResponse(resourceType, responseData);
        } catch (DebugFrameworkException e) {
            throw Utils.handleDebugException(e);
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
            if (StringUtils.isBlank(debugId)) {
                throw new DebugFrameworkClientException(
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                        "Debug ID cannot be null or empty.");
            }
            Map<String, Object> frameworkResponse = coordinator.getDebugResult(debugId);
            if (frameworkResponse == null) {
                throw new DebugFrameworkClientException(
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_RESULT_NOT_FOUND.getCode(),
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_RESULT_NOT_FOUND.getMessage(),
                        "Debug result not found for session id: " + debugId + ".");
            }
            return buildDebugResult(frameworkResponse, debugId);
        } catch (DebugFrameworkClientException e) {
            if (DebugFrameworkConstants.ErrorMessages.ERROR_CODE_RESULT_NOT_FOUND.getCode()
                    .equals(e.getErrorCode())) {
                throw Utils.handleException(
                        Response.Status.NOT_FOUND,
                        DebugConstants.ErrorMessage.ERROR_CODE_RESULT_NOT_FOUND.getCode(),
                        DebugConstants.ErrorMessage.ERROR_CODE_RESULT_NOT_FOUND.getMessage(),
                        DebugConstants.ErrorMessage.ERROR_CODE_RESULT_NOT_FOUND.getDescription());
            }
            throw Utils.handleDebugException(e);
        } catch (DebugFrameworkException e) {
            throw Utils.handleDebugException(e);
        }
    }

    /**
     * Sends a debug request to the coordinator and returns a safe copy of the response data.
     *
     * @param resourceType Resource type from path parameter.
     * @param properties Request properties map.
     * @return Response data map (safe copy, never null).
     * @throws DebugFrameworkClientException if the framework encounters a client error.
     * @throws DebugFrameworkServerException if the framework encounters a server error.
     */
    private Map<String, Object> handleDebugRequest(String resourceType,
                                                    Map<String, String> properties)
            throws DebugFrameworkClientException, DebugFrameworkServerException {

        DebugRequest debugRequest = new DebugRequest();
        debugRequest.setResourceType(resourceType);

        Map<String, String> requestProperties = properties != null ? properties : Collections.emptyMap();
        for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
            debugRequest.addContextProperty(entry.getKey(), entry.getValue());
        }

        org.wso2.carbon.identity.debug.framework.model.DebugResponse frameworkResponse =
                coordinator.handleDebugRequest(debugRequest);

        // Copy framework response into a new map to avoid mutating framework-owned data.
        Map<String, Object> responseData = new HashMap<>();
        if (frameworkResponse.getData() != null) {
            responseData.putAll(frameworkResponse.getData());
        }
        responseData.putIfAbsent(DebugConstants.ResponseKeys.STATUS,
                STATUS_BUILDER.buildDebugStatus(
                        responseData.get(DebugConstants.ResponseKeys.STATUS),
                        frameworkResponse.isSuccess()).name());

        return responseData;
    }

    /**
     * Builds a DebugResponse DTO from the framework response data map.
     *
     * @param responseData Framework response data.
     * @return DebugResponse DTO.
     * @throws DebugFrameworkServerException if required fields are missing.
     */
    private DebugResponse buildDebugResponse(String resourceType, Map<String, Object> responseData)
            throws DebugFrameworkServerException {

        DebugResponse response =
                new DebugResponse();

        Object debugId = resolveDebugId(responseData, null);
        if (debugId == null) {
            throw new DebugFrameworkServerException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getMessage(),
                    "Debug framework response does not contain debugId");
        }
        response.setDebugId(debugId.toString());

        DebugResponse.StatusEnum status =
                STATUS_BUILDER.buildDebugStatus(
                        responseData.get(DebugConstants.ResponseKeys.STATUS), null);
        response.setStatus(status);
        response.setMessage(resolveDebugMessage(
                status, responseData.get(DebugConstants.ResponseKeys.MESSAGE)));

        Map<String, Object> metadata = buildStartResponseMetadata(resourceType, responseData);

        if (!metadata.isEmpty()) {
            response.setMetadata(metadata);
        }

        return response;
    }

    /**
     * Builds a DebugResult DTO from the framework response data map.
     *
     * @param frameworkResponse Framework response data.
     * @param requestedDebugId  The originally requested debug ID (used as fallback).
     * @return DebugResult DTO.
     */
    private DebugResult buildDebugResult(Map<String, Object> frameworkResponse,
            String requestedDebugId) {

        DebugResult response = new DebugResult();

        Object debugId = resolveDebugId(frameworkResponse, requestedDebugId);
        if (debugId != null) {
            response.setDebugId(debugId.toString());
        }

        DebugResult.StatusEnum status = STATUS_BUILDER.buildResultStatus(
                frameworkResponse.get(DebugConstants.ResponseKeys.STATUS),
                frameworkResponse.get(DebugConstants.ResponseKeys.SUCCESS));
        response.setStatus(status);
        response.setMessage(resolveResultMessage(
                status, frameworkResponse.get(DebugConstants.ResponseKeys.MESSAGE)));

        // Build metadata: preserve framework-specific keys except fields extracted to the top level.
        Map<String, Object> metadata = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : frameworkResponse.entrySet()) {
            String key = entry.getKey();
            if (!DebugConstants.ResponseKeys.STATUS.equals(key)
                    && !DebugConstants.ResponseKeys.SUCCESS.equals(key)
                    && !DebugConstants.ResponseKeys.DEBUG_ID.equals(key)
                    && !DebugConstants.ResponseKeys.MESSAGE.equals(key)) {
                metadata.put(key, entry.getValue());
            }
        }
        response.setMetadata(metadata);

        return response;
    }

    /**
     * Builds API metadata for the start-session response.
     * The API layer exposes only contract-approved fields instead of forwarding
     * all framework data directly.
     *
     * @param resourceType Resource type of the debug request.
     * @param responseData Framework response data.
     * @return Curated metadata map for the API response.
     */
    private Map<String, Object> buildStartResponseMetadata(String resourceType, Map<String, Object> responseData) {

        Map<String, Object> metadata = new LinkedHashMap<>();
        if (DebugConstants.ResourceType.IDP.equals(resourceType)) {
            Object authorizationUrl = responseData.get(DebugConstants.ResponseKeys.AUTHORIZATION_URL);
            if (authorizationUrl != null) {
                metadata.put(DebugConstants.ResponseKeys.AUTHORIZATION_URL, authorizationUrl);
            }
            return metadata;
        }

        for (Map.Entry<String, Object> entry : responseData.entrySet()) {
            String key = entry.getKey();
            if (!DebugConstants.ResponseKeys.DEBUG_ID.equals(key)
                    && !DebugConstants.ResponseKeys.STATUS.equals(key)
                    && !DebugConstants.ResponseKeys.MESSAGE.equals(key)
                    && !DebugConstants.ResponseKeys.SUCCESS.equals(key)
                    && !"successful".equals(key)
                    && !"timestamp".equals(key)) {
                metadata.put(key, entry.getValue());
            }
        }
        return metadata;
    }

    /**
     * Resolves the response message for a response.
     *
     * @param status Resolved status enum.
     * @param frameworkMessage Raw message from framework.
     * @return Message string.
     */
    private String resolveDebugMessage(DebugResponse.StatusEnum status,
            Object frameworkMessage) {

        String normalizedMessage = frameworkMessage != null
                ? StringUtils.trimToNull(frameworkMessage.toString()) : null;
        if (normalizedMessage != null) {
            return normalizedMessage;
        }
        switch (status) {
            case FAILURE:
                return "Debug session execution failed";
            case SUCCESS_COMPLETE:
                return "Debug session executed successfully";
            case SUCCESS_INCOMPLETE:
                return "Debug session started successfully and is awaiting completion";
            default:
                return "Debug session executed successfully";
        }
    }

    /**
     * Resolves the response message for a debug result.
     *
     * @param status Resolved status enum.
     * @param frameworkMessage Raw message from framework.
     * @return Message string.
     */
    private String resolveResultMessage(DebugResult.StatusEnum status, Object frameworkMessage) {

        String normalizedMessage = frameworkMessage != null
                ? StringUtils.trimToNull(frameworkMessage.toString()) : null;
        if (normalizedMessage != null) {
            return normalizedMessage;
        }
        switch (status) {
            case IN_PROGRESS:
            case SUCCESS_INCOMPLETE:
                return "Debug session is in progress";
            case SUCCESS_COMPLETE:
            case SUCCESS:
                return "Debug session retrieved successfully.";
            case FAILURE:
            default:
                return "Debug session retrieval failed.";
        }
    }

    private Object resolveDebugId(Map<String, Object> responseData, String fallbackDebugId) {

        Object debugId = responseData.get(DebugConstants.ResponseKeys.DEBUG_ID);
        if (debugId == null) {
            debugId = fallbackDebugId;
        }
        return debugId;
    }

    // Temporary until we support more resource types and validation rules.
    private static Map<String, List<String>> createRequiredPropertiesByResourceType() {

        Map<String, List<String>> requiredPropertiesByResourceType = new HashMap<>();
        requiredPropertiesByResourceType.put(DebugConstants.ResourceType.IDP,
                Arrays.asList(DebugConstants.CONNECTION_ID));
        return Collections.unmodifiableMap(requiredPropertiesByResourceType);
    }

    /**
     * Validates the debug start session request.
     *
     * @param resourceType Resource type.
     * @param requestBody Request body.
     * @throws DebugFrameworkClientException if validation fails.
     */
    private void validateRequest(String resourceType,
                                 Map<String, String> requestBody)
            throws DebugFrameworkClientException {

        List<String> requiredProperties = REQUIRED_PROPERTIES_BY_RESOURCE_TYPE.get(resourceType);
        if (requiredProperties == null || requiredProperties.isEmpty()) {
            return;
        }

        if (requestBody == null) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                    "Debug request body cannot be null for resource type: " + resourceType + ".");
        }

        for (String requiredProperty : requiredProperties) {
            if (requestBody == null || !requestBody.containsKey(requiredProperty)) {
                throw new DebugFrameworkClientException(
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                        "Missing required property: " + requiredProperty + ".");
            }
        }
    }
}
