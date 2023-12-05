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

package org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.util;


import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.Constants;
import org.apache.xerces.util.SecurityManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.carbon.core.util.CryptoException;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.exception.OutputEventAdapterException;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.EventAdapterConstants;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.config.AdapterConfigs;
import org.wso2.carbon.identity.api.server.notification.sender.v1.core.internal.ds.OutputEventAdapterServiceValueHolder;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.securevault.SecretResolver;
import org.wso2.securevault.SecretResolverFactory;
import org.wso2.securevault.commons.MiscellaneousUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class contains utility methods used for event adapter configuration.
 */
public class EventAdapterConfigHelper {

    private static final Log log = LogFactory.getLog(EventAdapterConfigHelper.class);
    private static SecretResolver secretResolver;
    private static final int ENTITY_EXPANSION_LIMIT = 0;

    public static void secureResolveDocument(Document doc)
            throws OutputEventAdapterException {
        Element element = doc.getDocumentElement();
        if (element != null) {
            try {
                secureLoadElement(element);
            } catch (CryptoException e) {
                throw new OutputEventAdapterException("Error in secure load of global output " +
                        "event adapter properties: " + e.getMessage(), e);
            }
        }
    }

    public static void secureResolveOmElement(OMElement doc) throws OutputEventAdapterException {

        if (doc != null) {
            try {
                secretResolver = SecretResolverFactory.create(doc, true);
                secureLoadOMElement(doc);
            } catch (CryptoException e) {
                throw new OutputEventAdapterException("Error in secure load of global output event " +
                        "adapter properties: " + e.getMessage(), e);
            }
        }
    }

    private static void secureLoadOMElement(OMElement element) throws CryptoException {

        String alias = MiscellaneousUtil.getProtectedToken(element.getText());
        if (alias != null && !alias.isEmpty()) {
            element.setText(loadFromSecureVault(alias));
        } else {
            OMAttribute secureAttr = element.getAttribute(new QName(EventAdapterConstants.SECURE_VAULT_NS,
                    EventAdapterConstants.SECRET_ALIAS_ATTR_NAME));
            if (secureAttr != null) {
                element.setText(loadFromSecureVault(secureAttr.getAttributeValue()));
                element.removeAttribute(secureAttr);
            }
        }
        Iterator<OMElement> childNodes = element.getChildElements();
        while (childNodes.hasNext()) {
            OMElement tmpNode = childNodes.next();
            secureLoadOMElement(tmpNode);
        }
    }



    public static Document convertToDocument(File file) throws OutputEventAdapterException {
        DocumentBuilderFactory fac = getSecuredDocumentBuilder();
        fac.setNamespaceAware(true);
        try {
            return fac.newDocumentBuilder().parse(file);
        } catch (Exception e) {
            throw new OutputEventAdapterException("Error in creating an XML document from file: " +
                    e.getMessage(), e);
        }
    }

    public static OMElement convertToOmElement(File file) throws OutputEventAdapterException {

        try {
            StAXOMBuilder builder = new StAXOMBuilder(new FileInputStream(file));
            return builder.getDocumentElement();
        } catch (Exception e) {
            throw new OutputEventAdapterException("Error in creating an XML document from file: " +
                    e.getMessage(), e);
        }
    }

    private static DocumentBuilderFactory getSecuredDocumentBuilder() {

                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                dbf.setXIncludeAware(false);
                dbf.setExpandEntityReferences(false);
                try {
                        dbf.setFeature(Constants.SAX_FEATURE_PREFIX +
                                Constants.EXTERNAL_GENERAL_ENTITIES_FEATURE, false);
                        dbf.setFeature(Constants.SAX_FEATURE_PREFIX +
                                Constants.EXTERNAL_PARAMETER_ENTITIES_FEATURE, false);
                        dbf.setFeature(Constants.XERCES_FEATURE_PREFIX +
                                Constants.LOAD_EXTERNAL_DTD_FEATURE, false);
                    } catch (ParserConfigurationException e) {
                        log.error("Failed to load XML Processor Feature " + Constants.EXTERNAL_GENERAL_ENTITIES_FEATURE
                                + " or " + Constants.EXTERNAL_PARAMETER_ENTITIES_FEATURE + " or "
                                + Constants.LOAD_EXTERNAL_DTD_FEATURE);
                    }

                        SecurityManager securityManager = new SecurityManager();
                securityManager.setEntityExpansionLimit(ENTITY_EXPANSION_LIMIT);
                dbf.setAttribute(Constants.XERCES_PROPERTY_PREFIX + Constants.SECURITY_MANAGER_PROPERTY,
                        securityManager);

                        return dbf;
            }

    private static void secureLoadElement(Element element)
            throws CryptoException {

        Attr secureAttr = element.getAttributeNodeNS(EventAdapterConstants.SECURE_VAULT_NS,
                EventAdapterConstants.SECRET_ALIAS_ATTR_NAME);
        if (secureAttr != null) {
            element.setTextContent(loadFromSecureVault(secureAttr.getValue()));
            element.removeAttributeNode(secureAttr);
        }
        NodeList childNodes = element.getChildNodes();
        int count = childNodes.getLength();
        Node tmpNode;
        for (int i = 0; i < count; i++) {
            tmpNode = childNodes.item(i);
            if (tmpNode instanceof Element) {
                secureLoadElement((Element) tmpNode);
            }
        }
    }

    private static synchronized String loadFromSecureVault(String alias) {
        if (secretResolver == null) {
            secretResolver = SecretResolverFactory.create((OMElement) null, false);
            secretResolver.init(OutputEventAdapterServiceValueHolder.
                    getSecretCallbackHandlerService().getSecretCallbackHandler());
        }
        return secretResolver.resolve(alias);
    }

    public static AdapterConfigs loadGlobalConfigs() {

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
            return (AdapterConfigs) unmarshaller.unmarshal(globalConfigDoc.getXMLStreamReader());
        } catch (JAXBException e) {
            log.error("Error in loading " + EventAdapterConstants.GLOBAL_CONFIG_FILE_NAME + " from " + path + "," +
                    " hence Output Event Adapters will be running with default global configs.");
        } catch (OutputEventAdapterException e) {
            log.error("Error in converting " + EventAdapterConstants.GLOBAL_CONFIG_FILE_NAME + " to parsed document," +
                    " hence Output Event Adapters will be running with default global configs.");
        }
        return new AdapterConfigs();
    }
}
