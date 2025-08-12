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
import org.wso2.carbon.identity.claim.metadata.mgt.model.ExternalClaim;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.charon3.core.exceptions.CharonException;

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
    private static final String CORE_SCHEMA = "urn:ietf:params:scim:schemas:core:2.0";
    private static final String USER_SCHEMA = "urn:ietf:params:scim:schemas:core:2.0:User";
    private static final String ENTERPRISE_USER_SCHEMA = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User";
    private static final String SYSTEM_SCHEMA =  "urn:scim:wso2:schema";
    private static final String CUSTOM_SCHEMA = "urn:scim:schemas:extension:custom:User";

    private static final Log log = LogFactory.getLog(SchemaConfigParser.class);
    private static volatile SchemaConfigParser schemaConfigParser;

    private final String schemasFilePath;
    private Map<String, List<String>> defaultSchemaMap;

    private SchemaConfigParser() {

        schemasFilePath = IdentityUtil.getIdentityConfigDirPath() + File.separator + SCHEMA_FILE_NAME;
        buildConfiguration();
    }

    public static SchemaConfigParser getInstance() {

        if (schemaConfigParser == null) {
            synchronized (SchemaConfigParser.class) {
                if (schemaConfigParser == null) {
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

    private void buildConfiguration() throws CharonException {

        List<ExternalClaim> coreSchemaClaims = ConfigsServiceHolder.getExternalClaims(CORE_SCHEMA);
        List<ExternalClaim> userSchemaClaims = ConfigsServiceHolder.getExternalClaims(USER_SCHEMA);
        List<ExternalClaim> enterpriseUserSchemaClaims = ConfigsServiceHolder.getExternalClaims(ENTERPRISE_USER_SCHEMA);
        List<ExternalClaim> systemSchemaClaims = ConfigsServiceHolder.getExternalClaims(SYSTEM_SCHEMA);
        List<ExternalClaim> customSchemaClaims = ConfigsServiceHolder.getExternalClaims(CUSTOM_SCHEMA);

        Map<String, List<String>> schemaMap = buildSchemasConfiguration(coreSchemaClaims, CORE_SCHEMA);
        schemaMap.putAll(buildSchemasConfiguration(userSchemaClaims, USER_SCHEMA));
        schemaMap.putAll(buildSchemasConfiguration(enterpriseUserSchemaClaims, ENTERPRISE_USER_SCHEMA));
        schemaMap.putAll(buildSchemasConfiguration(systemSchemaClaims, SYSTEM_SCHEMA));
        schemaMap.putAll(buildSchemasConfiguration(customSchemaClaims, CUSTOM_SCHEMA));

        if (!schemaMap.isEmpty()) {
            defaultSchemaMap = Collections.EMPTY_MAP;
            return;
        }
        defaultSchemaMap = schemaMap;
        defaultSchemaMap = Collections.unmodifiableMap(defaultSchemaMap);
    }

    private Map<String, List<String>> buildSchemasConfiguration(List<ExternalClaim> externalClaims,
                                                                          String schemaType) {

        Map<String, List<String>> dataMap = new HashMap<>();
        List<String> attributeList = externalClaims.stream()
                .map(ExternalClaim::getClaimURI)
                .collect(Collectors.toList());
        dataMap.put(schemaType, attributeList);
        return dataMap;
    }
}
