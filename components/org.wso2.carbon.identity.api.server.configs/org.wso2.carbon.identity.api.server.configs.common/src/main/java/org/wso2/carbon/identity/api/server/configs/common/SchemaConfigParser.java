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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.claim.metadata.mgt.exception.ClaimMetadataException;
import org.wso2.carbon.identity.claim.metadata.mgt.model.ExternalClaim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Read and parse schema configurations in schemas.xml file.
 */
public class SchemaConfigParser {

    private static final String CORE_SCHEMA = "urn:ietf:params:scim:schemas:core:2.0";
    private static final String USER_SCHEMA = "urn:ietf:params:scim:schemas:core:2.0:User";
    private static final String ENTERPRISE_USER_SCHEMA = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User";
    private static final String SYSTEM_SCHEMA =  "urn:scim:wso2:schema";
    private static final String CUSTOM_SCHEMA = "urn:scim:schemas:extension:custom:User";

    private static final Log log = LogFactory.getLog(SchemaConfigParser.class);
    private static volatile SchemaConfigParser schemaConfigParser;

    private Map<String, List<String>> schemaMap;
    private Set<String> customSchemaAttributes;

    private SchemaConfigParser() {

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

        // Check if the custom shema attributes are updated.
        try {
            List<ExternalClaim> customSchemaClaims = ConfigsServiceHolder.getExternalClaims(CUSTOM_SCHEMA);
            Set<String> newCustomSchemaAttributes = customSchemaClaims.stream()
                        .map(ExternalClaim::getClaimURI)
                        .collect(Collectors.toSet());

            // If the custom schema attributes are not equal to the existing ones, update the schema map.
            if (customSchemaAttributes == null || !customSchemaAttributes.equals(newCustomSchemaAttributes)) {
                customSchemaAttributes = newCustomSchemaAttributes;
                updateSchemaMap(CUSTOM_SCHEMA, new ArrayList<>(customSchemaAttributes));
            }

        } catch (ClaimMetadataException e) {
            log.error("Error while retrieving external claims for custom schema.", e);
        }

        return schemaMap;
    }

    private void buildConfiguration() {

        List<ExternalClaim> coreSchemaClaims = null;
        List<ExternalClaim> userSchemaClaims = null;
        List<ExternalClaim> enterpriseUserSchemaClaims = null;
        List<ExternalClaim> systemSchemaClaims = null;
        List<ExternalClaim> customSchemaClaims = null;
        try {
            // Load the external claims for each schema type.
            coreSchemaClaims = ConfigsServiceHolder.getExternalClaims(CORE_SCHEMA);
            userSchemaClaims = ConfigsServiceHolder.getExternalClaims(USER_SCHEMA);
            enterpriseUserSchemaClaims = ConfigsServiceHolder.getExternalClaims(ENTERPRISE_USER_SCHEMA);
            systemSchemaClaims = ConfigsServiceHolder.getExternalClaims(SYSTEM_SCHEMA);
            customSchemaClaims = ConfigsServiceHolder.getExternalClaims(CUSTOM_SCHEMA);

            // Update the customSchemaAttributes if custom schema claims are available.
            if (customSchemaClaims != null && !customSchemaClaims.isEmpty()) {
                customSchemaAttributes = customSchemaClaims.stream()
                        .map(ExternalClaim::getClaimURI)
                        .collect(Collectors.toSet());
            }
        } catch (ClaimMetadataException e) {
            log.error("Error while retrieving external claims for schemas.", e);
        }

        Map<String, List<String>> schemaMap = buildSchemasConfiguration(coreSchemaClaims, CORE_SCHEMA);
        schemaMap.putAll(buildSchemasConfiguration(userSchemaClaims, USER_SCHEMA));
        schemaMap.putAll(buildSchemasConfiguration(enterpriseUserSchemaClaims, ENTERPRISE_USER_SCHEMA));
        schemaMap.putAll(buildSchemasConfiguration(systemSchemaClaims, SYSTEM_SCHEMA));
        schemaMap.putAll(buildSchemasConfiguration(customSchemaClaims, CUSTOM_SCHEMA));

        if (schemaMap.isEmpty()) {
            this.schemaMap = Collections.EMPTY_MAP;
            return;
        }

        this.schemaMap = schemaMap;
        this.schemaMap = Collections.unmodifiableMap(this.schemaMap);
    }

    private Map<String, List<String>> buildSchemasConfiguration(List<ExternalClaim> externalClaims,
                                                                          String schemaType) {

        Map<String, List<String>> dataMap = new HashMap<>();
        if (externalClaims == null || externalClaims.isEmpty()) {

            return dataMap;
        }

        List<String> attributeList = externalClaims.stream()
                .map(ExternalClaim::getClaimURI)
                .collect(Collectors.toList());
        dataMap.put(schemaType, attributeList);
        return dataMap;
    }

    private void updateSchemaMap(String schemaType, List<String> attributeList) {

        if (this.schemaMap == null) {
            this.schemaMap = new HashMap<>();
        }
        this.schemaMap.put(schemaType, attributeList);
    }
}
