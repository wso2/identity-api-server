/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Whether flows may define headers and runtime values as variables on this server, and the limits their variables are checked against when a flow is saved.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Whether flows may define headers and runtime values as variables on this server, and the limits their variables are checked against when a flow is saved.")
public class FlowVariableConfigs  {
  
    private Boolean enabled;
    private Integer maxHeaders;
    private Integer maxRuntimeValues;
    private Integer maxValueLength;
    private Integer maxArraySize;

    /**
    **/
    public FlowVariableConfigs enabled(Boolean enabled) {

        this.enabled = enabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("enabled")
    @Valid
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
    * Most headers a flow may define.
    **/
    public FlowVariableConfigs maxHeaders(Integer maxHeaders) {

        this.maxHeaders = maxHeaders;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "Most headers a flow may define.")
    @JsonProperty("maxHeaders")
    @Valid
    public Integer getMaxHeaders() {
        return maxHeaders;
    }
    public void setMaxHeaders(Integer maxHeaders) {
        this.maxHeaders = maxHeaders;
    }

    /**
    * Most runtime values a flow may define.
    **/
    public FlowVariableConfigs maxRuntimeValues(Integer maxRuntimeValues) {

        this.maxRuntimeValues = maxRuntimeValues;
        return this;
    }
    
    @ApiModelProperty(example = "20", value = "Most runtime values a flow may define.")
    @JsonProperty("maxRuntimeValues")
    @Valid
    public Integer getMaxRuntimeValues() {
        return maxRuntimeValues;
    }
    public void setMaxRuntimeValues(Integer maxRuntimeValues) {
        this.maxRuntimeValues = maxRuntimeValues;
    }

    /**
    * Longest text a value, or an entry of an array value, may have.
    **/
    public FlowVariableConfigs maxValueLength(Integer maxValueLength) {

        this.maxValueLength = maxValueLength;
        return this;
    }
    
    @ApiModelProperty(example = "255", value = "Longest text a value, or an entry of an array value, may have.")
    @JsonProperty("maxValueLength")
    @Valid
    public Integer getMaxValueLength() {
        return maxValueLength;
    }
    public void setMaxValueLength(Integer maxValueLength) {
        this.maxValueLength = maxValueLength;
    }

    /**
    * Most entries an array value may have.
    **/
    public FlowVariableConfigs maxArraySize(Integer maxArraySize) {

        this.maxArraySize = maxArraySize;
        return this;
    }
    
    @ApiModelProperty(example = "20", value = "Most entries an array value may have.")
    @JsonProperty("maxArraySize")
    @Valid
    public Integer getMaxArraySize() {
        return maxArraySize;
    }
    public void setMaxArraySize(Integer maxArraySize) {
        this.maxArraySize = maxArraySize;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowVariableConfigs flowVariableConfigs = (FlowVariableConfigs) o;
        return Objects.equals(this.enabled, flowVariableConfigs.enabled) &&
            Objects.equals(this.maxHeaders, flowVariableConfigs.maxHeaders) &&
            Objects.equals(this.maxRuntimeValues, flowVariableConfigs.maxRuntimeValues) &&
            Objects.equals(this.maxValueLength, flowVariableConfigs.maxValueLength) &&
            Objects.equals(this.maxArraySize, flowVariableConfigs.maxArraySize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, maxHeaders, maxRuntimeValues, maxValueLength, maxArraySize);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowVariableConfigs {\n");
        
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    maxHeaders: ").append(toIndentedString(maxHeaders)).append("\n");
        sb.append("    maxRuntimeValues: ").append(toIndentedString(maxRuntimeValues)).append("\n");
        sb.append("    maxValueLength: ").append(toIndentedString(maxValueLength)).append("\n");
        sb.append("    maxArraySize: ").append(toIndentedString(maxArraySize)).append("\n");
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

