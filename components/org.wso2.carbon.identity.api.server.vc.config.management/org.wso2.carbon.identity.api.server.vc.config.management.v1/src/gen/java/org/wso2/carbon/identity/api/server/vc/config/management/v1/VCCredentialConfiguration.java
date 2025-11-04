/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vc.config.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.Metadata;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class VCCredentialConfiguration  {
  
    private String id;
    private String identifier;
    private String configurationId;
    private String scope;
    private String format;
    private String signingAlgorithm;
    private String type;
    private Metadata metadata;
    private List<String> claims = new ArrayList<String>();

    private Integer expiresIn;

    /**
    **/
    public VCCredentialConfiguration id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "7e5f9d2a-6b5e-4df6-9b87-8a3d1a4a0c31", required = true, value = "")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Stable key; human/machine readable.
    **/
    public VCCredentialConfiguration identifier(String identifier) {

        this.identifier = identifier;
        return this;
    }
    
    @ApiModelProperty(example = "EmployeeBadge", required = true, value = "Stable key; human/machine readable.")
    @JsonProperty("identifier")
    @Valid
    @NotNull(message = "Property identifier cannot be null.")

    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
    * Published as &#x60;credential_configuration_id&#x60; in issuer metadata. Defaults to &#x60;identifier&#x60; if omitted.
    **/
    public VCCredentialConfiguration configurationId(String configurationId) {

        this.configurationId = configurationId;
        return this;
    }
    
    @ApiModelProperty(example = "EmployeeBadge", required = true, value = "Published as `credential_configuration_id` in issuer metadata. Defaults to `identifier` if omitted.")
    @JsonProperty("configurationId")
    @Valid
    @NotNull(message = "Property configurationId cannot be null.")

    public String getConfigurationId() {
        return configurationId;
    }
    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    /**
    **/
    public VCCredentialConfiguration scope(String scope) {

        this.scope = scope;
        return this;
    }
    
    @ApiModelProperty(example = "employee_badge", required = true, value = "")
    @JsonProperty("scope")
    @Valid
    @NotNull(message = "Property scope cannot be null.")

    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
    **/
    public VCCredentialConfiguration format(String format) {

        this.format = format;
        return this;
    }
    
    @ApiModelProperty(example = "jwt_vc_json", required = true, value = "")
    @JsonProperty("format")
    @Valid
    @NotNull(message = "Property format cannot be null.")

    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }

    /**
    * Single JWS algorithm
    **/
    public VCCredentialConfiguration signingAlgorithm(String signingAlgorithm) {

        this.signingAlgorithm = signingAlgorithm;
        return this;
    }
    
    @ApiModelProperty(example = "RS256", required = true, value = "Single JWS algorithm")
    @JsonProperty("signingAlgorithm")
    @Valid
    @NotNull(message = "Property signingAlgorithm cannot be null.")

    public String getSigningAlgorithm() {
        return signingAlgorithm;
    }
    public void setSigningAlgorithm(String signingAlgorithm) {
        this.signingAlgorithm = signingAlgorithm;
    }

    /**
    * Credential Type
    **/
    public VCCredentialConfiguration type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "employee_badge", required = true, value = "Credential Type")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    **/
    public VCCredentialConfiguration metadata(Metadata metadata) {

        this.metadata = metadata;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("metadata")
    @Valid
    @NotNull(message = "Property metadata cannot be null.")

    public Metadata getMetadata() {
        return metadata;
    }
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
    **/
    public VCCredentialConfiguration claims(List<String> claims) {

        this.claims = claims;
        return this;
    }
    
    @ApiModelProperty(example = "[\"givenname\",\"surname\",\"email\"]", required = true, value = "")
    @JsonProperty("claims")
    @Valid
    @NotNull(message = "Property claims cannot be null.")

    public List<String> getClaims() {
        return claims;
    }
    public void setClaims(List<String> claims) {
        this.claims = claims;
    }

    public VCCredentialConfiguration addClaimsItem(String claimsItem) {
        this.claims.add(claimsItem);
        return this;
    }

        /**
    **/
    public VCCredentialConfiguration expiresIn(Integer expiresIn) {

        this.expiresIn = expiresIn;
        return this;
    }
    
    @ApiModelProperty(example = "31536000", required = true, value = "")
    @JsonProperty("expiresIn")
    @Valid
    @NotNull(message = "Property expiresIn cannot be null.")

    public Integer getExpiresIn() {
        return expiresIn;
    }
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VCCredentialConfiguration vcCredentialConfiguration = (VCCredentialConfiguration) o;
        return Objects.equals(this.id, vcCredentialConfiguration.id) &&
            Objects.equals(this.identifier, vcCredentialConfiguration.identifier) &&
            Objects.equals(this.configurationId, vcCredentialConfiguration.configurationId) &&
            Objects.equals(this.scope, vcCredentialConfiguration.scope) &&
            Objects.equals(this.format, vcCredentialConfiguration.format) &&
            Objects.equals(this.signingAlgorithm, vcCredentialConfiguration.signingAlgorithm) &&
            Objects.equals(this.type, vcCredentialConfiguration.type) &&
            Objects.equals(this.metadata, vcCredentialConfiguration.metadata) &&
            Objects.equals(this.claims, vcCredentialConfiguration.claims) &&
            Objects.equals(this.expiresIn, vcCredentialConfiguration.expiresIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, configurationId, scope, format, signingAlgorithm, type, metadata, claims, expiresIn);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCCredentialConfiguration {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    configurationId: ").append(toIndentedString(configurationId)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
        sb.append("    signingAlgorithm: ").append(toIndentedString(signingAlgorithm)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
        sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
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

