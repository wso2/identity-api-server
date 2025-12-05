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

package org.wso2.carbon.identity.api.server.vc.config.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class VCCredentialConfigurationListItem  {
  
    private String id;
    private String identifier;
    private String displayName;
    private String scope;

    /**
    **/
    public VCCredentialConfigurationListItem id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "7e5f9d2a-6b5e-4df6-9b87-8a3d1a4a0c31", required = true, value = "")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Stable key; human/machine readable.
    **/
    public VCCredentialConfigurationListItem identifier(String identifier) {

        this.identifier = identifier;
        return this;
    }
    
    @ApiModelProperty(example = "EmployeeBadge", required = true, value = "Stable key; human/machine readable.")
    @JsonProperty("identifier")
    @Valid
    @NotNull(message = "Property identifier cannot be null.")

    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
    * Published as &#x60;credential_configuration_id&#x60; in issuer metadata. Defaults to &#x60;identifier&#x60; if omitted.
    **/
    public VCCredentialConfigurationListItem displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "EmployeeBadge", required = true, value = "Published as `credential_configuration_id` in issuer metadata. Defaults to `identifier` if omitted.")
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
    public VCCredentialConfigurationListItem scope(String scope) {

        this.scope = scope;
        return this;
    }
    
    @ApiModelProperty(example = "employee_badge", required = true, value = "")
    @JsonProperty("scope")
    @Valid
    @NotNull(message = "Property scope cannot be null.")

    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VCCredentialConfigurationListItem vcCredentialConfigurationListItem = (VCCredentialConfigurationListItem) o;
        return Objects.equals(this.id, vcCredentialConfigurationListItem.id) &&
            Objects.equals(this.identifier, vcCredentialConfigurationListItem.identifier) &&
            Objects.equals(this.displayName, vcCredentialConfigurationListItem.displayName) &&
            Objects.equals(this.scope, vcCredentialConfigurationListItem.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, displayName, scope);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCCredentialConfigurationListItem {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
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

