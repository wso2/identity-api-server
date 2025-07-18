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
import org.wso2.carbon.identity.api.server.application.management.v1.RoleShareConfigAudience;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RoleShareConfig  {
  
    private String displayName;
    private RoleShareConfigAudience audience;

    /**
    * The human-readable name of the role
    **/
    public RoleShareConfig displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "The human-readable name of the role")
    @JsonProperty("displayName")
    @Valid
    @NotNull(message = "Property displayName cannot be null.")

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public RoleShareConfig audience(RoleShareConfigAudience audience) {

        this.audience = audience;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("audience")
    @Valid
    @NotNull(message = "Property audience cannot be null.")

    public RoleShareConfigAudience getAudience() {
        return audience;
    }
    public void setAudience(RoleShareConfigAudience audience) {
        this.audience = audience;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleShareConfig roleShareConfig = (RoleShareConfig) o;
        return Objects.equals(this.displayName, roleShareConfig.displayName) &&
            Objects.equals(this.audience, roleShareConfig.audience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, audience);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RoleShareConfig {\n");
        
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    audience: ").append(toIndentedString(audience)).append("\n");
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

