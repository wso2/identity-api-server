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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdPGroup;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class IdentityProviderListItem  {
  
    private String id;
    private String name;
    private String description;
    private Boolean isEnabled = true;
    private String image;
    private String self;
    private Boolean isPrimary;
    private Boolean isFederationHub;
    private String homeRealmIdentifier;
    private Certificate certificate;
    private String alias;
    private Claims claims;
    private Roles roles;
    private List<IdPGroup> groups = null;

    private FederatedAuthenticatorListResponse federatedAuthenticators;
    private ProvisioningResponse provisioning;

    /**
    **/
    public IdentityProviderListItem id(String id) {

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
    public IdentityProviderListItem name(String name) {

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
    public IdentityProviderListItem description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "identity provider for google federation", value = "")
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
    public IdentityProviderListItem isEnabled(Boolean isEnabled) {

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
    public IdentityProviderListItem image(String image) {

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
    public IdentityProviderListItem self(String self) {

        this.self = self;
        return this;
    }
    
    @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-providers/123e4567-e89b-12d3-a456-556642440000", value = "")
    @JsonProperty("self")
    @Valid
    public String getSelf() {
        return self;
    }
    public void setSelf(String self) {
        this.self = self;
    }

    /**
    **/
    public IdentityProviderListItem isPrimary(Boolean isPrimary) {

        this.isPrimary = isPrimary;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "")
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
    public IdentityProviderListItem isFederationHub(Boolean isFederationHub) {

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
    public IdentityProviderListItem homeRealmIdentifier(String homeRealmIdentifier) {

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
    public IdentityProviderListItem certificate(Certificate certificate) {

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
    public IdentityProviderListItem alias(String alias) {

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
    public IdentityProviderListItem claims(Claims claims) {

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
    public IdentityProviderListItem roles(Roles roles) {

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
    * IdP groups supported by the IdP.
    **/
    public IdentityProviderListItem groups(List<IdPGroup> groups) {

        this.groups = groups;
        return this;
    }
    
    @ApiModelProperty(value = "IdP groups supported by the IdP.")
    @JsonProperty("groups")
    @Valid @Size(min=0)
    public List<IdPGroup> getGroups() {
        return groups;
    }
    public void setGroups(List<IdPGroup> groups) {
        this.groups = groups;
    }

    public IdentityProviderListItem addGroupsItem(IdPGroup groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<>();
        }
        this.groups.add(groupsItem);
        return this;
    }

        /**
    **/
    public IdentityProviderListItem federatedAuthenticators(FederatedAuthenticatorListResponse federatedAuthenticators) {

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
    public IdentityProviderListItem provisioning(ProvisioningResponse provisioning) {

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
        IdentityProviderListItem identityProviderListItem = (IdentityProviderListItem) o;
        return Objects.equals(this.id, identityProviderListItem.id) &&
            Objects.equals(this.name, identityProviderListItem.name) &&
            Objects.equals(this.description, identityProviderListItem.description) &&
            Objects.equals(this.isEnabled, identityProviderListItem.isEnabled) &&
            Objects.equals(this.image, identityProviderListItem.image) &&
            Objects.equals(this.self, identityProviderListItem.self) &&
            Objects.equals(this.isPrimary, identityProviderListItem.isPrimary) &&
            Objects.equals(this.isFederationHub, identityProviderListItem.isFederationHub) &&
            Objects.equals(this.homeRealmIdentifier, identityProviderListItem.homeRealmIdentifier) &&
            Objects.equals(this.certificate, identityProviderListItem.certificate) &&
            Objects.equals(this.alias, identityProviderListItem.alias) &&
            Objects.equals(this.claims, identityProviderListItem.claims) &&
            Objects.equals(this.roles, identityProviderListItem.roles) &&
            Objects.equals(this.groups, identityProviderListItem.groups) &&
            Objects.equals(this.federatedAuthenticators, identityProviderListItem.federatedAuthenticators) &&
            Objects.equals(this.provisioning, identityProviderListItem.provisioning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isEnabled, image, self, isPrimary, isFederationHub, homeRealmIdentifier, certificate, alias, claims, roles, groups, federatedAuthenticators, provisioning);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityProviderListItem {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    self: ").append(toIndentedString(self)).append("\n");
        sb.append("    isPrimary: ").append(toIndentedString(isPrimary)).append("\n");
        sb.append("    isFederationHub: ").append(toIndentedString(isFederationHub)).append("\n");
        sb.append("    homeRealmIdentifier: ").append(toIndentedString(homeRealmIdentifier)).append("\n");
        sb.append("    certificate: ").append(toIndentedString(certificate)).append("\n");
        sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
        sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
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

