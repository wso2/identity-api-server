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

package org.wso2.carbon.identity.api.server.debug.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Request body for starting a debug session.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Request body for starting a debug session.")
public class DebugConnectionRequest  {
  
    private String connectionId;

    /**
    * Connection identifier for the selected resource type.
    **/
    public DebugConnectionRequest connectionId(String connectionId) {

        this.connectionId = connectionId;
        return this;
    }
    
    @ApiModelProperty(example = "11cb1448-2c30-4f9f-af8e-426614174000", value = "Connection identifier for the selected resource type.")
    @JsonProperty("connectionId")
    @Valid @Size(min=1,max=255)
    public String getConnectionId() {
        return connectionId;
    }
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
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
        return Objects.equals(this.connectionId, debugConnectionRequest.connectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DebugConnectionRequest {\n");
        
        sb.append("    connectionId: ").append(toIndentedString(connectionId)).append("\n");
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

