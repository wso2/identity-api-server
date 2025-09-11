/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.selfservice.v1.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceMgtException;
import org.wso2.carbon.identity.api.resource.mgt.constant.APIResourceManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationManagementService;
import org.wso2.carbon.identity.api.server.application.management.v1.factories.ServerApplicationManagementServiceFactory;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.exceptions.SelfServiceMgtEndpointException;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.PropertyPatchReq;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.PropertyReq;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.PropertyRes;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.util.SelfServiceMgtConstants;
import org.wso2.carbon.identity.api.server.userstore.v1.core.ServerUserStoreService;
import org.wso2.carbon.identity.api.server.userstore.v1.factories.ServerUserStoreServiceFactory;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreReq;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.APIResource;
import org.wso2.carbon.identity.application.common.model.ApplicationBasicInfo;
import org.wso2.carbon.identity.application.common.model.AuthorizedAPI;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.Scope;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementService;
import org.wso2.carbon.identity.governance.IdentityGovernanceException;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.governance.bean.ConnectorConfig;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Call internal osgi services to perform self-service management related operations.
 */
public class SelfServiceMgtService {

    private final IdentityGovernanceService identityGovernanceService;
    private final ApplicationManagementService applicationManagementService;
    private final APIResourceManager apiResourceManager;
    private final AuthorizedAPIManagementService authorizedAPIManagementService;

    private static final Log LOG = LogFactory.getLog(SelfServiceMgtService.class);

    public SelfServiceMgtService(IdentityGovernanceService identityGovernanceService,
                                 ApplicationManagementService applicationManagementService,
                                 APIResourceManager apiResourceManager,
                                 AuthorizedAPIManagementService authorizedAPIManagementService) {

        this.identityGovernanceService = identityGovernanceService;
        this.applicationManagementService = applicationManagementService;
        this.apiResourceManager = apiResourceManager;
        this.authorizedAPIManagementService = authorizedAPIManagementService;
    }

