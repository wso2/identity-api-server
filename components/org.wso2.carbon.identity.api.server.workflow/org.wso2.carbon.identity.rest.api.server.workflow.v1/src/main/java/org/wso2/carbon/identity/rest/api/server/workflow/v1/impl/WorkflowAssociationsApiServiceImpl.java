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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.impl;

import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.WorkflowAssociationsApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.core.WorkflowService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.factories.WorkflowServiceFactory;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationPatchRequest;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationRequest;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationResponse;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.workflow.common.Constants.WORKFLOW_ASSOCIATION_PATH_COMPONENT;

/**
 * Implementation of workflow association management REST API.
 */
public class WorkflowAssociationsApiServiceImpl implements WorkflowAssociationsApiService {

    private final WorkflowService workflowService;

    public WorkflowAssociationsApiServiceImpl() {

       this.workflowService = WorkflowServiceFactory.getWorkflowService();
    }

    @Override
    public Response addWorkflowAssociation(WorkflowAssociationRequest workflowAssociationRequest) {

        WorkflowAssociationResponse workflowAssociationResponse =
                workflowService.addAssociation(workflowAssociationRequest);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + WORKFLOW_ASSOCIATION_PATH_COMPONENT
                + "/" + workflowAssociationResponse.getId());
        return Response.created(location).entity(workflowAssociationResponse).build();
    }

    @Override
    public Response deleteWorkflowAssociationById(String associationId) {

        workflowService.removeAssociation(associationId);
        return Response.noContent().build();
    }

    @Override
    public Response getWorkflowAssociationById(String associationId) {

        return Response.ok().entity(workflowService.getAssociation(associationId)).build();
    }

    @Override
    public Response getWorkflowAssociations(Integer limit, Integer offset, String filter) {

        return Response.ok().entity(workflowService.listPaginatedAssociations(limit, offset, filter)).build();
    }

    @Override
    public Response updateAssociation(String associationId, WorkflowAssociationPatchRequest
            workflowAssociationPatchRequest) {

        return Response.ok().entity(workflowService.updateAssociation(associationId,
                workflowAssociationPatchRequest)).build();
    }
}
