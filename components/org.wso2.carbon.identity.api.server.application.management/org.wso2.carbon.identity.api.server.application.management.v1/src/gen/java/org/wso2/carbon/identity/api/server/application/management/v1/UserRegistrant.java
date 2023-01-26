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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthAttribute;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class UserRegistrant  {
  
    private String id;
    private String name;
    private List<AuthAttribute> authAttributes = null;


    /**
    **/
    public UserRegistrant id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "8we2f4b8-0d22-8234-l4n7-1682a91b12cn", value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public UserRegistrant name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Username Password", value = "")
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
    public UserRegistrant authAttributes(List<AuthAttribute> authAttributes) {

        this.authAttributes = authAttributes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("authAttributes")
    @Valid
    public List<AuthAttribute> getAuthAttributes() {
        return authAttributes;
    }
    public void setAuthAttributes(List<AuthAttribute> authAttributes) {
        this.authAttributes = authAttributes;
    }

    public UserRegistrant addAuthAttributesItem(AuthAttribute authAttributesItem) {
        if (this.authAttributes == null) {
            this.authAttributes = new ArrayList<>();
        }
        this.authAttributes.add(authAttributesItem);
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
        UserRegistrant userRegistrant = (UserRegistrant) o;
        return Objects.equals(this.id, userRegistrant.id) &&
            Objects.equals(this.name, userRegistrant.name) &&
            Objects.equals(this.authAttributes, userRegistrant.authAttributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authAttributes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserRegistrant {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    authAttributes: ").append(toIndentedString(authAttributes)).append("\n");
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

