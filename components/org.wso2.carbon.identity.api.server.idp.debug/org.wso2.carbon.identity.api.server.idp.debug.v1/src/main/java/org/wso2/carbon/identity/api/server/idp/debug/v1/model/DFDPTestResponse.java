/*
 * Copyright (c) 2019-2025, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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
 * DFDP test response model.
 */
@ApiModel(description = "Response model for DFDP authentication testing")
public class DFDPTestResponse {

    @ApiModelProperty(value = "Request ID")
    @JsonProperty("requestId")
    private String requestId;

    @ApiModelProperty(value = "Authentication status")
    @JsonProperty("status")
    private String status;

    @ApiModelProperty(value = "Identity provider name")
    @JsonProperty("idpName")
    private String idpName;

    @ApiModelProperty(value = "Authenticator name")
    @JsonProperty("authenticatorName")
    private String authenticatorName;

    @ApiModelProperty(value = "Claims extracted from authentication")
    @JsonProperty("claims")
    private Map<String, Object> claims;

    @ApiModelProperty(value = "Authentication metadata")
    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    @ApiModelProperty(value = "Response timestamp")
    @JsonProperty("timestamp")
    private Long timestamp;

    @ApiModelProperty(value = "Error message if any")
    @JsonProperty("error")
    private String error;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdpName() {
        return idpName;
    }

    public void setIdpName(String idpName) {
        this.idpName = idpName;
    }

    public String getAuthenticatorName() {
        return authenticatorName;
    }

    public void setAuthenticatorName(String authenticatorName) {
        this.authenticatorName = authenticatorName;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, Object> claims) {
        this.claims = claims;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "DFDPTestResponse{" +
                "requestId='" + requestId + '\'' +
                ", status='" + status + '\'' +
                ", idpName='" + idpName + '\'' +
                ", authenticatorName='" + authenticatorName + '\'' +
                ", claims=" + claims +
                ", metadata=" + metadata +
                ", timestamp=" + timestamp +
                ", error='" + error + '\'' +
                '}';
    }
}
