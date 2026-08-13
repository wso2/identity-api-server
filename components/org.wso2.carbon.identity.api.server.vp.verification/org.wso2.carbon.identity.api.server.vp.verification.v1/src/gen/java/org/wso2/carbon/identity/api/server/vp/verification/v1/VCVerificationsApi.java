/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vp.verification.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.wso2.carbon.identity.api.server.vp.verification.v1.factories.VCVerificationsApiServiceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JAX-RS resource for standalone credential verification via OpenID for Verifiable Presentations.
 *
 * <p>Endpoints (public — no authentication required):
 * <ul>
 *   <li>POST /openid4vp/vc-verifications         — create a new verification request</li>
 *   <li>GET  /openid4vp/vc-verifications/{id}    — retrieve the request state</li>
 * </ul>
 */
@Path("/openid4vp/vc-verifications")
@Api(value = "/openid4vp/vc-verifications")
public class VCVerificationsApi {

    private final VCVerificationsApiService delegate =
            VCVerificationsApiServiceFactory.getVCVerificationsApi();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Create a credential verification request",
            notes = "Creates a new verification request and returns a wallet URL for QR code display.",
            response = VerificationInitiateResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = VerificationInitiateResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response initiateVerification(
            @ApiParam(value = "Verification request body", required = true)
                    VerificationInitiateRequest body) {

        return delegate.initiateVerification(body);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Get credential verification state",
            notes = "Returns the current state of a verification request. "
                    + "Returns ACTIVE while waiting, VERIFIED with the full presentation on success, "
                    + "or FAILED with errors. The resource is removed after a terminal state is returned.",
            response = VerificationStatusResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = VerificationStatusResponse.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getVerificationStatus(
            @ApiParam(value = "Verification request ID returned from creation.", required = true)
            @PathParam("id") String requestId) {

        return delegate.getVerificationStatus(requestId);
    }
}
