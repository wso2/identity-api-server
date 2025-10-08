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

import org.wso2.carbon.identity.rest.api.server.workflow.v1.WorkflowInstancesApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.core.WorkflowService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.factories.WorkflowServiceFactory;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.InstanceStatus;

import javax.ws.rs.core.Response;

/**
 * Implementation of the WorkflowInstancesApiService interface.
 */
public class WorkflowInstancesApiServiceImpl implements WorkflowInstancesApiService {

    private final WorkflowService workflowService;

    public WorkflowInstancesApiServiceImpl() {
        
        this.workflowService = WorkflowServiceFactory.getWorkflowService();
    }

    @Override
    public Response abortWorkflowInstance(String instanceId) {

        workflowService.abortWorkflowInstance(instanceId);
        return Response.ok().entity(InstanceStatus.ABORTED).build();
    }

    @Override
    public Response deleteWorkflowInstance(String instanceId) {

        workflowService.deleteWorkflowInstance(instanceId);
        return Response.noContent().build();
    }

    @Override
    public Response getWorkflowInstanceById(String instanceId) {

        return Response.ok().entity(workflowService.getWorkflowInstanceById(instanceId)).build();
    }

    @Override
    public Response getWorkflowInstances(Integer limit, Integer offset, String filter) {

        return Response.ok().entity(workflowService.getWorkflowInstances(limit, offset, filter)).build();
    }
}
