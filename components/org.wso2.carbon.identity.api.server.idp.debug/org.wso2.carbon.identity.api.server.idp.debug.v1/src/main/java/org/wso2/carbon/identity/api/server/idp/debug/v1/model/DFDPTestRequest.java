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
 * DFDP test request model.
 */
@ApiModel(description = "Request model for DFDP authentication testing")
public class DFDPTestRequest {

    @ApiModelProperty(value = "Identity provider name", required = true)
    @JsonProperty("idpName")
    private String idpName;

    @ApiModelProperty(value = "Authenticator name (optional)")
    @JsonProperty("authenticatorName")
    private String authenticatorName;

    @ApiModelProperty(value = "Response format", allowableValues = "json,html,text,summary")
    @JsonProperty("format")
    private String format = "json";

    @ApiModelProperty(value = "Test parameters")
    @JsonProperty("testParameters")
    private Map<String, Object> testParameters;

    @ApiModelProperty(value = "Timeout in seconds")
    @JsonProperty("timeoutSeconds")
    private Integer timeoutSeconds = 30;

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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Map<String, Object> getTestParameters() {
        return testParameters;
    }

    public void setTestParameters(Map<String, Object> testParameters) {
        this.testParameters = testParameters;
    }

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public String toString() {
        return "DFDPTestRequest{" +
                "idpName='" + idpName + '\'' +
                ", authenticatorName='" + authenticatorName + '\'' +
                ", format='" + format + '\'' +
                ", testParameters=" + testParameters +
                ", timeoutSeconds=" + timeoutSeconds +
                '}';
    }
}
