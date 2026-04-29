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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganization;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.Link;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.SharingMode;
import javax.validation.constraints.*;

/**
 * Response listing organizations where an agent has shared access, including sharing modes and role assignments.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Response listing organizations where an agent has shared access, including sharing modes and role assignments.")
public class AgentSharedOrganizationsResponse  {
  
    private List<Link> links = null;

    private SharingMode sharingMode;
    private List<AgentSharedOrganization> organizations = null;


    /**
    **/
    public AgentSharedOrganizationsResponse links(List<Link> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("links")
    @Valid
    public List<Link> getLinks() {
        return links;
    }
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public AgentSharedOrganizationsResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public AgentSharedOrganizationsResponse sharingMode(SharingMode sharingMode) {

        this.sharingMode = sharingMode;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("sharingMode")
    @Valid
    public SharingMode getSharingMode() {
        return sharingMode;
    }
    public void setSharingMode(SharingMode sharingMode) {
        this.sharingMode = sharingMode;
    }

    /**
    **/
    public AgentSharedOrganizationsResponse organizations(List<AgentSharedOrganization> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("organizations")
    @Valid
    public List<AgentSharedOrganization> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<AgentSharedOrganization> organizations) {
        this.organizations = organizations;
    }

    public AgentSharedOrganizationsResponse addOrganizationsItem(AgentSharedOrganization organizationsItem) {
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
        AgentSharedOrganizationsResponse agentSharedOrganizationsResponse = (AgentSharedOrganizationsResponse) o;
        return Objects.equals(this.links, agentSharedOrganizationsResponse.links) &&
            Objects.equals(this.sharingMode, agentSharedOrganizationsResponse.sharingMode) &&
            Objects.equals(this.organizations, agentSharedOrganizationsResponse.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, sharingMode, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentSharedOrganizationsResponse {\n");
        
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    sharingMode: ").append(toIndentedString(sharingMode)).append("\n");
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

