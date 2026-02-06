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

package org.wso2.carbon.identity.api.server.debug.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

/**
 * DFDP Debug Response model containing authentication flow analysis results.
 */
@ApiModel(description = "Debug response containing authentication flow analysis and results")
public class DebugResponse {

    @JsonProperty("sessionId")
    @ApiModelProperty(value = "Debug session identifier", example = "debug-session-12345")
    private String sessionId;

    @JsonProperty("status")
    @ApiModelProperty(value = "Debug operation status", example = "SUCCESS",
                      allowableValues = "SUCCESS,FAILURE,IN_PROGRESS")
    private String status;

    @JsonProperty("targetIdp")
    @ApiModelProperty(value = "Target Identity Provider that was tested", example = "Google")
    private String targetIdp;

    @JsonProperty("authenticatorUsed")
    @ApiModelProperty(value = "Authenticator that was used", example = "GoogleOIDCAuthenticator")
    private String authenticatorUsed;

    @JsonProperty("authenticationResult")
    @ApiModelProperty(value = "Authentication result details")
    @Valid
    private AuthenticationResult authenticationResult;

    @JsonProperty("claimsAnalysis")
    @ApiModelProperty(value = "Claims processing analysis captured via event listeners")
    @Valid
    private ClaimsAnalysis claimsAnalysis;

    @JsonProperty("flowEvents")
    @ApiModelProperty(value = "Authentication flow events captured by event listeners")
    @Valid
    private List<FlowEvent> flowEvents;

    @JsonProperty("errors")
    @ApiModelProperty(value = "Any errors encountered during debug flow")
    @Valid
    private List<DebugError> errors;

    @JsonProperty("metadata")
    @ApiModelProperty(value = "Additional debug metadata")
    @Valid
    private Map<String, Object> metadata;

    // Nested classes for structured data

    /**
     * Authentication result details.
     */
    @ApiModel(description = "Authentication result details")
    public static class AuthenticationResult {
        @JsonProperty("success")
        private boolean success;

        @JsonProperty("userExists")
        private boolean userExists;

        @JsonProperty("userDetails")
        private String userDetails;

        @JsonProperty("responseTime")
        private long responseTime;

        // Getters and setters
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public boolean isUserExists() {
            return userExists;
        }
        
        public void setUserExists(boolean userExists) {
            this.userExists = userExists;
        }
        
        public String getUserDetails() {
            return userDetails;
        }
        
        public void setUserDetails(String userDetails) {
            this.userDetails = userDetails;
        }
        
        public long getResponseTime() {
            return responseTime;
        }
        
        public void setResponseTime(long responseTime) {
            this.responseTime = responseTime;
        }
    }

    /**
     * Claims analysis from event listeners.
     */
    @ApiModel(description = "Claims analysis from event listeners")
    public static class ClaimsAnalysis {
        @JsonProperty("originalRemoteClaims")
        private Map<String, String> originalRemoteClaims;

        @JsonProperty("mappedLocalClaims")
        private Map<String, String> mappedLocalClaims;

        @JsonProperty("mappingErrors")
        private List<String> mappingErrors;

        // Getters and setters
        public Map<String, String> getOriginalRemoteClaims() {
            return originalRemoteClaims;
        }
        
        public void setOriginalRemoteClaims(Map<String, String> originalRemoteClaims) {
            this.originalRemoteClaims = originalRemoteClaims;
        }
        
        public Map<String, String> getMappedLocalClaims() {
            return mappedLocalClaims;
        }
        
        public void setMappedLocalClaims(Map<String, String> mappedLocalClaims) {
            this.mappedLocalClaims = mappedLocalClaims;
        }
        
        public List<String> getMappingErrors() {
            return mappingErrors;
        }
        
        public void setMappingErrors(List<String> mappingErrors) {
            this.mappingErrors = mappingErrors;
        }
    }

    /**
     * Authentication flow event captured by event listeners.
     */
    @ApiModel(description = "Authentication flow event captured by event listeners")
    public static class FlowEvent {
        @JsonProperty("timestamp")
        private long timestamp;

        @JsonProperty("eventType")
        private String eventType;

        @JsonProperty("step")
        private String step;

        @JsonProperty("success")
        private boolean success;

        @JsonProperty("authenticator")
        private String authenticator;

        @JsonProperty("data")
        private Object data;

        // Getters and setters
        public long getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
        
        public String getEventType() {
            return eventType;
        }
        
        public void setEventType(String eventType) {
            this.eventType = eventType;
        }
        
        public String getStep() {
            return step;
        }
        
        public void setStep(String step) {
            this.step = step;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public String getAuthenticator() {
            return authenticator;
        }
        
        public void setAuthenticator(String authenticator) {
            this.authenticator = authenticator;
        }
        
        public Object getData() {
            return data;
        }
        
        public void setData(Object data) {
            this.data = data;
        }
    }

    /**
     * Debug error information.
     */
    @ApiModel(description = "Debug error information")
    public static class DebugError {
        @JsonProperty("code")
        private String code;

        @JsonProperty("message")
        private String message;

        @JsonProperty("step")
        private String step;

        // Getters and setters
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getStep() {
            return step;
        }
        
        public void setStep(String step) {
            this.step = step;
        }
    }

    // Main class getters and setters

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTargetIdp() {
        return targetIdp;
    }

    public void setTargetIdp(String targetIdp) {
        this.targetIdp = targetIdp;
    }

    public String getAuthenticatorUsed() {
        return authenticatorUsed;
    }

    public void setAuthenticatorUsed(String authenticatorUsed) {
        this.authenticatorUsed = authenticatorUsed;
    }

    public AuthenticationResult getAuthenticationResult() {
        return authenticationResult;
    }

    public void setAuthenticationResult(AuthenticationResult authenticationResult) {
        this.authenticationResult = authenticationResult;
    }

    public ClaimsAnalysis getClaimsAnalysis() {
        return claimsAnalysis;
    }

    public void setClaimsAnalysis(ClaimsAnalysis claimsAnalysis) {
        this.claimsAnalysis = claimsAnalysis;
    }

    public List<FlowEvent> getFlowEvents() {
        return flowEvents;
    }

    public void setFlowEvents(List<FlowEvent> flowEvents) {
        this.flowEvents = flowEvents;
    }

    public List<DebugError> getErrors() {
        return errors;
    }

    public void setErrors(List<DebugError> errors) {
        this.errors = errors;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
