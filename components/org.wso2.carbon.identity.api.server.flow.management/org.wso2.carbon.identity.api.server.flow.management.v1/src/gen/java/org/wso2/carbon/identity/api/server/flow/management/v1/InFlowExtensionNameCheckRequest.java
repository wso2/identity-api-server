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
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class InFlowExtensionNameCheckRequest  {
  
    private String name;
    private String excludeId;

    /**
    **/
    public InFlowExtensionNameCheckRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public InFlowExtensionNameCheckRequest excludeId(String excludeId) {

        this.excludeId = excludeId;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("excludeId")
    @Valid
    public String getExcludeId() {
        return excludeId;
    }
    public void setExcludeId(String excludeId) {
        this.excludeId = excludeId;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InFlowExtensionNameCheckRequest inFlowExtensionNameCheckRequest = (InFlowExtensionNameCheckRequest) o;
        return Objects.equals(this.name, inFlowExtensionNameCheckRequest.name) &&
            Objects.equals(this.excludeId, inFlowExtensionNameCheckRequest.excludeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, excludeId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InFlowExtensionNameCheckRequest {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    excludeId: ").append(toIndentedString(excludeId)).append("\n");
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

