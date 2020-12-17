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
import org.wso2.carbon.identity.api.server.userstore.v1.model.Property;
import javax.validation.constraints.*;

/**
 * Secondary user store request.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Secondary user store request.")
public class UserStoreReq  {
  
    private String typeId;
    private String description;
    private String name;
    private List<Property> properties = new ArrayList<>();


    /**
    * The id of the user store manager class type.
    **/
    public UserStoreReq typeId(String typeId) {

        this.typeId = typeId;
        return this;
    }
    
    @ApiModelProperty(example = "VW5pcXVlSURKREJDVXNlclN0b3JlTWFuYWdlcg", required = true, value = "The id of the user store manager class type.")
    @JsonProperty("typeId")
    @Valid
    @NotNull(message = "Property typeId cannot be null.")

    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
    * Description of the user store.
    **/
    public UserStoreReq description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Some description about the user store.", required = true, value = "Description of the user store.")
    @JsonProperty("description")
    @Valid
    @NotNull(message = "Property description cannot be null.")

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * This is a unique name that identifies the user store.
    **/
    public UserStoreReq name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "JDBC-SECONDARY", required = true, value = "This is a unique name that identifies the user store.")
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
    * Various properties related to the user store such as connection URL, connection password etc.
    **/
    public UserStoreReq properties(List<Property> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Various properties related to the user store such as connection URL, connection password etc.")
    @JsonProperty("properties")
    @Valid
    @NotNull(message = "Property properties cannot be null.")

    public List<Property> getProperties() {
        return properties;
    }
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public UserStoreReq addPropertiesItem(Property propertiesItem) {
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
        UserStoreReq userStoreReq = (UserStoreReq) o;
        return Objects.equals(this.typeId, userStoreReq.typeId) &&
            Objects.equals(this.description, userStoreReq.description) &&
            Objects.equals(this.name, userStoreReq.name) &&
            Objects.equals(this.properties, userStoreReq.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, description, name, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserStoreReq {\n");
        
        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

