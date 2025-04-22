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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthenticatorListItem  {
  
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String image;
    private Boolean isEnabled = true;
    private String amrValue;

@XmlType(name="DefinedByEnum")
@XmlEnum(String.class)
public enum DefinedByEnum {

    @XmlEnumValue("SYSTEM") SYSTEM(String.valueOf("SYSTEM")), @XmlEnumValue("USER") USER(String.valueOf("USER"));


    private String value;

    DefinedByEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static DefinedByEnum fromValue(String value) {
        for (DefinedByEnum b : DefinedByEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private DefinedByEnum definedBy;

@XmlType(name="TypeEnum")
@XmlEnum(String.class)
public enum TypeEnum {

    @XmlEnumValue("LOCAL") LOCAL(String.valueOf("LOCAL")), @XmlEnumValue("REQUEST_PATH") REQUEST_PATH(String.valueOf("REQUEST_PATH"));


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
    private List<String> tags = null;

    private String self;

    /**
    **/
    public AuthenticatorListItem id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "QmFzaWNBdXRoZW50aWNhdG9y", value = "")
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
    public AuthenticatorListItem name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "BasicAuthenticator", value = "")
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
    public AuthenticatorListItem displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "basic", value = "")
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
    public AuthenticatorListItem description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Description for user defined local authenticator configuration.", value = "")
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
    public AuthenticatorListItem image(String image) {

        this.image = image;
        return this;
    }
    
    @ApiModelProperty(example = "https://example.com/logo/my-logo.png", value = "")
    @JsonProperty("image")
    @Valid
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    /**
    **/
    public AuthenticatorListItem isEnabled(Boolean isEnabled) {

        this.isEnabled = isEnabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("isEnabled")
    @Valid
    public Boolean getIsEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
    **/
    public AuthenticatorListItem definedBy(DefinedByEnum definedBy) {

        this.definedBy = definedBy;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("definedBy")
    @Valid
    public DefinedByEnum getDefinedBy() {
        return definedBy;
    }
    public void setDefinedBy(DefinedByEnum definedBy) {
        this.definedBy = definedBy;
    }

    /**
    **/
    public AuthenticatorListItem type(TypeEnum type) {

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
    public AuthenticatorListItem amrValue(String amrValue) {

        this.amrValue = amrValue;
        return this;
    }

    @ApiModelProperty(example = "amrValue", value = "")
    @JsonProperty("amrValue")
    @Valid
    public String getAmrValue() {
        return amrValue;
    }
    public void setAmrValue(String amrValue) {
        this.amrValue = amrValue;
    }

    /**
     **/
    public AuthenticatorListItem tags(List<String> tags) {

        this.tags = tags;
        return this;
    }
    
    @ApiModelProperty(example = "[\"2FA\",\"MFA\"]", value = "")
    @JsonProperty("tags")
    @Valid
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public AuthenticatorListItem addTagsItem(String tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
        return this;
    }

        /**
    **/
    public AuthenticatorListItem self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/carbon.super/api/server/v1/configs/authenticators/QmFzaWNBdXRoZW50aWNhdG9y", value = "")
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
        AuthenticatorListItem authenticatorListItem = (AuthenticatorListItem) o;
        return Objects.equals(this.id, authenticatorListItem.id) &&
            Objects.equals(this.name, authenticatorListItem.name) &&
            Objects.equals(this.displayName, authenticatorListItem.displayName) &&
            Objects.equals(this.description, authenticatorListItem.description) &&
            Objects.equals(this.image, authenticatorListItem.image) &&
            Objects.equals(this.isEnabled, authenticatorListItem.isEnabled) &&
            Objects.equals(this.definedBy, authenticatorListItem.definedBy) &&
            Objects.equals(this.amrValue, authenticatorListItem.amrValue) &&
            Objects.equals(this.type, authenticatorListItem.type) &&
            Objects.equals(this.tags, authenticatorListItem.tags) &&
            Objects.equals(this.self, authenticatorListItem.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, displayName, description, image, isEnabled, definedBy,amrValue, type, tags, self);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticatorListItem {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    definedBy: ").append(toIndentedString(definedBy)).append("\n");
        sb.append("    amrValue: ").append(toIndentedString(amrValue)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

