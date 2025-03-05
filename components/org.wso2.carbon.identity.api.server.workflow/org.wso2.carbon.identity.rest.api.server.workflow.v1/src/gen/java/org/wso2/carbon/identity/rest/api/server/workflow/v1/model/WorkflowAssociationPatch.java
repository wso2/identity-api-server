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
import io.swagger.annotations.ApiModelProperty;


import java.util.Objects;
import javax.validation.Valid;

public class WorkflowAssociationPatch  {
  
    private String associationName;
    private String operationName;
    private String workflowId;
    private String associationCondition;
    private Status associationStatus;

    /**
    * Name of the workflow association
    **/
    public WorkflowAssociationPatch associationName(String associationName) {

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
    * Name of the user operation
    **/
    public WorkflowAssociationPatch operationName(String operationName) {

        this.operationName = operationName;
        return this;
    }
    
    @ApiModelProperty(example = "Add User", value = "Name of the user operation")
    @JsonProperty("operationName")
    @Valid
    public String getOperationName() {
        return operationName;
    }
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
    * Id of the assigned workflow
    **/
    public WorkflowAssociationPatch workflowId(String workflowId) {

        this.workflowId = workflowId;
        return this;
    }
    
    @ApiModelProperty(example = "456", value = "Id of the assigned workflow")
    @JsonProperty("workflowId")
    @Valid
    public String getWorkflowId() {
        return workflowId;
    }
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    /**
    * Condition added to the association
    **/
    public WorkflowAssociationPatch associationCondition(String associationCondition) {

        this.associationCondition = associationCondition;
        return this;
    }
    
    @ApiModelProperty(example = "Role Name equals Manager", value = "Condition added to the association")
    @JsonProperty("associationCondition")
    @Valid
    public String getAssociationCondition() {
        return associationCondition;
    }
    public void setAssociationCondition(String associationCondition) {
        this.associationCondition = associationCondition;
    }

    /**
    **/
    public WorkflowAssociationPatch associationStatus(Status associationStatus) {

        this.associationStatus = associationStatus;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("associationStatus")
    @Valid
    public Status getAssociationStatus() {
        return associationStatus;
    }
    public void setAssociationStatus(Status associationStatus) {
        this.associationStatus = associationStatus;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowAssociationPatch workflowAssociationPatch = (WorkflowAssociationPatch) o;
        return Objects.equals(this.associationName, workflowAssociationPatch.associationName) &&
            Objects.equals(this.operationName, workflowAssociationPatch.operationName) &&
            Objects.equals(this.workflowId, workflowAssociationPatch.workflowId) &&
            Objects.equals(this.associationCondition, workflowAssociationPatch.associationCondition) &&
            Objects.equals(this.associationStatus, workflowAssociationPatch.associationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(associationName, operationName, workflowId, associationCondition, associationStatus);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowAssociationPatch {\n");
        
        sb.append("    associationName: ").append(toIndentedString(associationName)).append("\n");
        sb.append("    operationName: ").append(toIndentedString(operationName)).append("\n");
        sb.append("    workflowId: ").append(toIndentedString(workflowId)).append("\n");
        sb.append("    associationCondition: ").append(toIndentedString(associationCondition)).append("\n");
        sb.append("    associationStatus: ").append(toIndentedString(associationStatus)).append("\n");
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

