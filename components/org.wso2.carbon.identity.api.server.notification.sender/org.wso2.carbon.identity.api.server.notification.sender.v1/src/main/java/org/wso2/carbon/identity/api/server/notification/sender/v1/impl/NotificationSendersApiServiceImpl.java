/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.notification.sender.v1.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.base.MultitenantConstants;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.notification.sender.v1.NotificationSendersApiService;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.NotificationSenderManagementService;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderUpdateRequest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_PUBLISHER_TYPE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.NOTIFICATION_SENDER_CONTEXT_PATH;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PLUS;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMS_PUBLISHER_TYPE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.URL_ENCODED_SPACE;

/**
 * Implementation of notification senders REST API.
 */
public class NotificationSendersApiServiceImpl implements NotificationSendersApiService {

    @Autowired
    private NotificationSenderManagementService notificationSenderManagementService;

    @Override
    public Response createEmailSender(EmailSenderAdd emailSenderAdd) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        EmailSender emailSender = notificationSenderManagementService.addEmailSender(emailSenderAdd);
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(
                    V1_API_PATH_COMPONENT + NOTIFICATION_SENDER_CONTEXT_PATH + "/" + EMAIL_PUBLISHER_TYPE + "/" +
                            URLEncoder.encode(emailSender.getName(), StandardCharsets.UTF_8.name())
                                    .replace(PLUS, URL_ENCODED_SPACE));
        } catch (UnsupportedEncodingException e) {
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withMessage("Error due to unsupported encoding.").build();
            throw new APIError(Response.Status.METHOD_NOT_ALLOWED, errorResponse);
        }
        return Response.created(location).entity(emailSender).build();
    }

    @Override
    public Response createSMSSender(SMSSenderAdd smSSenderAdd) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        SMSSender smsSender = notificationSenderManagementService.addSMSSender(smSSenderAdd);
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(
                    V1_API_PATH_COMPONENT + NOTIFICATION_SENDER_CONTEXT_PATH + "/" + SMS_PUBLISHER_TYPE + "/" +
                            URLEncoder.encode(smsSender.getName(), StandardCharsets.UTF_8.name())
                                    .replace(PLUS, URL_ENCODED_SPACE));
        } catch (UnsupportedEncodingException e) {
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withMessage("Error due to unsupported encoding.").build();
            throw new APIError(Response.Status.METHOD_NOT_ALLOWED, errorResponse);
        }
        return Response.created(location).entity(smsSender).build();
    }

    @Override
    public Response deleteEmailSender(String senderName) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        notificationSenderManagementService.deleteNotificationSender(senderName);
        return Response.noContent().build();
    }

    @Override
    public Response deleteSMSSender(String senderName) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        notificationSenderManagementService.deleteNotificationSender(senderName);
        return Response.noContent().build();
    }

    @Override
    public Response getEmailSender(String senderName) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok().entity(notificationSenderManagementService.getEmailSender(senderName)).build();
    }

    @Override
    public Response getEmailSenders() {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok().entity(notificationSenderManagementService.getEmailSenders()).build();
    }

    @Override
    public Response getSMSSender(String senderName) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok().entity(notificationSenderManagementService.getSMSSender(senderName)).build();
    }

    @Override
    public Response getSMSSenders() {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok().entity(notificationSenderManagementService.getSMSSenders()).build();
    }

    @Override
    public Response updateEmailSender(String senderName, EmailSenderUpdateRequest emailSenderUpdateRequest) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok()
                .entity(notificationSenderManagementService.updateEmailSender(senderName, emailSenderUpdateRequest))
                .build();
    }

    @Override
    public Response updateSMSSender(String senderName, SMSSenderUpdateRequest smSSenderUpdateRequest) {

        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok()
                .entity(notificationSenderManagementService.updateSMSSender(senderName, smSSenderUpdateRequest))
                .build();
    }
}
