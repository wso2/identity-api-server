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
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * A single trusted issuer configuration for a credential.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "A single trusted issuer configuration for a credential.")
public class IssuerModel  {


@XmlType(name="KeySourceTypeEnum")
@XmlEnum(String.class)
public enum KeySourceTypeEnum {

    @XmlEnumValue("x5c") X5C(String.valueOf("x5c")), @XmlEnumValue("jwks_uri") JWKS_URI(String.valueOf("jwks_uri")), @XmlEnumValue("pem") PEM(String.valueOf("pem"));


    private String value;

    KeySourceTypeEnum(String v) {
        value = v;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static KeySourceTypeEnum fromValue(String value) {
        for (KeySourceTypeEnum b : KeySourceTypeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private KeySourceTypeEnum keySourceType;
    private String issuerUrl;
    private String keySource;

    /**
    * Type of key source used to resolve the issuer&#39;s public key for signature verification. Use x5c for X.509 certificate chain validation, jwks_uri to fetch public keys from a JWKS endpoint, or pem for a directly-supplied issuer certificate.
    **/
    public IssuerModel keySourceType(KeySourceTypeEnum keySourceType) {

        this.keySourceType = keySourceType;
        return this;
    }

    @ApiModelProperty(example = "jwks_uri", required = true, value = "Type of key source used to resolve the issuer's public key for signature verification. Use x5c for X.509 certificate chain validation, jwks_uri to fetch public keys from a JWKS endpoint, or pem for a directly-supplied issuer certificate. ")
    @JsonProperty("keySourceType")
    @Valid
    @NotNull(message = "Property keySourceType cannot be null.")

    public KeySourceTypeEnum getKeySourceType() {
        return keySourceType;
    }
    public void setKeySourceType(KeySourceTypeEnum keySourceType) {
        this.keySourceType = keySourceType;
    }

    /**
    * URL that uniquely identifies the credential issuer (the iss claim value). Required for jwks_uri and pem methods. Not used for x5c.
    **/
    public IssuerModel issuerUrl(String issuerUrl) {

        this.issuerUrl = issuerUrl;
        return this;
    }

    @ApiModelProperty(example = "https://issuer.example.com", value = "URL that uniquely identifies the credential issuer (the iss claim value). Required for jwks_uri and pem methods. Not used for x5c. ")
    @JsonProperty("issuerUrl")
    @Valid
    public String getIssuerUrl() {
        return issuerUrl;
    }
    public void setIssuerUrl(String issuerUrl) {
        this.issuerUrl = issuerUrl;
    }

    /**
    * Key source for signature verification. For jwks_uri: the JWKS endpoint URL. For pem: the Base64-encoded PEM signing certificate of the issuer. For x5c: the Base64-encoded PEM trusted root CA certificate.
    **/
    public IssuerModel keySource(String keySource) {

        this.keySource = keySource;
        return this;
    }

    @ApiModelProperty(example = "https://issuer.example.com/.well-known/jwks.json", value = "Key source for signature verification. For jwks_uri: the JWKS endpoint URL. For pem: the Base64-encoded PEM signing certificate of the issuer. For x5c: the Base64-encoded PEM trusted root CA certificate. ")
    @JsonProperty("keySource")
    @Valid
    public String getKeySource() {
        return keySource;
    }
    public void setKeySource(String keySource) {
        this.keySource = keySource;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssuerModel issuerModel = (IssuerModel) o;
        return Objects.equals(this.keySourceType, issuerModel.keySourceType) &&
            Objects.equals(this.issuerUrl, issuerModel.issuerUrl) &&
            Objects.equals(this.keySource, issuerModel.keySource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keySourceType, issuerUrl, keySource);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class IssuerModel {\n");

        sb.append("    keySourceType: ").append(toIndentedString(keySourceType)).append("\n");
        sb.append("    issuerUrl: ").append(toIndentedString(issuerUrl)).append("\n");
        sb.append("    keySource: ").append(toIndentedString(keySource)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

