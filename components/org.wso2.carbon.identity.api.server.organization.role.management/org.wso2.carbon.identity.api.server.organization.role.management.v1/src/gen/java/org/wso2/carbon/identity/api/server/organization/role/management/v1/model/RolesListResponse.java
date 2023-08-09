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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.RoleObj;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RolesListResponse  {
  
    private Integer totalResults;
    private String nextCursor;
    private String previousCursor;
    private Integer itemsPerPage;
    private List<RoleObj> resources = null;


    /**
    * Total results to be fetched.
    **/
    public RolesListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "100", value = "Total results to be fetched.")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    * A cursor to obtain the next page of results in a subsequent request.
    **/
    public RolesListResponse nextCursor(String nextCursor) {

        this.nextCursor = nextCursor;
        return this;
    }
    
    @ApiModelProperty(example = "eyJjdXJzb3JWYWx1ZSI6ImQgcm9sZSIsImRpcmVjdGlvbiI6IkZPUldBUkQifQ", value = "A cursor to obtain the next page of results in a subsequent request.")
    @JsonProperty("nextCursor")
    @Valid
    public String getNextCursor() {
        return nextCursor;
    }
    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    /**
    * A cursor to obtain the previous page of results in a subsequent request.
    **/
    public RolesListResponse previousCursor(String previousCursor) {

        this.previousCursor = previousCursor;
        return this;
    }
    
    @ApiModelProperty(example = "eyJjdXJzb3JWYWx1ZSI6ImIgcm9sZSIsImRpcmVjdGlvbiI6IkJBQ0tXQVJEIn0", value = "A cursor to obtain the previous page of results in a subsequent request.")
    @JsonProperty("previousCursor")
    @Valid
    public String getPreviousCursor() {
        return previousCursor;
    }
    public void setPreviousCursor(String previousCursor) {
        this.previousCursor = previousCursor;
    }

    /**
    * Number of roles per page.
    **/
    public RolesListResponse itemsPerPage(Integer itemsPerPage) {

        this.itemsPerPage = itemsPerPage;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "Number of roles per page.")
    @JsonProperty("itemsPerPage")
    @Valid
    public Integer getItemsPerPage() {
        return itemsPerPage;
    }
    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    /**
    **/
    public RolesListResponse resources(List<RoleObj> resources) {

        this.resources = resources;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("Resources")
    @Valid
    public List<RoleObj> getResources() {
        return resources;
    }
    public void setResources(List<RoleObj> resources) {
        this.resources = resources;
    }

    public RolesListResponse addResourcesItem(RoleObj resourcesItem) {
        if (this.resources == null) {
            this.resources = new ArrayList<>();
        }
        this.resources.add(resourcesItem);
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
        RolesListResponse rolesListResponse = (RolesListResponse) o;
        return Objects.equals(this.totalResults, rolesListResponse.totalResults) &&
            Objects.equals(this.nextCursor, rolesListResponse.nextCursor) &&
            Objects.equals(this.previousCursor, rolesListResponse.previousCursor) &&
            Objects.equals(this.itemsPerPage, rolesListResponse.itemsPerPage) &&
            Objects.equals(this.resources, rolesListResponse.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, nextCursor, previousCursor, itemsPerPage, resources);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RolesListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    nextCursor: ").append(toIndentedString(nextCursor)).append("\n");
        sb.append("    previousCursor: ").append(toIndentedString(previousCursor)).append("\n");
        sb.append("    itemsPerPage: ").append(toIndentedString(itemsPerPage)).append("\n");
        sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
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

