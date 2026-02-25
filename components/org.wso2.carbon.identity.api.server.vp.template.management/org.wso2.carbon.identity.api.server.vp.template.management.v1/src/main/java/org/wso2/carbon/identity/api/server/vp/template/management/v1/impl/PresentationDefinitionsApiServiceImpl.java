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

package org.wso2.carbon.identity.api.server.vp.template.management.v1.impl;

import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementConstants;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionCreationModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionResponse;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionUpdateModel;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.PresentationDefinitionsApiService;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.core.ServerVPDefinitionManagementService;

import java.net.URI;
import javax.ws.rs.core.Response;

/**
 * Implementation of PresentationDefinitionsApiService.
 * Delegates all operations to ServerVPDefinitionManagementService and wraps in JAX-RS Response.
 */
public class PresentationDefinitionsApiServiceImpl extends PresentationDefinitionsApiService {

    private static final ServerVPDefinitionManagementService CORE_SERVICE =
            new ServerVPDefinitionManagementService();

    @Override
    public Response listPresentationDefinitions() {

        return Response.ok().entity(CORE_SERVICE.listPresentationDefinitions()).build();
    }

    @Override
    public Response createPresentationDefinition(
            PresentationDefinitionCreationModel presentationDefinitionCreationModel) {

        PresentationDefinitionResponse created =
                CORE_SERVICE.createPresentationDefinition(presentationDefinitionCreationModel);
        URI location = URI.create(
                VPDefinitionManagementConstants.VP_DEFINITION_MANAGEMENT_PATH_COMPONENT
                        + "/" + created.getId());
        return Response.created(location).entity(created).build();
    }

    @Override
    public Response getPresentationDefinition(String definitionId) {

        return Response.ok().entity(CORE_SERVICE.getPresentationDefinition(definitionId)).build();
    }

    @Override
    public Response updatePresentationDefinition(String definitionId,
            PresentationDefinitionUpdateModel presentationDefinitionUpdateModel) {

        return Response.ok().entity(
                CORE_SERVICE.updatePresentationDefinition(definitionId,
                        presentationDefinitionUpdateModel)).build();
    }

    @Override
    public Response deletePresentationDefinition(String definitionId) {

        CORE_SERVICE.deletePresentationDefinition(definitionId);
        return Response.noContent().build();
    }
}
