package org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.factories;

import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.core.WorkflowService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementServiceImpl;

/**
 * Factory class for WorkflowService.
 */
public class WorkflowServiceFactory {

    private static final WorkflowService SERVICE;

    static {
        WorkflowManagementService workflowManagementService = new WorkflowManagementServiceImpl();

        if (workflowManagementService == null) {
            throw new IllegalStateException("WorkflowManagementService is not available.");
        }

        SERVICE = new WorkflowService(workflowManagementService);
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
