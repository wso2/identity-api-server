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

package org.wso2.carbon.identity.api.server.notification.sender.v2.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderServiceHolder;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.Properties;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.PushSender;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.PushSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.PushSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.SMSSender;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.SMSSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v2.model.SMSSenderUpdateRequest;
import org.wso2.carbon.identity.notification.sender.tenant.config.dto.EmailSenderDTO;
import org.wso2.carbon.identity.notification.sender.tenant.config.dto.PushSenderDTO;
import org.wso2.carbon.identity.notification.sender.tenant.config.dto.SMSSenderDTO;
import org.wso2.carbon.identity.notification.sender.tenant.config.exception.NotificationSenderManagementClientException;
import org.wso2.carbon.identity.notification.sender.tenant.config.exception.NotificationSenderManagementException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.BASIC;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.CLIENT_SECRET;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_CONFLICT_PUBLISHER;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_PUBLISHER_NOT_EXISTS;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.PASSWORD;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.USERNAME;

/**
 * Invoke internal OSGi service to perform notification sender management operations.
 */
public class NotificationSenderManagementService {

    private final org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementService
            notificationSenderManagementService;
    private static final Log log = LogFactory.getLog(NotificationSenderManagementService.class);

    public NotificationSenderManagementService(
            org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementService
                    notificationSenderManagementService) {

        this.notificationSenderManagementService = notificationSenderManagementService;
    }

    /**
     * Create an email sender resource with a resource file.
     *
     * @param emailSenderAdd Email sender post request.
     * @return Email sender.
     */
    public EmailSender addEmailSender(EmailSenderAdd emailSenderAdd) {

        if (emailSenderAdd == null) {
            log.error("Email sender add request is null");
            throw new IllegalArgumentException("Email sender add request cannot be null");
        }
        if (log.isDebugEnabled()) {
            log.debug("Adding email sender: " + emailSenderAdd.getName());
        }
        EmailSenderDTO dto = buildEmailSenderDTO(emailSenderAdd);
        try {
            EmailSenderDTO emailSenderDTO = notificationSenderManagementService.addEmailSender(dto);
            if (emailSenderDTO == null) {
                log.warn("Received null email sender DTO from service");
                throw new IllegalStateException("Failed to create email sender");
            }
            log.info("Email sender added successfully: " + emailSenderDTO.getName());
            return buildEmailSenderFromDTO(emailSenderDTO);
        } catch (NotificationSenderManagementException e) {
            log.error("Failed to add email sender: " + emailSenderAdd.getName() + ". Error: " + e.getMessage());
            throw handleException(e);
        }
    }

    /**
     * Create a sms sender resource with a resource file.
     *
     * @param smsSenderAdd SMS sender post request.
     * @return SMS sender.
     */
    public SMSSender addSMSSender(SMSSenderAdd smsSenderAdd) {

        if (smsSenderAdd == null) {
            log.error("SMS sender add request is null");
            throw new IllegalArgumentException("SMS sender add request cannot be null");
        }
        if (log.isDebugEnabled()) {
            log.debug("Adding SMS sender: " + smsSenderAdd.getName());
        }
        SMSSenderDTO dto = buildSMSSenderDTO(smsSenderAdd);
        try {
            SMSSenderDTO smsSenderDTO = notificationSenderManagementService.addSMSSender(dto);
            if (smsSenderDTO == null) {
                log.warn("Received null SMS sender DTO from service");
                throw new IllegalStateException("Failed to create SMS sender");
            }
            log.info("SMS sender added successfully: " + smsSenderDTO.getName());
            return buildSMSSenderFromDTO(smsSenderDTO);
        } catch (NotificationSenderManagementException e) {
            log.error("Failed to add SMS sender: " + smsSenderAdd.getName() + ". Error: " + e.getMessage());
            throw handleException(e);
        }
    }

