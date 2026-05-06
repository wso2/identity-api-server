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

public class ContextPath  {
  
    private String path;
    private Boolean encrypted = false;

    /**
    **/
    public ContextPath path(String path) {

        this.path = path;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("path")
    @Valid
    @NotNull(message = "Property path cannot be null.")

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    /**
    **/
    public ContextPath encrypted(Boolean encrypted) {

        this.encrypted = encrypted;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("encrypted")
    @Valid
    public Boolean getEncrypted() {
        return encrypted;
    }
    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContextPath contextPath = (ContextPath) o;
        return Objects.equals(this.path, contextPath.path) &&
            Objects.equals(this.encrypted, contextPath.encrypted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, encrypted);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ContextPath {\n");
        
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    encrypted: ").append(toIndentedString(encrypted)).append("\n");
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

