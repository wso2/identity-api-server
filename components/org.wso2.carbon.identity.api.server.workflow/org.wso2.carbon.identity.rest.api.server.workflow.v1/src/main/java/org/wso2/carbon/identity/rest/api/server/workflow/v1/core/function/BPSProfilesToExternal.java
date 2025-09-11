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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.core.function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.dto.WorkFlowEngineDTO;
import org.wso2.carbon.identity.workflow.impl.bean.BPSProfile;

import java.util.function.Function;

/**
 * Transform BPSProfile to WorkFlowEngineDTO
 */
public class BPSProfilesToExternal implements Function<BPSProfile, WorkFlowEngineDTO> {

    private static final Log log = LogFactory.getLog(BPSProfilesToExternal.class);

    @Override
    public WorkFlowEngineDTO apply(BPSProfile bpsProfile) {

        if (log.isDebugEnabled()) {
            log.debug("Transforming BPSProfile to WorkFlowEngineDTO for profile: " + 
                    (bpsProfile != null ? bpsProfile.getProfileName() : "null"));
        }
        if (bpsProfile == null) {
            return new WorkFlowEngineDTO();
        }
        WorkFlowEngineDTO workFlowEngineDTO = new WorkFlowEngineDTO();
        workFlowEngineDTO.setProfileName(bpsProfile.getProfileName());
        workFlowEngineDTO.setManagerHostURL(bpsProfile.getManagerHostURL());
        workFlowEngineDTO.setWorkerHostURL(bpsProfile.getWorkerHostURL());
        workFlowEngineDTO.setUserName(bpsProfile.getUsername());
        return workFlowEngineDTO;
    }
}
