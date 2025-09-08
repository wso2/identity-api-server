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

package org.wso2.carbon.identity.api.server.flow.execution.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;

/**
 * Flow execution error DTO for the flow execution API related error responses.
 */
public class FlowExecutionErrorDTO extends ErrorDTO {

    private static final long serialVersionUID = 6169725164223318439L;
    private String flowType = null;

    public FlowExecutionErrorDTO() {

        super();
    }

    public FlowExecutionErrorDTO(ErrorDTO errorDTO) {

        this.setCode(errorDTO.getCode());
        this.setMessage(errorDTO.getMessage());
        this.setDescription(errorDTO.getDescription());
        this.setRef(errorDTO.getRef());
    }

    @JsonProperty("flowType")
    public String getFlowType() {

        return flowType;
    }

    public void setFlowType(String flowType) {

        this.flowType = flowType;
    }
}
