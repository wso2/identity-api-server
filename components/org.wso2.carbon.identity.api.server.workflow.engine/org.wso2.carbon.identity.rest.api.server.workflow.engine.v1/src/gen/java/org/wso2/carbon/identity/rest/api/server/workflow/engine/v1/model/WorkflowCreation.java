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
import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.model.WorkflowTemplate;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowCreation  {
  
    private String workflowName;
    private String workflowDescription;
    private WorkflowTemplate workflowTemplate;
    private String workflowEngine;
    private String approvalTask;
    private String approvalTaskDescription;

    /**
    * Name of the created workflow
    **/
    public WorkflowCreation workflowName(String workflowName) {

        this.workflowName = workflowName;
        return this;
    }
    
    @ApiModelProperty(example = "User Registration Workflow", required = true, value = "Name of the created workflow")
    @JsonProperty("workflowName")
    @Valid
    @NotNull(message = "Property workflowName cannot be null.")

    public String getWorkflowName() {
        return workflowName;
    }
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    /**
    * Description of the created workflow
    **/
    public WorkflowCreation workflowDescription(String workflowDescription) {

        this.workflowDescription = workflowDescription;
        return this;
    }
    
    @ApiModelProperty(example = "Workflow to approve new user registrations before account activation", value = "Description of the created workflow")
    @JsonProperty("workflowDescription")
    @Valid
    public String getWorkflowDescription() {
        return workflowDescription;
    }
    public void setWorkflowDescription(String workflowDescription) {
        this.workflowDescription = workflowDescription;
    }

    /**
    **/
    public WorkflowCreation workflowTemplate(WorkflowTemplate workflowTemplate) {

        this.workflowTemplate = workflowTemplate;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("workflowTemplate")
    @Valid
    @NotNull(message = "Property workflowTemplate cannot be null.")

    public WorkflowTemplate getWorkflowTemplate() {
        return workflowTemplate;
    }
    public void setWorkflowTemplate(WorkflowTemplate workflowTemplate) {
        this.workflowTemplate = workflowTemplate;
    }

    /**
    * Name of the selected workflow engine
    **/
    public WorkflowCreation workflowEngine(String workflowEngine) {

        this.workflowEngine = workflowEngine;
        return this;
    }
    
    @ApiModelProperty(example = "Simple Workflow Engine", required = true, value = "Name of the selected workflow engine")
    @JsonProperty("workflowEngine")
    @Valid
    @NotNull(message = "Property workflowEngine cannot be null.")

    public String getWorkflowEngine() {
        return workflowEngine;
    }
    public void setWorkflowEngine(String workflowEngine) {
        this.workflowEngine = workflowEngine;
    }

    /**
    * Approval task subject to display
    **/
    public WorkflowCreation approvalTask(String approvalTask) {

        this.approvalTask = approvalTask;
        return this;
    }
    
    @ApiModelProperty(example = "User Registration Approval", required = true, value = "Approval task subject to display")
    @JsonProperty("ApprovalTask")
    @Valid
    @NotNull(message = "Property approvalTask cannot be null.")

    public String getApprovalTask() {
        return approvalTask;
    }
    public void setApprovalTask(String approvalTask) {
        this.approvalTask = approvalTask;
    }

    /**
    * Description of the approval task
    **/
    public WorkflowCreation approvalTaskDescription(String approvalTaskDescription) {

        this.approvalTaskDescription = approvalTaskDescription;
        return this;
    }
    
    @ApiModelProperty(example = "Approval task to validate and approve new user registrations before account activation", value = "Description of the approval task")
    @JsonProperty("ApprovalTaskDescription")
    @Valid
    public String getApprovalTaskDescription() {
        return approvalTaskDescription;
    }
    public void setApprovalTaskDescription(String approvalTaskDescription) {
        this.approvalTaskDescription = approvalTaskDescription;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowCreation workflowCreation = (WorkflowCreation) o;
        return Objects.equals(this.workflowName, workflowCreation.workflowName) &&
            Objects.equals(this.workflowDescription, workflowCreation.workflowDescription) &&
            Objects.equals(this.workflowTemplate, workflowCreation.workflowTemplate) &&
            Objects.equals(this.workflowEngine, workflowCreation.workflowEngine) &&
            Objects.equals(this.approvalTask, workflowCreation.approvalTask) &&
            Objects.equals(this.approvalTaskDescription, workflowCreation.approvalTaskDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowName, workflowDescription, workflowTemplate, workflowEngine, approvalTask, approvalTaskDescription);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowCreation {\n");
        
        sb.append("    workflowName: ").append(toIndentedString(workflowName)).append("\n");
        sb.append("    workflowDescription: ").append(toIndentedString(workflowDescription)).append("\n");
        sb.append("    workflowTemplate: ").append(toIndentedString(workflowTemplate)).append("\n");
        sb.append("    workflowEngine: ").append(toIndentedString(workflowEngine)).append("\n");
        sb.append("    approvalTask: ").append(toIndentedString(approvalTask)).append("\n");
        sb.append("    approvalTaskDescription: ").append(toIndentedString(approvalTaskDescription)).append("\n");
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

