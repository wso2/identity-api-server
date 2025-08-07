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

@ApiModel(description = "Result response for generating a flow")
public class FlowGenerateResult {

    private String status;
    private FlowGenerateResultData data;

    /**
     *
     **/
    public FlowGenerateResult status(String status) {

        this.status = status;
        return this;
    }

    @ApiModelProperty(example = "SUCCESS", value = "")
    @JsonProperty("status")
    @Valid
    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    /**
     *
     **/
    public FlowGenerateResult data(FlowGenerateResultData data) {

        this.data = data;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("data")
    @Valid
    public FlowGenerateResultData getData() {

        return data;
    }

    public void setData(FlowGenerateResultData data) {

        this.data = data;
    }


    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowGenerateResult flowGenerateResult = (FlowGenerateResult) o;
        return Objects.equals(this.status, flowGenerateResult.status) &&
                Objects.equals(this.data, flowGenerateResult.data);
    }

    @Override
    public int hashCode() {

        return Objects.hash(status, data);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowGenerateResult {\n");

        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

