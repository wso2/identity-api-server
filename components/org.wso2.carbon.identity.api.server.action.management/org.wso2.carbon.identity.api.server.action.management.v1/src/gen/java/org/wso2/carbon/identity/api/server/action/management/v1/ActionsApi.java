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

package org.wso2.carbon.identity.api.server.action.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionTypesResponseItem;
import org.wso2.carbon.identity.api.server.action.management.v1.Error;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.api.server.action.management.v1.factories.ActionsApiServiceFactory;

@Path("/actions")
@Api(description = "The actions API")

public class ActionsApi  {

    private final ActionsApiService delegate;

    public ActionsApi() {

        this.delegate = ActionsApiServiceFactory.getActionsApi();
    }

    @Valid
    @POST
    @Path("/{actionType}/{actionId}/activate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Activate Action", notes = "This API activates an action using the action's type and unique ID.    <b>Scope (Permission) required:</b> ``internal_action_mgt_update``  ", response = ActionBasicResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Action Activated", response = ActionBasicResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response activateAction(@ApiParam(value = "Name of the Action Type.",required=true, allowableValues="preIssueAccessToken, preUpdatePassword, preUpdateProfile, preRegistration, preIssueIdToken") @PathParam("actionType") String actionType, @ApiParam(value = "Unique identifier of the action.",required=true) @PathParam("actionId") String actionId) {

        return delegate.activateAction(actionType,  actionId );
    }

    @Valid
    @POST
    @Path("/{actionType}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add Action", notes = "This API creates an action and returns the action details along with the action's unique ID.   <b>Scope (Permission) required:</b> ``internal_action_mgt_create``  ", response = ActionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Action Created", response = ActionResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response createAction(@ApiParam(value = "Name of the Action Type.",required=true, allowableValues="preIssueAccessToken, preUpdatePassword, preUpdateProfile, preRegistration, preIssueIdToken") @PathParam("actionType") String actionType, @ApiParam(value = "This represents the information of the action to be created." ,required=true) @Valid String body) {

        return delegate.createAction(actionType,  body );
    }

    @Valid
    @POST
    @Path("/{actionType}/{actionId}/deactivate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Deactivate Action", notes = "This API deactivates an action using the action's type and unique ID.    <b>Scope (Permission) required:</b> ``internal_action_mgt_update``  ", response = ActionBasicResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Action Deactivated", response = ActionBasicResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response deactivateAction(@ApiParam(value = "Name of the Action Type.",required=true, allowableValues="preIssueAccessToken, preUpdatePassword, preUpdateProfile, preRegistration, preIssueIdToken") @PathParam("actionType") String actionType, @ApiParam(value = "Unique identifier of the action.",required=true) @PathParam("actionId") String actionId) {

        return delegate.deactivateAction(actionType,  actionId );
    }

    @Valid
    @DELETE
    @Path("/{actionType}/{actionId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete Action", notes = "This API deletes an action using the action's type and unique ID.    <b>Scope (Permission) required:</b> ``internal_action_mgt_delete``  ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Action Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response deleteAction(@ApiParam(value = "Name of the Action Type.",required=true, allowableValues="preIssueAccessToken, preUpdatePassword, preUpdateProfile, preRegistration, preIssueIdToken") @PathParam("actionType") String actionType, @ApiParam(value = "Unique identifier of the action.",required=true) @PathParam("actionId") String actionId) {

        return delegate.deleteAction(actionType,  actionId );
    }

    @Valid
    @GET
    @Path("/{actionType}/{actionId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Action by ID", notes = "This API provides the capability to retrieve the action by action Id.    <b>Scope (Permission) required:</b> ``internal_action_mgt_view``  ", response = ActionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ActionResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getActionByActionId(@ApiParam(value = "Name of the Action Type.",required=true, allowableValues="preIssueAccessToken, preUpdatePassword, preUpdateProfile, preRegistration, preIssueIdToken") @PathParam("actionType") String actionType, @ApiParam(value = "Unique identifier of the action.",required=true) @PathParam("actionId") String actionId) {

        return delegate.getActionByActionId(actionType,  actionId );
    }

    @Valid
    @GET
    @Path("/types")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List Action Types", notes = "This API returns the detailed summary of the action types.   <b>Scope (Permission) required:</b> ``internal_action_mgt_view``  ", response = ActionTypesResponseItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Action Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ActionTypesResponseItem.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getActionTypes() {

        return delegate.getActionTypes();
    }

    @Valid
    @GET
    @Path("/{actionType}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List Actions", notes = "This API returns actions according to the action type.    <b>Scope (Permission) required:</b> ``internal_action_mgt_view``  ", response = ActionBasicResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ActionBasicResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getActionsByActionType(@ApiParam(value = "Name of the Action Type.",required=true, allowableValues="preIssueAccessToken, preUpdatePassword, preUpdateProfile, preRegistration, preIssueIdToken") @PathParam("actionType") String actionType) {

        return delegate.getActionsByActionType(actionType );
    }

    @Valid
    @PATCH
    @Path("/{actionType}/{actionId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Action", notes = "This API updates an action and return the updated action.    <b>Scope (Permission) required:</b> ``internal_action_mgt_update``  ", response = ActionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Action Updated", response = ActionResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response updateAction(@ApiParam(value = "Name of the Action Type.",required=true, allowableValues="preIssueAccessToken, preUpdatePassword, preUpdateProfile, preRegistration, preIssueIdToken") @PathParam("actionType") String actionType, @ApiParam(value = "Unique identifier of the action.",required=true) @PathParam("actionId") String actionId, @ApiParam(value = "This represents the action to be updated." ,required=true) @Valid String body) {

        return delegate.updateAction(actionType,  actionId,  body );
    }

}
