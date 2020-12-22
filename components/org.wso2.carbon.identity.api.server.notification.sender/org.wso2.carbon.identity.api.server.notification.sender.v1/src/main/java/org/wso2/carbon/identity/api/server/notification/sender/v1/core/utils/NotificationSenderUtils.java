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
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.CLIENT_HTTP_METHOD_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.CONSTANT_HTTP_POST;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.CUSTOM_MAPPING_KEY;
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
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.HTTP_URL_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.INLINE;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.INLINE_BODY_PARAM_PREFIX;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.INLINE_BODY_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.JSON;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.MAPPING;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.MAPPING_TYPE_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PLACEHOLDER_IDENTIFIER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PROCESSING_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PROPERTIES_TO_SKIP_AT_ADAPTER_CONFIG;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.PUBLISHER_NAME;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.ROOT_ELEMENT;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SECRET;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SENDER;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_FROM_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_HOST_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_PASSWORD_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_PORT_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.SMTP_USER_PROPERTY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.STATISTICS_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.STREAM_NAME;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.STREAM_VERSION;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.TEXT;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.TO;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.TRACE_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.XMLNS_KEY;
import static org.wso2.carbon.identity.api.server.notification.sender.common.NotificationSenderManagementConstants.XMLNS_VALUE;

/**
 * Util class for notification sender.
 */
public class NotificationSenderUtils {

    /**
     * Generate EmailPublisher.xml input stream.
     *
     * @param emailSenderAdd Email sender post body.
     * @return Input stream of the EmailPublisher.
     * @throws ParserConfigurationException Parser configuration exception.
     * @throws TransformerException         Transformer exception.
     */
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
        // Collect event publisher attributes to a map and set attributes to root element.
        addEventPublisherAttributes(emailSenderAdd, document, root);
        // Add 'From' element (event stream details) to event publisher.
        addFromElement(properties, document, root);
        // Add 'Mapping' element (output mapping details) to event publisher.
        addMappingElementToEmailEventPublisher(document, root);
        // Add 'To' element (event adapter details) to event publisher.
        addToElementToEmailEventPublisher(emailSenderAdd, properties, document, root);
        DOMSource xmlSource = new DOMSource(document);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Result outputTarget = new StreamResult(outputStream);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(xmlSource, outputTarget);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    /**
     * Generate SMSPublisher.xml input stream.
     *
     * @param smsSenderAdd SMS sender post body.
     * @return Input stream of the SMSPublisher.
     * @throws ParserConfigurationException Parser configuration exception.
     * @throws TransformerException         Transformer exception.
     */
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
        // Collect event publisher attributes to a map and set attributes to root element.
        addEventPublisherAttributes(smsSenderAdd, document, root);
        // Add 'From' element (event stream details) to event publisher.
        addFromElement(properties, document, root);
        // Add 'Mapping' element (output mapping details) to event publisher.
        addMappingElementToSMSEventPublisher(smsSenderAdd, properties, document, root);
        // Add 'To' element (event adapter details) to event publisher.
        addToElementToSMSEventPublisher(smsSenderAdd, properties, document, root);
        DOMSource xmlSource = new DOMSource(document);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Result outputTarget = new StreamResult(outputStream);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(xmlSource, outputTarget);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void addEventPublisherAttributes(EmailSenderAdd emailSenderAdd, Document document, Element root) {

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
    }

    private static void addEventPublisherAttributes(SMSSenderAdd smsSenderAdd, Document document, Element root) {

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
    }

    private static void addFromElement(Map<String, String> properties, Document document, Element root) {

        Element from = document.createElement(FROM);
        root.appendChild(from);
        // Set attributes to From element.
        Attr streamNameAttr = document.createAttribute(STREAM_NAME);
        streamNameAttr.setValue(properties.get(STREAM_NAME));
        from.setAttributeNode(streamNameAttr);
        Attr streamVersionAttr = document.createAttribute(STREAM_VERSION);
        streamVersionAttr.setValue(properties.get(STREAM_VERSION));
        from.setAttributeNode(streamVersionAttr);
    }

    private static void addMappingElementToEmailEventPublisher(Document document, Element root) {

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
    }

    private static void addMappingElementToSMSEventPublisher(SMSSenderAdd smsSenderAdd, Map<String, String> properties,
                                                             Document document,
                                                             Element root) {

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
        if (StringUtils.isNotEmpty(properties.get(INLINE_BODY_PROPERTY))) {
            smsSendAPIBody = properties.get(INLINE_BODY_PROPERTY);
        } else {
            String smsSendAPIBodyTemplate = NotificationSenderServiceHolder.getSmsProviderPayloadTemplateManager()
                    .getSMSProviderPayloadTemplateByProvider(smsSenderAdd.getProvider()).getBody();
            smsSendAPIBody = generateSmsSendAPIBody(smsSendAPIBodyTemplate, smsSenderAdd);
        }
        inline.appendChild(document.createTextNode(smsSendAPIBody));
        mapping.appendChild(inline);
    }

    private static void addToElementToEmailEventPublisher(EmailSenderAdd emailSenderAdd, Map<String, String> properties,
                                                          Document document,
                                                          Element root) {

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
        for (Map.Entry<String, String> property : properties.entrySet()) {
            if (!(PROPERTIES_TO_SKIP_AT_ADAPTER_CONFIG.contains(property.getKey()) ||
                    property.getKey().startsWith(INLINE_BODY_PARAM_PREFIX))) {
                adapterProperties.put(property.getKey(), property.getValue());
            }
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
    }

    private static void addToElementToSMSEventPublisher(SMSSenderAdd smsSenderAdd, Map<String, String> properties,
                                                        Document document,
                                                        Element root) {

        Element to = document.createElement(TO);
        root.appendChild(to);
        // Set attributes to to element.
        Attr eventAdapterTypeAttr = document.createAttribute(ADAPTER_TYPE_KEY);
        eventAdapterTypeAttr.setValue(ADAPTER_TYPE_HTTP_VALUE);
        to.setAttributeNode(eventAdapterTypeAttr);
        // Take adapter properties to a map.
        Map<String, String> adapterProperties = new HashMap<>();
        adapterProperties.put(HTTP_URL_PROPERTY, smsSenderAdd.getProviderURL());
        // Default client method is httpPost. Can be changed by configuring properties.
        adapterProperties.put(CLIENT_HTTP_METHOD_PROPERTY, CONSTANT_HTTP_POST);
        for (Map.Entry<String, String> property : properties.entrySet()) {
            if (!(PROPERTIES_TO_SKIP_AT_ADAPTER_CONFIG.contains(property.getKey()) ||
                    property.getKey().startsWith(INLINE_BODY_PARAM_PREFIX))) {
                adapterProperties.put(property.getKey(), property.getValue());
            }
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
    }

    /**
     * Generate SMS send API body using template and input params.
     *
     * @param smsSendAPIBodyTemplate SMS sender's send SMS API payload template.
     * @param smsSenderAdd           SMS sender post body.
     * @return Inline body for SMSPublisher.
     */
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
