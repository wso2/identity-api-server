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


public class Property   {
  
  private String key;

  private String value;


  /**
   **/
  public Property key(String key) {
    this.key = key;
    return this;
  }

  
  @ApiModelProperty(example = "somePropertyKey", required = true, value = "")
  @JsonProperty("key")
  @Valid
  @NotNull(message = "Property key cannot be null.")
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }


  /**
   **/
  public Property value(String value) {
    this.value = value;
    return this;
  }

  
  @ApiModelProperty(example = "somePropertyValue", value = "")
  @JsonProperty("value")
  @Valid
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Property property = (Property) o;
    return Objects.equals(this.key, property.key) &&
        Objects.equals(this.value, property.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Property {\n");
    
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

