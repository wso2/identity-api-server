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

/**
 * Available User Store Configurations Response.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Available User Store Configurations Response.")
public class UserStoreConfigurationsRes  {
  
    private String typeName;
    private String typeId;
    private String name;
    private String description;
    private String className;
    private List<AddUserStorePropertiesRes> properties = null;


    /**
    **/
    public UserStoreConfigurationsRes typeName(String typeName) {

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
    public UserStoreConfigurationsRes typeId(String typeId) {

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
    public UserStoreConfigurationsRes name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "JDBC-SECONDARY", value = "")
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
    public UserStoreConfigurationsRes description(String description) {

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
    **/
    public UserStoreConfigurationsRes className(String className) {

        this.className = className;
        return this;
    }
    
    @ApiModelProperty(example = "org.wso2.carbon.user.core.jdbc.UniqueIDJDBCUserStoreManager", value = "")
    @JsonProperty("className")
    @Valid
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    /**
    * Configured user store property for the set
    **/
    public UserStoreConfigurationsRes properties(List<AddUserStorePropertiesRes> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "Configured user store property for the set")
    @JsonProperty("properties")
    @Valid
    public List<AddUserStorePropertiesRes> getProperties() {
        return properties;
    }
    public void setProperties(List<AddUserStorePropertiesRes> properties) {
        this.properties = properties;
    }

    public UserStoreConfigurationsRes addPropertiesItem(AddUserStorePropertiesRes propertiesItem) {
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
        UserStoreConfigurationsRes userStoreConfigurationsRes = (UserStoreConfigurationsRes) o;
        return Objects.equals(this.typeName, userStoreConfigurationsRes.typeName) &&
            Objects.equals(this.typeId, userStoreConfigurationsRes.typeId) &&
            Objects.equals(this.name, userStoreConfigurationsRes.name) &&
            Objects.equals(this.description, userStoreConfigurationsRes.description) &&
            Objects.equals(this.className, userStoreConfigurationsRes.className) &&
            Objects.equals(this.properties, userStoreConfigurationsRes.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, typeId, name, description, className, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserStoreConfigurationsRes {\n");
        
        sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    className: ").append(toIndentedString(className)).append("\n");
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

