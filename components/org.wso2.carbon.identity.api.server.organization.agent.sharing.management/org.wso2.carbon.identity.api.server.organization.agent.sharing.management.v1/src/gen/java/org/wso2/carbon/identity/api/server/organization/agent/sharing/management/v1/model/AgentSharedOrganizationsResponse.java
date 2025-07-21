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

package org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganizationsResponseLinks;
import org.wso2.carbon.identity.api.server.organization.agent.sharing.management.v1.model.AgentSharedOrganizationsResponseSharedOrganizations;
import javax.validation.constraints.*;

/**
 * Response listing organizations where a agent has shared access, including sharing policies, shared type and pagination links for navigating results. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Response listing organizations where a agent has shared access, including sharing policies, shared type and pagination links for navigating results. ")
public class AgentSharedOrganizationsResponse  {
  
    private List<AgentSharedOrganizationsResponseLinks> links = null;

    private List<AgentSharedOrganizationsResponseSharedOrganizations> sharedOrganizations = null;


    /**
    * Pagination links for navigating the result set.
    **/
    public AgentSharedOrganizationsResponse links(List<AgentSharedOrganizationsResponseLinks> links) {

        this.links = links;
        return this;
    }
    
    @ApiModelProperty(value = "Pagination links for navigating the result set.")
    @JsonProperty("links")
    @Valid
    public List<AgentSharedOrganizationsResponseLinks> getLinks() {
        return links;
    }
    public void setLinks(List<AgentSharedOrganizationsResponseLinks> links) {
        this.links = links;
    }

    public AgentSharedOrganizationsResponse addLinksItem(AgentSharedOrganizationsResponseLinks linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    * A list of shared access details for the agent across multiple organizations
    **/
    public AgentSharedOrganizationsResponse sharedOrganizations(List<AgentSharedOrganizationsResponseSharedOrganizations> sharedOrganizations) {

        this.sharedOrganizations = sharedOrganizations;
        return this;
    }
    
    @ApiModelProperty(value = "A list of shared access details for the agent across multiple organizations")
    @JsonProperty("sharedOrganizations")
    @Valid
    public List<AgentSharedOrganizationsResponseSharedOrganizations> getSharedOrganizations() {
        return sharedOrganizations;
    }
    public void setSharedOrganizations(List<AgentSharedOrganizationsResponseSharedOrganizations> sharedOrganizations) {
        this.sharedOrganizations = sharedOrganizations;
    }

    public AgentSharedOrganizationsResponse addSharedOrganizationsItem(AgentSharedOrganizationsResponseSharedOrganizations sharedOrganizationsItem) {
        if (this.sharedOrganizations == null) {
            this.sharedOrganizations = new ArrayList<>();
        }
        this.sharedOrganizations.add(sharedOrganizationsItem);
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
            Objects.equals(this.sharedOrganizations, agentSharedOrganizationsResponse.sharedOrganizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, sharedOrganizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AgentSharedOrganizationsResponse {\n");
        
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    sharedOrganizations: ").append(toIndentedString(sharedOrganizations)).append("\n");
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

