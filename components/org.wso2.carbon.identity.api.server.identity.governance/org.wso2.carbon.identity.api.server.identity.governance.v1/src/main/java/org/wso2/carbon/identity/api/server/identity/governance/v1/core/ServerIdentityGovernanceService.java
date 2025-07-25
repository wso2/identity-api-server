/*
 * Copyright (c) 2019-2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.identity.governance.v1.core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.identity.governance.common.GovernanceConstants;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoriesRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoryConnectorsRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoryRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.MetaRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.MultipleConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PreferenceResp;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PreferenceSearchAttribute;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PropertyReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PropertyRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PropertyRevertReq;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.governance.IdentityGovernanceException;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.governance.bean.ConnectorConfig;
import org.wso2.carbon.identity.governance.exceptions.general.IdentityGovernanceClientException;
import org.wso2.carbon.identity.password.expiry.models.PasswordExpiryRule;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForBody;
import static org.wso2.carbon.identity.api.server.identity.governance.common.GovernanceConstants.ErrorMessage
        .ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.identity.governance.common.GovernanceConstants.ErrorMessage
        .ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.identity.governance.common.GovernanceConstants.ErrorMessage
        .ERROR_CODE_SORTING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.identity.governance.common.GovernanceConstants
        .IDENTITY_GOVERNANCE_PATH_COMPONENT;
import static org.wso2.carbon.identity.input.validation.mgt.utils.Constants.PW_POLICY_PATTERN;
import static org.wso2.carbon.identity.password.expiry.constants.PasswordPolicyConstants.PASSWORD_EXPIRY_RULES_PREFIX;

/**
 * Call internal osgi services to perform identity governance related operations.
 */
public class ServerIdentityGovernanceService {

    private final IdentityGovernanceService identityGovernanceService;
    private static final Log LOG = LogFactory.getLog(ServerIdentityGovernanceService.class);

    public ServerIdentityGovernanceService(IdentityGovernanceService identityGovernanceService) {

        this.identityGovernanceService = identityGovernanceService;
    }

    /**
     * Get all governance connector categories.
     *
     * @param limit  Page size.
     * @param offset Page start index.
     * @param filter Filter to search for categories.
     * @param sort   Sort order.
     * @return List of governance connector categories.
     */
    public List<CategoriesRes> getGovernanceConnectors(Integer limit, Integer offset, String filter, String sort) {

        handleNotImplementedCapabilities(limit, offset, filter, sort);

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieving governance connector categories for tenant: " + tenantDomain);
            }
            Map<String, List<ConnectorConfig>> connectorConfigs =
                    identityGovernanceService.getCategorizedConnectorListWithConfigs(tenantDomain);