    /**
     * Get organization governance configs.
     *
     * @return Organization governance configs.
     */
    public List<PropertyRes> getOrganizationGovernanceConfigs() {

        try {
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Retrieving organization governance configs for tenant domain: " + tenantDomain);
            }
            ConnectorConfig connectorConfig = identityGovernanceService.getConnectorWithConfigs(tenantDomain,
                    SelfServiceMgtConstants.SELF_SERVICE_GOVERNANCE_CONNECTOR);
            List<PropertyRes> properties = buildConnectorResDTO(connectorConfig);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved " + properties.size() +
                        " governance configurations for tenant: " + tenantDomain);
            }
            return properties;
        } catch (IdentityGovernanceException e) {
            LOG.error(SelfServiceMgtConstants.ErrorMessage.ERROR_RETRIEVING_SELF_SERVICE_CONFIG.getDescription(), e);
            throw new SelfServiceMgtEndpointException(Response.Status.INTERNAL_SERVER_ERROR,
                    getError(SelfServiceMgtConstants.ErrorMessage.ERROR_RETRIEVING_SELF_SERVICE_CONFIG.getCode(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_RETRIEVING_SELF_SERVICE_CONFIG.getMessage(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_RETRIEVING_SELF_SERVICE_CONFIG
                                    .getDescription()));
        }
    }

    /**
     * Update governance connector property.
     *
     * @param governanceConnector Connector property to update.
     */
    public void updateOrganizationGovernanceConfigs(PropertyPatchReq governanceConnector, Boolean enablePostListener) {

        try {
            Map<String, String> configurationDetails = new HashMap<>();
            for (PropertyReq propertyReqDTO : governanceConnector.getProperties()) {
                configurationDetails.put(propertyReqDTO.getName(), propertyReqDTO.getValue());
            }
            Map<String, String> copiedConfigurationDetails = new HashMap<>(configurationDetails);
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Updating " + configurationDetails.size() + " governance configurations for tenant: " +
                        tenantDomain);
            }
            identityGovernanceService.updateConfiguration(tenantDomain, configurationDetails);
            if (enablePostListener) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Executing post configuration update listeners for tenant: " + tenantDomain);
                }
                doPostConfigurationUpdate(copiedConfigurationDetails);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully updated governance configurations for tenant: " + tenantDomain);
            }
        } catch (Exception e) {
            LOG.error(SelfServiceMgtConstants.ErrorMessage.ERROR_UPDATING_SELF_SERVICE_CONFIG.getDescription(), e);
            throw new SelfServiceMgtEndpointException(Response.Status.INTERNAL_SERVER_ERROR,
                    getError(SelfServiceMgtConstants.ErrorMessage.ERROR_UPDATING_SELF_SERVICE_CONFIG.getCode(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_UPDATING_SELF_SERVICE_CONFIG.getMessage(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_UPDATING_SELF_SERVICE_CONFIG.getDescription()));
        }
    }

    private void doPostConfigurationUpdate(Map<String, String> configurationDetails) {

        processSelfServiceEnablement(configurationDetails);
        processEmailVerification(configurationDetails);
    }

    private void processSelfServiceEnablement(Map<String, String> configurationDetails) {

        boolean enableSelfService = Boolean.parseBoolean(configurationDetails
                .getOrDefault(SelfServiceMgtConstants.SELF_SERVICE_ENABLE_PROPERTY_NAME, "false"));

        boolean isEnableSelfServiceUpdated =
                configurationDetails.containsKey(SelfServiceMgtConstants.SELF_SERVICE_ENABLE_PROPERTY_NAME);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing self-service enablement. Enable: " + enableSelfService +
                    ", Updated: " + isEnableSelfServiceUpdated);
        }

        // Create or remove self-service application.
        if (isEnableSelfServiceUpdated) {
            if (enableSelfService) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Creating self-service system application.");
                }
                createSystemApplication();
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Deleting self-service system application.");
                }
                deleteSystemApplication();
            }
        }
    }

    private void processEmailVerification(Map<String, String> configurationDetails) {

        boolean enableAdminEmailVerification = Boolean.parseBoolean(configurationDetails
                .getOrDefault(SelfServiceMgtConstants.SELF_SERVICE_ADMIN_EMAIL_VERIFICATION_PROPERTY_NAME, "false"));
        boolean enableOnboardToSubOrganization = Boolean.parseBoolean(configurationDetails
                .getOrDefault(SelfServiceMgtConstants.SELF_SERVICE_ONBOARD_ADMIN_TO_SUB_ORG_PROPERTY_NAME, "false"));

        boolean isEmailVerificationUpdated = configurationDetails
                .containsKey(SelfServiceMgtConstants.SELF_SERVICE_ADMIN_EMAIL_VERIFICATION_PROPERTY_NAME);
        boolean isSubOrgOnboardUpdated = configurationDetails
                .containsKey(SelfServiceMgtConstants.SELF_SERVICE_ONBOARD_ADMIN_TO_SUB_ORG_PROPERTY_NAME);

        if (!isSubOrgOnboardUpdated && !isEmailVerificationUpdated) {
            return;
        }

        if (!isSubOrgOnboardUpdated) {
            // If sub-organization onboard value is not provided then fetch it from DB.
            try {
                enableOnboardToSubOrganization = Boolean.parseBoolean(getGovernanceConfigValue(
                        SelfServiceMgtConstants.SELF_SERVICE_ONBOARD_ADMIN_TO_SUB_ORG_PROPERTY_NAME));
            } catch (IdentityGovernanceException e) {
                enableOnboardToSubOrganization = false;
            }
        } else if (!isEmailVerificationUpdated) {
            // If enable email value is not provided then fetch it from DB.
            try {
                enableAdminEmailVerification = Boolean.parseBoolean(getGovernanceConfigValue(
                        SelfServiceMgtConstants.SELF_SERVICE_ADMIN_EMAIL_VERIFICATION_PROPERTY_NAME));
            } catch (IdentityGovernanceException e) {
                enableAdminEmailVerification = false;
            }
        }

        if (enableOnboardToSubOrganization && enableAdminEmailVerification) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Onboarding lite user store as both sub-org onboard and email verification are enabled.");
            }
            onboardLiteUserStore();
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Removing lite user store as conditions are not met. Sub-org onboard: " +
                        enableOnboardToSubOrganization + ", Email verification: " + enableAdminEmailVerification);
            }
            removeLiteUserStore();
        }
    }

    private void removeLiteUserStore() {

        String userStoreName = getConfigProperty(SelfServiceMgtConstants.LITE_USER_USER_STORE_NAME);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Removing lite user store: " + userStoreName);
        }
        String domainId = new String(Base64.getEncoder().encode(userStoreName.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
        getServerUserStoreService().deleteUserStore(domainId);
        updateLiteUserStoreConnectorConfigs(false);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully removed lite user store: " + userStoreName);
        }
    }

    private void onboardLiteUserStore() {

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Starting lite user store onboarding process.");
            }
            InputStream inputStream = loadResourceFromClasspath(SelfServiceMgtConstants
                    .CREATE_LITE_USER_STORE_REQUEST_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(inputStream);
            // Update Lite user store configs.
            updateOnboardConfigValues(rootNode);
            // Convert updated JSON to string and use it in the request body
            String requestBody = objectMapper.writeValueAsString(rootNode);
            UserStoreReq userStoreReq = objectMapper.readValue(requestBody, UserStoreReq.class);
            getServerUserStoreService().addUserStore(userStoreReq);
            updateLiteUserStoreConnectorConfigs(true);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully onboarded lite user store.");
            }
        } catch (IOException e) {
            LOG.error(SelfServiceMgtConstants.ErrorMessage.ERROR_ONBOARDING_LITE_USER_STORE.getDescription(), e);
            throw new SelfServiceMgtEndpointException(Response.Status.INTERNAL_SERVER_ERROR,
                    getError(SelfServiceMgtConstants.ErrorMessage.ERROR_ONBOARDING_LITE_USER_STORE.getCode(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_ONBOARDING_LITE_USER_STORE.getMessage(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_ONBOARDING_LITE_USER_STORE.getDescription()));
        }
    }

    private void updateLiteUserStoreConnectorConfigs(boolean enableLiteUser) {

        try {
            InputStream inputStream;
            if (enableLiteUser) {
                inputStream = loadResourceFromClasspath(SelfServiceMgtConstants.ENABLE_LITE_USER_REQUEST_JSON);
            } else {
                inputStream = loadResourceFromClasspath(SelfServiceMgtConstants.DISABLE_LITE_USER_REQUEST_JSON);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(inputStream);
            // Convert updated JSON to string and use it in the request body
            String requestBody = objectMapper.writeValueAsString(rootNode);
            PropertyPatchReq updateLiteUserConfig = objectMapper.readValue(requestBody, PropertyPatchReq.class);
            updateOrganizationGovernanceConfigs(updateLiteUserConfig, false);
        } catch (IOException e) {
            LOG.error(SelfServiceMgtConstants.ErrorMessage.ERROR_UPDATING_GOVERNANCE_CONFIG.getDescription(), e);
            throw new SelfServiceMgtEndpointException(Response.Status.INTERNAL_SERVER_ERROR,
                    getError(SelfServiceMgtConstants.ErrorMessage.ERROR_UPDATING_GOVERNANCE_CONFIG.getCode(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_UPDATING_GOVERNANCE_CONFIG.getMessage(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_UPDATING_GOVERNANCE_CONFIG.getDescription()));
        }
    }

    private void createSystemApplication() {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        String appName = getConfigProperty(SelfServiceMgtConstants.SELF_SERVICE_DEFAULT_APP_NAME);

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Creating system application '" + appName + "' for tenant: " + tenantDomain);
            }
            // Check if the self-service app already exists, if yes, then return.
            if (isSSAppExists(tenantDomain, userName, appName)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("System application '" + appName + "' already exists for tenant: " + tenantDomain);
                }
                return;
            }

            // Load the request JSON template from a resource.
            InputStream inputStream = loadResourceFromClasspath(SelfServiceMgtConstants
                    .CREATE_SELF_SERVICE_APP_REQUEST_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(inputStream);

            // Update JSON fields based on config values.
            updateAppConfigValues(rootNode);

            // Convert updated JSON to string and use it in the request body.
            String requestBody = objectMapper.writeValueAsString(rootNode);
            ApplicationModel model = objectMapper.readValue(requestBody, ApplicationModel.class);

            // Create the application using the Application Management Service.
            getServerApplicationManagementService().createApplication(model, null);
            if (LOG.isInfoEnabled()) {
                LOG.info("Successfully created system application '" + appName + "' for tenant: " + tenantDomain);
            }

            // If legacy authorization runtime is enabled, skip subscribing to APIs.
            if (isLegacyAuthzRuntime()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Legacy authorization runtime is enabled, skipping API subscriptions.");
                }
                return;
            }

            // Subscribe to APIs required for Organization Management Self Service.
            ApplicationBasicInfo sSApplicationBasicInfo = applicationManagementService
                    .getApplicationBasicInfoByName(appName, tenantDomain);

            if (sSApplicationBasicInfo == null) {
                LOG.error(SelfServiceMgtConstants.ErrorMessage.ERROR_CREATING_SYSTEM_APP.getDescription());
                throw new SelfServiceMgtEndpointException(Response.Status.INTERNAL_SERVER_ERROR,
                        getError(SelfServiceMgtConstants.ErrorMessage.ERROR_CREATING_SYSTEM_APP.getCode(),
                                SelfServiceMgtConstants.ErrorMessage.ERROR_CREATING_SYSTEM_APP.getMessage(),
                                SelfServiceMgtConstants.ErrorMessage.ERROR_CREATING_SYSTEM_APP.getDescription()));
            }
            String sSApplicationId = sSApplicationBasicInfo.getApplicationResourceId();

            Map<String, List<String>> authorizedAPIAndScopeNames = getAuthorizedAPIsAndScopeNamesForSSApp();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Authorizing " + authorizedAPIAndScopeNames.size() + " APIs for system application.");
            }
            // Loop through the APIs and subscribe to them.
            for (Map.Entry<String, List<String>> entry : authorizedAPIAndScopeNames.entrySet()) {
                String apiId = entry.getKey();
                List<String> scopeNames = entry.getValue();
                authorizeAPItoSelfServiceApp(apiId, scopeNames, tenantDomain, sSApplicationId);
            }

            // Share the self-service app with all the child organizations.
            ServiceProvider serviceProvider = applicationManagementService.getServiceProvider(sSApplicationBasicInfo
                    .getApplicationId());
            applicationManagementService.updateApplication(serviceProvider, tenantDomain, userName);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully shared system application with child organizations.");
            }

        } catch (IOException | IdentityApplicationManagementException e) {
            LOG.error(SelfServiceMgtConstants.ErrorMessage.ERROR_CREATING_SYSTEM_APP.getDescription(), e);
            throw new SelfServiceMgtEndpointException(Response.Status.INTERNAL_SERVER_ERROR,
                    getError(SelfServiceMgtConstants.ErrorMessage.ERROR_CREATING_SYSTEM_APP.getCode(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_CREATING_SYSTEM_APP.getMessage(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_CREATING_SYSTEM_APP.getDescription()));
        }
    }

    private void deleteSystemApplication() {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        String userName = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
        String appName = getConfigProperty(SelfServiceMgtConstants.SELF_SERVICE_DEFAULT_APP_NAME);

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Deleting system application '" + appName + "' for tenant: " + tenantDomain);
            }
            // Check if the self-service app exists, if yes, then delete it.
            if (isSSAppExists(tenantDomain, userName, appName)) {
                applicationManagementService.deleteApplication(appName, tenantDomain, userName);
                if (LOG.isInfoEnabled()) {
                    LOG.info("Successfully deleted system application '" + appName + "' for tenant: " + tenantDomain);
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("System application '" + appName + "' does not exist for tenant: " + tenantDomain);
                }
            }
        } catch (IdentityApplicationManagementException e) {
            LOG.error(SelfServiceMgtConstants.ErrorMessage.ERROR_DELETING_SYSTEM_APP.getDescription(), e);
            throw new SelfServiceMgtEndpointException(Response.Status.INTERNAL_SERVER_ERROR,
                    getError(SelfServiceMgtConstants.ErrorMessage.ERROR_DELETING_SYSTEM_APP.getCode(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_DELETING_SYSTEM_APP.getMessage(),
                            SelfServiceMgtConstants.ErrorMessage.ERROR_DELETING_SYSTEM_APP.getDescription()));
        }
    }

    private boolean isSSAppExists(String tenantDomain, String userName, String appName)
            throws IdentityApplicationManagementException {

        // Get the basic info of the self-service app using the Application Management Service.
        ApplicationBasicInfo[] applicationBasicInfos = applicationManagementService
                .getApplicationBasicInfo(tenantDomain, userName, appName);
        // Check if there is exactly one self-service app with the given name.
        return applicationBasicInfos.length == 1;
    }

    private void updateAppConfigValues(JsonNode rootNode) {

        String appName = getConfigProperty(SelfServiceMgtConstants.SELF_SERVICE_DEFAULT_APP_NAME);
        String expiryTime = getConfigProperty(SelfServiceMgtConstants.SELF_SERVICE_DEFAULT_TOKEN_EXPIRY_TIME);

        JsonNode nameNode = rootNode.get(SelfServiceMgtConstants.PROPERTY_APP_NAME);
        if (nameNode != null) {
            ((ObjectNode) rootNode).put(SelfServiceMgtConstants.PROPERTY_APP_NAME, appName);
        }

        JsonNode accessTokenExpireNode = rootNode.get(SelfServiceMgtConstants.PROPERTY_INBOUND_PROTOCOL);
        if (accessTokenExpireNode != null) {
            ((ObjectNode) rootNode
                    .path(SelfServiceMgtConstants.PROPERTY_INBOUND_PROTOCOL)
                    .path(SelfServiceMgtConstants.PROPERTY_OIDC_PROTOCOL)
                    .path(SelfServiceMgtConstants.PROPERTY_ACCESS_TOKEN))
                    .put(SelfServiceMgtConstants.PROPERTY_TOKEN_EXPIRY, Integer.valueOf(expiryTime));
        }
    }

    private void updateOnboardConfigValues(JsonNode rootNode) {

        String connectionURL = getConfigProperty(SelfServiceMgtConstants.LITE_USER_CONNECTION_URL);
        String userStoreName = getConfigProperty(SelfServiceMgtConstants.LITE_USER_USER_STORE_NAME);
        String userStorePassword = getConfigProperty(SelfServiceMgtConstants.LITE_USER_CONNECTION_PASSWORD);
        String userStoreUsername = getConfigProperty(SelfServiceMgtConstants.LITE_USER_CONNECTION_USERNAME);
        String driverName = getConfigProperty(SelfServiceMgtConstants.LITE_USER_CONNECTION_DRIVER_NAME);

        JsonNode propertiesNode = rootNode.get(SelfServiceMgtConstants.PROPERTY_USER_STORE_PROPERTIES);
        if (propertiesNode.isArray()) {
            for (JsonNode elementNode : propertiesNode) {
                String name = elementNode.get(SelfServiceMgtConstants.PROPERTY_NAME).asText();
                ObjectNode objectNode = (ObjectNode) elementNode;
                switch (name) {
                    case SelfServiceMgtConstants.PROPERTY_USER_STORE_CONNECTION_URL:
                        objectNode.put(SelfServiceMgtConstants.PROPERTY_VALUE, connectionURL);
                        break;
                    case SelfServiceMgtConstants.PROPERTY_USER_STORE_CONNECTION_USERNAME:
                        objectNode.put(SelfServiceMgtConstants.PROPERTY_VALUE, userStoreUsername);
                        break;
                    case SelfServiceMgtConstants.PROPERTY_USER_STORE_CONNECTION_PASSWORD:
                        objectNode.put(SelfServiceMgtConstants.PROPERTY_VALUE, userStorePassword);
                        break;
                    case SelfServiceMgtConstants.PROPERTY_USER_STORE_CONNECTION_DRIVER:
                        objectNode.put(SelfServiceMgtConstants.PROPERTY_VALUE, driverName);
                        break;
                    default:
                        break;
                }
            }
        }

        JsonNode nameNode = rootNode.get(SelfServiceMgtConstants.PROPERTY_USER_STORE_NAME);
        if (nameNode != null) {
            ((ObjectNode) rootNode).put(SelfServiceMgtConstants.PROPERTY_USER_STORE_NAME, userStoreName);
        }
    }

    private List<PropertyRes> buildConnectorResDTO(ConnectorConfig connectorConfig)
            throws IdentityGovernanceException {

        List<PropertyRes> properties = new ArrayList<>();
        if (connectorConfig == null || connectorConfig.getProperties() == null) {
            throw new IdentityGovernanceException(SelfServiceMgtConstants.CONFIGS_NOT_FOUND_ERROR);
        }
        for (Property property : connectorConfig.getProperties()) {
            PropertyRes propertyRes = new PropertyRes();
            propertyRes.setName(property.getName());
            propertyRes.setValue(property.getValue());
            propertyRes.setDisplayName(property.getDisplayName());
            propertyRes.setDescription(property.getDescription() != null ? property.getDescription() :
                    StringUtils.EMPTY);
            properties.add(propertyRes);
        }
        return properties;
    }

    private String getGovernanceConfigValue(String preferenceProperty) throws IdentityGovernanceException {

        String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
        return identityGovernanceService.getConfiguration(new String[]{preferenceProperty}, tenantDomain)[0].getValue();
    }

    private String getConfigProperty(String propertyName) {

        return org.wso2.carbon.identity.organization.management.service.util.OrganizationManagementConfigUtil
                .getProperty(propertyName);
    }

    private Error getError(String errorCode, String errorMessage, String errorDescription) {

        Error error = new Error();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }

    private Map<String, List<String>> getAuthorizedAPIsAndScopeNamesForSSApp() {

        Map<String, List<String>> authorizedAPIMap = new HashMap<>();

        // Authorize Organization Management API.
        authorizedAPIMap.put("/api/server/v1/organizations",
                new ArrayList<>(Arrays.asList("internal_organization_view", "internal_organization_create")));

        // Authorize Scim User API.
        authorizedAPIMap.put("/scim2/Users",
                new ArrayList<>(Arrays.asList("internal_user_mgt_view", "internal_user_mgt_create")));

        // Authorize Scim Organization User API.
        authorizedAPIMap.put("/o/scim2/Users",
                new ArrayList<>(Arrays.asList("internal_org_user_mgt_create", "internal_org_user_mgt_list")));

        // Authorize Scim Organization Roles API.
        authorizedAPIMap.put("/o/scim2/Roles",
                new ArrayList<>(Arrays.asList("internal_org_role_mgt_view", "internal_org_role_mgt_update")));

        // Authorize organization application Management
        authorizedAPIMap.put("/o/api/server/v1/applications",
                new ArrayList<>(Arrays.asList("internal_org_application_mgt_view")));

        return authorizedAPIMap;
    }

    private void authorizeAPItoSelfServiceApp(String aPIIResourceIdentifier, List<String> scopeNames,
                                              String tenantDomain, String sSApplicationId) {

        String aPIFilter = "identifier eq " + aPIIResourceIdentifier;
        try {
            List<APIResource> apiResources = apiResourceManager.getAPIResources(null, null, 1,
                    aPIFilter, APIResourceManagementConstants.ASC,
                    tenantDomain).getAPIResources();
            if (apiResources != null && !apiResources.isEmpty()) {

                APIResource apiResource = apiResources.get(0);

                String policyId = APIResourceManagementConstants.RBAC_AUTHORIZATION;
                List<Scope> scopes = apiResourceManager.getAPIScopesById(apiResource.getId(), tenantDomain);

                List<Scope> authorizedScopes = new ArrayList<>();
                for (Scope scope : scopes) {
                    if (scopeNames.contains(scope.getName())) {
                        authorizedScopes.add(scope);
                    }
                }

                AuthorizedAPI authorizedAPI = new AuthorizedAPI.AuthorizedAPIBuilder()
                        .apiId(apiResource.getId())
                        .appId(sSApplicationId)
                        .scopes(authorizedScopes)
                        .policyId(policyId)
                        .build();
                authorizedAPIManagementService.addAuthorizedAPI(sSApplicationId,
                        authorizedAPI, tenantDomain);
            }

        } catch (APIResourceMgtException | IdentityApplicationManagementException e) {
            LOG.error("Error while authorizing APIs to the Organization Self Service application.", e);
        }
    }

    /**
     * Load resource from the classpath.
     *
     * @param resourcePath Resource path.
     * @return InputStream of the resource.
     * @throws IOException If an error occurred while loading the resource.
     */
    private InputStream loadResourceFromClasspath(String resourcePath) throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        return inputStream;
    }

    public static boolean isLegacyAuthzRuntime() {

        return CarbonConstants.ENABLE_LEGACY_AUTHZ_RUNTIME;
    }

    /**
     * Get ServerApplicationManagementService instance.
     *
     * @return ServerApplicationManagementService.
     */
    private static ServerApplicationManagementService getServerApplicationManagementService() {

        try {
            return ServerApplicationManagementServiceFactory.getServerApplicationManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating ServerApplicationManagementService.", e);
        }
    }

    /**
     * Get ServerUserStoreService instance.
     *
     * @return ServerUserStoreService.
     */
    private static ServerUserStoreService getServerUserStoreService() {

        try {
            return ServerUserStoreServiceFactory.getServerUserStoreService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating ServerUserStoreService.", e);
        }
    }
}
