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
import org.wso2.carbon.identity.api.server.idp.v1.model.Endpoint;
import org.wso2.carbon.identity.api.server.idp.v1.model.Property;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class FederatedAuthenticator  {
  
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
    private Boolean isDefault = false;
    private List<String> tags = null;

    private List<Property> properties = null;

    private Endpoint endpoint;

    /**
    **/
    public FederatedAuthenticator authenticatorId(String authenticatorId) {

        this.authenticatorId = authenticatorId;
        return this;
    }
    
    @ApiModelProperty(example = "U0FNTDJBdXRoZW50aWNhdG9y", required = true, value = "")
    @JsonProperty("authenticatorId")
    @Valid
    @NotNull(message = "Property authenticatorId cannot be null.")

    public String getAuthenticatorId() {
        return authenticatorId;
    }
    public void setAuthenticatorId(String authenticatorId) {
        this.authenticatorId = authenticatorId;
    }

    /**
    **/
    public FederatedAuthenticator name(String name) {

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
    public FederatedAuthenticator isEnabled(Boolean isEnabled) {

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
    public FederatedAuthenticator definedBy(DefinedByEnum definedBy) {

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

    public FederatedAuthenticator amrValue(String amrValue) {
        this.amrValue = amrValue;
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

    /**
    **/
    public FederatedAuthenticator isDefault(Boolean isDefault) {

        this.isDefault = isDefault;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("isDefault")
    @Valid
    public Boolean getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
    **/
    public FederatedAuthenticator tags(List<String> tags) {

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

    public FederatedAuthenticator addTagsItem(String tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
        return this;
    }

        /**
    **/
    public FederatedAuthenticator properties(List<Property> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("properties")
    @Valid
    public List<Property> getProperties() {
        return properties;
    }
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public FederatedAuthenticator addPropertiesItem(Property propertiesItem) {
        if (this.properties == null) {
            this.properties = new ArrayList<>();
        }
        this.properties.add(propertiesItem);
        return this;
    }

        /**
    **/
    public FederatedAuthenticator endpoint(Endpoint endpoint) {

        this.endpoint = endpoint;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("endpoint")
    @Valid
    public Endpoint getEndpoint() {
        return endpoint;
    }
    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }


    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FederatedAuthenticator federatedAuthenticator = (FederatedAuthenticator) o;
        return Objects.equals(this.authenticatorId, federatedAuthenticator.authenticatorId) &&
            Objects.equals(this.name, federatedAuthenticator.name) &&
            Objects.equals(this.isEnabled, federatedAuthenticator.isEnabled) &&
            Objects.equals(this.definedBy, federatedAuthenticator.definedBy) &&
            Objects.equals(this.amrValue, federatedAuthenticator.amrValue) &&
            Objects.equals(this.isDefault, federatedAuthenticator.isDefault) &&
            Objects.equals(this.tags, federatedAuthenticator.tags) &&
            Objects.equals(this.properties, federatedAuthenticator.properties) &&
            Objects.equals(this.endpoint, federatedAuthenticator.endpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticatorId, name, isEnabled, definedBy, amrValue, isDefault, tags, properties,
                endpoint);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FederatedAuthenticator {\n");
        
        sb.append("    authenticatorId: ").append(toIndentedString(authenticatorId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    definedBy: ").append(toIndentedString(definedBy)).append("\n");
        sb.append("    amrValue: ").append(toIndentedString(amrValue)).append("\n");
        sb.append("    isDefault: ").append(toIndentedString(isDefault)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
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

