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

package org.wso2.carbon.identity.api.server.configs.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.configs.v1.ConfigsApiService;
import org.wso2.carbon.identity.api.server.configs.v1.core.ServerConfigManagementService;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthPassiveSTSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthSAML2Config;
import org.wso2.carbon.identity.api.server.configs.v1.model.JWTKeyValidatorPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.Patch;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfigListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.ScimConfig;

import java.util.List;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Server Configurations Rest API.
 */
public class ConfigsApiServiceImpl implements ConfigsApiService {

    @Autowired
    private ServerConfigManagementService configManagementService;


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
    public Response getRemoteLoggingConfig(String logType) {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response getRemoteLoggingConfigs() {

        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @Override
    public Response patchPrivatKeyJWTValidationConfiguration(List<JWTKeyValidatorPatch> jwTKeyValidatorPatch) {

        configManagementService.patchPrivateKeyJWTValidatorSConfig(jwTKeyValidatorPatch);
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
    public Response updateInboundScimConfigs(ScimConfig scimConfig) {

        configManagementService.updateInboundScimConfigs(scimConfig);
        return Response.ok().build();
    }

    @Override
    public Response updateRemoteLoggingConfig(String logType, RemoteLoggingConfig remoteLoggingConfig) {

        configManagementService.updateRemoteLoggingConfig(logType, remoteLoggingConfig);
        return Response.accepted().build();
    }

    @Override
    public Response updateRemoteLoggingConfigs(List<RemoteLoggingConfigListItem> remoteLoggingConfigListItem) {

        configManagementService.updateRemoteLoggingConfigs(remoteLoggingConfigListItem);
        return Response.accepted().build();
    }

    @Override
    public Response getSAMLInboundAuthConfig() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateSAMLInboundAuthConfig(InboundAuthSAML2Config inboundAuthSAML2Config) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getPassiveSTSInboundAuthConfig() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updatePassiveSTSInboundAuthConfig(InboundAuthPassiveSTSConfig inboundAuthPassiveSTSConfig) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }
}
