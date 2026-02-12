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

package org.wso2.carbon.identity.api.server.debug.v1;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.debug.v1.impl.DebugApiServiceImpl; // Import implementation
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionRequest;
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponse;
import org.wso2.carbon.identity.api.server.debug.v1.model.Error;

import java.util.UUID;
import java.util.regex.Pattern;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Debug API for OAuth 2.0 authentication flow debugging.
 * 
 * This API provides endpoints for:
 * 
 * Starting debug sessions for identity providers
 * Retrieving debug results by session ID
 */

@Path("/debug")
@Api(description = "Debug API for IdP authentication flow testing")
public class DebugApi {

    private static final Log LOG = LogFactory.getLog(DebugApi.class);

    // Session ID validation pattern: alphanumeric with hyphens, max 128 characters
    private static final Pattern SESSION_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-_]{1,128}$");

    private final DebugApiService delegate;

    /**
     * Constructor initializes the API delegate.
     */
    public DebugApi() {

        this.delegate = new DebugApiServiceImpl();
    }

    /**
     * Retrieves the debug result for a given session ID.
     *
     * @param sessionId The session ID (state) to fetch the debug result for.
     * @return JSON debug result or 404 if not found.
     */
    @GET
    @Path("/result/{session-id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get debug result by session ID", 
        notes = "Fetches the debug result for the given session ID (state).", 
        response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Debug result found", response = String.class),
            @ApiResponse(code = 404, message = "Debug result not found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    @SuppressFBWarnings("JAXRS_ENDPOINT")
    public Response getDebugResult(

            @ApiParam(value = "Session ID (state)", required = true) 
            @PathParam("session-id") String sessionId) {

        // Validate session ID format to prevent injection attacks
        if (!isValidSessionId(sessionId)) {
            return createBadRequestResponse("Invalid session ID format");
        }

        return delegate.getDebugResult(sessionId);
    }

    /**
     * Validates the session ID format.
     *
     * @param sessionId The session ID to validate.
     * @return true if valid, false otherwise.
     */
    private boolean isValidSessionId(String sessionId) {

        return sessionId != null && SESSION_ID_PATTERN.matcher(sessionId).matches();
    }

    /**
     * Creates a bad request error response.
     *
     * @param message The error message.
     * @return Error response with 400 status.
     */
    private Response createBadRequestResponse(String message) {

        Error errorResponse = createError("INVALID_REQUEST", message);
        errorResponse.setTraceId(UUID.randomUUID().toString());
        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }

    /**
     * Starts a debug session for any resource type.
     *
     * @param debugRequest Debug request with resourceId, resourceType, and
     *                     properties.
     * @return Response containing debug session information.
     */
    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Start debug session", 
        notes = "Initiates a debug session for any resource type and properties.", 
        response = DebugConnectionResponse.class, authorizations = {
    @Authorization(value = "BasicAuth"),
    @Authorization(value = "OAuth2", scopes = {})
    }, tags = { "Debug" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = DebugConnectionResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    @SuppressFBWarnings("JAXRS_ENDPOINT")
    public Response startDebugSession(

            @ApiParam(value = "Debug request with resourceId, resourceType, and properties", required = true) 
            @Valid DebugConnectionRequest debugRequest) {

        return delegate.startDebugSession(debugRequest);
    }

    /**
     * Creates an Error object with the given code and message.
     *
     * @param code    Error code.
     * @param message Error message.
     * @return Error object.
     */
    private Error createError(String code, String message) {

        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        return error;
    }
}
