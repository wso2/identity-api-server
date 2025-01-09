/*
 * Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.configs.v1.core;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.configs.common.ConfigsServiceHolder;
import org.wso2.carbon.identity.api.server.configs.common.Constants;
import org.wso2.carbon.identity.api.server.configs.common.SchemaConfigParser;
import org.wso2.carbon.identity.api.server.configs.common.factory.JWTAuthenticationMgtOGSiServiceFactory;
import org.wso2.carbon.identity.api.server.configs.v1.exception.JWTClientAuthenticatorException;
import org.wso2.carbon.identity.api.server.configs.v1.function.CORSConfigurationToCORSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.function.DCRConnectorUtil;
import org.wso2.carbon.identity.api.server.configs.v1.function.JWTConnectorUtil;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticationType;
import org.wso2.carbon.identity.api.server.configs.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticatorListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticatorProperty;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.DCRConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.DCRPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.Endpoint;
import org.wso2.carbon.identity.api.server.configs.v1.model.ImpersonationConfiguration;
import org.wso2.carbon.identity.api.server.configs.v1.model.ImpersonationPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthPassiveSTSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthSAML2Config;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.JWTKeyValidatorPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.JWTValidatorConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.Patch;
import org.wso2.carbon.identity.api.server.configs.v1.model.ProvisioningConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.RealmConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfigListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.Schema;
import org.wso2.carbon.identity.api.server.configs.v1.model.SchemaListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.ScimConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.ServerConfig;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementClientException;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementServerException;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.IdentityProviderProperty;
import org.wso2.carbon.identity.application.common.model.InboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.RequestPathAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.UserDefinedAuthenticatorEndpointConfig;
import org.wso2.carbon.identity.application.common.model.UserDefinedLocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationManagementUtil;
import org.wso2.carbon.identity.application.mgt.ApplicationConstants;
import org.wso2.carbon.identity.base.AuthenticatorPropertyConstants;
import org.wso2.carbon.identity.core.ServiceURLBuilder;
import org.wso2.carbon.identity.core.URLBuilderException;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceClientException;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceException;
import org.wso2.carbon.identity.cors.mgt.core.exception.CORSManagementServiceServerException;
import org.wso2.carbon.identity.cors.mgt.core.model.CORSConfiguration;
import org.wso2.carbon.identity.oauth.dcr.exception.DCRMException;
import org.wso2.carbon.identity.oauth2.impersonation.exceptions.ImpersonationConfigMgtClientException;
import org.wso2.carbon.identity.oauth2.impersonation.exceptions.ImpersonationConfigMgtException;
import org.wso2.carbon.identity.oauth2.impersonation.exceptions.ImpersonationConfigMgtServerException;
import org.wso2.carbon.identity.oauth2.impersonation.models.ImpersonationConfig;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementClientException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementException;
import org.wso2.carbon.idp.mgt.IdentityProviderManagementServerException;
import org.wso2.carbon.logging.service.data.RemoteServerLoggerData;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLDecode;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLEncode;
import static org.wso2.carbon.identity.api.server.configs.common.Constants.CONFIGS_AUTHENTICATOR_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.configs.common.Constants.CONFIGS_SCHEMAS_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.configs.common.Constants.ErrorMessage.ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND;
import static org.wso2.carbon.identity.api.server.configs.common.Constants.PATH_SEPERATOR;

/**
 * Call internal osgi services to perform server configuration management.
 */
public class ServerConfigManagementService {

    private static final Log log = LogFactory.getLog(ServerConfigManagementService.class);

