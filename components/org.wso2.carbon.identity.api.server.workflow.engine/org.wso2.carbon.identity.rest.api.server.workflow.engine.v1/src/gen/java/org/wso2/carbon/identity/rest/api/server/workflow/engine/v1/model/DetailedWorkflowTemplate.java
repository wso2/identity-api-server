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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.model.WorkflowTemplateParameters;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class DetailedWorkflowTemplate  {
  
    private String name;
    private String templateDescription;
    private List<WorkflowTemplateParameters> properties = null;


    /**
    * Name of the workflow template
    **/
    public DetailedWorkflowTemplate name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "MultiStepApproval", value = "Name of the workflow template")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Description of the workflow template
    **/
    public DetailedWorkflowTemplate templateDescription(String templateDescription) {

        this.templateDescription = templateDescription;
        return this;
    }
    
    @ApiModelProperty(example = "The operation should be approved by an authorized person with given role, to complete.", value = "Description of the workflow template")
    @JsonProperty("templateDescription")
    @Valid
    public String getTemplateDescription() {
        return templateDescription;
    }
    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
    }

    /**
    **/
    public DetailedWorkflowTemplate properties(List<WorkflowTemplateParameters> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("properties")
    @Valid
    public List<WorkflowTemplateParameters> getProperties() {
        return properties;
    }
    public void setProperties(List<WorkflowTemplateParameters> properties) {
        this.properties = properties;
    }

    public DetailedWorkflowTemplate addPropertiesItem(WorkflowTemplateParameters propertiesItem) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(propertiesItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DetailedWorkflowTemplate detailedWorkflowTemplate = (DetailedWorkflowTemplate) o;
        return Objects.equals(this.name, detailedWorkflowTemplate.name) &&
            Objects.equals(this.templateDescription, detailedWorkflowTemplate.templateDescription) &&
            Objects.equals(this.properties, detailedWorkflowTemplate.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, templateDescription, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DetailedWorkflowTemplate {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    templateDescription: ").append(toIndentedString(templateDescription)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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

