package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claim;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class ClaimMapping   {
  
  private String idpClaim;

  private Claim localClaim;


  /**
   **/
  public ClaimMapping idpClaim(String idpClaim) {
    this.idpClaim = idpClaim;
    return this;
  }

  
  @ApiModelProperty(example = "country", value = "")
  @JsonProperty("idpClaim")
  @Valid
  public String getIdpClaim() {
    return idpClaim;
  }
  public void setIdpClaim(String idpClaim) {
    this.idpClaim = idpClaim;
  }


  /**
   **/
  public ClaimMapping localClaim(Claim localClaim) {
    this.localClaim = localClaim;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("localClaim")
  @Valid
  public Claim getLocalClaim() {
    return localClaim;
  }
  public void setLocalClaim(Claim localClaim) {
    this.localClaim = localClaim;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClaimMapping claimMapping = (ClaimMapping) o;
    return Objects.equals(this.idpClaim, claimMapping.idpClaim) &&
        Objects.equals(this.localClaim, claimMapping.localClaim);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idpClaim, localClaim);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClaimMapping {\n");
    
    sb.append("    idpClaim: ").append(toIndentedString(idpClaim)).append("\n");
    sb.append("    localClaim: ").append(toIndentedString(localClaim)).append("\n");
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

