/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.BasicOrganizationResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.Link;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class SharedOrganizationsResponse  {
  
    private List<Link> links = null;

    private List<BasicOrganizationResponse> organizations = null;


    /**
    **/
    public SharedOrganizationsResponse links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(example = "[{\"href\":\"/api/server/v1/applications/67f5a202-48c4-4313-9327-016da5f08f17/share?limit=10&recursive=false&next=MTA=\",\"rel\":\"next\"},{\"href\":\"/api/server/v1/applications/67f5a202-48c4-4313-9327-016da5f08f17/share?limit=10&recursive=false&after=MTA=\",\"rel\":\"previous\"}]", value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public SharedOrganizationsResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public SharedOrganizationsResponse organizations(List<BasicOrganizationResponse> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("organizations")
    @Valid
    public List<BasicOrganizationResponse> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<BasicOrganizationResponse> organizations) {
        this.organizations = organizations;
    }

    public SharedOrganizationsResponse addOrganizationsItem(BasicOrganizationResponse organizationsItem) {
        if (this.organizations == null) {
            this.organizations = new ArrayList<>();
        }
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
        SharedOrganizationsResponse sharedOrganizationsResponse = (SharedOrganizationsResponse) o;
        return Objects.equals(this.links, sharedOrganizationsResponse.links) &&
            Objects.equals(this.organizations, sharedOrganizationsResponse.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SharedOrganizationsResponse {\n");
        
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