    /**
     * Create a push sender resource with a resource file.
     *
     * @param pushSenderAdd Push sender post request.
     * @return Push sender.
     */
    public PushSender addPushSender(PushSenderAdd pushSenderAdd) {

        if (pushSenderAdd == null) {
            log.error("Push sender add request is null");
            throw new IllegalArgumentException("Push sender add request cannot be null");
        }
        if (log.isDebugEnabled()) {
            log.debug("Adding push sender: " + pushSenderAdd.getName());
        }
        PushSenderDTO dto = buildPushSenderDTO(pushSenderAdd);
        try {
            PushSenderDTO pushSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .addPushSender(dto);
            if (pushSenderDTO == null) {
                log.warn("Received null push sender DTO from service");
                throw new IllegalStateException("Failed to create push sender");
            }
            log.info("Push sender added successfully: " + pushSenderDTO.getName());
            return buildPushSenderFromDTO(pushSenderDTO);
        } catch (NotificationSenderManagementException e) {
            log.error("Failed to add push sender: " + pushSenderAdd.getName() + ". Error: " + e.getMessage());
            throw handleException(e);
        }
    }

    /**
     * Delete a SMS/Email sender by name.
     *
     * @param notificationSenderName Name of the notification sender.
     */
    public void deleteNotificationSender(String notificationSenderName) {

        if (log.isDebugEnabled()) {
            log.debug("Deleting notification sender: " + notificationSenderName);
        }
        try {
            notificationSenderManagementService.deleteNotificationSender(notificationSenderName);
            log.info("Notification sender deleted successfully: " + notificationSenderName);
        } catch (NotificationSenderManagementException e) {
            log.error("Failed to delete notification sender: " + notificationSenderName + ". Error: " + 
                      e.getMessage());
            throw handleException(e);
        }
    }

