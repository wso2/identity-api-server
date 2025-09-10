/*
 * Copyright (c) 2023-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.configs.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.configs.v1.ConfigsApiService;
import org.wso2.carbon.identity.api.server.configs.v1.core.ServerConfigManagementService;
import org.wso2.carbon.identity.api.server.configs.v1.factories.ServerConfigManagementServiceFactory;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.DCRPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.ImpersonationPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthPassiveSTSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthSAML2Config;
import org.wso2.carbon.identity.api.server.configs.v1.model.JWTKeyValidatorPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.Patch;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfigListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.ScimConfig;
import org.wso2.carbon.logging.service.data.RemoteServerLoggerData;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Server Configurations Rest API.
 */
public class ConfigsApiServiceImpl implements ConfigsApiService {

    private static final Log log = LogFactory.getLog(ConfigsApiServiceImpl.class);
    private final ServerConfigManagementService configManagementService;

    public ConfigsApiServiceImpl() {

        try {
            configManagementService = ServerConfigManagementServiceFactory.getServerConfigManagementService();
            log.info("Server config management service initialized successfully.");
        } catch (Exception e) {
            log.error("Error occurred while initiating server config management services.", e);
            throw new RuntimeException("Error occurred while initiating server config management services.", e);
        }
    }

    @Override
    public Response getAuthenticator(String authenticatorId) {

        log.info("Retrieving authenticator configuration for authenticatorId: " + authenticatorId);
        return Response.ok().entity(configManagementService.getAuthenticator(authenticatorId)).build();
    }

    @Override
    public Response getCORSConfiguration() {

        log.info("Retrieving CORS configuration.");
        return Response.ok().entity(configManagementService.getCORSConfiguration()).build();
    }

    @Override
    public Response getConfigs() {

        log.info("Retrieving server configurations.");
        return Response.ok().entity(configManagementService.getConfigs()).build();
    }

    @Override
    public Response getHomeRealmIdentifiers() {
        log.info("Retrieving home realm identifiers.");
        return Response.ok().entity(configManagementService.getHomeRealmIdentifiers()).build();
    }

    @Override
    public Response getImpersonationConfiguration() {

        log.info("Retrieving impersonation configuration.");
        return Response.ok().entity(configManagementService.getImpersonationConfiguration()).build();
    }

    @Override
    public Response getInboundScimConfigs() {

        log.info("Retrieving inbound SCIM configurations.");
        return Response.ok().entity(configManagementService.getInboundScimConfig()).build();
    }

    @Override
    public Response getSchema(String schemaId) {

        log.info("Retrieving schema configuration for schemaId: " + schemaId);
        return Response.ok().entity(configManagementService.getSchema(schemaId)).build();
    }

    @Override
    public Response getPrivatKeyJWTValidationConfiguration() {

        log.info("Retrieving private key JWT validation configuration.");
        return Response.ok().entity(configManagementService.getPrivateKeyJWTValidatorConfiguration()).build();

    }

    @Override
    public Response getDCRConfiguration() {

        log.info("Retrieving DCR configuration.");
        return Response.ok().entity(configManagementService.getDCRConfiguration()).build();

    }

