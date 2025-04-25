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
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class MetaFederatedAuthenticator  {
  
    private String authenticatorId;
    private String name;
    private String displayName;

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
    private String amrValue;
    private List<String> tags = null;

    private List<MetaProperty> properties = null;


    /**
    **/
    public MetaFederatedAuthenticator authenticatorId(String authenticatorId) {

        this.authenticatorId = authenticatorId;
        return this;
    }
    
    @ApiModelProperty(example = "U0FNTFNTT0F1dGhlbnRpY2F0b3I", value = "")
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
    public MetaFederatedAuthenticator name(String name) {

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
    public MetaFederatedAuthenticator displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "SAML2 Web SSO Configuration", value = "")
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
    public MetaFederatedAuthenticator definedBy(DefinedByEnum definedBy) {

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
    public MetaFederatedAuthenticator amrValue(String amrValue) {

        this.amrValue = amrValue;
        return this;
    }

    @ApiModelProperty(example = "basic_amr", value = "")
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
    public MetaFederatedAuthenticator tags(List<String> tags) {

        this.tags = tags;
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

    public MetaFederatedAuthenticator addTagsItem(String tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
        return this;
    }

        /**
    **/
    public MetaFederatedAuthenticator properties(List<MetaProperty> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("properties")
    @Valid
    public List<MetaProperty> getProperties() {
        return properties;
    }
    public void setProperties(List<MetaProperty> properties) {
        this.properties = properties;
    }

    public MetaFederatedAuthenticator addPropertiesItem(MetaProperty propertiesItem) {
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
        MetaFederatedAuthenticator metaFederatedAuthenticator = (MetaFederatedAuthenticator) o;
        return Objects.equals(this.authenticatorId, metaFederatedAuthenticator.authenticatorId) &&
            Objects.equals(this.name, metaFederatedAuthenticator.name) &&
            Objects.equals(this.displayName, metaFederatedAuthenticator.displayName) &&
            Objects.equals(this.definedBy, metaFederatedAuthenticator.definedBy) &&
            Objects.equals(this.tags, metaFederatedAuthenticator.tags) &&
            Objects.equals(this.properties, metaFederatedAuthenticator.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticatorId, name, displayName, definedBy, tags, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MetaFederatedAuthenticator {\n");
        
        sb.append("    authenticatorId: ").append(toIndentedString(authenticatorId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    definedBy: ").append(toIndentedString(definedBy)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

