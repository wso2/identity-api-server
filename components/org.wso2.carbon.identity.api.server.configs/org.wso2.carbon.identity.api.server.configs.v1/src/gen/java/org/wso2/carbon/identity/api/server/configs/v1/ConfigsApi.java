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

package org.wso2.carbon.identity.api.server.configs.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.configs.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticatorListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.Error;
import java.util.List;
import org.wso2.carbon.identity.api.server.configs.v1.model.Patch;
import org.wso2.carbon.identity.api.server.configs.v1.model.ScimConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.ServerConfig;
import org.wso2.carbon.identity.api.server.configs.v1.ConfigsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/configs")
@Api(description = "The configs API")

public class ConfigsApi  {

    @Autowired
    private ConfigsApiService delegate;

    @Valid
    @GET
    @Path("/authenticators/{authenticator-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get authenticator by ID", notes = "By passing in the appropriate authenticator ID, you can retrieve authenticator details ", response = Authenticator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Local Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Authenticator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAuthenticator(@ApiParam(value = "ID of an authenticator",required=true) @PathParam("authenticator-id") String authenticatorId) {

        return delegate.getAuthenticator(authenticatorId );
    }

    @Valid
    @GET
    @Path("/cors")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the tenant CORS configuration.", notes = "Retrieve the tenant CORS configuration.", response = CORSConfig.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "CORS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = CORSConfig.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCORSConfiguration() {

        return delegate.getCORSConfiguration();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Server Configs", notes = "Retrieve Server Configs ", response = ServerConfig.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = ServerConfig.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getConfigs() {

        return delegate.getConfigs();
    }

    @Valid
    @GET
    @Path("/home-realm-identifiers")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the Home Realm Identifiers.", notes = "Retrieve the Home Realm Identifiers.", response = String.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Home Realm Identifiers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = String.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getHomeRealmIdentifiers() {

        return delegate.getHomeRealmIdentifiers();
    }

    @Valid
    @GET
    @Path("/provisioning/inbound/scim")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Server Inbound SCIM configs", notes = "Retrieve Server Inbound SCIM Configs ", response = ScimConfig.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Inbound SCIM", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = ScimConfig.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getInboundScimConfigs() {

        return delegate.getInboundScimConfigs();
    }

    @Valid
    @GET
    @Path("/authenticators")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List local authenticators in the server", notes = "List available local authenticators of the server ", response = AuthenticatorListItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Local Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = AuthenticatorListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response listAuthenticators(    @Valid@ApiParam(value = "Type of authenticators. Can be either 'LOCAL' or 'REQUEST_PATH' ")  @QueryParam("type") String type) {

        return delegate.listAuthenticators(type );
    }

    @Valid
    @PATCH
    @Path("/cors")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch the tenant CORS configuration.", notes = "A JSONPatch as defined by RFC 6902.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "CORS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchCORSConfiguration(@ApiParam(value = "" ,required=true) @Valid List<CORSPatch> coRSPatch) {

        return delegate.patchCORSConfiguration(coRSPatch );
    }

    @Valid
    @PATCH
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch Server Configs", notes = "Patch Server Configs. Patch operation is supported only for primary attributes (homeRealmIdentifier, idleSessionTimeoutPeriod and rememberMePeriod) ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchConfigs(@ApiParam(value = "" ,required=true) @Valid List<Patch> patch) {

        return delegate.patchConfigs(patch );
    }

    @Valid
    @PUT
    @Path("/provisioning/inbound/scim")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Server Inbound SCIM configs", notes = "Update Server Inbound SCIM configs ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Inbound SCIM" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateInboundScimConfigs(@ApiParam(value = "" ,required=true) @Valid ScimConfig scimConfig) {

        return delegate.updateInboundScimConfigs(scimConfig );
    }

}
