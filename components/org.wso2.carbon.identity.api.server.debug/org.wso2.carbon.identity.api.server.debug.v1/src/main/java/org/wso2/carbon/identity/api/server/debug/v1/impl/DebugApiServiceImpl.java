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
import org.wso2.carbon.identity.api.server.debug.v1.core.DebugServiceCore;
import org.wso2.carbon.identity.api.server.debug.v1.factories.DebugServiceCoreFactory;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponseMetadata;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugResponse;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkClientException;
import org.wso2.carbon.identity.debug.framework.exception.DebugFrameworkServerException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;

/**
 * Implementation of the DebugApiService interface.
 */
public class DebugApiServiceImpl implements DebugApiService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE =
            new TypeReference<Map<String, Object>>() {
            };
    private static final String SESSION_ID = "sessionId";
    private static final String STATE = "state";
    private static final String SUCCESS = "success";
    private static final String AUTHORIZATION_URL = "authorizationUrl";
    private static final String TIMESTAMP = "timestamp";

    private final DebugServiceCore debugServiceCore;

    public DebugApiServiceImpl() {

        this.debugServiceCore = DebugServiceCoreFactory.getDebugServiceCore();
    }

    @Override
    public Response startDebugSession(DebugConnectionRequest debugConnectionRequest) {

        try {
            Map<String, Object> debugResult = debugServiceCore.processStartSession(debugConnectionRequest);
            return Response.ok().entity(buildDebugConnectionResponse(debugResult)).build();
        } catch (DebugFrameworkClientException e) {
            return buildErrorResponse(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (DebugFrameworkServerException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Response getDebugResult(String sessionId) {

        String result = debugServiceCore.processGetResult(sessionId);
        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            Map<String, Object> frameworkResponse =
                    OBJECT_MAPPER.readValue(result, MAP_TYPE_REFERENCE);
            DebugResponse apiResponse = buildDebugResponse(frameworkResponse, sessionId);
            return Response.ok().entity(apiResponse).build();
        } catch (JsonProcessingException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    "Failed to process debug result.");
        }
    }

    private DebugConnectionResponse buildDebugConnectionResponse(Map<String, Object> debugResult) {

        DebugConnectionResponse response = new DebugConnectionResponse();

        Object sessionId = debugResult.get(SESSION_ID);
        if (sessionId == null) {
            sessionId = debugResult.get(STATE);
        }

        if (sessionId == null) {
            response.setDebugId("debug-" + UUID.randomUUID());
        } else {
            response.setDebugId(sessionId.toString());
        }

        response.setStatus(DebugConnectionResponse.StatusEnum.SUCCESS);
        response.setMessage("Debug session executed successfully");

        Object timestamp = debugResult.get(TIMESTAMP);
        if (timestamp instanceof Number) {
            response.setTimestamp(((Number) timestamp).longValue());
        } else {
            response.setTimestamp(System.currentTimeMillis());
        }

        Object authorizationUrl = debugResult.get(AUTHORIZATION_URL);
        if (authorizationUrl != null) {
            DebugConnectionResponseMetadata metadata = new DebugConnectionResponseMetadata();
            metadata.setAuthorizationUrl(authorizationUrl.toString());
            response.setMetadata(metadata);
        }

        return response;
    }

    private Response buildErrorResponse(Response.Status status, String message) {

        DebugConnectionResponse response = new DebugConnectionResponse();
        response.setDebugId("debug-" + UUID.randomUUID());
        response.setStatus(DebugConnectionResponse.StatusEnum.FAILURE);
        response.setMessage(message);
        response.setTimestamp(System.currentTimeMillis());
        return Response.status(status).entity(response).build();
    }

    private DebugResponse buildDebugResponse(Map<String, Object> frameworkResponse,
                                             String requestedSessionId) {

        DebugResponse response = new DebugResponse();

        Object sessionId = frameworkResponse.get(SESSION_ID);
        if (sessionId == null) {
            sessionId = frameworkResponse.get(STATE);
        }
        if (sessionId == null) {
            sessionId = requestedSessionId;
        }
        response.setSessionId(sessionId != null ? sessionId.toString() : null);

        Object success = frameworkResponse.get(SUCCESS);
        response.setSuccess(success instanceof Boolean ? (Boolean) success : false);

        Map<String, Object> metadata = new HashMap<>();
        for (Map.Entry<String, Object> entry : frameworkResponse.entrySet()) {
            String key = entry.getKey();
            if (!SESSION_ID.equals(key) && !SUCCESS.equals(key)) {
                metadata.put(key, entry.getValue());
            }
        }
        response.setMetadata(metadata);

        return response;
    }
}
