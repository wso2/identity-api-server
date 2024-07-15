/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.action.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ActionTypesResponseItem  {
  

@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("PRE_ISSUE_ACCESS_TOKEN") PRE_ISSUE_ACCESS_TOKEN(String.valueOf("PRE_ISSUE_ACCESS_TOKEN")), @XmlEnumValue("PRE_UPDATE_PASSWORD") PRE_UPDATE_PASSWORD(String.valueOf("PRE_UPDATE_PASSWORD")), @XmlEnumValue("PRE_UPDATE_PROFILE") PRE_UPDATE_PROFILE(String.valueOf("PRE_UPDATE_PROFILE")), @XmlEnumValue("PRE_REGISTRATION") PRE_REGISTRATION(String.valueOf("PRE_REGISTRATION")), @XmlEnumValue("POST_LOGIN") POST_LOGIN(String.valueOf("POST_LOGIN")), @XmlEnumValue("PRE_LOGIN") PRE_LOGIN(String.valueOf("PRE_LOGIN"));


    private String value;

    TypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static TypeEnum fromValue(String value) {
        for (TypeEnum b : TypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private TypeEnum type;
    private String displayName;
    private String description;
    private Integer count;
    private String self;

    /**
    **/
    public ActionTypesResponseItem type(TypeEnum type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("type")
    @Valid
    public TypeEnum getType() {
        return type;
    }
    public void setType(TypeEnum type) {
        this.type = type;
    }

    /**
    **/
    public ActionTypesResponseItem displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Pre Issue Access Token.", value = "")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public ActionTypesResponseItem description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Extension point configuration for Pre Issue Access Token.", value = "")
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
    public ActionTypesResponseItem count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "2", value = "")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    **/
    public ActionTypesResponseItem self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/wso2.com/api/server/v1/actions/preIssueAccessToken", value = "")
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
        ActionTypesResponseItem actionTypesResponseItem = (ActionTypesResponseItem) o;
        return Objects.equals(this.type, actionTypesResponseItem.type) &&
            Objects.equals(this.displayName, actionTypesResponseItem.displayName) &&
            Objects.equals(this.description, actionTypesResponseItem.description) &&
            Objects.equals(this.count, actionTypesResponseItem.count) &&
            Objects.equals(this.self, actionTypesResponseItem.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, displayName, description, count, self);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ActionTypesResponseItem {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
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

