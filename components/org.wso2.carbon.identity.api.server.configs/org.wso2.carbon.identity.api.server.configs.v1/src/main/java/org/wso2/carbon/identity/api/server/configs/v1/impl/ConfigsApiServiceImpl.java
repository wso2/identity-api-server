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

import org.wso2.carbon.identity.api.server.configs.v1.*;
import org.wso2.carbon.identity.api.server.configs.v1.model.*;
import java.util.List;

import javax.ws.rs.core.Response;

public class ConfigsApiServiceImpl implements ConfigsApiService {

    @Override
    public Response getAuthenticator(String authenticatorId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getCORSConfiguration() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getConfigs() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getHomeRealmIdentifiers() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getInboundScimConfigs() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getPassiveSTSInboundAuthConfig() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getPrivatKeyJWTValidationConfiguration() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getRemoteLoggingConfig(String logType) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getRemoteLoggingConfigs() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getSAMLInboundAuthConfig() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getSchema(String schemaId) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response getSchemas() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response listAuthenticators(String type) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response patchCORSConfiguration(List<CORSPatch> coRSPatch) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response patchConfigs(List<Patch> patch) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response patchPrivatKeyJWTValidationConfiguration(List<JWTKeyValidatorPatch> jwTKeyValidatorPatch) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response restoreServerRemoteLoggingConfiguration(String logType) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response restoreServerRemoteLoggingConfigurations() {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateInboundScimConfigs(ScimConfig scimConfig) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updatePassiveSTSInboundAuthConfig(InboundAuthPassiveSTSConfig inboundAuthPassiveSTSConfig) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateRemoteLoggingConfig(String logType, RemoteLoggingConfig remoteLoggingConfig) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateRemoteLoggingConfigs(List<RemoteLoggingConfigListItem> remoteLoggingConfigListItem) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }

    @Override
    public Response updateSAMLInboundAuthConfig(InboundAuthSAML2Config inboundAuthSAML2Config) {

        // do some magic!
        return Response.ok().entity("magic!").build();
    }
}
