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

public class WorkflowSummary  {
  
    private String id;
    private String workflowName;
    private String workflowDescription;
    private String workflowEngine;
    private String workflowTemplate;

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
    * Category in which the workflow is deployed
    **/
    public WorkflowSummary workflowEngine(String workflowEngine) {

        this.workflowEngine = workflowEngine;
        return this;
    }
    
    @ApiModelProperty(example = "Simple Workflow Engine", value = "Category in which the workflow is deployed")
    @JsonProperty("workflowEngine")
    @Valid
    public String getWorkflowEngine() {
        return workflowEngine;
    }
    public void setWorkflowEngine(String workflowEngine) {
        this.workflowEngine = workflowEngine;
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
            Objects.equals(this.workflowEngine, workflowSummary.workflowEngine) &&
            Objects.equals(this.workflowTemplate, workflowSummary.workflowTemplate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workflowName, workflowDescription, workflowEngine, workflowTemplate);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowSummary {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    workflowName: ").append(toIndentedString(workflowName)).append("\n");
        sb.append("    workflowDescription: ").append(toIndentedString(workflowDescription)).append("\n");
        sb.append("    workflowEngine: ").append(toIndentedString(workflowEngine)).append("\n");
        sb.append("    workflowTemplate: ").append(toIndentedString(workflowTemplate)).append("\n");
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

