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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * Debug connection response model for debug operation results.
 * Contains generic debug information and resource-specific metadata.
 */
@ApiModel(description = "Debug connection response containing generic debug information and resource-specific metadata")
public class DebugConnectionResponse {

    @ApiModelProperty(value = "Debug session ID", example = "debug-496f5a3f-0094-42f2-8188-d2baa9a1287c")
    @JsonProperty("debugId")
    private String debugId;

    @ApiModelProperty(value = "Status of the debug operation", example = "SUCCESS")
    @JsonProperty("status")
    private String status;

    @ApiModelProperty(value = "Response message", example = "OAuth 2.0 authorization URL generated successfully")
    @JsonProperty("message")
    private String message;

    @ApiModelProperty(value = "Timestamp when the debug operation was processed (in milliseconds)", 
        example = "1771217107937")
    @JsonProperty("timestamp")
    private Long timestamp;

    @ApiModelProperty(value = "Resource-specific metadata")
    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    /**
     * Default constructor.
     */
    public DebugConnectionResponse() {

        // Default constructor.
    }

    /**
     * Gets the debug ID.
     *
     * @return Debug ID.
     */
    public String getDebugId() {

        return debugId;
    }

    /**
     * Sets the debug ID.
     *
     * @param debugId Debug ID to set.
     */
    public void setDebugId(String debugId) {

        this.debugId = debugId;
    }

    /**
     * Gets the status.
     *
     * @return Status.
     */
    public String getStatus() {

        return status;
    }

    /**
     * Sets the status.
     *
     * @param status Status to set.
     */
    public void setStatus(String status) {

        this.status = status;
    }

    /**
     * Gets the message.
     *
     * @return Message.
     */
    public String getMessage() {

        return message;
    }

    /**
     * Sets the message.
     *
     * @param message Message to set.
     */
    public void setMessage(String message) {

        this.message = message;
    }

    /**
     * Gets the timestamp.
     *
     * @return Timestamp.
     */
    public Long getTimestamp() {

        return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp Timestamp to set.
     */
    public void setTimestamp(Long timestamp) {

        this.timestamp = timestamp;
    }

    /**
     * Gets the metadata.
     *
     * @return Metadata.
     */
    public Map<String, Object> getMetadata() {

        return metadata;
    }

    /**
     * Sets the metadata.
     *
     * @param metadata Metadata to set.
     */
    public void setMetadata(Map<String, Object> metadata) {

        this.metadata = metadata;
    }

    @Override
    public String toString() {
        
        return "DebugConnectionResponse{" +
                "debugId='" + debugId + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", metadata=" + metadata +
                '}';
    }
}
