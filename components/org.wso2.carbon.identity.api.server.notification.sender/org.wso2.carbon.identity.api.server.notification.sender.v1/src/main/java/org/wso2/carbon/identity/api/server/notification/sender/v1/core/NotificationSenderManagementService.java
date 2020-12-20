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

package org.wso2.carbon.identity.api.server.notification.sender.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.email.mgt.SMSProviderPayloadTemplateManager;
import org.wso2.carbon.email.mgt.model.SMSProviderTemplate;
import org.wso2.carbon.event.publisher.core.EventPublisherService;
import org.wso2.carbon.event.publisher.core.config.EventPublisherConfiguration;
import org.wso2.carbon.event.publisher.core.exception.EventPublisherConfigurationException;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants;
import org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderServiceHolder;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderUpdateRequest;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.Properties;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSender;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderUpdateRequest;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementClientException;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementServerException;
import org.wso2.carbon.identity.configuration.mgt.core.model.Attribute;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resource;
import org.wso2.carbon.identity.configuration.mgt.core.model.ResourceAdd;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resources;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.DEFAULT_EMAIL_PUBLISHER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.DEFAULT_SMS_PUBLISHER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_PUBLISHER_TYPE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_CONFLICT_PUBLISHER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_NOTIFICATION_SENDER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_ERROR_DELETING_NOTIFICATION_SENDER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDERS_BY_TYPE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_NOTIFICATION_SENDER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_PARSER_CONFIG_EXCEPTION;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_PUBLISHER_NOT_EXISTS_IN_SUPER_TENANT;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_SERVER_ERRORS_GETTING_EVENT_PUBLISHER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_SMS_PAYLOAD_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_SMS_PROVIDER_REQUIRED;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_SMS_PROVIDER_URL_REQUIRED;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_TRANSFORMER_EXCEPTION;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.FROM_ADDRESS;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.NOTIFICATION_SENDER_ERROR_PREFIX;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PASSWORD;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PROVIDER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PROVIDER_URL;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PUBLISHER_RESOURCE_TYPE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PUBLISHER_TYPE_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.RESOURCE_NOT_EXISTS_ERROR_CODE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SECRET;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SENDER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMS_PUBLISHER_TYPE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMS_SEND_API_BODY_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_PORT;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_SERVER_HOST;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.STREAM_NAME;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.STREAM_VERSION;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.USERNAME;
import static org.wso2.carbon.identity.api.server.notification.sender.v1.core.utils.NotificationSenderUtils.generateEmailPublisher;
import static org.wso2.carbon.identity.api.server.notification.sender.v1.core.utils.NotificationSenderUtils.generateSMSPublisher;

/**
 * Invoke internal OSGi service to perform notification sender management operations.
 */
public class NotificationSenderManagementService {

    private static final Log log = LogFactory.getLog(NotificationSenderManagementService.class);