    /**
     * Get list of local authenticators supported by the server.
     *
     * @return List of authenticator basic information.
     */
    public List<AuthenticatorListItem> getAuthenticators(String type) {

        try {
            LocalAuthenticatorConfig[] localConfigs = null;
            RequestPathAuthenticatorConfig[] requestPathConfigs = null;
            if (StringUtils.isBlank(type) || type.equals(Authenticator.TypeEnum.LOCAL.value())) {
                localConfigs = ConfigsServiceHolder.getInstance()
                        .getApplicationManagementService()
                        .getAllLocalAuthenticators(ContextLoader.getTenantDomainFromContext());
            }
            if (StringUtils.isBlank(type) || type.equals(Authenticator.TypeEnum.REQUEST_PATH.value())) {
                requestPathConfigs = ConfigsServiceHolder.getInstance()
                        .getApplicationManagementService()
                        .getAllRequestPathAuthenticators(ContextLoader.getTenantDomainFromContext());
            }
            return buildAuthenticatorListResponse(localConfigs, requestPathConfigs);
        } catch (IdentityApplicationManagementException e) {
            throw handleApplicationMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_AUTHENTICATORS,
                    null);
        }
    }

    /**
     * Get an authenticator identified by its resource ID.
     *
     * @param authenticatorId Authenticator resource ID.
     * @return Authenticator.
     */
    public Authenticator getAuthenticator(String authenticatorId) {

        try {
            LocalAuthenticatorConfig authenticatorConfig = getAuthenticatorById(
                    ConfigsServiceHolder.getInstance().getApplicationManagementService().getAllLocalAuthenticators(
                            ContextLoader.getTenantDomainFromContext()), authenticatorId);
            if (authenticatorConfig != null) {
                return buildAuthenticatorResponse(authenticatorConfig);
            }

            RequestPathAuthenticatorConfig requestPathConfig = getAuthenticatorById(ConfigsServiceHolder.getInstance
                    ().getApplicationManagementService().getAllRequestPathAuthenticators(ContextLoader
                    .getTenantDomainFromContext()), authenticatorId);
            if (requestPathConfig != null) {
                return buildAuthenticatorResponse(requestPathConfig);
            }

            throw handleException(Response.Status.NOT_FOUND,
                    Constants.ErrorMessage.ERROR_CODE_AUTHENTICATOR_NOT_FOUND, authenticatorId);
        } catch (IdentityApplicationManagementException e) {
            throw handleApplicationMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_AUTHENTICATOR,
                    authenticatorId);
        }
    }

    /**
     * Get Server Configs.
     *
     * @return ServerConfig.
     */
    public ServerConfig getConfigs() {

        IdentityProvider residentIdP = getResidentIdP();

        UserRealm userRealm = CarbonContext.getThreadLocalCarbonContext().getUserRealm();
        RealmConfig realmConfig = new RealmConfig();
        try {
            if (userRealm != null && userRealm.getRealmConfiguration() != null) {
                realmConfig.adminUser(userRealm.getRealmConfiguration().getAdminUserName());
                realmConfig.adminRole(userRealm.getRealmConfiguration().getAdminRoleName());
                realmConfig.everyoneRole(userRealm.getRealmConfiguration().getEveryOneRoleName());
            }
        } catch (UserStoreException e) {
            log.error("Error while retrieving user-realm information.", e);
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_RETRIEVING_CONFIGS, null);
        }
        String idleSessionTimeout = null;
        IdentityProviderProperty idleSessionProp = IdentityApplicationManagementUtil.getProperty(
                residentIdP.getIdpProperties(), IdentityApplicationConstants.SESSION_IDLE_TIME_OUT);
        if (idleSessionProp != null) {
            idleSessionTimeout = idleSessionProp.getValue();
        }

        String rememberMePeriod = null;
        IdentityProviderProperty rememberMeProp = IdentityApplicationManagementUtil.getProperty(
                residentIdP.getIdpProperties(), IdentityApplicationConstants.REMEMBER_ME_TIME_OUT);
        if (rememberMeProp != null) {
            rememberMePeriod = rememberMeProp.getValue();
        }

        String homeRealmIdStr = residentIdP.getHomeRealmId();
        List<String> homeRealmIdentifiers = null;
        if (StringUtils.isNotBlank(homeRealmIdStr)) {
            homeRealmIdentifiers =
                    Arrays.stream(homeRealmIdStr.trim().split("\\s*,\\s*")).collect(Collectors.toList());
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setRealmConfig(realmConfig);
        serverConfig.setIdleSessionTimeoutPeriod(idleSessionTimeout);
        serverConfig.setRememberMePeriod(rememberMePeriod);
        serverConfig.setHomeRealmIdentifiers(homeRealmIdentifiers);
        serverConfig.setProvisioning(buildProvisioningConfig());
        serverConfig.setAuthenticators(getAuthenticators(null));
        serverConfig.setCors(getCORSConfiguration());
        serverConfig.setDcr(getDCRConfiguration());
        return serverConfig;
    }

    /**
     * Patch Server Configs. Patch 'REPLACE', 'ADD', 'REMOVE' operations have been supported for primary attributes in
     * ServerConfig model.
     *
     * @param patchRequest Patch request in Json Patch notation See
     *                     <a href="https://tools.ietf.org/html/rfc6902">https://tools.ietf
     *                     .org/html/rfc6902</a>.
     */
    public void patchConfigs(List<Patch> patchRequest) {

        try {
            IdentityProvider residentIdP =
                    ConfigsServiceHolder.getInstance().getIdentityProviderManager().getResidentIdP(ContextLoader
                            .getTenantDomainFromContext());
            // Resident Identity Provider can be null only due to an internal server error.
            if (residentIdP == null) {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                        .ERROR_CODE_ERROR_UPDATING_CONFIGS, null);
            }
            IdentityProvider idpToUpdate = createIdPClone(residentIdP);
            processPatchRequest(patchRequest, idpToUpdate);
            // To avoid updating non-existing authenticators in DB layer.
            idpToUpdate.setFederatedAuthenticatorConfigs(new FederatedAuthenticatorConfig[0]);
            ConfigsServiceHolder.getInstance().getIdentityProviderManager()
                    .updateResidentIdP(idpToUpdate, ContextLoader.getTenantDomainFromContext());

        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_CONFIGS, null);
        }
    }

    /**
     * Get SCIM Inbound Provisioning Configurations.
     *
     * @return ScimConfig.
     */
    public ScimConfig getInboundScimConfig() {

        ServiceProvider application = getResidentApplication();
        ScimConfig scimConfig = new ScimConfig();
        scimConfig.setEnableProxyMode(application.getInboundProvisioningConfig().isDumbMode());
        scimConfig.setProvisioningUserstore(application.getInboundProvisioningConfig().getProvisioningUserStore());
        return scimConfig;
    }

    /**
     * Update SCIM Inbound Provisioning Configurations.
     *
     * @param scimConfig ScimConfig.
     */
    public void updateInboundScimConfigs(ScimConfig scimConfig) {

        ServiceProvider application = getResidentApplication();

        if (scimConfig != null) {
            InboundProvisioningConfig inboundProvisioningConfig = new InboundProvisioningConfig();
            inboundProvisioningConfig.setDumbMode(scimConfig.getEnableProxyMode());
            if (!scimConfig.getEnableProxyMode()) {
                inboundProvisioningConfig.setProvisioningEnabled(true);
                inboundProvisioningConfig.setProvisioningUserStore(scimConfig.getProvisioningUserstore());
            }
            ServiceProvider applicationClone = createApplicationClone(application);
            applicationClone.setInboundProvisioningConfig(inboundProvisioningConfig);

            try {
                ConfigsServiceHolder.getInstance().getApplicationManagementService().updateApplicationByResourceId
                        (applicationClone.getApplicationResourceId(), applicationClone, ContextLoader
                                .getTenantDomainFromContext(), ContextLoader.getUsernameFromContext());
            } catch (IdentityApplicationManagementException e) {
                throw handleApplicationMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CONFIGS,
                        null);
            }
        }
    }

    /**
     * Retrieves the impersonation configuration for the current tenant domain.
     *
     * @return ImpersonationConfiguration The current impersonation configuration.
     * @throws ImpersonationConfigMgtException If there is an error retrieving the impersonation configuration.
     */
    public ImpersonationConfiguration getImpersonationConfiguration() {

        // Retrieve the tenant domain from the current context
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        ImpersonationConfiguration impersonationConfiguration = new ImpersonationConfiguration();
        try {
            // Get the impersonation configuration for the tenant domain
            ImpersonationConfig impersonationConfig = ConfigsServiceHolder.getInstance()
                    .getImpersonationConfigMgtService().getImpersonationConfig(tenantDomain);

            // Enable email notifications based on the retrieved configuration
            return impersonationConfiguration.enableEmailNotification(impersonationConfig.isEnableEmailNotification());
        } catch (ImpersonationConfigMgtException e) {
            // Handle exceptions related to retrieving impersonation configuration
            throw handleImpersonationConfigException(e, Constants.ErrorMessage.ERROR_CODE_IMP_CONFIG_RETRIEVE, null);
        }
    }

    /**
     * Applies patch operations to the impersonation configuration for the current tenant domain.
     *
     * @param impersonationPatchList List of patch operations to apply.
     * @throws ImpersonationConfigMgtException If there is an error updating the impersonation configuration.
     */
    public void patchImpersonationConfiguration(List<ImpersonationPatch> impersonationPatchList) {

        // Return if the patch list is empty
        if (CollectionUtils.isEmpty(impersonationPatchList)) {
            return;
        }

        // Retrieve the tenant domain from the current context
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        ImpersonationConfig impersonationConfig;
        try {
            // Get the current impersonation configuration for the tenant domain
            impersonationConfig = ConfigsServiceHolder.getInstance()
                    .getImpersonationConfigMgtService().getImpersonationConfig(tenantDomain);
        } catch (ImpersonationConfigMgtException e) {
            // Handle exceptions related to retrieving impersonation configuration
            throw handleImpersonationConfigException(e, Constants.ErrorMessage.ERROR_CODE_IMP_CONFIG_RETRIEVE, null);
        }

        try {
            // Iterate over each patch operation in the list
            for (ImpersonationPatch impersonationPatch : impersonationPatchList) {
                String path = impersonationPatch.getPath();
                ImpersonationPatch.OperationEnum operation = impersonationPatch.getOperation();
                boolean value = impersonationPatch.getValue();

                // Support only 'REPLACE' and 'ADD' patch operations for email notifications
                if (operation == ImpersonationPatch.OperationEnum.REPLACE
                        || operation == ImpersonationPatch.OperationEnum.ADD) {
                    if (path.matches(Constants.IMPERSONATION_CONFIG_ENABLE_EMAIL_NOTIFICATION)) {
                        impersonationConfig.setEnableEmailNotification(value);
                    } else {
                        // Throw an error if the patch path is unsupported
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                    }
                } else {
                    // Throw an error if the patch operation is unsupported
                    throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                }
            }

            // Update the impersonation configuration for the tenant with the patched configuration
            ConfigsServiceHolder.getInstance().getImpersonationConfigMgtService()
                    .setImpersonationConfig(impersonationConfig, tenantDomain);
        } catch (ImpersonationConfigMgtException e) {
            // Handle exceptions related to updating impersonation configuration
            throw handleImpersonationConfigException(e, Constants.ErrorMessage.ERROR_CODE_IMP_CONFIG_UPDATE, null);
        }
    }


    /**
     * Get the CORS config for a tenant.
     */
    public CORSConfig getCORSConfiguration() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            CORSConfiguration corsConfiguration = ConfigsServiceHolder.getInstance().getCorsManagementService()
                    .getCORSConfiguration(tenantDomain);

            return new CORSConfigurationToCORSConfig().apply(corsConfiguration);
        } catch (CORSManagementServiceException e) {
            throw handleCORSException(e, Constants.ErrorMessage.ERROR_CODE_CORS_CONFIG_RETRIEVE, null);
        }
    }

    /**
     * Patch the CORS config of a tenant.
     *
     * @param corsPatchList List of patch operations.
     */
    public void patchCORSConfig(List<CORSPatch> corsPatchList) {

        if (CollectionUtils.isEmpty(corsPatchList)) {
            return;
        }

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        CORSConfiguration corsConfiguration;
        try {
            corsConfiguration = ConfigsServiceHolder.getInstance().getCorsManagementService()
                    .getCORSConfiguration(tenantDomain);
        } catch (CORSManagementServiceException e) {
            throw handleCORSException(e, Constants.ErrorMessage.ERROR_CODE_CORS_CONFIG_RETRIEVE, null);
        }

        try {
            for (CORSPatch corsPatch : corsPatchList) {
                String path = corsPatch.getPath();
                CORSPatch.OperationEnum operation = corsPatch.getOperation();
                String value = corsPatch.getValue().trim();

                // We support only 'REPLACE', 'ADD' and 'REMOVE' patch operations.
                if (operation == CORSPatch.OperationEnum.REPLACE) {
                    if (path.matches(Constants.CORS_CONFIG_ALLOW_GENERIC_HTTP_PATH_REGEX)) {
                        corsConfiguration.setAllowGenericHttpRequests(Boolean.parseBoolean(value));
                    } else if (path.matches(Constants.CORS_CONFIG_ALLOW_ANY_ORIGIN_PATH_REGEX)) {
                        corsConfiguration.setAllowAnyOrigin(Boolean.parseBoolean(value));
                    } else if (path.matches(Constants.CORS_CONFIG_ALLOW_SUBDOMAINS_PATH_REGEX)) {
                        corsConfiguration.setAllowSubdomains(Boolean.parseBoolean(value));
                    } else if (path.matches(Constants.CORS_CONFIG_SUPPORTED_METHODS_PATH_REGEX)) {
                        corsConfiguration.setSupportedMethods(new HashSet<>(Collections.singletonList(value)));
                    } else if (path.matches(Constants.CORS_CONFIG_SUPPORT_ANY_HEADER_PATH_REGEX)) {
                        corsConfiguration.setSupportAnyHeader(Boolean.parseBoolean(value));
                    } else if (path.matches(Constants.CORS_CONFIG_SUPPORTED_HEADERS_PATH_REGEX)) {
                        corsConfiguration.setSupportedHeaders(new HashSet<>(Collections.singletonList(value)));
                    } else if (path.matches(Constants.CORS_CONFIG_EXPOSED_HEADERS_PATH_REGEX)) {
                        corsConfiguration.setExposedHeaders(new HashSet<>(Collections.singletonList(value)));
                    } else if (path.matches(Constants.CORS_CONFIG_SUPPORTS_CREDENTIALS_PATH_REGEX)) {
                        corsConfiguration.setSupportsCredentials(Boolean.parseBoolean(value));
                    } else if (path.matches(Constants.CORS_CONFIG_MAX_AGE_PATH_REGEX)) {
                        corsConfiguration.setMaxAge(Integer.parseInt(value));
                    } else {
                        // Throw an error if any other patch operations are sent in the request.
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                    }
                } else if (operation == CORSPatch.OperationEnum.ADD) {
                    if (path.matches(Constants.CORS_CONFIG_SUPPORTED_METHODS_PATH_REGEX)) {
                        corsConfiguration.getSupportedMethods().add(value);
                    } else if (path.matches(Constants.CORS_CONFIG_SUPPORTED_HEADERS_PATH_REGEX)) {
                        corsConfiguration.getSupportedHeaders().add(value);
                    } else if (path.matches(Constants.CORS_CONFIG_EXPOSED_HEADERS_PATH_REGEX)) {
                        corsConfiguration.getExposedHeaders().add(value);
                    } else {
                        // Throw an error if any other patch operations are sent in the request.
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                    }
                } else if (operation == CORSPatch.OperationEnum.REMOVE) {
                    if (path.matches(Constants.CORS_CONFIG_SUPPORTED_METHODS_PATH_REGEX)) {
                        corsConfiguration.getSupportedMethods().remove(value);
                    } else if (path.matches(Constants.CORS_CONFIG_SUPPORTED_HEADERS_PATH_REGEX)) {
                        corsConfiguration.getSupportedHeaders().remove(value);
                    } else if (path.matches(Constants.CORS_CONFIG_EXPOSED_HEADERS_PATH_REGEX)) {
                        corsConfiguration.getExposedHeaders().remove(value);
                    } else {
                        // Throw an error if any other patch operations are sent in the request.
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                    }
                } else {
                    // Throw an error if any other patch operations are sent in the request.
                    throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                }
            }

            // Set the patched configuration object as the new CORS configuration for the tenant.
            ConfigsServiceHolder.getInstance().getCorsManagementService()
                    .setCORSConfiguration(corsConfiguration, tenantDomain);
        } catch (CORSManagementServiceException e) {
            throw handleCORSException(e, Constants.ErrorMessage.ERROR_CODE_CORS_CONFIG_UPDATE, null);
        }
    }

    /**
     * Get Home Realm Identifiers.
     *
     * @return List of home realm identifiers.
     */
    public List<String> getHomeRealmIdentifiers() {

        IdentityProvider residentIdP = getResidentIdP();
        String homeRealmIdStr = residentIdP.getHomeRealmId();
        List<String> homeRealmIdentifiers = new ArrayList<>();
        if (StringUtils.isNotBlank(homeRealmIdStr)) {
            homeRealmIdentifiers =
                    Arrays.stream(homeRealmIdStr.trim().split("\\s*,\\s*")).collect(Collectors.toList());
        }
        return homeRealmIdentifiers;
    }

    /**
     * Get schemas supported by the server.
     *
     * @return List of schema metadata.
     */
    public List<SchemaListItem> getSchemas() {

        return SchemaConfigParser.getInstance().getSchemaMap().keySet().stream()
                .map(key -> {
                    final String schemaId = base64URLEncode(key);
                    return new SchemaListItem().id(schemaId).name(key)
                            .self(ContextLoader.buildURIForBody(V1_API_PATH_COMPONENT +
                                    CONFIGS_SCHEMAS_PATH_COMPONENT + PATH_SEPERATOR + schemaId).toString());
                }).collect(Collectors.toList());
    }

    /**
     * Get attributes of a schema.
     *
     * @param schemaId Schema ID.
     * @return Schema attribute list.
     */
    public Schema getSchema(String schemaId) {

        String schemaName = base64URLDecode(schemaId);
        Map<String, List<String>> schemaMap = SchemaConfigParser.getInstance().getSchemaMap();

        if (!schemaMap.containsKey(schemaName)) {
            throw handleException(Response.Status.NOT_FOUND,
                    Constants.ErrorMessage.ERROR_CODE_SCHEMA_NOT_FOUND, schemaId);
        }

        Schema schema = new Schema();
        schema.setId(schemaId);
        schema.setName(schemaName);
        schema.setAttributes(schemaMap.get(schemaName));
        return schema;
    }

    /**
     * Reset Remote Server configuration to default values.
     */
    public void resetRemoteServerConfig() {

        resetRemoteServerConfig(Constants.AUDIT);
        resetRemoteServerConfig(Constants.CARBON);
    }

    /**
     * Reset Remote Server configuration to default values.
     *
     * @param logType Log Type (ex: CARBON or AUDIT).
     */
    public void resetRemoteServerConfig(String logType) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        validateTenantDomain(tenantDomain, "Resetting remote server configuration service is not available for %s");

        RemoteServerLoggerData remoteServerLoggerData = new RemoteServerLoggerData();

        validateLogType(logType);
        // Backend logic only supports logType in Uppercase.
        remoteServerLoggerData.setLogType(logType.toUpperCase(Locale.ENGLISH));

        try {
            ConfigsServiceHolder.getInstance().getRemoteLoggingConfigService()
                    .resetRemoteServerConfig(remoteServerLoggerData);
        } catch (ConfigurationException | IOException e) {
            log.error("Error while resetting remote server configuration.", e);
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_RESETTING_REMOTE_LOGGING_CONFIGS, null);
        }
    }

    private void validateTenantDomain(String tenantDomain, String message) {

        if (!StringUtils.equalsIgnoreCase(tenantDomain, MultitenantConstants.SUPER_TENANT_DOMAIN_NAME)) {
            if (log.isDebugEnabled()) {
                log.debug(String.format(message,
                        tenantDomain));
            }
            throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                    .ERROR_CODE_INVALID_TENANT_DOMAIN_FOR_REMOTE_LOGGING_CONFIG, null);
        }
    }

    private void validateLogType(String logType) {

        if (!Constants.AUDIT.equals(logType) && !Constants.CARBON.equals(logType)) {
            throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                    .ERROR_CODE_INVALID_LOG_TYPE_FOR_REMOTE_LOGGING_CONFIG, null);
        }
    }

    /**
     * Update remote server logging configurations. Each list item should correspond to specific log type.
     *
     * @param remoteLoggingConfigListItem Remote Logging Config List Item.
     */
    public void updateRemoteLoggingConfigs(List<RemoteLoggingConfigListItem> remoteLoggingConfigListItem) {

        for (RemoteLoggingConfigListItem loggingConfigListItem: remoteLoggingConfigListItem) {

            switch (loggingConfigListItem.getLogType()) {
                case AUDIT:
                    updateRemoteLoggingConfig(Constants.AUDIT, getRemoteLoggingConfig(loggingConfigListItem));
                    break;
                case CARBON:
                    updateRemoteLoggingConfig(Constants.CARBON, getRemoteLoggingConfig(loggingConfigListItem));
                    break;
                default:
                    throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                            .ERROR_CODE_INVALID_LOG_TYPE_FOR_REMOTE_LOGGING_CONFIG, null);
            }
        }
    }

    private RemoteLoggingConfig getRemoteLoggingConfig(RemoteLoggingConfigListItem loggingConfigListItem) {

        RemoteLoggingConfig remoteLoggingConfig = new RemoteLoggingConfig();
        remoteLoggingConfig.setRemoteUrl(loggingConfigListItem.getRemoteUrl());
        remoteLoggingConfig.setConnectTimeoutMillis(loggingConfigListItem.getConnectTimeoutMillis());
        remoteLoggingConfig.setVerifyHostname(loggingConfigListItem.getVerifyHostname());
        remoteLoggingConfig.setUsername(loggingConfigListItem.getUsername());
        remoteLoggingConfig.setPassword(loggingConfigListItem.getPassword());
        remoteLoggingConfig.setKeystoreLocation(loggingConfigListItem.getKeystoreLocation());
        remoteLoggingConfig.setKeystorePassword(loggingConfigListItem.getKeystorePassword());
        remoteLoggingConfig.setTruststoreLocation(loggingConfigListItem.getTruststoreLocation());
        remoteLoggingConfig.setTruststorePassword(loggingConfigListItem.getTruststorePassword());
        return remoteLoggingConfig;
    }

    /**
     * Update remote server logging configurations for given log type.
     *
     * @param logType               Log type (ex: AUDIT or CARBON).
     * @param remoteLoggingConfig   Remote server logging configurations.
     */
    public void updateRemoteLoggingConfig(String logType, RemoteLoggingConfig remoteLoggingConfig) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        validateTenantDomain(tenantDomain, "Resetting remote server configuration service is not available for %s");

        RemoteServerLoggerData remoteServerLoggerData = getRemoteServerLoggerData(remoteLoggingConfig);
        validateLogType(logType);
        // Backend logic only supports logType in Uppercase.
        remoteServerLoggerData.setLogType(logType.toUpperCase(Locale.ENGLISH));

        try {
            ConfigsServiceHolder.getInstance().getRemoteLoggingConfigService()
                    .addRemoteServerConfig(remoteServerLoggerData);
        } catch (ConfigurationException | IOException e) {
            log.error("Error while updating remote server configuration.", e);
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_UPDATING_REMOTE_LOGGING_CONFIGS, null);
        }
    }

    private RemoteServerLoggerData getRemoteServerLoggerData(RemoteLoggingConfig remoteLoggingConfig) {

        RemoteServerLoggerData remoteServerLoggerData = new RemoteServerLoggerData();
        remoteServerLoggerData.setUrl(remoteLoggingConfig.getRemoteUrl());
        remoteServerLoggerData.setConnectTimeoutMillis(remoteLoggingConfig.getConnectTimeoutMillis());
        remoteServerLoggerData.setVerifyHostname(remoteLoggingConfig.getVerifyHostname());
        remoteServerLoggerData.setUsername(remoteLoggingConfig.getUsername());
        remoteServerLoggerData.setPassword(remoteLoggingConfig.getPassword());
        remoteServerLoggerData.setKeystoreLocation(remoteLoggingConfig.getKeystoreLocation());
        remoteServerLoggerData.setKeystorePassword(remoteLoggingConfig.getKeystorePassword());
        remoteServerLoggerData.setTruststoreLocation(remoteLoggingConfig.getTruststoreLocation());
        remoteServerLoggerData.setTruststorePassword(remoteLoggingConfig.getTruststorePassword());
        return remoteServerLoggerData;
    }

    private List<AuthenticatorListItem> buildAuthenticatorListResponse(
            LocalAuthenticatorConfig[] localConfigs, RequestPathAuthenticatorConfig[] requestPathConfigs) {

        List<AuthenticatorListItem> authenticatorListItems = new ArrayList<>();
        if (localConfigs != null) {
            for (LocalAuthenticatorConfig config : localConfigs) {
                AuthenticatorListItem authenticatorListItem = new AuthenticatorListItem();
                String authenticatorId = base64URLEncode(config.getName());
                authenticatorListItem.setId(authenticatorId);
                authenticatorListItem.setName(config.getName());
                authenticatorListItem.setDisplayName(config.getDisplayName());
                authenticatorListItem.setIsEnabled(config.isEnabled());
                authenticatorListItem.setType(AuthenticatorListItem.TypeEnum.LOCAL);
                authenticatorListItem.setDefinedBy(
                        AuthenticatorListItem.DefinedByEnum.valueOf(config.getDefinedByType().toString()));
                String[] tags = config.getTags();
                if (ArrayUtils.isNotEmpty(tags)) {
                    authenticatorListItem.setTags(Arrays.asList(tags));
                }
                authenticatorListItem.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                        CONFIGS_AUTHENTICATOR_PATH_COMPONENT + PATH_SEPERATOR + "%s", authenticatorId)).toString());
                authenticatorListItems.add(authenticatorListItem);
            }
        }
        if (requestPathConfigs != null) {
            for (RequestPathAuthenticatorConfig config : requestPathConfigs) {
                AuthenticatorListItem authenticatorListItem = new AuthenticatorListItem();
                String authenticatorId = base64URLEncode(config.getName());
                authenticatorListItem.setId(authenticatorId);
                authenticatorListItem.setName(config.getName());
                authenticatorListItem.setDisplayName(config.getDisplayName());
                authenticatorListItem.setIsEnabled(config.isEnabled());
                authenticatorListItem.setType(AuthenticatorListItem.TypeEnum.REQUEST_PATH);
                authenticatorListItem.setDefinedBy(AuthenticatorListItem.DefinedByEnum.SYSTEM);
                String[] tags = config.getTags();
                if (ArrayUtils.isNotEmpty(tags)) {
                    authenticatorListItem.setTags(Arrays.asList(tags));
                }
                authenticatorListItem.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                        CONFIGS_AUTHENTICATOR_PATH_COMPONENT + PATH_SEPERATOR + "%s", authenticatorId)).toString());
                authenticatorListItems.add(authenticatorListItem);
            }
        }
        return authenticatorListItems;
    }

    private LocalAuthenticatorConfig getAuthenticatorById(LocalAuthenticatorConfig[] authenticatorConfigs,
                                                          String authenticatorId) {

        String authenticatorName = base64URLDecode(authenticatorId);
        for (LocalAuthenticatorConfig config : authenticatorConfigs) {
            if (StringUtils.equals(authenticatorName, config.getName())) {
                return config;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Unable to find a local authenticator with the name: " + authenticatorName);
        }
        return null;
    }

    private RequestPathAuthenticatorConfig getAuthenticatorById(RequestPathAuthenticatorConfig[] authenticatorConfigs,
                                                                String authenticatorId) {

        String authenticatorName = base64URLDecode(authenticatorId);
        for (RequestPathAuthenticatorConfig config : authenticatorConfigs) {
            if (StringUtils.equals(authenticatorName, config.getName())) {
                return config;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Unable to find a request path authenticator with the name: " + authenticatorName);
        }
        return null;
    }

    private Authenticator buildAuthenticatorResponse(LocalAuthenticatorConfig config)
            throws IdentityApplicationManagementServerException {

        Authenticator authenticator = new Authenticator();
        authenticator.setId(base64URLEncode(config.getName()));
        authenticator.setName(config.getName());
        authenticator.setDisplayName(config.getDisplayName());
        authenticator.setIsEnabled(config.isEnabled());
        if (config instanceof RequestPathAuthenticatorConfig) {
            authenticator.setType(Authenticator.TypeEnum.REQUEST_PATH);
            authenticator.setDefinedBy(Authenticator.DefinedByEnum.SYSTEM);
            setAuthenticatorProperties(config, authenticator);
        } else {
            authenticator.setType(Authenticator.TypeEnum.LOCAL);
            if (AuthenticatorPropertyConstants.DefinedByType.USER == config.getDefinedByType()) {
                authenticator.setDefinedBy(Authenticator.DefinedByEnum.USER);
                resolveEndpointConfiguration(authenticator, config);
            } else {
                authenticator.setDefinedBy(Authenticator.DefinedByEnum.SYSTEM);
                setAuthenticatorProperties(config, authenticator);
            }
        }
        String[] tags = config.getTags();
        if (ArrayUtils.isNotEmpty(tags)) {
            authenticator.setTags(Arrays.asList(tags));
        }
        return authenticator;
    }

    private void resolveEndpointConfiguration(Authenticator authenticator, LocalAuthenticatorConfig config)
            throws IdentityApplicationManagementServerException {

        try {
            UserDefinedLocalAuthenticatorConfig userDefinedConfig = (UserDefinedLocalAuthenticatorConfig) config;
            UserDefinedAuthenticatorEndpointConfig endpointConfig = userDefinedConfig.getEndpointConfig();

            AuthenticationType authenticationType = new AuthenticationType();
            authenticationType.setType(AuthenticationType.TypeEnum.fromValue(
                    endpointConfig.getAuthenticatorEndpointAuthenticationType()));
            authenticationType.setProperties(new HashMap<>(
                    endpointConfig.getAuthenticatorEndpointAuthenticationProperties()));

            Endpoint endpoint = new Endpoint();
            endpoint.setAuthentication(authenticationType);
            endpoint.setUri(endpointConfig.getAuthenticatorEndpointUri());
            authenticator.addEndpointItem(endpoint);
        } catch (ClassCastException e) {
            throw new IdentityApplicationManagementServerException(String.format("For authenticator: %s of " +
                    "definedBy: USER,  the authenticator config must be an instance of " +
                    "UserDefinedAuthenticatorEndpointConfig", config.getName()) , e);
        }
    }

    private void setAuthenticatorProperties(LocalAuthenticatorConfig config, Authenticator authenticator) {

        List<AuthenticatorProperty> authenticatorProperties = Arrays.stream(config.getProperties())
                .map(propertyToExternal).collect(Collectors.toList());
        authenticator.setProperties(authenticatorProperties);
    }

    private Function<Property, AuthenticatorProperty> propertyToExternal = property -> {

        AuthenticatorProperty authenticatorProperty = new AuthenticatorProperty();
        authenticatorProperty.setKey(property.getName());
        authenticatorProperty.setValue(property.getValue());
        return authenticatorProperty;
    };

    private ProvisioningConfig buildProvisioningConfig() {

        ProvisioningConfig provisioningConfig = new ProvisioningConfig();
        InboundConfig inboundConfig = new InboundConfig();
        inboundConfig.setScim(getInboundScimConfig());
        provisioningConfig.setInbound(inboundConfig);
        return provisioningConfig;
    }

    /**
     * Evaluate the list of patch operations and update the root level attributes of the ServerConfig accordingly.
     *
     * @param patchRequest List of patch operations.
     * @param idpToUpdate  Resident Identity Provider to be updated.
     */
    private void processPatchRequest(List<Patch> patchRequest, IdentityProvider idpToUpdate) {

        if (CollectionUtils.isEmpty(patchRequest)) {
            return;
        }
        for (Patch patch : patchRequest) {
            String path = patch.getPath();
            Patch.OperationEnum operation = patch.getOperation();
            String value = patch.getValue();
            // We support only 'REPLACE', 'ADD' and 'REMOVE' patch operations.
            if (operation == Patch.OperationEnum.REPLACE) {
                if (path.matches(Constants.HOME_REALM_PATH_REGEX) && path.split(Constants.PATH_SEPERATOR).length == 3) {

                    int index = Integer.parseInt(path.split(Constants.PATH_SEPERATOR)[2]);
                    String[] homeRealmArr = StringUtils.split(idpToUpdate.getHomeRealmId(), ",");
                    if (ArrayUtils.isNotEmpty(homeRealmArr) && (index >= 0)
                            && (index < homeRealmArr.length)) {
                        List<String> homeRealmIds = Arrays.asList(homeRealmArr);
                        homeRealmIds.set(index, value);
                        idpToUpdate.setHomeRealmId(StringUtils.join(homeRealmIds, ","));
                    } else {
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, "Invalid index in 'path' attribute");
                    }
                } else {
                    switch (path) {
                        case Constants.IDLE_SESSION_PATH:
                            updateIdPProperty(idpToUpdate, IdentityApplicationConstants.SESSION_IDLE_TIME_OUT, value);
                            break;
                        case Constants.REMEMBER_ME_PATH:
                            updateIdPProperty(idpToUpdate, IdentityApplicationConstants.REMEMBER_ME_TIME_OUT, value);
                            break;
                        default:
                            throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                    .ERROR_CODE_INVALID_INPUT, "Unsupported value for 'path' attribute");
                    }
                }
            } else if (operation == Patch.OperationEnum.ADD && path.matches(Constants.HOME_REALM_PATH_REGEX) && path
                    .split(Constants.PATH_SEPERATOR).length == 3) {
                List<String> homeRealmIds;
                int index = Integer.parseInt(path.split(Constants.PATH_SEPERATOR)[2]);
                String[] homeRealmArr = StringUtils.split(idpToUpdate.getHomeRealmId(), ",");
                if (ArrayUtils.isNotEmpty(homeRealmArr) && (index >= 0) && index <= homeRealmArr.length) {
                    homeRealmIds = new ArrayList<>(Arrays.asList(homeRealmArr));
                    homeRealmIds.add(index, value);
                } else if (index == 0) {
                    homeRealmIds = new ArrayList<>();
                    homeRealmIds.add(value);
                } else {
                    throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT, "Invalid index in 'path' attribute");
                }
                idpToUpdate.setHomeRealmId(StringUtils.join(homeRealmIds, ","));
            } else if (operation == Patch.OperationEnum.REMOVE && path.matches(Constants.HOME_REALM_PATH_REGEX) &&
                    path.split(Constants.PATH_SEPERATOR).length == 3) {
                List<String> homeRealmIds;
                int index = Integer.parseInt(path.split(Constants.PATH_SEPERATOR)[2]);
                String[] homeRealmArr = StringUtils.split(idpToUpdate.getHomeRealmId(), ",");
                if (ArrayUtils.isNotEmpty(homeRealmArr) && (index >= 0) && index < homeRealmArr.length) {
                    homeRealmIds = new ArrayList<>(Arrays.asList(homeRealmArr));
                    homeRealmIds.remove(index);
                } else {
                    throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT, "Invalid index in 'path' attribute");
                }
                idpToUpdate.setHomeRealmId(StringUtils.join(homeRealmIds, ","));
            } else {
                // Throw an error if any other patch operations are sent in the request.
                throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                        .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
            }
        }
    }

    private void updateIdPProperty(IdentityProvider identityProvider, String key, String value) {

        List<IdentityProviderProperty> idPProperties = new ArrayList<>(Arrays.asList(identityProvider
                .getIdpProperties()));
        if (StringUtils.isBlank(value) || !StringUtils.isNumeric(value) || Integer.parseInt(value) <= 0) {
            String message = "Value should be numeric and positive";
            throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage.ERROR_CODE_INVALID_INPUT,
                    message);
        }
        boolean isPropertyFound = false;
        if (CollectionUtils.isNotEmpty(idPProperties)) {
            for (IdentityProviderProperty property : idPProperties) {
                if (StringUtils.equals(key, property.getName())) {
                    isPropertyFound = true;
                    property.setValue(value);
                }
            }
        }
        if (!isPropertyFound) {
            IdentityProviderProperty property = new IdentityProviderProperty();
            property.setName(key);
            property.setDisplayName(key);
            property.setValue(value);
            idPProperties.add(property);
        }
        identityProvider.setIdpProperties(idPProperties.toArray(new IdentityProviderProperty[0]));
    }

    private IdentityProvider getResidentIdP() {

        IdentityProvider residentIdP;
        try {
            residentIdP = ConfigsServiceHolder.getInstance().getIdentityProviderManager().getResidentIdP(ContextLoader
                    .getTenantDomainFromContext());
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CONFIGS, null);
        }

        // Resident Identity Provider can be null only due to an internal server error.
        if (residentIdP == null) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_RETRIEVING_CONFIGS, null);
        }
        return residentIdP;
    }

    private ServiceProvider getResidentApplication() {

        ServiceProvider residentSP;
        try {
            residentSP = ConfigsServiceHolder.getInstance().getApplicationManagementService()
                    .getServiceProvider(ApplicationConstants.LOCAL_SP, ContextLoader.getTenantDomainFromContext());
        } catch (IdentityApplicationManagementException e) {
            throw handleApplicationMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_CONFIGS, null);
        }

        // Resident Service Provider can be null only due to an internal server error.
        if (residentSP == null) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_RETRIEVING_CONFIGS, null);
        }
        return residentSP;
    }

    /**
     * Create a shallow copy of the input Identity Provider.
     *
     * @param idP Identity Provider.
     * @return Clone of IDP.
     */
    private IdentityProvider createIdPClone(IdentityProvider idP) {

        try {
            Gson gson = new Gson();
            IdentityProvider clonedIdentityProvider = gson.fromJson(gson.toJson(idP), IdentityProvider.class);
            return clonedIdentityProvider;
        } catch (JsonSyntaxException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_UPDATING_CONFIGS, null);
        }
    }

    /**
     * Create a shallow copy of the input Service Provider.
     *
     * @param serviceProvider Service Provider.
     * @return Clone of Application.
     */
    private ServiceProvider createApplicationClone(ServiceProvider serviceProvider) {

        try {
            return (ServiceProvider) BeanUtils.cloneBean(serviceProvider);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException
                e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, Constants.ErrorMessage
                    .ERROR_CODE_ERROR_UPDATING_CONFIGS, null);
        }
    }

    /**
     * Handle IdentityApplicationManagementException, extract error code, error description and status code to be sent
     * in the response.
     *
     * @param e         IdentityApplicationManagementException
     * @param errorEnum Error Message information.
     * @return APIError.
     */
    private APIError handleApplicationMgtException(IdentityApplicationManagementException e,
                                                   Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof IdentityApplicationManagementClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof IdentityApplicationManagementServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle IdentityProviderManagementException, extract error code, error description and status code to be sent
     * in the response.
     *
     * @param e         IdentityProviderManagementException.
     * @param errorEnum Error Message information.
     * @return APIError.
     */
    private APIError handleIdPException(IdentityProviderManagementException e,
                                        Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof IdentityProviderManagementClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof IdentityProviderManagementServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleCORSException(CORSManagementServiceException e,
                                         Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof CORSManagementServiceClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof CORSManagementServiceServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    private APIError handleImpersonationConfigException(ImpersonationConfigMgtException e,
                                         Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse;

        Response.Status status;

        if (e instanceof ImpersonationConfigMgtClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof ImpersonationConfigMgtServerException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, Constants.ErrorMessage error, String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.code()).withMessage(errorMsg.message())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Constant.ErrorMessage.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(Constants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.description(), data);
        } else {
            message = error.description();
        }
        return message;
    }

    /**
     * Get the private key JWT validator configuration.
     *
     * @return JWTValidatorConfig  JWTValidatorConfig.
     */
    public JWTValidatorConfig getPrivateKeyJWTValidatorConfiguration() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            if (JWTAuthenticationMgtOGSiServiceFactory.getInstance() != null) {

                return new JWTValidatorConfig()
                        .enableTokenReuse(JWTAuthenticationMgtOGSiServiceFactory.getInstance()
                                .getPrivateKeyJWTClientAuthenticatorConfiguration(tenantDomain).isEnableTokenReuse());
            }
            throw new JWTClientAuthenticatorException(ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND.message(),
                    ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND.code());

        } catch (Exception e) {
            if (ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND.message().equals(e.getMessage())) {
                throw handleNotFoundError(ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND);
            } else {
                throw JWTConnectorUtil.handlePrivateKeyJWTValidationException(e,
                        Constants.ErrorMessage.ERROR_CODE_PRIVATE_KEY_JWT_VALIDATOR_CONFIG_RETRIEVE,
                        null);
            }
        }
    }

    /**
     * Patch the Private Key JWT validation Authenticator config of a tenant.
     *
     * @param privateKeyJWTValidatorPatchList List of patch operations.
     */
    public void patchPrivateKeyJWTValidatorSConfig(List<JWTKeyValidatorPatch> privateKeyJWTValidatorPatchList) {

        if (CollectionUtils.isEmpty(privateKeyJWTValidatorPatchList)) {
            return;
        }

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        JWTValidatorConfig jwtValidatorConfig = null;
        try {
            if (JWTAuthenticationMgtOGSiServiceFactory.getInstance() != null) {
                jwtValidatorConfig = JWTConnectorUtil.getJWTValidatorConfig(tenantDomain);
            } else {
                throw new JWTClientAuthenticatorException(ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND.message(),
                        ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND.code());
            }
        } catch (Exception e) {
            if (e.getMessage().equals(ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND.message())) {
                throw handleNotFoundError(ERROR_JWT_AUTHENTICATOR_SERVICE_NOT_FOUND);
            } else {
                throw JWTConnectorUtil.handlePrivateKeyJWTValidationException(e,
                        Constants.ErrorMessage.ERROR_CODE_CORS_CONFIG_RETRIEVE, null);
            }
        }

        try {
            for (JWTKeyValidatorPatch jwtKeyValidatorPatch : privateKeyJWTValidatorPatchList) {
                String path = jwtKeyValidatorPatch.getPath();
                JWTKeyValidatorPatch.OperationEnum operation = jwtKeyValidatorPatch.getOperation();
                Boolean value = jwtKeyValidatorPatch.getValue();

                // We support only 'REPLACE' and 'ADD' patch operations.
                if (operation == JWTKeyValidatorPatch.OperationEnum.REPLACE) {
                    if (path.matches(Constants.PRIVATE_KEY_JWT_VALIDATION_CONFIG_TOKEN_REUSE)) {
                        jwtValidatorConfig.setEnableTokenReuse(value);
                    } else {
                        // Throw an error if any other patch operations are sent in the request.
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                    }
                } else if (operation == JWTKeyValidatorPatch.OperationEnum.ADD) {
                    if (path.matches(Constants.PRIVATE_KEY_JWT_VALIDATION_CONFIG_TOKEN_REUSE)) {
                        jwtValidatorConfig.setEnableTokenReuse(value);
                    } else {
                        // Throw an error if any other patch operations are sent in the request.
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                    }
                } else {
                    // Throw an error if any other patch operations are sent in the request.
                    throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                }
            }

            // Set the patched configuration object as the new JWT Authentication configuration for the tenant.
            if (JWTAuthenticationMgtOGSiServiceFactory.getInstance() != null) {
                JWTAuthenticationMgtOGSiServiceFactory.getInstance()
                        .setPrivateKeyJWTClientAuthenticatorConfiguration
                                (JWTConnectorUtil.getJWTDaoConfig(jwtValidatorConfig),
                                        tenantDomain);
            }
        } catch (Exception e) {
            throw JWTConnectorUtil.handlePrivateKeyJWTValidationException(e,
                    Constants.ErrorMessage.ERROR_CODE_PRIVATE_KEY_JWT_VALIDATOR_CONFIG_UPDATE, null);
        }
    }

    /**
     * Get the DCR Configuration.
     *
     * @return DCRConfig  DCRConfig.
     */
    public DCRConfig getDCRConfiguration() {

        try {

            return DCRConnectorUtil.getDCRConfig();
        } catch (DCRMException e) {
            throw DCRConnectorUtil.handleDCRConfigException(e, Constants.ErrorMessage.ERROR_CODE_DCR_CONFIG_RETRIEVE,
                    null);
        }
    }

    /**
     * Patch the DCR config of a tenant.
     *
     * @param dcrPatchList List of patch operations.
     */
    public void patchDCRConfig(List<DCRPatch> dcrPatchList) {

        if (CollectionUtils.isEmpty(dcrPatchList)) {
            return;
        }

        DCRConfig dcrConfig = null;
        try {
            dcrConfig = DCRConnectorUtil.getDCRConfig();
        } catch (DCRMException e) {
            throw DCRConnectorUtil.handleDCRConfigException(e,
                    Constants.ErrorMessage.ERROR_CODE_DCR_CONFIG_RETRIEVE, null);
        }

            for (DCRPatch dcrPatch : dcrPatchList) {
                String path = dcrPatch.getPath();
                DCRPatch.OperationEnum operation = dcrPatch.getOperation();

                // We support only 'REPLACE' patch operation.
                if (operation == DCRPatch.OperationEnum.REPLACE) {
                    if (path.matches(Constants.DCR_CONFIG_ENABLE_FAPI_ENFORCEMENT)) {
                        String value = dcrPatch.getValue();
                        if (Objects.equals(value, "true")) {
                            dcrConfig.setEnableFapiEnforcement(true);
                        } else if (Objects.equals(value, "false")) {
                            dcrConfig.setEnableFapiEnforcement(false);
                        } else {
                            throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                    .ERROR_CODE_INVALID_INPUT, "Unsupported patch value for the given path");
                        }
                    } else if (path.matches(Constants.DCR_CONFIG_AUTHENTICATION_REQUIRED)) {
                        String value = dcrPatch.getValue();
                        if (Objects.equals(value, "true")) {
                            dcrConfig.setAuthenticationRequired(true);
                        } else if (Objects.equals(value, "false")) {
                            dcrConfig.setAuthenticationRequired(false);
                        } else {
                            throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                    .ERROR_CODE_INVALID_INPUT, "Unsupported patch value for the given path");
                        }
                    } else if (path.matches(Constants.DCR_CONFIG_MANDATE_SSA)) {
                        String value = dcrPatch.getValue();
                        if (Objects.equals(value, "true")) {
                            dcrConfig.setMandateSSA(true);
                        } else if (Objects.equals(value, "false")) {
                            dcrConfig.setMandateSSA(false);
                        } else {
                            throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                    .ERROR_CODE_INVALID_INPUT, "Unsupported patch value for the given path");
                        }
                    } else if (path.matches(Constants.DCR_CONFIG_SSA_JWKS)) {
                        String value = dcrPatch.getValue();
                        dcrConfig.setSsaJwks(value);
                    } else {
                        // Throw an error if any other patch operations are sent in the request.
                        throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                                .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                    }
                } else {
                    // Throw an error if any other patch operations are sent in the request.
                    throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT, "Unsupported patch operation");
                }
            }

            // Set the patched configuration object as the new DCR configuration for the tenant.
        try {
            DCRConnectorUtil.setDCRConfig(dcrConfig);
        } catch (DCRMException e) {
            throw DCRConnectorUtil.handleDCRConfigException(e,
                    Constants.ErrorMessage.ERROR_CODE_DCR_CONFIG_UPDATE, e.getMessage());
        }

    }

    /**
     * Extract the required arguments and build a not found error.
     *
     * @return APIError with exception code, message and description.
     */
    private APIError handleNotFoundError(Constants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse = new ErrorResponse.Builder()
                .withCode(errorEnum.code())
                .withMessage(errorEnum.message())
                .withDescription(errorEnum.description())
                .build(log, errorEnum.description());

        Response.Status status = Response.Status.NOT_FOUND;
        return new APIError(status, errorResponse);
    }

    public RemoteServerLoggerData getRemoteServerConfig(String logType) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        validateTenantDomain(tenantDomain, "Getting remote server configuration service is not available for %s");
        validateLogType(logType);
        try {
            // Backend logic only supports logType in Uppercase.
            return ConfigsServiceHolder.getInstance().getRemoteLoggingConfigService().getRemoteServerConfig(
                    logType.toUpperCase(Locale.ENGLISH));
        } catch (ConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_GETTING_REMOTE_LOGGING_CONFIGS, null);
        }
    }

    public List<RemoteServerLoggerData> getRemoteServerConfigs() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        validateTenantDomain(tenantDomain, "Listing remote server configuration service is not available for %s");
        try {
            return ConfigsServiceHolder.getInstance().getRemoteLoggingConfigService().getRemoteServerConfigs();
        } catch (ConfigurationException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_GETTING_REMOTE_LOGGING_CONFIGS, null);
        }
    }

    /**
     * Get the SAML inbound authentication configuration for the tenant.
     */
    public InboundAuthSAML2Config getSAMLInboundAuthConfig() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        InboundAuthSAML2Config inboundAuthConfig = new InboundAuthSAML2Config();
        try {
            IdentityProvider residentIdp = ConfigsServiceHolder.getInstance().getIdentityProviderManager()
                    .getResidentIdP(tenantDomain);
            if (residentIdp != null) {
                FederatedAuthenticatorConfig federatedAuthConfig = IdentityApplicationManagementUtil
                        .getFederatedAuthenticator(residentIdp.getFederatedAuthenticatorConfigs(),
                                IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                if (federatedAuthConfig == null) {
                    throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                            Constants.ErrorMessage.ERROR_CODE_FEDERATED_AUTHENTICATOR_CONFIG_NOT_FOUND,
                            IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                }

                Property[] idpProperties = federatedAuthConfig.getProperties();
                if (idpProperties == null || idpProperties.length == 0) {
                    throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                            Constants.ErrorMessage.ERROR_CODE_FEDERATED_AUTHENTICATOR_PROPERTIES_NOT_FOUND,
                            IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                }

                inboundAuthConfig.setDestinationURLs(IdentityApplicationManagementUtil
                        .getPropertyValuesForNameStartsWith(idpProperties,
                                IdentityApplicationConstants.Authenticator.SAML2SSO.DESTINATION_URL_PREFIX));

                String metadataValidityPeriod = IdentityApplicationManagementUtil.getPropertyValue(idpProperties,
                        IdentityApplicationConstants.Authenticator.SAML2SSO.SAML_METADATA_VALIDITY_PERIOD);
                if (StringUtils.isNotEmpty(metadataValidityPeriod)) {
                    inboundAuthConfig.setMetadataValidityPeriod(Integer.parseInt(metadataValidityPeriod));
                }

                inboundAuthConfig.setEnableMetadataSigning(Boolean.parseBoolean(
                        IdentityApplicationManagementUtil.getPropertyValue(idpProperties,
                                IdentityApplicationConstants.Authenticator.SAML2SSO.SAML_METADATA_SIGNING_ENABLED)));

                // Construct and set the SAML metadata download endpoint.
                String samlMetadataEndpoint;
                if (MultitenantConstants.SUPER_TENANT_DOMAIN_NAME.equals(tenantDomain)) {
                    samlMetadataEndpoint = ServiceURLBuilder.create().addPath(
                            Constants.SAML2_METADATA_ENDPOINT_URI_PATH).build().getAbsolutePublicURL();
                } else {
                    samlMetadataEndpoint = ServiceURLBuilder.create().addPath("/t/" + tenantDomain +
                            Constants.SAML2_METADATA_ENDPOINT_URI_PATH).build().getAbsolutePublicURL();
                }
                inboundAuthConfig.setMetadataEndpoint(samlMetadataEndpoint);
            } else {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        Constants.ErrorMessage.ERROR_CODE_RESIDENT_IDP_NOT_FOUND, tenantDomain);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_SAML_INBOUND_AUTH_CONFIG_RETRIEVE, null);
        } catch (URLBuilderException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_SAML_INBOUND_AUTH_CONFIG_RETRIEVE, null);
        }

        return inboundAuthConfig;
    }

    /**
     * Update the SAML inbound authentication configuration for the tenant.
     *
     * @param authConfigToUpdate SAML inbound authentication configs to update.
     */
    public void updateSAMLInboundAuthConfig(InboundAuthSAML2Config authConfigToUpdate) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        validateSAMLAuthConfigUpdate(authConfigToUpdate);

        try {
            IdentityProvider residentIdp = ConfigsServiceHolder.getInstance().getIdentityProviderManager()
                    .getResidentIdP(tenantDomain);
            if (residentIdp != null) {
                FederatedAuthenticatorConfig federatedAuthConfig = IdentityApplicationManagementUtil
                        .getFederatedAuthenticator(residentIdp.getFederatedAuthenticatorConfigs(),
                                IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                if (federatedAuthConfig == null) {
                    throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                            Constants.ErrorMessage.ERROR_CODE_FEDERATED_AUTHENTICATOR_CONFIG_NOT_FOUND,
                            IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                }

                Property[] idpProperties = federatedAuthConfig.getProperties();
                if (idpProperties == null) {
                    throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                            Constants.ErrorMessage.ERROR_CODE_FEDERATED_AUTHENTICATOR_PROPERTIES_NOT_FOUND,
                            IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                }
                Property[] updatedIdpProperties = getUpdatedSAMLFederatedAuthConfigProperties(idpProperties,
                        authConfigToUpdate);
                federatedAuthConfig.setProperties(updatedIdpProperties);
                residentIdp.setFederatedAuthenticatorConfigs(new FederatedAuthenticatorConfig[]{federatedAuthConfig});
                ConfigsServiceHolder.getInstance().getIdentityProviderManager().updateResidentIdP(
                        residentIdp, tenantDomain);
            } else {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        Constants.ErrorMessage.ERROR_CODE_RESIDENT_IDP_NOT_FOUND, tenantDomain);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_SAML_INBOUND_AUTH_CONFIG_UPDATE, null);
        }
    }

    private Property[] getUpdatedSAMLFederatedAuthConfigProperties(Property[] properties,
                                                                   InboundAuthSAML2Config authConfigToUpdate) {

        List<Property> updatedPropertyList = new ArrayList<>();

        for (Property property : properties) {
            if (property.getName().equals(
                    IdentityApplicationConstants.Authenticator.SAML2SSO.SAML_METADATA_VALIDITY_PERIOD)) {
                if (authConfigToUpdate.getMetadataValidityPeriod() != null) {
                    property.setValue(Integer.toString(authConfigToUpdate.getMetadataValidityPeriod()));
                }
                updatedPropertyList.add(property);
            } else if (property.getName().equals(
                    IdentityApplicationConstants.Authenticator.SAML2SSO.SAML_METADATA_SIGNING_ENABLED)) {
                if (authConfigToUpdate.getEnableMetadataSigning() != null) {
                    property.setValue(Boolean.toString(authConfigToUpdate.getEnableMetadataSigning()));
                }
                updatedPropertyList.add(property);
            } else if (!property.getName().startsWith(
                    IdentityApplicationConstants.Authenticator.SAML2SSO.DESTINATION_URL_PREFIX)) {
                updatedPropertyList.add(property);
            } else if (authConfigToUpdate.getDestinationURLs() == null) {
                updatedPropertyList.add(property);
            }
        }

        // Update destination URLs.
        if (authConfigToUpdate.getDestinationURLs() != null) {
            int destUriIndex = 1;
            for (String destinationURL : authConfigToUpdate.getDestinationURLs()) {
                Property property = new Property();
                property.setName(IdentityApplicationConstants.Authenticator.SAML2SSO.DESTINATION_URL_PREFIX +
                        "." + destUriIndex);
                property.setValue(destinationURL);
                updatedPropertyList.add(property);
                destUriIndex += 1;
            }
        }

        return updatedPropertyList.toArray(new Property[0]);
    }

    private void validateSAMLAuthConfigUpdate(InboundAuthSAML2Config authConfigToUpdate) {

        if (authConfigToUpdate.getDestinationURLs() != null && authConfigToUpdate.getDestinationURLs().isEmpty()) {
            throw handleException(Response.Status.BAD_REQUEST, Constants.ErrorMessage.ERROR_CODE_INVALID_INPUT,
                    "Should contain at least one destination URL");
        }
    }

    /**
     * Get the Passive STS inbound authentication configuration for the tenant.
     */
    public InboundAuthPassiveSTSConfig getPassiveSTSInboundAuthConfig() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        InboundAuthPassiveSTSConfig inboundAuthConfig = new InboundAuthPassiveSTSConfig();
        try {
            IdentityProvider residentIdp = ConfigsServiceHolder.getInstance().getIdentityProviderManager()
                    .getResidentIdP(tenantDomain);
            if (residentIdp != null) {
                FederatedAuthenticatorConfig federatedAuthConfig = IdentityApplicationManagementUtil
                        .getFederatedAuthenticator(residentIdp.getFederatedAuthenticatorConfigs(),
                                IdentityApplicationConstants.Authenticator.PassiveSTS.NAME);
                if (federatedAuthConfig == null) {
                    throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                            Constants.ErrorMessage.ERROR_CODE_FEDERATED_AUTHENTICATOR_CONFIG_NOT_FOUND,
                            IdentityApplicationConstants.Authenticator.PassiveSTS.NAME);
                }

                Property[] idpProperties = federatedAuthConfig.getProperties();
                if (idpProperties == null || idpProperties.length == 0) {
                    throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                            Constants.ErrorMessage.ERROR_CODE_FEDERATED_AUTHENTICATOR_PROPERTIES_NOT_FOUND,
                            IdentityApplicationConstants.Authenticator.PassiveSTS.NAME);
                }

                inboundAuthConfig.setPassiveSTSUrl(IdentityApplicationManagementUtil.getPropertyValue(idpProperties,
                        IdentityApplicationConstants.Authenticator.PassiveSTS.IDENTITY_PROVIDER_URL));

                /*
                 Note: SAML 'samlAuthnRequestsSigningEnabled' property is used as the authentication request
                 signing property of passive STS in the management console. Hence, the same property is used
                 in the API.
                */
                FederatedAuthenticatorConfig samlFederatedAuthConfig = IdentityApplicationManagementUtil
                        .getFederatedAuthenticator(residentIdp.getFederatedAuthenticatorConfigs(),
                                IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                if (samlFederatedAuthConfig != null) {
                    inboundAuthConfig.setEnableRequestSigning(Boolean.parseBoolean(IdentityApplicationManagementUtil
                            .getPropertyValue(samlFederatedAuthConfig.getProperties(), IdentityApplicationConstants
                                    .Authenticator.SAML2SSO.SAML_METADATA_AUTHN_REQUESTS_SIGNING_ENABLED)));
                }
            } else {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        Constants.ErrorMessage.ERROR_CODE_RESIDENT_IDP_NOT_FOUND, tenantDomain);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_PASSIVE_STS_INBOUND_AUTH_CONFIG_RETRIEVE, null);
        }

        return inboundAuthConfig;
    }

    /**
     * Update the Passive STS inbound authentication configuration for the tenant.
     *
     * @param authConfigToUpdate Passive STS inbound authentication configs to update.
     */
    public void updatePassiveSTSInboundAuthConfig(InboundAuthPassiveSTSConfig authConfigToUpdate) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            IdentityProvider residentIdp = ConfigsServiceHolder.getInstance().getIdentityProviderManager()
                    .getResidentIdP(tenantDomain);
            if (residentIdp != null) {
                /*
                 Note: SAML 'samlAuthnRequestsSigningEnabled' property is used as the authentication request
                 signing property of passive STS in the management console. Hence, the same property is used
                 in the API.
                */
                FederatedAuthenticatorConfig federatedAuthConfig = IdentityApplicationManagementUtil
                        .getFederatedAuthenticator(residentIdp.getFederatedAuthenticatorConfigs(),
                                IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                if (federatedAuthConfig == null) {
                    throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                            Constants.ErrorMessage.ERROR_CODE_FEDERATED_AUTHENTICATOR_CONFIG_NOT_FOUND,
                            IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                }

                Property[] idpProperties = federatedAuthConfig.getProperties();
                if (idpProperties == null) {
                    throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                            Constants.ErrorMessage.ERROR_CODE_FEDERATED_AUTHENTICATOR_PROPERTIES_NOT_FOUND,
                            IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
                }

                for (Property property : idpProperties) {
                    if (property.getName().equals(IdentityApplicationConstants.Authenticator
                            .SAML2SSO.SAML_METADATA_AUTHN_REQUESTS_SIGNING_ENABLED)
                            && authConfigToUpdate.getEnableRequestSigning() != null) {
                        property.setValue(Boolean.toString(authConfigToUpdate.getEnableRequestSigning()));
                    }
                }
                residentIdp.setFederatedAuthenticatorConfigs(new FederatedAuthenticatorConfig[]{federatedAuthConfig});
                ConfigsServiceHolder.getInstance().getIdentityProviderManager().updateResidentIdP(
                        residentIdp, tenantDomain);
            } else {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR,
                        Constants.ErrorMessage.ERROR_CODE_RESIDENT_IDP_NOT_FOUND, tenantDomain);
            }
        } catch (IdentityProviderManagementException e) {
            throw handleIdPException(e,
                    Constants.ErrorMessage.ERROR_CODE_ERROR_PASSIVE_STS_INBOUND_AUTH_CONFIG_UPDATE, null);
        }
    }
}
