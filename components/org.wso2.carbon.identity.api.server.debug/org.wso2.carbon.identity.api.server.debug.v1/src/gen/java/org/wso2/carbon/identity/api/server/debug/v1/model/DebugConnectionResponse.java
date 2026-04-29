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
import org.wso2.carbon.identity.api.server.debug.v1.model.DebugConnectionResponseMetadata;
import javax.validation.constraints.*;

/**
 * Debug connection response containing generic debug information and resource-specific metadata.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Debug connection response containing generic debug information and resource-specific metadata.")
public class DebugConnectionResponse  {
  
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
    private DebugConnectionResponseMetadata metadata;

    /**
    * Debug session identifier.
    **/
    public DebugConnectionResponse debugId(String debugId) {

        this.debugId = debugId;
        return this;
    }
    
    @ApiModelProperty(example = "debug-496f5a3f-0094-42f2-8188-d2baa9a1287c", value = "Debug session identifier.")
    @JsonProperty("debugId")
    @Valid
    public String getDebugId() {
        return debugId;
    }
    public void setDebugId(String debugId) {
        this.debugId = debugId;
    }

    /**
    * Status of the debug operation.
    **/
    public DebugConnectionResponse status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "SUCCESS", value = "Status of the debug operation.")
    @JsonProperty("status")
    @Valid
    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    * Generic response message.
    **/
    public DebugConnectionResponse message(String message) {

        this.message = message;
        return this;
    }
    
    @ApiModelProperty(example = "Debug session executed successfully", value = "Generic response message.")
    @JsonProperty("message")
    @Valid
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    /**
    **/
    public DebugConnectionResponse metadata(DebugConnectionResponseMetadata metadata) {

        this.metadata = metadata;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("metadata")
    @Valid
    public DebugConnectionResponseMetadata getMetadata() {
        return metadata;
    }
    public void setMetadata(DebugConnectionResponseMetadata metadata) {
        this.metadata = metadata;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DebugConnectionResponse debugConnectionResponse = (DebugConnectionResponse) o;
        return Objects.equals(this.debugId, debugConnectionResponse.debugId) &&
            Objects.equals(this.status, debugConnectionResponse.status) &&
            Objects.equals(this.message, debugConnectionResponse.message) &&
            Objects.equals(this.metadata, debugConnectionResponse.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debugId, status, message, metadata);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class DebugConnectionResponse {\n");
        
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
