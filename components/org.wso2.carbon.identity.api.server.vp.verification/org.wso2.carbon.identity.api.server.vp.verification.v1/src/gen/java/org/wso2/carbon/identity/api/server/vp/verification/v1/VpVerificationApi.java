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
import org.wso2.carbon.identity.api.server.vp.verification.v1.factories.VpVerificationApiServiceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JAX-RS resource for standalone VP (Verifiable Presentation) verification.
 *
 * <p>Endpoints (public — no authentication required):
 * <ul>
 *   <li>POST /vp/verification/initiate — start a new verification transaction</li>
 *   <li>GET  /vp/verification/status/{txn_id} — poll for transaction result</li>
 * </ul>
 */
@Path("/vp/verification")
@Api(value = "/vp/verification", description = "Standalone VP Verification API")
public class VpVerificationApi {

    private final VpVerificationApiService delegate =
            VpVerificationApiServiceFactory.getVpVerificationApi();

    @POST
    @Path("/initiate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Initiate a VP verification transaction",
            notes = "Creates a new verification transaction and returns a wallet URL for QR code display.",
            response = VerificationInitiateResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = VerificationInitiateResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response initiateVerification(
            @ApiParam(value = "Verification initiation request", required = true)
                    VerificationInitiateRequest body) {

        return delegate.initiateVerification(body);
    }

    @GET
    @Path("/status/{txn_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Get VP verification status",
            notes = "Poll for the result of a verification transaction. "
                    + "Returns PENDING while waiting, COMPLETED with result_token on success, "
                    + "FAILED with error, or EXPIRED.",
            response = VerificationStatusResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = VerificationStatusResponse.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getVerificationStatus(
            @ApiParam(value = "Transaction ID returned from initiation.", required = true)
            @PathParam("txn_id") String txnId) {

        return delegate.getVerificationStatus(txnId);
    }
}
