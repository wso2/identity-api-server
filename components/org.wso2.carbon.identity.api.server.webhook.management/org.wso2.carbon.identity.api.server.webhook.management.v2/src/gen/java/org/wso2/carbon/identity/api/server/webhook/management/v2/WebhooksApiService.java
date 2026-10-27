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

import org.wso2.carbon.identity.api.server.webhook.management.v2.*;
import org.wso2.carbon.identity.api.server.webhook.management.v2.model.*;
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
import javax.ws.rs.core.Response;


public interface WebhooksApiService {

      public Response activateWebhook(String webhookId);

      public Response createWebhook(WebhookCreateRequest webhookCreateRequest);

      public Response deactivateWebhook(String webhookId);

      public Response deleteWebhook(String webhookId);

      public Response getChannelSubscribedOrganizations(String webhookId, String channelId, Integer limit, Integer offset);

      public Response getWebhookByWebhookId(String webhookId);

      public Response getWebhookChannel(String webhookId, String channelId);

      public Response getWebhooks(Integer limit, Integer offset);

      public Response patchWebhook(String webhookId, WebhookPatchRequest webhookPatchRequest);

      public Response retryWebhook(String webhookId);

      public Response updateWebhook(String webhookId, WebhookUpdateRequest webhookUpdateRequest);
}
