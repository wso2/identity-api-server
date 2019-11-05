package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnector;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class OutboundProvisioningRequest   {
  
  private String defaultConnectorId;

  private List<OutboundConnector> connectors = null;


  /**
   **/
  public OutboundProvisioningRequest defaultConnectorId(String defaultConnectorId) {
    this.defaultConnectorId = defaultConnectorId;
    return this;
  }

  
  @ApiModelProperty(example = "U0NJTQ==", required = true, value = "")
  @JsonProperty("defaultConnectorId")
  @Valid
  @NotNull(message = "Property defaultConnectorId cannot be null.")
  public String getDefaultConnectorId() {
    return defaultConnectorId;
  }
  public void setDefaultConnectorId(String defaultConnectorId) {
    this.defaultConnectorId = defaultConnectorId;
  }


  /**
   **/
  public OutboundProvisioningRequest connectors(List<OutboundConnector> connectors) {
    this.connectors = connectors;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("connectors")
  @Valid
  public List<OutboundConnector> getConnectors() {
    return connectors;
  }
  public void setConnectors(List<OutboundConnector> connectors) {
    this.connectors = connectors;
  }

  public OutboundProvisioningRequest addConnectorsItem(OutboundConnector connectorsItem) {
    if (this.connectors == null) {
      this.connectors = new ArrayList<>();
    }
    this.connectors.add(connectorsItem);
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
    OutboundProvisioningRequest outboundProvisioningRequest = (OutboundProvisioningRequest) o;
    return Objects.equals(this.defaultConnectorId, outboundProvisioningRequest.defaultConnectorId) &&
        Objects.equals(this.connectors, outboundProvisioningRequest.connectors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defaultConnectorId, connectors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OutboundProvisioningRequest {\n");
    
    sb.append("    defaultConnectorId: ").append(toIndentedString(defaultConnectorId)).append("\n");
    sb.append("    connectors: ").append(toIndentedString(connectors)).append("\n");
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

