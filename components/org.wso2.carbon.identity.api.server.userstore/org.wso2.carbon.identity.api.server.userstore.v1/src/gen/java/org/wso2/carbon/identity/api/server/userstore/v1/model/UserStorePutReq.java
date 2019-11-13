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

package org.wso2.carbon.identity.api.server.userstore.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.userstore.v1.model.Property;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class UserStorePutReq  {
  
    private String typeName;
    private String description;
    private String name;
    private List<Property> properties = null;


    /**
    * The name of the user store manager class type.
    **/
    public UserStorePutReq typeName(String typeName) {

        this.typeName = typeName;
        return this;
    }
    
    @ApiModelProperty(example = "JDBCUserStoreManager", value = "The name of the user store manager class type.")
    @JsonProperty("typeName")
    @Valid
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
    * Description of the user store.
    **/
    public UserStorePutReq description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Some description about the user store.", value = "Description of the user store.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * This is a unique name that identifies the user store.
    **/
    public UserStorePutReq name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "JDBC-SECONDARY", value = "This is a unique name that identifies the user store.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Various properties related to the user store such as connection URL, connection password etc.
    **/
    public UserStorePutReq properties(List<Property> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "Various properties related to the user store such as connection URL, connection password etc.")
    @JsonProperty("properties")
    @Valid
    public List<Property> getProperties() {
        return properties;
    }
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public UserStorePutReq addPropertiesItem(Property propertiesItem) {
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
        UserStorePutReq userStorePutReq = (UserStorePutReq) o;
        return Objects.equals(this.typeName, userStorePutReq.typeName) &&
            Objects.equals(this.description, userStorePutReq.description) &&
            Objects.equals(this.name, userStorePutReq.name) &&
            Objects.equals(this.properties, userStorePutReq.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, description, name, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserStorePutReq {\n");
        
        sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
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