    public EmailSender addEmailSender(EmailSenderAdd emailSenderAdd) {

        Integer tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        EventPublisherConfiguration publisherInSuperTenant =
                validateEmailSenderAddAndGetPublisherInSuperTenant(emailSenderAdd, tenantId);
        addDefaultProperties(emailSenderAdd, publisherInSuperTenant);
        ResourceAdd emailSenderResourceAdd = null;
        try {
            InputStream inputStream = generateEmailPublisher(emailSenderAdd);
            emailSenderResourceAdd = getResourceAddFromEmailSenderAdd(emailSenderAdd);
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .addResource(PUBLISHER_RESOURCE_TYPE, emailSenderResourceAdd);
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .addFile(PUBLISHER_RESOURCE_TYPE, emailSenderAdd.getName(), emailSenderAdd.getName(), inputStream);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_ADDING_NOTIFICATION_SENDER,
                    emailSenderAdd.getName());
        } catch (ParserConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_PARSER_CONFIG_EXCEPTION,
                    e.getMessage());
        } catch (TransformerException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_TRANSFORMER_EXCEPTION,
                    e.getMessage());
        }
        return buildEmailSenderFromEmailSenderResourceAdd(emailSenderResourceAdd);
    }

    public SMSSender addSMSSender(SMSSenderAdd smsSenderAdd) {

        Integer tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        EventPublisherConfiguration publisherInSuperTenant =
                validateSMSSenderAddAndGetPublisherInSuperTenant(smsSenderAdd, tenantId);
        addDefaultProperties(smsSenderAdd, publisherInSuperTenant);
        ResourceAdd smsSenderResourceAdd = null;
        try {
            InputStream inputStream = generateSMSPublisher(smsSenderAdd);
            smsSenderResourceAdd = getResourceAddFromSmsSenderAdd(smsSenderAdd);
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .addResource(PUBLISHER_RESOURCE_TYPE, smsSenderResourceAdd);
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .addFile(PUBLISHER_RESOURCE_TYPE, smsSenderAdd.getName(), smsSenderAdd.getName(), inputStream);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_ADDING_NOTIFICATION_SENDER,
                    smsSenderAdd.getName());
        } catch (ParserConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_PARSER_CONFIG_EXCEPTION,
                    e.getMessage());
        } catch (TransformerException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_TRANSFORMER_EXCEPTION,
                    e.getMessage());
        }
        return buildSmsSenderFromSmsSenderResourceAdd(smsSenderResourceAdd);
    }

    public void deleteNotificationSender(String notificationSenderName) {

        try {
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .deleteResource(PUBLISHER_RESOURCE_TYPE, notificationSenderName);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_DELETING_NOTIFICATION_SENDER,
                    notificationSenderName);
        }
    }

    public EmailSender getEmailSender(String senderName) {

        try {
            Resource resource = NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResource(PUBLISHER_RESOURCE_TYPE, senderName);
            return getEmailSenderFromResource(resource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDER, senderName);
        }
    }

    public SMSSender getSMSSender(String senderName) {

        try {
            Resource resource = NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResource(PUBLISHER_RESOURCE_TYPE, senderName);
            return getSmsSenderFromResource(resource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDER, senderName);
        }
    }

    public List<EmailSender> getEmailSenders() {

        try {
            Resources publisherResources = NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResourcesByType(PUBLISHER_RESOURCE_TYPE);
            List<Resource> emailPublisherResources = publisherResources.getResources().stream().filter(resource ->
                    resource.getAttributes().stream().anyMatch(attribute ->
                            PUBLISHER_TYPE_PROPERTY.equals(attribute.getKey()) &&
                                    EMAIL_PUBLISHER_TYPE.equals(attribute.getValue()))).collect(Collectors.toList());
            return emailPublisherResources.stream().map(resource -> getEmailSenderFromResource(resource)).collect(
                    Collectors.toList());
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDERS_BY_TYPE,
                    EMAIL_PUBLISHER_TYPE);
        }
    }

    public List<SMSSender> getSMSSenders() {

        try {
            Resources publisherResources = NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResourcesByType(PUBLISHER_RESOURCE_TYPE);
            List<Resource> smsPublisherResources = publisherResources.getResources().stream().filter(resource ->
                    resource.getAttributes().stream().anyMatch(attribute ->
                            PUBLISHER_TYPE_PROPERTY.equals(attribute.getKey()) &&
                                    SMS_PUBLISHER_TYPE.equals(attribute.getValue()))).collect(Collectors.toList());
            return smsPublisherResources.stream().map(resource -> getSmsSenderFromResource(resource)).collect(
                    Collectors.toList());
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDERS_BY_TYPE,
                    SMS_PUBLISHER_TYPE);
        }
    }

    public EmailSender updateEmailSender(String senderName, EmailSenderUpdateRequest emailSenderUpdateRequest) {

        ResourceAdd emailSenderResourceAdd = null;
        //TODO generate emailSenderAdd by emailSenderUpdateRequest
        try {
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .replaceResource(senderName, emailSenderResourceAdd);
            //TODO file replacement
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_UPDATING_NOTIFICATION_SENDER, senderName);
        }
        return buildEmailSenderFromEmailSenderResourceAdd(emailSenderResourceAdd);
    }

    public SMSSender updateSMSSender(String senderName, SMSSenderUpdateRequest smsSenderUpdateRequest) {

        ResourceAdd smsSenderResourceAdd = null;
        //TODO generate smsSenderResourceAdd by smsSenderUpdateRequest
        try {
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .replaceResource(senderName, smsSenderResourceAdd);
            //TODO file replacement
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_UPDATING_NOTIFICATION_SENDER, senderName);
        }
        return buildSmsSenderFromSmsSenderResourceAdd(smsSenderResourceAdd);
    }

    private EventPublisherConfiguration validateEmailSenderAddAndGetPublisherInSuperTenant(
            EmailSenderAdd emailSenderAdd, int tenantId) {

        String emailSenderAddName = emailSenderAdd.getName();
        EventPublisherConfiguration publisherInSuperTenant = null;
        EventPublisherService eventPublisherService = NotificationSenderServiceHolder.getEventPublisherService();
        List<EventPublisherConfiguration> activeEventPublisherConfigurations = null;
        try {
            startSuperTenantFlow();
            activeEventPublisherConfigurations =
                    eventPublisherService.getAllActiveEventPublisherConfigurations();
            if (activeEventPublisherConfigurations == null) {
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND, "carbon.super");
            }
            startTenantFlow(tenantId);
            // Set the default publisher name if name is not defined.
            if (StringUtils.isEmpty(emailSenderAdd.getName())) {
                emailSenderAddName = DEFAULT_EMAIL_PUBLISHER;
            }
            // Check whether the super tenant has a publisher with the defined name.
            String finalEmailSenderAddName = emailSenderAddName;
            publisherInSuperTenant = activeEventPublisherConfigurations.stream()
                    .filter(publisher -> publisher.getEventPublisherName().equals(finalEmailSenderAddName)).findAny()
                    .orElse(null);
            if (publisherInSuperTenant == null) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_PUBLISHER_NOT_EXISTS_IN_SUPER_TENANT,
                        emailSenderAddName);
            }
            // Check whether a publisher already exists with the same name in the particular tenant to be added.
            Resource resource =
                    NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                            .getResource(PUBLISHER_RESOURCE_TYPE, emailSenderAddName);
            if (resource != null) {
                throw handleException(Response.Status.CONFLICT, ERROR_CODE_CONFLICT_PUBLISHER,
                        emailSenderAddName);
            }
        } catch (EventPublisherConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    ERROR_CODE_SERVER_ERRORS_GETTING_EVENT_PUBLISHER, e.getMessage());
        } catch (ConfigurationManagementException e) {
            if (!RESOURCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDER,
                        emailSenderAddName);
            }
        }
        return publisherInSuperTenant;
    }

    private EventPublisherConfiguration validateSMSSenderAddAndGetPublisherInSuperTenant(SMSSenderAdd smsSenderAdd,
                                                                                         int tenantId) {

        String smsSenderAddName = smsSenderAdd.getName();
        EventPublisherConfiguration publisherInSuperTenant = null;
        EventPublisherService eventPublisherService = NotificationSenderServiceHolder.getEventPublisherService();
        SMSProviderPayloadTemplateManager smsProviderPayloadTemplateManager =
                NotificationSenderServiceHolder.getSmsProviderPayloadTemplateManager();
        List<EventPublisherConfiguration> activeEventPublisherConfigurations = null;
        Map<String, String> properties = new HashMap<>();
        if (smsSenderAdd.getProperties() != null) {
            smsSenderAdd.getProperties().stream()
                    .map(property -> properties.put(property.getKey(), property.getValue()))
                    .collect(Collectors.toList());
        }
        try {
            if (StringUtils.isEmpty(smsSenderAdd.getProvider())) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_SMS_PROVIDER_REQUIRED, null);
            }
            if (StringUtils.isEmpty(properties.get(SMS_SEND_API_BODY_PROPERTY))) {
                SMSProviderTemplate sendSmsAPIPayload = smsProviderPayloadTemplateManager
                        .getSMSProviderPayloadTemplateByProvider(smsSenderAdd.getProvider());
                if (sendSmsAPIPayload == null) {
                    throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_SMS_PAYLOAD_NOT_FOUND,
                            smsSenderAdd.getProvider());
                }
            }
            if (StringUtils.isEmpty(smsSenderAdd.getProviderURL())) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_SMS_PROVIDER_URL_REQUIRED, null);
            }
            startSuperTenantFlow();
            activeEventPublisherConfigurations =
                    eventPublisherService.getAllActiveEventPublisherConfigurations();
            if (activeEventPublisherConfigurations == null) {
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND, "carbon.super");
            }
            startTenantFlow(tenantId);
            // Set the default publisher name if name is not defined.
            if (StringUtils.isEmpty(smsSenderAdd.getName())) {
                smsSenderAddName = DEFAULT_SMS_PUBLISHER;
            } else {
                smsSenderAddName = smsSenderAdd.getName();
            }
            // Check whether the super tenant has a publisher with the defined name.
            String finalSmsSenderAddName = smsSenderAddName;
            publisherInSuperTenant = activeEventPublisherConfigurations.stream()
                    .filter(publisher -> publisher.getEventPublisherName().equals(finalSmsSenderAddName)).findAny()
                    .orElse(null);
            if (publisherInSuperTenant == null) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_PUBLISHER_NOT_EXISTS_IN_SUPER_TENANT,
                        smsSenderAddName);
            }
            // Check whether a publisher already exists with the same name in the particular tenant to be added.
            Resource resource =
                    NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                            .getResource(PUBLISHER_RESOURCE_TYPE, smsSenderAddName);
            if (resource != null) {
                throw handleException(Response.Status.CONFLICT, ERROR_CODE_CONFLICT_PUBLISHER, smsSenderAddName);
            }
        } catch (EventPublisherConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    ERROR_CODE_SERVER_ERRORS_GETTING_EVENT_PUBLISHER, e.getMessage());
        } catch (ConfigurationManagementException e) {
            if (!RESOURCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDER,
                        smsSenderAddName);
            }
        }
        return publisherInSuperTenant;
    }

    private void addDefaultProperties(EmailSenderAdd emailSenderAdd,
                                      EventPublisherConfiguration publisherInSuperTenant) {

        // Set the default publisher name if name is not defined.
        if (StringUtils.isEmpty(emailSenderAdd.getName())) {
            emailSenderAdd.setName(DEFAULT_EMAIL_PUBLISHER);
        }
        // Add the relevant stream name and version to the new publisher.
        if (emailSenderAdd.getProperties() == null) {
            List<Properties> properties = new ArrayList<>();
            emailSenderAdd.setProperties(properties);
        }
        Properties streamNameProperty = new Properties();
        streamNameProperty.setKey(STREAM_NAME);
        streamNameProperty.setValue(publisherInSuperTenant.getFromStreamName());
        emailSenderAdd.getProperties().add(streamNameProperty);
        Properties streamVersionProperty = new Properties();
        streamVersionProperty.setKey(STREAM_VERSION);
        streamVersionProperty.setValue(publisherInSuperTenant.getFromStreamVersion());
        emailSenderAdd.getProperties().add(streamVersionProperty);
        // Add the publisher type to the new publisher.
        Properties typeProperty = new Properties();
        typeProperty.setKey(PUBLISHER_TYPE_PROPERTY);
        typeProperty.setValue(EMAIL_PUBLISHER_TYPE);
        emailSenderAdd.getProperties().add(typeProperty);
    }

    private void addDefaultProperties(SMSSenderAdd smsSenderAdd, EventPublisherConfiguration publisherInSuperTenant) {

        // Set the default publisher name if name is not defined.
        if (StringUtils.isEmpty(smsSenderAdd.getName())) {
            smsSenderAdd.setName(DEFAULT_SMS_PUBLISHER);
        }
        // Add the relevant stream name and version to the new publisher.
        if (smsSenderAdd.getProperties() == null) {
            List<Properties> propertiesList = new ArrayList<>();
            smsSenderAdd.setProperties(propertiesList);
        }
        Properties streamNameProperty = new Properties();
        streamNameProperty.setKey(STREAM_NAME);
        streamNameProperty.setValue(publisherInSuperTenant.getFromStreamName());
        smsSenderAdd.getProperties().add(streamNameProperty);
        Properties streamVersionProperty = new Properties();
        streamVersionProperty.setKey(STREAM_VERSION);
        streamVersionProperty.setValue(publisherInSuperTenant.getFromStreamVersion());
        smsSenderAdd.getProperties().add(streamVersionProperty);
        // Add the publisher type to the new publisher.
        Properties typeProperty = new Properties();
        typeProperty.setKey(PUBLISHER_TYPE_PROPERTY);
        typeProperty.setValue(SMS_PUBLISHER_TYPE);
        smsSenderAdd.getProperties().add(typeProperty);
    }

    private ResourceAdd getResourceAddFromEmailSenderAdd(EmailSenderAdd emailSenderAdd) {

        ResourceAdd resourceAdd = new ResourceAdd();
        resourceAdd.setName(emailSenderAdd.getName());
        Map<String, String> emailSenderAttributes = new HashMap<>();
        emailSenderAttributes.put(FROM_ADDRESS, emailSenderAdd.getFromAddress());
        emailSenderAttributes.put(USERNAME, emailSenderAdd.getUserName());
        emailSenderAttributes.put(PASSWORD, emailSenderAdd.getPassword());
        emailSenderAttributes.put(SMTP_SERVER_HOST, emailSenderAdd.getSmtpServerHost());
        emailSenderAttributes.put(SMTP_PORT, String.valueOf(emailSenderAdd.getSmtpPort()));
        emailSenderAdd.getProperties().stream()
                .map(property -> emailSenderAttributes.put(property.getKey(), property.getValue()))
                .collect(Collectors.toList());
        List<Attribute> resourceAttributes =
                emailSenderAttributes.entrySet().stream()
                        .filter(attribute -> attribute.getValue() != null && !"null".equals(attribute.getValue()))
                        .map(attribute -> new Attribute(attribute.getKey(), attribute.getValue()))
                        .collect(Collectors.toList());
        resourceAdd.setAttributes(resourceAttributes);
        return resourceAdd;
    }

    private EmailSender buildEmailSenderFromEmailSenderResourceAdd(ResourceAdd emailSenderResourceAdd) {

        EmailSender emailSender = new EmailSender();
        emailSender.setName(emailSenderResourceAdd.getName());
        List<Properties> emailSenderProperties = new ArrayList<>();
        Map<String, String> attributesMap = emailSenderResourceAdd.getAttributes().stream()
                .filter(attribute -> !(STREAM_NAME.equals(attribute.getKey()) ||
                        STREAM_VERSION.equals(attribute.getKey()) ||
                        PUBLISHER_TYPE_PROPERTY.equals(attribute.getKey())))
                .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
        attributesMap.entrySet().forEach(attribute -> {
            switch (attribute.getKey()) {
                case SMTP_SERVER_HOST:
                    emailSender.setSmtpServerHost(attribute.getKey());
                    break;
                case SMTP_PORT:
                    emailSender.setSmtpPort(Integer.valueOf(attribute.getKey()));
                    break;
                case FROM_ADDRESS:
                    emailSender.setFromAddress(attribute.getKey());
                    break;
                case USERNAME:
                    emailSender.setUserName(attribute.getValue());
                    break;
                case PASSWORD:
                    emailSender.setPassword(attribute.getValue());
                    break;
                case STREAM_NAME:
                case STREAM_VERSION:
                case PUBLISHER_TYPE_PROPERTY:
                    break;
                default:
                    Properties property = new Properties();
                    property.setKey(attribute.getKey());
                    property.setValue(attribute.getValue());
                    emailSenderProperties.add(property);
            }
        });
        emailSender.setProperties(emailSenderProperties);
        return emailSender;
    }

    private EmailSender getEmailSenderFromResource(Resource resource) {

        EmailSender emailSender = new EmailSender();
        emailSender.setName(resource.getResourceName());
        List<Properties> emailSenderProperties = new ArrayList<>();
        Map<String, String> attributesMap =
                resource.getAttributes().stream().filter(attribute -> !(STREAM_NAME.equals(attribute.getKey()) ||
                        STREAM_VERSION.equals(attribute.getKey()) ||
                        PUBLISHER_TYPE_PROPERTY.equals(attribute.getKey())))
                        .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
        attributesMap.entrySet().forEach(attribute -> {
            switch (attribute.getKey()) {
                case SMTP_SERVER_HOST:
                    emailSender.setSmtpServerHost(attribute.getKey());
                    break;
                case SMTP_PORT:
                    emailSender.setSmtpPort(Integer.valueOf(attribute.getKey()));
                    break;
                case FROM_ADDRESS:
                    emailSender.setFromAddress(attribute.getKey());
                    break;
                case USERNAME:
                    emailSender.setUserName(attribute.getValue());
                    break;
                case PASSWORD:
                    emailSender.setPassword(attribute.getValue());
                    break;
                case STREAM_NAME:
                case STREAM_VERSION:
                case PUBLISHER_TYPE_PROPERTY:
                    break;
                default:
                    Properties property = new Properties();
                    property.setKey(attribute.getKey());
                    property.setValue(attribute.getValue());
                    emailSenderProperties.add(property);
            }
        });
        emailSender.setProperties(emailSenderProperties);
        return emailSender;
    }

    private ResourceAdd getResourceAddFromSmsSenderAdd(SMSSenderAdd smsSenderAdd) {

        ResourceAdd resourceAdd = new ResourceAdd();
        resourceAdd.setName(smsSenderAdd.getName());
        Map<String, String> smsSenderAttributes = new HashMap<>();
        smsSenderAttributes.put(PROVIDER, smsSenderAdd.getProvider());
        smsSenderAttributes.put(PROVIDER_URL, smsSenderAdd.getProviderURL());
        smsSenderAttributes.put(KEY, smsSenderAdd.getKey());
        smsSenderAttributes.put(SECRET, smsSenderAdd.getSecret());
        smsSenderAttributes.put(SENDER, smsSenderAdd.getSender());
        smsSenderAdd.getProperties().stream()
                .map(property -> smsSenderAttributes.put(property.getKey(), property.getValue())).collect(
                Collectors.toList());
        List<Attribute> resourceAttributes =
                smsSenderAttributes.entrySet().stream().filter(attribute -> attribute.getValue() != null)
                        .map(attribute -> new Attribute(attribute.getKey(), attribute.getValue()))
                        .collect(Collectors.toList());
        resourceAdd.setAttributes(resourceAttributes);
        return resourceAdd;
    }

    private SMSSender buildSmsSenderFromSmsSenderResourceAdd(ResourceAdd smsSenderResourceAdd) {

        SMSSender smsSender = new SMSSender();
        smsSender.setName(smsSenderResourceAdd.getName());
        List<Properties> smsSenderProperties = new ArrayList<>();
        Map<String, String> attributesMap = smsSenderResourceAdd.getAttributes().stream()
                .filter(attribute -> !(STREAM_NAME.equals(attribute.getKey()) ||
                        STREAM_VERSION.equals(attribute.getKey()) ||
                        PUBLISHER_TYPE_PROPERTY.equals(attribute.getKey())))
                .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
        attributesMap.entrySet().forEach(attribute -> {
            switch (attribute.getKey()) {
                case PROVIDER:
                    smsSender.setProvider(attribute.getKey());
                    break;
                case PROVIDER_URL:
                    smsSender.setProviderURL(attribute.getValue());
                    break;
                case KEY:
                    smsSender.setKey(attribute.getValue());
                    break;
                case SECRET:
                    smsSender.setSecret(attribute.getValue());
                    break;
                case SENDER:
                    smsSender.setSender(attribute.getValue());
                    break;
                case STREAM_NAME:
                case STREAM_VERSION:
                case PUBLISHER_TYPE_PROPERTY:
                    break;
                default:
                    Properties property = new Properties();
                    property.setKey(attribute.getKey());
                    property.setValue(attribute.getValue());
                    smsSenderProperties.add(property);
            }
        });
        smsSender.setProperties(smsSenderProperties);
        return smsSender;
    }

    private SMSSender getSmsSenderFromResource(Resource resource) {

        SMSSender smsSender = new SMSSender();
        smsSender.setName(resource.getResourceName());
        List<Properties> smsSenderProperties = new ArrayList<>();
        Map<String, String> attributesMap =
                resource.getAttributes().stream().filter(attribute -> !(STREAM_NAME.equals(attribute.getKey()) ||
                        STREAM_VERSION.equals(attribute.getKey()) ||
                        PUBLISHER_TYPE_PROPERTY.equals(attribute.getKey())))
                        .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
        attributesMap.entrySet().forEach(attribute -> {
            switch (attribute.getKey()) {
                case PROVIDER:
                    smsSender.setProvider(attribute.getKey());
                    break;
                case PROVIDER_URL:
                    smsSender.setProviderURL(attribute.getValue());
                    break;
                case KEY:
                    smsSender.setKey(attribute.getValue());
                    break;
                case SECRET:
                    smsSender.setSecret(attribute.getValue());
                    break;
                case SENDER:
                    smsSender.setSender(attribute.getValue());
                    break;
                case STREAM_NAME:
                case STREAM_VERSION:
                case PUBLISHER_TYPE_PROPERTY:
                    break;
                default:
                    Properties property = new Properties();
                    property.setKey(attribute.getKey());
                    property.setValue(attribute.getValue());
                    smsSenderProperties.add(property);
            }
        });
        smsSender.setProperties(smsSenderProperties);
        return smsSender;
    }

    private void startSuperTenantFlow() {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext();
        carbonContext.setTenantId(MultitenantConstants.SUPER_TENANT_ID);
        carbonContext.setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
    }

    private void startTenantFlow(int tenantId) {

        PrivilegedCarbonContext.startTenantFlow();
        PrivilegedCarbonContext.getThreadLocalCarbonContext().setTenantId(tenantId);
        PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .setTenantDomain(IdentityTenantUtil.getTenantDomain(tenantId));
    }

    private APIError handleConfigurationMgtException(ConfigurationManagementException e,
                                                     NotificationSenderManagementConstants.ErrorMessage errorEnum,
                                                     String data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
        Response.Status status;
        if (e instanceof ConfigurationManagementClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : NOTIFICATION_SENDER_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof ConfigurationManagementServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : NOTIFICATION_SENDER_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, NotificationSenderManagementConstants.ErrorMessage error,
                                     String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(NotificationSenderManagementConstants.ErrorMessage errorMsg,
                                                  String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Error message.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(NotificationSenderManagementConstants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getDescription(), data);
        } else {
            message = error.getDescription();
        }
        return message;
    }
}
