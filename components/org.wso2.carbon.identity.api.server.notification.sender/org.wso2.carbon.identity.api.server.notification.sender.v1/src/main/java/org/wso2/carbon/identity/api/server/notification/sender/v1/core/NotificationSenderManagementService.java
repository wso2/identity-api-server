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

import org.apache.commons.collections.CollectionUtils;
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
import org.wso2.carbon.identity.configuration.mgt.core.model.ResourceFile;
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

import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.CONFIG_MGT_ERROR_CODE_DELIMITER;
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
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_NO_RESOURCE_EXISTS;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_PARSER_CONFIG_EXCEPTION;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_PUBLISHER_NOT_EXISTS_IN_SUPER_TENANT;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_SERVER_ERRORS_GETTING_EVENT_PUBLISHER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_SMS_PAYLOAD_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_SMS_PROVIDER_REQUIRED;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_SMS_PROVIDER_URL_REQUIRED;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ErrorMessage.ERROR_CODE_TRANSFORMER_EXCEPTION;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.FROM_ADDRESS;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.INLINE_BODY_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.INTERNAL_PROPERTIES;
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

    /**
     * Create an email sender resource with a resource file.
     *
     * @param emailSenderAdd Email sender post request.
     * @return Email sender.
     */
    public EmailSender addEmailSender(EmailSenderAdd emailSenderAdd) {

        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        EventPublisherConfiguration publisherInSuperTenant =
                validateEmailSenderAddAndGetPublisherInSuperTenant(emailSenderAdd, tenantId);
        addDefaultProperties(emailSenderAdd, publisherInSuperTenant);
        Resource emailSenderResource = null;
        try {
            InputStream inputStream = generateEmailPublisher(emailSenderAdd);
            emailSenderResource = buildResourceFromEmailSenderAdd(emailSenderAdd, inputStream);
            /*
            The input properties will be saved as the attributes of a resource to return in the notification-senders
            API responses.
            Also an event publisher file is generated with the input values and save it as a file of the resource.
            It is used when loading tenant wise event publisher loading flow.
             */
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .addResource(PUBLISHER_RESOURCE_TYPE, emailSenderResource);
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
        return buildEmailSenderFromResource(emailSenderResource);
    }

    /**
     * Create a sms sender resource with a resource file.
     *
     * @param smsSenderAdd SMS sender post request.
     * @return SMS sender.
     */
    public SMSSender addSMSSender(SMSSenderAdd smsSenderAdd) {

        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        EventPublisherConfiguration publisherInSuperTenant =
                validateSMSSenderAddAndGetPublisherInSuperTenant(smsSenderAdd, tenantId);
        addDefaultProperties(smsSenderAdd, publisherInSuperTenant);
        Resource smsSenderResource = null;
        try {
            InputStream inputStream = generateSMSPublisher(smsSenderAdd);
            smsSenderResource = buildResourceFromSmsSenderAdd(smsSenderAdd, inputStream);
            /*
            The input properties will be saved as the attributes of a resource to return in the notification-senders
            API responses.
            Also an event publisher file is generated with the input values and save it as a file of the resource.
            It is used when loading tenant wise event publisher loading flow.
             */
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .addResource(PUBLISHER_RESOURCE_TYPE, smsSenderResource);
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
        return buildSmsSenderFromResource(smsSenderResource);
    }

    /**
     * Delete a SMS/Email sender by name.
     *
     * @param notificationSenderName Name of the notification sender.
     */
    public void deleteNotificationSender(String notificationSenderName) {

        try {
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .deleteResource(PUBLISHER_RESOURCE_TYPE, notificationSenderName);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_DELETING_NOTIFICATION_SENDER,
                    notificationSenderName);
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
            Resource resource = NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResource(PUBLISHER_RESOURCE_TYPE, senderName);
            return buildEmailSenderFromResource(resource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDER, senderName);
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
            Resource resource = NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResource(PUBLISHER_RESOURCE_TYPE, senderName);
            return buildSmsSenderFromResource(resource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDER, senderName);
        }
    }

    /**
     * Retrieve all email senders of the tenant.
     *
     * @return Email senders of the tenant.
     */
    public List<EmailSender> getEmailSenders() {

        try {
            Resources publisherResources = NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResourcesByType(PUBLISHER_RESOURCE_TYPE);
            List<Resource> emailPublisherResources = publisherResources.getResources().stream().filter(resource ->
                    resource.getAttributes().stream().anyMatch(attribute ->
                            PUBLISHER_TYPE_PROPERTY.equals(attribute.getKey()) &&
                                    EMAIL_PUBLISHER_TYPE.equals(attribute.getValue()))).collect(Collectors.toList());
            return emailPublisherResources.stream().map(resource -> buildEmailSenderFromResource(resource)).collect(
                    Collectors.toList());
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDERS_BY_TYPE,
                    EMAIL_PUBLISHER_TYPE);
        }
    }

    /**
     * Retrieve all sms senders of the tenant.
     *
     * @return SMS senders of the tenant.
     */
    public List<SMSSender> getSMSSenders() {

        try {
            Resources publisherResources = NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResourcesByType(PUBLISHER_RESOURCE_TYPE);
            List<Resource> smsPublisherResources = publisherResources.getResources().stream().filter(resource ->
                    resource.getAttributes().stream().anyMatch(attribute ->
                            PUBLISHER_TYPE_PROPERTY.equals(attribute.getKey()) &&
                                    SMS_PUBLISHER_TYPE.equals(attribute.getValue()))).collect(Collectors.toList());
            return smsPublisherResources.stream().map(resource -> buildSmsSenderFromResource(resource)).collect(
                    Collectors.toList());
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_NOTIFICATION_SENDERS_BY_TYPE,
                    SMS_PUBLISHER_TYPE);
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

        Resource emailSenderResource = null;
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        EventPublisherConfiguration publisherInSuperTenant =
                validateEmailSenderUpdateRequestAndGetPublisherInSuperTenant(senderName, tenantId);
        EmailSenderAdd emailSenderAdd =
                buildEmailSenderAddFromEmailSenderUpdateRequest(senderName, emailSenderUpdateRequest);
        addDefaultProperties(emailSenderAdd, publisherInSuperTenant);
        try {
            InputStream inputStream = generateEmailPublisher(emailSenderAdd);
            emailSenderResource = buildResourceFromEmailSenderAdd(emailSenderAdd, inputStream);
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .replaceResource(PUBLISHER_RESOURCE_TYPE, emailSenderResource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_UPDATING_NOTIFICATION_SENDER, senderName);
        } catch (ParserConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_PARSER_CONFIG_EXCEPTION,
                    e.getMessage());
        } catch (TransformerException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_TRANSFORMER_EXCEPTION,
                    e.getMessage());
        }
        return buildEmailSenderFromResource(emailSenderResource);
    }

    /**
     * Update sms sender details by name.
     *
     * @param senderName             SMS sender' name.
     * @param smsSenderUpdateRequest SMS sender's updated configurations.
     * @return Updated SMS sender.
     */
    public SMSSender updateSMSSender(String senderName, SMSSenderUpdateRequest smsSenderUpdateRequest) {

        Resource smsSenderResource = null;
        int tenantId = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantId();
        EventPublisherConfiguration publisherInSuperTenant =
                validateSmsSenderUpdateRequestAndGetPublisherInSuperTenant(senderName, smsSenderUpdateRequest,
                        tenantId);
        SMSSenderAdd smsSenderAdd = buildSMSSenderAddFromSMSSenderUpdateRequest(senderName, smsSenderUpdateRequest);
        addDefaultProperties(smsSenderAdd, publisherInSuperTenant);
        try {
            InputStream inputStream = generateSMSPublisher(smsSenderAdd);
            smsSenderResource = buildResourceFromSmsSenderAdd(smsSenderAdd, inputStream);
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .replaceResource(PUBLISHER_RESOURCE_TYPE, smsSenderResource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_UPDATING_NOTIFICATION_SENDER, senderName);
        } catch (ParserConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_PARSER_CONFIG_EXCEPTION,
                    e.getMessage());
        } catch (TransformerException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_TRANSFORMER_EXCEPTION,
                    e.getMessage());
        }
        return buildSmsSenderFromResource(smsSenderResource);
    }

    /**
     * Validate the email Sender post request and get the corresponding super tenant's event publisher configuration.
     *
     * @param emailSenderAdd Email sender post request.
     * @param tenantId       Tenant id.
     * @return Corresponding super tenant's event publisher's configuration.
     */
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

    /**
     * Validate the email Sender put request and get the corresponding super tenant's event publisher configuration.
     *
     * @param senderName Email sender's name.
     * @param tenantId   Tenant id.
     * @return Corresponding super tenant's event publisher's configuration.
     */
    private EventPublisherConfiguration validateEmailSenderUpdateRequestAndGetPublisherInSuperTenant(
            String senderName, int tenantId) {

        EventPublisherService eventPublisherService = NotificationSenderServiceHolder.getEventPublisherService();
        EventPublisherConfiguration publisherInSuperTenant = null;
        try {
            // Check whether a publisher exists to replace.
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResource(PUBLISHER_RESOURCE_TYPE, senderName);
            startSuperTenantFlow();
            List<EventPublisherConfiguration> activeEventPublisherConfigurations =
                    eventPublisherService.getAllActiveEventPublisherConfigurations();
            if (activeEventPublisherConfigurations == null) {
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND, "carbon.super");
            }
            startTenantFlow(tenantId);
            // Check whether the super tenant has a publisher with the defined name.
            publisherInSuperTenant = activeEventPublisherConfigurations.stream()
                    .filter(publisher -> publisher.getEventPublisherName().equals(senderName)).findAny()
                    .orElse(null);
            if (publisherInSuperTenant == null) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_PUBLISHER_NOT_EXISTS_IN_SUPER_TENANT,
                        senderName);
            }
        } catch (EventPublisherConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    ERROR_CODE_SERVER_ERRORS_GETTING_EVENT_PUBLISHER, e.getMessage());
        } catch (ConfigurationManagementException e) {
            // If resource not found by id.
            if (RESOURCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                throw handleConfigurationMgtException(e, ERROR_CODE_NO_RESOURCE_EXISTS, senderName);
            }
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_UPDATING_NOTIFICATION_SENDER, senderName);
        }
        return publisherInSuperTenant;
    }

    /**
     * Validate the SMS Sender post request and get the corresponding super tenant's event publisher configuration.
     *
     * @param smsSenderAdd SMS sender post request.
     * @param tenantId     Tenant id.
     * @return Corresponding super tenant's event publisher's configuration.
     */
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
            if (StringUtils.isEmpty(properties.get(INLINE_BODY_PROPERTY))) {
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

    /**
     * Validate the SMS Sender put request and get the corresponding super tenant's event publisher configuration.
     *
     * @param senderName             SMS sender's name.
     * @param smsSenderUpdateRequest SMS Sender put request.
     * @param tenantId               Tenant id.
     * @return Corresponding super tenant's event publisher's configuration.
     */
    private EventPublisherConfiguration validateSmsSenderUpdateRequestAndGetPublisherInSuperTenant(String senderName,
                                                               SMSSenderUpdateRequest smsSenderUpdateRequest,
                                                               int tenantId) {

        EventPublisherConfiguration publisherInSuperTenant = null;
        EventPublisherService eventPublisherService = NotificationSenderServiceHolder.getEventPublisherService();
        SMSProviderPayloadTemplateManager smsProviderPayloadTemplateManager =
                NotificationSenderServiceHolder.getSmsProviderPayloadTemplateManager();
        Map<String, String> properties = new HashMap<>();
        if (smsSenderUpdateRequest.getProperties() != null) {
            smsSenderUpdateRequest.getProperties().stream()
                    .map(property -> properties.put(property.getKey(), property.getValue()))
                    .collect(Collectors.toList());
        }
        try {
            // Check whether a publisher exists to replace.
            NotificationSenderServiceHolder.getNotificationSenderConfigManager()
                    .getResource(PUBLISHER_RESOURCE_TYPE, senderName);
            if (StringUtils.isEmpty(smsSenderUpdateRequest.getProvider())) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_SMS_PROVIDER_REQUIRED, null);
            }
            if (StringUtils.isEmpty(properties.get(INLINE_BODY_PROPERTY))) {
                SMSProviderTemplate sendSmsAPIPayload = smsProviderPayloadTemplateManager
                        .getSMSProviderPayloadTemplateByProvider(smsSenderUpdateRequest.getProvider());
                if (sendSmsAPIPayload == null) {
                    throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_SMS_PAYLOAD_NOT_FOUND,
                            smsSenderUpdateRequest.getProvider());
                }
            }
            if (StringUtils.isEmpty(smsSenderUpdateRequest.getProviderURL())) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_SMS_PROVIDER_URL_REQUIRED, null);
            }
            startSuperTenantFlow();
            List<EventPublisherConfiguration> activeEventPublisherConfigurations =
                    eventPublisherService.getAllActiveEventPublisherConfigurations();
            if (activeEventPublisherConfigurations == null) {
                throw handleException(Response.Status.NOT_FOUND, ERROR_CODE_NO_ACTIVE_PUBLISHERS_FOUND, "carbon.super");
            }
            startTenantFlow(tenantId);
            // Check whether the super tenant has a publisher with the defined name.
            publisherInSuperTenant = activeEventPublisherConfigurations.stream()
                    .filter(publisher -> publisher.getEventPublisherName().equals(senderName)).findAny()
                    .orElse(null);
            if (publisherInSuperTenant == null) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_PUBLISHER_NOT_EXISTS_IN_SUPER_TENANT,
                        senderName);
            }
        } catch (ConfigurationManagementException e) {
            // If resource not found by id.
            if (RESOURCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                throw handleConfigurationMgtException(e, ERROR_CODE_NO_RESOURCE_EXISTS, senderName);
            }
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_UPDATING_NOTIFICATION_SENDER, senderName);
        } catch (EventPublisherConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    ERROR_CODE_SERVER_ERRORS_GETTING_EVENT_PUBLISHER, e.getMessage());
        }
        return publisherInSuperTenant;
    }

    /**
     * Set default properties to EmailSenderAdd object.
     *
     * @param emailSenderAdd         Email sender post body.
     * @param publisherInSuperTenant Corresponding super tenant's event publisher's configuration.
     */
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

    /**
     * Set default properties to SMSSenderAdd object.
     *
     * @param smsSenderAdd           SMS sender post body.
     * @param publisherInSuperTenant Corresponding super tenant's event publisher's configuration.
     */
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

    /**
     * Build Resource by email sender body request.
     *
     * @param emailSenderAdd EmailSender post body.
     * @param inputStream    Email event publisher file stream.
     * @return Resource object.
     */
    private Resource buildResourceFromEmailSenderAdd(EmailSenderAdd emailSenderAdd, InputStream inputStream) {

        Resource resource = new Resource();
        resource.setResourceName(emailSenderAdd.getName());
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
        resource.setAttributes(resourceAttributes);
        // Set file.
        ResourceFile file = new ResourceFile();
        file.setName(emailSenderAdd.getName());
        file.setInputStream(inputStream);
        List<ResourceFile> resourceFiles = new ArrayList<>();
        resourceFiles.add(file);
        resource.setFiles(resourceFiles);
        return resource;
    }

    /**
     * Build an email sender response from email sender's resource object.
     *
     * @param resource Email sender resource object.
     * @return Email Sender response.
     */
    private EmailSender buildEmailSenderFromResource(Resource resource) {

        EmailSender emailSender = new EmailSender();
        emailSender.setName(resource.getResourceName());
        List<Properties> emailSenderProperties = new ArrayList<>();
        // Skip STREAM_NAME, STREAM_VERSION and PUBLISHER_TYPE_PROPERTY properties which are stored for internal use.
        Map<String, String> attributesMap =
                resource.getAttributes().stream()
                        .filter(attribute -> !(INTERNAL_PROPERTIES.contains(attribute.getKey())))
                        .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
        attributesMap.entrySet().forEach(attribute -> {
            switch (attribute.getKey()) {
                case SMTP_SERVER_HOST:
                    emailSender.setSmtpServerHost(attribute.getValue());
                    break;
                case SMTP_PORT:
                    emailSender.setSmtpPort(Integer.valueOf(attribute.getValue()));
                    break;
                case FROM_ADDRESS:
                    emailSender.setFromAddress(attribute.getValue());
                    break;
                case USERNAME:
                    emailSender.setUserName(attribute.getValue());
                    break;
                case PASSWORD:
                    emailSender.setPassword(attribute.getValue());
                    break;
                default:
                    Properties property = new Properties();
                    property.setKey(attribute.getKey());
                    property.setValue(attribute.getValue());
                    emailSenderProperties.add(property);
            }
        });
        if (CollectionUtils.isNotEmpty(emailSenderProperties)) {
            emailSender.setProperties(emailSenderProperties);
        }
        return emailSender;
    }

    /**
     * Build email sender add object from email sender update request.
     *
     * @param senderName               Email sender's name.
     * @param emailSenderUpdateRequest Email sender's update request body.
     * @return Email sender add object
     */
    private EmailSenderAdd buildEmailSenderAddFromEmailSenderUpdateRequest(
            String senderName, EmailSenderUpdateRequest emailSenderUpdateRequest) {

        EmailSenderAdd emailSenderAdd = new EmailSenderAdd();
        emailSenderAdd.setName(senderName);
        emailSenderAdd.setSmtpServerHost(emailSenderUpdateRequest.getSmtpServerHost());
        emailSenderAdd.setSmtpPort(emailSenderUpdateRequest.getSmtpPort());
        emailSenderAdd.setFromAddress(emailSenderUpdateRequest.getFromAddress());
        emailSenderAdd.setUserName(emailSenderUpdateRequest.getUserName());
        emailSenderAdd.setPassword(emailSenderUpdateRequest.getPassword());
        emailSenderAdd.setProperties(emailSenderUpdateRequest.getProperties());
        return emailSenderAdd;
    }

    /**
     * Build a resource object from SMS Sender post body.
     *
     * @param smsSenderAdd SMS sender post body.
     * @param inputStream  SMS event publisher file stream.
     * @return Resource object.
     */
    private Resource buildResourceFromSmsSenderAdd(SMSSenderAdd smsSenderAdd, InputStream inputStream) {

        Resource resource = new Resource();
        resource.setResourceName(smsSenderAdd.getName());
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
        resource.setAttributes(resourceAttributes);
        // Set file.
        ResourceFile file = new ResourceFile();
        file.setName(smsSenderAdd.getName());
        file.setInputStream(inputStream);
        List<ResourceFile> resourceFiles = new ArrayList<>();
        resourceFiles.add(file);
        resource.setFiles(resourceFiles);
        return resource;
    }

    /**
     * Build a SMS sender response from SMS sender's resource object.
     *
     * @param resource SMS sender resource object.
     * @return SMS sender response.
     */
    private SMSSender buildSmsSenderFromResource(Resource resource) {

        SMSSender smsSender = new SMSSender();
        smsSender.setName(resource.getResourceName());
        List<Properties> smsSenderProperties = new ArrayList<>();
        // Skip STREAM_NAME, STREAM_VERSION and PUBLISHER_TYPE_PROPERTY properties which are stored for internal use.
        Map<String, String> attributesMap =
                resource.getAttributes().stream()
                        .filter(attribute -> !(INTERNAL_PROPERTIES.contains(attribute.getKey())))
                        .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
        attributesMap.entrySet().forEach(attribute -> {
            switch (attribute.getKey()) {
                case PROVIDER:
                    smsSender.setProvider(attribute.getValue());
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
                default:
                    Properties property = new Properties();
                    property.setKey(attribute.getKey());
                    property.setValue(attribute.getValue());
                    smsSenderProperties.add(property);
            }
        });
        if (CollectionUtils.isNotEmpty(smsSenderProperties)) {
            smsSender.setProperties(smsSenderProperties);
        }
        return smsSender;
    }

    /**
     * Build SMS sender add object from SMS sender update request.
     *
     * @param senderName             SMS sender's name.
     * @param smsSenderUpdateRequest SMS sender's update request body.
     * @return SMS sender add object.
     */
    private SMSSenderAdd buildSMSSenderAddFromSMSSenderUpdateRequest(String senderName,
                                                                     SMSSenderUpdateRequest smsSenderUpdateRequest) {

        SMSSenderAdd smsSenderAdd = new SMSSenderAdd();
        smsSenderAdd.setName(senderName);
        smsSenderAdd.setProvider(smsSenderUpdateRequest.getProvider());
        smsSenderAdd.setProviderURL(smsSenderUpdateRequest.getProviderURL());
        smsSenderAdd.setKey(smsSenderUpdateRequest.getKey());
        smsSenderAdd.setSecret(smsSenderUpdateRequest.getSecret());
        smsSenderAdd.setSender(smsSenderUpdateRequest.getSender());
        smsSenderAdd.setProperties(smsSenderUpdateRequest.getProperties());
        return smsSenderAdd;
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
                errorCode = errorCode.contains(CONFIG_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        NOTIFICATION_SENDER_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof ConfigurationManagementServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(CONFIG_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        NOTIFICATION_SENDER_ERROR_PREFIX + errorCode;
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
