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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * DFDP Debug Request model for initiating debug authentication flows.
 */
@ApiModel(description = "Debug request for DFDP authentication flow testing")
public class DebugRequest {

    @JsonProperty("targetIdp")
    @ApiModelProperty(value = "Target Identity Provider ID or name", required = true, example = "Google")
    @NotNull(message = "Target IdP is required")
    private String targetIdp;

    @JsonProperty("targetAuthenticator")
    @ApiModelProperty(value = "Specific authenticator to test", example = "GoogleOIDCAuthenticator")
    private String targetAuthenticator;

    @JsonProperty("testUser")
    @ApiModelProperty(value = "Test user identifier", example = "testuser@gmail.com")
    private String testUser;

    @JsonProperty("testClaims")
    @ApiModelProperty(value = "Expected claims for validation")
    @Valid
    private Map<String, String> testClaims;

    @JsonProperty("debugMode")
    @ApiModelProperty(value = "Debug mode level", 
        example = "DETAILED", 
        allowableValues = "BASIC,DETAILED,COMPREHENSIVE")
    private String debugMode = "DETAILED";

    @JsonProperty("enableEventCapture")
    @ApiModelProperty(value = "Enable event listener-based claim capture", example = "true")
    private Boolean enableEventCapture = true;

    @JsonProperty("analysisConfig")
    @ApiModelProperty(value = "Analysis configuration parameters")
    @Valid
    private Map<String, Object> analysisConfig;

    // Getters and Setters

    public String getTargetIdp() {

        return targetIdp;
    }

    public void setTargetIdp(String targetIdp) {

        this.targetIdp = targetIdp;
    }

    public String getTargetAuthenticator() {

        return targetAuthenticator;
    }

    public void setTargetAuthenticator(String targetAuthenticator) {

        this.targetAuthenticator = targetAuthenticator;
    }

    public String getTestUser() {

        return testUser;
    }

    public void setTestUser(String testUser) {

        this.testUser = testUser;
    }

    public Map<String, String> getTestClaims() {

        return testClaims;
    }

    public void setTestClaims(Map<String, String> testClaims) {

        this.testClaims = testClaims;
    }

    public String getDebugMode() {

        return debugMode;
    }

    public void setDebugMode(String debugMode) {

        this.debugMode = debugMode;
    }

    public Boolean getEnableEventCapture() {

        return enableEventCapture;
    }

    public void setEnableEventCapture(Boolean enableEventCapture) {

        this.enableEventCapture = enableEventCapture;
    }

    public Map<String, Object> getAnalysisConfig() {

        return analysisConfig;
    }

    public void setAnalysisConfig(Map<String, Object> analysisConfig) {

        this.analysisConfig = analysisConfig;
    }

    @Override
    public String toString() {
        
        return "DebugRequest{" +
                "targetIdp='" + targetIdp + '\'' +
                ", targetAuthenticator='" + targetAuthenticator + '\'' +
                ", testUser='" + testUser + '\'' +
                ", debugMode='" + debugMode + '\'' +
                ", enableEventCapture=" + enableEventCapture +
                '}';
    }
}
