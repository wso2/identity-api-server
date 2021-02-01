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

package org.wso2.carbon.identity.api.server.userstore.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.userstore.v1.model.AddUserStorePropertiesRes;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class UserStoreListResponse  {
  
    private String id;
    private String name;
    private Boolean enabled;
    private String description;
    private String self;
    private List<AddUserStorePropertiesRes> properties = null;


    /**
    * base64 url encoded value of domain name
    **/
    public UserStoreListResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "SkRCQy1TRUNPTkRBUlk", value = "base64 url encoded value of domain name")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Domain name of the secondary user store.
    **/
    public UserStoreListResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "JDBC-SECONDARY", value = "Domain name of the secondary user store.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Enabled status of the userstore.
    **/
    public UserStoreListResponse enabled(Boolean enabled) {

        this.enabled = enabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Enabled status of the userstore.")
    @JsonProperty("enabled")
    @Valid
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
    **/
    public UserStoreListResponse description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Some description of the user store", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Location of the created/updated resource.
    **/
    public UserStoreListResponse self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/{tenant-domain}/api/server/v1/userstores/SkRCQy1TRUNPTkRBUlk", value = "Location of the created/updated resource.")
    @JsonProperty("self")
    @Valid
    public String getSelf() {
        return self;
    }
    public void setSelf(String self) {
        this.self = self;
    }

    /**
    * Requested configured user store property for the set
    **/
    public UserStoreListResponse properties(List<AddUserStorePropertiesRes> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "Requested configured user store property for the set")
    @JsonProperty("properties")
    @Valid
    public List<AddUserStorePropertiesRes> getProperties() {
        return properties;
    }
    public void setProperties(List<AddUserStorePropertiesRes> properties) {
        this.properties = properties;
    }

    public UserStoreListResponse addPropertiesItem(AddUserStorePropertiesRes propertiesItem) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(propertiesItem);
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
        UserStoreListResponse userStoreListResponse = (UserStoreListResponse) o;
        return Objects.equals(this.id, userStoreListResponse.id) &&
            Objects.equals(this.name, userStoreListResponse.name) &&
            Objects.equals(this.enabled, userStoreListResponse.enabled) &&
            Objects.equals(this.description, userStoreListResponse.description) &&
            Objects.equals(this.self, userStoreListResponse.self) &&
            Objects.equals(this.properties, userStoreListResponse.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, enabled, description, self, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserStoreListResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    self: ").append(toIndentedString(self)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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

