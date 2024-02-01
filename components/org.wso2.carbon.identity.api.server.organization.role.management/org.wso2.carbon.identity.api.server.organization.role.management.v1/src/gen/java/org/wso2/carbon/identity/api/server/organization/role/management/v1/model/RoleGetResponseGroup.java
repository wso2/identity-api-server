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

package org.wso2.carbon.identity.api.server.organization.role.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RoleGetResponseGroup  {
  
    private String $ref;
    private String display;
    private String value;

    /**
    **/
    public RoleGetResponseGroup $ref(String $ref) {

        this.$ref = $ref;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/o/10084a8d-113f-4211-a0d5-efe36b082211/api/server/v1/organizations/48e31bc5-1669-4de1-bb22-c71e443aeb8b/groups/7bac6a86-1f21-4937-9fb1-5be4a93ef469", value = "")
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
    public RoleGetResponseGroup display(String display) {

        this.display = display;
        return this;
    }
    
    @ApiModelProperty(example = "PRIMARY/manager", value = "")
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
    public RoleGetResponseGroup value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(example = "7bac6a86-1f21-4937-9fb1-5be4a93ef469", value = "")
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
        RoleGetResponseGroup roleGetResponseGroup = (RoleGetResponseGroup) o;
        return Objects.equals(this.$ref, roleGetResponseGroup.$ref) &&
            Objects.equals(this.display, roleGetResponseGroup.display) &&
            Objects.equals(this.value, roleGetResponseGroup.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash($ref, display, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RoleGetResponseGroup {\n");
        
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