    /**
     * Retrieve the email sender details by name.
     *
     * @param senderName Email sender's name.
     * @return Email sender.
     */
    public EmailSender getEmailSender(String senderName) {

        try {
            EmailSenderDTO emailSenderDTO = notificationSenderManagementService.getEmailSender(senderName);
            return buildEmailSenderFromDTO(emailSenderDTO);
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    /**
     * Retrieve the sms sender details by name.
     *
     * @param senderName SMS sender's name.
     * @return SMS sender.
     */
    public SMSSender getSMSSender(String senderName) {

        try {
            SMSSenderDTO smsSenderDTO = notificationSenderManagementService.getSMSSender(senderName, false);
            return buildSMSSenderFromDTO(smsSenderDTO);
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    /**
     * Retrieve the push sender details by name.
     *
     * @param senderName Push sender's name.
     * @return Push sender.
     */
    public PushSender getPushSender(String senderName) {

        try {
            PushSenderDTO pushSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .getPushSender(senderName, false);
            return buildPushSenderFromDTO(pushSenderDTO);
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    /**
     * Retrieve all email senders of the tenant.
     *
     * @return Email senders of the tenant.
     */
    public List<EmailSender> getEmailSenders() {

        try {
            List<EmailSenderDTO> emailSenders = notificationSenderManagementService.getEmailSenders();
            return emailSenders.stream().map(this::buildEmailSenderFromDTO).collect(Collectors.toList());
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    /**
     * Retrieve all sms senders of the tenant.
     *
     * @return SMS senders of the tenant.
     */
    public List<SMSSender> getSMSSenders() {

        try {
            List<SMSSenderDTO> smsSenders = notificationSenderManagementService.getSMSSenders(false);
            return smsSenders.stream().map(this::buildSMSSenderFromDTO).collect(Collectors.toList());
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    /**
     * Retrieve all push senders of the tenant.
     *
     * @return Push senders of the tenant.
     */
    public List<PushSender> getPushSenders() {

        try {
            List<PushSenderDTO> pushSenders = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .getPushSenders(false);
            return pushSenders.stream().map(this::buildPushSenderFromDTO).collect(Collectors.toList());
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    /**
     * Update email sender details by name.
     *
     * @param senderName               Email sender's name.
     * @param emailSenderUpdateRequest Email sender's updated configurations.
     * @return Updated email sender.
     */
    public EmailSender updateEmailSender(String senderName, EmailSenderUpdateRequest emailSenderUpdateRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating email sender: " + senderName);
        }
        EmailSenderDTO dto = buildEmailSenderDTO(senderName, emailSenderUpdateRequest);
        try {
            EmailSenderDTO emailSenderDTO = notificationSenderManagementService.updateEmailSender(dto);
            log.info("Email sender updated successfully: " + senderName);
            return buildEmailSenderFromDTO(emailSenderDTO);
        } catch (NotificationSenderManagementException e) {
            log.error("Failed to update email sender: " + senderName + ". Error: " + e.getMessage());
            throw handleException(e);
        }
    }

    /**
     * Update sms sender details by name.
     *
     * @param senderName             SMS sender' name.
     * @param smsSenderUpdateRequest SMS sender's updated configurations.
     * @return Updated SMS sender.
     */
    public SMSSender updateSMSSender(String senderName, SMSSenderUpdateRequest smsSenderUpdateRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating SMS sender: " + senderName);
        }
        SMSSenderDTO dto = buildSMSSenderDTO(senderName, smsSenderUpdateRequest);
        try {
            SMSSenderDTO smsSenderDTO = notificationSenderManagementService.updateSMSSender(dto);
            log.info("SMS sender updated successfully: " + senderName);
            return buildSMSSenderFromDTO(smsSenderDTO);
        } catch (NotificationSenderManagementException e) {
            log.error("Failed to update SMS sender: " + senderName + ". Error: " + e.getMessage());
            throw handleException(e);
        }
    }

    /**
     * Update push sender details by name.
     *
     * @param senderName              Push sender's name.
     * @param pushSenderUpdateRequest Push sender's updated configurations.
     * @return Updated push sender.
     */
    public PushSender updatePushSender(String senderName, PushSenderUpdateRequest pushSenderUpdateRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating push sender: " + senderName);
        }
        PushSenderDTO dto = buildPushSenderDTO(senderName, pushSenderUpdateRequest);
        try {
            PushSenderDTO pushSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .updatePushSender(dto);
            log.info("Push sender updated successfully: " + senderName);
            return buildPushSenderFromDTO(pushSenderDTO);
        } catch (NotificationSenderManagementException e) {
            log.error("Failed to update push sender: " + senderName + ". Error: " + e.getMessage());
            throw handleException(e);
        }
    }

    private EmailSenderDTO buildEmailSenderDTO(EmailSenderAdd emailSenderAdd) {

        if (emailSenderAdd == null) {
            throw new IllegalArgumentException("EmailSenderAdd cannot be null");
        }
        EmailSenderDTO dto = new EmailSenderDTO();
        dto.setName(emailSenderAdd.getName());
        dto.setFromAddress(emailSenderAdd.getFromAddress());
        dto.setSmtpPort(emailSenderAdd.getSmtpPort());
        dto.setAuthType(emailSenderAdd.getAuthType());
        dto.setSmtpServerHost(emailSenderAdd.getSmtpServerHost());
        List<Properties> properties = emailSenderAdd.getProperties();
        if (properties == null) {
            return dto;
        }
        properties.forEach((prop) -> {
            if (StringUtils.isNotBlank(prop.getKey()) && StringUtils.isNotBlank(prop.getValue())) {
                dto.getProperties().put(prop.getKey(), prop.getValue());
            }
        });
        return dto;
    }


    private EmailSenderDTO buildEmailSenderDTO(String senderName, EmailSenderUpdateRequest emailSenderUpdateRequest) {

        EmailSenderDTO dto = new EmailSenderDTO();
        dto.setName(senderName);
        dto.setFromAddress(emailSenderUpdateRequest.getFromAddress());
        dto.setSmtpPort(emailSenderUpdateRequest.getSmtpPort());
        dto.setSmtpServerHost(emailSenderUpdateRequest.getSmtpServerHost());
        dto.setAuthType(emailSenderUpdateRequest.getAuthType());
        List<Properties> properties = emailSenderUpdateRequest.getProperties();
        if (properties == null) {
            return dto;
        }
        properties.forEach((prop) -> {
            if (StringUtils.isNotBlank(prop.getKey()) && StringUtils.isNotBlank(prop.getValue())) {
                dto.getProperties().put(prop.getKey(), prop.getValue());
            }
        });
        return dto;
    }

    private EmailSender buildEmailSenderFromDTO(EmailSenderDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("EmailSenderDTO cannot be null");
        }
        EmailSender emailSender = new EmailSender();
        emailSender.setName(dto.getName());
        emailSender.setFromAddress(dto.getFromAddress());
        emailSender.setSmtpPort(dto.getSmtpPort());
        emailSender.setSmtpServerHost(dto.getSmtpServerHost());
        emailSender.setAuthType(StringUtils.isBlank(dto.getAuthType()) ? BASIC : dto.getAuthType());
        List<Properties> properties = new ArrayList<>();

        // Exclude credentials from the response.
        Set<String> excludedKeys = new HashSet<>(Arrays.asList(PASSWORD, CLIENT_SECRET));

        if (dto.getProperties() == null) {
            return emailSender;
        }
        dto.getProperties().forEach((key, value) -> {
            if (excludedKeys.contains(key) || StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
                return;
            }
            Properties prop = new Properties();
            prop.setKey(key);
            prop.setValue(value);
            properties.add(prop);
        });
        /* In case where the email provider is configured via V1 API, the username is not set in the properties, but as
        a first class attribute. As V2 doesn't support credentials as first class attributes username needs to be
        set as a property. */
        if (StringUtils.isNotBlank(dto.getUsername())) {
            Properties prop = new Properties();
            prop.setKey(USERNAME);
            prop.setValue(dto.getUsername());
            properties.add(prop);
        }
        emailSender.setProperties(properties);
        return emailSender;
    }

    private SMSSenderDTO buildSMSSenderDTO(SMSSenderAdd smsSenderAdd) {

        if (smsSenderAdd == null) {
            throw new IllegalArgumentException("SMSSenderAdd cannot be null");
        }
        SMSSenderDTO dto = new SMSSenderDTO();
        dto.setName(smsSenderAdd.getName());
        dto.setProvider(smsSenderAdd.getProvider());
        dto.setProviderURL(smsSenderAdd.getProviderURL());
        dto.setKey(smsSenderAdd.getKey());
        dto.setSecret(smsSenderAdd.getSecret());
        dto.setSender(smsSenderAdd.getSender());
        dto.setContentType(smsSenderAdd.getContentType().toString());
        List<Properties> properties = smsSenderAdd.getProperties();
        if (properties != null) {
            properties.forEach((prop) -> dto.getProperties().put(prop.getKey(), prop.getValue()));
        }
        return dto;
    }


    private SMSSenderDTO buildSMSSenderDTO(String senderName, SMSSenderUpdateRequest smsSenderUpdateRequest) {

        SMSSenderDTO dto = new SMSSenderDTO();
        dto.setName(senderName);
        dto.setProvider(smsSenderUpdateRequest.getProvider());
        dto.setProviderURL(smsSenderUpdateRequest.getProviderURL());
        dto.setKey(smsSenderUpdateRequest.getKey());
        dto.setSecret(smsSenderUpdateRequest.getSecret());
        dto.setSender(smsSenderUpdateRequest.getSender());
        dto.setContentType(smsSenderUpdateRequest.getContentType().toString());
        List<Properties> properties = smsSenderUpdateRequest.getProperties();
        if (properties != null) {
            properties.forEach((prop) -> dto.getProperties().put(prop.getKey(), prop.getValue()));
        }
        return dto;
    }

    private APIError handleException(NotificationSenderManagementException e) {

        if (e instanceof NotificationSenderManagementClientException) {
            throw buildClientError(e);
        }
        throw buildServerError(e);
    }

    private SMSSender buildSMSSenderFromDTO(SMSSenderDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("SMSSenderDTO cannot be null");
        }
        SMSSender smsSender = new SMSSender();
        smsSender.setName(dto.getName());
        smsSender.setProvider(dto.getProvider());
        smsSender.setProviderURL(dto.getProviderURL());
        smsSender.setKey(dto.getKey());
        smsSender.setSecret(dto.getSecret());
        smsSender.setSender(dto.getSender());
        smsSender.setContentType(SMSSender.ContentTypeEnum.valueOf(dto.getContentType()));
        List<Properties> properties = new ArrayList<>();
        dto.getProperties().forEach((key, value) -> {
            Properties prop = new Properties();
            prop.setKey(key);
            prop.setValue(value);
            properties.add(prop);
        });
        smsSender.setProperties(properties);
        return smsSender;
    }

    private PushSenderDTO buildPushSenderDTO(PushSenderAdd pushSenderAdd) {

        if (pushSenderAdd == null) {
            throw new IllegalArgumentException("PushSenderAdd cannot be null");
        }
        PushSenderDTO dto = new PushSenderDTO();
        dto.setName(pushSenderAdd.getName());
        dto.setProvider(pushSenderAdd.getProvider());
        List<Properties> properties = pushSenderAdd.getProperties();
        if (properties != null) {
            properties.forEach((prop) -> {
                if (prop != null && prop.getKey() != null && prop.getValue() != null) {
                    dto.getProperties().put(prop.getKey(), prop.getValue());
                }
            });
        }
        return dto;
    }

    private PushSenderDTO buildPushSenderDTO(String senderName, PushSenderUpdateRequest pushSenderUpdateRequest) {

        PushSenderDTO dto = new PushSenderDTO();
        dto.setName(senderName);
        dto.setProvider(pushSenderUpdateRequest.getProvider());
        List<Properties> properties = pushSenderUpdateRequest.getProperties();
        properties.forEach((prop) -> dto.getProperties().put(prop.getKey(), prop.getValue()));
        return dto;
    }

    private PushSender buildPushSenderFromDTO(PushSenderDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("PushSenderDTO cannot be null");
        }
        PushSender pushSender = new PushSender();
        pushSender.setName(dto.getName());
        pushSender.setProvider(dto.getProvider());
        List<Properties> properties = new ArrayList<>();
        dto.getProperties().forEach((key, value) -> {
            Properties prop = new Properties();
            prop.setKey(key);
            prop.setValue(value);
            properties.add(prop);
        });
        pushSender.setProperties(properties);
        return pushSender;
    }

    private APIError buildServerError(NotificationSenderManagementException e) {

        String errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorCode)
                .withMessage(e.getMessage())
                .withDescription(e.getDescription())
                .build(log, e, e.getMessage());

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }

    private APIError buildClientError(NotificationSenderManagementException e) {

        String errorCode = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(e.getErrorCode())
                .withMessage(e.getMessage())
                .withDescription(e.getDescription())
                .build(log, e.getMessage());

        Response.Status status = Response.Status.BAD_REQUEST;
        if (ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND.getCode().equals(errorCode) ||
                ERROR_CODE_PUBLISHER_NOT_EXISTS.getCode().equals(errorCode)) {
            status = Response.Status.NOT_FOUND;
        } else if (ERROR_CODE_CONFLICT_PUBLISHER.getCode().equals(errorCode)) {
            status = Response.Status.CONFLICT;
        }
        return new APIError(status, errorResponse);
    }
}
