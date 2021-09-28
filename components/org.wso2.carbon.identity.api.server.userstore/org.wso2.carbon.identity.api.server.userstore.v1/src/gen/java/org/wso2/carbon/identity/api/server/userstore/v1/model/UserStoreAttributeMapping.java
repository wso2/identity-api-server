/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.userstore.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.userstore.v1.model.UserStoreAttribute;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class UserStoreAttributeMapping  {
  
    private String typeName;
    private String typeId;
    private Boolean isLocal;
    private List<UserStoreAttribute> attributeMappings = null;


    /**
    * Type name of the userstore.
    **/
    public UserStoreAttributeMapping typeName(String typeName) {

        this.typeName = typeName;
        return this;
    }
    
    @ApiModelProperty(example = "org.wso2.carbon.user.core.ldap.UniqueIDActiveDirectoryUserStoreManager", value = "Type name of the userstore.")
    @JsonProperty("typeName")
    @Valid
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
    * Type id of the userstore.
    **/
    public UserStoreAttributeMapping typeId(String typeId) {

        this.typeId = typeId;
        return this;
    }
    
    @ApiModelProperty(example = "b3JnLndzbzIuY2FyYm9uLnVzZXIuY29yZS5qZGJjLkpEQkNVc2VyU3RvcmVNYW5hZ2Vy", value = "Type id of the userstore.")
    @JsonProperty("typeId")
    @Valid
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
    * Whether the userstore is local or not.
    **/
    public UserStoreAttributeMapping isLocal(Boolean isLocal) {

        this.isLocal = isLocal;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether the userstore is local or not.")
    @JsonProperty("isLocal")
    @Valid
    public Boolean getIsLocal() {
        return isLocal;
    }
    public void setIsLocal(Boolean isLocal) {
        this.isLocal = isLocal;
    }

    /**
    * Userstore attribute mappings.
    **/
    public UserStoreAttributeMapping attributeMappings(List<UserStoreAttribute> attributeMappings) {

        this.attributeMappings = attributeMappings;
        return this;
    }
    
    @ApiModelProperty(value = "Userstore attribute mappings.")
    @JsonProperty("attributeMappings")
    @Valid
    public List<UserStoreAttribute> getAttributeMappings() {
        return attributeMappings;
    }
    public void setAttributeMappings(List<UserStoreAttribute> attributeMappings) {
        this.attributeMappings = attributeMappings;
    }

    public UserStoreAttributeMapping addAttributeMappingsItem(UserStoreAttribute attributeMappingsItem) {
        if (this.attributeMappings == null) {
            this.attributeMappings = new ArrayList<>();
        }
        this.attributeMappings.add(attributeMappingsItem);
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
        UserStoreAttributeMapping userStoreAttributeMapping = (UserStoreAttributeMapping) o;
        return Objects.equals(this.typeName, userStoreAttributeMapping.typeName) &&
            Objects.equals(this.typeId, userStoreAttributeMapping.typeId) &&
            Objects.equals(this.isLocal, userStoreAttributeMapping.isLocal) &&
            Objects.equals(this.attributeMappings, userStoreAttributeMapping.attributeMappings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, typeId, isLocal, attributeMappings);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserStoreAttributeMapping {\n");
        
        sb.append("    typeName: ").append(toIndentedString(typeName)).append("\n");
        sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
        sb.append("    isLocal: ").append(toIndentedString(isLocal)).append("\n");
        sb.append("    attributeMappings: ").append(toIndentedString(attributeMappings)).append("\n");
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

