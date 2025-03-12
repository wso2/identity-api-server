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
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowTemplateBase;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowDetails  {
  
    private String id;
    private String name;
    private String description;
    private String engine;
    private WorkflowTemplateBase template;
    private String approvalTask;
    private String approvalTaskDescription;

    /**
    * Id of the workflow
    **/
    public WorkflowDetails id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "100", value = "Id of the workflow")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Name of the created workflow
    **/
    public WorkflowDetails name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "User Approval Workflow", value = "Name of the created workflow")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Description of the created workflow
    **/
    public WorkflowDetails description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Workflow to approve user role related requests", value = "Description of the created workflow")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Name of the selected workflow engine
    **/
    public WorkflowDetails engine(String engine) {

        this.engine = engine;
        return this;
    }
    
    @ApiModelProperty(example = "Simple Workflow Engine", value = "Name of the selected workflow engine")
    @JsonProperty("engine")
    @Valid
    public String getEngine() {
        return engine;
    }
    public void setEngine(String engine) {
        this.engine = engine;
    }

    /**
    **/
    public WorkflowDetails template(WorkflowTemplateBase template) {

        this.template = template;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("template")
    @Valid
    public WorkflowTemplateBase getTemplate() {
        return template;
    }
    public void setTemplate(WorkflowTemplateBase template) {
        this.template = template;
    }

    /**
    * Approval task subject to display
    **/
    public WorkflowDetails approvalTask(String approvalTask) {

        this.approvalTask = approvalTask;
        return this;
    }
    
    @ApiModelProperty(example = "Approval Required", value = "Approval task subject to display")
    @JsonProperty("approvalTask")
    @Valid
    public String getApprovalTask() {
        return approvalTask;
    }
    public void setApprovalTask(String approvalTask) {
        this.approvalTask = approvalTask;
    }

    /**
    * Description of the approval task
    **/
    public WorkflowDetails approvalTaskDescription(String approvalTaskDescription) {

        this.approvalTaskDescription = approvalTaskDescription;
        return this;
    }
    
    @ApiModelProperty(example = "Your approval is needed to complete this task", value = "Description of the approval task")
    @JsonProperty("approvalTaskDescription")
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
        WorkflowDetails workflowDetails = (WorkflowDetails) o;
        return Objects.equals(this.id, workflowDetails.id) &&
            Objects.equals(this.name, workflowDetails.name) &&
            Objects.equals(this.description, workflowDetails.description) &&
            Objects.equals(this.engine, workflowDetails.engine) &&
            Objects.equals(this.template, workflowDetails.template) &&
            Objects.equals(this.approvalTask, workflowDetails.approvalTask) &&
            Objects.equals(this.approvalTaskDescription, workflowDetails.approvalTaskDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, engine, template, approvalTask, approvalTaskDescription);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowDetails {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    engine: ").append(toIndentedString(engine)).append("\n");
        sb.append("    template: ").append(toIndentedString(template)).append("\n");
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

