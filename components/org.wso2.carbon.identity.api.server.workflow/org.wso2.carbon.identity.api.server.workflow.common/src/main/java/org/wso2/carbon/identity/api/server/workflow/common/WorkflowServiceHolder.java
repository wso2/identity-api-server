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

package org.wso2.carbon.identity.api.server.workflow.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.workflow.impl.WorkflowImplServiceImpl;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementServiceImpl;

/**
 * Service holder class for workflow management.
 */
public class WorkflowServiceHolder {

    private static final Log log = LogFactory.getLog(WorkflowServiceHolder.class);
    private static final WorkflowManagementService service = new WorkflowManagementServiceImpl();

    public static WorkflowManagementService getWorkflowManagementService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving WorkflowManagementService instance.");
        }
        return service;
    }

    // This is a placeholder for the actual implementation of WorkflowImplServiceImpl.
    public static WorkflowImplServiceImpl getWorkflowImplService() {

        log.warn("WorkflowImplService implementation is not available. Returning null.");
        return null;
    }
}
