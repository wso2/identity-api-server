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
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStorePropertiesRes;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class MetaUserStoreType  {
  
    private String typeName;
    private String typeId;
    private String className;
    private UserStorePropertiesRes properties;

    /**
    **/
    public MetaUserStoreType typeName(String typeName) {

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
    public MetaUserStoreType typeId(String typeId) {

        this.typeId = typeId;
        return this;
    }
    
    @ApiModelProperty(example = "b3JnLndzbzIuY2FyYm9uLnVzZXIuY29yZS5qZGJjLkpEQkNVc2VyU3RvcmVNYW5hZ2Vy", value = "")
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
    public MetaUserStoreType className(String className) {

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
    **/
    public MetaUserStoreType properties(UserStorePropertiesRes properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("properties")
    @Valid
    public UserStorePropertiesRes getProperties() {
        return properties;
    }
    public void setProperties(UserStorePropertiesRes properties) {
        this.properties = properties;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MetaUserStoreType metaUserStoreType = (MetaUserStoreType) o;
        return Objects.equals(this.typeName, metaUserStoreType.typeName) &&
            Objects.equals(this.typeId, metaUserStoreType.typeId) &&
            Objects.equals(this.className, metaUserStoreType.className) &&
            Objects.equals(this.properties, metaUserStoreType.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, typeId, className, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MetaUserStoreType {\n");
        
        sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
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

