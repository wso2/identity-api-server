/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class FapiMetadata  {
  
    private List<String> allowedSignatureAlgorithms = null;

    private List<String> allowedEncryptionAlgorithms = null;

    private ClientAuthenticationMethodMetadata tokenEndpointAuthMethod;


    /**
    **/
    public FapiMetadata allowedSignatureAlgorithms(List<String> allowedSignatureAlgorithms) {

        this.allowedSignatureAlgorithms = allowedSignatureAlgorithms;
        return this;
    }
    
    @ApiModelProperty(example = "[\"PS256\",\"ES256\"]", value = "")
    @JsonProperty("allowedSignatureAlgorithms")
    @Valid
    public List<String> getAllowedSignatureAlgorithms() {
        return allowedSignatureAlgorithms;
    }
    public void setAllowedSignatureAlgorithms(List<String> allowedSignatureAlgorithms) {
        this.allowedSignatureAlgorithms = allowedSignatureAlgorithms;
    }

    public FapiMetadata addAllowedSignatureAlgorithmsItem(String allowedSignatureAlgorithmsItem) {
        if (this.allowedSignatureAlgorithms == null) {
            this.allowedSignatureAlgorithms = new ArrayList<>();
        }
        this.allowedSignatureAlgorithms.add(allowedSignatureAlgorithmsItem);
        return this;
    }

        /**
    **/
    public FapiMetadata allowedEncryptionAlgorithms(List<String> allowedEncryptionAlgorithms) {

        this.allowedEncryptionAlgorithms = allowedEncryptionAlgorithms;
        return this;
    }
    
    @ApiModelProperty(example = "[\"RSA-OAEP\",\"RSA1_5\"]", value = "")
    @JsonProperty("allowedEncryptionAlgorithms")
    @Valid
    public List<String> getAllowedEncryptionAlgorithms() {
        return allowedEncryptionAlgorithms;
    }
    public void setAllowedEncryptionAlgorithms(List<String> allowedEncryptionAlgorithms) {
        this.allowedEncryptionAlgorithms = allowedEncryptionAlgorithms;
    }

    public FapiMetadata addAllowedEncryptionAlgorithmsItem(String allowedEncryptionAlgorithmsItem) {
        if (this.allowedEncryptionAlgorithms == null) {
            this.allowedEncryptionAlgorithms = new ArrayList<>();
        }
        this.allowedEncryptionAlgorithms.add(allowedEncryptionAlgorithmsItem);
        return this;
    }

    /**
     **/
    public FapiMetadata tokenEndpointAuthMethod(ClientAuthenticationMethodMetadata tokenEndpointAuthMethod) {

        this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
        return this;
    }

    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("tokenEndpointAuthMethod")
    @Valid
    public ClientAuthenticationMethodMetadata getTokenEndpointAuthMethod() {
        return tokenEndpointAuthMethod;
    }
    public void setTokenEndpointAuthMethod(ClientAuthenticationMethodMetadata tokenEndpointAuthMethod) {
        this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
    }
    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FapiMetadata fapiMetadata = (FapiMetadata) o;
        return Objects.equals(this.allowedSignatureAlgorithms, fapiMetadata.allowedSignatureAlgorithms) &&
            Objects.equals(this.allowedEncryptionAlgorithms, fapiMetadata.allowedEncryptionAlgorithms) &&
            Objects.equals(this.tokenEndpointAuthMethod, fapiMetadata.tokenEndpointAuthMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedSignatureAlgorithms, allowedEncryptionAlgorithms, tokenEndpointAuthMethod);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FapiMetadata {\n");
        
        sb.append("    allowedSignatureAlgorithms: ").append(toIndentedString(allowedSignatureAlgorithms)).append("\n");
        sb.append("    allowedEncryptionAlgorithms: ").append(toIndentedString(allowedEncryptionAlgorithms)).append("\n");
        sb.append("    tokenEndpointAuthMethod: ").append(toIndentedString(tokenEndpointAuthMethod)).append("\n");
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

