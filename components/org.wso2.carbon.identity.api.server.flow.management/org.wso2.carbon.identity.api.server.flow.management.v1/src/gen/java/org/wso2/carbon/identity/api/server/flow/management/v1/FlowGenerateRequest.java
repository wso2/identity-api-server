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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ApiModel(description = "Request payload for generating a flow")
public class FlowGenerateRequest {

    private String flowType;
    private String userQuery;

    /**
     * Type of the flow being updated
     **/
    public FlowGenerateRequest flowType(String flowType) {

        this.flowType = flowType;
        return this;
    }

    @ApiModelProperty(example = "REGISTRATION", required = true, value = "Type of the flow being updated")
    @JsonProperty("flowType")
    @Valid
    @NotNull(message = "Property flowType cannot be null.")

    public String getFlowType() {

        return flowType;
    }

    public void setFlowType(String flowType) {

        this.flowType = flowType;
    }

    /**
     * User query to generate the flow
     **/
    public FlowGenerateRequest userQuery(String userQuery) {

        this.userQuery = userQuery;
        return this;
    }

    @ApiModelProperty(example = "Generate a registration flow with basic details and passkey", value = "User query to generate the flow")
    @JsonProperty("userQuery")
    @Valid
    public String getUserQuery() {

        return userQuery;
    }

    public void setUserQuery(String userQuery) {

        this.userQuery = userQuery;
    }


    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowGenerateRequest flowGenerateRequest = (FlowGenerateRequest) o;
        return Objects.equals(this.flowType, flowGenerateRequest.flowType) &&
                Objects.equals(this.userQuery, flowGenerateRequest.userQuery);
    }

    @Override
    public int hashCode() {

        return Objects.hash(flowType, userQuery);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowGenerateRequest {\n");

        sb.append("    flowType: ").append(toIndentedString(flowType)).append("\n");
        sb.append("    userQuery: ").append(toIndentedString(userQuery)).append("\n");
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

