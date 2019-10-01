package org.wso2.carbon.identity.api.server.identity.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoryConnectorsRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.Link;
import javax.validation.constraints.*;

/**
 * Governance connector category response.
 **/

import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;

@ApiModel(description = "Governance connector category response.")
public class CategoriesRes   {
  
  private String id;

  private String name;

  private List<Link> links = null;

  private List<CategoryConnectorsRes> connectors = null;


  /**
   * Connector category id.
   **/
  public CategoriesRes id(String id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(example = "QWNjb3VudCBNYW5hZ2VtZW50IFBvbGljaWVz", value = "Connector category id.")
  @JsonProperty("id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }


  /**
   * Connector category name.
   **/
  public CategoriesRes name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(example = "Account Management Policies", value = "Connector category name.")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }


  /**
   * Connectors of the category with minimal attributes.
   **/
  public CategoriesRes links(List<Link> links) {
    this.links = links;
    return this;
  }

  
  @ApiModelProperty(value = "Connectors of the category with minimal attributes.")
  @JsonProperty("links")
  public List<Link> getLinks() {
    return links;
  }
  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public CategoriesRes addLinksItem(Link linksItem) {
    if (this.links == null) {
      this.links = new ArrayList<>();
    }
    this.links.add(linksItem);
    return this;
  }


  /**
   * Connectors of the category with minimal attributes.
   **/
  public CategoriesRes connectors(List<CategoryConnectorsRes> connectors) {
    this.connectors = connectors;
    return this;
  }

  
  @ApiModelProperty(value = "Connectors of the category with minimal attributes.")
  @JsonProperty("connectors")
  public List<CategoryConnectorsRes> getConnectors() {
    return connectors;
  }
  public void setConnectors(List<CategoryConnectorsRes> connectors) {
    this.connectors = connectors;
  }

  public CategoriesRes addConnectorsItem(CategoryConnectorsRes connectorsItem) {
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
    CategoriesRes categoriesRes = (CategoriesRes) o;
    return Objects.equals(id, categoriesRes.id) &&
        Objects.equals(name, categoriesRes.name) &&
        Objects.equals(links, categoriesRes.links) &&
        Objects.equals(connectors, categoriesRes.connectors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, links, connectors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CategoriesRes {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

