package org.wso2.carbon.identity.api.server.identity.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;


public class Link   {
  
  private String href;

  private String rel;


  /**
   * Path to the target resource.
   **/
  public Link href(String href) {
    this.href = href;
    return this;
  }

  
  @ApiModelProperty(example = "/t/carbon.super/api/server/v1/identity-governance/QWNjb3VudCBNYW5hZ2VtZW50IFBvbGljaWVz", value = "Path to the target resource.")
  @JsonProperty("href")
  public String getHref() {
    return href;
  }
  public void setHref(String href) {
    this.href = href;
  }


  /**
   * Describes how the current context is related to the target resource.
   **/
  public Link rel(String rel) {
    this.rel = rel;
    return this;
  }

  
  @ApiModelProperty(example = "connector", value = "Describes how the current context is related to the target resource.")
  @JsonProperty("rel")
  public String getRel() {
    return rel;
  }
  public void setRel(String rel) {
    this.rel = rel;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Link link = (Link) o;
    return Objects.equals(href, link.href) &&
        Objects.equals(rel, link.rel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(href, rel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Link {\n");
    
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    rel: ").append(toIndentedString(rel)).append("\n");
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

