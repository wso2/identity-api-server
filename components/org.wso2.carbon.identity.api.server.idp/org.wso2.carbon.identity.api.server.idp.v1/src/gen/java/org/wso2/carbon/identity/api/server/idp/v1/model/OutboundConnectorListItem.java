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


public class OutboundConnectorListItem   {
  
  private String connectorId;

  private String name;

  private Boolean isEnabled = false;

  private String connector;


  /**
   **/
  public OutboundConnectorListItem connectorId(String connectorId) {
    this.connectorId = connectorId;
    return this;
  }

  
  @ApiModelProperty(example = "U0NJTQ==", value = "")
  @JsonProperty("connectorId")
  @Valid
  public String getConnectorId() {
    return connectorId;
  }
  public void setConnectorId(String connectorId) {
    this.connectorId = connectorId;
  }


  /**
   **/
  public OutboundConnectorListItem name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(example = "SCIM", value = "")
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
  public OutboundConnectorListItem isEnabled(Boolean isEnabled) {
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
  public OutboundConnectorListItem connector(String connector) {
    this.connector = connector;
    return this;
  }

  
  @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-providers/123e4567-e89b-12d3-a456-556642440000/provisioning/outbound-connectors/U0NJTQ", value = "")
  @JsonProperty("connector")
  @Valid
  public String getConnector() {
    return connector;
  }
  public void setConnector(String connector) {
    this.connector = connector;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OutboundConnectorListItem outboundConnectorListItem = (OutboundConnectorListItem) o;
    return Objects.equals(this.connectorId, outboundConnectorListItem.connectorId) &&
        Objects.equals(this.name, outboundConnectorListItem.name) &&
        Objects.equals(this.isEnabled, outboundConnectorListItem.isEnabled) &&
        Objects.equals(this.connector, outboundConnectorListItem.connector);
  }

  @Override
  public int hashCode() {
    return Objects.hash(connectorId, name, isEnabled, connector);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OutboundConnectorListItem {\n");
    
    sb.append("    connectorId: ").append(toIndentedString(connectorId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
    sb.append("    connector: ").append(toIndentedString(connector)).append("\n");
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