            return buildConnectorCategoriesResDTOS(connectorConfigs);

        } catch (IdentityGovernanceException e) {
            LOG.error("Error occurred while retrieving governance connector categories.", e);
            GovernanceConstants.ErrorMessage errorEnum =
                    GovernanceConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CATEGORIES;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Get governance connector category.
     *
     * @param categoryId Governance connector category id.
     * @return List of governance connectors for the give id.
     */
    public CategoryRes getGovernanceConnectorCategory(String categoryId) {

        List<ConnectorRes> connectors = getGovernanceConnectorsByCategory(categoryId);
        String categoryName = new String(Base64.getUrlDecoder().decode(categoryId), StandardCharsets.UTF_8);
        CategoryRes category = new CategoryRes();
        category.setConnectors(connectors);
        category.setName(categoryName);

        return category;
    }

    /**
     * Get governance connector category.
     *
     * @param categoryId Governance connector category id.
     * @return List of governance connectors for the give id.
     */
    public List<ConnectorRes> getGovernanceConnectorsByCategory(String categoryId) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            String category = new String(Base64.getUrlDecoder().decode(categoryId), StandardCharsets.UTF_8);
            List<ConnectorConfig> connectorConfigs =
                    identityGovernanceService.getConnectorListWithConfigsByCategory(tenantDomain, category);

            if (connectorConfigs.size() == 0) {
                throw handleNotFoundError(categoryId, GovernanceConstants.ErrorMessage.ERROR_CODE_CATEGORY_NOT_FOUND);
            }

            return buildConnectorsResDTOS(connectorConfigs);

        } catch (IdentityGovernanceException e) {
            GovernanceConstants.ErrorMessage errorEnum =
                    GovernanceConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CATEGORY;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Get governance connector.
     *
     * @param categoryId  Governance connector category id.
     * @param connectorId Governance connector id.
     * @return Governance connectors for the give id.
     */
    public ConnectorRes getGovernanceConnector(String categoryId, String connectorId) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            String connectorName = new String(Base64.getUrlDecoder().decode(connectorId), StandardCharsets.UTF_8);
            ConnectorConfig connectorConfig =
                    identityGovernanceService.getConnectorWithConfigs(tenantDomain, connectorName);
            if (connectorConfig == null) {
                throw handleNotFoundError(connectorId, GovernanceConstants.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND);
            }
            String categoryIdFound = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(connectorConfig.getCategory().getBytes(StandardCharsets.UTF_8));
            if (!categoryId.equals(categoryIdFound)) {
                throw handleNotFoundError(connectorId, GovernanceConstants.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND);
            }

            return buildConnectorResDTO(connectorConfig);

        } catch (IdentityGovernanceException e) {
            GovernanceConstants.ErrorMessage errorEnum =
                    GovernanceConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CONNECTOR;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Get governance connector properties according to the search attribute.
     *
     * @param preferenceSearchAttribute Governance connector details.
     * @return Governance connector properties for the given connector or properties.
     */
    public List<PreferenceResp> getConfigPreference(List<PreferenceSearchAttribute> preferenceSearchAttribute) {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        List<PreferenceResp> preferenceRespList = new ArrayList<>();
        for (PreferenceSearchAttribute prefSearchAttr : preferenceSearchAttribute) {
            String connectorName = prefSearchAttr.getConnectorName();
            List<String> expectedProperties = prefSearchAttr.getProperties();
            Property[] properties;
            try {
                ConnectorConfig connectorConfig = identityGovernanceService.getConnectorWithConfigs(tenantDomain,
                        connectorName);
                if (connectorConfig == null) {
                    throw handleBadRequestError(GovernanceConstants.ErrorMessage.ERROR_CODE_INCORRECT_CONNECTOR_NAME,
                            connectorName);
                }
                properties = connectorConfig.getProperties();
                PreferenceResp preferenceResp =
                        buildPreferenceRespDTO(connectorName, properties, expectedProperties);
                preferenceRespList.add(preferenceResp);
            } catch (IdentityGovernanceException e) {
                GovernanceConstants.ErrorMessage errorEnum =
                        GovernanceConstants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CONNECTOR_PREFERENCES;
                Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
                throw handleException(e, errorEnum, status);
            }
        }
        return preferenceRespList;
    }

    private PreferenceResp buildPreferenceRespDTO(String connectorName, Property[] properties,
                                                  List<String> expectedProperties) {

        PreferenceResp preferenceResp = new PreferenceResp();
        List<PropertyReq> propertyReqList = buildPropertyReqDTO(properties, expectedProperties);
        preferenceResp.setProperties(propertyReqList);
        preferenceResp.setConnectorName(connectorName);

        return preferenceResp;
    }

    private List<PropertyReq> buildPropertyReqDTO(Property[] properties, List<String> expectedProperties) {

        if (expectedProperties != null) {
            return buildPropertyReqForExpectedAttributes(properties, expectedProperties);
        }
        return buildPropertyReqForAllProperties(properties);

    }

    private List<PropertyReq> buildPropertyReqForAllProperties(Property[] properties) {

        List<PropertyReq> propertyReqList = new ArrayList<>();
        for (Property property : properties) {
            if (property.isConfidential()) {
                continue;
            }
            createPropertyRequest(propertyReqList, property);
        }
        return propertyReqList;
    }

    private List<PropertyReq> buildPropertyReqForExpectedAttributes(Property[] properties,
                                                                    List<String> expectedProperties) {

        List<PropertyReq> propertyReqList = new ArrayList<>();
        Map<String, Property> propertyMap = new HashMap<>();
        for (Property property : properties) {
            propertyMap.put(property.getName(), property);
        }
        for (String expectedProperty : expectedProperties) {
            Property property = propertyMap.get(expectedProperty);
            if (property == null || property.isConfidential()) {
                throw handleException(new IdentityGovernanceException(GovernanceConstants.ErrorMessage
                                .ERROR_CODE_UNSUPPORTED_PROPERTY_NAME.getMessage()), GovernanceConstants.
                                ErrorMessage.ERROR_CODE_UNSUPPORTED_PROPERTY_NAME, Response.Status.BAD_REQUEST,
                        expectedProperty);
            }
            createPropertyRequest(propertyReqList, property);
        }
        return propertyReqList;
    }

    private void createPropertyRequest(List<PropertyReq> propertyReqList, Property property) {

        PropertyReq propertyReq = new PropertyReq();
        propertyReq.setName(property.getName());
        propertyReq.setValue(property.getValue());
        propertyReqList.add(propertyReq);
    }

    /**
     * Update governance connector property.
     *
     * @param categoryId          Governance connector category id.
     * @param connectorId         Governance connector id.
     * @param governanceConnector Connector property to update.
     */
    public void updateGovernanceConnectorProperty(String categoryId, String connectorId,
                                                  ConnectorsPatchReq governanceConnector) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Updating governance connector property for tenant: " + tenantDomain + 
                        ", categoryId: " + categoryId + ", connectorId: " + connectorId);
            }

            ConnectorRes connector = getGovernanceConnector(categoryId, connectorId);
            if (connector == null) {
                throw handleNotFoundError(connectorId, GovernanceConstants.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND);
            }

            Map<String, String> configurationDetails = new HashMap<>();
            for (PropertyReq propertyReqDTO : governanceConnector.getProperties()) {
                configurationDetails.put(propertyReqDTO.getName(), propertyReqDTO.getValue());
                if (StringUtils.startsWith(propertyReqDTO.getName(), PASSWORD_EXPIRY_RULES_PREFIX) &&
                        StringUtils.isNotBlank(propertyReqDTO.getValue()) &&
                        !isValidPasswordExpiryRule(propertyReqDTO.getValue())) {
                    LOG.warn("Invalid password expiry rule provided: " + propertyReqDTO.getValue());
                    throw handleBadRequestError(
                            GovernanceConstants.ErrorMessage.ERROR_CODE_INVALID_PASSWORD_EXPIRY_RULE,
                            propertyReqDTO.getValue());
                }
                if (StringUtils.equals(propertyReqDTO.getName(), PW_POLICY_PATTERN) &&
                        StringUtils.isNotBlank(propertyReqDTO.getValue()) &&
                        !isValidPasswordPatternRegex(propertyReqDTO.getValue())) {
                    LOG.warn("Invalid password pattern regex provided for property: " + propertyReqDTO.getName());
                    throw handleBadRequestError(
                            GovernanceConstants.ErrorMessage.ERROR_CODE_INVALID_PASSWORD_PATTERN_REGEX);
                }
            }
            identityGovernanceService.updateConfiguration(tenantDomain, configurationDetails);
            LOG.info("Successfully updated governance connector property for tenant: " + tenantDomain + 
                    ", categoryId: " + categoryId + ", connectorId: " + connectorId);
        } catch (IdentityGovernanceClientException e) {
            LOG.error("Invalid connector configuration provided.", e);
            throw handleBadRequestError(GovernanceConstants.ErrorMessage.ERROR_CODE_INVALID_CONNECTOR_CONFIGURATION,
                    e.getMessage());
        } catch (IdentityGovernanceException e) {
            LOG.error("Error occurred while updating governance connector property.", e);
            GovernanceConstants.ErrorMessage errorEnum =
                    GovernanceConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CONNECTOR_PROPERTY;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     *
     * Update multiple governance connector properties.
     *
     * @param categoryId                 Governance connector category id.
     * @param multipleConnectorsPatchReq Governance connectors and properties to update.
     */
    public void updateGovernanceConnectorProperties(String categoryId,
                                                    MultipleConnectorsPatchReq multipleConnectorsPatchReq) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Updating multiple governance connector properties for tenant: " + tenantDomain + 
                        ", categoryId: " + categoryId);
            }

            // Check whether the category ID exists.
            CategoryRes category = getGovernanceConnectorCategory(categoryId);
            if (category == null) {
                throw handleNotFoundError(categoryId, GovernanceConstants.ErrorMessage.ERROR_CODE_CATEGORY_NOT_FOUND);
            }

            Map<String, String> configurationDetails = new HashMap<>();

            // Loop through each connector in the request and add the properties to be updated to a hashmap.
            for (ConnectorReq connectorReq : multipleConnectorsPatchReq.getConnectors()) {
                String connectorId = connectorReq.getId();

                // Check whether the connector ID exists.
                ConnectorRes connector = getGovernanceConnector(categoryId, connectorId);
                if (connector == null) {
                    throw handleNotFoundError(connectorId,
                            GovernanceConstants.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND);
                }

                // Add properties of the connector to be updated to the configurationDetails hashmap.
                for (PropertyReq propertyReqDTO : connectorReq.getProperties()) {
                    if (StringUtils.startsWith(propertyReqDTO.getName(), PASSWORD_EXPIRY_RULES_PREFIX) &&
                            StringUtils.isNotBlank(propertyReqDTO.getValue()) &&
                            !isValidPasswordExpiryRule(propertyReqDTO.getValue())) {
                        LOG.warn("Invalid password expiry rule provided: " + propertyReqDTO.getValue());
                        throw handleBadRequestError(
                                GovernanceConstants.ErrorMessage.ERROR_CODE_INVALID_PASSWORD_EXPIRY_RULE,
                                propertyReqDTO.getValue());
                    }
                    if (StringUtils.equals(propertyReqDTO.getName(), PW_POLICY_PATTERN) &&
                            StringUtils.isNotBlank(propertyReqDTO.getValue()) &&
                            !isValidPasswordPatternRegex(propertyReqDTO.getValue())) {
                        LOG.warn("Invalid password pattern regex provided for property: " + propertyReqDTO.getName());
                        throw handleBadRequestError(
                                GovernanceConstants.ErrorMessage.ERROR_CODE_INVALID_PASSWORD_PATTERN_REGEX);
                    }
                    configurationDetails.put(propertyReqDTO.getName(), propertyReqDTO.getValue());
                }
            }

            identityGovernanceService.updateConfiguration(tenantDomain, configurationDetails);
            LOG.info("Successfully updated multiple governance connector properties for tenant: " + tenantDomain + 
                    ", categoryId: " + categoryId);

        } catch (IdentityGovernanceException e) {
            LOG.error("Error occurred while updating multiple governance connector properties.", e);
            GovernanceConstants.ErrorMessage errorEnum =
                    GovernanceConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CONNECTOR_PROPERTY;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Revert provided properties of a governance connector.
     *
     * @param categoryId        Governance connector category id.
     * @param connectorId       Governance connector id.
     * @param propertyRevertReq Properties to revert.
     */
    public void revertGovernanceConnectorProperties(String categoryId, String connectorId,
                                                    PropertyRevertReq propertyRevertReq) {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Reverting governance connector properties for tenant: " + tenantDomain + 
                        ", categoryId: " + categoryId + ", connectorId: " + connectorId);
            }
            ConnectorRes connector = getGovernanceConnector(categoryId, connectorId);

            // Throw an error if propertyRevertReq contain properties that are not in the connector.
            for (String propertyName : propertyRevertReq.getProperties()) {
                if (connector.getProperties().stream().noneMatch(
                        property -> property.getName().equals(propertyName))) {
                    LOG.warn("Property not found in connector: " + propertyName);
                    throw handleBadRequestError(GovernanceConstants.ErrorMessage.ERROR_CODE_PROPERTY_NOT_FOUND,
                            propertyName, connector.getFriendlyName());
                }
            }

            identityGovernanceService.deleteConfiguration(propertyRevertReq.getProperties(), tenantDomain);
            LOG.info("Successfully reverted governance connector properties for tenant: " + tenantDomain + 
                    ", categoryId: " + categoryId + ", connectorId: " + connectorId);
        } catch (IdentityGovernanceException e) {
            LOG.error("Error occurred while reverting governance connector properties.", e);
            GovernanceConstants.ErrorMessage errorEnum =
                    GovernanceConstants.ErrorMessage.ERROR_CODE_ERROR_REVERTING_CONNECTOR_PROPERTY;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    private APIError handleException(Exception e, GovernanceConstants.ErrorMessage errorEnum, Response.Status status,
                                     String... data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(LOG, e, buildErrorDescription(errorEnum,
                data));
        return new APIError(status, errorResponse);
    }

    private APIError handleBadRequestError(GovernanceConstants.ErrorMessage errorMessage, String... data) {

        Response.Status status = Response.Status.BAD_REQUEST;
        ErrorResponse errorResponse = getErrorBuilder(errorMessage, data).build(LOG,
                buildErrorDescription(errorMessage, data));
        return new APIError(status, errorResponse);
    }

    private List<CategoriesRes> buildConnectorCategoriesResDTOS(
            Map<String, List<ConnectorConfig>> connectorConfigs) {

        List<CategoriesRes> categories = new ArrayList<>();

        for (Map.Entry<String, List<ConnectorConfig>> category : connectorConfigs.entrySet()) {

            CategoriesRes categoriesRes = new CategoriesRes();
            categoriesRes.setName(category.getKey());
            String categoryId = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(category.getKey().getBytes(StandardCharsets.UTF_8));
            categoriesRes.setId(categoryId);
            URI categoryLocation =
                    buildURIForBody(String.format(V1_API_PATH_COMPONENT + IDENTITY_GOVERNANCE_PATH_COMPONENT + "/%s",
                            categoryId));
            categoriesRes.setSelf(categoryLocation.toString());

            List<CategoryConnectorsRes> connectors = buildCategoryConnectorsResDTOS(categoryId, category.getValue());
            categoriesRes.setConnectors(connectors);
            categories.add(categoriesRes);
        }

        return categories;
    }

    private List<ConnectorRes> buildConnectorsResDTOS(List<ConnectorConfig> connectorConfigList) {

        List<ConnectorRes> connectors = new ArrayList<>();
        for (ConnectorConfig connectorConfig : connectorConfigList) {
            ConnectorRes connectorResDTO = buildConnectorResDTO(connectorConfig);
            connectors.add(connectorResDTO);
        }
        return connectors;
    }

    private List<CategoryConnectorsRes> buildCategoryConnectorsResDTOS(String categoryId,
                                                                       List<ConnectorConfig> connectorConfigList) {

        List<CategoryConnectorsRes> connectors = new ArrayList<>();
        for (ConnectorConfig connectorConfig : connectorConfigList) {
            CategoryConnectorsRes connectorsResDTO = new CategoryConnectorsRes();
            String connectorId = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(connectorConfig.getName().getBytes(StandardCharsets.UTF_8));
            connectorsResDTO.setId(connectorId);
            URI connectorLocation =
                    buildURIForBody(String.format(V1_API_PATH_COMPONENT + IDENTITY_GOVERNANCE_PATH_COMPONENT + "/%s" +
                                    "/connectors/%s",
                            categoryId, connectorId));
            connectorsResDTO.setSelf(connectorLocation.toString());
            connectors.add(connectorsResDTO);
        }
        return connectors;
    }

    private ConnectorRes buildConnectorResDTO(ConnectorConfig connectorConfig) {

        ConnectorRes connectorsResDTO = new ConnectorRes();
        connectorsResDTO.setId(Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(connectorConfig.getName().getBytes(StandardCharsets.UTF_8)));
        connectorsResDTO.setName(connectorConfig.getName());
        connectorsResDTO.setFriendlyName(connectorConfig.getFriendlyName());
        connectorsResDTO.setCategory(connectorConfig.getCategory());
        connectorsResDTO.setSubCategory(connectorConfig.getSubCategory());
        connectorsResDTO.setOrder(connectorConfig.getOrder());

        List<PropertyRes> properties = new ArrayList<>();
        for (Property property : connectorConfig.getProperties()) {
            PropertyRes propertyRes = new PropertyRes();
            propertyRes.setName(property.getName());
            propertyRes.setValue(property.getValue());
            propertyRes.setDisplayName(property.getDisplayName());
            propertyRes.setDescription(property.getDescription() != null ? property.getDescription() : "");
            MetaRes metaRes = new MetaRes();
            metaRes.setType(property.getType());
            metaRes.setRegex(property.getRegex());
            metaRes.setGroupID(property.getGroupId());
            propertyRes.setMeta(metaRes);
            properties.add(propertyRes);
        }
        connectorsResDTO.setProperties(properties);
        return connectorsResDTO;
    }

    private ErrorResponse.Builder getErrorBuilder(GovernanceConstants.ErrorMessage errorMsg, String... data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(buildErrorDescription(errorMsg, data));
    }

    private String buildErrorDescription(GovernanceConstants.ErrorMessage errorEnum, String... data) {

        String errorDescription;

        if (ArrayUtils.isNotEmpty(data)) {
            errorDescription = String.format(errorEnum.getDescription(), data);
        } else {
            errorDescription = errorEnum.getDescription();
        }

        return errorDescription;
    }

    private void handleNotImplementedCapabilities(Integer limit, Integer offset, String filter,
                                                  String sort) {

        GovernanceConstants.ErrorMessage errorEnum = null;

        if (limit != null) {
            errorEnum = ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (offset != null) {
            errorEnum = ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
        } else if (filter != null) {
            errorEnum = ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
        } else if (sort != null) {
            errorEnum = ERROR_CODE_SORTING_NOT_IMPLEMENTED;
        }

        if (errorEnum != null) {
            ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(LOG, errorEnum.getDescription());
            Response.Status status = Response.Status.NOT_IMPLEMENTED;

            throw new APIError(status, errorResponse);
        }
    }

    private APIError handleNotFoundError(String resourceId,
                                         GovernanceConstants.ErrorMessage errorMessage) {

        Response.Status status = Response.Status.NOT_FOUND;
        ErrorResponse errorResponse =
                getErrorBuilder(errorMessage, resourceId).build(LOG, errorMessage.getDescription());

        return new APIError(status, errorResponse);
    }

    /**
     * Validate the password expiry rule.
     *
     * @param rule Password expiry rule.
     * @throws APIError if the rule is invalid.
     */
    private boolean isValidPasswordExpiryRule(String rule) throws APIError {

        try {
            new PasswordExpiryRule(rule);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Validate the password pattern regex.
     *
     * @param passwordPatternRegex Password pattern regex.
     * @return true if the regex is valid, false otherwise.
     */
    private boolean isValidPasswordPatternRegex(String passwordPatternRegex) {

        try {
            Pattern.compile(passwordPatternRegex);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
