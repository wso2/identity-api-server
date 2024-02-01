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

package org.wso2.carbon.identity.api.server.api.resource.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import java.util.Objects;
import javax.validation.Valid;

public class APIResourceCollectionItem  {
  
    private String id;
    private String name;
    private String description;
    private String identifier;
    private String type;
    private List<ScopeGetModel> scopes = null;

    private String self;

    /**
    **/
    public APIResourceCollectionItem id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "gh43-jk34-vb34-df67", required = true, value = "")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public APIResourceCollectionItem name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Greetings API", required = true, value = "")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public APIResourceCollectionItem description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Greeting API representation", value = "")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    **/
    public APIResourceCollectionItem identifier(String identifier) {

        this.identifier = identifier;
        return this;
    }
    
    @ApiModelProperty(example = "greetings_api", value = "")
    @JsonProperty("identifier")
    @Valid
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
    **/
    public APIResourceCollectionItem type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "SYSTEM", value = "")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    **/
    public APIResourceCollectionItem scopes(List<ScopeGetModel> scopes) {

        this.scopes = scopes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("scopes")
    @Valid
    public List<ScopeGetModel> getScopes() {
        return scopes;
    }
    public void setScopes(List<ScopeGetModel> scopes) {
        this.scopes = scopes;
    }

    public APIResourceCollectionItem addScopesItem(ScopeGetModel scopesItem) {
        if (this.scopes == null) {
            this.scopes = new ArrayList<ScopeGetModel>();
        }
        this.scopes.add(scopesItem);
        return this;
    }

        /**
    **/
    public APIResourceCollectionItem self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/carbon.super/api/server/v1/api-resources/eDUwOUNlcnRpZmljYXRlQXV0aGVudGljYXRvcg", required = true, value = "")
    @JsonProperty("self")
    @Valid
    @NotNull(message = "Property self cannot be null.")

    public String getSelf() {
        return self;
    }
    public void setSelf(String self) {
        this.self = self;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        APIResourceCollectionItem apIResourceCollectionItem = (APIResourceCollectionItem) o;
        return Objects.equals(this.id, apIResourceCollectionItem.id) &&
            Objects.equals(this.name, apIResourceCollectionItem.name) &&
            Objects.equals(this.description, apIResourceCollectionItem.description) &&
            Objects.equals(this.identifier, apIResourceCollectionItem.identifier) &&
            Objects.equals(this.type, apIResourceCollectionItem.type) &&
            Objects.equals(this.scopes, apIResourceCollectionItem.scopes) &&
            Objects.equals(this.self, apIResourceCollectionItem.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, identifier, type, scopes, self);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class APIResourceCollectionItem {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    scopes: ").append(toIndentedString(scopes)).append("\n");
        sb.append("    self: ").append(toIndentedString(self)).append("\n");
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

