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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Per-issuer signature verification configuration for a credential.
 */
@ApiModel(description = "Per-issuer signature verification configuration")
public class IssuerConfigModel {

    @NotNull
    private String keySourceType;
    private String issuerUrl;
    private String keySource;

    @ApiModelProperty(required = true,
            value = "Key source type for issuer signature verification. One of: x5c, jwks_uri, pem.")
    @JsonProperty("keySourceType")
    public String getKeySourceType() {

        return keySourceType;
    }

    public void setKeySourceType(String keySourceType) {

        this.keySourceType = keySourceType;
    }

    @ApiModelProperty(
            value = "Expected issuer URL (iss claim). Required for jwks_uri and pem methods.")
    @JsonProperty("issuerUrl")
    public String getIssuerUrl() {

        return issuerUrl;
    }

    public void setIssuerUrl(String issuerUrl) {

        this.issuerUrl = issuerUrl;
    }

    @ApiModelProperty(
            value = "Key source for signature verification. For jwks_uri: the JWKS endpoint URL." +
                    " For pem/x5c: the Base64-encoded PEM certificate" +
                    " (issuer signing cert for pem, trusted root CA cert for x5c).")
    @JsonProperty("keySource")
    public String getKeySource() {

        return keySource;
    }

    public void setKeySource(String keySource) {

        this.keySource = keySource;
    }
}
