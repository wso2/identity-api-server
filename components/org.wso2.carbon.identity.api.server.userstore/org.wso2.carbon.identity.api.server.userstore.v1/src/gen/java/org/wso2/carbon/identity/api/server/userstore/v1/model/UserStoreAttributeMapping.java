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
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.user.store.configuration.model.UserStoreAttributeDO;

import java.util.List;
import javax.validation.Valid;

public class UserStoreAttributeMapping {

    private String typeId;
    private String typeName;
    private boolean isLocal;
    private List<UserStoreAttributeDO> attributeMappings;

    /**
     * Set user store attribute type and get the object.
     *
     * @param typeId String user store type id.
     * @return UserStoreAttributeMapping .
     */
    public UserStoreAttributeMapping typeId(String typeId) {

        this.typeId = typeId;
        return this;
    }

    @ApiModelProperty(example = "b3JnLndzbzIuY2FyYm9uLnVzZXIuY29yZS5qZGJjLkpEQkNVc2VyU3RvcmVNYW5hZ2Vy",
            value = "User store type")
    @JsonProperty("typeId")
    @Valid
    public String getTypeId() {

        return typeId;
    }

    public void setTypeId(String typeId) {

        this.typeId = typeId;
    }

    /**
     * Set user store attribute type and get the object.
     *
     * @param typeName String user store type.
     * @return UserStoreAttributeMapping .
     */
    public UserStoreAttributeMapping typeName(String typeName) {

        this.typeName = typeName;
        return this;
    }

    @ApiModelProperty(example = "org.wso2.carbon.user.core.ldap.UniqueIDActiveDirectoryUserStoreManager",
            value = "User store type")
    @JsonProperty("typeName")
    @Valid
    public String getTypeName() {

        return typeName;
    }

    public void setTypeName(String typeName) {

        this.typeName = typeName;
    }

    /**
     * Set local user store is local or not and get the object.
     *
     * @param isLocal Boolean local user store or not.
     * @return UserStoreAttributeMapping .
     */
    public UserStoreAttributeMapping isLocal(boolean isLocal) {

        this.isLocal = isLocal;
        return this;
    }

    @ApiModelProperty(example = "true",
            value = "Is local user store or not")
    @JsonProperty("isLocal")
    @Valid
    public boolean getIsLocal() {

        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {

        this.isLocal = isLocal;
    }

    /**
     * Set attribute mappings and get the object.
     *
     * @param attributeMappings List of attribute mappings.
     * @return UserStoreAttributeMapping.
     */
    public UserStoreAttributeMapping attributeMapping(List<UserStoreAttributeDO> attributeMappings) {

        this.attributeMappings = attributeMappings;
        return this;
    }

    @ApiModelProperty(value = "Attribute mappings")
    @JsonProperty("AttributeMapping")
    @Valid
    public List<UserStoreAttributeDO> getAttributeMappings() {

        return attributeMappings;
    }
}
