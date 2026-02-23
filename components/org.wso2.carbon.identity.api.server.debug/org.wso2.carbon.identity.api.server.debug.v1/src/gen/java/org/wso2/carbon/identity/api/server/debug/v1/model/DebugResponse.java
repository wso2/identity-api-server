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
  * Debug response containing sessionId and success at top level, with all protocol-specific data in metadata.
 **/
@ApiModel(description="Debug response containing sessionId and success at top level, with all protocol-specific data in metadata.")
public class DebugResponse  {
  
  @ApiModelProperty(example = "debug-session-12345", required = true, value = "Debug session identifier. Used to retrieve debug results via the GET endpoint.")
 /**
   * Debug session identifier. Used to retrieve debug results via the GET endpoint.
  **/
  private String sessionId;

  @ApiModelProperty(example = "true", required = true, value = "Whether the debug operation was successful.")
 /**
   * Whether the debug operation was successful.
  **/
  private Boolean success;

  @ApiModelProperty(example = "{\"state\":\"debug-session-12345\",\"userAttributes\":{\"sub\":\"9d5ddf10-d814-4000-9bf7-35f3eef9b86e\",\"email\":\"user@example.com\"},\"mappedClaims\":[{\"idpClaim\":\"sub\",\"isClaim\":\"http://wso2.org/claims/sub\",\"value\":\"9d5ddf10-d814-4000-9bf7-35f3eef9b86e\",\"status\":\"Auto-Discovered\"}],\"steps\":{\"claimMappingStatus\":\"success\",\"authenticationStatus\":\"success\",\"connectionStatus\":\"success\"},\"idToken\":\"eyJ...\",\"externalRedirectUrl\":\"https://...\"}", value = "Protocol-specific and resource-specific debug data. For IDP OAuth debugging, includes userAttributes, mappedClaims, steps, tokens, URLs, and diagnostic information.")
 /**
   * Protocol-specific and resource-specific debug data. For IDP OAuth debugging, includes userAttributes, mappedClaims, steps, tokens, URLs, and diagnostic information.
  **/
  private Map<String, Object> metadata = null;
 /**
   * Debug session identifier. Used to retrieve debug results via the GET endpoint.
   * @return sessionId
  **/
  @JsonProperty("sessionId")
  @NotNull
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public DebugResponse sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

 /**
   * Whether the debug operation was successful.
   * @return success
  **/
  @JsonProperty("success")
  @NotNull
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public DebugResponse success(Boolean success) {
    this.success = success;
    return this;
  }

 /**
   * Protocol-specific and resource-specific debug data. For IDP OAuth debugging, includes userAttributes, mappedClaims, steps, tokens, URLs, and diagnostic information.
   * @return metadata
  **/
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  public DebugResponse metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public DebugResponse putMetadataItem(String key, Object metadataItem) {
    this.metadata.put(key, metadataItem);
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DebugResponse {\n");
    
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
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

