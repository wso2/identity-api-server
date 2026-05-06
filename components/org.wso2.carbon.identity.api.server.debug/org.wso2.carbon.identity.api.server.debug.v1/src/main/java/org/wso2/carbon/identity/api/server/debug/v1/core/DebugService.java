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
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponseMetadata;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResult;
import org.wso2.carbon.identity.api.server.debug.v1.utils.Utils;
import org.wso2.carbon.identity.debug.framework.DebugFrameworkConstants;
import org.wso2.carbon.identity.debug.framework.core.DebugRequestCoordinator;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkServerException;
import org.wso2.carbon.identity.debug.framework.model.DebugRequest;
import org.wso2.carbon.identity.debug.framework.model.DebugResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Core service for debug API request handling.
 */
public class DebugService {

    private static final Log LOG = LogFactory.getLog(DebugService.class);
    private static final Map<String, List<String>> REQUIRED_PROPERTIES_BY_RESOURCE_TYPE =
            createRequiredPropertiesByResourceType();
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
     * @return DebugConnectionResponse DTO.
     */
    public DebugConnectionResponse processStartSession(String resourceType,
                                                       Map<String, String> requestBody) {

        try {
            String normalizedResourceType = normalizeInput(resourceType);
            Map<String, String> properties = getNormalizedProperties(requestBody);
            validateRequest(normalizedResourceType, requestBody, properties);
            Map<String, Object> responseData = handleDebugRequest(normalizedResourceType, properties);
            return buildDebugConnectionResponse(responseData);
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
            String normalizedDebugId = normalizeInput(debugId);
            if (normalizedDebugId == null) {
                throw new DebugFrameworkClientException(
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                        "Debug ID cannot be null or empty.");
            }
            Map<String, Object> frameworkResponse = coordinator.getDebugResult(normalizedDebugId);
            if (frameworkResponse == null) {
                throw new DebugFrameworkClientException(
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_RESULT_NOT_FOUND.getCode(),
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_RESULT_NOT_FOUND.getMessage(),
                        "Debug result not found for session id: " + normalizedDebugId + ".");
            }
            return buildDebugResult(frameworkResponse, normalizedDebugId);
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
     * @param properties Normalized properties map.
     * @return Response data map (safe copy, never null).
     * @throws DebugFrameworkClientException if the framework encounters a client error.
     * @throws DebugFrameworkServerException if the framework encounters a server error.
     */
    private Map<String, Object> handleDebugRequest(String resourceType,
                                                    Map<String, String> properties)
            throws DebugFrameworkClientException, DebugFrameworkServerException {

        DebugRequest debugRequest = new DebugRequest();
        debugRequest.setResourceType(resourceType);
        debugRequest.setConnectionId(properties.get(DebugConstants.CONNECTION_ID_KEY));

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            debugRequest.addContextProperty(entry.getKey(), entry.getValue());
        }

        DebugResponse response = coordinator.handleDebugRequest(debugRequest);
        if (response == null) {
            throw new DebugFrameworkServerException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getMessage(),
                    "Debug framework returned an empty response.");
        }

        // Copy framework response into a new map to avoid mutating framework-owned data.
        Map<String, Object> responseData = new HashMap<>();
        if (response.getData() != null) {
            responseData.putAll(response.getData());
        }
        responseData.putIfAbsent(DebugConstants.ResponseKeys.STATUS,
                deriveStatus(response.isSuccess()));

        return responseData;
    }

    /**
     * Builds a DebugConnectionResponse DTO from the framework response data map.
     *
     * @param responseData Framework response data.
     * @return DebugConnectionResponse DTO.
     * @throws DebugFrameworkServerException if required fields are missing.
     */
    private DebugConnectionResponse buildDebugConnectionResponse(Map<String, Object> responseData)
            throws DebugFrameworkServerException {

        DebugConnectionResponse response = new DebugConnectionResponse();

        Object debugId = resolveDebugId(responseData, null);
        if (debugId == null) {
            throw new DebugFrameworkServerException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_SERVER_ERROR.getMessage(),
                    "Debug framework response does not contain debugId");
        }
        response.setDebugId(debugId.toString());

        DebugConnectionResponse.StatusEnum status =
                resolveConnectionStatus(responseData.get(DebugConstants.ResponseKeys.STATUS));
        response.setStatus(status);
        response.setMessage(resolveConnectionMessage(
                status, responseData.get(DebugConstants.ResponseKeys.MESSAGE)));

        Object authorizationUrl = responseData.get(DebugConstants.ResponseKeys.AUTHORIZATION_URL);
        if (authorizationUrl != null) {
            DebugConnectionResponseMetadata metadata = new DebugConnectionResponseMetadata();
            metadata.setAuthorizationUrl(authorizationUrl.toString());
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

        DebugResult.StatusEnum status = resolveResultStatus(
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
                    && !DebugConstants.ResponseKeys.DEBUG_ID.equals(key)) {
                metadata.put(key, entry.getValue());
            }
        }
        response.setMetadata(metadata);

