/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.OrgShareConfig;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class IdentityProviderShareSelectedRequestBody  {
  
    private String identityProviderId;
    private List<OrgShareConfig> organizations = new ArrayList<>();


    /**
    **/
    public IdentityProviderShareSelectedRequestBody identityProviderId(String identityProviderId) {

        this.identityProviderId = identityProviderId;
        return this;
    }
    
    @ApiModelProperty(example = "c75e27f9-98c7-4518-a968-c6cd59f0ac6b", required = true, value = "")
    @JsonProperty("identityProviderId")
    @Valid
    @NotNull(message = "Property identityProviderId cannot be null.")

    public String getIdentityProviderId() {
        return identityProviderId;
    }
    public void setIdentityProviderId(String identityProviderId) {
        this.identityProviderId = identityProviderId;
    }

    /**
    **/
    public IdentityProviderShareSelectedRequestBody organizations(List<OrgShareConfig> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("organizations")
    @Valid
    @NotNull(message = "Property organizations cannot be null.")

    public List<OrgShareConfig> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<OrgShareConfig> organizations) {
        this.organizations = organizations;
    }

    public IdentityProviderShareSelectedRequestBody addOrganizationsItem(OrgShareConfig organizationsItem) {
        this.organizations.add(organizationsItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityProviderShareSelectedRequestBody identityProviderShareSelectedRequestBody = (IdentityProviderShareSelectedRequestBody) o;
        return Objects.equals(this.identityProviderId, identityProviderShareSelectedRequestBody.identityProviderId) &&
            Objects.equals(this.organizations, identityProviderShareSelectedRequestBody.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identityProviderId, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityProviderShareSelectedRequestBody {\n");
        
        sb.append("    identityProviderId: ").append(toIndentedString(identityProviderId)).append("\n");
        sb.append("    organizations: ").append(toIndentedString(organizations)).append("\n");
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

