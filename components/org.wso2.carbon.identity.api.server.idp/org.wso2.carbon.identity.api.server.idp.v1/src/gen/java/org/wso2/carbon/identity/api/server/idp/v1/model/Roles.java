package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.RoleMapping;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class Roles   {
  
  private List<RoleMapping> mappings = null;

  private List<String> outboundProvisioningRoles = null;


  /**
   **/
  public Roles mappings(List<RoleMapping> mappings) {
    this.mappings = mappings;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("mappings")
  @Valid
  public List<RoleMapping> getMappings() {
    return mappings;
  }
  public void setMappings(List<RoleMapping> mappings) {
    this.mappings = mappings;
  }

  public Roles addMappingsItem(RoleMapping mappingsItem) {
    if (this.mappings == null) {
      this.mappings = new ArrayList<>();
    }
    this.mappings.add(mappingsItem);
    return this;
  }


  /**
   **/
  public Roles outboundProvisioningRoles(List<String> outboundProvisioningRoles) {
    this.outboundProvisioningRoles = outboundProvisioningRoles;
    return this;
  }

  
  @ApiModelProperty(example = "[\"manager\",\"hr-admin\"]", value = "")
  @JsonProperty("outboundProvisioningRoles")
  @Valid
  public List<String> getOutboundProvisioningRoles() {
    return outboundProvisioningRoles;
  }
  public void setOutboundProvisioningRoles(List<String> outboundProvisioningRoles) {
    this.outboundProvisioningRoles = outboundProvisioningRoles;
  }

  public Roles addOutboundProvisioningRolesItem(String outboundProvisioningRolesItem) {
    if (this.outboundProvisioningRoles == null) {
      this.outboundProvisioningRoles = new ArrayList<>();
    }
    this.outboundProvisioningRoles.add(outboundProvisioningRolesItem);
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
    Roles roles = (Roles) o;
    return Objects.equals(this.mappings, roles.mappings) &&
        Objects.equals(this.outboundProvisioningRoles, roles.outboundProvisioningRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mappings, outboundProvisioningRoles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Roles {\n");
    
    sb.append("    mappings: ").append(toIndentedString(mappings)).append("\n");
    sb.append("    outboundProvisioningRoles: ").append(toIndentedString(outboundProvisioningRoles)).append("\n");
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

