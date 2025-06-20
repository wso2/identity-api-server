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
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowTemplate;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowRequest  {
  
    private String name;
    private String description;
    private String engine;
    private WorkflowTemplate template;

    /**
    * Name of the created workflow
    **/
    public WorkflowRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "User Approval Workflow", required = true, value = "Name of the created workflow")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Description of the created workflow
    **/
    public WorkflowRequest description(String description) {

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
    public WorkflowRequest engine(String engine) {

        this.engine = engine;
        return this;
    }
    
    @ApiModelProperty(example = "Simple Workflow Engine", required = true, value = "Name of the selected workflow engine")
    @JsonProperty("engine")
    @Valid
    @NotNull(message = "Property engine cannot be null.")

    public String getEngine() {
        return engine;
    }
    public void setEngine(String engine) {
        this.engine = engine;
    }

    /**
    **/
    public WorkflowRequest template(WorkflowTemplate template) {

        this.template = template;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("template")
    @Valid
    public WorkflowTemplate getTemplate() {
        return template;
    }
    public void setTemplate(WorkflowTemplate template) {
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
        WorkflowRequest workflowRequest = (WorkflowRequest) o;
        return Objects.equals(this.name, workflowRequest.name) &&
            Objects.equals(this.description, workflowRequest.description) &&
            Objects.equals(this.engine, workflowRequest.engine) &&
            Objects.equals(this.template, workflowRequest.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, engine, template);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowRequest {\n");
        
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

