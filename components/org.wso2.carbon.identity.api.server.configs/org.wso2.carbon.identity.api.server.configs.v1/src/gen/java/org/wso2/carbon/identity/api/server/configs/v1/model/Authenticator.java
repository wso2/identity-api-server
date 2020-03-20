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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticatorProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Authenticator  {
  
    private String id;
    private String name;
    private String displayName;
    private Boolean isEnabled = true;
    private List<AuthenticatorProperty> properties = null;


    /**
    **/
    public Authenticator id(String id) {

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
    public Authenticator name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "BasicAuthenticator", required = true, value = "")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public Authenticator displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "basic", required = true, value = "")
    @JsonProperty("displayName")
    @Valid
    @NotNull(message = "Property displayName cannot be null.")

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public Authenticator isEnabled(Boolean isEnabled) {

        this.isEnabled = isEnabled;
        return this;
    }
    
    @ApiModelProperty(value = "")
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
    public Authenticator properties(List<AuthenticatorProperty> properties) {

        this.properties = properties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("properties")
    @Valid
    public List<AuthenticatorProperty> getProperties() {
        return properties;
    }
    public void setProperties(List<AuthenticatorProperty> properties) {
        this.properties = properties;
    }

    public Authenticator addPropertiesItem(AuthenticatorProperty propertiesItem) {
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
        Authenticator authenticator = (Authenticator) o;
        return Objects.equals(this.id, authenticator.id) &&
            Objects.equals(this.name, authenticator.name) &&
            Objects.equals(this.displayName, authenticator.displayName) &&
            Objects.equals(this.isEnabled, authenticator.isEnabled) &&
            Objects.equals(this.properties, authenticator.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, displayName, isEnabled, properties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Authenticator {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
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

