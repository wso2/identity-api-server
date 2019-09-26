/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationStep;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthenticationSequence  {
  
    private List<AuthenticationStep> steps = null;

    private String script;
    private String subjectStepId;
    private String attributeStepId;

    /**
    **/
    public AuthenticationSequence steps(List<AuthenticationStep> steps) {

        this.steps = steps;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("steps")
    @Valid
    public List<AuthenticationStep> getSteps() {
        return steps;
    }
    public void setSteps(List<AuthenticationStep> steps) {
        this.steps = steps;
    }

    public AuthenticationSequence addStepsItem(AuthenticationStep stepsItem) {
        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }
        this.steps.add(stepsItem);
        return this;
    }

        /**
    **/
    public AuthenticationSequence script(String script) {

        this.script = script;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("script")
    @Valid
    public String getScript() {
        return script;
    }
    public void setScript(String script) {
        this.script = script;
    }

    /**
    **/
    public AuthenticationSequence subjectStepId(String subjectStepId) {

        this.subjectStepId = subjectStepId;
        return this;
    }
    
    @ApiModelProperty(example = "step1", value = "")
    @JsonProperty("subjectStepId")
    @Valid
    public String getSubjectStepId() {
        return subjectStepId;
    }
    public void setSubjectStepId(String subjectStepId) {
        this.subjectStepId = subjectStepId;
    }

    /**
    **/
    public AuthenticationSequence attributeStepId(String attributeStepId) {

        this.attributeStepId = attributeStepId;
        return this;
    }
    
    @ApiModelProperty(example = "step1", value = "")
    @JsonProperty("attributeStepId")
    @Valid
    public String getAttributeStepId() {
        return attributeStepId;
    }
    public void setAttributeStepId(String attributeStepId) {
        this.attributeStepId = attributeStepId;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationSequence authenticationSequence = (AuthenticationSequence) o;
        return Objects.equals(this.steps, authenticationSequence.steps) &&
            Objects.equals(this.script, authenticationSequence.script) &&
            Objects.equals(this.subjectStepId, authenticationSequence.subjectStepId) &&
            Objects.equals(this.attributeStepId, authenticationSequence.attributeStepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps, script, subjectStepId, attributeStepId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticationSequence {\n");
        
        sb.append("    steps: ").append(toIndentedString(steps)).append("\n");
        sb.append("    script: ").append(toIndentedString(script)).append("\n");
        sb.append("    subjectStepId: ").append(toIndentedString(subjectStepId)).append("\n");
        sb.append("    attributeStepId: ").append(toIndentedString(attributeStepId)).append("\n");
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

