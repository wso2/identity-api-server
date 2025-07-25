/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.configs.common;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.base.IdentityRuntimeException;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

/**
 * Read and parse schema configurations in schemas.xml file.
 */
public class SchemaConfigParser {

    private static final String SCHEMA_FILE_NAME = "schemas.xml";
    private static final String SCHEMAS_NAMESPACE = "http://wso2.org/projects/carbon/carbon.xml";
    private static final String DEFAULT_SCHEMA_CONFIG = "DefaultSchema";
    private static final String ADD_SCHEMA_CONFIG = "AddSchema";
    private static final String REMOVE_SCHEMA_CONFIG = "RemoveSchema";
    private static final String SCHEMAS_CONFIG = "Schemas";
    private static final String SCHEMA_CONFIG = "Schema";
    private static final String SCHEMA_ID_CONFIG = "id";
    private static final String ATTRIBUTE_CONFIG = "Attribute";

    private static final Log log = LogFactory.getLog(SchemaConfigParser.class);
    private static volatile SchemaConfigParser schemaConfigParser;

    private final String schemasFilePath;
    private Map<String, List<String>> defaultSchemaMap;

    private SchemaConfigParser() {

        schemasFilePath = IdentityUtil.getIdentityConfigDirPath() + File.separator + SCHEMA_FILE_NAME;
        if (log.isDebugEnabled()) {
            log.debug("Initializing SchemaConfigParser with schemas file path: " + schemasFilePath);
        }
        buildConfiguration();
    }

    public static SchemaConfigParser getInstance() {

        if (schemaConfigParser == null) {
            synchronized (SchemaConfigParser.class) {
                if (schemaConfigParser == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Creating new SchemaConfigParser instance.");
                    }
                    schemaConfigParser = new SchemaConfigParser();
                }
            }
        }
        return schemaConfigParser;
    }

    /**
     * Return Schemas supported by the server.
     *
     * @return Schema Map.
     */
    public Map<String, List<String>> getSchemaMap() {

        return defaultSchemaMap;
    }

    private void buildConfiguration() {

        File schemaFile = new File(schemasFilePath);
        if (!schemaFile.exists()) {
            log.warn("Schema configuration file not found at path: " + schemasFilePath + 
                    ". Using empty schema configuration.");
            defaultSchemaMap = Collections.EMPTY_MAP;
            return;
        }

        try (InputStream inputStream = new FileInputStream(schemaFile)) {
            Optional<Map<String, List<String>>> schemaMap;
            Optional<Map<String, List<String>>> addToSchemaMap;
            Optional<Map<String, List<String>>> removeFromMap;
            StAXOMBuilder builder = new StAXOMBuilder(inputStream);
            if (log.isDebugEnabled()) {
                log.debug("Building schema configuration from file: " + schemasFilePath);
            }
            schemaMap = buildSchemasConfiguration(builder, DEFAULT_SCHEMA_CONFIG);
            addToSchemaMap = buildSchemasConfiguration(builder, ADD_SCHEMA_CONFIG);
            removeFromMap = buildSchemasConfiguration(builder, REMOVE_SCHEMA_CONFIG);
            if (!schemaMap.isPresent()) {
                log.warn("No default schema configuration found. Using empty schema map.");
                defaultSchemaMap = Collections.EMPTY_MAP;
                return;
            }
            defaultSchemaMap = schemaMap.get();
            addToSchemaMap.ifPresent(stringListMap -> stringListMap.forEach((key, values) -> {
                if (defaultSchemaMap.containsKey(key)) {
                    defaultSchemaMap.get(key).addAll(values);
                    List<String> uniqueAttributes = defaultSchemaMap.get(key).stream()
                            .distinct().collect(Collectors.toList());
                    defaultSchemaMap.put(key, uniqueAttributes);
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Invalid configuration. Schema ID: " + key + " not available in '" +
                                DEFAULT_SCHEMA_CONFIG + "' of " + schemasFilePath);
                    }
                }
            }));
            removeFromMap.ifPresent(stringListMap -> stringListMap.forEach((key, values) -> {
                if (defaultSchemaMap.containsKey(key)) {
                    defaultSchemaMap.get(key).removeAll(values);
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Invalid configuration. Schema ID: " + key + " not available in '" +
                                DEFAULT_SCHEMA_CONFIG + "' of " + schemasFilePath);
                    }
                }
            }));

        } catch (IOException | XMLStreamException e) {
            log.error("Error occurred while reading schema configuration in path: " + schemasFilePath, e);
            throw IdentityRuntimeException.error("Error occurred while reading schema configuration in path: " +
                    schemasFilePath, e);
        }
        if (log.isDebugEnabled()) {
            log.debug("Schema configuration successfully loaded with " + 
                    (defaultSchemaMap != null ? defaultSchemaMap.size() : 0) + " schema entries.");
        }
        defaultSchemaMap = Collections.unmodifiableMap(defaultSchemaMap);
    }

    private Optional<Map<String, List<String>>> buildSchemasConfiguration(StAXOMBuilder builder,
                                                                                 String configName) {

        OMElement configSchema = builder.getDocumentElement().getFirstChildWithName(new QName(SCHEMAS_NAMESPACE,
                configName));
        if (configSchema == null) {
            if (log.isDebugEnabled()) {
                log.debug(configName + " not defined in file: " + schemasFilePath);
            }
            return Optional.empty();
        }
        OMElement schemas = configSchema.getFirstChildWithName(new QName(SCHEMAS_NAMESPACE, SCHEMAS_CONFIG));
        if (schemas == null) {
            if (log.isDebugEnabled()) {
                log.debug(SCHEMAS_CONFIG + " not defined for the element: " + configName + " in file: " +
                        schemasFilePath);
            }
            return Optional.empty();
        }
        Iterator schemaIterator = schemas.getChildrenWithLocalName(SCHEMA_CONFIG);
        if (schemaIterator == null) {
            if (log.isDebugEnabled()) {
                log.debug(SCHEMA_CONFIG + " not defined for the element: " + configName + "in file: " +
                        schemasFilePath);
            }
            return Optional.empty();
        }
        Map<String, List<String>> dataMap = new HashMap<>();
        while (schemaIterator.hasNext()) {
            OMElement schema = (OMElement) schemaIterator.next();
            String schemaId = schema.getAttributeValue(new QName("id"));
            if (StringUtils.isBlank(schemaId)) {
                log.error("Invalid configuration. '" +  SCHEMA_ID_CONFIG + "' attribute of a '" + SCHEMAS_CONFIG + 
                        "' element cannot be undefined in file: " + schemasFilePath);
                throw IdentityRuntimeException.error("Invalid configuration. '" +  SCHEMA_ID_CONFIG + "' attribute of" +
                        " a '" + SCHEMAS_CONFIG + "' element cannot be undefined in file: " + schemasFilePath);
            }
            Iterator attributes = schema.getChildrenWithLocalName(ATTRIBUTE_CONFIG);
            if (attributes == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Attributes not defined for the schema ID: '" + schemaId + "' under element: " +
                            configName + " in file: " + schemasFilePath);
                }
                continue;
            }
            List<String> attributeList = new ArrayList<>();
            while (attributes.hasNext()) {
                OMElement attribute = (OMElement) attributes.next();
                if (attribute != null && StringUtils.isNotBlank(attribute.getText())) {
                    attributeList.add(attribute.getText());
                }
            }
            dataMap.put(schemaId, attributeList);
        }
        return Optional.of(dataMap);
    }
}
