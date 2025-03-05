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

package org.wso2.carbon.identity.api.server.idp.v1.model;

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

public class FederatedAuthenticatorListItem  {
  
    private String authenticatorId;
    private String name;
    private Boolean isEnabled = false;
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
    private List<String> tags = null;

    private String self;

    /**
    **/
    public FederatedAuthenticatorListItem authenticatorId(String authenticatorId) {

        this.authenticatorId = authenticatorId;
        return this;
    }
    
    @ApiModelProperty(example = "U0FNTDJBdXRoZW50aWNhdG9y", value = "")
    @JsonProperty("authenticatorId")
    @Valid
    public String getAuthenticatorId() {
        return authenticatorId;
    }
    public void setAuthenticatorId(String authenticatorId) {
        this.authenticatorId = authenticatorId;
    }

    /**
    **/
    public FederatedAuthenticatorListItem name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "SAML2Authenticator", value = "")
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
    public FederatedAuthenticatorListItem isEnabled(Boolean isEnabled) {

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
    public FederatedAuthenticatorListItem definedBy(DefinedByEnum definedBy) {

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
    public FederatedAuthenticatorListItem tags(List<String> tags) {

        this.tags = tags;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("amrValue")
    @Valid
    public String getAmrValue() {
        return amrValue;
    }
    public void setAmrValue(String amrValue) {
        this.amrValue = amrValue;
    }

    public FederatedAuthenticatorListItem amrValue(String amrValue) {
        this.amrValue = amrValue;
        return this;
    }
    
    @ApiModelProperty(example = "[\"Social Login\",\"OIDC\"]", value = "")
    @JsonProperty("tags")
    @Valid
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public FederatedAuthenticatorListItem addTagsItem(String tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
        return this;
    }

        /**
    **/
    public FederatedAuthenticatorListItem self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-providers/123e4567-e89b-12d3-a456-556642440000/federated-authenticators/U0FNTDJBdXRoZW50aWNhdG9y", value = "")
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
        FederatedAuthenticatorListItem federatedAuthenticatorListItem = (FederatedAuthenticatorListItem) o;
        return Objects.equals(this.authenticatorId, federatedAuthenticatorListItem.authenticatorId) &&
            Objects.equals(this.name, federatedAuthenticatorListItem.name) &&
            Objects.equals(this.isEnabled, federatedAuthenticatorListItem.isEnabled) &&
            Objects.equals(this.definedBy, federatedAuthenticatorListItem.definedBy) &&
                Objects.equals(this.amrValue, federatedAuthenticatorListItem.amrValue) &&
            Objects.equals(this.tags, federatedAuthenticatorListItem.tags) &&
            Objects.equals(this.self, federatedAuthenticatorListItem.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticatorId, name, isEnabled, definedBy, amrValue, tags, self);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FederatedAuthenticatorListItem {\n");
        
        sb.append("    authenticatorId: ").append(toIndentedString(authenticatorId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    definedBy: ").append(toIndentedString(definedBy)).append("\n");
        sb.append("    amrValue: ").append(toIndentedString(amrValue)).append("\n");
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

