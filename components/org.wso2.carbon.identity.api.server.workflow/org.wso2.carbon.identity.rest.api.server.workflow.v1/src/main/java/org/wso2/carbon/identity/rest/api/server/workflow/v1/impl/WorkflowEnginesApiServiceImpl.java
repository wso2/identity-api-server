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
import org.wso2.carbon.identity.rest.api.server.workflow.v1.WorkflowEnginesApiService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.core.WorkflowEngineService;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.factories.WorkflowEngineServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of workflow engines REST API.
 */
public class WorkflowEnginesApiServiceImpl implements WorkflowEnginesApiService {

    private static final Log log = LogFactory.getLog(WorkflowEnginesApiServiceImpl.class);
    private final WorkflowEngineService workflowEngineService;

    public WorkflowEnginesApiServiceImpl() {

        this.workflowEngineService = WorkflowEngineServiceFactory.getWorkflowEngineService();
    }

    @Override
    public Response searchWorkFlowEngines() {

        if (log.isDebugEnabled()) {
            log.debug("Received request to search workflow engines");
        }
        return Response.ok().entity(workflowEngineService.listWorkflowEngines()).build();
    }
}
