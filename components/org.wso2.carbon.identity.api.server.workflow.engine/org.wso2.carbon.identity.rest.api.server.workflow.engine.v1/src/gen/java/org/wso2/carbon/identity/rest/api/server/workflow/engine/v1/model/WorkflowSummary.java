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

public class WorkflowSummary  {
  
    private String id;
    private String workflowName;
    private String workflowDescription;
    private String workflowTemplate;
    private String deployment;

    /**
    * Unique id to represent a workflow
    **/
    public WorkflowSummary id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "100", value = "Unique id to represent a workflow")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Display name of the workflow
    **/
    public WorkflowSummary workflowName(String workflowName) {

        this.workflowName = workflowName;
        return this;
    }
    
    @ApiModelProperty(example = "User Role Approval", value = "Display name of the workflow")
    @JsonProperty("workflowName")
    @Valid
    public String getWorkflowName() {
        return workflowName;
    }
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    /**
    * Detailed description of the workflow
    **/
    public WorkflowSummary workflowDescription(String workflowDescription) {

        this.workflowDescription = workflowDescription;
        return this;
    }
    
    @ApiModelProperty(example = "Workflow to approve new user registrations before account activation", value = "Detailed description of the workflow")
    @JsonProperty("workflowDescription")
    @Valid
    public String getWorkflowDescription() {
        return workflowDescription;
    }
    public void setWorkflowDescription(String workflowDescription) {
        this.workflowDescription = workflowDescription;
    }

    /**
    * Template defining the approval process for the workflow
    **/
    public WorkflowSummary workflowTemplate(String workflowTemplate) {

        this.workflowTemplate = workflowTemplate;
        return this;
    }
    
    @ApiModelProperty(example = "MultiStepApprovalTemplate", value = "Template defining the approval process for the workflow")
    @JsonProperty("workflowTemplate")
    @Valid
    public String getWorkflowTemplate() {
        return workflowTemplate;
    }
    public void setWorkflowTemplate(String workflowTemplate) {
        this.workflowTemplate = workflowTemplate;
    }

    /**
    * Category in which the workflow is deployed
    **/
    public WorkflowSummary deployment(String deployment) {

        this.deployment = deployment;
        return this;
    }
    
    @ApiModelProperty(example = "ApprovalWorkflow", value = "Category in which the workflow is deployed")
    @JsonProperty("deployment")
    @Valid
    public String getDeployment() {
        return deployment;
    }
    public void setDeployment(String deployment) {
        this.deployment = deployment;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowSummary workflowSummary = (WorkflowSummary) o;
        return Objects.equals(this.id, workflowSummary.id) &&
            Objects.equals(this.workflowName, workflowSummary.workflowName) &&
            Objects.equals(this.workflowDescription, workflowSummary.workflowDescription) &&
            Objects.equals(this.workflowTemplate, workflowSummary.workflowTemplate) &&
            Objects.equals(this.deployment, workflowSummary.deployment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workflowName, workflowDescription, workflowTemplate, deployment);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowSummary {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    workflowName: ").append(toIndentedString(workflowName)).append("\n");
        sb.append("    workflowDescription: ").append(toIndentedString(workflowDescription)).append("\n");
        sb.append("    workflowTemplate: ").append(toIndentedString(workflowTemplate)).append("\n");
        sb.append("    deployment: ").append(toIndentedString(deployment)).append("\n");
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

