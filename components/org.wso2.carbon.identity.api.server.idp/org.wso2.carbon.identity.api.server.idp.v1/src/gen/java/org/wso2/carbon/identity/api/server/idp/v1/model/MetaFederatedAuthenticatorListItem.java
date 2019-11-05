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


public class MetaFederatedAuthenticatorListItem   {
  
  private String authenticatorId;

  private String name;

  private String metaAuthenticator;


  /**
   **/
  public MetaFederatedAuthenticatorListItem authenticatorId(String authenticatorId) {
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
  public MetaFederatedAuthenticatorListItem name(String name) {
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
  public MetaFederatedAuthenticatorListItem metaAuthenticator(String metaAuthenticator) {
    this.metaAuthenticator = metaAuthenticator;
    return this;
  }

  
  @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-providers/meta/federated-authenticators/U0FNTFNTT0F1dGhlbnRpY2F0b3I", value = "")
  @JsonProperty("metaAuthenticator")
  @Valid
  public String getMetaAuthenticator() {
    return metaAuthenticator;
  }
  public void setMetaAuthenticator(String metaAuthenticator) {
    this.metaAuthenticator = metaAuthenticator;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaFederatedAuthenticatorListItem metaFederatedAuthenticatorListItem = (MetaFederatedAuthenticatorListItem) o;
    return Objects.equals(this.authenticatorId, metaFederatedAuthenticatorListItem.authenticatorId) &&
        Objects.equals(this.name, metaFederatedAuthenticatorListItem.name) &&
        Objects.equals(this.metaAuthenticator, metaFederatedAuthenticatorListItem.metaAuthenticator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticatorId, name, metaAuthenticator);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetaFederatedAuthenticatorListItem {\n");
    
    sb.append("    authenticatorId: ").append(toIndentedString(authenticatorId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    metaAuthenticator: ").append(toIndentedString(metaAuthenticator)).append("\n");
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

