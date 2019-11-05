package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderGetResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Property;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class IdentityProviderResponse extends IdentityProviderGetResponse  {
  
  private String self;


  /**
   **/
  public IdentityProviderResponse self(String self) {
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



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
        return false;
    }
    
    IdentityProviderResponse identityProviderResponse = (IdentityProviderResponse) o;
    return Objects.equals(this.self, identityProviderResponse.self);
  }

  @Override
  public int hashCode() {
    return Objects.hash(self);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentityProviderResponse {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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

