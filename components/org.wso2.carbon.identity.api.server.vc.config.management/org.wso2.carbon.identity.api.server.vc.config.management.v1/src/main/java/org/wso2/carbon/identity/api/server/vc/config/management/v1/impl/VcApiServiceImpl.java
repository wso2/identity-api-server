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
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOffer;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferCreationModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferUpdateModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VcApiService;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.core.ServerVCCredentialConfigManagementService;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.core.ServerVCOfferManagementService;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.factories.ServerVCCredentialConfigManagementServiceFactory;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.factories.ServerVCOfferManagementServiceFactory;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * Implementation of VC API Service.
 */
public class VcApiServiceImpl implements VcApiService {

    private static final Log LOG = LogFactory.getLog(VcApiServiceImpl.class);

    private volatile ServerVCCredentialConfigManagementService serverVCCredentialConfigManagementService;
    private volatile ServerVCOfferManagementService serverVCOfferManagementService;

    public VcApiServiceImpl() {

        try {
            this.serverVCCredentialConfigManagementService = ServerVCCredentialConfigManagementServiceFactory
                    .getServerVCCredentialConfigManagementService();
            this.serverVCOfferManagementService = ServerVCOfferManagementServiceFactory
                    .getServerVCOfferManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating server vc config management service.", e);
        }
    }

    @Override
    public Response addVCCredentialConfiguration(
            VCCredentialConfigurationCreationModel vcCredentialConfigurationCreationModel) {

        VCCredentialConfiguration createdConfiguration = serverVCCredentialConfigManagementService
                .addVCCredentialConfiguration(vcCredentialConfigurationCreationModel);
        URI location = buildConfigResourceLocation(createdConfiguration != null ? createdConfiguration.getId() : null);
        return Response.created(location).entity(createdConfiguration).build();
    }

    @Override
    public Response createVCOffer(VCOfferCreationModel vcOfferCreationModel) {

        VCOffer createdOffer = serverVCOfferManagementService.addVCOffer(vcOfferCreationModel);
        URI location = buildOfferResourceLocation(createdOffer != null ? createdOffer.getOfferId() : null);
        return Response.created(location).entity(createdOffer).build();
    }

    @Override
    public Response deleteVCCredentialConfiguration(String configId) {

        serverVCCredentialConfigManagementService.deleteVCCredentialConfiguration(configId);
        return Response.noContent().build();
    }

    @Override
    public Response deleteVCOffer(String offerId) {

        serverVCOfferManagementService.deleteVCOffer(offerId);
        return Response.noContent().build();
    }

    @Override
    public Response getVCCredentialConfiguration(String configId) {

        VCCredentialConfiguration configuration = serverVCCredentialConfigManagementService
                .getVCCredentialConfiguration(configId);
        return Response.ok().entity(configuration).build();
    }

    @Override
    public Response getVCOffer(String offerId) {

        VCOffer offer = serverVCOfferManagementService.getVCOffer(offerId);
        return Response.ok().entity(offer).build();
    }

    @Override
    public Response listVCCredentialConfigurations() {

        return Response.ok().entity(serverVCCredentialConfigManagementService.listVCCredentialConfigurations()).build();
    }

    @Override
    public Response listVCOffers() {

        return Response.ok().entity(serverVCOfferManagementService.listVCOffers()).build();
    }

    @Override
    public Response updateVCCredentialConfiguration(String configId,
                                                     VCCredentialConfigurationUpdateModel
                                                             vcCredentialConfigurationUpdateModel) {

        VCCredentialConfiguration updatedConfiguration = serverVCCredentialConfigManagementService
                .updateVCCredentialConfiguration(configId, vcCredentialConfigurationUpdateModel);
        return Response.ok().entity(updatedConfiguration).build();
    }

    @Override
    public Response updateVCOffer(String offerId, VCOfferUpdateModel vcOfferUpdateModel) {

        VCOffer updatedOffer = serverVCOfferManagementService.updateVCOffer(offerId, vcOfferUpdateModel);
        return Response.ok().entity(updatedOffer).build();
    }

    private URI buildConfigResourceLocation(String resourceId) {

        StringBuilder pathBuilder = new StringBuilder(Constants.V1_API_PATH_COMPONENT)
                .append(VCCredentialConfigManagementConstants.VC_CREDENTIAL_CONFIG_PATH_COMPONENT);
        if (resourceId != null) {
            pathBuilder.append(VCCredentialConfigManagementConstants.PATH_SEPARATOR).append(resourceId);
        }
        return ContextLoader.buildURIForHeader(pathBuilder.toString());
    }

    private URI buildOfferResourceLocation(String offerId) {

        StringBuilder pathBuilder = new StringBuilder(Constants.V1_API_PATH_COMPONENT)
                .append(VCCredentialConfigManagementConstants.VC_OFFER_PATH_COMPONENT);
        if (offerId != null) {
            pathBuilder.append(VCCredentialConfigManagementConstants.PATH_SEPARATOR).append(offerId);
        }
        return ContextLoader.buildURIForHeader(pathBuilder.toString());
    }
}
