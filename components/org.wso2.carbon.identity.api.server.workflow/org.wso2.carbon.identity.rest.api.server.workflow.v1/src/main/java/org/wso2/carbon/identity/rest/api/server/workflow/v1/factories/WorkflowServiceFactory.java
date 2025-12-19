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

import org.wso2.carbon.identity.api.server.workflow.common.WorkflowServiceHolder;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.core.WorkflowService;

/**
 * Factory class for WorkflowService.
 */
public class WorkflowServiceFactory {

    private static final WorkflowService SERVICE;

    static {
        org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService workflowManagementService =
                WorkflowServiceHolder.getWorkflowManagementService();
        org.wso2.carbon.identity.workflow.engine.ApprovalTaskService approvalTaskService =
                WorkflowServiceHolder.getApprovalTaskService();
        org.wso2.carbon.identity.rule.management.api.service.RuleManagementService ruleManagementService =
                WorkflowServiceHolder.getRuleManagementService();

        if (workflowManagementService == null) {
            throw new IllegalStateException("WorkflowManagementService is not available from OSGi context.");
        }
        if (approvalTaskService == null) {
            throw new IllegalStateException("ApprovalTaskService is not available from OSGi context.");
        }
        if (ruleManagementService == null) {
            throw new IllegalStateException("RuleManagementService is not available from OSGi context.");
        }

        SERVICE = new WorkflowService(workflowManagementService, approvalTaskService, ruleManagementService);
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
