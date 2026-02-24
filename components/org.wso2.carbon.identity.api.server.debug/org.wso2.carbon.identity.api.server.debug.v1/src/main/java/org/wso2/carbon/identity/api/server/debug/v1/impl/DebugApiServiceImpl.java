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

package org.wso2.carbon.identity.api.server.debug.v1.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.wso2.carbon.identity.api.server.debug.v1.DebugApiService;
import org.wso2.carbon.identity.api.server.debug.v1.constants.DebugConstants;
import org.wso2.carbon.identity.api.server.debug.v1.core.DebugServiceCore;
import org.wso2.carbon.identity.api.server.debug.v1.factories.DebugServiceCoreFactory;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponseMetadata;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.Error;
import org.wso2.carbon.identity.debug.framework.DebugFrameworkConstants;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkServerException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Implementation of the DebugApiService interface.
 */
public class DebugApiServiceImpl implements DebugApiService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE =
            new TypeReference<Map<String, Object>>() {
            };
    private static final DebugConstants.ErrorMessage REQUEST_VALIDATION_ERROR =
            DebugConstants.ErrorMessage.ERROR_CODE_ERROR_VALIDATING_REQUEST;
    private static final DebugConstants.ErrorMessage PROCESSING_ERROR =
            DebugConstants.ErrorMessage.ERROR_CODE_ERROR_PROCESSING_REQUEST;
    private static final DebugConstants.ErrorMessage RESULT_NOT_FOUND_ERROR =
            DebugConstants.ErrorMessage.ERROR_CODE_RESULT_NOT_FOUND;

    private final DebugServiceCore debugServiceCore;

    public DebugApiServiceImpl() {

        this.debugServiceCore = DebugServiceCoreFactory.getDebugServiceCore();
    }

    @Override
    public Response startDebugSession(DebugConnectionRequest debugConnectionRequest) {

        try {
            Map<String, Object> debugResult = debugServiceCore.processStartSession(debugConnectionRequest);
            return Response.ok().entity(buildDebugConnectionResponse(debugResult)).build();
        } catch (DebugFrameworkClientException | IllegalArgumentException e) {
            return buildErrorResponse(Response.Status.BAD_REQUEST, REQUEST_VALIDATION_ERROR.getCode(), e.getMessage(),
                    "Invalid debug request.");
        } catch (DebugFrameworkServerException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, PROCESSING_ERROR.getCode(), e.getMessage(),
                    "Error occurred while processing debug request.");
        } catch (RuntimeException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, PROCESSING_ERROR.getCode(),
                    "Error occurred while processing debug request.", e.getMessage());
        }
    }

    @Override
    public Response getDebugResult(String sessionId) {

        try {
            String result = debugServiceCore.processGetResult(sessionId);
            if (result == null) {
                return buildErrorResponse(Response.Status.NOT_FOUND, RESULT_NOT_FOUND_ERROR.getCode(),
                        RESULT_NOT_FOUND_ERROR.getMessage(), RESULT_NOT_FOUND_ERROR.getDescription());
            }
            Map<String, Object> frameworkResponse =
                    OBJECT_MAPPER.readValue(result, MAP_TYPE_REFERENCE);
            DebugResponse apiResponse = buildDebugResponse(frameworkResponse, sessionId);
            return Response.ok().entity(apiResponse).build();
        } catch (JsonProcessingException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, PROCESSING_ERROR.getCode(),
                    "Failed to process debug result.", "Framework response is not valid JSON.");
        } catch (DebugFrameworkClientException e) {
            if (DebugFrameworkConstants.ErrorMessages.ERROR_CODE_RESULT_NOT_FOUND.getCode().equals(e.getErrorCode())) {
                return buildErrorResponse(Response.Status.NOT_FOUND, RESULT_NOT_FOUND_ERROR.getCode(),
                        RESULT_NOT_FOUND_ERROR.getMessage(), RESULT_NOT_FOUND_ERROR.getDescription());
            }
            return buildErrorResponse(Response.Status.BAD_REQUEST, REQUEST_VALIDATION_ERROR.getCode(), e.getMessage(),
                    "Invalid debug result request.");
        } catch (RuntimeException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, PROCESSING_ERROR.getCode(),
                    "Error retrieving debug result.", e.getMessage());
        }
    }

    private DebugConnectionResponse buildDebugConnectionResponse(Map<String, Object> debugResult) {

        DebugConnectionResponse response = new DebugConnectionResponse();

        Object sessionId = debugResult.get(DebugConstants.ResponseKeys.SESSION_ID);
        if (sessionId == null) {
            sessionId = debugResult.get(DebugConstants.ResponseKeys.STATE);
        }
        if (sessionId == null) {
            response.setDebugId("debug-" + System.currentTimeMillis());
        } else {
            response.setDebugId(sessionId.toString());
        }
        DebugConnectionResponse.StatusEnum status 
            = resolveConnectionStatus(debugResult.get(DebugConstants.ResponseKeys.STATUS));
        response.setStatus(status);
        response.setMessage(resolveConnectionMessage(status, debugResult.get(DebugConstants.ResponseKeys.MESSAGE)));
        Object timestamp = debugResult.get(DebugConstants.ResponseKeys.TIMESTAMP);
        if (timestamp instanceof Number) {
            response.setTimestamp(((Number) timestamp).longValue());
        } else {
            response.setTimestamp(System.currentTimeMillis());
        }
        Object authorizationUrl = debugResult.get(DebugConstants.ResponseKeys.AUTHORIZATION_URL);
        if (authorizationUrl != null) {
            DebugConnectionResponseMetadata metadata = new DebugConnectionResponseMetadata();
            metadata.setAuthorizationUrl(authorizationUrl.toString());
            response.setMetadata(metadata);
        }

        return response;
    }

    private DebugConnectionResponse.StatusEnum resolveConnectionStatus(Object status) {

        if (status == null) {
            return DebugConnectionResponse.StatusEnum.SUCCESS;
        }
        try {
            return DebugConnectionResponse.StatusEnum.valueOf(status.toString().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return DebugConnectionResponse.StatusEnum.SUCCESS;
        }
    }

    private String resolveConnectionMessage(DebugConnectionResponse.StatusEnum status, Object frameworkMessage) {

        if (frameworkMessage != null && !frameworkMessage.toString().trim().isEmpty()) {
            return frameworkMessage.toString();
        }
        switch (status) {
            case IN_PROGRESS:
                return "Debug session is in progress";
            case FAILURE:
                return "Debug session execution failed";
            case DIRECT_RESULT:
                return "Debug session completed with direct result";
            case SUCCESS:
            default:
                return "Debug session executed successfully";
        }
    }

    private Response buildErrorResponse(Response.Status status, String code, String message, String description) {

        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        error.setDescription(description);
        return Response.status(status).entity(error).build();
    }

    private DebugResponse buildDebugResponse(Map<String, Object> frameworkResponse,
                                             String requestedSessionId) {

        DebugResponse response = new DebugResponse();
        
        Object sessionId = frameworkResponse.get(DebugConstants.ResponseKeys.SESSION_ID);
        if (sessionId == null) {
            sessionId = frameworkResponse.get(DebugConstants.ResponseKeys.STATE);
        }
        if (sessionId == null) {
            sessionId = requestedSessionId;
        }
        response.setSessionId(sessionId != null ? sessionId.toString() : null);

        Object success = frameworkResponse.get(DebugConstants.ResponseKeys.SUCCESS);
        response.setSuccess(success instanceof Boolean ? (Boolean) success : false);

        Map<String, Object> metadata = new HashMap<>();
        for (Map.Entry<String, Object> entry : frameworkResponse.entrySet()) {
            String key = entry.getKey();
            if (!DebugConstants.ResponseKeys.SESSION_ID.equals(key) && !DebugConstants.ResponseKeys.SUCCESS.equals(key)
                && !DebugConstants.ResponseKeys.STATE.equals(key)) {
                metadata.put(key, entry.getValue());
            }
        }
        response.setMetadata(metadata);

        return response;
    }
}
