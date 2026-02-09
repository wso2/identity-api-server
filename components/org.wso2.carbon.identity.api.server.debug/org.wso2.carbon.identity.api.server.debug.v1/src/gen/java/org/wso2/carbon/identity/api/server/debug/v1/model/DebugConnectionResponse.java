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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * Debug connection response model for IdP OAuth 2.0 authentication testing
 * results.
 * Wraps the debug operation result in a 'result' object for consistent
 * structure.
 */
@ApiModel(description = "Debug connection response for IdP OAuth 2.0 authentication testing results")
public class DebugConnectionResponse {

    @ApiModelProperty(value = "Debug operation result containing sessionId, authorizationUrl, status, and metadata")
    @JsonProperty("result")
    private DebugResult result;

    /**
     * Default constructor.
     */
    public DebugConnectionResponse() {

        // Default constructor.
    }

    /**
     * Gets the debug result.
     *
     * @return Debug result.
     */
    public DebugResult getResult() {

        return result;
    }

    /**
     * Sets the debug result.
     *
     * @param result Debug result to set.
     */
    public void setResult(DebugResult result) {

        this.result = result;
    }

    /**
     * Gets the debug ID from result.
     *
     * @return Debug ID.
     */
    @JsonIgnore
    public String getDebugId() {

        return result != null ? result.getDebugId() : null;
    }

    /**
     * Sets the debug ID in result.
     *
     * @param debugId Debug ID to set.
     */
    public void setDebugId(String debugId) {

        if (this.result == null) {
            this.result = new DebugResult();
        }
        this.result.setDebugId(debugId);
    }

    /**
     * Gets the authorization URL from result.
     *
     * @return Authorization URL.
     */
    @JsonIgnore
    public String getAuthorizationUrl() {

        return result != null ? result.getAuthorizationUrl() : null;
    }

    /**
     * Sets the authorization URL in result.
     *
     * @param authorizationUrl Authorization URL to set.
     */
    public void setAuthorizationUrl(String authorizationUrl) {

        if (this.result == null) {
            this.result = new DebugResult();
        }
        this.result.setAuthorizationUrl(authorizationUrl);
    }

    /**
     * Gets the status from result.
     *
     * @return Status.
     */
    @JsonIgnore
    public String getStatus() {

        return result != null ? result.getStatus() : null;
    }

    /**
     * Sets the status in result.
     *
     * @param status Status to set.
     */
    public void setStatus(String status) {

        if (this.result == null) {
            this.result = new DebugResult();
        }
        this.result.setStatus(status);
    }

    /**
     * Gets the message from result.
     *
     * @return Message.
     */
    @JsonIgnore
    public String getMessage() {

        return result != null ? result.getMessage() : null;
    }

    /**
     * Sets the message in result.
     *
     * @param message Message to set.
     */
    public void setMessage(String message) {

        if (this.result == null) {
            this.result = new DebugResult();
        }
        this.result.setMessage(message);
    }

    /**
     * Gets the metadata from result.
     *
     * @return Metadata.
     */
    @JsonIgnore
    public Map<String, Object> getMetadata() {

        return result != null ? result.getMetadata() : null;
    }

    /**
     * Sets the metadata in result.
     *
     * @param metadata Metadata to set.
     */
    public void setMetadata(Map<String, Object> metadata) {

        if (this.result == null) {
            this.result = new DebugResult();
        }
        this.result.setMetadata(metadata);
    }

    /**
     * Gets the timestamp from result.
     *
     * @return Timestamp.
     */
    @JsonIgnore
    public Long getTimestamp() {

        return result != null ? result.getTimestamp() : null;
    }

    /**
     * Sets the timestamp in result.
     *
     * @param timestamp Timestamp to set.
     */
    public void setTimestamp(Long timestamp) {

        if (this.result == null) {
            this.result = new DebugResult();
        }
        this.result.setTimestamp(timestamp);
    }

    @Override
    public String toString() {
        
        return "DebugConnectionResponse{" +
                "result=" + result +
                '}';
    }
}
