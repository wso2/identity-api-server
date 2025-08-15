/*
 * Copyright (c) 2021-2025, WSO2 Inc. (http://www.wso2.com).
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
import org.wso2.carbon.identity.scim2.common.utils.SCIMCommonUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Read and parse schema configurations in schemas.xml file.
 */
public class SchemaConfigParser {

    private static final String CORE_SCHEMA = "urn:ietf:params:scim:schemas:core:2.0";
    private static final String USER_SCHEMA = "urn:ietf:params:scim:schemas:core:2.0:User";
    private static final String ENTERPRISE_USER_SCHEMA = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User";
    private static final String SYSTEM_SCHEMA =  "urn:scim:wso2:schema";

    private static final Log log = LogFactory.getLog(SchemaConfigParser.class);
    private static volatile SchemaConfigParser schemaConfigParser;

    private SchemaConfigParser() {}

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

        List<ExternalClaim> coreSchemaClaims = null;
        List<ExternalClaim> userSchemaClaims = null;
        List<ExternalClaim> enterpriseUserSchemaClaims = null;
        List<ExternalClaim> systemSchemaClaims = null;
        List<ExternalClaim> customSchemaClaims = null;

        String tenantDomain = SCIMCommonUtils.getTenantDomainFromContext();
        String customSchemaURI = SCIMCommonUtils.getCustomSchemaURI();
        try {
            // Load the external claims for each schema type.
            coreSchemaClaims = Utils.getExternalClaims(CORE_SCHEMA, tenantDomain);
            userSchemaClaims = Utils.getExternalClaims(USER_SCHEMA, tenantDomain);
            enterpriseUserSchemaClaims = Utils.getExternalClaims(ENTERPRISE_USER_SCHEMA, tenantDomain);
            systemSchemaClaims = Utils.getExternalClaims(SYSTEM_SCHEMA, tenantDomain);
            customSchemaClaims = Utils.getExternalClaims(customSchemaURI, tenantDomain);

        } catch (ClaimMetadataException e) {
            log.error("Error while retrieving external claims for schemas.", e);
        }

        Map<String, List<String>> schemaMap = buildSchemasConfiguration(coreSchemaClaims, CORE_SCHEMA);
        schemaMap.putAll(buildSchemasConfiguration(userSchemaClaims, USER_SCHEMA));
        schemaMap.putAll(buildSchemasConfiguration(enterpriseUserSchemaClaims, ENTERPRISE_USER_SCHEMA));
        schemaMap.putAll(buildSchemasConfiguration(systemSchemaClaims, SYSTEM_SCHEMA));
        schemaMap.putAll(buildSchemasConfiguration(customSchemaClaims, customSchemaURI));

        if (schemaMap.isEmpty()) {
            schemaMap = Collections.emptyMap();
        }
        return schemaMap;
    }

    private Map<String, List<String>> buildSchemasConfiguration(List<ExternalClaim> externalClaims,
                                                                          String schemaType) {

        Map<String, List<String>> dataMap = new HashMap<>();
        if (externalClaims == null || externalClaims.isEmpty()) {

            dataMap.put(schemaType, Collections.emptyList());
            return dataMap;
        }

        List<String> attributeList = externalClaims.stream()
                .map(ExternalClaim::getClaimURI)
                .collect(Collectors.toList());
        dataMap.put(schemaType, attributeList);
        return dataMap;
    }
}
