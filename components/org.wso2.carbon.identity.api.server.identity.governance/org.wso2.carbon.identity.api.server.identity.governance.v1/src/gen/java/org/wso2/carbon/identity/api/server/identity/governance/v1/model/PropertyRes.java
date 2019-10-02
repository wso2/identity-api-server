package org.wso2.carbon.identity.api.server.identity.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Governance connector property.
 **/

import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;

@ApiModel(description = "Governance connector property.")
public class PropertyRes   {
  
  private String name;

  private String value;

  private String displayName;

  private String description;


  /**
   * Property name.
   **/
  public PropertyRes name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(example = "suspension.notification.enable", value = "Property name.")
  @JsonProperty("name")
@Valid
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }


  /**
   * Property value.
   **/
  public PropertyRes value(String value) {
    this.value = value;
    return this;
  }

  
  @ApiModelProperty(example = "false", value = "Property value.")
  @JsonProperty("value")
@Valid
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }


  /**
   * Property display name.
   **/
  public PropertyRes displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  
  @ApiModelProperty(example = "Enable", value = "Property display name.")
  @JsonProperty("displayName")
@Valid
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }


  /**
   * Property description.
   **/
  public PropertyRes description(String description) {
    this.description = description;
    return this;
  }

  
  @ApiModelProperty(example = "Enable account suspend notifications.", value = "Property description.")
  @JsonProperty("description")
@Valid
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }



  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PropertyRes propertyRes = (PropertyRes) o;
    return Objects.equals(name, propertyRes.name) &&
        Objects.equals(value, propertyRes.value) &&
        Objects.equals(displayName, propertyRes.displayName) &&
        Objects.equals(description, propertyRes.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value, displayName, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PropertyRes {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

