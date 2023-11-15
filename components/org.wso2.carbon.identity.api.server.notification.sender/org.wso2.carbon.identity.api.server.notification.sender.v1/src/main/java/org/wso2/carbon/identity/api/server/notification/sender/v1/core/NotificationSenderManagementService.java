/*
 * Copyright (c) 2023, WSO2 Inc. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.notification.sender.v1.core;

import org.apache.axiom.om.OMElement;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderServiceHolder;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.exception.OutputEventAdapterException;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.EventAdapterConstants;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.config.AdapterConfigs;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.config.Property;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.Properties;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderUpdateRequest;
import org.wso2.carbon.identity.notification.sender.tenant.config.dto.EmailSenderDTO;
import org.wso2.carbon.identity.notification.sender.tenant.config.dto.SMSSenderDTO;
import org.wso2.carbon.identity.notification.sender.tenant.config.exception.NotificationSenderManagementClientException;
import org.wso2.carbon.identity.notification.sender.tenant.config.exception.NotificationSenderManagementException;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import static org.wso2.carbon.identity.api.server.common.ContextLoader.getTenantDomainFromContext;
import static org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.util.EventAdapterConfigHelper.convertToOmElement;
import static org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.util.EventAdapterConfigHelper.secureResolveOmElement;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_CONFLICT_PUBLISHER;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND;
import static org.wso2.carbon.identity.notification.sender.tenant.config.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_PUBLISHER_NOT_EXISTS;

/**
 * Invoke internal OSGi service to perform notification sender management operations.
 */
public class NotificationSenderManagementService {

    private static final Log log = LogFactory.getLog(NotificationSenderManagementService.class);

    /**
     * Create an email sender resource with a resource file.
     *
     * @param emailSenderAdd Email sender post request.
     * @return Email sender.
     */
    public EmailSender addEmailSender(EmailSenderAdd emailSenderAdd) {

        EmailSenderDTO dto = buildEmailSenderDTO(emailSenderAdd);
        try {
            EmailSenderDTO emailSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .addEmailSender(dto);
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
            SMSSenderDTO smsSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .addSMSSender(dto);
            return buildSMSSenderFromDTO(smsSenderDTO);
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
            NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .deleteNotificationSender(notificationSenderName);
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
            EmailSenderDTO emailSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .getEmailSender(senderName);
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
            SMSSenderDTO smsSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .getSMSSender(senderName);
            return buildSMSSenderFromDTO(smsSenderDTO);
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

        List<EmailSenderDTO> emailSenders;
        try {
            if (StringUtils.equals(getTenantDomainFromContext(), MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
                emailSenders = getSuperTenantEmailSender();
            } else {
                emailSenders = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                                .getEmailSenders();
            }
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
            List<SMSSenderDTO> smsSenders = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .getSMSSenders();
            return smsSenders.stream().map(this::buildSMSSenderFromDTO).collect(Collectors.toList());
        } catch (NotificationSenderManagementException e) {
            throw handleException(e);
        }
    }

    public static List<EmailSenderDTO> getSuperTenantEmailSender() {

        String path = CarbonUtils.getCarbonConfigDirPath() + File.separator
                + EventAdapterConstants.GLOBAL_CONFIG_FILE_NAME;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AdapterConfigs.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            File configFile = new File(path);
            if (!configFile.exists()) {
                log.warn(EventAdapterConstants.GLOBAL_CONFIG_FILE_NAME + " can not found in " + path + "," +
                        " hence Output Event Adapters will be running with default global configs.");
            }
            OMElement globalConfigDoc = convertToOmElement(configFile);
            secureResolveOmElement(globalConfigDoc);
            AdapterConfigs adapterConfigs = (AdapterConfigs) unmarshaller.unmarshal(globalConfigDoc
                    .getXMLStreamReader());
            ArrayList<Property> properties =
                    (ArrayList<Property>) adapterConfigs.getAdapterConfig("email").getGlobalProperties();

            List<EmailSenderDTO> emailSenderDTOList = new ArrayList<>();
            emailSenderDTOList.add(getEmailSenderDTO(properties));
            return emailSenderDTOList;
        } catch (JAXBException e) {
            log.error("Error in loading " + EventAdapterConstants.GLOBAL_CONFIG_FILE_NAME + " from " + path + "," +
                    " hence Output Event Adapters will be running with default global configs.");
        } catch (OutputEventAdapterException e) {
            log.error("Error in converting " + EventAdapterConstants.GLOBAL_CONFIG_FILE_NAME + " to parsed document," +
                    " hence Output Event Adapters will be running with default global configs.");
        }
        return new ArrayList<>();
    }

    private static EmailSenderDTO getEmailSenderDTO(ArrayList<Property> properties) {

        EmailSenderDTO emailSenderDTO = new EmailSenderDTO();
        emailSenderDTO.setName("EmailPublisher");
        emailSenderDTO.setFromAddress(getValueFromKey("mail.smtp.from", properties));
        emailSenderDTO.setUsername(getValueFromKey("mail.smtp.user", properties));
        emailSenderDTO.setPassword(getValueFromKey("mail.smtp.password", properties));
        emailSenderDTO.setSmtpPort(Integer.parseInt(getValueFromKey("mail.smtp.port", properties)));
        emailSenderDTO.setSmtpServerHost(getValueFromKey("mail.smtp.host", properties));
        Map<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("mail.smtp.signature", getValueFromKey("mail.smtp.signature", properties));
        propertiesMap.put("mail.smtp.replyTo", getValueFromKey("mail.smtp.replyTo", properties));
        emailSenderDTO.setProperties(propertiesMap);

        return emailSenderDTO;
    }

    private static String getValueFromKey(String key, ArrayList<Property> list) {
        for (Property property : list) {
            if (property.getKey().equals(key)) {
                return property.getValue();
            }
        }
        return null;
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
            EmailSenderDTO emailSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .updateEmailSender(dto);
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
            SMSSenderDTO smsSenderDTO = NotificationSenderServiceHolder.getNotificationSenderManagementService()
                    .updateSMSSender(dto);
            return buildSMSSenderFromDTO(smsSenderDTO);
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
