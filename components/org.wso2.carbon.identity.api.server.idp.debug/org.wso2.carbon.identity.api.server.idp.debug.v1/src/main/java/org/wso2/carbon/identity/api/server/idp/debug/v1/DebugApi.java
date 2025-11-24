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

package org.wso2.carbon.identity.api.server.idp.debug.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.idp.debug.v1.model.Error;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Debug API for OAuth 2.0 authentication flow debugging.
 */
@Path("/debug")
@Api(description = "The debug API")
public class DebugApi {

    private final DebugApiService delegate;

    public DebugApi() {
        this.delegate = new DebugApiService();
    }

    /**
     * Retrieves the debug result for a given session ID (state).
     *
     * @param sessionId The session ID (state) to fetch the debug result for.
     * @return JSON debug result or 404 if not found.
     */
    @GET
    @Path("/result/{session-id}")
    @Produces({"application/json"})
    @ApiOperation(value = "Get debug result by session ID",
            notes = "Fetches the debug result for the given session ID (state).",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Debug result found", response = String.class),
            @ApiResponse(code = 404, message = "Debug result not found", response = Error.class)
    })
    public Response getDebugResult(
            @ApiParam(value = "Session ID (state)", required = true)

            @PathParam("session-id") String sessionId) {
       
        // Try lookup with the session ID as-is first (works for contextId with hyphens)
        String resultJson = getDebugResultFromCache(sessionId);
        
        // If not found, try with normalized format (removes hyphens for state parameter)
        if (resultJson == null) {
            String normalizedSessionId = normalizeSessionId(sessionId);
            resultJson = getDebugResultFromCache(normalizedSessionId);
        }
        
        if (resultJson != null) {
            // Parse the JSON result and enrich with step status metadata if needed.
            try {
                // Use Jackson for JSON parsing (assume available in project).
                ObjectMapper mapper = new ObjectMapper();
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> resultMap = mapper.readValue(resultJson, java.util.Map.class);
                // Check for metadata and step status fields.
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> metadata = (java.util.Map<String, Object>) resultMap.get("metadata");
                if (metadata == null || metadata.isEmpty()) {
                    metadata = new java.util.HashMap<>();
                    resultMap.put("metadata", metadata);
                }
                // Always copy step status fields from top-level result to metadata if present.
                String[] stepKeys = {"step_connection_status", "step_authentication_status",
                        "step_claim_mapping_status"};
                for (String key : stepKeys) {
                    if (resultMap.containsKey(key)) {
                        metadata.put(key, resultMap.get(key));
                    }
                }
                // Return enriched result as JSON.
                String enrichedJson = mapper.writeValueAsString(resultMap);
                return Response.ok(enrichedJson).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to process debug result.")
                        .build();
            }
        } else {
            return createNotFoundResponse(sessionId);
        }
    }

    /**
     * Normalizes the session ID by removing hyphens from UUID format.
     * Handles both formats:
     * - debug-d045722f-2c10-4c18-97ca-4f38befef4e4 (with hyphens) 
     * - debug-d04572f2c104c1897ca4f38befef4e4 (without hyphens)
     * 
     * Always converts to: debug-<32-char-hex-UUID-no-hyphens>
     * 
     * @param sessionId The session ID to normalize.
     * @return Normalized session ID without hyphens in UUID part.
     */
    private String normalizeSessionId(String sessionId) {
        if (sessionId == null || !sessionId.startsWith("debug-")) {
            return sessionId;
        }
        
        // Extract everything after "debug-" prefix
        String uuidPart = sessionId.substring(6);
        
        // Remove ALL hyphens from the UUID part
        String normalizedUuid = uuidPart.replaceAll("-", "");
        
        // Return normalized format: debug-<32-char-uuid-without-hyphens>
        return "debug-" + normalizedUuid;
    }

    /**
     * Retrieves debug result from cache using reflection to avoid direct OSGi dependency.
     *
     * @param sessionId The session ID (state) to fetch the debug result for.
     * @return The debug result JSON or null if not found.
     */
    private String getDebugResultFromCache(String sessionId) {
        try {
            Class<?> cacheClass = Class.forName(
                    "org.wso2.carbon.identity.debug.framework.core.cache.DebugResultCache");
            java.lang.reflect.Method getMethod = cacheClass.getMethod("get", String.class);
            Object result = getMethod.invoke(null, sessionId);
            
            org.apache.commons.logging.LogFactory.getLog(DebugApi.class)
                    .info("Retrieving debug result from cache for session ID: " + sessionId + 
                          ", result: " + (result != null ? "found" : "not found"));
            
            return (String) result;
        } catch (Exception e) {
            org.apache.commons.logging.LogFactory.getLog(DebugApi.class)
                    .debug("Error retrieving debug result from cache using reflection: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Creates an error response for missing debug results.
     *
     * @param sessionId The session ID that was not found.
     * @return Error response with appropriate HTTP status.
     */
    private Response createNotFoundResponse(String sessionId) {
        Error errorResponse = new Error();
        errorResponse.setCode("DEBUG_RESULT_NOT_FOUND");
        errorResponse.setMessage("Debug result not found");
        errorResponse.setDescription("No debug result found for session ID: " + sessionId + 
                ". The debug session may have expired or the session ID is invalid.");
        
        return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
    }

    @Valid
    @POST
    @Path("")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Start debug session",
            notes = "Initiates a debug session for any resource type and properties.",
            response = DebugConnectionResponse.class,
            authorizations = {
                    @Authorization(value = "BasicAuth"),
                    @Authorization(value = "OAuth2", scopes = {})
            },
            tags = {"Debug"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response",
                    response = DebugConnectionResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response startDebugSession(
            @ApiParam(value = " Debug request with resourceId, resourceType, and properties", required = true)
            @Valid DebugConnectionRequest debugRequest) {
        return delegate.startDebugSession(debugRequest);
    }
}
