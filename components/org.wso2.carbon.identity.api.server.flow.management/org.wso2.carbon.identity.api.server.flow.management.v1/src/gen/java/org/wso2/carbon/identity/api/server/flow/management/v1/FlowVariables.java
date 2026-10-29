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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowHeader;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRuntimeValue;
import javax.validation.constraints.*;

/**
 * What the flow defines besides its steps: the headers it reads from each request, and the runtime values it keeps. Conditions read them through the request.headers and runtimeValues fields.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "What the flow defines besides its steps: the headers it reads from each request, and the runtime values it keeps. Conditions read them through the request.headers and runtimeValues fields.")
public class FlowVariables  {
  
    private List<FlowHeader> headers = null;

    private List<FlowRuntimeValue> runtimeValues = null;


    /**
    * Headers the flow reads from each request. Names are matched ignoring case.
    **/
    public FlowVariables headers(List<FlowHeader> headers) {

        this.headers = headers;
        return this;
    }
    
    @ApiModelProperty(value = "Headers the flow reads from each request. Names are matched ignoring case.")
    @JsonProperty("headers")
    @Valid
    public List<FlowHeader> getHeaders() {
        return headers;
    }
    public void setHeaders(List<FlowHeader> headers) {
        this.headers = headers;
    }

    public FlowVariables addHeadersItem(FlowHeader headersItem) {
        if (this.headers == null) {
            this.headers = new ArrayList<FlowHeader>();
        }
        this.headers.add(headersItem);
        return this;
    }

    /**
    * Runtime values the flow keeps, each with the value every run of the flow starts from.
    **/
    public FlowVariables runtimeValues(List<FlowRuntimeValue> runtimeValues) {

        this.runtimeValues = runtimeValues;
        return this;
    }
    
    @ApiModelProperty(value = "Runtime values the flow keeps, each with the value every run of the flow starts from.")
    @JsonProperty("runtimeValues")
    @Valid
    public List<FlowRuntimeValue> getRuntimeValues() {
        return runtimeValues;
    }
    public void setRuntimeValues(List<FlowRuntimeValue> runtimeValues) {
        this.runtimeValues = runtimeValues;
    }

    public FlowVariables addRuntimeValuesItem(FlowRuntimeValue runtimeValuesItem) {
        if (this.runtimeValues == null) {
            this.runtimeValues = new ArrayList<FlowRuntimeValue>();
        }
        this.runtimeValues.add(runtimeValuesItem);
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
        FlowVariables flowVariables = (FlowVariables) o;
        return Objects.equals(this.headers, flowVariables.headers) &&
            Objects.equals(this.runtimeValues, flowVariables.runtimeValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers, runtimeValues);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowVariables {\n");
        
        sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
        sb.append("    runtimeValues: ").append(toIndentedString(runtimeValues)).append("\n");
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

