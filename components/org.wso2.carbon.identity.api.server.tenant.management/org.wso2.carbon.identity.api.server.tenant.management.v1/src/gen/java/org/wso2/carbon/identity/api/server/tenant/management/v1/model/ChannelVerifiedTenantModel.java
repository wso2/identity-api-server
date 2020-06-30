/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.tenant.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.Owner;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ChannelVerifiedTenantModel  {
  
    private String domain;
    private String code;
    private String purpose;
    private List<Owner> owners = new ArrayList<>();


    /**
    * Tenant domain of the tenant.
    **/
    public ChannelVerifiedTenantModel domain(String domain) {

        this.domain = domain;
        return this;
    }
    
    @ApiModelProperty(example = "wso2.com", required = true, value = "Tenant domain of the tenant.")
    @JsonProperty("domain")
    @Valid
    @NotNull(message = "Property domain cannot be null.")

    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
    * Tenant domain of the tenant.
    **/
    public ChannelVerifiedTenantModel code(String code) {

        this.code = code;
        return this;
    }
    
    @ApiModelProperty(example = "3723dghdg32yt236734563236", required = true, value = "Tenant domain of the tenant.")
    @JsonProperty("code")
    @Valid
    @NotNull(message = "Property code cannot be null.")

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    /**
    * The purpose of the tenant creation. Personal or Organizational.
    **/
    public ChannelVerifiedTenantModel purpose(String purpose) {

        this.purpose = purpose;
        return this;
    }
    
    @ApiModelProperty(example = "personal", required = true, value = "The purpose of the tenant creation. Personal or Organizational.")
    @JsonProperty("purpose")
    @Valid
    @NotNull(message = "Property purpose cannot be null.")

    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
    **/
    public ChannelVerifiedTenantModel owners(List<Owner> owners) {

        this.owners = owners;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("owners")
    @Valid
    @NotNull(message = "Property owners cannot be null.")

    public List<Owner> getOwners() {
        return owners;
    }
    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public ChannelVerifiedTenantModel addOwnersItem(Owner ownersItem) {
        this.owners.add(ownersItem);
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
        ChannelVerifiedTenantModel channelVerifiedTenantModel = (ChannelVerifiedTenantModel) o;
        return Objects.equals(this.domain, channelVerifiedTenantModel.domain) &&
            Objects.equals(this.code, channelVerifiedTenantModel.code) &&
            Objects.equals(this.purpose, channelVerifiedTenantModel.purpose) &&
            Objects.equals(this.owners, channelVerifiedTenantModel.owners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, code, purpose, owners);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ChannelVerifiedTenantModel {\n");
        
        sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
        sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
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

