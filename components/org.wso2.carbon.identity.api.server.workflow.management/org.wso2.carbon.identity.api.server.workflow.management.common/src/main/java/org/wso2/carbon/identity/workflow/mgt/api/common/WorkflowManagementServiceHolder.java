/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 LLC. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.carbon.identity.workflow.mgt.api.common;

import org.wso2.carbon.identity.workflow.mgt.WorkFlowExecutorManager;

/**
 * Service holder class for user organization management services.
 */
public class WorkflowManagementServiceHolder {

    private static WorkFlowExecutorManager workFlowExecutorManager;

    /**
     * Get workflow execute manager service.
     *
     * @return WorkFlowExecutorManager service.
     */
    public static WorkFlowExecutorManager getWorkFlowExecutorManager() {

        return workFlowExecutorManager;
    }

    /**
     * Set workflow executor management OSGi service.
     *
     * @param workFlowExecutorManager service.
     */
    public static void setWorkFlowExecutorManager(WorkFlowExecutorManager workFlowExecutorManager) {

        WorkflowManagementServiceHolder.workFlowExecutorManager = workFlowExecutorManager;
    }
}
