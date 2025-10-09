/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.idp.debug.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * Debug connection response model for IdP OAuth 2.0 authentication testing results.
 */
@ApiModel(description = "Debug connection response for IdP OAuth 2.0 authentication testing results")
public class DebugConnectionResponse {

    @ApiModelProperty(value = "Debug session ID", example = "debug-session-12345")
    @JsonProperty("sessionId")
    private String sessionId;

    @ApiModelProperty(value = "OAuth 2.0 authorization URL for user authentication", 
                      example = "https://accounts.google.com/oauth/authorize?response_type=code&client_id=123"
                               + "&redirect_uri=https%3A%2F%2Flocalhost%3A9443%2Fcommonauth"
                               + "&scope=openid+profile+email&state=debug-session-12345"
                               + "&code_challenge=abc123&code_challenge_method=S256")
    @JsonProperty("authorizationUrl")
    private String authorizationUrl;

    @ApiModelProperty(value = "Status of the debug operation", example = "URL_GENERATED")
    @JsonProperty("status")
    private String status;

    @ApiModelProperty(value = "Response message", example = "OAuth 2.0 authorization URL generated successfully")
    @JsonProperty("message")
    private String message;

    @ApiModelProperty(value = "Additional metadata about the debug operation")
    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    /**
     * Default constructor.
     */
    public DebugConnectionResponse() {
        // Default constructor.
    }

    /**
     * Gets the session ID.
     *
     * @return Session ID.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the session ID.
     *
     * @param sessionId Session ID to set.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Gets the OAuth 2.0 authorization URL.
     *
     * @return Authorization URL.
     */
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    /**
     * Sets the OAuth 2.0 authorization URL.
     *
     * @param authorizationUrl Authorization URL to set.
     */
    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
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
                "sessionId='" + sessionId + '\'' +
                ", authorizationUrl='" + authorizationUrl + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
