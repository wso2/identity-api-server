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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

@ApiModel(description = "Tenant-level OpenID for Verifiable Presentations (OpenID4VP) configuration.")
public class OpenID4VPConfiguration  {


@XmlType(name="ClientIdSchemeEnum")
@XmlEnum(String.class)
public enum ClientIdSchemeEnum {

    @XmlEnumValue("x509_san_dns") X509_SAN_DNS(String.valueOf("x509_san_dns")), @XmlEnumValue("x509_hash") X509_HASH(String.valueOf("x509_hash"));


    private String value;

    ClientIdSchemeEnum(String v) {
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
    public static ClientIdSchemeEnum fromValue(String value) {
        for (ClientIdSchemeEnum b : ClientIdSchemeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

@XmlType(name="ResponseModeEnum")
@XmlEnum(String.class)
public enum ResponseModeEnum {

    @XmlEnumValue("direct_post") DIRECT_POST(String.valueOf("direct_post")), @XmlEnumValue("direct_post.jwt") DIRECT_POST_JWT(String.valueOf("direct_post.jwt"));


    private String value;

    ResponseModeEnum(String v) {
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
    public static ResponseModeEnum fromValue(String value) {
        for (ResponseModeEnum b : ResponseModeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private ClientIdSchemeEnum clientIdScheme;
    private ResponseModeEnum responseMode;

    /**
    * The client_id_scheme used when building the Verifiable Presentation request JWT. Determines how the verifier identifies itself to the wallet.
    **/
    public OpenID4VPConfiguration clientIdScheme(ClientIdSchemeEnum clientIdScheme) {

        this.clientIdScheme = clientIdScheme;
        return this;
    }

    @ApiModelProperty(example = "x509_san_dns", value = "The client_id_scheme used when building the Verifiable Presentation request JWT. Determines how the verifier identifies itself to the wallet.")
    @JsonProperty("clientIdScheme")
    @Valid
    public ClientIdSchemeEnum getClientIdScheme() {
        return clientIdScheme;
    }
    public void setClientIdScheme(ClientIdSchemeEnum clientIdScheme) {
        this.clientIdScheme = clientIdScheme;
    }

    /**
    * The response_mode used in the Verifiable Presentation request. Use direct_post.jwt for encrypted responses or direct_post for plain JSON.
    **/
    public OpenID4VPConfiguration responseMode(ResponseModeEnum responseMode) {

        this.responseMode = responseMode;
        return this;
    }

    @ApiModelProperty(example = "direct_post", value = "The response_mode used in the Verifiable Presentation request. Use direct_post.jwt for encrypted responses or direct_post for plain JSON.")
    @JsonProperty("responseMode")
    @Valid
    public ResponseModeEnum getResponseMode() {
        return responseMode;
    }
    public void setResponseMode(ResponseModeEnum responseMode) {
        this.responseMode = responseMode;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpenID4VPConfiguration openID4VPConfiguration = (OpenID4VPConfiguration) o;
        return Objects.equals(this.clientIdScheme, openID4VPConfiguration.clientIdScheme) &&
            Objects.equals(this.responseMode, openID4VPConfiguration.responseMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientIdScheme, responseMode);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OpenID4VPConfiguration {\n");

        sb.append("    clientIdScheme: ").append(toIndentedString(clientIdScheme)).append("\n");
        sb.append("    responseMode: ").append(toIndentedString(responseMode)).append("\n");
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
