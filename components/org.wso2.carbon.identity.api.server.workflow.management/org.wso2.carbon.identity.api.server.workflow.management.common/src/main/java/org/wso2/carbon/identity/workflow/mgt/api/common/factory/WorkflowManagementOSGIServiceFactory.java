/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 LLC. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein in any form is strictly forbidden, unless permitted by WSO2 expressly.
 * You may not alter or remove any copyright or other notice from copies of this content.
 */

package org.wso2.carbon.identity.workflow.mgt.api.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.identity.workflow.mgt.WorkFlowExecutorManager;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the WorkFlowExecutorManager type of object inside the container.
 */
public class WorkflowManagementOSGIServiceFactory extends AbstractFactoryBean<WorkFlowExecutorManager> {

    private WorkFlowExecutorManager workFlowExecutorManager;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected WorkFlowExecutorManager createInstance() throws Exception {

        if (this.workFlowExecutorManager == null) {
            WorkFlowExecutorManager service = WorkFlowExecutorManager.getInstance();
            if (service != null) {
                this.workFlowExecutorManager = service;
            } else {
                throw new Exception("Unable to retrieve WorkFlowExecutorManager service.");
            }
        }
        return this.workFlowExecutorManager;
    }
}
