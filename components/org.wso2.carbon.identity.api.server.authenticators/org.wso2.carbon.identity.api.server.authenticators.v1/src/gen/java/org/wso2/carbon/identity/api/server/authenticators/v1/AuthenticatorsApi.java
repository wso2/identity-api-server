/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.authenticators.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.authenticators.v1.factories.AuthenticatorsApiServiceFactory;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.ConnectedApps;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.Error;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.SystemLocalAuthenticatorUpdate;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorCreation;
import org.wso2.carbon.identity.api.server.authenticators.v1.model.UserDefinedLocalAuthenticatorUpdate;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/authenticators")
@Api(description = "The authenticators API")

public class AuthenticatorsApi  {

    private final AuthenticatorsApiService delegate;

    public AuthenticatorsApi() {

        this.delegate = AuthenticatorsApiServiceFactory.getAuthenticatorsApi();
    }

    @Valid
    @POST
    @Path("/custom")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a new user defined local authenticator. ", notes = "This API provides the capability to create a new user defined local authenticator. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/custom_authenticator/create <br> <b>Scope required:</b> <br>     * internal_custom_authenticator_create <br> ", response = Authenticator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User defined local authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = Authenticator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addUserDefinedLocalAuthenticator(@ApiParam(value = "This represents the user defined local authenticator to be created." ,required=true) @Valid UserDefinedLocalAuthenticatorCreation userDefinedLocalAuthenticatorCreation) {

        return delegate.addUserDefinedLocalAuthenticator(userDefinedLocalAuthenticatorCreation );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List all authenticators in the server", notes = "List all authenticators in the server", response = Authenticator.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Authenticator.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response authenticatorsGet(    @Valid@ApiParam(value = "Condition to filter the retrieval of records. Only supports filtering based on the 'tag' and 'name' attribute. For local authenticators and request path authenticators, the 'displayName' is considered as the 'name' attribute during filtering. The 'name' attribute only supports 'eq' and 'sw operations. Filtering with multiple 'name' attributes is not supported. The 'tag' attribute only supports 'eq' operation. Filtering with multiple 'tag' attributes is supported with only 'or' as the complex query operation. E.g. /configs/authenticators?filter=name+sw+fi+and+(tag+eq+2FA+or+tag+eq+MFA) ")  @QueryParam("filter") String filter,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_ ")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_ ")  @QueryParam("offset") Integer offset) {

        return delegate.authenticatorsGet(filter,  limit,  offset );
    }

    @Valid
    @GET
    @Path("/meta/tags")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List all authenticator tags", notes = "List all authenticator tags", response = String.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = String.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response authenticatorsMetaTagsGet() {

        return delegate.authenticatorsMetaTagsGet();
    }

    @Valid
    @DELETE
    @Path("/custom/{authenticator-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a user defined local authenticator. ", notes = "This API provides the capability to delete a user defined local authenticators. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/custom_authenticator/delete <br> <b>Scope required:</b> <br>     * internal_custom_authenticator_delete <br> ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User defined local authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successful response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteUserDefinedLocalAuthenticator(@ApiParam(value = "ID of an authenticator",required=true) @PathParam("authenticator-id") String authenticatorId) {

        return delegate.deleteUserDefinedLocalAuthenticator(authenticatorId );
    }

    @Valid
    @GET
    @Path("/system")

    @Produces({ "application/json" })
    @ApiOperation(value = "Get all the system local autheticators ", notes = "Get all the system local autheticators ", response = Authenticator.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "System Local Authenticators", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful response", response = Authenticator.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getAllSystemLocalAuthenticators(    @Valid@ApiParam(value = "Condition to filter the retrieval of records. Only supports filtering based on the 'tag' and 'name' attribute. For local authenticators and request path authenticators, the 'displayName' is considered as the 'name' attribute during filtering. The 'name' attribute only supports 'eq' and 'sw operations. Filtering with multiple 'name' attributes is not supported. The 'tag' attribute only supports 'eq' operation. Filtering with multiple 'tag' attributes is supported with only 'or' as the complex query operation. E.g. /configs/authenticators?filter=name+sw+fi+and+(tag+eq+2FA+or+tag+eq+MFA) ")  @QueryParam("filter") String filter,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_ ")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_ ")  @QueryParam("offset") Integer offset) {

        return delegate.getAllSystemLocalAuthenticators(filter,  limit,  offset );
    }

    @Valid
    @GET
    @Path("/{authenticator-id}/connected-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get connected apps by authenticator ID", notes = "By passing in the appropriate authenticator ID, you can retrieve connected app details ", response = ConnectedApps.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Connected apps of local authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = ConnectedApps.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getConnectedAppsOfLocalAuthenticator(@ApiParam(value = "ID of an authenticator",required=true) @PathParam("authenticator-id") String authenticatorId,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_ ")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_ ")  @QueryParam("offset") Integer offset) {

        return delegate.getConnectedAppsOfLocalAuthenticator(authenticatorId,  limit,  offset );
    }

    @Valid
    @GET
    @Path("/system/{authenticator-id}")

    @Produces({ "application/json" })
    @ApiOperation(value = "Get the system local autheticator specified by the authenticator-id ", notes = "Get the system local autheticator specified by the authenticator-id ", response = Authenticator.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "System Local Authenticators", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful response", response = Authenticator.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getSystemLocalAuthenticatorById(@ApiParam(value = "ID of an authenticator",required=true) @PathParam("authenticator-id") String authenticatorId,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_ ")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_ ")  @QueryParam("offset") Integer offset) {

        return delegate.getSystemLocalAuthenticatorById(authenticatorId,  limit,  offset );
    }

    @Valid
    @PATCH
    @Path("/system/{authenticator-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the system local authenticators ", notes = "Update the system local authenticators." +
            " Currently only supports updating the AMR value. ", response = Authenticator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "System Local Authenticators", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful response", response = Authenticator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateSystemLocalAuthenticatorAmrValueById(@ApiParam(value = "ID of an authenticator",required=true) @PathParam("authenticator-id") String authenticatorId, @ApiParam(value = "This represents the user defined local authenticator to be created." ,required=true) @Valid
    SystemLocalAuthenticatorUpdate systemLocalAuthenticatorUpdate) {

        return delegate.updateSystemLocalAuthenticatorAmrValueById(authenticatorId,  systemLocalAuthenticatorUpdate );
    }

    @Valid
    @PUT
    @Path("/custom/{authenticator-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a user defined local authenticator. ", notes = "This API provides the capability to update a user defined local authenticator configurations. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/custom_authenticator/update <br> <b>Scope required:</b> <br>     * internal_custom_authenticator_update <br> ", response = Authenticator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "User defined local authenticators" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Authenticator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateUserDefinedLocalAuthenticator(@ApiParam(value = "ID of an authenticator",required=true) @PathParam("authenticator-id") String authenticatorId, @ApiParam(value = "This represents the user defined local authenticator to be created." ,required=true) @Valid UserDefinedLocalAuthenticatorUpdate userDefinedLocalAuthenticatorUpdate) {

        return delegate.updateUserDefinedLocalAuthenticator(authenticatorId,  userDefinedLocalAuthenticatorUpdate );
    }

}
