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
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowRequestValueInput;
import org.wso2.carbon.identity.api.server.flow.management.v1.FlowVariableInput;
import javax.validation.constraints.*;

/**
 * What the flow takes in besides its steps: the headers and query parameters it reads from each request, and the variables it keeps. Conditions read them through the request.headers, request.queryParameters and variables fields.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "What the flow takes in besides its steps: the headers and query parameters it reads from each request, and the variables it keeps. Conditions read them through the request.headers, request.queryParameters and variables fields.")
public class FlowInputs  {
  
    private List<FlowRequestValueInput> headers = null;

    private List<FlowRequestValueInput> queryParams = null;

    private List<FlowVariableInput> variables = null;


    /**
    * Headers the flow reads from each request. Names are matched ignoring case.
    **/
    public FlowInputs headers(List<FlowRequestValueInput> headers) {

        this.headers = headers;
        return this;
    }
    
    @ApiModelProperty(value = "Headers the flow reads from each request. Names are matched ignoring case.")
    @JsonProperty("headers")
    @Valid
    public List<FlowRequestValueInput> getHeaders() {
        return headers;
    }
    public void setHeaders(List<FlowRequestValueInput> headers) {
        this.headers = headers;
    }

    public FlowInputs addHeadersItem(FlowRequestValueInput headersItem) {
        if (this.headers == null) {
            this.headers = new ArrayList<FlowRequestValueInput>();
        }
        this.headers.add(headersItem);
        return this;
    }

    /**
    * Query parameters the flow reads from each request.
    **/
    public FlowInputs queryParams(List<FlowRequestValueInput> queryParams) {

        this.queryParams = queryParams;
        return this;
    }
    
    @ApiModelProperty(value = "Query parameters the flow reads from each request.")
    @JsonProperty("queryParams")
    @Valid
    public List<FlowRequestValueInput> getQueryParams() {
        return queryParams;
    }
    public void setQueryParams(List<FlowRequestValueInput> queryParams) {
        this.queryParams = queryParams;
    }

    public FlowInputs addQueryParamsItem(FlowRequestValueInput queryParamsItem) {
        if (this.queryParams == null) {
            this.queryParams = new ArrayList<FlowRequestValueInput>();
        }
        this.queryParams.add(queryParamsItem);
        return this;
    }

    /**
    * Variables the flow keeps, each with the value every run of the flow starts from.
    **/
    public FlowInputs variables(List<FlowVariableInput> variables) {

        this.variables = variables;
        return this;
    }
    
    @ApiModelProperty(value = "Variables the flow keeps, each with the value every run of the flow starts from.")
    @JsonProperty("variables")
    @Valid
    public List<FlowVariableInput> getVariables() {
        return variables;
    }
    public void setVariables(List<FlowVariableInput> variables) {
        this.variables = variables;
    }

    public FlowInputs addVariablesItem(FlowVariableInput variablesItem) {
        if (this.variables == null) {
            this.variables = new ArrayList<FlowVariableInput>();
        }
        this.variables.add(variablesItem);
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
        FlowInputs flowInputs = (FlowInputs) o;
        return Objects.equals(this.headers, flowInputs.headers) &&
            Objects.equals(this.queryParams, flowInputs.queryParams) &&
            Objects.equals(this.variables, flowInputs.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(headers, queryParams, variables);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowInputs {\n");
        
        sb.append("    headers: ").append(toIndentedString(headers)).append("\n");
        sb.append("    queryParams: ").append(toIndentedString(queryParams)).append("\n");
        sb.append("    variables: ").append(toIndentedString(variables)).append("\n");
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

