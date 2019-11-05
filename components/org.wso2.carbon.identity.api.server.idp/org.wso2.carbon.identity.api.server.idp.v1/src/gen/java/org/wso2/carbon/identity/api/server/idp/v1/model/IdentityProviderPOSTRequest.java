package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Property;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class IdentityProviderPOSTRequest   {
  
  private String name;

  private String description;

  private String image;

  private Boolean isPrimary = false;

  private Boolean isFederationHub = false;

  private String homeRealmIdentifier;

  private Certificate certificate = null;

  private String alias;

  private Claims claims;

  private Roles roles;

  private FederatedAuthenticatorRequest federatedAuthenticators;

  private ProvisioningRequest provisioning;

  private List<Property> properties = null;


  /**
   **/
  public IdentityProviderPOSTRequest name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(example = "google", required = true, value = "")
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
  public IdentityProviderPOSTRequest description(String description) {
    this.description = description;
    return this;
  }

  
  @ApiModelProperty(example = "IDP for Google Federation", value = "")
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
  public IdentityProviderPOSTRequest image(String image) {
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
  public IdentityProviderPOSTRequest isPrimary(Boolean isPrimary) {
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
  public IdentityProviderPOSTRequest isFederationHub(Boolean isFederationHub) {
    this.isFederationHub = isFederationHub;
    return this;
  }

  
  @ApiModelProperty(value = "")
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
  public IdentityProviderPOSTRequest homeRealmIdentifier(String homeRealmIdentifier) {
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
  public IdentityProviderPOSTRequest certificate(Certificate certificate) {
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
  public IdentityProviderPOSTRequest alias(String alias) {
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
  public IdentityProviderPOSTRequest claims(Claims claims) {
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
  public IdentityProviderPOSTRequest roles(Roles roles) {
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
  public IdentityProviderPOSTRequest federatedAuthenticators(FederatedAuthenticatorRequest federatedAuthenticators) {
    this.federatedAuthenticators = federatedAuthenticators;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("federatedAuthenticators")
  @Valid
  public FederatedAuthenticatorRequest getFederatedAuthenticators() {
    return federatedAuthenticators;
  }
  public void setFederatedAuthenticators(FederatedAuthenticatorRequest federatedAuthenticators) {
    this.federatedAuthenticators = federatedAuthenticators;
  }


  /**
   **/
  public IdentityProviderPOSTRequest provisioning(ProvisioningRequest provisioning) {
    this.provisioning = provisioning;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("provisioning")
  @Valid
  public ProvisioningRequest getProvisioning() {
    return provisioning;
  }
  public void setProvisioning(ProvisioningRequest provisioning) {
    this.provisioning = provisioning;
  }


  /**
   **/
  public IdentityProviderPOSTRequest properties(List<Property> properties) {
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

  public IdentityProviderPOSTRequest addPropertiesItem(Property propertiesItem) {
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
    IdentityProviderPOSTRequest identityProviderPOSTRequest = (IdentityProviderPOSTRequest) o;
    return Objects.equals(this.name, identityProviderPOSTRequest.name) &&
        Objects.equals(this.description, identityProviderPOSTRequest.description) &&
        Objects.equals(this.image, identityProviderPOSTRequest.image) &&
        Objects.equals(this.isPrimary, identityProviderPOSTRequest.isPrimary) &&
        Objects.equals(this.isFederationHub, identityProviderPOSTRequest.isFederationHub) &&
        Objects.equals(this.homeRealmIdentifier, identityProviderPOSTRequest.homeRealmIdentifier) &&
        Objects.equals(this.certificate, identityProviderPOSTRequest.certificate) &&
        Objects.equals(this.alias, identityProviderPOSTRequest.alias) &&
        Objects.equals(this.claims, identityProviderPOSTRequest.claims) &&
        Objects.equals(this.roles, identityProviderPOSTRequest.roles) &&
        Objects.equals(this.federatedAuthenticators, identityProviderPOSTRequest.federatedAuthenticators) &&
        Objects.equals(this.provisioning, identityProviderPOSTRequest.provisioning) &&
        Objects.equals(this.properties, identityProviderPOSTRequest.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, image, isPrimary, isFederationHub, homeRealmIdentifier, certificate, alias, claims, roles, federatedAuthenticators, provisioning, properties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentityProviderPOSTRequest {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("    isPrimary: ").append(toIndentedString(isPrimary)).append("\n");
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

