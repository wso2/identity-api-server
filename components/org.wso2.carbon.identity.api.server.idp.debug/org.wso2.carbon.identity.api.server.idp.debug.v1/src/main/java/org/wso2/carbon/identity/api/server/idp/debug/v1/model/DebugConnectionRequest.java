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

import javax.validation.constraints.NotNull;

/**
 * Debug connection request model for IdP authentication testing.
 * Contains username and password for authentication flow testing.
 */
@ApiModel(description = "Debug connection request for IdP authentication testing")
public class DebugConnectionRequest {

    @NotNull
    @ApiModelProperty(value = "Username for authentication test", required = true, example = "abc@gmail.com")
    @JsonProperty("username")
    private String username;

    @NotNull
    @ApiModelProperty(value = "Password for authentication test", required = true, example = "Linuka@123")
    @JsonProperty("password")
    private String password;

    @ApiModelProperty(value = "Optional authenticator name to use for testing")
    @JsonProperty("authenticatorName")
    private String authenticatorName;

    @ApiModelProperty(value = "Request timeout in seconds", example = "30")
    @JsonProperty("timeoutSeconds")
    private Integer timeoutSeconds = 30;

    /**
     * Default constructor.
     */
    public DebugConnectionRequest() {
        // Default constructor.
    }

    /**
     * Constructor with username and password.
     *
     * @param username Username for authentication test.
     * @param password Password for authentication test.
     */
    public DebugConnectionRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username.
     *
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username Username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return Password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password Password to set.
     */
    public void setPassword(String password) {
        this.password = password;
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
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                ", authenticatorName='" + authenticatorName + '\'' +
                ", timeoutSeconds=" + timeoutSeconds +
                '}';
    }
}
