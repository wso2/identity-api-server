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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.ORRule;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.Operation;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowAssociationPatchRequest  {
  
    private String associationName;
    private Operation operation;
    private String workflowId;
    private Boolean isEnabled;
    private ORRule rule;

    /**
    * Name of the workflow association
    **/
    public WorkflowAssociationPatchRequest associationName(String associationName) {

        this.associationName = associationName;
        return this;
    }
    
    @ApiModelProperty(example = "User Registration Workflow Association", value = "Name of the workflow association")
    @JsonProperty("associationName")
    @Valid
    public String getAssociationName() {
        return associationName;
    }
    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }

    /**
    **/
    public WorkflowAssociationPatchRequest operation(Operation operation) {

        this.operation = operation;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("operation")
    @Valid
    public Operation getOperation() {
        return operation;
    }
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
    * Id of the assigned workflow
    **/
    public WorkflowAssociationPatchRequest workflowId(String workflowId) {

        this.workflowId = workflowId;
        return this;
    }
    
    @ApiModelProperty(example = "100", value = "Id of the assigned workflow")
    @JsonProperty("workflowId")
    @Valid
    public String getWorkflowId() {
        return workflowId;
    }
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    /**
    * Association Status
    **/
    public WorkflowAssociationPatchRequest isEnabled(Boolean isEnabled) {

        this.isEnabled = isEnabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Association Status")
    @JsonProperty("isEnabled")
    @Valid
    public Boolean getIsEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
    **/
    public WorkflowAssociationPatchRequest rule(ORRule rule) {

        this.rule = rule;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("rule")
    @Valid
    public ORRule getRule() {
        return rule;
    }
    public void setRule(ORRule rule) {
        this.rule = rule;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowAssociationPatchRequest workflowAssociationPatchRequest = (WorkflowAssociationPatchRequest) o;
        return Objects.equals(this.associationName, workflowAssociationPatchRequest.associationName) &&
            Objects.equals(this.operation, workflowAssociationPatchRequest.operation) &&
            Objects.equals(this.workflowId, workflowAssociationPatchRequest.workflowId) &&
            Objects.equals(this.isEnabled, workflowAssociationPatchRequest.isEnabled) &&
            Objects.equals(this.rule, workflowAssociationPatchRequest.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(associationName, operation, workflowId, isEnabled, rule);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowAssociationPatchRequest {\n");
        
        sb.append("    associationName: ").append(toIndentedString(associationName)).append("\n");
        sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
        sb.append("    workflowId: ").append(toIndentedString(workflowId)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    rule: ").append(toIndentedString(rule)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

