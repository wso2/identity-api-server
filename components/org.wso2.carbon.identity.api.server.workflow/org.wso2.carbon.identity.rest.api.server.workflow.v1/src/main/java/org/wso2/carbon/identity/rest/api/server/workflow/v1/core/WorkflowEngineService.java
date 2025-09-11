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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.core.function.BPSProfilesToExternal;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.dto.WorkFlowEngineDTO;
import org.wso2.carbon.identity.workflow.impl.WorkflowImplException;
import org.wso2.carbon.identity.workflow.impl.WorkflowImplServiceImpl;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.rest.api.server.workflow.v1.core.WorkflowEngineConstants.ErrorMessage
        .ERROR_CODE_ERROR_RETRIEVING_BPS_PROFILES;

/**
 * Workflow engine service class
 */
public class WorkflowEngineService {

    private static final Log log = LogFactory.getLog(WorkflowEngineService.class);
    private final WorkflowImplServiceImpl workflowImplService;

    public WorkflowEngineService(WorkflowImplServiceImpl workflowImplService) {

        log.debug("Initializing WorkflowEngineService");
        this.workflowImplService = workflowImplService;
    }

    public List<WorkFlowEngineDTO> listWorkflowEngines() {

        log.debug("Listing workflow engines");
        try {
            int tenantId = IdentityTenantUtil.getTenantId(ContextLoader.getTenantDomainFromContext());
            List<WorkFlowEngineDTO> engines = workflowImplService.listBPSProfiles(tenantId).stream()
                    .map(new BPSProfilesToExternal()).collect(Collectors.toList());
            if (log.isDebugEnabled()) {
                log.debug("Found " + engines.size() + " workflow engines");
            }
            return engines;
        } catch (WorkflowImplException e) {
            log.error("Error retrieving BPS profiles for workflow engines", e);
            throw handleError(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_ERROR_RETRIEVING_BPS_PROFILES);
        }
    }

    private APIError handleError(Response.Status status, WorkflowEngineConstants.ErrorMessage error) {

        return new APIError(status, getErrorBuilder(error).build());
    }

    private ErrorResponse.Builder getErrorBuilder(WorkflowEngineConstants.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(errorMsg.getDescription());
    }
}
