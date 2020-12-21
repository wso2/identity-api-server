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

package org.wso2.carbon.identity.api.server.notification.sender.v1.core.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderServiceHolder;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.EmailSenderAdd;
import org.wso2.carbon.identity.api.server.notification.sender.v1.model.SMSSenderAdd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ADAPTER_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ADAPTER_PROPERTY_NAME;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ADAPTER_TYPE_EMAIL_VALUE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ADAPTER_TYPE_HTTP_VALUE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ADAPTER_TYPE_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.AUTH;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.CLIENT_HTTP_METHOD_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.CONSTANT_HTTP_POST;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.CUSTOM_MAPPING_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.DEFAULT_MAX_CONNECTIONS_PER_HOST;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.DISABLE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_ADDRESS_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_ADDRESS_VALUE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_INLINE_BODY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_SUBJECT_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_SUBJECT_VALUE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_TYPE_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.EMAIL_TYPE_VALUE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ENABLE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.FROM;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.HTTP_HEADERS;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.HTTP_PASSWORD_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.HTTP_PROXY_HOST;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.HTTP_PROXY_PORT;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.HTTP_URL_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.HTTP_USERNAME_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.INLINE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.INLINE_BODY_PARAM_PREFIX;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.JOB_QUEUE_SIZE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.JSON;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.KEEP_ALIVE_TIME_IN_MILLIS;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.MAPPING;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.MAPPING_TYPE_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.MAX_THREAD;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.MAX_TOTAL_CONNECTIONS;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.MIN_THREAD;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PASSWORD;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PASSWORD_ENCRYPTED_ATTR_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PASSWORD_ENCRYPTED_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PLACEHOLDER_IDENTIFIER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PROCESSING_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PUBLISHER_NAME;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.REPLY_TO;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ROOT_ELEMENT;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SECRET;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SENDER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SIGNATURE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMS_SEND_API_BODY_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_FROM_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_HOST_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_PASSWORD_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_PORT_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_USER_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.STARTTLS;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.STATISTICS_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.STREAM_NAME;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.STREAM_VERSION;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.TEXT;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.TO;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.TRACE_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.USERNAME;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.XMLNS_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.XMLNS_VALUE;

/**
 * Util class for notification sender.
 */
public class NotificationSenderUtils {

