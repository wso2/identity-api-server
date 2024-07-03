/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.action.management.v1.ActionBasicResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionModel;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionResponse;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionTypesResponseItem;
import org.wso2.carbon.identity.api.server.action.management.v1.Error;
import org.wso2.carbon.identity.api.server.action.management.v1.ActionsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/actions")
@Api(description = "The actions API")

public class ActionsApi  {

    @Autowired
    private ActionsApiService delegate;

    @Valid
    @POST
    @Path("/{actionType}/{actionId}/activate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Activates the action by given Id. ", notes = "This API provides the capability to activate an action by action Id. <br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/actionmgt/update <br>   <b>Scope required:</b> <br>       * internal_action_mgt_update ", response = ActionBasicResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ActionBasicResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response activateAction(@ApiParam(value = "Name of the Action Type.",required=true) @PathParam("actionType") String actionType, @ApiParam(value = "Id of the Action.",required=true) @PathParam("actionId") String actionId) {

        return delegate.activateAction(actionType,  actionId );
    }

    @Valid
    @POST
    @Path("/{actionType}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add action ", notes = "This API provides the capability to store the action information that is provided by users.<br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/actionmgt/create <br>   <b>Scope required:</b> <br>       * internal_action_mgt_create ", response = ActionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response.", response = ActionResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response createAction(@ApiParam(value = "Name of the Action Type.",required=true) @PathParam("actionType") String actionType, @ApiParam(value = "This represents the action to be created." ,required=true) @Valid ActionModel actionModel) {

        return delegate.createAction(actionType,  actionModel );
    }

    @Valid
    @POST
    @Path("/{actionType}/{actionId}/deactivate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Deactivates the action by given Id. ", notes = "This API provides the capability to deactivate an action by action Id. <br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/actionmgt/update <br>   <b>Scope required:</b> <br>       * internal_action_mgt_update ", response = ActionBasicResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ActionBasicResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deactivateAction(@ApiParam(value = "Name of the Action Type.",required=true) @PathParam("actionType") String actionType, @ApiParam(value = "Id of the Action.",required=true) @PathParam("actionId") String actionId) {

        return delegate.deactivateAction(actionType,  actionId );
    }

    @Valid
    @DELETE
    @Path("/{actionType}/{actionId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete action by action type and action id. ", notes = "This API provides the capability to delete an action by action type. <br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/actionmgt/delete <br>   <b>Scope required:</b> <br>       * internal_action_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteAction(@ApiParam(value = "Name of the Action Type.",required=true) @PathParam("actionType") String actionType, @ApiParam(value = "Id of the Action.",required=true) @PathParam("actionId") String actionId) {

        return delegate.deleteAction(actionType,  actionId );
    }

    @Valid
    @GET
    @Path("/{actionType}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List action ", notes = "This API provides the capability to retrieve the action by action type.<br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/actionmgt/view <br>   <b>Scope required:</b> <br>       * internal_action_mgt_view ", response = ActionResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ActionResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getActionsByActionType(@ApiParam(value = "Name of the Action Type.",required=true) @PathParam("actionType") String actionType) {

        return delegate.getActionsByActionType(actionType );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Detailed summary of the Action Types ", notes = "This API provides the capability to retrieve the detailed summary of the action types.<br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/actionmgt/view <br>   <b>Scope required:</b> <br>       * internal_action_mgt_view ", response = ActionTypesResponseItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ActionTypesResponseItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getActions() {

        return delegate.getActions();
    }

    @Valid
    @PUT
    @Path("/{actionType}/{actionId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update action by action type and action id. ", notes = "This API provides the capability to update an action by action type. <br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/actionmgt/update <br>   <b>Scope required:</b> <br>       * internal_action_mgt_update ", response = ActionResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Actions" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ActionResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateAction(@ApiParam(value = "Name of the Action Type.",required=true) @PathParam("actionType") String actionType, @ApiParam(value = "Id of the Action.",required=true) @PathParam("actionId") String actionId, @ApiParam(value = "This represents the action to be updated." ,required=true) @Valid ActionModel actionModel) {

        return delegate.updateAction(actionType,  actionId,  actionModel );
    }

}
