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

package org.wso2.carbon.identity.api.server.notification.sender.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.Error;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.NotificationSendersApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/notification-senders")
@Api(description = "The notification-senders API")

public class NotificationSendersApi  {

    @Autowired
    private NotificationSendersApiService delegate;

    @Valid
    @POST
    @Path("/email")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create an email sender", notes = "This API provides the capability to create an email sender.\\n\\nIf the 'name' is not not defined, 'EmailPublisher' is taken as the default name. <br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/add <br>   <b>Scope required:</b> <br>     * internal_config_mgt_add ", response = EmailSender.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful Response", response = EmailSender.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response createEmailSender(@ApiParam(value = "" ) @Valid EmailSenderAdd emailSenderAdd) {

        return delegate.createEmailSender(emailSenderAdd );
    }

    @Valid
    @POST
    @Path("/sms")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a SMS sender", notes = "This API provides the capability to create a SMS sender. If the 'name' is not not defined, 'SMSPublisher' is taken as the default name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/add <br>   <b>Scope required:</b> <br>     * internal_config_mgt_add ", response = SMSSender.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful Response", response = SMSSender.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response createSMSSender(@ApiParam(value = "" ) @Valid SMSSenderAdd smSSenderAdd) {

        return delegate.createSMSSender(smSSenderAdd );
    }

    @Valid
    @DELETE
    @Path("/email/{sender-name}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an email sender by name", notes = "This API provides the capability to delete an email sender by name. The URL encoded email sender name is used as sender-name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/delete <br>   <b>Scope required:</b> <br>     * internal_config_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteEmailSender(@ApiParam(value = "name of the email sender",required=true) @PathParam("sender-name") String senderName) {

        return delegate.deleteEmailSender(senderName );
    }

    @Valid
    @DELETE
    @Path("/sms/{sender-name}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a SMS sender by name", notes = "This API provides the capability to delete a SMS sender by name. The URL encoded SMS sender name is used as sender-name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/delete <br>   <b>Scope required:</b> <br>     * internal_config_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteSMSSender(@ApiParam(value = "name of the SMS sender",required=true) @PathParam("sender-name") String senderName) {

        return delegate.deleteSMSSender(senderName );
    }

    @Valid
    @GET
    @Path("/email/{sender-name}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve an email sender by name", notes = "This API provides the capability to retrieve an email sender by name. The URL encoded email sender name is used as sender-name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/view <br>   <b>Scope required:</b> <br>     * internal_config_mgt_view ", response = EmailSender.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = EmailSender.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getEmailSender(@ApiParam(value = "name of the email sender",required=true) @PathParam("sender-name") String senderName) {

        return delegate.getEmailSender(senderName );
    }

    @Valid
    @GET
    @Path("/email")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a list of email senders", notes = "This API provides the capability to retrieve the list of email senders. <br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/view <br>   <b>Scope required:</b> <br>     * internal_config_mgt_view ", response = EmailSender.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = EmailSender.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getEmailSenders() {

        return delegate.getEmailSenders();
    }

    @Valid
    @GET
    @Path("/sms/{sender-name}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a SMS sender by name", notes = "This API provides the capability to retrieve a SMS notification sender by name. The URL encoded SMS sender name is used as sender-name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/view <br>   <b>Scope required:</b> <br>     * internal_config_mgt_view ", response = SMSSender.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = SMSSender.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSMSSender(@ApiParam(value = "name of the SMS sender",required=true) @PathParam("sender-name") String senderName) {

        return delegate.getSMSSender(senderName );
    }

    @Valid
    @GET
    @Path("/sms")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a list of SMS senders", notes = "This API provides the capability to retrieve a list of SMS notification senders.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/view <br>   <b>Scope required:</b> <br>     * internal_config_mgt_view ", response = SMSSender.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = SMSSender.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSMSSenders() {

        return delegate.getSMSSenders();
    }

    @Valid
    @PUT
    @Path("/email/{sender-name}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an email sender", notes = "This API provides the capability to update an email sender by name. The URL encoded email sender name is used as sender-name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/update <br>   <b>Scope required:</b> <br>     * internal_config_mgt_update ", response = EmailSender.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = EmailSender.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateEmailSender(@ApiParam(value = "name of the email sender",required=true) @PathParam("sender-name") String senderName, @ApiParam(value = "" ,required=true) @Valid EmailSenderUpdateRequest emailSenderUpdateRequest) {

        return delegate.updateEmailSender(senderName,  emailSenderUpdateRequest );
    }

    @Valid
    @PUT
    @Path("/sms/{sender-name}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a SMS sender", notes = "This API provides the capability to update a SMS Sender. The URL encoded SMS sender name is used as sender-name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/update <br>   <b>Scope required:</b> <br>     * internal_config_mgt_update ", response = SMSSender.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Senders" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = SMSSender.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateSMSSender(@ApiParam(value = "name of the SMS sender",required=true) @PathParam("sender-name") String senderName, @ApiParam(value = "" ,required=true) @Valid SMSSenderUpdateRequest smSSenderUpdateRequest) {

        return delegate.updateSMSSender(senderName,  smSSenderUpdateRequest );
    }

}
