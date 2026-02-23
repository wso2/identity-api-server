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

public class Error  {
  
  @ApiModelProperty(example = "IDP-60001", required = true, value = "An error code.")
 /**
   * An error code.
  **/
  private String code;

  @ApiModelProperty(example = "Error message.", required = true, value = "An error message.")
 /**
   * An error message.
  **/
  private String message;

  @ApiModelProperty(example = "Detailed error description.", value = "A detailed error description.")
 /**
   * A detailed error description.
  **/
  private String description;

  @ApiModelProperty(example = "trace-123456", value = "Trace identifier for error correlation.")
 /**
   * Trace identifier for error correlation.
  **/
  private String traceId;
 /**
   * An error code.
   * @return code
  **/
  @JsonProperty("code")
  @NotNull
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Error code(String code) {
    this.code = code;
    return this;
  }

 /**
   * An error message.
   * @return message
  **/
  @JsonProperty("message")
  @NotNull
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Error message(String message) {
    this.message = message;
    return this;
  }

 /**
   * A detailed error description.
   * @return description
  **/
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Error description(String description) {
    this.description = description;
    return this;
  }

 /**
   * Trace identifier for error correlation.
   * @return traceId
  **/
  @JsonProperty("traceId")
  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public Error traceId(String traceId) {
    this.traceId = traceId;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
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

