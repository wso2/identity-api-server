/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.model;

import io.swagger.annotations.ApiModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;
import javax.validation.Valid;

import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
  * Request body for starting a debug session. The connectionId is required in properties for IDP resource type.
 **/
@ApiModel(description="Request body for starting a debug session. The connectionId is required in properties for IDP resource type.")
public class DebugConnectionRequest  {
  
@XmlType(name="ResourceTypeEnum")
@XmlEnum(String.class)
public enum ResourceTypeEnum {

@XmlEnumValue("IDP") IDP(String.valueOf("IDP")), @XmlEnumValue("FRAUD_DETECTION") FRAUD_DETECTION(String.valueOf("FRAUD_DETECTION"));


    private String value;

    ResourceTypeEnum (String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static ResourceTypeEnum fromValue(String value) {
        for (ResourceTypeEnum b : ResourceTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  @ApiModelProperty(example = "IDP", required = true, value = "Type of resource to debug. Allowed values: IDP, FRAUD_DETECTION.")
 /**
   * Type of resource to debug. Allowed values: IDP, FRAUD_DETECTION.
  **/
  private ResourceTypeEnum resourceType;

  @ApiModelProperty(example = "{\"connectionId\":\"123e4567-e89b-12d3-a456-426614174000\",\"authenticatorName\":\"OpenIDConnectAuthenticator\"}", value = "Generic properties for resource debugging as key-value pairs. Maximum 50 properties allowed. Each property value can be up to 2048 characters. For IDP resource type, the connectionId property is required.")
 /**
   * Generic properties for resource debugging as key-value pairs. Maximum 50 properties allowed. Each property value can be up to 2048 characters. For IDP resource type, the connectionId property is required.
  **/
  private Map<String, String> properties = null;
 /**
   * Type of resource to debug. Allowed values: IDP, FRAUD_DETECTION.
   * @return resourceType
  **/
  @JsonProperty("resourceType")
  @NotNull
 @Size(min=1,max=50)  public String getResourceType() {
    if (resourceType == null) {
      return null;
    }
    return resourceType.value();
  }

  public void setResourceType(ResourceTypeEnum resourceType) {
    this.resourceType = resourceType;
  }

  public DebugConnectionRequest resourceType(ResourceTypeEnum resourceType) {
    this.resourceType = resourceType;
    return this;
  }

 /**
   * Generic properties for resource debugging as key-value pairs. Maximum 50 properties allowed. Each property value can be up to 2048 characters. For IDP resource type, the connectionId property is required.
   * @return properties
  **/
  @JsonProperty("properties")
 @Size(max=50)  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, String> properties) {
    this.properties = properties;
  }

  public DebugConnectionRequest properties(Map<String, String> properties) {
    this.properties = properties;
    return this;
  }

  public DebugConnectionRequest putPropertiesItem(String key, String propertiesItem) {
    this.properties.put(key, propertiesItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DebugConnectionRequest {\n");
    
    sb.append("    resourceType: ").append(toIndentedString(resourceType)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

