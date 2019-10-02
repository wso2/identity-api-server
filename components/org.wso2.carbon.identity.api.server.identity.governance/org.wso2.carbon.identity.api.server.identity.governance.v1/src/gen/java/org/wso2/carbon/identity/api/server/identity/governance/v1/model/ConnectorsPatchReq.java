package org.wso2.carbon.identity.api.server.identity.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.PropertyReq;
import javax.validation.constraints.*;

/**
 * Governance connector property patch request.
 **/

import io.swagger.annotations.*;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.*;

@ApiModel(description = "Governance connector property patch request.")
public class ConnectorsPatchReq   {
  

@XmlType(name="OperationEnum")
@XmlEnum(String.class)
public enum OperationEnum {

    @XmlEnumValue("UPDATE") UPDATE(String.valueOf("UPDATE"));


    private String value;

    OperationEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static OperationEnum fromValue(String value) {
        for (OperationEnum b : OperationEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private OperationEnum operation;

  private List<PropertyReq> properties = new ArrayList<>();


  /**
   * Governance connector properties patch operation.
   **/
  public ConnectorsPatchReq operation(OperationEnum operation) {
    this.operation = operation;
    return this;
  }

  
  @ApiModelProperty(example = "UPDATE", required = true, value = "Governance connector properties patch operation.")
  @JsonProperty("operation")
  @Valid
  @NotNull(message = "Property operation cannot be null.")
  public OperationEnum getOperation() {
    return operation;
  }
  public void setOperation(OperationEnum operation) {
    this.operation = operation;
  }


  /**
   * Governance connector properties to patch.
   **/
  public ConnectorsPatchReq properties(List<PropertyReq> properties) {
    this.properties = properties;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Governance connector properties to patch.")
  @JsonProperty("properties")
  @Valid
  @NotNull(message = "Property properties cannot be null.")
  public List<PropertyReq> getProperties() {
    return properties;
  }
  public void setProperties(List<PropertyReq> properties) {
    this.properties = properties;
  }

  public ConnectorsPatchReq addPropertiesItem(PropertyReq propertiesItem) {
    this.properties.add(propertiesItem);
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
    ConnectorsPatchReq connectorsPatchReq = (ConnectorsPatchReq) o;
    return Objects.equals(this.operation, connectorsPatchReq.operation) &&
        Objects.equals(this.properties, connectorsPatchReq.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operation, properties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConnectorsPatchReq {\n");
    
    sb.append("    operation: ").append(toIndentedString(operation)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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

