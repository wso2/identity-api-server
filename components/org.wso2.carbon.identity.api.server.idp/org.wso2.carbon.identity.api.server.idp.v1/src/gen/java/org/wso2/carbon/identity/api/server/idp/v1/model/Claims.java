package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claim;
import org.wso2.carbon.identity.api.server.idp.v1.model.ClaimMapping;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningClaim;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class Claims   {
  
  private Claim userIdClaim;

  private Claim roleClaim;

  private List<ClaimMapping> mappings = null;

  private List<ProvisioningClaim> provisioningClaims = null;


  /**
   **/
  public Claims userIdClaim(Claim userIdClaim) {
    this.userIdClaim = userIdClaim;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("userIdClaim")
  @Valid
  public Claim getUserIdClaim() {
    return userIdClaim;
  }
  public void setUserIdClaim(Claim userIdClaim) {
    this.userIdClaim = userIdClaim;
  }


  /**
   **/
  public Claims roleClaim(Claim roleClaim) {
    this.roleClaim = roleClaim;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("roleClaim")
  @Valid
  public Claim getRoleClaim() {
    return roleClaim;
  }
  public void setRoleClaim(Claim roleClaim) {
    this.roleClaim = roleClaim;
  }


  /**
   **/
  public Claims mappings(List<ClaimMapping> mappings) {
    this.mappings = mappings;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("mappings")
  @Valid
  public List<ClaimMapping> getMappings() {
    return mappings;
  }
  public void setMappings(List<ClaimMapping> mappings) {
    this.mappings = mappings;
  }

  public Claims addMappingsItem(ClaimMapping mappingsItem) {
    if (this.mappings == null) {
      this.mappings = new ArrayList<>();
    }
    this.mappings.add(mappingsItem);
    return this;
  }


  /**
   **/
  public Claims provisioningClaims(List<ProvisioningClaim> provisioningClaims) {
    this.provisioningClaims = provisioningClaims;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("provisioningClaims")
  @Valid
  public List<ProvisioningClaim> getProvisioningClaims() {
    return provisioningClaims;
  }
  public void setProvisioningClaims(List<ProvisioningClaim> provisioningClaims) {
    this.provisioningClaims = provisioningClaims;
  }

  public Claims addProvisioningClaimsItem(ProvisioningClaim provisioningClaimsItem) {
    if (this.provisioningClaims == null) {
      this.provisioningClaims = new ArrayList<>();
    }
    this.provisioningClaims.add(provisioningClaimsItem);
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
    Claims claims = (Claims) o;
    return Objects.equals(this.userIdClaim, claims.userIdClaim) &&
        Objects.equals(this.roleClaim, claims.roleClaim) &&
        Objects.equals(this.mappings, claims.mappings) &&
        Objects.equals(this.provisioningClaims, claims.provisioningClaims);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userIdClaim, roleClaim, mappings, provisioningClaims);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Claims {\n");
    
    sb.append("    userIdClaim: ").append(toIndentedString(userIdClaim)).append("\n");
    sb.append("    roleClaim: ").append(toIndentedString(roleClaim)).append("\n");
    sb.append("    mappings: ").append(toIndentedString(mappings)).append("\n");
    sb.append("    provisioningClaims: ").append(toIndentedString(provisioningClaims)).append("\n");
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

