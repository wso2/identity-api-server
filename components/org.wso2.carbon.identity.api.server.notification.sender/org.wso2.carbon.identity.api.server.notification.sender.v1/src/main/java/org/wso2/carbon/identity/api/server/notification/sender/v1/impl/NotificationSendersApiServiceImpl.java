/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.notification.sender.v1.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.notification.sender.v1.NotificationSendersApiService;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.NotificationSenderManagementService;
import org.wso2.carbon.identity.api.server.notification.sender.v1.factories.NotificationSenderManagementServiceFactory;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.PushSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.PushSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.PushSenderUpdateRequest;
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
                log.debug("NotificationSenderManagementService initialized successfully.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating notification sender service.", e);
            throw new RuntimeException("Error occurred while initiating notification sender service.", e);
        }
    }

    @Override
    public Response createEmailSender(EmailSenderAdd emailSenderAdd) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Creating email sender: " + (emailSenderAdd != null ? emailSenderAdd.getName() : "null") + 
                     " for tenant: " + tenantDomain);
        }
        
        if (StringUtils.equals(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email sender creation not allowed for super tenant domain.");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        EmailSender emailSender = notificationSenderManagementService.addEmailSender(emailSenderAdd);
        log.info("Email sender created successfully: " + emailSender.getName() + " for tenant: " + tenantDomain);
        
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(
                    V1_API_PATH_COMPONENT + NOTIFICATION_SENDER_CONTEXT_PATH + "/" + EMAIL_PUBLISHER_TYPE + "/" +
                            URLEncoder.encode(emailSender.getName(), StandardCharsets.UTF_8.name())
                                    .replace(PLUS, URL_ENCODED_SPACE));
        } catch (UnsupportedEncodingException e) {
            log.error("Error occurred while encoding email sender name: " + emailSender.getName(), e);
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withMessage("Error due to unsupported encoding.").build();
            throw new APIError(Response.Status.METHOD_NOT_ALLOWED, errorResponse);
        }
        return Response.created(location).entity(emailSender).build();
    }

    @Override
    public Response createPushSender(PushSenderAdd pushSenderAdd) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Creating push sender: " + (pushSenderAdd != null ? pushSenderAdd.getName() : "null") + 
                     " for tenant: " + tenantDomain);
        }
        
        if (pushSenderAdd == null) {
            log.error("Push sender add request cannot be null for tenant: " + tenantDomain);
            ErrorResponse errorResponse = 
                    new ErrorResponse.Builder().withMessage("Push sender add request cannot be null.").build();
            throw new APIError(Response.Status.BAD_REQUEST, errorResponse);
        }
        
        PushSender pushSender = notificationSenderManagementService.addPushSender(pushSenderAdd);
        log.info("Push sender created successfully: " + pushSender.getName() + " for tenant: " + tenantDomain);
        
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(
                    V1_API_PATH_COMPONENT + NOTIFICATION_SENDER_CONTEXT_PATH + "/" + PUSH_PUBLISHER_TYPE + "/" +
                            URLEncoder.encode(pushSender.getName(), StandardCharsets.UTF_8.name())
                                    .replace(PLUS, URL_ENCODED_SPACE));
        } catch (UnsupportedEncodingException e) {
            log.error("Error occurred while encoding push sender name: " + pushSender.getName(), e);
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withMessage("Error due to unsupported encoding.").build();
            throw new APIError(Response.Status.METHOD_NOT_ALLOWED, errorResponse);
        }
        return Response.created(location).entity(pushSender).build();
    }

    @Override
    public Response createSMSSender(SMSSenderAdd smSSenderAdd) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Creating SMS sender: " + (smSSenderAdd != null ? smSSenderAdd.getName() : "null") + 
                     " for tenant: " + tenantDomain);
        }
        
        if (smSSenderAdd == null) {
            log.error("SMS sender add request cannot be null for tenant: " + tenantDomain);
            ErrorResponse errorResponse = 
                    new ErrorResponse.Builder().withMessage("SMS sender add request cannot be null.").build();
            throw new APIError(Response.Status.BAD_REQUEST, errorResponse);
        }
        
        SMSSender smsSender = notificationSenderManagementService.addSMSSender(smSSenderAdd);
        log.info("SMS sender created successfully: " + smsSender.getName() + " for tenant: " + tenantDomain);
        
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(
                    V1_API_PATH_COMPONENT + NOTIFICATION_SENDER_CONTEXT_PATH + "/" + SMS_PUBLISHER_TYPE + "/" +
                            URLEncoder.encode(smsSender.getName(), StandardCharsets.UTF_8.name())
                                    .replace(PLUS, URL_ENCODED_SPACE));
        } catch (UnsupportedEncodingException e) {
            log.error("Error occurred while encoding SMS sender name: " + smsSender.getName(), e);
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withMessage("Error due to unsupported encoding.").build();
            throw new APIError(Response.Status.METHOD_NOT_ALLOWED, errorResponse);
        }
        return Response.created(location).entity(smsSender).build();
    }

    @Override
    public Response deleteEmailSender(String senderName) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Deleting email sender: " + senderName + " for tenant: " + tenantDomain);
        }
        
        if (StringUtils.equals(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email sender deletion not allowed for super tenant domain.");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        
        notificationSenderManagementService.deleteNotificationSender(senderName);
        log.info("Email sender deleted successfully: " + senderName + " for tenant: " + tenantDomain);
        return Response.noContent().build();
    }

    @Override
    public Response deletePushSender(String senderName) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Deleting push sender: " + senderName + " for tenant: " + tenantDomain);
        }
        
        notificationSenderManagementService.deleteNotificationSender(senderName);
        log.info("Push sender deleted successfully: " + senderName + " for tenant: " + tenantDomain);
        return Response.noContent().build();
    }

    @Override
    public Response deleteSMSSender(String senderName) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Deleting SMS sender: " + senderName + " for tenant: " + tenantDomain);
        }
        
        notificationSenderManagementService.deleteNotificationSender(senderName);
        log.info("SMS sender deleted successfully: " + senderName + " for tenant: " + tenantDomain);
        return Response.noContent().build();
    }

    @Override
    public Response getEmailSender(String senderName) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Retrieving email sender: " + senderName + " for tenant: " + tenantDomain);
        }
        
        if (StringUtils.equals(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email sender retrieval not allowed for super tenant domain.");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok().entity(notificationSenderManagementService.getEmailSender(senderName)).build();
    }

    @Override
    public Response getEmailSenders() {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Retrieving all email senders for tenant: " + tenantDomain);
        }
        
        if (StringUtils.equals(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email senders retrieval not allowed for super tenant domain.");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        return Response.ok().entity(notificationSenderManagementService.getEmailSenders()).build();
    }

    @Override
    public Response getPushSender(String senderName) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving push sender: " + senderName + " for tenant: " + getTenantDomainFromContext());
        }
        return Response.ok().entity(notificationSenderManagementService.getPushSender(senderName)).build();
    }

    @Override
    public Response getPushSenders() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all push senders for tenant: " + getTenantDomainFromContext());
        }
        return Response.ok().entity(notificationSenderManagementService.getPushSenders()).build();
    }

    @Override
    public Response getSMSSender(String senderName) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving SMS sender: " + senderName + " for tenant: " + getTenantDomainFromContext());
        }
        return Response.ok().entity(notificationSenderManagementService.getSMSSender(senderName)).build();
    }

    @Override
    public Response getSMSSenders() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all SMS senders for tenant: " + getTenantDomainFromContext());
        }
        return Response.ok().entity(notificationSenderManagementService.getSMSSenders()).build();
    }

    @Override
    public Response updateEmailSender(String senderName, EmailSenderUpdateRequest emailSenderUpdateRequest) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Updating email sender: " + senderName + " for tenant: " + tenantDomain);
        }
        
        if (StringUtils.equals(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            log.warn("Email sender update not allowed for super tenant domain.");
            return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
        }
        
        EmailSender updatedEmailSender = notificationSenderManagementService.updateEmailSender(senderName, 
                emailSenderUpdateRequest);
        log.info("Email sender updated successfully: " + senderName + " for tenant: " + tenantDomain);
        return Response.ok().entity(updatedEmailSender).build();
    }

    @Override
    public Response updatePushSender(String senderName, PushSenderUpdateRequest pushSenderUpdateRequest) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Updating push sender: " + senderName + " for tenant: " + tenantDomain);
        }
        
        PushSender updatedPushSender = notificationSenderManagementService.updatePushSender(senderName, 
                pushSenderUpdateRequest);
        log.info("Push sender updated successfully: " + senderName + " for tenant: " + tenantDomain);
        return Response.ok().entity(updatedPushSender).build();
    }

    @Override
    public Response updateSMSSender(String senderName, SMSSenderUpdateRequest smSSenderUpdateRequest) {

        String tenantDomain = getTenantDomainFromContext();
        if (log.isDebugEnabled()) {
            log.debug("Updating SMS sender: " + senderName + " for tenant: " + tenantDomain);
        }
        
        SMSSender updatedSMSSender = notificationSenderManagementService.updateSMSSender(senderName, 
                smSSenderUpdateRequest);
        log.info("SMS sender updated successfully: " + senderName + " for tenant: " + tenantDomain);
        return Response.ok().entity(updatedSMSSender).build();
    }
}
