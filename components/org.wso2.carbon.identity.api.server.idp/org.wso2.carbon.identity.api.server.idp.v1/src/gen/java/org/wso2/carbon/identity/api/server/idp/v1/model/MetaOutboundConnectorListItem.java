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


public class MetaOutboundConnectorListItem   {
  
  private String connectorId;

  private String name;

  private String metaConnector;


  /**
   **/
  public MetaOutboundConnectorListItem connectorId(String connectorId) {
    this.connectorId = connectorId;
    return this;
  }

  
  @ApiModelProperty(example = "U0NJTQ", value = "")
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
  public MetaOutboundConnectorListItem name(String name) {
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
  public MetaOutboundConnectorListItem metaConnector(String metaConnector) {
    this.metaConnector = metaConnector;
    return this;
  }

  
  @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-providers/meta/outbound-provisioning-connectos/U0NJTQ", value = "")
  @JsonProperty("metaConnector")
  @Valid
  public String getMetaConnector() {
    return metaConnector;
  }
  public void setMetaConnector(String metaConnector) {
    this.metaConnector = metaConnector;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaOutboundConnectorListItem metaOutboundConnectorListItem = (MetaOutboundConnectorListItem) o;
    return Objects.equals(this.connectorId, metaOutboundConnectorListItem.connectorId) &&
        Objects.equals(this.name, metaOutboundConnectorListItem.name) &&
        Objects.equals(this.metaConnector, metaOutboundConnectorListItem.metaConnector);
  }

  @Override
  public int hashCode() {
    return Objects.hash(connectorId, name, metaConnector);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetaOutboundConnectorListItem {\n");
    
    sb.append("    connectorId: ").append(toIndentedString(connectorId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    metaConnector: ").append(toIndentedString(metaConnector)).append("\n");
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

