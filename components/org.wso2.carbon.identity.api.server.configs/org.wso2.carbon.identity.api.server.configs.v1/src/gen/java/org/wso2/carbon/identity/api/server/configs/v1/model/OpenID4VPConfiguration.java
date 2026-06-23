/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

import javax.validation.Valid;

/**
 * OpenID4VP tenant-level configuration.
 */
public class OpenID4VPConfiguration {

    private String clientIdScheme;
    private String clientId;
    private String responseMode;
    private String registrationCertificate;
    private Boolean rejectVcWithoutStatusClaim;

    public OpenID4VPConfiguration clientIdScheme(String clientIdScheme) {

        this.clientIdScheme = clientIdScheme;
        return this;
    }

    @ApiModelProperty(example = "x509_san_dns",
            value = "The client_id_scheme used when building the VP request JWT.")
    @JsonProperty("clientIdScheme")
    @Valid
    public String getClientIdScheme() {
        return clientIdScheme;
    }

    public void setClientIdScheme(String clientIdScheme) {
        this.clientIdScheme = clientIdScheme;
    }

    public OpenID4VPConfiguration clientId(String clientId) {

        this.clientId = clientId;
        return this;
    }

    @ApiModelProperty(example = "x509_san_dns:myserver.example.com",
            value = "Optional override for the client_id sent in VP requests. "
                    + "Leave blank to auto-derive from the client ID scheme.")
    @JsonProperty("clientId")
    @Valid
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public OpenID4VPConfiguration responseMode(String responseMode) {

        this.responseMode = responseMode;
        return this;
    }

    @ApiModelProperty(example = "direct_post",
            value = "The response_mode used in the VP request (direct_post or direct_post.jwt).")
    @JsonProperty("responseMode")
    @Valid
    public String getResponseMode() {
        return responseMode;
    }

    public void setResponseMode(String responseMode) {
        this.responseMode = responseMode;
    }

    public OpenID4VPConfiguration registrationCertificate(String registrationCertificate) {

        this.registrationCertificate = registrationCertificate;
        return this;
    }

    @ApiModelProperty(value = "Optional verifier_attestation JWT for wallet registration.")
    @JsonProperty("registrationCertificate")
    @Valid
    public String getRegistrationCertificate() {
        return registrationCertificate;
    }

    public void setRegistrationCertificate(String registrationCertificate) {
        this.registrationCertificate = registrationCertificate;
    }

    public OpenID4VPConfiguration rejectVcWithoutStatusClaim(Boolean rejectVcWithoutStatusClaim) {

        this.rejectVcWithoutStatusClaim = rejectVcWithoutStatusClaim;
        return this;
    }

    @ApiModelProperty(value = "When true, VCs that do not carry a status claim are rejected during verification.")
    @JsonProperty("rejectVcWithoutStatusClaim")
    @Valid
    public Boolean getRejectVcWithoutStatusClaim() {
        return rejectVcWithoutStatusClaim;
    }

    public void setRejectVcWithoutStatusClaim(Boolean rejectVcWithoutStatusClaim) {
        this.rejectVcWithoutStatusClaim = rejectVcWithoutStatusClaim;
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpenID4VPConfiguration that = (OpenID4VPConfiguration) o;
        return Objects.equals(this.clientIdScheme, that.clientIdScheme)
                && Objects.equals(this.clientId, that.clientId)
                && Objects.equals(this.responseMode, that.responseMode)
                && Objects.equals(this.registrationCertificate, that.registrationCertificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientIdScheme, clientId, responseMode, registrationCertificate);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OpenID4VPConfiguration {\n");
        sb.append("    clientIdScheme: ").append(clientIdScheme).append("\n");
        sb.append("    clientId: ").append(clientId).append("\n");
        sb.append("    responseMode: ").append(responseMode).append("\n");
        sb.append("    registrationCertificate: ").append("[REDACTED]").append("\n");
        sb.append("}");
        return sb.toString();
    }
}
