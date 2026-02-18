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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.ApproverNotifications;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowTemplateParameters;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowTemplate  {
  
    private String name;
    private ApproverNotifications notificationsForApprovers;
    private List<WorkflowTemplateParameters> steps = null;


    /**
    * Name of the workflow template
    **/
    public WorkflowTemplate name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "MultiStepApprovalTemplate", value = "Name of the workflow template")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public WorkflowTemplate notificationsForApprovers(ApproverNotifications notificationsForApprovers) {

        this.notificationsForApprovers = notificationsForApprovers;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("notificationsForApprovers")
    @Valid
    public ApproverNotifications getNotificationsForApprovers() {
        return notificationsForApprovers;
    }
    public void setNotificationsForApprovers(ApproverNotifications notificationsForApprovers) {
        this.notificationsForApprovers = notificationsForApprovers;
    }

    /**
    **/
    public WorkflowTemplate steps(List<WorkflowTemplateParameters> steps) {

        this.steps = steps;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("steps")
    @Valid @Size(min=1)
    public List<WorkflowTemplateParameters> getSteps() {
        return steps;
    }
    public void setSteps(List<WorkflowTemplateParameters> steps) {
        this.steps = steps;
    }

    public WorkflowTemplate addStepsItem(WorkflowTemplateParameters stepsItem) {
        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }
        this.steps.add(stepsItem);
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
        WorkflowTemplate workflowTemplate = (WorkflowTemplate) o;
        return Objects.equals(this.name, workflowTemplate.name) &&
            Objects.equals(this.notificationsForApprovers, workflowTemplate.notificationsForApprovers) &&
            Objects.equals(this.steps, workflowTemplate.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, notificationsForApprovers, steps);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowTemplate {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    notificationsForApprovers: ").append(toIndentedString(notificationsForApprovers)).append("\n");
        sb.append("    steps: ").append(toIndentedString(steps)).append("\n");
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

