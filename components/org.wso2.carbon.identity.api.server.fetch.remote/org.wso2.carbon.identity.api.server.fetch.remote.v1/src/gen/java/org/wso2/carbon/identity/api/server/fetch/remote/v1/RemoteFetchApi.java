/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.fetch.remote.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;

import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.Error;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.PushEventWebHookPOSTRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationGetResponse;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationListResponse;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationPOSTRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationPatchRequest;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.StatusListResponse;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.RemoteFetchApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/remote-fetch")
@Api(description = "The remote-fetch API")

public class RemoteFetchApi  {

    @Autowired
    private RemoteFetchApiService delegate;

    @Valid
    @POST
    
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Add a new remotefetch configuration ", notes = "This API provides the capability to create a new remotefetch configuration. <br> <b>Permission required:</b> <br>     * None <br> <b>Scope required:</b> <br>     * internal_login ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Remote Fetch Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfully created.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addRemoteFetch(@ApiParam(value = "This represents the remotefetch configuration to be created." ,required=true) @Valid RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest) {

        return delegate.addRemoteFetch(remoteFetchConfigurationPOSTRequest );
    }

    @Valid
    @DELETE
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a remotefetch Configuration by using the remotefetch Configuration's ID. ", notes = "This API provides the capability to delete a remotefetch Configuration by giving its ID. <br> <b>Permission required:</b> <br>     * None <br> <b>Scope required:</b> <br>     * internal_login ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Remote Fetch Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteRemoteFetch(@ApiParam(value = "ID of the remotefetch Configuration",required=true) @PathParam("id") String id) {

        return delegate.deleteRemoteFetch(id );
    }

    @Valid
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Retrieve remotefetch Configuration by remotefetch Configuration's ID ", notes = "This API provides the capability to retrieve the Remote Fetch Configuration details by using its ID.<br> <b>Permission required:</b> <br>     * None <br> <b>Scope required:</b> <br>     * internal_login ", response = RemoteFetchConfigurationGetResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Remote Fetch Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = RemoteFetchConfigurationGetResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getRemoteFetch(@ApiParam(value = "ID of the remotefetch Configuration.",required=true) @PathParam("id") String id) {

        return delegate.getRemoteFetch(id );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List remote fetch configurations ", notes = "This API provides the capability to retrieve the list of remotefetch configurations.<br> <b>Permission required:</b> <br> * None <br> <b>Scope required:</b> <br> * internal_login", response = RemoteFetchConfigurationListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Remote Fetch Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = RemoteFetchConfigurationListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getRemoteFetchConfigs() {

        return delegate.getRemoteFetchConfigs();
    }

    @Valid
    @GET
    @Path("/{id}/status")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve latest Deployment status of remotefetch configuration. ", notes = "This API provides the status of remote fetch configuration. The status hold deployment status and the deployment failure details . <br> <b>Permission required:</b> <br>     * None <br> <b>Scope required:</b> <br>     * internal_login ", response = StatusListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Remote fetch Configuration Status", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = StatusListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getStatus(@ApiParam(value = "ID of the remote fetch configuration.",required=true) @PathParam("id") String id) {

        return delegate.getStatus(id );
    }

    @Valid
    @POST
    @Path("/webhook/sp")
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Web hook endpoint for remote fetch configuration. ", notes = "This API provides the capability to handle web hook request from remote repository. <br> ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Remote Fetch Webhook", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Successful Response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response handleWebHook(@ApiParam(value = "" ) @Valid PushEventWebHookPOSTRequest pushEventWebHookPOSTRequest) {

        return delegate.handleWebHook(pushEventWebHookPOSTRequest );
    }

    @Valid
    @POST
    @Path("/{id}/trigger")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Trigger a remote fetch configuration. ", notes = "This API pprovides the capability to trigger given remotefetch configuration. <br> <b>Permission required:</b> <br>     * None <br> <b>Scope required:</b> <br>     * internal_login ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Remote fetch Configuration Trigger", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Accepted.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response triggerRemoteFetch(@ApiParam(value = "ID of the remote fetch configuration.",required=true) @PathParam("id") String id) {

        return delegate.triggerRemoteFetch(id );
    }

    @Valid
    @PATCH
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch a remotefetch Configuration property by ID. ", notes = "This API provides the capability to update a remotefetch Configuration property using patch request. Patch is supported only for key-value pairs. <br> <b>Permission required:</b> <br>     * None <br> <b>Scope required:</b> <br>     * internal_login ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Remote Fetch Configurations" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateRemoteFetch(@ApiParam(value = "ID of the remotefetch Configuration.",required=true) @PathParam("id") String id, @ApiParam(value = "" ,required=true) @Valid RemoteFetchConfigurationPatchRequest remoteFetchConfigurationPatchRequest) {

        return delegate.updateRemoteFetch(id,  remoteFetchConfigurationPatchRequest );
    }

}
