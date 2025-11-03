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

public class VCCredentialConfigurationCreationModel  {
  
    private String identifier;
    private String configurationId;
    private String scope;
    private String format;
    private String signingAlgorithm;
    private String type;
    private Metadata metadata;
    private List<String> claims = new ArrayList<String>();

    private Integer expiryIn;

    /**
    **/
    public VCCredentialConfigurationCreationModel identifier(String identifier) {

        this.identifier = identifier;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
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
    * If not provided, server sets to &#x60;identifier&#x60;.
    **/
    public VCCredentialConfigurationCreationModel configurationId(String configurationId) {

        this.configurationId = configurationId;
        return this;
    }
    
    @ApiModelProperty(value = "If not provided, server sets to `identifier`.")
    @JsonProperty("configurationId")
    @Valid
    public String getConfigurationId() {
        return configurationId;
    }
    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    /**
    **/
    public VCCredentialConfigurationCreationModel scope(String scope) {

        this.scope = scope;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
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
    public VCCredentialConfigurationCreationModel format(String format) {

        this.format = format;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
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
    **/
    public VCCredentialConfigurationCreationModel signingAlgorithm(String signingAlgorithm) {

        this.signingAlgorithm = signingAlgorithm;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
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
    **/
    public VCCredentialConfigurationCreationModel type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
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
    public VCCredentialConfigurationCreationModel metadata(Metadata metadata) {

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
    public VCCredentialConfigurationCreationModel claims(List<String> claims) {

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

    public VCCredentialConfigurationCreationModel addClaimsItem(String claimsItem) {
        this.claims.add(claimsItem);
        return this;
    }

        /**
    * minimum: 60
    **/
    public VCCredentialConfigurationCreationModel expiryIn(Integer expiryIn) {

        this.expiryIn = expiryIn;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("expiryIn")
    @Valid
    @NotNull(message = "Property expiryIn cannot be null.")
 @Min(60)
    public Integer getExpiryIn() {
        return expiryIn;
    }
    public void setExpiryIn(Integer expiryIn) {
        this.expiryIn = expiryIn;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VCCredentialConfigurationCreationModel vcCredentialConfigurationCreationModel = (VCCredentialConfigurationCreationModel) o;
        return Objects.equals(this.identifier, vcCredentialConfigurationCreationModel.identifier) &&
            Objects.equals(this.configurationId, vcCredentialConfigurationCreationModel.configurationId) &&
            Objects.equals(this.scope, vcCredentialConfigurationCreationModel.scope) &&
            Objects.equals(this.format, vcCredentialConfigurationCreationModel.format) &&
            Objects.equals(this.signingAlgorithm, vcCredentialConfigurationCreationModel.signingAlgorithm) &&
            Objects.equals(this.type, vcCredentialConfigurationCreationModel.type) &&
            Objects.equals(this.metadata, vcCredentialConfigurationCreationModel.metadata) &&
            Objects.equals(this.claims, vcCredentialConfigurationCreationModel.claims) &&
            Objects.equals(this.expiryIn, vcCredentialConfigurationCreationModel.expiryIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, configurationId, scope, format, signingAlgorithm, type, metadata, claims, expiryIn);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCCredentialConfigurationCreationModel {\n");
        
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    configurationId: ").append(toIndentedString(configurationId)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
        sb.append("    signingAlgorithm: ").append(toIndentedString(signingAlgorithm)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
        sb.append("    expiryIn: ").append(toIndentedString(expiryIn)).append("\n");
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

