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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionSharedOrganization;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionSharingMode;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.Link;
import javax.validation.constraints.*;

/**
 * Response listing the organizations that a connection has been shared with.  - &#x60;sharingMode&#x60; at the top level is only present when the connection was shared   via &#x60;ALL_EXISTING_AND_FUTURE_ORGS&#x60; policy **and** &#x60;attributes&#x3D;sharingMode&#x60;   was requested. - &#x60;sharingMode&#x60; on each organization entry is only present when the connection   was shared via &#x60;SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN&#x60; policy   **and** &#x60;attributes&#x3D;sharingMode&#x60; was requested.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Response listing the organizations that a connection has been shared with.  - `sharingMode` at the top level is only present when the connection was shared   via `ALL_EXISTING_AND_FUTURE_ORGS` policy **and** `attributes=sharingMode`   was requested. - `sharingMode` on each organization entry is only present when the connection   was shared via `SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN` policy   **and** `attributes=sharingMode` was requested.")
public class ConnectionSharedOrganizationsResponse  {
  
    private List<Link> links = null;

    private ConnectionSharingMode sharingMode;
    private List<ConnectionSharedOrganization> organizations = null;


    /**
    **/
    public ConnectionSharedOrganizationsResponse links(List<Link> links) {

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

    public ConnectionSharedOrganizationsResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

        /**
    **/
    public ConnectionSharedOrganizationsResponse sharingMode(ConnectionSharingMode sharingMode) {

        this.sharingMode = sharingMode;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("sharingMode")
    @Valid
    public ConnectionSharingMode getSharingMode() {
        return sharingMode;
    }
    public void setSharingMode(ConnectionSharingMode sharingMode) {
        this.sharingMode = sharingMode;
    }

    /**
    **/
    public ConnectionSharedOrganizationsResponse organizations(List<ConnectionSharedOrganization> organizations) {

        this.organizations = organizations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("organizations")
    @Valid
    public List<ConnectionSharedOrganization> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(List<ConnectionSharedOrganization> organizations) {
        this.organizations = organizations;
    }

    public ConnectionSharedOrganizationsResponse addOrganizationsItem(ConnectionSharedOrganization organizationsItem) {
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
        ConnectionSharedOrganizationsResponse connectionSharedOrganizationsResponse = (ConnectionSharedOrganizationsResponse) o;
        return Objects.equals(this.links, connectionSharedOrganizationsResponse.links) &&
            Objects.equals(this.sharingMode, connectionSharedOrganizationsResponse.sharingMode) &&
            Objects.equals(this.organizations, connectionSharedOrganizationsResponse.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, sharingMode, organizations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConnectionSharedOrganizationsResponse {\n");
        
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

