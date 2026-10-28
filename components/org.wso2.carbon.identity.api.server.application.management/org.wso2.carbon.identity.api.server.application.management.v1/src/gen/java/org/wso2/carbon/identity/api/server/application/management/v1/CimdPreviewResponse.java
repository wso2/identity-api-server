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
@ApiModel(description = "Values taken from a validated Client ID Metadata Document.")
public class CimdPreviewResponse  {
  
    private String clientId;

    private String clientName;

    private String clientUri;

    private List<String> redirectUris = null;

    private String tokenEndpointAuthMethod;

    private String jwksUri;

    private List<String> grantTypes = null;

    /**
    * Client Identifier URL.
    **/
    public CimdPreviewResponse clientId(String clientId) {

        this.clientId = clientId;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Client Identifier URL.")
    @JsonProperty("clientId")
    @Valid
    @NotNull(message = "Property clientId cannot be null.")
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
    * Client name from the document, or the URL host.
    **/
    public CimdPreviewResponse clientName(String clientName) {

        this.clientName = clientName;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Client name from the document, or the URL host.")
    @JsonProperty("clientName")
    @Valid
    @NotNull(message = "Property clientName cannot be null.")
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
    * Client home page, when the document declares an https one.
    **/
    public CimdPreviewResponse clientUri(String clientUri) {

        this.clientUri = clientUri;
        return this;
    }
    
    @ApiModelProperty(value = "Client home page, when the document declares an https one.")
    @JsonProperty("clientUri")
    @Valid
    public String getClientUri() {
        return clientUri;
    }
    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    /**
    * Redirect URIs from the document.
    **/
    public CimdPreviewResponse redirectUris(List<String> redirectUris) {

        this.redirectUris = redirectUris;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Redirect URIs from the document.")
    @JsonProperty("redirectUris")
    @Valid
    @NotNull(message = "Property redirectUris cannot be null.")
    public List<String> getRedirectUris() {
        return redirectUris;
    }
    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public CimdPreviewResponse addRedirectUrisItem(String redirectUrisItem) {
        if (this.redirectUris == null) {
            this.redirectUris = new ArrayList<String>();
        }
        this.redirectUris.add(redirectUrisItem);
        return this;
    }

    /**
    * none or private_key_jwt.
    **/
    public CimdPreviewResponse tokenEndpointAuthMethod(String tokenEndpointAuthMethod) {

        this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "none or private_key_jwt.")
    @JsonProperty("tokenEndpointAuthMethod")
    @Valid
    @NotNull(message = "Property tokenEndpointAuthMethod cannot be null.")
    public String getTokenEndpointAuthMethod() {
        return tokenEndpointAuthMethod;
    }
    public void setTokenEndpointAuthMethod(String tokenEndpointAuthMethod) {
        this.tokenEndpointAuthMethod = tokenEndpointAuthMethod;
    }

    /**
    * JWKS URI, for private_key_jwt.
    **/
    public CimdPreviewResponse jwksUri(String jwksUri) {

        this.jwksUri = jwksUri;
        return this;
    }
    
    @ApiModelProperty(value = "JWKS URI, for private_key_jwt.")
    @JsonProperty("jwksUri")
    @Valid
    public String getJwksUri() {
        return jwksUri;
    }
    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

    /**
    * Grants from the document, limited to authorization_code and refresh_token.
    **/
    public CimdPreviewResponse grantTypes(List<String> grantTypes) {

        this.grantTypes = grantTypes;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Grants from the document, limited to authorization_code and refresh_token.")
    @JsonProperty("grantTypes")
    @Valid
    @NotNull(message = "Property grantTypes cannot be null.")
    public List<String> getGrantTypes() {
        return grantTypes;
    }
    public void setGrantTypes(List<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public CimdPreviewResponse addGrantTypesItem(String grantTypesItem) {
        if (this.grantTypes == null) {
            this.grantTypes = new ArrayList<String>();
        }
        this.grantTypes.add(grantTypesItem);
        return this;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CimdPreviewResponse cimdPreviewResponse = (CimdPreviewResponse) o;
        return Objects.equals(this.clientId, cimdPreviewResponse.clientId) &&
            Objects.equals(this.clientName, cimdPreviewResponse.clientName) &&
            Objects.equals(this.clientUri, cimdPreviewResponse.clientUri) &&
            Objects.equals(this.redirectUris, cimdPreviewResponse.redirectUris) &&
            Objects.equals(this.tokenEndpointAuthMethod, cimdPreviewResponse.tokenEndpointAuthMethod) &&
            Objects.equals(this.jwksUri, cimdPreviewResponse.jwksUri) &&
            Objects.equals(this.grantTypes, cimdPreviewResponse.grantTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientName, clientUri, redirectUris, tokenEndpointAuthMethod, jwksUri, grantTypes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CimdPreviewResponse {\n");
        
        sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
        sb.append("    clientName: ").append(toIndentedString(clientName)).append("\n");
        sb.append("    clientUri: ").append(toIndentedString(clientUri)).append("\n");
        sb.append("    redirectUris: ").append(toIndentedString(redirectUris)).append("\n");
        sb.append("    tokenEndpointAuthMethod: ").append(toIndentedString(tokenEndpointAuthMethod)).append("\n");
        sb.append("    jwksUri: ").append(toIndentedString(jwksUri)).append("\n");
        sb.append("    grantTypes: ").append(toIndentedString(grantTypes)).append("\n");
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

