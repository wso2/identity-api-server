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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log log = LogFactory.getLog(GuestsApiServiceImpl.class);
    private final GuestApiServiceCore guestApiServiceCore;

    public GuestsApiServiceImpl() {

        try {
            this.guestApiServiceCore = GuestApiServiceCoreFactory.getGuestApiServiceCore();
            if (log.isDebugEnabled()) {
                log.debug("GuestsApiServiceImpl initialized successfully.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating user invitation management services.", e);
            throw new RuntimeException("Error occurred while initiating user invitation management services.", e);
        }
    }

    @Override
    public Response invitationAcceptPost(AcceptanceRequestBody acceptanceRequestBody) {

        if (log.isDebugEnabled()) {
            log.debug("Processing invitation acceptance request.");
        }
        guestApiServiceCore.acceptInvitation(acceptanceRequestBody);
        log.info("Invitation acceptance processed successfully.");
        return Response.noContent().build();
    }

    @Override
    public Response invitationDelete(String invitationId) {

        if (log.isDebugEnabled()) {
            log.debug("Processing invitation deletion request for invitationId: " + invitationId);
        }
        guestApiServiceCore.deleteInvitation(invitationId);
        log.info("Invitation deleted successfully for invitationId: " + invitationId);
        return Response.noContent().build();
    }

    @Override
    public Response invitationIntrospectPost(IntrospectRequestBody introspectRequestBody) {

        if (log.isDebugEnabled()) {
            log.debug("Processing invitation introspection request.");
        }
        IntrospectSuccessResponse introspectSuccessResponse =
                guestApiServiceCore.introspectInvitation(introspectRequestBody.getConfirmationCode());
        log.info("Invitation introspection completed successfully.");
        return Response.ok().entity(introspectSuccessResponse).build();
    }

    @Override
    public Response invitationListGet(String filter, Integer limit, Integer offset, String sortOrder, String sortBy) {

        if (log.isDebugEnabled()) {
            log.debug("Processing invitations list request with filter: " + filter);
        }
        InvitationsListResponse invitationsListResponse = guestApiServiceCore.getInvitations(filter, limit, offset,
                sortOrder, sortBy);
        log.info("Invitations list retrieved successfully.");
        return Response.ok().entity(invitationsListResponse).build();
    }

    @Override
    public Response invitationTriggerPost(InvitationRequestBody invitationRequestBody) {

        if (invitationRequestBody == null) {
            log.error("InvitationRequestBody cannot be null in invitation creation request.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (log.isDebugEnabled()) {
            log.debug("Processing invitation creation request for usernames: " +
                    (invitationRequestBody.getUsernames() != null ?
                            invitationRequestBody.getUsernames().size() + " users" : "unknown users"));
        }
        List<InvitationSuccessResponse> invitationSuccessResponse =
                guestApiServiceCore.createInvitation(invitationRequestBody);
        log.info("Invitation creation processed successfully.");
        return Response.ok().entity(invitationSuccessResponse).build();
    }
}
