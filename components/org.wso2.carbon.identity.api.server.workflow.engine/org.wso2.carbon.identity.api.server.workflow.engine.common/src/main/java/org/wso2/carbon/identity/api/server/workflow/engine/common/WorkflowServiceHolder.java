package org.wso2.carbon.identity.api.server.workflow.engine.common;

import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementServiceImpl;

public class WorkflowServiceHolder {
    private final static WorkflowManagementService service = new WorkflowManagementServiceImpl();

    public static WorkflowManagementService getWorkflowManagementService()
    {
        return service;
    }


}
