package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Property;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class IdentityProviderPUTRequest extends IdentityProviderPOSTRequest  {
  
  private Boolean isEnabled = true;


  /**
   * Whether identity provider is enabled or not
   **/
  public IdentityProviderPUTRequest isEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  
  @ApiModelProperty(example = "false", value = "Whether identity provider is enabled or not")
  @JsonProperty("isEnabled")
  @Valid
  public Boolean getIsEnabled() {
    return isEnabled;
  }
  public void setIsEnabled(Boolean isEnabled) {
    this.isEnabled = isEnabled;
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
    
    IdentityProviderPUTRequest identityProviderPUTRequest = (IdentityProviderPUTRequest) o;
    return Objects.equals(this.isEnabled, identityProviderPUTRequest.isEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentityProviderPUTRequest {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
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

