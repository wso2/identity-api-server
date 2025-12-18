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

package org.wso2.carbon.identity.api.server.vc.template.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.vc.template.management.common.VCTemplateManagementConstants;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplate;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateCreationModel;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateUpdateModel;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VcTemplatesApiService;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.core.ServerVCTemplateManagementService;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.factories.ServerVCTemplateManagementServiceFactory;

import java.net.URI;

import javax.ws.rs.core.Response;

/**
 * Implementation of the VcTemplatesApiService interface.
 * Handles VC template management operations including creation, retrieval, update, and deletion.
 */
public class VcTemplatesApiServiceImpl implements VcTemplatesApiService {

    private static final Log LOG = LogFactory.getLog(VcTemplatesApiServiceImpl.class);

    private final ServerVCTemplateManagementService serverVCTemplateManagementService;

    public VcTemplatesApiServiceImpl() {

        try {
            this.serverVCTemplateManagementService = ServerVCTemplateManagementServiceFactory
                    .getServerVCTemplateManagementService();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Error occurred while initiating server vc config management service.", e);
        }
    }

    @Override
    public Response addVCTemplate(VCTemplateCreationModel vcTemplateCreationModel) {

        VCTemplate createdConfiguration = serverVCTemplateManagementService
                .addVCTemplate(vcTemplateCreationModel);
        URI location = buildTemplateLocation(createdConfiguration != null ? createdConfiguration.getId() : null);
        return Response.created(location).entity(createdConfiguration).build();
    }

    @Override
    public Response deleteVCTemplate(String templateId) {

        serverVCTemplateManagementService.deleteVCTemplate(templateId);
        return Response.noContent().build();
    }

    @Override
    public Response generateVCCredentialOffer(String templateId) {

        return Response.ok().entity(serverVCTemplateManagementService.generateOffer(templateId))
                .build();
    }

    @Override
    public Response getVCTemplate(String templateId) {

        VCTemplate configuration = serverVCTemplateManagementService.getVCTemplate(templateId);
        return Response.ok().entity(configuration).build();
    }

    @Override
    public Response listVCTemplates(String before, String after, String filter, Integer limit, String attributes) {

        return Response.ok().entity(serverVCTemplateManagementService.listVCTemplates(before,
                after, filter, limit, attributes)).build();
    }

    @Override
    public Response revokeVCCredentialOffer(String templateId) {

        return Response.ok().entity(serverVCTemplateManagementService.revokeOffer(templateId)).build();
    }

    @Override
    public Response updateVCTemplate(String configId, VCTemplateUpdateModel vcTemplateUpdateModel) {

        VCTemplate updatedConfiguration = serverVCTemplateManagementService
                .updateVCTemplate(configId, vcTemplateUpdateModel);
        return Response.ok().entity(updatedConfiguration).build();
    }

    private URI buildTemplateLocation(String resourceId) {

        StringBuilder pathBuilder = new StringBuilder(Constants.V1_API_PATH_COMPONENT)
                .append(VCTemplateManagementConstants.VC_TEMPLATE_PATH_COMPONENT);
        if (resourceId != null) {
            pathBuilder.append(VCTemplateManagementConstants.PATH_SEPARATOR).append(resourceId);
        }
        return ContextLoader.buildURIForHeader(pathBuilder.toString());
    }
}
