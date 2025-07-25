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

        if (log.isDebugEnabled()) {
            log.debug("Initializing ConfigsApiServiceImpl.");
        }
        try {
            configManagementService = ServerConfigManagementServiceFactory.getServerConfigManagementService();
            if (log.isDebugEnabled()) {
                log.debug("Successfully initialized server config management service.");
            }
        } catch (Exception e) {
            log.error("Error occurred while initiating server config management services.", e);
            throw new RuntimeException("Error occurred while initiating server config management services.", e);
        }
    }

    @Override
    public Response getAuthenticator(String authenticatorId) {

        return Response.ok().entity(configManagementService.getAuthenticator(authenticatorId)).build();
    }

    @Override
    public Response getCORSConfiguration() {

        return Response.ok().entity(configManagementService.getCORSConfiguration()).build();
    }

    @Override
    public Response getConfigs() {

        return Response.ok().entity(configManagementService.getConfigs()).build();
    }

    @Override
    public Response getHomeRealmIdentifiers() {
        return Response.ok().entity(configManagementService.getHomeRealmIdentifiers()).build();
    }

    @Override
    public Response getImpersonationConfiguration() {

        return Response.ok().entity(configManagementService.getImpersonationConfiguration()).build();
    }

    @Override
    public Response getInboundScimConfigs() {

        return Response.ok().entity(configManagementService.getInboundScimConfig()).build();
    }

    @Override
    public Response getSchema(String schemaId) {

        return Response.ok().entity(configManagementService.getSchema(schemaId)).build();
    }

    @Override
    public Response getPrivatKeyJWTValidationConfiguration() {

        return Response.ok().entity(configManagementService.getPrivateKeyJWTValidatorConfiguration()).build();

    }

    @Override
    public Response getDCRConfiguration() {

        return Response.ok().entity(configManagementService.getDCRConfiguration()).build();

    }

    @Override
    public Response getRemoteLoggingConfig(String logType) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving remote logging configuration for log type: " + logType);
        }
        RemoteServerLoggerData remoteServerLoggerResponseData =
                configManagementService.getRemoteServerConfig(logType);
        if (remoteServerLoggerResponseData != null) {
            if (log.isDebugEnabled()) {
                log.debug("Successfully retrieved remote logging configuration for log type: " + logType);
            }
            return Response.ok().entity(createRemoteLoggingConfig(remoteServerLoggerResponseData)).build();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("No remote logging configuration found for log type: " + logType);
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getRemoteLoggingConfigs() {

        List<RemoteServerLoggerData> remoteServerLoggerResponseData =
                configManagementService.getRemoteServerConfigs();
        return Response.ok()
                .entity(remoteServerLoggerResponseData.stream().map(this::createRemoteLoggingConfigListItem)
                        .collect(Collectors.toList())).build();
    }

    @Override
    public Response patchPrivatKeyJWTValidationConfiguration(List<JWTKeyValidatorPatch> jwTKeyValidatorPatch) {

        configManagementService.patchPrivateKeyJWTValidatorSConfig(jwTKeyValidatorPatch);
        return Response.ok().build();
    }

    @Override
    public Response patchDCRConfiguration(List<DCRPatch> dcrPatch) {

        configManagementService.patchDCRConfig(dcrPatch);
        return Response.ok().build();
    }

    @Override
    public Response restoreServerRemoteLoggingConfiguration(String logType) {

        configManagementService.resetRemoteServerConfig(logType);
        return Response.noContent().build();
    }

    @Override
    public Response restoreServerRemoteLoggingConfigurations() {

        configManagementService.resetRemoteServerConfig();
        return Response.noContent().build();
    }

    @Override
    public Response getSchemas() {

        return Response.ok().entity(configManagementService.getSchemas()).build();
    }

    @Override
    public Response listAuthenticators(String type) {

        return Response.ok().entity(configManagementService.getAuthenticators(type)).build();
    }

    @Override
    public Response patchCORSConfiguration(List<CORSPatch> coRSPatch) {

        configManagementService.patchCORSConfig(coRSPatch);
        return Response.ok().build();
    }

    @Override
    public Response patchConfigs(List<Patch> patch) {

        configManagementService.patchConfigs(patch);
        return Response.ok().build();
    }

    @Override
    public Response patchImpersonationConfiguration(List<ImpersonationPatch> impersonationPatch) {

        configManagementService.patchImpersonationConfiguration(impersonationPatch);
        return Response.ok().build();
    }

    @Override
    public Response updateInboundScimConfigs(ScimConfig scimConfig) {

        configManagementService.updateInboundScimConfigs(scimConfig);
        return Response.ok().build();
    }

    @Override
    public Response updateRemoteLoggingConfig(String logType, RemoteLoggingConfig remoteLoggingConfig) {

        if (log.isDebugEnabled()) {
            log.debug("Updating remote logging configuration for log type: " + logType);
        }
        configManagementService.updateRemoteLoggingConfig(logType, remoteLoggingConfig);
        if (log.isDebugEnabled()) {
            log.debug("Successfully updated remote logging configuration for log type: " + logType);
        }
        return Response.accepted().build();
    }

    @Override
    public Response updateRemoteLoggingConfigs(List<RemoteLoggingConfigListItem> remoteLoggingConfigListItem) {

        configManagementService.updateRemoteLoggingConfigs(remoteLoggingConfigListItem);
        return Response.accepted().build();
    }

    @Override
    public Response getSAMLInboundAuthConfig() {

        return Response.ok().entity(configManagementService.getSAMLInboundAuthConfig()).build();
    }

    @Override
    public Response updateSAMLInboundAuthConfig(InboundAuthSAML2Config inboundAuthSAML2Config) {

        if (log.isDebugEnabled()) {
            log.debug("Updating SAML inbound authentication configuration.");
        }
        configManagementService.updateSAMLInboundAuthConfig(inboundAuthSAML2Config);
        if (log.isDebugEnabled()) {
            log.debug("Successfully updated SAML inbound authentication configuration.");
        }
        return Response.ok().build();
    }

    @Override
    public Response getPassiveSTSInboundAuthConfig() {

        return Response.ok().entity(configManagementService.getPassiveSTSInboundAuthConfig()).build();
    }

    @Override
    public Response updatePassiveSTSInboundAuthConfig(InboundAuthPassiveSTSConfig inboundAuthPassiveSTSConfig) {

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

        if (log.isDebugEnabled()) {
            log.debug("Deleting Passive STS inbound authentication configuration.");
        }
        configManagementService.deletePassiveSTSInboundAuthConfig();
        if (log.isDebugEnabled()) {
            log.debug("Successfully deleted Passive STS inbound authentication configuration.");
        }
        return Response.noContent().build();
    }

    /**
     * Deletes the SAML inbound authentication configuration of an organization.
     *
     * @return Response indicating the result of the operation.
     */
    @Override
    public Response deleteSAMLInboundAuthConfig() {

        if (log.isDebugEnabled()) {
            log.debug("Deleting SAML inbound authentication configuration.");
        }
        configManagementService.deleteSAMLInboundAuthConfig();
        if (log.isDebugEnabled()) {
            log.debug("Successfully deleted SAML inbound authentication configuration.");
        }
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
