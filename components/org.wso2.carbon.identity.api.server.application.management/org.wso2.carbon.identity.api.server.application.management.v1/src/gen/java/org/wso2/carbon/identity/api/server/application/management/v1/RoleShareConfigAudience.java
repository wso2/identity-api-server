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

public class RoleShareConfigAudience  {
  
    private String display;
    private String type;

    /**
    * Display name of the audience (e.g., organization or application name)
    **/
    public RoleShareConfigAudience display(String display) {

        this.display = display;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Display name of the audience (e.g., organization or application name)")
    @JsonProperty("display")
    @Valid
    @NotNull(message = "Property display cannot be null.")

    public String getDisplay() {
        return display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
    * The type of the role audience: - &#x60;organization&#x60;: Role is scoped at organization level. - &#x60;application&#x60;: Role is scoped at application level. 
    **/
    public RoleShareConfigAudience type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "The type of the role audience: - `organization`: Role is scoped at organization level. - `application`: Role is scoped at application level. ")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleShareConfigAudience roleShareConfigAudience = (RoleShareConfigAudience) o;
        return Objects.equals(this.display, roleShareConfigAudience.display) &&
            Objects.equals(this.type, roleShareConfigAudience.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(display, type);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RoleShareConfigAudience {\n");
        
        sb.append("    display: ").append(toIndentedString(display)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

