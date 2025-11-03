/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vc.config.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.vc.config.management.common.VCCredentialConfigManagementConstants;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfiguration;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationCreationModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationUpdateModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VcCredentialConfigurationsApiService;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.core.ServerVCCredentialConfigManagementService;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.factories.ServerVCCredentialConfigManagementServiceFactory;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * Implementation of the VC credential configuration API.
 */
public class VcCredentialConfigurationsApiServiceImpl implements VcCredentialConfigurationsApiService {

    private static final Log LOG = LogFactory.getLog(VcCredentialConfigurationsApiServiceImpl.class);

    private volatile ServerVCCredentialConfigManagementService serverVCCredentialConfigManagementService;

    public VcCredentialConfigurationsApiServiceImpl() {

        try {
            this.serverVCCredentialConfigManagementService = ServerVCCredentialConfigManagementServiceFactory
                    .getServerVCCredentialConfigManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating server vc config management service.", e);
        }
    }

    @Override
    public Response addVCCredentialConfiguration(
            VCCredentialConfigurationCreationModel vcCredentialConfigurationCreationModel) {

        VCCredentialConfiguration createdConfiguration = serverVCCredentialConfigManagementService
                .addVCCredentialConfiguration(vcCredentialConfigurationCreationModel);
        URI location = buildResourceLocation(createdConfiguration != null ? createdConfiguration.getId() : null);
        return Response.created(location).entity(createdConfiguration).build();
    }

    @Override
    public Response deleteVCCredentialConfiguration(String configId) {

        serverVCCredentialConfigManagementService.deleteVCCredentialConfiguration(configId);
        return Response.noContent().build();
    }

    @Override
    public Response getVCCredentialConfiguration(String configId) {

        VCCredentialConfiguration configuration = serverVCCredentialConfigManagementService
                .getVCCredentialConfiguration(configId);
        return Response.ok().entity(configuration).build();
    }

    @Override
    public Response listVCCredentialConfigurations() {

        return Response.ok().entity(serverVCCredentialConfigManagementService.listVCCredentialConfigurations()).build();
    }

    @Override
    public Response updateVCCredentialConfiguration(String configId, VCCredentialConfigurationUpdateModel
            vcCredentialConfigurationUpdateModel) {

        VCCredentialConfiguration updatedConfiguration = serverVCCredentialConfigManagementService
                .updateVCCredentialConfiguration(configId, vcCredentialConfigurationUpdateModel);
        return Response.ok().entity(updatedConfiguration).build();
    }

    private URI buildResourceLocation(String resourceId) {

        StringBuilder pathBuilder = new StringBuilder(Constants.V1_API_PATH_COMPONENT)
                .append(VCCredentialConfigManagementConstants.VC_CREDENTIAL_CONFIG_PATH_COMPONENT);
        if (resourceId != null) {
            pathBuilder.append(VCCredentialConfigManagementConstants.PATH_SEPARATOR).append(resourceId);
        }
        return ContextLoader.buildURIForHeader(pathBuilder.toString());
    }
}
