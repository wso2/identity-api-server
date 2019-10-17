/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.identity.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoryConnectorsRes;
import javax.validation.constraints.*;

/**
 * Governance connector category response.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Governance connector category response.")
public class CategoriesRes  {
  
    private String id;
    private String name;
    private String self;
    private List<CategoryConnectorsRes> connectors = null;


    /**
    * Connector category id.
    **/
    public CategoriesRes id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "QWNjb3VudCBNYW5hZ2VtZW50IFBvbGljaWVz", value = "Connector category id.")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Connector category name.
    **/
    public CategoriesRes name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Account Management Policies", value = "Connector category name.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Path to retrieve the full connector information.
    **/
    public CategoriesRes self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-governance/QWNjb3VudCBNYW5hZ2VtZW50IFBvbGljaWVz", value = "Path to retrieve the full connector information.")
    @JsonProperty("self")
    @Valid
    public String getSelf() {
        return self;
    }
    public void setSelf(String self) {
        this.self = self;
    }

    /**
    * Connectors of the category with minimal attributes.
    **/
    public CategoriesRes connectors(List<CategoryConnectorsRes> connectors) {

        this.connectors = connectors;
        return this;
    }
    
    @ApiModelProperty(value = "Connectors of the category with minimal attributes.")
    @JsonProperty("connectors")
    @Valid
    public List<CategoryConnectorsRes> getConnectors() {
        return connectors;
    }
    public void setConnectors(List<CategoryConnectorsRes> connectors) {
        this.connectors = connectors;
    }

    public CategoriesRes addConnectorsItem(CategoryConnectorsRes connectorsItem) {
        if (this.connectors == null) {
            this.connectors = new ArrayList<>();
        }
        this.connectors.add(connectorsItem);
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
        CategoriesRes categoriesRes = (CategoriesRes) o;
        return Objects.equals(this.id, categoriesRes.id) &&
            Objects.equals(this.name, categoriesRes.name) &&
            Objects.equals(this.self, categoriesRes.self) &&
            Objects.equals(this.connectors, categoriesRes.connectors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, self, connectors);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CategoriesRes {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    self: ").append(toIndentedString(self)).append("\n");
        sb.append("    connectors: ").append(toIndentedString(connectors)).append("\n");
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

