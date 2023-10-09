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

package org.wso2.carbon.identity.api.server.application.tag.management.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.ApplicationsTagsApiService;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.constants.ApplicationTagMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.core.ServerApplicationTagManagementService;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagCreationResponse;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagModel;
import org.wso2.carbon.identity.api.server.common.ContextLoader;

import java.net.URI;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Implementation of the Application Tags REST Api.
 */
public class ApplicationsTagsApiServiceImpl implements ApplicationsTagsApiService {

    @Autowired
    private ServerApplicationTagManagementService serverApplicationTagManagementService;

    @Override
    public Response createApplicationTag(ApplicationTagModel applicationTagCreationModel) {

        ApplicationTagCreationResponse applicationTag = serverApplicationTagManagementService
                .addApplicationTag(applicationTagCreationModel);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                ApplicationTagMgtEndpointConstants.APPLICATION_TAG_PATH_COMPONENT + "/" + applicationTag.getId());
        return Response.created(location).entity(applicationTag).build();
    }

    @Override
    public Response deleteApplicationTag(String tagId) {

        serverApplicationTagManagementService.deleteApplicationTag(tagId);
        return Response.noContent().build();
    }

    @Override
    public Response getAllApplicationTags(Integer limit, Integer offset, String filter) {

        return Response.ok().entity(serverApplicationTagManagementService.getAllApplicationTags(offset, limit,
                filter)).build();
    }

    @Override
    public Response getApplicationTag(String tagId) {

        return Response.ok().entity(serverApplicationTagManagementService.getApplicationTagById(tagId)).build();
    }

    @Override
    public Response patchTag(String tagId, ApplicationTagModel applicationTagPatchModel) {

        serverApplicationTagManagementService.patchApplicationTag(tagId, applicationTagPatchModel);
        return Response.ok().build();
    }
}
