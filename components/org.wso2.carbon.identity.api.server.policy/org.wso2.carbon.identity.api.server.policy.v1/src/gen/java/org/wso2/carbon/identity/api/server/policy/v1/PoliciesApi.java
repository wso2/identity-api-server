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

package org.wso2.carbon.identity.api.server.policy.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.policy.v1.model.Error;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyListResponse;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyRequest;
import org.wso2.carbon.identity.api.server.policy.v1.model.PolicyResponse;
import org.wso2.carbon.identity.api.server.policy.v1.PoliciesApiService;
import org.wso2.carbon.identity.api.server.policy.v1.factories.PoliciesApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/policies")
@Api(description = "The policies API")

public class PoliciesApi  {

    private final PoliciesApiService delegate;

    public PoliciesApi() {

        this.delegate = PoliciesApiServiceFactory.getPoliciesApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a policy.", notes = "This API provides the capability to create a new policy.", response = PolicyResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Policy Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfully Created", response = PolicyResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addPolicy(@ApiParam(value = "" ,required=true) @Valid PolicyRequest policyRequest) {

        return delegate.addPolicy(policyRequest );
    }

    @Valid
    @DELETE
    @Path("/{policy-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a policy.", notes = "This API provides the capability to delete a policy by ID.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Policy Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deletePolicy(@ApiParam(value = "ID of the policy",required=true) @PathParam("policy-id") String policyId) {

        return delegate.deletePolicy(policyId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List policies.", notes = "This API provides the capability to retrieve a paginated list of policies for the tenant, optionally filtered by name.", response = PolicyListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Policy Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = PolicyListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getPolicies(    @Valid @Min(1)@ApiParam(value = "Maximum number of records to return.", defaultValue="30") @DefaultValue("30")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination.", defaultValue="0") @DefaultValue("0")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Filter policies whose name contains the given value (case-insensitive). If omitted, all policies are returned.")  @QueryParam("filter") String filter) {

        return delegate.getPolicies(limit,  offset,  filter );
    }

    @Valid
    @GET
    @Path("/{policy-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a policy by ID.", notes = "This API provides the capability to retrieve a policy by ID.", response = PolicyResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Policy Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = PolicyResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getPolicyById(@ApiParam(value = "ID of the policy",required=true) @PathParam("policy-id") String policyId) {

        return delegate.getPolicyById(policyId );
    }

    @Valid
    @PUT
    @Path("/{policy-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a policy.", notes = "This API provides the capability to update an existing policy.", response = PolicyResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Policy Management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = PolicyResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updatePolicy(@ApiParam(value = "ID of the policy",required=true) @PathParam("policy-id") String policyId, @ApiParam(value = "" ,required=true) @Valid PolicyRequest policyRequest) {

        return delegate.updatePolicy(policyId,  policyRequest );
    }

}