    public static InputStream generateEmailPublisher(EmailSenderAdd emailSenderAdd)
            throws ParserConfigurationException, TransformerException {

        Map<String, String> properties = new HashMap<>();
        emailSenderAdd.getProperties().stream().map(property -> properties.put(property.getKey(), property.getValue()))
                .collect(Collectors.toList());
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        // Root element (eventPublisher).
        Element root = document.createElement(ROOT_ELEMENT);
        document.appendChild(root);

        // Collect event publisher attributes to a map.
        Map<String, String> eventPublisherAttributes = new HashMap<>();
        eventPublisherAttributes.put(PUBLISHER_NAME, emailSenderAdd.getName());
        eventPublisherAttributes.put(STATISTICS_KEY, DISABLE);
        eventPublisherAttributes.put(TRACE_KEY, DISABLE);
        eventPublisherAttributes.put(XMLNS_KEY, XMLNS_VALUE);
        // Set attributes to root element.
        for (Map.Entry<String, String> eventPublisherAttribute : eventPublisherAttributes.entrySet()) {
            Attr publisherAttributes = document.createAttribute(eventPublisherAttribute.getKey());
            publisherAttributes.setValue(eventPublisherAttribute.getValue());
            root.setAttributeNode(publisherAttributes);
        }

        // From event stream element.
        Element from = document.createElement(FROM);
        root.appendChild(from);
        // Set attributes to From element.
        Attr streamNameAttr = document.createAttribute(STREAM_NAME);
        streamNameAttr.setValue(properties.get(STREAM_NAME));
        from.setAttributeNode(streamNameAttr);
        Attr streamVersionAttr = document.createAttribute(STREAM_VERSION);
        streamVersionAttr.setValue(properties.get(STREAM_VERSION));
        from.setAttributeNode(streamVersionAttr);

        // Mapping element.
        Element mapping = document.createElement(MAPPING);
        root.appendChild(mapping);
        // Set attributes to mapping element.
        Attr customMappingAttr = document.createAttribute(CUSTOM_MAPPING_KEY);
        customMappingAttr.setValue(ENABLE);
        mapping.setAttributeNode(customMappingAttr);
        Attr mappingTypeAttr = document.createAttribute(MAPPING_TYPE_KEY);
        mappingTypeAttr.setValue(TEXT);
        mapping.setAttributeNode(mappingTypeAttr);
        // Inline element.
        Element inline = document.createElement(INLINE);
        inline.appendChild(document.createTextNode(EMAIL_INLINE_BODY));
        mapping.appendChild(inline);

        // To element.
        Element to = document.createElement(TO);
        root.appendChild(to);
        // Set attributes to to element.
        Attr eventAdapterTypeAttr = document.createAttribute(ADAPTER_TYPE_KEY);
        eventAdapterTypeAttr.setValue(ADAPTER_TYPE_EMAIL_VALUE);
        to.setAttributeNode(eventAdapterTypeAttr);
        // Take adapter properties to a map.
        Map<String, String> adapterProperties = new HashMap<>();
        adapterProperties.put(EMAIL_ADDRESS_PROPERTY, EMAIL_ADDRESS_VALUE);
        adapterProperties.put(EMAIL_TYPE_PROPERTY, EMAIL_TYPE_VALUE);
        adapterProperties.put(EMAIL_SUBJECT_PROPERTY, EMAIL_SUBJECT_VALUE);
        if (StringUtils.isNotEmpty(emailSenderAdd.getPassword())) {
            adapterProperties.put(SMTP_PASSWORD_PROPERTY, emailSenderAdd.getPassword());
        }
        if (StringUtils.isNotEmpty(emailSenderAdd.getFromAddress())) {
            adapterProperties.put(SMTP_FROM_PROPERTY, emailSenderAdd.getFromAddress());
        }
        if (StringUtils.isNotEmpty(emailSenderAdd.getUserName())) {
            adapterProperties.put(SMTP_USER_PROPERTY, emailSenderAdd.getUserName());
        }
        if (StringUtils.isNotEmpty(emailSenderAdd.getSmtpServerHost())) {
            adapterProperties.put(SMTP_HOST_PROPERTY, emailSenderAdd.getSmtpServerHost());
        }
        if (!"null".equals(String.valueOf(emailSenderAdd.getSmtpPort()))) {
            adapterProperties.put(SMTP_PORT_PROPERTY, String.valueOf(emailSenderAdd.getSmtpPort()));
        }
        if (StringUtils.isNotEmpty(properties.get(STARTTLS))) {
            adapterProperties.put(STARTTLS, properties.get(STARTTLS));
        }
        if (StringUtils.isNotEmpty(properties.get(AUTH))) {
            adapterProperties.put(AUTH, properties.get(AUTH));
        }
        if (StringUtils.isNotEmpty(properties.get(SIGNATURE))) {
            adapterProperties.put(SIGNATURE, properties.get(SIGNATURE));
        }
        if (StringUtils.isNotEmpty(properties.get(REPLY_TO))) {
            adapterProperties.put(REPLY_TO, properties.get(REPLY_TO));
        }
        // Thread Pool Related Properties.
        if (StringUtils.isNotEmpty(properties.get(MIN_THREAD))) {
            adapterProperties.put(MIN_THREAD, properties.get(MIN_THREAD));
        }
        if (StringUtils.isNotEmpty(properties.get(MAX_THREAD))) {
            adapterProperties.put(MAX_THREAD, properties.get(MAX_THREAD));
        }
        if (StringUtils.isNotEmpty(properties.get(KEEP_ALIVE_TIME_IN_MILLIS))) {
            adapterProperties.put(KEEP_ALIVE_TIME_IN_MILLIS, properties.get(KEEP_ALIVE_TIME_IN_MILLIS));
        }
        if (StringUtils.isNotEmpty(properties.get(JOB_QUEUE_SIZE))) {
            adapterProperties.put(JOB_QUEUE_SIZE, properties.get(JOB_QUEUE_SIZE));
        }
        // Add properties.
        for (Map.Entry<String, String> property : adapterProperties.entrySet()) {
            Element adapterProperty = document.createElement(ADAPTER_PROPERTY);
            Attr attribute = document.createAttribute(ADAPTER_PROPERTY_NAME);
            attribute.setValue(property.getKey());
            adapterProperty.setAttributeNode(attribute);
            adapterProperty.appendChild(document.createTextNode(property.getValue()));
            to.appendChild(adapterProperty);
        }

        DOMSource xmlSource = new DOMSource(document);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Result outputTarget = new StreamResult(outputStream);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(xmlSource, outputTarget);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static InputStream generateSMSPublisher(SMSSenderAdd smsSenderAdd)
            throws ParserConfigurationException, TransformerException {

        Map<String, String> properties = new HashMap<>();
        smsSenderAdd.getProperties().stream().map(property -> properties.put(property.getKey(), property.getValue()))
                .collect(Collectors.toList());
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        // Root element (eventPublisher).
        Element root = document.createElement(ROOT_ELEMENT);
        document.appendChild(root);

        // Collect event publisher attributes to a map.
        Map<String, String> eventPublisherAttributes = new HashMap<>();
        eventPublisherAttributes.put(PUBLISHER_NAME, smsSenderAdd.getName());
        eventPublisherAttributes.put(PROCESSING_KEY, ENABLE);
        eventPublisherAttributes.put(STATISTICS_KEY, DISABLE);
        eventPublisherAttributes.put(TRACE_KEY, DISABLE);
        eventPublisherAttributes.put(XMLNS_KEY, XMLNS_VALUE);
        // Set attributes to root element.
        for (Map.Entry<String, String> eventPublisherAttribute : eventPublisherAttributes.entrySet()) {
            Attr publisherAttributes = document.createAttribute(eventPublisherAttribute.getKey());
            publisherAttributes.setValue(eventPublisherAttribute.getValue());
            root.setAttributeNode(publisherAttributes);
        }

        // From event stream element.
        Element from = document.createElement(FROM);
        root.appendChild(from);
        // Set attributes to From element.
        Attr streamNameAttr = document.createAttribute(STREAM_NAME);
        streamNameAttr.setValue(properties.get(STREAM_NAME));
        from.setAttributeNode(streamNameAttr);
        Attr streamVersionAttr = document.createAttribute(STREAM_VERSION);
        streamVersionAttr.setValue(properties.get(STREAM_VERSION));
        from.setAttributeNode(streamVersionAttr);

        // Mapping element.
        Element mapping = document.createElement(MAPPING);
        root.appendChild(mapping);
        // Set attributes to mapping element.
        Attr customMappingAttr = document.createAttribute(CUSTOM_MAPPING_KEY);
        customMappingAttr.setValue(ENABLE);
        mapping.setAttributeNode(customMappingAttr);
        Attr mappingTypeAttr = document.createAttribute(MAPPING_TYPE_KEY);
        mappingTypeAttr.setValue(JSON);
        mapping.setAttributeNode(mappingTypeAttr);
        // Inline element.
        Element inline = document.createElement(INLINE);
        String smsSendAPIBody;
        // If body is given as an input we expect that contains all required attributes with values.
        if (StringUtils.isNotEmpty(properties.get(SMS_SEND_API_BODY_PROPERTY))) {
            smsSendAPIBody = properties.get(SMS_SEND_API_BODY_PROPERTY);
        } else {
            String smsSendAPIBodyTemplate = NotificationSenderServiceHolder.getSmsProviderPayloadTemplateManager()
                    .getSMSProviderPayloadTemplateByProvider(smsSenderAdd.getProvider()).getBody();
            smsSendAPIBody = generateSmsSendAPIBody(smsSendAPIBodyTemplate, smsSenderAdd);
        }
        inline.appendChild(document.createTextNode(smsSendAPIBody));
        mapping.appendChild(inline);

        // To element.
        Element to = document.createElement(TO);
        root.appendChild(to);
        // Set attributes to to element.
        Attr eventAdapterTypeAttr = document.createAttribute(ADAPTER_TYPE_KEY);
        eventAdapterTypeAttr.setValue(ADAPTER_TYPE_HTTP_VALUE);
        to.setAttributeNode(eventAdapterTypeAttr);
        // Take adapter properties to a map.
        Map<String, String> adapterProperties = new HashMap<>();
        adapterProperties.put(HTTP_URL_PROPERTY, smsSenderAdd.getProviderURL());
        if (StringUtils.isNotEmpty(properties.get(CLIENT_HTTP_METHOD_PROPERTY))) {
            adapterProperties.put(CLIENT_HTTP_METHOD_PROPERTY, properties.get(CLIENT_HTTP_METHOD_PROPERTY));
        } else {
            adapterProperties.put(CLIENT_HTTP_METHOD_PROPERTY, CONSTANT_HTTP_POST);
        }
        if (StringUtils.isNotEmpty(properties.get(HTTP_HEADERS))) {
            adapterProperties.put(HTTP_HEADERS, properties.get(HTTP_HEADERS));
        }
        if (StringUtils.isNotEmpty(properties.get(USERNAME))) {
            adapterProperties.put(HTTP_USERNAME_PROPERTY, properties.get(USERNAME));
        }
        if (StringUtils.isNotEmpty(properties.get(PASSWORD))) {
            adapterProperties.put(HTTP_PASSWORD_PROPERTY, properties.get(PASSWORD));
        }
        if (StringUtils.isNotEmpty(properties.get(PASSWORD_ENCRYPTED_PROPERTY))) {
            adapterProperties.put(PASSWORD_ENCRYPTED_ATTR_KEY, properties.get(PASSWORD_ENCRYPTED_PROPERTY));
        }
        if (StringUtils.isNotEmpty(properties.get(HTTP_PROXY_HOST))) {
            adapterProperties.put(HTTP_PROXY_HOST, properties.get(HTTP_PROXY_HOST));
        }
        if (StringUtils.isNotEmpty(properties.get(HTTP_PROXY_PORT))) {
            adapterProperties.put(HTTP_PROXY_PORT, properties.get(HTTP_PROXY_PORT));
        }

        // Thread Pool Related Properties.
        if (StringUtils.isNotEmpty(properties.get(MIN_THREAD))) {
            adapterProperties.put(MIN_THREAD, properties.get(MIN_THREAD));
        }
        if (StringUtils.isNotEmpty(properties.get(MAX_THREAD))) {
            adapterProperties.put(MAX_THREAD, properties.get(MAX_THREAD));
        }
        if (StringUtils.isNotEmpty(properties.get(KEEP_ALIVE_TIME_IN_MILLIS))) {
            adapterProperties.put(KEEP_ALIVE_TIME_IN_MILLIS, properties.get(KEEP_ALIVE_TIME_IN_MILLIS));
        }
        if (StringUtils.isNotEmpty(properties.get(JOB_QUEUE_SIZE))) {
            adapterProperties.put(JOB_QUEUE_SIZE, properties.get(JOB_QUEUE_SIZE));
        }

        // HTTP Client Pool Related Properties.
        if (StringUtils.isNotEmpty(properties.get(DEFAULT_MAX_CONNECTIONS_PER_HOST))) {
            adapterProperties.put(DEFAULT_MAX_CONNECTIONS_PER_HOST, properties.get(DEFAULT_MAX_CONNECTIONS_PER_HOST));
        }
        if (StringUtils.isNotEmpty(properties.get(MAX_TOTAL_CONNECTIONS))) {
            adapterProperties.put(MAX_TOTAL_CONNECTIONS, properties.get(MAX_TOTAL_CONNECTIONS));
        }

        // Add properties.
        for (Map.Entry<String, String> property : adapterProperties.entrySet()) {
            if (!PASSWORD_ENCRYPTED_ATTR_KEY.equals(property.getKey())) {
                Element adapterProperty = document.createElement(ADAPTER_PROPERTY);
                Attr attribute = document.createAttribute(ADAPTER_PROPERTY_NAME);
                attribute.setValue(property.getKey());
                if (HTTP_PASSWORD_PROPERTY.equals(property.getKey())) {
                    Attr encryptedAttribute = document.createAttribute(PASSWORD_ENCRYPTED_ATTR_KEY);
                    if (StringUtils.isEmpty(properties.get(PASSWORD_ENCRYPTED_PROPERTY))) {
                        encryptedAttribute.setValue("false");
                    } else {
                        encryptedAttribute.setValue(properties.get(PASSWORD_ENCRYPTED_PROPERTY));
                    }
                    adapterProperty.setAttributeNode(encryptedAttribute);
                }
                adapterProperty.setAttributeNode(attribute);
                adapterProperty.appendChild(document.createTextNode(property.getValue()));
                to.appendChild(adapterProperty);
            }
        }

        DOMSource xmlSource = new DOMSource(document);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Result outputTarget = new StreamResult(outputStream);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(xmlSource, outputTarget);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static String generateSmsSendAPIBody(String smsSendAPIBodyTemplate, SMSSenderAdd smsSenderAdd) {

        String inlineBody = smsSendAPIBodyTemplate;
        Map<String, String> inlineBodyProperties = new HashMap<>();
        /*
        key, secret, sender inputs or any property defined with key value starting from "body." are considered
        when generating the inline body of SMS publisher.
         */
        if (StringUtils.isNotEmpty(smsSenderAdd.getKey())) {
            inlineBodyProperties.put(KEY, smsSenderAdd.getKey());
        }
        if (StringUtils.isNotEmpty(smsSenderAdd.getSecret())) {
            inlineBodyProperties.put(SECRET, smsSenderAdd.getSecret());
        }
        if (StringUtils.isNotEmpty(smsSenderAdd.getSender())) {
            inlineBodyProperties.put(SENDER, smsSenderAdd.getSender());
        }
        if (CollectionUtils.isNotEmpty(smsSenderAdd.getProperties())) {
            smsSenderAdd.getProperties().stream()
                    .filter(property -> property.getKey().startsWith(INLINE_BODY_PARAM_PREFIX))
                    .forEach(property -> inlineBodyProperties.put(property.getKey(), property.getValue()));
        }
        for (Map.Entry property : inlineBodyProperties.entrySet()) {
            inlineBody =
                    inlineBody.replace(PLACEHOLDER_IDENTIFIER + property.getKey(), (CharSequence) property.getValue());
        }
        return inlineBody;
    }
}
