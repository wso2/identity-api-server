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


public class FederatedAuthenticatorListItem   {
  
  private String authenticatorId;

  private String name;

  private Boolean isEnabled = false;

  private String authenticator;


  /**
   **/
  public FederatedAuthenticatorListItem authenticatorId(String authenticatorId) {
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
  public FederatedAuthenticatorListItem name(String name) {
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
  public FederatedAuthenticatorListItem isEnabled(Boolean isEnabled) {
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
  public FederatedAuthenticatorListItem authenticator(String authenticator) {
    this.authenticator = authenticator;
    return this;
  }

  
  @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-providers/123e4567-e89b-12d3-a456-556642440000/federated-authenticators/U0FNTDJBdXRoZW50aWNhdG9y", value = "")
  @JsonProperty("authenticator")
  @Valid
  public String getAuthenticator() {
    return authenticator;
  }
  public void setAuthenticator(String authenticator) {
    this.authenticator = authenticator;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FederatedAuthenticatorListItem federatedAuthenticatorListItem = (FederatedAuthenticatorListItem) o;
    return Objects.equals(this.authenticatorId, federatedAuthenticatorListItem.authenticatorId) &&
        Objects.equals(this.name, federatedAuthenticatorListItem.name) &&
        Objects.equals(this.isEnabled, federatedAuthenticatorListItem.isEnabled) &&
        Objects.equals(this.authenticator, federatedAuthenticatorListItem.authenticator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticatorId, name, isEnabled, authenticator);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FederatedAuthenticatorListItem {\n");
    
    sb.append("    authenticatorId: ").append(toIndentedString(authenticatorId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    authenticator: ").append(toIndentedString(authenticator)).append("\n");
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

