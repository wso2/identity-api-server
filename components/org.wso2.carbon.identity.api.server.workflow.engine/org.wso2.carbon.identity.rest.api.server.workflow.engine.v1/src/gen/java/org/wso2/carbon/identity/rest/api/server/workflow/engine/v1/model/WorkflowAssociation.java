/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowAssociation  {
  
    private String id;
    private String workflowAssociationName;
    private String operation;
    private String workflowName;
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
    public WorkflowAssociation operation(String operation) {

        this.operation = operation;
        return this;
    }
    
    @ApiModelProperty(example = "Add User", value = "User Operation")
    @JsonProperty("operation")
    @Valid
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
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
            Objects.equals(this.operation, workflowAssociation.operation) &&
            Objects.equals(this.workflowName, workflowAssociation.workflowName) &&
            Objects.equals(this.isEnabled, workflowAssociation.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workflowAssociationName, operation, workflowName, isEnabled);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowAssociation {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    workflowAssociationName: ").append(toIndentedString(workflowAssociationName)).append("\n");
        sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
        sb.append("    workflowName: ").append(toIndentedString(workflowName)).append("\n");
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

