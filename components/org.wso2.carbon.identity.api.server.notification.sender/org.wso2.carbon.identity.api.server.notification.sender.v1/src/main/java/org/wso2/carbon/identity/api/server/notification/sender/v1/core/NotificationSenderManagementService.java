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

package org.wso2.carbon.identity.api.server.notification.sender.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderServiceHolder;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.Properties;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.PushSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.PushSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.PushSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderUpdateRequest;
import org.wso2.carbon.identity.notification.sender.tenant.config.dto.EmailSenderDTO;
import org.wso2.carbon.identity.notification.sender.tenant.config.dto.PushSenderDTO;
import org.wso2.carbon.identity.notification.sender.tenant.config.dto.SMSSenderDTO;
import org.wso2.carbon.identity.notification.sender.tenant.config.exception.NotificationSenderManagementClientException;
import org.wso2.carbon.identity.notification.sender.tenant.config.exception.NotificationSenderManagementException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_CONFLICT_PUBLISHER;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_PUBLISHER_NOT_EXISTS;

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

        EmailSenderDTO dto = buildEmailSenderDTO(emailSenderAdd);
        try {
            EmailSenderDTO emailSenderDTO = notificationSenderManagementService.addEmailSender(dto, false);
            return buildEmailSenderFromDTO(emailSenderDTO);
        } catch (NotificationSenderManagementException e) {
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

        SMSSenderDTO dto = buildSMSSenderDTO(smsSenderAdd);
        try {
            SMSSenderDTO smsSenderDTO = notificationSenderManagementService.addSMSSender(dto);
            return buildSMSSenderFromDTO(smsSenderDTO);
        } catch (NotificationSenderManagementException e) {
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

        PushSenderDTO dto = buildPushSenderDTO(pushSenderAdd);
        try {
            PushSenderDTO pushSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .addPushSender(dto);
            return buildPushSenderFromDTO(pushSenderDTO);
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    /**
     * Delete a SMS/Email sender by name.
     *
     * @param notificationSenderName Name of the notification sender.
     */
    public void deleteNotificationSender(String notificationSenderName) {

        try {
            notificationSenderManagementService.deleteNotificationSender(notificationSenderName);
        } catch (NotificationSenderManagementException e) {
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

        EmailSenderDTO dto = buildEmailSenderDTO(senderName, emailSenderUpdateRequest);
        try {
            EmailSenderDTO emailSenderDTO = notificationSenderManagementService.updateEmailSender(dto, false);
            return buildEmailSenderFromDTO(emailSenderDTO);
        } catch (NotificationSenderManagementException e) {
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

        SMSSenderDTO dto = buildSMSSenderDTO(senderName, smsSenderUpdateRequest);
        try {
            SMSSenderDTO smsSenderDTO = notificationSenderManagementService.updateSMSSender(dto);
            return buildSMSSenderFromDTO(smsSenderDTO);
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    /**
     * Update push sender details by name.
     *
     * @param senderName               Push sender's name.
     * @param pushSenderUpdateRequest Push sender's updated configurations.
     * @return Updated push sender.
     */
    public PushSender updatePushSender(String senderName, PushSenderUpdateRequest pushSenderUpdateRequest) {

        PushSenderDTO dto = buildPushSenderDTO(senderName, pushSenderUpdateRequest);
        try {
            PushSenderDTO pushSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .updatePushSender(dto);
            return buildPushSenderFromDTO(pushSenderDTO);
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    private EmailSenderDTO buildEmailSenderDTO(EmailSenderAdd emailSenderAdd) {

        EmailSenderDTO dto = new EmailSenderDTO();
        dto.setName(emailSenderAdd.getName());
        dto.setFromAddress(emailSenderAdd.getFromAddress());
        dto.setUsername(emailSenderAdd.getUserName());
        dto.setPassword(emailSenderAdd.getPassword());
        dto.setSmtpPort(emailSenderAdd.getSmtpPort());
        dto.setSmtpServerHost(emailSenderAdd.getSmtpServerHost());
        List<Properties> properties = emailSenderAdd.getProperties();
        properties.forEach((prop) -> dto.getProperties().put(prop.getKey(), prop.getValue()));
        return dto;
    }


    private EmailSenderDTO buildEmailSenderDTO(String senderName, EmailSenderUpdateRequest emailSenderUpdateRequest) {

        EmailSenderDTO dto = new EmailSenderDTO();
        dto.setName(senderName);
        dto.setFromAddress(emailSenderUpdateRequest.getFromAddress());
        dto.setUsername(emailSenderUpdateRequest.getUserName());
        dto.setPassword(emailSenderUpdateRequest.getPassword());
        dto.setSmtpPort(emailSenderUpdateRequest.getSmtpPort());
        dto.setSmtpServerHost(emailSenderUpdateRequest.getSmtpServerHost());
        List<Properties> properties = emailSenderUpdateRequest.getProperties();
        properties.forEach((prop) -> dto.getProperties().put(prop.getKey(), prop.getValue()));
        return dto;
    }

    private EmailSender buildEmailSenderFromDTO(EmailSenderDTO dto) {

        EmailSender emailSender = new EmailSender();
        emailSender.setName(dto.getName());
        emailSender.setFromAddress(dto.getFromAddress());
        emailSender.setUserName(dto.getUsername());
        emailSender.setPassword(dto.getPassword());
        emailSender.setSmtpPort(dto.getSmtpPort());
        emailSender.setSmtpServerHost(dto.getSmtpServerHost());
        List<Properties> properties = new ArrayList<>();
        dto.getProperties().forEach((key, value) -> {
            Properties prop = new Properties();
            prop.setKey(key);
            prop.setValue(value);
            properties.add(prop);
        });
        emailSender.setProperties(properties);
        return emailSender;
    }

    private SMSSenderDTO buildSMSSenderDTO(SMSSenderAdd smsSenderAdd) {

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

        PushSenderDTO dto = new PushSenderDTO();
        dto.setName(pushSenderAdd.getName());
        dto.setProvider(pushSenderAdd.getProvider());
        List<Properties> properties = pushSenderAdd.getProperties();
        properties.forEach((prop) -> dto.getProperties().put(prop.getKey(), prop.getValue()));
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
