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

package org.wso2.carbon.identity.api.server.notification.sender.v2.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.notification.sender.v2.NotificationSendersApiService;
import org.wso2.carbon.identity.api.server.notification.sender.v2.core.NotificationSenderManagementService;
import org.wso2.carbon.identity.api.server.notification.sender.v2.factories.NotificationSenderManagementServiceFactory;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.PushSender;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.PushSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.PushSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.SMSSender;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.SMSSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.SMSSenderUpdateRequest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V2_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_PUBLISHER_TYPE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.NOTIFICATION_SENDER_CONTEXT_PATH;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PLUS;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PUSH_PUBLISHER_TYPE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMS_PUBLISHER_TYPE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.URL_ENCODED_SPACE;

/**
 * Implementation of notification senders REST API.
 */
public class NotificationSendersApiServiceImpl implements NotificationSendersApiService {

    private static final Log log = LogFactory.getLog(NotificationSendersApiServiceImpl.class);
    private final NotificationSenderManagementService notificationSenderManagementService;

    public NotificationSendersApiServiceImpl() {

        try {
            this.notificationSenderManagementService = NotificationSenderManagementServiceFactory
                    .getNotificationSenderManagementService();
            if (log.isDebugEnabled()) {
                log.debug("NotificationSendersApiServiceImpl initialized successfully");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating notification sender service.", e);
            throw new RuntimeException("Error occurred while initiating notification sender service.", e);
        }
    }

    @Override
    public Response createEmailSender(EmailSenderAdd emailSenderAdd) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Creating email sender: %s",
                    emailSenderAdd != null ? emailSenderAdd.getName() : "null"));
        }
        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email sender creation is not allowed for super tenant");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        EmailSender emailSender = notificationSenderManagementService.addEmailSender(emailSenderAdd);
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(
                    V2_API_PATH_COMPONENT + NOTIFICATION_SENDER_CONTEXT_PATH + "/" + EMAIL_PUBLISHER_TYPE + "/" +
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
    public Response createPushSender(PushSenderAdd pushSenderAdd) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Creating push sender: %s",
                    pushSenderAdd != null ? pushSenderAdd.getName() : "null"));
        }
        PushSender pushSender = notificationSenderManagementService.addPushSender(pushSenderAdd);
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(
                    V2_API_PATH_COMPONENT + NOTIFICATION_SENDER_CONTEXT_PATH + "/" + PUSH_PUBLISHER_TYPE + "/" +
                            URLEncoder.encode(pushSender.getName(), StandardCharsets.UTF_8.name())
                                    .replace(PLUS, URL_ENCODED_SPACE));
        } catch (UnsupportedEncodingException e) {
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withMessage("Error due to unsupported encoding.").build();
            throw new APIError(Response.Status.METHOD_NOT_ALLOWED, errorResponse);
        }
        return Response.created(location).entity(pushSender).build();
    }

    @Override
    public Response createSMSSender(SMSSenderAdd smSSenderAdd) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Creating SMS sender: %s", smSSenderAdd != null ? smSSenderAdd.getName() : "null"));
        }
        SMSSender smsSender = notificationSenderManagementService.addSMSSender(smSSenderAdd);
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(
                    V2_API_PATH_COMPONENT + NOTIFICATION_SENDER_CONTEXT_PATH + "/" + SMS_PUBLISHER_TYPE + "/" +
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

        if (log.isDebugEnabled()) {
            log.debug(String.format("Deleting email sender: %s", senderName));
        }
        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email sender deletion is not allowed for super tenant");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        notificationSenderManagementService.deleteNotificationSender(senderName);
        return Response.noContent().build();
    }

    @Override
    public Response deletePushSender(String senderName) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Deleting push sender: %s", senderName));
        }
        notificationSenderManagementService.deleteNotificationSender(senderName);
        return Response.noContent().build();
    }

    @Override
    public Response deleteSMSSender(String senderName) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Deleting SMS sender: %s", senderName));
        }
        notificationSenderManagementService.deleteNotificationSender(senderName);
        return Response.noContent().build();
    }

    @Override
    public Response getEmailSender(String senderName) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving email sender: %s", senderName));
        }
        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email sender retrieval is not allowed for super tenant");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok().entity(notificationSenderManagementService.getEmailSender(senderName)).build();
    }

    @Override
    public Response getEmailSenders() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all email senders");
        }
        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email senders retrieval is not allowed for super tenant");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok().entity(notificationSenderManagementService.getEmailSenders()).build();
    }

    @Override
    public Response getPushSender(String senderName) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving push sender: %s", senderName));
        }
        return Response.ok().entity(notificationSenderManagementService.getPushSender(senderName)).build();
    }

    @Override
    public Response getPushSenders() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all push senders");
        }
        return Response.ok().entity(notificationSenderManagementService.getPushSenders()).build();
    }

    @Override
    public Response getSMSSender(String senderName) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Retrieving SMS sender: %s", senderName));
        }
        return Response.ok().entity(notificationSenderManagementService.getSMSSender(senderName)).build();
    }

    @Override
    public Response getSMSSenders() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all SMS senders");
        }
        return Response.ok().entity(notificationSenderManagementService.getSMSSenders()).build();
    }

    @Override
    public Response updateEmailSender(String senderName, EmailSenderUpdateRequest emailSenderUpdateRequest) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Updating email sender: %s", senderName));
        }
        if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email sender update is not allowed for super tenant");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok()
                .entity(notificationSenderManagementService.updateEmailSender(senderName, emailSenderUpdateRequest))
                .build();
    }

    @Override
    public Response updatePushSender(String senderName, PushSenderUpdateRequest pushSenderUpdateRequest) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Updating push sender: %s", senderName));
        }
        return Response.ok()
                .entity(notificationSenderManagementService.updatePushSender(senderName, pushSenderUpdateRequest))
                .build();
    }

    @Override
    public Response updateSMSSender(String senderName, SMSSenderUpdateRequest smSSenderUpdateRequest) {

        if (log.isDebugEnabled()) {
            log.debug(String.format("Updating SMS sender: %s", senderName));
        }
        return Response.ok()
                .entity(notificationSenderManagementService.updateSMSSender(senderName, smSSenderUpdateRequest))
                .build();
    }
}
