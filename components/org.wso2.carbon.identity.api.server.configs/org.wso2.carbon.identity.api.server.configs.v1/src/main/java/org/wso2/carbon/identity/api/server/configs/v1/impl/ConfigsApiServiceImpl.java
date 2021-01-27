/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.configs.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.configs.v1.ConfigsApiService;
import org.wso2.carbon.identity.api.server.configs.v1.core.ServerConfigManagementService;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.Patch;
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
}
