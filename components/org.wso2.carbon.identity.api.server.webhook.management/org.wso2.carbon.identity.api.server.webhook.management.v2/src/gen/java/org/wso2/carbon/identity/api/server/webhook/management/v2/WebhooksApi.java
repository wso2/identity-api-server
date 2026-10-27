/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.webhook.management.v2;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.webhook.management.v2.model.ChannelResponse;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.Error;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.SubscribedOrganizationList;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookCreateRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookList;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookPatchRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookResponse;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.WebhookUpdateRequest;
import org.wso2.carbon.identity.api.server.webhook.management.v2.WebhooksApiService;
import org.wso2.carbon.identity.api.server.webhook.management.v2.factories.WebhooksApiServiceFactory;

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
    public Response activateWebhook(@ApiParam(value = "Unique identifier of the webhook.",required=true) @PathParam("webhookId") String webhookId) {

        return delegate.activateWebhook(webhookId );
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add Webhook", notes = "Create a new webhook.    `subscribedChannels` is a list of channel objects. Each channel may carry its organization fanout configuration inline under `childOrgSubscription`, so the webhook and its fanout are created together in one atomic call. A channel that omits it defaults to `subscriptionPolicy: NONE`.   Fanout is changed later through the webhook replace and patch operations, which accept the same per-channel `childOrgSubscription`.   <b>Scope(Permission) required:</b> `internal_webhook_create`   ", response = WebhookResponse.class, authorizations = {
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
    public Response createWebhook(@ApiParam(value = "" ,required=true) @Valid WebhookCreateRequest webhookCreateRequest) {

        return delegate.createWebhook(webhookCreateRequest );
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
    public Response deactivateWebhook(@ApiParam(value = "Unique identifier of the webhook.",required=true) @PathParam("webhookId") String webhookId) {

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
    public Response deleteWebhook(@ApiParam(value = "Unique identifier of the webhook.",required=true) @PathParam("webhookId") String webhookId) {

        return delegate.deleteWebhook(webhookId );
    }

    @Valid
    @GET
    @Path("/{webhookId}/channels/{channelId}/organizations")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List Child Organizations Subscribed to a Channel", notes = "This API returns the child organizations currently receiving events for the given channel, resolved with names and parent references.   For `SELECTED_ORGS_ONLY` this is the configured set; for `ALL_EXISTING_AND_FUTURE_ORGS` it is every child organization currently subscribed, which grows as organizations are created; for `NONE` the list is empty.   This is the only representation of the subscribed organizations. The webhook and channel retrieval APIs report the policy and link here, because the collection is paginated and cannot be embedded in either representation.   Results are ordered parents before children, so the hierarchy can be reconstructed from `parentOrgId` in a single pass.   <b>Scope(Permission) required:</b> `internal_webhook_view`   ", response = SubscribedOrganizationList.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhook Channels", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SubscribedOrganizationList.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Webhook or channel not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response getChannelSubscribedOrganizations(@ApiParam(value = "Unique identifier of the webhook.",required=true) @PathParam("webhookId") String webhookId, @ApiParam(value = "Unique identifier of the subscribed channel, returned as `channelId` on each entry in `subscribedChannels`. ",required=true) @PathParam("channelId") String channelId,     @Valid @Min(1) @Max(500)@ApiParam(value = "Maximum number of records to return.", defaultValue="50") @DefaultValue("50")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination.", defaultValue="0") @DefaultValue("0")  @QueryParam("offset") Integer offset) {

        return delegate.getChannelSubscribedOrganizations(webhookId,  channelId,  limit,  offset );
    }

    @Valid
    @GET
    @Path("/{webhookId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Webhook by ID", notes = "This API returns the webhook details along with the webhook unique ID.    Each entry in `subscribedChannels` reports the channel URI, its identifier, its subscription status, its child organization fanout policy and a `self` link. The organizations themselves are not included — follow `childOrgSubscription.organizationsRef` to retrieve them, paginated and resolved with names and parent references.   This representation is exactly what the replace operation accepts, so it can be modified and sent back without reshaping.   <b>Scope(Permission) required:</b> `internal_webhook_view`   ", response = WebhookResponse.class, authorizations = {
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
    public Response getWebhookByWebhookId(@ApiParam(value = "Unique identifier of the webhook.",required=true) @PathParam("webhookId") String webhookId) {

        return delegate.getWebhookByWebhookId(webhookId );
    }

    @Valid
    @GET
    @Path("/{webhookId}/channels/{channelId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Subscribed Channel by ID", notes = "This API returns a single subscribed channel of a webhook, with its subscription status and child organization fanout policy.   The `channelId` is the unique identifier of the subscribed channel, returned as `channelId` on each entry in `subscribedChannels` by the webhook retrieval API, which also carries the `self` link to this resource.   This response carries `childOrgSubscription.organizationsRef`, the link to the child organizations receiving events for the channel. That link is present here and not in the webhook representation, so each representation links exactly one level down and the webhook response stays free of nested organization links.   <b>Scope(Permission) required:</b> `internal_webhook_view`   ", response = ChannelResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhook Channels", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ChannelResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Webhook or channel not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response getWebhookChannel(@ApiParam(value = "Unique identifier of the webhook.",required=true) @PathParam("webhookId") String webhookId, @ApiParam(value = "Unique identifier of the subscribed channel, returned as `channelId` on each entry in `subscribedChannels`. ",required=true) @PathParam("channelId") String channelId) {

        return delegate.getWebhookChannel(webhookId,  channelId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List Webhooks", notes = "This API returns the detailed summary of the webhooks.   Results are paginated. Use `limit` and `offset` to page through the collection; `totalResults` reports the full size and `links` carries the next and previous pages where applicable.   <b>Scope(Permission) required:</b> `internal_webhook_view`   ", response = WebhookList.class, authorizations = {
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
    public Response getWebhooks(    @Valid @Min(1) @Max(100)@ApiParam(value = "Maximum number of records to return.", defaultValue="25") @DefaultValue("25")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination.", defaultValue="0") @DefaultValue("0")  @QueryParam("offset") Integer offset) {

        return delegate.getWebhooks(limit,  offset );
    }

    @Valid
    @PATCH
    @Path("/{webhookId}")
    @Consumes({ "application/merge-patch+json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch Webhook by ID", notes = "This API partially updates a webhook and returns the updated webhook.    Only the properties present in the request body are modified; every omitted property is left unchanged. This avoids the full-replacement behaviour of `PUT`, which clears any writable property the client does not resend.   Arrays are replaced wholesale rather than merged. Supplying `subscribedChannels` therefore replaces the entire channel set, with the same semantics as `PUT`, including per-channel `childOrgSubscription`. Because the retrieval API returns every channel with its policy, a client can read the webhook, adjust one channel in the array and send the whole set back.   `status` cannot be patched; use the activate and deactivate sub-resources.   <b>JSON Merge Patch</b>   The request body is a JSON Merge Patch document (RFC 7386), sent as `application/merge-patch+json`. It is not JSON Patch (RFC 6902) — there is no operation list; the body is a partial representation of the webhook itself.   Two deviations from RFC 7386 apply, both of which fail loudly rather than silently:   - A `null` member is <b>rejected with 400</b> rather than removing the property. No webhook property has a valid removed state: `endpoint`, `name`, `eventProfile` and `subscribedChannels` are required; a channel's organization fanout is cleared by setting `subscriptionPolicy` to `NONE` rather than by removal; and `secret` must never be removed, since a webhook that cannot sign its payloads delivers events no receiver can verify. Nulls are rejected instead of ignored so that a client assuming removal is told, rather than receiving `200` for a change that did not happen. - An empty document `{}` is <b>rejected with 400</b>, although RFC 7386 defines it as a valid no-op. An empty patch is almost always a client error and is worth reporting.   Should a property that can validly be removed be introduced later, it will be marked `nullable` and accept `null` as a removal directive; the rejection above applies only to properties for which removal has no valid result.   <b>Scope(Permission) required:</b> `internal_webhook_update`   ", response = WebhookResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Webhooks", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Webhook Updated", response = WebhookResponse.class),
        @ApiResponse(code = 400, message = "Bad Request. Also returned when a member is `null` — no webhook property has a valid removed state, so `null` is rejected rather than treated as a removal directive — and when the document is empty. ", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Webhook not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error", response = Error.class)
    })
    public Response patchWebhook(@ApiParam(value = "Unique identifier of the webhook.",required=true) @PathParam("webhookId") String webhookId, @ApiParam(value = "" ,required=true) @Valid WebhookPatchRequest webhookPatchRequest) {

        return delegate.patchWebhook(webhookId,  webhookPatchRequest );
    }

    @Valid
    @POST
    @Path("/{webhookId}/retry")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retry Webhook Subscription or Unsubscription", notes = "Retry a webhook subscription or unsubscription by its unique ID.   Every channel currently in `SUBSCRIPTION_ERROR` or `UNSUBSCRIPTION_ERROR` is retried in the direction its status indicates. Channels in any other state are left untouched.   <b>Scope(Permission) required:</b> `internal_webhook_update`   ", response = WebhookResponse.class, authorizations = {
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
    public Response retryWebhook(@ApiParam(value = "Unique identifier of the webhook.",required=true) @PathParam("webhookId") String webhookId) {

        return delegate.retryWebhook(webhookId );
    }

    @Valid
    @PUT
    @Path("/{webhookId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Webhook", notes = "This API updates a webhook and return the updated webhook.    The request body is exactly the representation returned by the retrieval API, minus the read-only properties — which are ignored rather than rejected if supplied. A webhook can therefore be retrieved, modified and sent back unchanged otherwise.   This is a full replacement: a writable property omitted from the body is cleared. The two write-only properties are the exception — `secret` and `subscribedChannels[].childOrgSubscription.organizations` are never returned, so a client cannot resend what it did not receive, and omitting either preserves the stored value.   <b>Channels</b>   `subscribedChannels` is replaced wholesale: channels stored but absent from the array are unsubscribed, and channels present but not stored are subscribed.   Each channel may carry its `childOrgSubscription`, so a single call can change the webhook, its channel set and its per-channel fanout together. For a channel that omits `childOrgSubscription`, an existing subscription keeps its current policy and a new one defaults to `NONE`. Within a supplied `childOrgSubscription`, omitting `organizations` preserves the channel's current organization subscriptions; supplying it replaces them. To stop organization fanout for a channel, set its `subscriptionPolicy` to `NONE` — an empty `organizations` array is rejected, since `SELECTED_ORGS_ONLY` with nothing selected is the same state as `NONE`.   Removing a channel and adding it back in the same request resets its fanout, because the subscription is recreated.   <b>Scope(Permission) required:</b> `internal_webhook_update`   ", response = WebhookResponse.class, authorizations = {
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
    public Response updateWebhook(@ApiParam(value = "Unique identifier of the webhook.",required=true) @PathParam("webhookId") String webhookId, @ApiParam(value = "" ,required=true) @Valid WebhookUpdateRequest webhookUpdateRequest) {

        return delegate.updateWebhook(webhookId,  webhookUpdateRequest );
    }

}
