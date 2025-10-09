/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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



/**
 * Debug connection request model for IdP OAuth 2.0 authentication testing.
 * Contains parameters for OAuth 2.0 authorization URL generation.
 */
@ApiModel(description = "Debug connection request for IdP OAuth 2.0 authentication testing")
public class DebugConnectionRequest {

    @ApiModelProperty(value = "Optional authenticator name to use for testing")
    @JsonProperty("authenticatorName")
    private String authenticatorName;

    @ApiModelProperty(value = "Custom redirect URI for OAuth 2.0 callback (optional)")
    @JsonProperty("redirectUri")
    private String redirectUri;

    @ApiModelProperty(value = "Custom OAuth 2.0 scope (optional)", example = "openid profile email")
    @JsonProperty("scope")
    private String scope;

    @ApiModelProperty(value = "Request timeout in seconds", example = "30")
    @JsonProperty("timeoutSeconds")
    private Integer timeoutSeconds = 30;

    @ApiModelProperty(value = "Additional OAuth 2.0 parameters as key-value pairs")
    @JsonProperty("additionalParams")
    private java.util.Map<String, String> additionalParams;

    /**
     * Default constructor.
     */
    public DebugConnectionRequest() {
        // Default constructor.
    }

    /**
     * Constructor with authenticator name.
     *
     * @param authenticatorName Authenticator name for OAuth 2.0 test.
     */
    public DebugConnectionRequest(String authenticatorName) {
        this.authenticatorName = authenticatorName;
    }

    /**
     * Gets the redirect URI.
     *
     * @return Redirect URI.
     */
    public String getRedirectUri() {
        return redirectUri;
    }

    /**
     * Sets the redirect URI.
     *
     * @param redirectUri Redirect URI to set.
     */
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    /**
     * Gets the OAuth 2.0 scope.
     *
     * @return OAuth 2.0 scope.
     */
    public String getScope() {
        return scope;
    }

    /**
     * Sets the OAuth 2.0 scope.
     *
     * @param scope OAuth 2.0 scope to set.
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Gets the additional OAuth 2.0 parameters.
     *
     * @return Additional OAuth 2.0 parameters.
     */
    public java.util.Map<String, String> getAdditionalParams() {
        return additionalParams;
    }

    /**
     * Sets the additional OAuth 2.0 parameters.
     *
     * @param additionalParams Additional OAuth 2.0 parameters to set.
     */
    public void setAdditionalParams(java.util.Map<String, String> additionalParams) {
        this.additionalParams = additionalParams;
    }

    /**
     * Gets the authenticator name.
     *
     * @return Authenticator name.
     */
    public String getAuthenticatorName() {
        return authenticatorName;
    }

    /**
     * Sets the authenticator name.
     *
     * @param authenticatorName Authenticator name to set.
     */
    public void setAuthenticatorName(String authenticatorName) {
        this.authenticatorName = authenticatorName;
    }

    /**
     * Gets the timeout in seconds.
     *
     * @return Timeout in seconds.
     */
    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    /**
     * Sets the timeout in seconds.
     *
     * @param timeoutSeconds Timeout in seconds to set.
     */
    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public String toString() {
        return "DebugConnectionRequest{" +
                "authenticatorName='" + authenticatorName + '\'' +
                ", redirectUri='" + redirectUri + '\'' +
                ", scope='" + scope + '\'' +
                ", timeoutSeconds=" + timeoutSeconds +
                ", additionalParams=" + additionalParams +
                '}';
    }
}
