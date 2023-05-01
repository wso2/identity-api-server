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

/**
 * Decides whether an FIdP uses app role mappings.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Decides whether an FIdP uses app role mappings.")
public class AppRoleConfig  {
  
    private String idp;
    private Boolean useAppRoleMappings = false;

    /**
    * FIdP name.
    **/
    public AppRoleConfig idp(String idp) {

        this.idp = idp;
        return this;
    }
    
    @ApiModelProperty(example = "googleIdP", required = true, value = "FIdP name.")
    @JsonProperty("idp")
    @Valid
    @NotNull(message = "Property idp cannot be null.")

    public String getIdp() {
        return idp;
    }
    public void setIdp(String idp) {
        this.idp = idp;
    }

    /**
    * FIdP use application role mappings.
    **/
    public AppRoleConfig useAppRoleMappings(Boolean useAppRoleMappings) {

        this.useAppRoleMappings = useAppRoleMappings;
        return this;
    }
    
    @ApiModelProperty(example = "false", required = true, value = "FIdP use application role mappings.")
    @JsonProperty("useAppRoleMappings")
    @Valid
    @NotNull(message = "Property useAppRoleMappings cannot be null.")

    public Boolean getUseAppRoleMappings() {
        return useAppRoleMappings;
    }
    public void setUseAppRoleMappings(Boolean useAppRoleMappings) {
        this.useAppRoleMappings = useAppRoleMappings;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppRoleConfig appRoleConfig = (AppRoleConfig) o;
        return Objects.equals(this.idp, appRoleConfig.idp) &&
            Objects.equals(this.useAppRoleMappings, appRoleConfig.useAppRoleMappings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idp, useAppRoleMappings);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AppRoleConfig {\n");
        
        sb.append("    idp: ").append(toIndentedString(idp)).append("\n");
        sb.append("    useAppRoleMappings: ").append(toIndentedString(useAppRoleMappings)).append("\n");
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

