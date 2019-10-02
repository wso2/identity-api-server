package org.wso2.carbon.identity.api.server.identity.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorLink;
import javax.validation.constraints.*;

/**
 * Governance connector response with minimal attributes.
 **/

import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;

@ApiModel(description = "Governance connector response with minimal attributes.")
public class CategoryConnectorsRes   {
  
  private String id;

  private List<ConnectorLink> links = null;


  /**
   * Connector id.
   **/
  public CategoryConnectorsRes id(String id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(example = "c3VzcGVuc2lvbi5ub3RpZmljYXRpb24", value = "Connector id.")
  @JsonProperty("id")
  @Valid
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }


  /**
   * Connectors of the category with minimal attributes.
   **/
  public CategoryConnectorsRes links(List<ConnectorLink> links) {
    this.links = links;
    return this;
  }

  
  @ApiModelProperty(value = "Connectors of the category with minimal attributes.")
  @JsonProperty("links")
  @Valid
  public List<ConnectorLink> getLinks() {
    return links;
  }
  public void setLinks(List<ConnectorLink> links) {
    this.links = links;
  }

  public CategoryConnectorsRes addLinksItem(ConnectorLink linksItem) {
    if (this.links == null) {
      this.links = new ArrayList<>();
    }
    this.links.add(linksItem);
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
    CategoryConnectorsRes categoryConnectorsRes = (CategoryConnectorsRes) o;
    return Objects.equals(this.id, categoryConnectorsRes.id) &&
        Objects.equals(this.links, categoryConnectorsRes.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CategoryConnectorsRes {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

