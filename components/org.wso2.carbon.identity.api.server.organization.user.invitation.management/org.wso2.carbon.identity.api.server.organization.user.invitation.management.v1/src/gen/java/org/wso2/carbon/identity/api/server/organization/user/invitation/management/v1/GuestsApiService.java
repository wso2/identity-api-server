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

import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.*;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.AcceptanceRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.IntrospectRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.IntrospectSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationsListResponse;
import javax.ws.rs.core.Response;


public interface GuestsApiService {

      public Response invitationAcceptPost(AcceptanceRequestBody acceptanceRequestBody);

      public Response invitationDelete(String invitationId);

      public Response invitationIntrospectPost(IntrospectRequestBody introspectRequestBody);

      public Response invitationListGet(String filter, Integer limit, Integer offset, String sortOrder, String sortBy);

      public Response invitationTriggerPost(InvitationRequestBody invitationRequestBody);
}
