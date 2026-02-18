/*
 * Copyright (c) 2020-2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.notification.sender.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.notification.sender.v1.factories.NotificationSendersApiServiceFactory;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.Error;
import java.util.Map;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.PushSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.PushSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.PushSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderUpdateRequest;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/notification-senders")
@Api(description = "The notification-senders API")

public class NotificationSendersApi  {

    private final NotificationSendersApiService delegate;

    public NotificationSendersApi() {

        this.delegate = NotificationSendersApiServiceFactory.getNotificationSendersApi();
    }

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
    @Path("/push")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a push notification sender", notes = "This API provides the capability to create a push notification sender.\\n\\nIf the 'name' is not defined, 'PushPublisher' is taken as the default name. <br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/add <br>   <b>Scope required:</b> <br>     * internal_config_mgt_add ", response = PushSender.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Push Notification Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful Response", response = PushSender.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response createPushSender(@ApiParam(value = "" ) @Valid PushSenderAdd pushSenderAdd) {

        return delegate.createPushSender(pushSenderAdd );
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
    @Path("/push/{sender-name}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a push notification sender by name", notes = "This API provides the capability to delete a push notification sender by name. The URL encoded push notification sender name is used as sender-name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/delete <br>   <b>Scope required:</b> <br>     * internal_config_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Push Notification Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deletePushSender(@ApiParam(value = "name of the email sender",required=true) @PathParam("sender-name") String senderName) {

        return delegate.deletePushSender(senderName );
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
    @Path("/configs/{publisher-type}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get notification sender configurations by publisher type", notes = "This API provides the capability to retrieve notification sender configurations by publisher type. <br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/view <br>   <b>Scope required:</b> <br>     * internal_config_mgt_view ", response = String.class, responseContainer = "Map", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Notification Sender Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Map.class, responseContainer = "Map"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getNotiSenderConfigurations(@ApiParam(value = "Type of the notification publisher (e.g., email, sms, push)",required=true) @PathParam("publisher-type") String publisherType) {

        return delegate.getNotiSenderConfigurations(publisherType );
    }

    @Valid
    @GET
    @Path("/push/{sender-name}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve a push notification sender by name", notes = "This API provides the capability to retrieve a push notification sender by name. The URL encoded push notification sender name is used as sender-name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/view <br>   <b>Scope required:</b> <br>     * internal_config_mgt_view ", response = PushSender.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Push Notification Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = PushSender.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getPushSender(@ApiParam(value = "name of the push notification sender",required=true) @PathParam("sender-name") String senderName) {

        return delegate.getPushSender(senderName );
    }

    @Valid
    @GET
    @Path("/push")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a list of push notification senders", notes = "This API provides the capability to retrieve the list of push notification senders. <br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/view <br>   <b>Scope required:</b> <br>     * internal_config_mgt_view ", response = PushSender.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Push Notification Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = PushSender.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getPushSenders() {

        return delegate.getPushSenders();
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
    @Path("/configs/{publisher-type}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update notification sender configurations by publisher type", notes = "This API provides the capability to update notification sender configurations by publisher type. <br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/update <br>   <b>Scope required:</b> <br>     * internal_config_mgt_update ", response = String.class, responseContainer = "Map", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Notification Sender Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Map.class, responseContainer = "Map"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response setNotiSenderConfigurations(@ApiParam(value = "Type of the notification publisher (e.g., email, sms, push)",required=true) @PathParam("publisher-type") String publisherType, @ApiParam(value = "" ,required=true) @Valid Map<String, String> requestBody) {

        return delegate.setNotiSenderConfigurations(publisherType,  requestBody );
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
    @Path("/push/{sender-name}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a push notification sender", notes = "This API provides the capability to update a push notification sender by name. The URL encoded push notification sender name is used as sender-name.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/update <br>   <b>Scope required:</b> <br>     * internal_config_mgt_update ", response = PushSender.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Push Notification Senders", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = PushSender.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updatePushSender(@ApiParam(value = "name of the push notification sender",required=true) @PathParam("sender-name") String senderName, @ApiParam(value = "" ,required=true) @Valid PushSenderUpdateRequest pushSenderUpdateRequest) {

        return delegate.updatePushSender(senderName,  pushSenderUpdateRequest );
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
