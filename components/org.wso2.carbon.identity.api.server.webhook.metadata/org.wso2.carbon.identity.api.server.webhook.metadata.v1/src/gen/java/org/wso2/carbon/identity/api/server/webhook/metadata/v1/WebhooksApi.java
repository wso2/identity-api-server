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

package org.wso2.carbon.identity.api.server.webhook.metadata.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.Error;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.EventProfile;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.WebhookMetadata;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.model.WebhookMetadataProperties;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.WebhooksApiService;
import org.wso2.carbon.identity.api.server.webhook.metadata.v1.factories.WebhooksApiServiceFactory;

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
    @GET
    @Path("/metadata/event-profiles/{profileName}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Event Profile Details", notes = "This API retrieves detailed information about a specific event profile including its channels and events. <b>Scope(Permission) required:</b> `internal_webhook_meta_view` ", response = EventProfile.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhook Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = EventProfile.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Profile not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response getEventProfile(@ApiParam(value = "",required=true) @PathParam("profileName") String profileName) {

        return delegate.getEventProfile(profileName );
    }

    @Valid
    @GET
    @Path("/metadata")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List Event Profiles", notes = "This API returns the list of event profiles supported by WSO2 Identity Server.   <b>Scope(Permission) required:</b> `internal_webhook_meta_view`   ", response = WebhookMetadata.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhook Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = WebhookMetadata.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response getEventProfiles() {

        return delegate.getEventProfiles();
    }

    @Valid
    @PATCH
    @Path("/metadata")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update webhook metadata properties", notes = "This API updates an webhook metadata property and return the updated webhook metadata properties.    <b>Scope (Permission) required:</b> ``internal_webhook_meta_update``  ", response = WebhookMetadataProperties.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhook Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = WebhookMetadataProperties.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchWebhookMetadata(@ApiParam(value = "" ,required=true) @Valid WebhookMetadataProperties webhookMetadataProperties) {

        return delegate.patchWebhookMetadata(webhookMetadataProperties );
    }

    @Valid
    @PUT
    @Path("/metadata")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Method Not Allowed", notes = "PUT operation is not supported on this resource.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhook Metadata" })
    @ApiResponses(value = { 
        @ApiResponse(code = 405, message = "Method Not Allowed", response = Error.class)
    })
    public Response putWebhookMetadataNotAllowed() {

        return delegate.putWebhookMetadataNotAllowed();
    }

}
