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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.WorkflowsApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.core.WorkflowService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.factories.WorkflowServiceFactory;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowRequest;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowResponse;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.workflow.common.Constants.WORKFLOW_PATH_COMPONENT;

/**
 * Implementation of Workflow Management REST API.
 */
public class WorkflowsApiServiceImpl implements WorkflowsApiService {

    private static final Log log = LogFactory.getLog(WorkflowsApiServiceImpl.class);
    private final WorkflowService workflowService;

    public WorkflowsApiServiceImpl() {

        this.workflowService = WorkflowServiceFactory.getWorkflowService();
    }

    @Override
    public Response addWorkflow(WorkflowRequest workflowRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Adding workflow: " + (workflowRequest != null ? workflowRequest.getName() : "null"));
        }
        WorkflowResponse workflowResponse = workflowService.addWorkflow(workflowRequest);
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT + WORKFLOW_PATH_COMPONENT + "/" +
                workflowResponse.getId());
        return Response.created(location).entity(workflowResponse).build();
    }

    @Override
    public Response deleteWorkflowById(String workflowId) {

        if (log.isDebugEnabled()) {
            log.debug("Deleting workflow with ID: " + workflowId);
        }
        workflowService.removeWorkflow(workflowId);
        return Response.noContent().build();
    }

    @Override
    public Response getWorkflowById(String workflowId) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving workflow with ID: " + workflowId);
        }
        return Response.ok().entity(workflowService.getWorkflow(workflowId)).build();
    }

    @Override
    public Response getWorkflows(Integer limit, Integer offset, String filter) {

        if (log.isDebugEnabled()) {
            log.debug("Listing workflows with limit: " + limit + ", offset: " + offset + ", filter: " + filter);
        }
        return Response.ok().entity(workflowService.listPaginatedWorkflows(limit, offset, filter)).build();
    }

    @Override
    public Response updateWorkflow(String workflowId, WorkflowRequest workflowRequest) {

        if (log.isDebugEnabled()) {
            log.debug("Updating workflow with ID: " + workflowId);
        }
        return Response.ok().entity(workflowService.updateWorkflow(workflowRequest, workflowId)).build();
    }
}
