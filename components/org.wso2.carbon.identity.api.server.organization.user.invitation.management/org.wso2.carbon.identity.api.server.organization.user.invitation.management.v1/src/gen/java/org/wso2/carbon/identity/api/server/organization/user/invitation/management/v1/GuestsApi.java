/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.factories.GuestsApiServiceFactory;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.AcceptanceRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.IntrospectRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.IntrospectSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationsListResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.GuestsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/guests")
@Api(description = "The guests API")

public class GuestsApi  {

    private final GuestsApiService delegate;

    public GuestsApi() {

        this.delegate = GuestsApiServiceFactory.getGuestsApi();
    }

    @Valid
    @POST
    @Path("/invitation/accept")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Accepts an invitation from a user in the parent organization", notes = "After user clicks on the link provided, the redirected application should invoke this API. This API is a public API and this should be invoked with the confirmation code which is  appended to the notification.  <b>Scope(Permission) required:</b> None ", response = Void.class, tags={ "Parent Organization User Invitation", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response invitationAcceptPost(@ApiParam(value = "Details that need to confirm an invitation" ,required=true) @Valid AcceptanceRequestBody acceptanceRequestBody) {

        return delegate.invitationAcceptPost(acceptanceRequestBody );
    }

    @Valid
    @DELETE
    @Path("/invitations/{invitationId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an invitation", notes = "Based on the requirements the invitations which are initiated by the same organization can be deleted. This should be invoked from an access token issued from an administrator of that organization.  <b>Scope(Permission) required:</b> `internal_org_guest_mgt_invite_delete` ", response = Void.class, tags={ "Invitation Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successful Response and if the resource not found", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response invitationDelete(@ApiParam(value = "ID of the invitation to delete",required=true) @PathParam("invitationId") String invitationId) {

        return delegate.invitationDelete(invitationId );
    }

    @Valid
    @POST
    @Path("/invitation/introspect")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "introspect an invitation's confirmation code", notes = "This API can be used to introspect the confirmation code. This will be  invoked from the application with the access token of the user which was logged into the application and switched to the organization where  the user resides in.  <b>Scope(Permission) required:</b> `internal_org_guest_mgt_invite_list` ", response = IntrospectSuccessResponse.class, tags={ "Parent Organization User Invitation", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = IntrospectSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response invitationIntrospectPost(@ApiParam(value = "Details that need to introspect an invitation" ,required=true) @Valid IntrospectRequestBody introspectRequestBody) {

        return delegate.invitationIntrospectPost(introspectRequestBody );
    }

    @Valid
    @GET
    @Path("/invitations")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List down the user invitations", notes = "List down the invitations triggered from the current organization. This should be invoked from an access token issued from an administrator of that organization.  <b>Scope(Permission) required:</b> `internal_org_guest_mgt_invite_list` ", response = InvitationsListResponse.class, tags={ "Invitation Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = InvitationsListResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response invitationListGet(    @Valid@ApiParam(value = "Filtering the invitation based on the status. Status can be PENDING or EXPIRED.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Maximum number of records to return _This parameter is not supported yet._ ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Starting index of the pagination _This parameter is not supported yet._ ")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Sort order of the returned records. Either ASC or DESC _This parameter is not supported yet._ ", allowableValues="ASC, DESC")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Sort by a specific field _This parameter is not supported yet._ ")  @QueryParam("sortBy") String sortBy) {

        return delegate.invitationListGet(filter,  limit,  offset,  sortOrder,  sortBy );
    }

    @Valid
    @POST
    @Path("/invite")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Invite a parent organization user to a child organization", notes = "Initiates an invitation to a user in the parent organization to onboard to the child organization. This will be initiated from the child organization.  <b>Scope(Permission) required:</b> `internal_org_guest_mgt_invite_add` ", response = InvitationSuccessResponse.class, responseContainer = "List", tags={ "Parent Organization User Invitation" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful Response", response = InvitationSuccessResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response invitationTriggerPost(@ApiParam(value = "Details that need to initiate an invitation" ,required=true) @Valid InvitationRequestBody invitationRequestBody) {

        return delegate.invitationTriggerPost(invitationRequestBody );
    }

}
