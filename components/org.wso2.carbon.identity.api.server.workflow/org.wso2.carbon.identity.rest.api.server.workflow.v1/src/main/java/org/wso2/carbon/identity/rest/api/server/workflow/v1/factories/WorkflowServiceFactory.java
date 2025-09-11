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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.workflow.common.WorkflowServiceHolder;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.core.WorkflowService;

/**
 * Factory class for WorkflowService.
 */
public class WorkflowServiceFactory {

    private static final Log log = LogFactory.getLog(WorkflowServiceFactory.class);
    private static final WorkflowService SERVICE;

    static {
        log.debug("Initializing WorkflowService in factory");
        SERVICE = new WorkflowService(WorkflowServiceHolder.getWorkflowManagementService(),
                WorkflowServiceHolder.getApprovalTaskService());
    }

    /**
     * Get WorkflowService.
     *
     * @return WorkflowService.
     */
    public static WorkflowService getWorkflowService() {

        return SERVICE;
    }
}
