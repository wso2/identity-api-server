package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.idp.v1.model.JustInTimeProvisioning;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class ProvisioningResponse   {
  
  private JustInTimeProvisioning jit;

  private OutboundConnectorListResponse outboundConnectors;


  /**
   **/
  public ProvisioningResponse jit(JustInTimeProvisioning jit) {
    this.jit = jit;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("jit")
  @Valid
  public JustInTimeProvisioning getJit() {
    return jit;
  }
  public void setJit(JustInTimeProvisioning jit) {
    this.jit = jit;
  }


  /**
   **/
  public ProvisioningResponse outboundConnectors(OutboundConnectorListResponse outboundConnectors) {
    this.outboundConnectors = outboundConnectors;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("outboundConnectors")
  @Valid
  public OutboundConnectorListResponse getOutboundConnectors() {
    return outboundConnectors;
  }
  public void setOutboundConnectors(OutboundConnectorListResponse outboundConnectors) {
    this.outboundConnectors = outboundConnectors;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProvisioningResponse provisioningResponse = (ProvisioningResponse) o;
    return Objects.equals(this.jit, provisioningResponse.jit) &&
        Objects.equals(this.outboundConnectors, provisioningResponse.outboundConnectors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jit, outboundConnectors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProvisioningResponse {\n");
    
    sb.append("    jit: ").append(toIndentedString(jit)).append("\n");
    sb.append("    outboundConnectors: ").append(toIndentedString(outboundConnectors)).append("\n");
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

