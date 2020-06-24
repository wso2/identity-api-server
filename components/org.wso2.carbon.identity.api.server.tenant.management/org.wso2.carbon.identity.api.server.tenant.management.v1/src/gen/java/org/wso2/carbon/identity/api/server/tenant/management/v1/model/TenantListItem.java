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
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.LifeCycleStatus;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.OwnerResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class TenantListItem  {
  
    private String id;
    private String domain;
    private List<OwnerResponse> owners = null;

    private String createdDate;
    private LifeCycleStatus lifecycleStatus;
    private String region;

    /**
    * tenant id of the tenant owner.
    **/
    public TenantListItem id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "123e4567-e89b-12d3-a456-556642440000", value = "tenant id of the tenant owner.")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Tenant domain of the tenant.
    **/
    public TenantListItem domain(String domain) {

        this.domain = domain;
        return this;
    }
    
    @ApiModelProperty(example = "abc.com", value = "Tenant domain of the tenant.")
    @JsonProperty("domain")
    @Valid
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
    **/
    public TenantListItem owners(List<OwnerResponse> owners) {

        this.owners = owners;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("owners")
    @Valid
    public List<OwnerResponse> getOwners() {
        return owners;
    }
    public void setOwners(List<OwnerResponse> owners) {
        this.owners = owners;
    }

    public TenantListItem addOwnersItem(OwnerResponse ownersItem) {
        if (this.owners == null) {
            this.owners = new ArrayList<>();
        }
        this.owners.add(ownersItem);
        return this;
    }

        /**
    * Tenant created time.
    **/
    public TenantListItem createdDate(String createdDate) {

        this.createdDate = createdDate;
        return this;
    }
    
    @ApiModelProperty(example = "2020-03-03T17:04:06.570+05:30", value = "Tenant created time.")
    @JsonProperty("createdDate")
    @Valid
    public String getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
    **/
    public TenantListItem lifecycleStatus(LifeCycleStatus lifecycleStatus) {

        this.lifecycleStatus = lifecycleStatus;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("lifecycleStatus")
    @Valid
    public LifeCycleStatus getLifecycleStatus() {
        return lifecycleStatus;
    }
    public void setLifecycleStatus(LifeCycleStatus lifecycleStatus) {
        this.lifecycleStatus = lifecycleStatus;
    }

    /**
    * Region of the tenant.
    **/
    public TenantListItem region(String region) {

        this.region = region;
        return this;
    }
    
    @ApiModelProperty(example = "USA", value = "Region of the tenant.")
    @JsonProperty("region")
    @Valid
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantListItem tenantListItem = (TenantListItem) o;
        return Objects.equals(this.id, tenantListItem.id) &&
            Objects.equals(this.domain, tenantListItem.domain) &&
            Objects.equals(this.owners, tenantListItem.owners) &&
            Objects.equals(this.createdDate, tenantListItem.createdDate) &&
            Objects.equals(this.lifecycleStatus, tenantListItem.lifecycleStatus) &&
            Objects.equals(this.region, tenantListItem.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, domain, owners, createdDate, lifecycleStatus, region);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class TenantListItem {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
        sb.append("    owners: ").append(toIndentedString(owners)).append("\n");
        sb.append("    createdDate: ").append(toIndentedString(createdDate)).append("\n");
        sb.append("    lifecycleStatus: ").append(toIndentedString(lifecycleStatus)).append("\n");
        sb.append("    region: ").append(toIndentedString(region)).append("\n");
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

