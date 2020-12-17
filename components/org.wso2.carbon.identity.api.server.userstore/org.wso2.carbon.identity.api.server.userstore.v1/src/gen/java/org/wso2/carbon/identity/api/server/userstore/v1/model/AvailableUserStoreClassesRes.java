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
import javax.validation.constraints.*;

/**
 * Available User Store Classes Response.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Available User Store Classes Response.")
public class AvailableUserStoreClassesRes  {
  
    private String typeId;
    private String typeName;
    private String className;
    private String self;

    /**
    **/
    public AvailableUserStoreClassesRes typeId(String typeId) {

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
    public AvailableUserStoreClassesRes typeName(String typeName) {

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
    public AvailableUserStoreClassesRes className(String className) {

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
    public AvailableUserStoreClassesRes self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/{tenant-domain}/api/server/v1/userstores/meta/types/VW5pcXVlSURKREJDVXNlclN0b3JlTWFuYWdlcg", value = "")
    @JsonProperty("self")
    @Valid
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
        AvailableUserStoreClassesRes availableUserStoreClassesRes = (AvailableUserStoreClassesRes) o;
        return Objects.equals(this.typeId, availableUserStoreClassesRes.typeId) &&
            Objects.equals(this.typeName, availableUserStoreClassesRes.typeName) &&
            Objects.equals(this.className, availableUserStoreClassesRes.className) &&
            Objects.equals(this.self, availableUserStoreClassesRes.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, typeName, className, self);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AvailableUserStoreClassesRes {\n");
        
        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
        sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
        sb.append("    className: ").append(toIndentedString(className)).append("\n");
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

