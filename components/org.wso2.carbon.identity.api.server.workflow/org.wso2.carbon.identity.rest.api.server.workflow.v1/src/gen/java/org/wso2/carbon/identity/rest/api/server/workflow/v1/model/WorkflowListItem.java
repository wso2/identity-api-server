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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowListItem  {
  
    private String id;
    private String name;
    private String description;
    private String engine;
    private String template;

    /**
    * Unique id to represent a workflow
    **/
    public WorkflowListItem id(String id) {

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
    public WorkflowListItem name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "User Role Approval", value = "Display name of the workflow")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Detailed description of the workflow
    **/
    public WorkflowListItem description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Workflow to approve user role related requests", value = "Detailed description of the workflow")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Category in which the workflow is deployed
    **/
    public WorkflowListItem engine(String engine) {

        this.engine = engine;
        return this;
    }
    
    @ApiModelProperty(example = "Simple Workflow Engine", value = "Category in which the workflow is deployed")
    @JsonProperty("engine")
    @Valid
    public String getEngine() {
        return engine;
    }
    public void setEngine(String engine) {
        this.engine = engine;
    }

    /**
    * Template defining the approval process for the workflow
    **/
    public WorkflowListItem template(String template) {

        this.template = template;
        return this;
    }
    
    @ApiModelProperty(example = "MultiStepApprovalTemplate", value = "Template defining the approval process for the workflow")
    @JsonProperty("template")
    @Valid
    public String getTemplate() {
        return template;
    }
    public void setTemplate(String template) {
        this.template = template;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowListItem workflowListItem = (WorkflowListItem) o;
        return Objects.equals(this.id, workflowListItem.id) &&
            Objects.equals(this.name, workflowListItem.name) &&
            Objects.equals(this.description, workflowListItem.description) &&
            Objects.equals(this.engine, workflowListItem.engine) &&
            Objects.equals(this.template, workflowListItem.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, engine, template);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowListItem {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    engine: ").append(toIndentedString(engine)).append("\n");
        sb.append("    template: ").append(toIndentedString(template)).append("\n");
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

