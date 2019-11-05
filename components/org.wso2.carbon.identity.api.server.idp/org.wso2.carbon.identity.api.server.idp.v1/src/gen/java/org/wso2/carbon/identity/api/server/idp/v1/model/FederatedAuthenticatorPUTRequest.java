package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.Property;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class FederatedAuthenticatorPUTRequest   {
  
  private String authenticatorId;

  private String name;

  private Boolean isEnabled = false;

  private List<Property> properties = null;

  private String self;


  /**
   **/
  public FederatedAuthenticatorPUTRequest authenticatorId(String authenticatorId) {
    this.authenticatorId = authenticatorId;
    return this;
  }

  
  @ApiModelProperty(value = "")
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
  public FederatedAuthenticatorPUTRequest name(String name) {
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
  public FederatedAuthenticatorPUTRequest isEnabled(Boolean isEnabled) {
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
  public FederatedAuthenticatorPUTRequest properties(List<Property> properties) {
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

  public FederatedAuthenticatorPUTRequest addPropertiesItem(Property propertiesItem) {
    if (this.properties == null) {
      this.properties = new ArrayList<>();
    }
    this.properties.add(propertiesItem);
    return this;
  }


  /**
   **/
  public FederatedAuthenticatorPUTRequest self(String self) {
    this.self = self;
    return this;
  }

  
  @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-providers/123e4567-e89b-12d3-a456-556642440000/federated-authenticators/U0FNTDJBdXRoZW50aWNhdG9y", value = "")
  @JsonProperty("self")
  @Valid
  public String getSelf() {
    return self;
  }
  public void setSelf(String self) {
    this.self = self;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FederatedAuthenticatorPUTRequest federatedAuthenticatorPUTRequest = (FederatedAuthenticatorPUTRequest) o;
    return Objects.equals(this.authenticatorId, federatedAuthenticatorPUTRequest.authenticatorId) &&
        Objects.equals(this.name, federatedAuthenticatorPUTRequest.name) &&
        Objects.equals(this.isEnabled, federatedAuthenticatorPUTRequest.isEnabled) &&
        Objects.equals(this.properties, federatedAuthenticatorPUTRequest.properties) &&
        Objects.equals(this.self, federatedAuthenticatorPUTRequest.self);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticatorId, name, isEnabled, properties, self);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FederatedAuthenticatorPUTRequest {\n");
    
    sb.append("    authenticatorId: ").append(toIndentedString(authenticatorId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    self: ").append(toIndentedString(self)).append("\n");
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

