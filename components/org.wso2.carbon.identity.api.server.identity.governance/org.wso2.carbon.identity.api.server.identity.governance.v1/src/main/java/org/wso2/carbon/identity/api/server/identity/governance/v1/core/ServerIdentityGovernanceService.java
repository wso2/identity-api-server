/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.identity.api.server.identity.governance.v1.core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.common.Util;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.identity.governance.v1.GovernanceConstant;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.CategoriesResDTO;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.CategoryConnectorsResDTO;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.ConnectorResDTO;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.ConnectorsPatchReqDTO;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.PropertyReqDTO;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.PropertyResDTO;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.governance.IdentityGovernanceException;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.governance.bean.ConnectorConfig;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURI;
import static org.wso2.carbon.identity.api.server.identity.governance.v1.GovernanceConstant.ErrorMessage.ERROR_CODE_FILTERING_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.identity.governance.v1.GovernanceConstant.ErrorMessage.ERROR_CODE_PAGINATION_NOT_IMPLEMENTED;
import static org.wso2.carbon.identity.api.server.identity.governance.v1.GovernanceConstant.ErrorMessage.ERROR_CODE_SORTING_NOT_IMPLEMENTED;

/**
 * Call internal osgi services to perform identity governance related operations.
 */
public class ServerIdentityGovernanceService {

    private static final Log LOG = LogFactory.getLog(ServerIdentityGovernanceService.class);
    public static final String IDENTITY_GOVERNANCE_PATH_COMPONENT = "/identity-governance";

