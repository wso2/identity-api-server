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


public class RoleMapping   {
  
  private String idpRole;

  private String localRole;


  /**
   **/
  public RoleMapping idpRole(String idpRole) {
    this.idpRole = idpRole;
    return this;
  }

  
  @ApiModelProperty(example = "google-manager", value = "")
  @JsonProperty("idpRole")
  @Valid
  public String getIdpRole() {
    return idpRole;
  }
  public void setIdpRole(String idpRole) {
    this.idpRole = idpRole;
  }


  /**
   **/
  public RoleMapping localRole(String localRole) {
    this.localRole = localRole;
    return this;
  }

  
  @ApiModelProperty(example = "manager", value = "")
  @JsonProperty("localRole")
  @Valid
  public String getLocalRole() {
    return localRole;
  }
  public void setLocalRole(String localRole) {
    this.localRole = localRole;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleMapping roleMapping = (RoleMapping) o;
    return Objects.equals(this.idpRole, roleMapping.idpRole) &&
        Objects.equals(this.localRole, roleMapping.localRole);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idpRole, localRole);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RoleMapping {\n");
    
    sb.append("    idpRole: ").append(toIndentedString(idpRole)).append("\n");
    sb.append("    localRole: ").append(toIndentedString(localRole)).append("\n");
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

