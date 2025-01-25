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

package org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.impl;

import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.GuestsApiService;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.core.GuestApiServiceCore;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.factories.GuestApiServiceCoreFactory;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.AcceptanceRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.IntrospectRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.IntrospectSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationRequestBody;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationSuccessResponse;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.model.InvitationsListResponse;

import java.util.List;
import javax.ws.rs.core.Response;

/**
 * Implementation of the invitation management APIs.
 */
public class GuestsApiServiceImpl implements GuestsApiService {

    private final GuestApiServiceCore guestApiServiceCore;

    public GuestsApiServiceImpl() {

        try {
            this.guestApiServiceCore = GuestApiServiceCoreFactory.getGuestApiServiceCore();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating user invitation management services.", e);
        }
    }

    @Override
    public Response invitationAcceptPost(AcceptanceRequestBody acceptanceRequestBody) {

        guestApiServiceCore.acceptInvitation(acceptanceRequestBody);
        return Response.noContent().build();
    }

    @Override
    public Response invitationDelete(String invitationId) {

        guestApiServiceCore.deleteInvitation(invitationId);
        return Response.noContent().build();
    }

    @Override
    public Response invitationIntrospectPost(IntrospectRequestBody introspectRequestBody) {

        IntrospectSuccessResponse introspectSuccessResponse =
                guestApiServiceCore.introspectInvitation(introspectRequestBody.getConfirmationCode());
        return Response.ok().entity(introspectSuccessResponse).build();
    }

    @Override
    public Response invitationListGet(String filter, Integer limit, Integer offset, String sortOrder, String sortBy) {

        InvitationsListResponse invitationsListResponse = guestApiServiceCore.getInvitations(filter, limit, offset,
                sortOrder, sortBy);
        return Response.ok().entity(invitationsListResponse).build();
    }

    @Override
    public Response invitationTriggerPost(InvitationRequestBody invitationRequestBody) {

        List<InvitationSuccessResponse> invitationSuccessResponse =
                guestApiServiceCore.createInvitation(invitationRequestBody);
        return Response.ok().entity(invitationSuccessResponse).build();
    }
}
