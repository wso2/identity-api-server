/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.Map;

/**
 * Request body for starting a debug session.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
@ApiModel(description = "Request body for starting a debug session.")
public class DebugConnectionRequest  {
  
    private Map<String, String> properties = null;


    /**
    * Resource-specific properties for the selected resource type.
    **/
    public DebugConnectionRequest properties(Map<String, String> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(example = "{\"sampleKey\":\"sampleValue\"}", value = "Resource-specific properties for the selected resource type.")
    @JsonAnyGetter
    @Valid
    public Map<String, String> getProperties() {

        return properties;
    }

    public void setProperties(Map<String, String> properties) {

        this.properties = properties;
    }


    public DebugConnectionRequest putPropertiesItem(String key, String propertiesItem) {

        if (this.properties == null) {
            this.properties = new HashMap<String, String>();
        }
        this.properties.put(key, propertiesItem);
        return this;
    }

    @JsonAnySetter
    public void putProperty(String key, String value) {

        putPropertiesItem(key, value);
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DebugConnectionRequest debugConnectionRequest = (DebugConnectionRequest) o;
        return Objects.equals(this.properties, debugConnectionRequest.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DebugConnectionRequest {\n");
        
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
