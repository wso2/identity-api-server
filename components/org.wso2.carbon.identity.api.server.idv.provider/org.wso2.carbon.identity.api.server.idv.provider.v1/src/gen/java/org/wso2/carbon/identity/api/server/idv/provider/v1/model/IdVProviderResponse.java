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

package org.wso2.carbon.identity.api.server.idv.provider.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.ConfigProperty;
import org.wso2.carbon.identity.api.server.idv.provider.v1.model.VerificationClaim;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class IdVProviderResponse  {
  
    private String id;
    private String type;
    private String name;
    private String description;
    private String image;
    private Boolean isEnabled;
    private List<VerificationClaim> claims = null;

    private List<ConfigProperty> configProperties = null;


    /**
    **/
    public IdVProviderResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "123e4567-e89b-12d3-a456-556642440000", value = "")
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
    public IdVProviderResponse type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "ONFIDO", value = "")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    **/
    public IdVProviderResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "ONFIDO", value = "")
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
    public IdVProviderResponse description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "identity verification provider", value = "")
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
    public IdVProviderResponse image(String image) {

        this.image = image;
        return this;
    }
    
    @ApiModelProperty(example = "onfido-logo-url", value = "")
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
    public IdVProviderResponse isEnabled(Boolean isEnabled) {

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
    public IdVProviderResponse claims(List<VerificationClaim> claims) {

        this.claims = claims;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("claims")
    @Valid
    public List<VerificationClaim> getClaims() {
        return claims;
    }
    public void setClaims(List<VerificationClaim> claims) {
        this.claims = claims;
    }

    public IdVProviderResponse addClaimsItem(VerificationClaim claimsItem) {
        if (this.claims == null) {
            this.claims = new ArrayList<>();
        }
        this.claims.add(claimsItem);
        return this;
    }

        /**
    **/
    public IdVProviderResponse configProperties(List<ConfigProperty> configProperties) {

        this.configProperties = configProperties;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("configProperties")
    @Valid
    public List<ConfigProperty> getConfigProperties() {
        return configProperties;
    }
    public void setConfigProperties(List<ConfigProperty> configProperties) {
        this.configProperties = configProperties;
    }

    public IdVProviderResponse addConfigPropertiesItem(ConfigProperty configPropertiesItem) {
        if (this.configProperties == null) {
            this.configProperties = new ArrayList<>();
        }
        this.configProperties.add(configPropertiesItem);
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
        IdVProviderResponse idVProviderResponse = (IdVProviderResponse) o;
        return Objects.equals(this.id, idVProviderResponse.id) &&
            Objects.equals(this.type, idVProviderResponse.type) &&
            Objects.equals(this.name, idVProviderResponse.name) &&
            Objects.equals(this.description, idVProviderResponse.description) &&
            Objects.equals(this.image, idVProviderResponse.image) &&
            Objects.equals(this.isEnabled, idVProviderResponse.isEnabled) &&
            Objects.equals(this.claims, idVProviderResponse.claims) &&
            Objects.equals(this.configProperties, idVProviderResponse.configProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, description, image, isEnabled, claims, configProperties);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IdVProviderResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
        sb.append("    configProperties: ").append(toIndentedString(configProperties)).append("\n");
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

