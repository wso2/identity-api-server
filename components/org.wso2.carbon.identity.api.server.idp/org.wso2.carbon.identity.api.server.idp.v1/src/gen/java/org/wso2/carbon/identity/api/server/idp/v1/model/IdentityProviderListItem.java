package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class IdentityProviderListItem   {
  
  private String id;

  private String name;

  private String description;

  private Boolean isEnabled = true;

  private String image;

  private String identityProvider;


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
  public IdentityProviderListItem identityProvider(String identityProvider) {
    this.identityProvider = identityProvider;
    return this;
  }

  
  @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-providers/123e4567-e89b-12d3-a456-556642440000", value = "")
  @JsonProperty("identityProvider")
  @Valid
  public String getIdentityProvider() {
    return identityProvider;
  }
  public void setIdentityProvider(String identityProvider) {
    this.identityProvider = identityProvider;
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
        Objects.equals(this.identityProvider, identityProviderListItem.identityProvider);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, isEnabled, image, identityProvider);
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
    sb.append("    identityProvider: ").append(toIndentedString(identityProvider)).append("\n");
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

