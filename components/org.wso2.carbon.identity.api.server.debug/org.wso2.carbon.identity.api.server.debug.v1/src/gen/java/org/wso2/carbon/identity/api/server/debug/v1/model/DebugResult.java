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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.*;

/**
 * Debug response containing debugId and status at top level, with all protocol-specific data in metadata.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Debug response containing debugId and status at top level, with all protocol-specific data in metadata.")
public class DebugResult  {
  
    private String debugId;

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("SUCCESS") SUCCESS(String.valueOf("SUCCESS")), @XmlEnumValue("IN_PROGRESS") IN_PROGRESS(String.valueOf("IN_PROGRESS")), @XmlEnumValue("FAILURE") FAILURE(String.valueOf("FAILURE")), @XmlEnumValue("DIRECT_RESULT") DIRECT_RESULT(String.valueOf("DIRECT_RESULT"));

    private String value;

    StatusEnum(String v) {
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

    private StatusEnum status;
    private String message;
    private Map<String, Object> metadata = null;

    /**
    * Debug session identifier. Used to retrieve debug results via the GET endpoint.
    **/
    public DebugResult debugId(String debugId) {

        this.debugId = debugId;
        return this;
    }
    
    @ApiModelProperty(example = "debug-12345", required = true, value = "Debug session identifier. Used to retrieve debug results via the GET endpoint.")
    @JsonProperty("debugId")
    @Valid
    @NotNull(message = "Property debugId cannot be null.")

    public String getDebugId() {
        return debugId;
    }
    public void setDebugId(String debugId) {
        this.debugId = debugId;
    }

    /**
    * Status of the debug operation.
    **/
    public DebugResult status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "SUCCESS", required = true, value = "Status of the debug operation.")
    @JsonProperty("status")
    @Valid
    @NotNull(message = "Property status cannot be null.")

    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    * Generic response message.
    **/
    public DebugResult message(String message) {

        this.message = message;
        return this;
    }
    
    @ApiModelProperty(example = "Debug session retrieved successfully.", value = "Generic response message.")
    @JsonProperty("message")
    @Valid
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
    * Protocol-specific and resource-specific debug data. For IDP OAuth debugging, includes userAttributes, mappedClaims, steps, tokens, URLs, and diagnostic information.
    **/
    public DebugResult metadata(Map<String, Object> metadata) {

        this.metadata = metadata;
        return this;
    }
    
    @ApiModelProperty(example = "{\"state\":\"debug-12345\",\"userAttributes\":{\"sub\":\"9d5ddf10-d814-4000-9bf7-35f3eef9b86e\",\"email\":\"user@example.com\"},\"mappedClaims\":[{\"idpClaim\":\"sub\",\"isClaim\":\"http://wso2.org/claims/sub\",\"value\":\"9d5ddf10-d814-4000-9bf7-35f3eef9b86e\",\"status\":\"Auto-Discovered\"}],\"steps\":{\"claimMappingStatus\":\"success\",\"authenticationStatus\":\"success\",\"connectionStatus\":\"success\"},\"idToken\":\"eyJ...\",\"externalRedirectUrl\":\"https://...\"}", value = "Protocol-specific and resource-specific debug data. For IDP OAuth debugging, includes userAttributes, mappedClaims, steps, tokens, URLs, and diagnostic information.")
    @JsonProperty("metadata")
    @Valid
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public DebugResult putMetadataItem(String key, Object metadataItem) {
        if (this.metadata == null) {
            this.metadata = new HashMap<String, Object>();
        }
        this.metadata.put(key, metadataItem);
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
        DebugResult debugResult = (DebugResult) o;
        return Objects.equals(this.debugId, debugResult.debugId) &&
            Objects.equals(this.status, debugResult.status) &&
            Objects.equals(this.message, debugResult.message) &&
            Objects.equals(this.metadata, debugResult.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debugId, status, message, metadata);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DebugResult {\n");
        
        sb.append("    debugId: ").append(toIndentedString(debugId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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
        return o.toString().replace("\n", "\n");
    }
}
