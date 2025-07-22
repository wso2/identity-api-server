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
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

public class FlowMetaResponseConnectionMeta  {
  
    private List<Map<String, Object>> supportedConnections = null;


    /**
    **/
    public FlowMetaResponseConnectionMeta supportedConnections(List<Map<String, Object>> supportedConnections) {

        this.supportedConnections = supportedConnections;
        return this;
    }
    
    @ApiModelProperty(example = "[\"Email OTP\",\"SMS OTP\",\"Passkey\",\"LinkedIn\"]", value = "")
    @JsonProperty("supportedConnections")
    @Valid
    public List<Map<String, Object>> getSupportedConnections() {
        return supportedConnections;
    }
    public void setSupportedConnections(List<Map<String, Object>> supportedConnections) {
        this.supportedConnections = supportedConnections;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowMetaResponseConnectionMeta flowMetaResponseConnectionMeta = (FlowMetaResponseConnectionMeta) o;
        return Objects.equals(this.supportedConnections, flowMetaResponseConnectionMeta.supportedConnections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supportedConnections);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowMetaResponseConnectionMeta {\n");
        
        sb.append("    supportedConnections: ").append(toIndentedString(supportedConnections)).append("\n");
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