        return response;
    }

    /**
     * Resolves the StatusEnum from the raw status object, defaulting to FAILURE for unknown values.
     *
     * @param status Raw status object from framework response.
     * @return Resolved StatusEnum.
     */
    private DebugConnectionResponse.StatusEnum resolveConnectionStatus(Object status) {

        if (status == null) {
            return DebugConnectionResponse.StatusEnum.SUCCESS;
        }
        String statusValue = status.toString().toUpperCase(Locale.ROOT);
        for (DebugConnectionResponse.StatusEnum candidate : DebugConnectionResponse.StatusEnum.values()) {
            if (candidate.name().equals(statusValue)) {
                return candidate;
            }
        }
        LOG.warn("Unrecognized debug connection status from framework: " + status
                + ". Falling back to FAILURE.");
        return DebugConnectionResponse.StatusEnum.FAILURE;
    }

    /**
     * Resolves the result StatusEnum from both status string and boolean success flag.
     * FAILURE is the safer default for unrecognized statuses to avoid masking errors.
     *
     * @param status Raw status object from framework response.
     * @param success Raw success flag object from framework response.
     * @return Resolved StatusEnum.
     */
    private DebugResult.StatusEnum resolveResultStatus(Object status, Object success) {

        if (status != null) {
            String statusValue = status.toString().toUpperCase(Locale.ROOT);
            for (DebugResult.StatusEnum candidate : DebugResult.StatusEnum.values()) {
                if (candidate.name().equals(statusValue)) {
                    return candidate;
                }
            }
            LOG.warn("Unrecognized debug result status from framework: " + status + ". Falling back to FAILURE.");
        }

        if (success instanceof Boolean) {
            return (Boolean) success ? DebugResult.StatusEnum.SUCCESS : DebugResult.StatusEnum.FAILURE;
        }

        return DebugResult.StatusEnum.FAILURE;
    }

    /**
     * Resolves the response message for a connection response.
     *
     * @param status Resolved status enum.
     * @param frameworkMessage Raw message from framework.
     * @return Message string.
     */
    private String resolveConnectionMessage(DebugConnectionResponse.StatusEnum status,
                                            Object frameworkMessage) {

        String normalizedMessage = frameworkMessage != null ? StringUtils.trimToNull(frameworkMessage.toString()) : null;
        if (normalizedMessage != null) {
            return normalizedMessage;
        }
        switch (status) {
            case FAILURE:
                return "Debug session execution failed";
            case SUCCESS:
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

        String normalizedMessage = frameworkMessage != null ? StringUtils.trimToNull(frameworkMessage.toString()) : null;
        if (normalizedMessage != null) {
            return normalizedMessage;
        }
        switch (status) {
            case IN_PROGRESS:
                return "Debug session is in progress";
            case SUCCESS:
                return "Debug session retrieved successfully.";
            case FAILURE:
            default:
                return "Debug session retrieval failed.";
        }
    }

    /**
     * Derives a status string when the framework omits the explicit status field.
     *
     * @param success Framework response success flag.
     * @return SUCCESS or FAILURE string.
     */
    private String deriveStatus(boolean success) {

        return success ? DebugConnectionResponse.StatusEnum.SUCCESS.name()
                : DebugConnectionResponse.StatusEnum.FAILURE.name();
    }

    private Object resolveDebugId(Map<String, Object> responseData, String fallbackDebugId) {

        Object debugId = responseData.get(DebugConstants.ResponseKeys.DEBUG_ID);
        if (debugId == null) {
            debugId = fallbackDebugId;
        }
        return debugId;
    }

    private static Map<String, List<String>> createRequiredPropertiesByResourceType() {

        Map<String, List<String>> requiredPropertiesByResourceType = new HashMap<>();
        requiredPropertiesByResourceType.put(DebugConstants.ResourceType.IDP,
                Arrays.asList(DebugConstants.CONNECTION_ID_KEY));
        return Collections.unmodifiableMap(requiredPropertiesByResourceType);
    }

    /**
     * Validates the debug start session request.
     *
     * @param resourceType Normalized resource type.
     * @param requestBody Request body.
     * @param properties Normalized properties map.
     * @throws DebugFrameworkClientException if validation fails.
     */
    private void validateRequest(String resourceType,
                                 Map<String, String> requestBody,
                                 Map<String, String> properties)
            throws DebugFrameworkClientException {

        if (StringUtils.isBlank(resourceType)) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_MISSING_RESOURCE_TYPE.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_MISSING_RESOURCE_TYPE.getMessage(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_MISSING_RESOURCE_TYPE.getDescription());
        }

        List<String> requiredProperties = REQUIRED_PROPERTIES_BY_RESOURCE_TYPE.get(resourceType);
        if (requiredProperties == null) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                    "Invalid resource type. Supported values are: "
                            + String.join(", ", REQUIRED_PROPERTIES_BY_RESOURCE_TYPE.keySet()) + ".");
        }

        if (requestBody == null && !requiredProperties.isEmpty()) {
            throw new DebugFrameworkClientException(
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                    DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                    "Debug request body cannot be null for resource type: " + resourceType + ".");
        }

        for (String requiredProperty : requiredProperties) {
            if (!properties.containsKey(requiredProperty)) {
                throw new DebugFrameworkClientException(
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getCode(),
                        DebugFrameworkConstants.ErrorMessages.ERROR_CODE_INVALID_REQUEST.getMessage(),
                        "Missing required property: " + requiredProperty + ".");
            }
        }
    }

    /**
     * Normalizes and filters properties from the request, removing null/blank keys and values.
     *
     * @param requestBody Request body.
     * @return Normalized properties map, never null.
     */
    private Map<String, String> getNormalizedProperties(Map<String, String> requestBody) {

        if (requestBody == null) {
            return new HashMap<>();
        }

        Map<String, String> normalized = new HashMap<>();
        for (Map.Entry<String, String> entry : requestBody.entrySet()) {
            String key = normalizeInput(entry.getKey());
            String value = normalizeInput(entry.getValue());
            if (key != null && value != null) {
                normalized.put(key, value);
            }
        }
        return normalized;
    }

    private String normalizeInput(String value) {

        return StringUtils.trimToNull(value);
    }
}
