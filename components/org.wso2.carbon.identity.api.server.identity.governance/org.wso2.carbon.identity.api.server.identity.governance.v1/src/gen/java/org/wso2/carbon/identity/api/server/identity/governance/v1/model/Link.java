package org.wso2.carbon.identity.api.server.identity.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;


public class Link   {
  
  private String href;


@XmlType(name="RelEnum")
@XmlEnum(String.class)
public enum RelEnum {

    @XmlEnumValue("CATEGORY") CATEGORY(String.valueOf("CATEGORY")), @XmlEnumValue("CONNECTOR") CONNECTOR(String.valueOf("CONNECTOR"));


    private String value;

    RelEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static RelEnum fromValue(String value) {
        for (RelEnum b : RelEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private RelEnum rel;


  /**
   * Path to the target resource.
   **/
  public Link href(String href) {
    this.href = href;
    return this;
  }

  
  @ApiModelProperty(value = "Path to the target resource.")
  @JsonProperty("href")
  @Valid
  public String getHref() {
    return href;
  }
  public void setHref(String href) {
    this.href = href;
  }


  /**
   * Describes how the current context is related to the target resource.
   **/
  public Link rel(RelEnum rel) {
    this.rel = rel;
    return this;
  }

  
  @ApiModelProperty(value = "Describes how the current context is related to the target resource.")
  @JsonProperty("rel")
  @Valid
  public RelEnum getRel() {
    return rel;
  }
  public void setRel(RelEnum rel) {
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
    return Objects.equals(this.href, link.href) &&
        Objects.equals(this.rel, link.rel);
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