    /**
     * Get all governance connector categories.
     *
     * @param limit  Page size.
     * @param offset Page start index.
     * @param filter Filter to search for categories.
     * @param sort   Sort order.
     * @return List of governance connector categories.
     */
    public List<CategoriesResDTO> getGovernanceConnectors(Integer limit, Integer offset, String filter, String sort) {

        handleNotImplementedCapabilities(limit, offset, filter, sort);

        try {
            IdentityGovernanceService identityGovernanceService = Util.getIdentityGovernanceService();
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            Map<String, List<ConnectorConfig>> connectorConfigs =
                    identityGovernanceService.getCategorizedConnectorListWithConfigs(tenantDomain);

            return buildConnectorCategoriesResDTOS(connectorConfigs);

        } catch (IdentityGovernanceException e) {
            GovernanceConstant.ErrorMessage errorEnum =
                    GovernanceConstant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CATEGORIES;
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
    public List<ConnectorResDTO> getGovernanceConnectorsByCategory(String categoryId) {

        try {
            IdentityGovernanceService identityGovernanceService = Util.getIdentityGovernanceService();
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            String category = new String(Base64.getUrlDecoder().decode(categoryId), StandardCharsets.UTF_8);
            List<ConnectorConfig> connectorConfigs =
                    identityGovernanceService.getConnectorListWithConfigsByCategory(tenantDomain, category);

            if (connectorConfigs.size() == 0) {
                throw handleNotFoundError(categoryId, GovernanceConstant.ErrorMessage.ERROR_CODE_CATEGORY_NOT_FOUND);
            }

            return buildConnectorsResDTOS(connectorConfigs);

        } catch (IdentityGovernanceException e) {
            GovernanceConstant.ErrorMessage errorEnum =
                    GovernanceConstant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CATEGORY;
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
    public ConnectorResDTO getGovernanceConnector(String categoryId, String connectorId) {

        try {
            IdentityGovernanceService identityGovernanceService = Util.getIdentityGovernanceService();
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            String connectorName = new String(Base64.getUrlDecoder().decode(connectorId), StandardCharsets.UTF_8);
            ConnectorConfig connectorConfig =
                    identityGovernanceService.getConnectorWithConfigs(tenantDomain, connectorName);
            if (connectorConfig == null) {
                throw handleNotFoundError(connectorId, GovernanceConstant.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND);
            }
            String categoryIdFound = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(connectorConfig.getCategory().getBytes(StandardCharsets.UTF_8));
            if (!categoryId.equals(categoryIdFound)) {
                throw handleNotFoundError(connectorId, GovernanceConstant.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND);
            }

            return buildConnectorResDTO(connectorConfig);

        } catch (IdentityGovernanceException e) {
            GovernanceConstant.ErrorMessage errorEnum =
                    GovernanceConstant.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CONNECTOR;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    /**
     * Update governance connector property.
     *
     * @param categoryId          Governance connector category id.
     * @param connectorId         Governance connector id.
     * @param governanceConnector Connector property to update.
     */
    public void updateGovernanceConnectorProperty(String categoryId, String connectorId,
                                                  ConnectorsPatchReqDTO governanceConnector) {

        try {
            IdentityGovernanceService identityGovernanceService = Util.getIdentityGovernanceService();
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();

            ConnectorResDTO connector = getGovernanceConnector(categoryId, connectorId);
            if (connector == null) {
                throw handleNotFoundError(connectorId, GovernanceConstant.ErrorMessage.ERROR_CODE_CONNECTOR_NOT_FOUND);
            }

            Map<String, String> configurationDetails = new HashMap<>();
            for (PropertyReqDTO propertyReqDTO : governanceConnector.getProperties()) {
                configurationDetails.put(propertyReqDTO.getName(), propertyReqDTO.getValue());
            }
            identityGovernanceService.updateConfiguration(tenantDomain, configurationDetails);

        } catch (IdentityGovernanceException e) {
            GovernanceConstant.ErrorMessage errorEnum =
                    GovernanceConstant.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CONNECTOR_PROPERTY;
            Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
            throw handleException(e, errorEnum, status);
        }
    }

    private APIError handleException(Exception e, GovernanceConstant.ErrorMessage errorEnum, Response.Status status) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(LOG, e, errorEnum.getDescription());
        return new APIError(status, errorResponse);
    }

    private List<CategoriesResDTO> buildConnectorCategoriesResDTOS(
            Map<String, List<ConnectorConfig>> connectorConfigs) {

        List<CategoriesResDTO> categories = new ArrayList<>();

        for (Map.Entry<String, List<ConnectorConfig>> category : connectorConfigs.entrySet()) {

            CategoriesResDTO categoriesRes = new CategoriesResDTO();
            categoriesRes.setName(category.getKey());
            String categoryId = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(category.getKey().getBytes(StandardCharsets.UTF_8));
            categoriesRes.setId(categoryId);
            URI categoryLocation =
                    buildURI(String.format(V1_API_PATH_COMPONENT + IDENTITY_GOVERNANCE_PATH_COMPONENT + "/%s",
                            categoryId));
            categoriesRes.setLocation(categoryLocation.toString());

            List<CategoryConnectorsResDTO> connectors = buildCategoryConnectorsResDTOS(categoryId, category.getValue());
            categoriesRes.setConnectors(connectors);
            categories.add(categoriesRes);
        }

        return categories;
    }

    private List<ConnectorResDTO> buildConnectorsResDTOS(List<ConnectorConfig> connectorConfigList) {

        List<ConnectorResDTO> connectors = new ArrayList<>();
        for (ConnectorConfig connectorConfig : connectorConfigList) {
            ConnectorResDTO connectorResDTO = buildConnectorResDTO(connectorConfig);
            connectors.add(connectorResDTO);
        }
        return connectors;
    }

    private List<CategoryConnectorsResDTO> buildCategoryConnectorsResDTOS(String categoryId,
                                                                          List<ConnectorConfig> connectorConfigList) {

        List<CategoryConnectorsResDTO> connectors = new ArrayList<>();
        for (ConnectorConfig connectorConfig : connectorConfigList) {
            CategoryConnectorsResDTO connectorsResDTO = new CategoryConnectorsResDTO();
            String connectorId = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(connectorConfig.getName().getBytes(StandardCharsets.UTF_8));
            connectorsResDTO.setId(connectorId);
            URI connectorLocation =
                    buildURI(String.format(V1_API_PATH_COMPONENT + IDENTITY_GOVERNANCE_PATH_COMPONENT + "/%s/%s",
                            categoryId, connectorId));
            connectorsResDTO.setLocation(connectorLocation.toString());
            connectors.add(connectorsResDTO);
        }
        return connectors;
    }

    private ConnectorResDTO buildConnectorResDTO(ConnectorConfig connectorConfig) {

        ConnectorResDTO connectorsResDTO = new ConnectorResDTO();
        connectorsResDTO.setId(Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(connectorConfig.getName().getBytes(StandardCharsets.UTF_8)));
        connectorsResDTO.setName(connectorConfig.getName());
        connectorsResDTO.setFriendlyName(connectorConfig.getFriendlyName());
        connectorsResDTO.setCategory(connectorConfig.getCategory());
        connectorsResDTO.setSubCategory(connectorConfig.getSubCategory());
        connectorsResDTO.setOrder(connectorConfig.getOrder());

        List<PropertyResDTO> properties = new ArrayList<>();
        for (Property property : connectorConfig.getProperties()) {
            PropertyResDTO propertyRes = new PropertyResDTO();
            propertyRes.setName(property.getName());
            propertyRes.setValue(property.getValue());
            propertyRes.setDisplayName(property.getDisplayName());
            propertyRes.setDescription(property.getDescription());
            properties.add(propertyRes);
        }

        connectorsResDTO.setProperties(properties);
        return connectorsResDTO;
    }

    private ErrorResponse.Builder getErrorBuilder(GovernanceConstant.ErrorMessage errorMsg, String... data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(buildErrorDescription(errorMsg, data));
    }

    private String buildErrorDescription(GovernanceConstant.ErrorMessage errorEnum, String... data) {

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

        GovernanceConstant.ErrorMessage errorEnum = null;

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
                                         GovernanceConstant.ErrorMessage errorMessage) {

        Response.Status status = Response.Status.NOT_FOUND;
        ErrorResponse errorResponse =
                getErrorBuilder(errorMessage, resourceId).build(LOG, errorMessage.getDescription());

        return new APIError(status, errorResponse);
    }
}
