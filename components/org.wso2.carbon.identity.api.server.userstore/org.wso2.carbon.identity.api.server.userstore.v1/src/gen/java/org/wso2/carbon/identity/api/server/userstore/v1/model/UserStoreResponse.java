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

public class UserStoreResponse  {
  
    private String id;
    private String name;
    private String typeName;
    private String typeId;
    private String description;
    private List<AddUserStorePropertiesRes> properties = null;


    /**
    * base64 url encoded value of domain name
    **/
    public UserStoreResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "SkRCQy1TRUNPTkRBUlk=", value = "base64 url encoded value of domain name")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * domain name of the secondary user store
    **/
    public UserStoreResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "JDBC-SECONDARY", value = "domain name of the secondary user store")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public UserStoreResponse typeName(String typeName) {

        this.typeName = typeName;
        return this;
    }
    
    @ApiModelProperty(example = "UniqueIDJDBCUserStoreManager", value = "")
    @JsonProperty("typeName")
    @Valid
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
    **/
    public UserStoreResponse typeId(String typeId) {

        this.typeId = typeId;
        return this;
    }
    
    @ApiModelProperty(example = "VW5pcXVlSURKREJDVXNlclN0b3JlTWFuYWdlcg", value = "")
    @JsonProperty("typeId")
    @Valid
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
    **/
    public UserStoreResponse description(String description) {

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
    * Configured user store proper for the set.
    **/
    public UserStoreResponse properties(List<AddUserStorePropertiesRes> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "Configured user store proper for the set.")
    @JsonProperty("properties")
    @Valid
    public List<AddUserStorePropertiesRes> getProperties() {
        return properties;
    }
    public void setProperties(List<AddUserStorePropertiesRes> properties) {
        this.properties = properties;
    }

    public UserStoreResponse addPropertiesItem(AddUserStorePropertiesRes propertiesItem) {
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
        UserStoreResponse userStoreResponse = (UserStoreResponse) o;
        return Objects.equals(this.id, userStoreResponse.id) &&
            Objects.equals(this.name, userStoreResponse.name) &&
            Objects.equals(this.typeName, userStoreResponse.typeName) &&
            Objects.equals(this.typeId, userStoreResponse.typeId) &&
            Objects.equals(this.description, userStoreResponse.description) &&
            Objects.equals(this.properties, userStoreResponse.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, typeName, typeId, description, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserStoreResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

