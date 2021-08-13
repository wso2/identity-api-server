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
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class IdentityProviderResponse  {
  
    private String id;
    private String name;
    private String description;
    private String templateId;
    private Boolean isEnabled = true;
    private Boolean isPrimary = false;
    private String image;
    private Boolean isFederationHub;
    private String homeRealmIdentifier;
    private Certificate certificate;
    private String alias;
    private Claims claims;
    private Roles roles;
    private FederatedAuthenticatorListResponse federatedAuthenticators;
    private ProvisioningResponse provisioning;

    /**
    **/
    public IdentityProviderResponse id(String id) {

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
    public IdentityProviderResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "google", value = "")
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
    public IdentityProviderResponse description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(value = "")
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
    public IdentityProviderResponse templateId(String templateId) {

        this.templateId = templateId;
        return this;
    }
    
    @ApiModelProperty(example = "8ea23303-49c0-4253-b81f-82c0fe6fb4a0", value = "")
    @JsonProperty("templateId")
    @Valid
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
    **/
    public IdentityProviderResponse isEnabled(Boolean isEnabled) {

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
    public IdentityProviderResponse isPrimary(Boolean isPrimary) {

        this.isPrimary = isPrimary;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("isPrimary")
    @Valid
    public Boolean getIsPrimary() {
        return isPrimary;
    }
    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    /**
    **/
    public IdentityProviderResponse image(String image) {

        this.image = image;
        return this;
    }
    
    @ApiModelProperty(example = "google-logo-url", value = "")
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
    public IdentityProviderResponse isFederationHub(Boolean isFederationHub) {

        this.isFederationHub = isFederationHub;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
    @JsonProperty("isFederationHub")
    @Valid
    public Boolean getIsFederationHub() {
        return isFederationHub;
    }
    public void setIsFederationHub(Boolean isFederationHub) {
        this.isFederationHub = isFederationHub;
    }

    /**
    **/
    public IdentityProviderResponse homeRealmIdentifier(String homeRealmIdentifier) {

        this.homeRealmIdentifier = homeRealmIdentifier;
        return this;
    }
    
    @ApiModelProperty(example = "localhost", value = "")
    @JsonProperty("homeRealmIdentifier")
    @Valid
    public String getHomeRealmIdentifier() {
        return homeRealmIdentifier;
    }
    public void setHomeRealmIdentifier(String homeRealmIdentifier) {
        this.homeRealmIdentifier = homeRealmIdentifier;
    }

    /**
    **/
    public IdentityProviderResponse certificate(Certificate certificate) {

        this.certificate = certificate;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("certificate")
    @Valid
    public Certificate getCertificate() {
        return certificate;
    }
    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    /**
    **/
    public IdentityProviderResponse alias(String alias) {

        this.alias = alias;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9444/oauth2/token", value = "")
    @JsonProperty("alias")
    @Valid
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
    **/
    public IdentityProviderResponse claims(Claims claims) {

        this.claims = claims;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("claims")
    @Valid
    public Claims getClaims() {
        return claims;
    }
    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    /**
    **/
    public IdentityProviderResponse roles(Roles roles) {

        this.roles = roles;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("roles")
    @Valid
    public Roles getRoles() {
        return roles;
    }
    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    /**
    **/
    public IdentityProviderResponse federatedAuthenticators(FederatedAuthenticatorListResponse federatedAuthenticators) {

        this.federatedAuthenticators = federatedAuthenticators;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("federatedAuthenticators")
    @Valid
    public FederatedAuthenticatorListResponse getFederatedAuthenticators() {
        return federatedAuthenticators;
    }
    public void setFederatedAuthenticators(FederatedAuthenticatorListResponse federatedAuthenticators) {
        this.federatedAuthenticators = federatedAuthenticators;
    }

    /**
    **/
    public IdentityProviderResponse provisioning(ProvisioningResponse provisioning) {

        this.provisioning = provisioning;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("provisioning")
    @Valid
    public ProvisioningResponse getProvisioning() {
        return provisioning;
    }
    public void setProvisioning(ProvisioningResponse provisioning) {
        this.provisioning = provisioning;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityProviderResponse identityProviderResponse = (IdentityProviderResponse) o;
        return Objects.equals(this.id, identityProviderResponse.id) &&
            Objects.equals(this.name, identityProviderResponse.name) &&
            Objects.equals(this.description, identityProviderResponse.description) &&
            Objects.equals(this.templateId, identityProviderResponse.templateId) &&
            Objects.equals(this.isEnabled, identityProviderResponse.isEnabled) &&
            Objects.equals(this.isPrimary, identityProviderResponse.isPrimary) &&
            Objects.equals(this.image, identityProviderResponse.image) &&
            Objects.equals(this.isFederationHub, identityProviderResponse.isFederationHub) &&
            Objects.equals(this.homeRealmIdentifier, identityProviderResponse.homeRealmIdentifier) &&
            Objects.equals(this.certificate, identityProviderResponse.certificate) &&
            Objects.equals(this.alias, identityProviderResponse.alias) &&
            Objects.equals(this.claims, identityProviderResponse.claims) &&
            Objects.equals(this.roles, identityProviderResponse.roles) &&
            Objects.equals(this.federatedAuthenticators, identityProviderResponse.federatedAuthenticators) &&
            Objects.equals(this.provisioning, identityProviderResponse.provisioning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, templateId, isEnabled, isPrimary, image, isFederationHub, homeRealmIdentifier, certificate, alias, claims, roles, federatedAuthenticators, provisioning);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityProviderResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    isPrimary: ").append(toIndentedString(isPrimary)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    isFederationHub: ").append(toIndentedString(isFederationHub)).append("\n");
        sb.append("    homeRealmIdentifier: ").append(toIndentedString(homeRealmIdentifier)).append("\n");
        sb.append("    certificate: ").append(toIndentedString(certificate)).append("\n");
        sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
        sb.append("    federatedAuthenticators: ").append(toIndentedString(federatedAuthenticators)).append("\n");
        sb.append("    provisioning: ").append(toIndentedString(provisioning)).append("\n");
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