    @Override
    public Response getRemoteLoggingConfig(String logType) {

        log.info("Retrieving remote logging configuration for logType: " + logType);
        RemoteServerLoggerData remoteServerLoggerResponseData =
                configManagementService.getRemoteServerConfig(logType);
        if (remoteServerLoggerResponseData != null) {
            return Response.ok().entity(createRemoteLoggingConfig(remoteServerLoggerResponseData)).build();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Remote logging configuration not found for logType: " + logType);
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getRemoteLoggingConfigs() {

        log.info("Retrieving all remote logging configurations.");
        List<RemoteServerLoggerData> remoteServerLoggerResponseData =
                configManagementService.getRemoteServerConfigs();
        return Response.ok()
                .entity(remoteServerLoggerResponseData.stream().map(this::createRemoteLoggingConfigListItem)
                        .collect(Collectors.toList())).build();
    }

    @Override
    public Response patchPrivatKeyJWTValidationConfiguration(List<JWTKeyValidatorPatch> jwTKeyValidatorPatch) {

        log.info("Updating private key JWT validation configuration with " + jwTKeyValidatorPatch.size() 
                + " patches.");
        configManagementService.patchPrivateKeyJWTValidatorSConfig(jwTKeyValidatorPatch);
        return Response.ok().build();
    }

    @Override
    public Response patchDCRConfiguration(List<DCRPatch> dcrPatch) {

        log.info("Updating DCR configuration with " + dcrPatch.size() + " patches.");
        configManagementService.patchDCRConfig(dcrPatch);
        return Response.ok().build();
    }

    @Override
    public Response restoreServerRemoteLoggingConfiguration(String logType) {

        log.info("Restoring remote logging configuration for logType: " + logType);
        configManagementService.resetRemoteServerConfig(logType);
        return Response.noContent().build();
    }

    @Override
    public Response restoreServerRemoteLoggingConfigurations() {

        log.info("Restoring all remote logging configurations.");
        configManagementService.resetRemoteServerConfig();
        return Response.noContent().build();
    }

    @Override
    public Response getSchemas() {

        log.info("Retrieving all schemas.");
        return Response.ok().entity(configManagementService.getSchemas()).build();
    }

    @Override
    public Response listAuthenticators(String type) {

        log.info("Retrieving authenticators for type: " + type);
        return Response.ok().entity(configManagementService.getAuthenticators(type)).build();
    }

    @Override
    public Response patchCORSConfiguration(List<CORSPatch> coRSPatch) {

        log.info("Updating CORS configuration with " + coRSPatch.size() + " patches.");
        configManagementService.patchCORSConfig(coRSPatch);
        return Response.ok().build();
    }

    @Override
    public Response patchConfigs(List<Patch> patch) {

        log.info("Updating server configurations with " + patch.size() + " patches.");
        configManagementService.patchConfigs(patch);
        return Response.ok().build();
    }

    @Override
    public Response patchImpersonationConfiguration(List<ImpersonationPatch> impersonationPatch) {

        log.info("Updating impersonation configuration with " + impersonationPatch.size() + " patches.");
        configManagementService.patchImpersonationConfiguration(impersonationPatch);
        return Response.ok().build();
    }

    @Override
    public Response deleteImpersonationConfiguration() {

        log.info("Deleting impersonation configuration.");
        configManagementService.deleteImpersonationConfiguration();
        return Response.noContent().build();
    }

    @Override
    public Response updateInboundScimConfigs(ScimConfig scimConfig) {

        log.info("Updating inbound SCIM configurations.");
        configManagementService.updateInboundScimConfigs(scimConfig);
        return Response.ok().build();
    }

    @Override
    public Response updateRemoteLoggingConfig(String logType, RemoteLoggingConfig remoteLoggingConfig) {

        log.info("Updating remote logging configuration for logType: " + logType);
        configManagementService.updateRemoteLoggingConfig(logType, remoteLoggingConfig);
        return Response.accepted().build();
    }

    @Override
    public Response updateRemoteLoggingConfigs(List<RemoteLoggingConfigListItem> remoteLoggingConfigListItem) {

        log.info("Updating " + remoteLoggingConfigListItem.size() + " remote logging configurations.");
        configManagementService.updateRemoteLoggingConfigs(remoteLoggingConfigListItem);
        return Response.accepted().build();
    }

    @Override
    public Response getSAMLInboundAuthConfig() {

        log.info("Retrieving SAML inbound authentication configuration.");
        return Response.ok().entity(configManagementService.getSAMLInboundAuthConfig()).build();
    }

    @Override
    public Response updateSAMLInboundAuthConfig(InboundAuthSAML2Config inboundAuthSAML2Config) {

        log.info("Updating SAML inbound authentication configuration.");
        configManagementService.updateSAMLInboundAuthConfig(inboundAuthSAML2Config);
        return Response.ok().build();
    }

    @Override
    public Response getPassiveSTSInboundAuthConfig() {

        log.info("Retrieving Passive STS inbound authentication configuration.");
        return Response.ok().entity(configManagementService.getPassiveSTSInboundAuthConfig()).build();
    }

    @Override
    public Response updatePassiveSTSInboundAuthConfig(InboundAuthPassiveSTSConfig inboundAuthPassiveSTSConfig) {

        log.info("Updating Passive STS inbound authentication configuration.");
        configManagementService.updatePassiveSTSInboundAuthConfig(inboundAuthPassiveSTSConfig);
        return Response.ok().build();
    }

    /**
     * Deletes the passive STS inbound authentication configuration of an organization.
     *
     * @return Response indicating the result of the operation.
     */
    @Override
    public Response deletePassiveSTSInboundAuthConfig() {

        log.info("Deleting Passive STS inbound authentication configuration.");
        configManagementService.deletePassiveSTSInboundAuthConfig();
        return Response.noContent().build();
    }

    /**
     * Deletes the SAML inbound authentication configuration of an organization.
     *
     * @return Response indicating the result of the operation.
     */
    @Override
    public Response deleteSAMLInboundAuthConfig() {

        log.info("Deleting SAML inbound authentication configuration.");
        configManagementService.deleteSAMLInboundAuthConfig();
        return Response.noContent().build();
    }
    
    private RemoteLoggingConfigListItem createRemoteLoggingConfigListItem(
            RemoteServerLoggerData remoteServerLoggerData) {

        RemoteLoggingConfigListItem remoteLoggingConfigListItem = new RemoteLoggingConfigListItem();
        remoteLoggingConfigListItem.setRemoteUrl(remoteServerLoggerData.getUrl());
        remoteLoggingConfigListItem.setConnectTimeoutMillis(remoteServerLoggerData.getConnectTimeoutMillis());
        remoteLoggingConfigListItem.setVerifyHostname(remoteServerLoggerData.isVerifyHostname());
        remoteLoggingConfigListItem.setUsername(remoteServerLoggerData.getUsername());
        remoteLoggingConfigListItem.setPassword(remoteServerLoggerData.getPassword());
        remoteLoggingConfigListItem.setKeystoreLocation(remoteServerLoggerData.getKeystoreLocation());
        remoteLoggingConfigListItem.setKeystorePassword(remoteServerLoggerData.getKeystorePassword());
        remoteLoggingConfigListItem.setTruststoreLocation(remoteServerLoggerData.getTruststoreLocation());
        remoteLoggingConfigListItem.setTruststorePassword(remoteServerLoggerData.getTruststorePassword());
        remoteLoggingConfigListItem.setLogType(
                RemoteLoggingConfigListItem.LogTypeEnum.valueOf(remoteServerLoggerData.getLogType()));
        return remoteLoggingConfigListItem;
    }

    private RemoteLoggingConfig createRemoteLoggingConfig(
            RemoteServerLoggerData remoteServerLoggerData) {

        RemoteLoggingConfig remoteLoggingConfig = new RemoteLoggingConfig();
        remoteLoggingConfig.setRemoteUrl(remoteServerLoggerData.getUrl());
        remoteLoggingConfig.setConnectTimeoutMillis(remoteServerLoggerData.getConnectTimeoutMillis());
        remoteLoggingConfig.setVerifyHostname(remoteServerLoggerData.isVerifyHostname());
        remoteLoggingConfig.setUsername(remoteServerLoggerData.getUsername());
        remoteLoggingConfig.setPassword(remoteServerLoggerData.getPassword());
        remoteLoggingConfig.setKeystoreLocation(remoteServerLoggerData.getKeystoreLocation());
        remoteLoggingConfig.setKeystorePassword(remoteServerLoggerData.getKeystorePassword());
        remoteLoggingConfig.setTruststoreLocation(remoteServerLoggerData.getTruststoreLocation());
        remoteLoggingConfig.setTruststorePassword(remoteServerLoggerData.getTruststorePassword());
        return remoteLoggingConfig;
    }
}
