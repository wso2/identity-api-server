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
import org.wso2.carbon.identity.rest.api.server.workflow.v1.WorkflowInstancesApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.core.WorkflowService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.factories.WorkflowServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of the WorkflowInstancesApiService interface.
 */
public class WorkflowInstancesApiServiceImpl implements WorkflowInstancesApiService {

    private static final Log log = LogFactory.getLog(WorkflowInstancesApiServiceImpl.class);
    private final WorkflowService workflowService;

    public WorkflowInstancesApiServiceImpl() {
        
        log.debug("Initializing WorkflowInstancesApiServiceImpl");
        this.workflowService = WorkflowServiceFactory.getWorkflowService();
    }

    @Override
    public Response deleteWorkflowInstance(String instanceId) {

        log.debug("Deleting workflow instance with ID: " + instanceId);
        workflowService.deleteWorkflowInstance(instanceId);
        log.info("Successfully deleted workflow instance with ID: " + instanceId);
        return Response.noContent().build();
    }

    @Override
    public Response getWorkflowInstanceById(String instanceId) {

        log.debug("Retrieving workflow instance with ID: " + instanceId);
        return Response.ok().entity(workflowService.getWorkflowInstanceById(instanceId)).build();
    }

    @Override
    public Response getWorkflowInstances(Integer limit, Integer offset, String filter, String sortBy,
            String sortOrder) {

        if (log.isDebugEnabled()) {
            log.debug("Listing workflow instances with limit: " + limit + ", offset: " + offset);
        }
        return Response.ok().entity(workflowService.getWorkflowInstances(limit, offset, filter)).build();
    }
}
