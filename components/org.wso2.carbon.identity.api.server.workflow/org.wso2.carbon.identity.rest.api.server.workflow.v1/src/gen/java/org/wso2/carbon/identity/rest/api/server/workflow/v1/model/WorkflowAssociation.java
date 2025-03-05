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

public class WorkflowAssociation  {
  
    private String id;
    private String workflowAssociationName;
    private String operationName;
    private String workflowName;
    private String associationCondition;
    private Boolean isEnabled;

    /**
    * Unique id to represent a workflow association
    **/
    public WorkflowAssociation id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "500", value = "Unique id to represent a workflow association")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Name of the workflow association
    **/
    public WorkflowAssociation workflowAssociationName(String workflowAssociationName) {

        this.workflowAssociationName = workflowAssociationName;
        return this;
    }
    
    @ApiModelProperty(example = "User registration Workflow Association", value = "Name of the workflow association")
    @JsonProperty("workflowAssociationName")
    @Valid
    public String getWorkflowAssociationName() {
        return workflowAssociationName;
    }
    public void setWorkflowAssociationName(String workflowAssociationName) {
        this.workflowAssociationName = workflowAssociationName;
    }

    /**
    * User Operation
    **/
    public WorkflowAssociation operationName(String operationName) {

        this.operationName = operationName;
        return this;
    }
    
    @ApiModelProperty(example = "Add User", value = "User Operation")
    @JsonProperty("operationName")
    @Valid
    public String getOperationName() {
        return operationName;
    }
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
    * Assigned Workflow
    **/
    public WorkflowAssociation workflowName(String workflowName) {

        this.workflowName = workflowName;
        return this;
    }
    
    @ApiModelProperty(example = "User Registration Approval", value = "Assigned Workflow")
    @JsonProperty("workflowName")
    @Valid
    public String getWorkflowName() {
        return workflowName;
    }
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    /**
    * Assigned Association Condition
    **/
    public WorkflowAssociation associationCondition(String associationCondition) {

        this.associationCondition = associationCondition;
        return this;
    }
    
    @ApiModelProperty(example = "Role equals Manager", value = "Assigned Association Condition")
    @JsonProperty("associationCondition")
    @Valid
    public String getAssociationCondition() {
        return associationCondition;
    }
    public void setAssociationCondition(String associationCondition) {
        this.associationCondition = associationCondition;
    }

    /**
    * Association Status
    **/
    public WorkflowAssociation isEnabled(Boolean isEnabled) {

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



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowAssociation workflowAssociation = (WorkflowAssociation) o;
        return Objects.equals(this.id, workflowAssociation.id) &&
            Objects.equals(this.workflowAssociationName, workflowAssociation.workflowAssociationName) &&
            Objects.equals(this.operationName, workflowAssociation.operationName) &&
            Objects.equals(this.workflowName, workflowAssociation.workflowName) &&
            Objects.equals(this.associationCondition, workflowAssociation.associationCondition) &&
            Objects.equals(this.isEnabled, workflowAssociation.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workflowAssociationName, operationName, workflowName, associationCondition, isEnabled);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowAssociation {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    workflowAssociationName: ").append(toIndentedString(workflowAssociationName)).append("\n");
        sb.append("    operationName: ").append(toIndentedString(operationName)).append("\n");
        sb.append("    workflowName: ").append(toIndentedString(workflowName)).append("\n");
        sb.append("    associationCondition: ").append(toIndentedString(associationCondition)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
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

