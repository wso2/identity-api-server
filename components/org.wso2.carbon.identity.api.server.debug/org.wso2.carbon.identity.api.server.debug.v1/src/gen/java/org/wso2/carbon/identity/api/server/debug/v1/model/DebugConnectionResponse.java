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
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponseMetadata;
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
  * Debug connection response containing generic debug information and resource-specific metadata.
 **/
@ApiModel(description="Debug connection response containing generic debug information and resource-specific metadata.")
public class DebugConnectionResponse  {
  
  @ApiModelProperty(example = "debug-496f5a3f-0094-42f2-8188-d2baa9a1287c", value = "Debug session ID.")
 /**
   * Debug session ID.
  **/
  private String debugId;

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

@XmlEnumValue("SUCCESS") SUCCESS(String.valueOf("SUCCESS")), @XmlEnumValue("IN_PROGRESS") IN_PROGRESS(String.valueOf("IN_PROGRESS")), @XmlEnumValue("FAILURE") FAILURE(String.valueOf("FAILURE")), @XmlEnumValue("DIRECT_RESULT") DIRECT_RESULT(String.valueOf("DIRECT_RESULT"));


    private String value;

    StatusEnum (String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
        for (StatusEnum b : StatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  @ApiModelProperty(example = "SUCCESS", value = "Status of the debug operation.")
 /**
   * Status of the debug operation.
  **/
  private StatusEnum status;

  @ApiModelProperty(example = "Debug session executed successfully", value = "Generic response message.")
 /**
   * Generic response message.
  **/
  private String message;

  @ApiModelProperty(example = "1771217107937", value = "Timestamp when the debug operation was processed (in milliseconds).")
 /**
   * Timestamp when the debug operation was processed (in milliseconds).
  **/
  private Long timestamp;

  @ApiModelProperty(value = "")
  @Valid
  private DebugConnectionResponseMetadata metadata;
 /**
   * Debug session ID.
   * @return debugId
  **/
  @JsonProperty("debugId")
  public String getDebugId() {
    return debugId;
  }

  public void setDebugId(String debugId) {
    this.debugId = debugId;
  }

  public DebugConnectionResponse debugId(String debugId) {
    this.debugId = debugId;
    return this;
  }

 /**
   * Status of the debug operation.
   * @return status
  **/
  @JsonProperty("status")
  public String getStatus() {
    if (status == null) {
      return null;
    }
    return status.value();
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public DebugConnectionResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

 /**
   * Generic response message.
   * @return message
  **/
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public DebugConnectionResponse message(String message) {
    this.message = message;
    return this;
  }

 /**
   * Timestamp when the debug operation was processed (in milliseconds).
   * @return timestamp
  **/
  @JsonProperty("timestamp")
  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  public DebugConnectionResponse timestamp(Long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

 /**
   * Get metadata
   * @return metadata
  **/
  @JsonProperty("metadata")
  public DebugConnectionResponseMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(DebugConnectionResponseMetadata metadata) {
    this.metadata = metadata;
  }

  public DebugConnectionResponse metadata(DebugConnectionResponseMetadata metadata) {
    this.metadata = metadata;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DebugConnectionResponse {\n");
    
    sb.append("    debugId: ").append(toIndentedString(debugId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

