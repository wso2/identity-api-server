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

package org.wso2.carbon.identity.api.server.vp.verification.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Request body for initiating a VP verification session.
 */
@ApiModel(description = "Request body for initiating a VP verification session.")
public class VerificationInitiateRequest {

    private String presentationDefinitionId;
    private String clientIdScheme;
    private String responseMode;
    private String registrationCert;

    @ApiModelProperty(required = true,
            value = "ID of the presentation definition the verifier wants the wallet to satisfy.")
    @JsonProperty("presentationDefinitionId")
    public String getPresentationDefinitionId() {
        return presentationDefinitionId;
    }

    public void setPresentationDefinitionId(String presentationDefinitionId) {
        this.presentationDefinitionId = presentationDefinitionId;
    }

    @ApiModelProperty(value = "Client ID scheme: redirect_uri, x509_san_dns, or x509_hash. "
            + "Defaults to x509_san_dns.")
    @JsonProperty("clientIdScheme")
    public String getClientIdScheme() {
        return clientIdScheme;
    }

    public void setClientIdScheme(String clientIdScheme) {
        this.clientIdScheme = clientIdScheme;
    }

    @ApiModelProperty(value = "Response mode: direct_post or direct_post.jwt. Defaults to direct_post.")
    @JsonProperty("responseMode")
    public String getResponseMode() {
        return responseMode;
    }

    public void setResponseMode(String responseMode) {
        this.responseMode = responseMode;
    }

    @ApiModelProperty(value = "PEM-encoded X.509 certificate to include in the x5c header of the request JWT. "
            + "Required for x509_san_dns and x509_hash schemes.")
    @JsonProperty("registrationCert")
    public String getRegistrationCert() {
        return registrationCert;
    }

    public void setRegistrationCert(String registrationCert) {
        this.registrationCert = registrationCert;
    }
}
