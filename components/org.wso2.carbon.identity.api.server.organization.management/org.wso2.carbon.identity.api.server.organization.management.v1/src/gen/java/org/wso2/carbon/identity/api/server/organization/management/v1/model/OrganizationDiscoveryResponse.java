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

package org.wso2.carbon.identity.api.server.organization.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.DiscoveryAttribute;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class OrganizationDiscoveryResponse  {
  
    private String organizationId;
    private String organizationName;
    private List<DiscoveryAttribute> attributes = new ArrayList<>();


    /**
    * The ID of the organization.
    **/
    public OrganizationDiscoveryResponse organizationId(String organizationId) {

        this.organizationId = organizationId;
        return this;
    }
    
    @ApiModelProperty(example = "06c1f4e2-3339-44e4-a825-96585e3653b1", required = true, value = "The ID of the organization.")
    @JsonProperty("organizationId")
    @Valid
    @NotNull(message = "Property organizationId cannot be null.")

    public String getOrganizationId() {
        return organizationId;
    }
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
    * The name of the organization.
    **/
    public OrganizationDiscoveryResponse organizationName(String organizationName) {

        this.organizationName = organizationName;
        return this;
    }
    
    @ApiModelProperty(example = "ABC Builders", required = true, value = "The name of the organization.")
    @JsonProperty("organizationName")
    @Valid
    @NotNull(message = "Property organizationName cannot be null.")

    public String getOrganizationName() {
        return organizationName;
    }
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
    **/
    public OrganizationDiscoveryResponse attributes(List<DiscoveryAttribute> attributes) {

        this.attributes = attributes;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("attributes")
    @Valid
    @NotNull(message = "Property attributes cannot be null.")

    public List<DiscoveryAttribute> getAttributes() {
        return attributes;
    }
    public void setAttributes(List<DiscoveryAttribute> attributes) {
        this.attributes = attributes;
    }

    public OrganizationDiscoveryResponse addAttributesItem(DiscoveryAttribute attributesItem) {
        this.attributes.add(attributesItem);
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
        OrganizationDiscoveryResponse organizationDiscoveryResponse = (OrganizationDiscoveryResponse) o;
        return Objects.equals(this.organizationId, organizationDiscoveryResponse.organizationId) &&
            Objects.equals(this.organizationName, organizationDiscoveryResponse.organizationName) &&
            Objects.equals(this.attributes, organizationDiscoveryResponse.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationId, organizationName, attributes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OrganizationDiscoveryResponse {\n");
        
        sb.append("    organizationId: ").append(toIndentedString(organizationId)).append("\n");
        sb.append("    organizationName: ").append(toIndentedString(organizationName)).append("\n");
        sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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

