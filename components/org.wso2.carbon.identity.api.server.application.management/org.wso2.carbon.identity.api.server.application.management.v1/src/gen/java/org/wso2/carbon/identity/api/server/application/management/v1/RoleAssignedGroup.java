/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RoleAssignedGroup  {
  
    private String $ref;
    private String display;
    private String value;

    /**
    **/
    public RoleAssignedGroup $ref(String $ref) {

        this.$ref = $ref;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/scim2/Groups/3a12bae9-4386-44be-befd-caf349297f45", value = "")
    @JsonProperty("$ref")
    @Valid
    public String get$Ref() {
        return $ref;
    }
    public void set$Ref(String $ref) {
        this.$ref = $ref;
    }

    /**
    **/
    public RoleAssignedGroup display(String display) {

        this.display = display;
        return this;
    }
    
    @ApiModelProperty(example = "display", value = "")
    @JsonProperty("display")
    @Valid
    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
    **/
    public RoleAssignedGroup value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "e44dbc52-dcc3-443d-96f5-fe9dc208e9d8", value = "")
    @JsonProperty("value")
    @Valid
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleAssignedGroup roleAssignedGroup = (RoleAssignedGroup) o;
        return Objects.equals(this.$ref, roleAssignedGroup.$ref) &&
            Objects.equals(this.display, roleAssignedGroup.display) &&
            Objects.equals(this.value, roleAssignedGroup.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash($ref, display, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RoleAssignedGroup {\n");
        
        sb.append("    $ref: ").append(toIndentedString($ref)).append("\n");
        sb.append("    display: ").append(toIndentedString(display)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

