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

package org.wso2.carbon.identity.api.server.webhook.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.webhook.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookList;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v1.model.WebhookResponse;
import org.wso2.carbon.identity.api.server.webhook.management.v1.WebhooksApiService;
import org.wso2.carbon.identity.api.server.webhook.management.v1.factories.WebhooksApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/webhooks")
@Api(description = "The webhooks API")

public class WebhooksApi  {

    private final WebhooksApiService delegate;

    public WebhooksApi() {

        this.delegate = WebhooksApiServiceFactory.getWebhooksApi();
    }

    @Valid
    @POST
    @Path("/{webhookId}/activate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Activate Webhook", notes = "Activate a webhook by its unique ID.   <b>Scope(Permission) required:</b> `internal_webhook_update`   ", response = WebhookResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhooks", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Webhook Activated", response = WebhookResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Webhook not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response activateWebhook(@ApiParam(value = "",required=true) @PathParam("webhookId") String webhookId) {

        return delegate.activateWebhook(webhookId );
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add Webhook", notes = "Create a new webhook.    <b>Scope(Permission) required:</b> `internal_webhook_create`   ", response = WebhookResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhooks", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Webhook Created", response = WebhookResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response createWebhook(@ApiParam(value = "" ,required=true) @Valid WebhookRequest webhookRequest) {

        return delegate.createWebhook(webhookRequest );
    }

    @Valid
    @POST
    @Path("/{webhookId}/deactivate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Deactivate Webhook", notes = "Deactivate a webhook by its unique ID.   <b>Scope(Permission) required:</b> `internal_webhook_update`   ", response = WebhookResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhooks", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Webhook Deactivated", response = WebhookResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Webhook not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response deactivateWebhook(@ApiParam(value = "",required=true) @PathParam("webhookId") String webhookId) {

        return delegate.deactivateWebhook(webhookId );
    }

    @Valid
    @DELETE
    @Path("/{webhookId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete Webhook", notes = "This API deletes a webhook using the webhook unique ID.    <b>Scope(Permission) required:</b> `internal_webhook_delete`   ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhooks", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Webhook Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Webhook not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response deleteWebhook(@ApiParam(value = "",required=true) @PathParam("webhookId") String webhookId) {

        return delegate.deleteWebhook(webhookId );
    }

    @Valid
    @GET
    @Path("/{webhookId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Webhook by ID", notes = "This API returns the webhook details along with the webhook unique ID.    <b>Scope(Permission) required:</b> `internal_webhook_view`   ", response = WebhookResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhooks", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = WebhookResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Webhook not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response getWebhookByWebhookId(@ApiParam(value = "",required=true) @PathParam("webhookId") String webhookId) {

        return delegate.getWebhookByWebhookId(webhookId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List Webhooks", notes = "This API returns the detailed summary of the webhooks.   <b>Scope(Permission) required:</b> `internal_webhook_view`   ", response = WebhookList.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhooks", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = WebhookList.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response getWebhooks() {

        return delegate.getWebhooks();
    }

    @Valid
    @POST
    @Path("/{webhookId}/retry")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retry Webhook Subscription or Unsubscription", notes = "Retry a webhook subscription or unsubscription by its unique ID.   <b>Scope(Permission) required:</b> `internal_webhook_update`   ", response = WebhookResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhooks", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Webhook Retried", response = WebhookResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Webhook not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response retryWebhook(@ApiParam(value = "",required=true) @PathParam("webhookId") String webhookId) {

        return delegate.retryWebhook(webhookId );
    }

    @Valid
    @PUT
    @Path("/{webhookId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Webhook", notes = "This API updates a webhook and return the updated webhook.    <b>Scope(Permission) required:</b> `internal_webhook_update`   ", response = WebhookResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhooks" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Webhook Updated", response = WebhookResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Webhook not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response updateWebhook(@ApiParam(value = "",required=true) @PathParam("webhookId") String webhookId, @ApiParam(value = "" ,required=true) @Valid WebhookRequest webhookRequest) {

        return delegate.updateWebhook(webhookId,  webhookRequest );
    }

}
