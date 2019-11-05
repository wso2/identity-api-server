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
import org.wso2.carbon.identity.api.server.idp.v1.model.Property;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class IdentityProviderGetResponse   {
  
  private String id;

  private String name;

  private String description;

  private Boolean isEnabled = true;

  private Boolean isPrimary = false;

  private String image;

  private Boolean isFederationHub;

  private String homeRealmIdentifier;

  private Certificate certificate = null;

  private String alias;

  private Claims claims;

  private Roles roles;

  private FederatedAuthenticatorListResponse federatedAuthenticators;

  private ProvisioningResponse provisioning;

  private List<Property> properties = null;


  /**
   **/
  public IdentityProviderGetResponse id(String id) {
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
  public IdentityProviderGetResponse name(String name) {
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
  public IdentityProviderGetResponse description(String description) {
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
  public IdentityProviderGetResponse isEnabled(Boolean isEnabled) {
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
  public IdentityProviderGetResponse isPrimary(Boolean isPrimary) {
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
  public IdentityProviderGetResponse image(String image) {
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
  public IdentityProviderGetResponse isFederationHub(Boolean isFederationHub) {
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
  public IdentityProviderGetResponse homeRealmIdentifier(String homeRealmIdentifier) {
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
  public IdentityProviderGetResponse certificate(Certificate certificate) {
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
  public IdentityProviderGetResponse alias(String alias) {
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
  public IdentityProviderGetResponse claims(Claims claims) {
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
  public IdentityProviderGetResponse roles(Roles roles) {
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
  public IdentityProviderGetResponse federatedAuthenticators(FederatedAuthenticatorListResponse federatedAuthenticators) {
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
  public IdentityProviderGetResponse provisioning(ProvisioningResponse provisioning) {
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


  /**
   **/
  public IdentityProviderGetResponse properties(List<Property> properties) {
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

  public IdentityProviderGetResponse addPropertiesItem(Property propertiesItem) {
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
    IdentityProviderGetResponse identityProviderGetResponse = (IdentityProviderGetResponse) o;
    return Objects.equals(this.id, identityProviderGetResponse.id) &&
        Objects.equals(this.name, identityProviderGetResponse.name) &&
        Objects.equals(this.description, identityProviderGetResponse.description) &&
        Objects.equals(this.isEnabled, identityProviderGetResponse.isEnabled) &&
        Objects.equals(this.isPrimary, identityProviderGetResponse.isPrimary) &&
        Objects.equals(this.image, identityProviderGetResponse.image) &&
        Objects.equals(this.isFederationHub, identityProviderGetResponse.isFederationHub) &&
        Objects.equals(this.homeRealmIdentifier, identityProviderGetResponse.homeRealmIdentifier) &&
        Objects.equals(this.certificate, identityProviderGetResponse.certificate) &&
        Objects.equals(this.alias, identityProviderGetResponse.alias) &&
        Objects.equals(this.claims, identityProviderGetResponse.claims) &&
        Objects.equals(this.roles, identityProviderGetResponse.roles) &&
        Objects.equals(this.federatedAuthenticators, identityProviderGetResponse.federatedAuthenticators) &&
        Objects.equals(this.provisioning, identityProviderGetResponse.provisioning) &&
        Objects.equals(this.properties, identityProviderGetResponse.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, isEnabled, isPrimary, image, isFederationHub, homeRealmIdentifier, certificate, alias, claims, roles, federatedAuthenticators, provisioning, properties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentityProviderGetResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
    return o.toString().replace("\n", "\n    ");
  }
}

