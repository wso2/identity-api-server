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
import org.wso2.carbon.identity.api.server.vc.config.management.v1.ClaimMapping;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.CredentialMetadata;
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
    private String credentialSigningAlgValuesSupported;
    private String credentialType;
    private CredentialMetadata credentialMetadata;
    private List<ClaimMapping> claimMappings = new ArrayList<ClaimMapping>();

    private Integer expiryInSeconds;

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
    public VCCredentialConfigurationCreationModel credentialSigningAlgValuesSupported(String credentialSigningAlgValuesSupported) {

        this.credentialSigningAlgValuesSupported = credentialSigningAlgValuesSupported;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("credentialSigningAlgValuesSupported")
    @Valid
    @NotNull(message = "Property credentialSigningAlgValuesSupported cannot be null.")

    public String getCredentialSigningAlgValuesSupported() {
        return credentialSigningAlgValuesSupported;
    }
    public void setCredentialSigningAlgValuesSupported(String credentialSigningAlgValuesSupported) {
        this.credentialSigningAlgValuesSupported = credentialSigningAlgValuesSupported;
    }

    /**
    **/
    public VCCredentialConfigurationCreationModel credentialType(String credentialType) {

        this.credentialType = credentialType;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("credentialType")
    @Valid
    @NotNull(message = "Property credentialType cannot be null.")

    public String getCredentialType() {
        return credentialType;
    }
    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    /**
    **/
    public VCCredentialConfigurationCreationModel credentialMetadata(CredentialMetadata credentialMetadata) {

        this.credentialMetadata = credentialMetadata;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("credentialMetadata")
    @Valid
    @NotNull(message = "Property credentialMetadata cannot be null.")

    public CredentialMetadata getCredentialMetadata() {
        return credentialMetadata;
    }
    public void setCredentialMetadata(CredentialMetadata credentialMetadata) {
        this.credentialMetadata = credentialMetadata;
    }

    /**
    **/
    public VCCredentialConfigurationCreationModel claimMappings(List<ClaimMapping> claimMappings) {

        this.claimMappings = claimMappings;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("claimMappings")
    @Valid
    @NotNull(message = "Property claimMappings cannot be null.")

    public List<ClaimMapping> getClaimMappings() {
        return claimMappings;
    }
    public void setClaimMappings(List<ClaimMapping> claimMappings) {
        this.claimMappings = claimMappings;
    }

    public VCCredentialConfigurationCreationModel addClaimMappingsItem(ClaimMapping claimMappingsItem) {
        this.claimMappings.add(claimMappingsItem);
        return this;
    }

        /**
    * minimum: 60
    **/
    public VCCredentialConfigurationCreationModel expiryInSeconds(Integer expiryInSeconds) {

        this.expiryInSeconds = expiryInSeconds;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("expiryInSeconds")
    @Valid
    @NotNull(message = "Property expiryInSeconds cannot be null.")
 @Min(60)
    public Integer getExpiryInSeconds() {
        return expiryInSeconds;
    }
    public void setExpiryInSeconds(Integer expiryInSeconds) {
        this.expiryInSeconds = expiryInSeconds;
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
            Objects.equals(this.credentialSigningAlgValuesSupported, vcCredentialConfigurationCreationModel.credentialSigningAlgValuesSupported) &&
            Objects.equals(this.credentialType, vcCredentialConfigurationCreationModel.credentialType) &&
            Objects.equals(this.credentialMetadata, vcCredentialConfigurationCreationModel.credentialMetadata) &&
            Objects.equals(this.claimMappings, vcCredentialConfigurationCreationModel.claimMappings) &&
            Objects.equals(this.expiryInSeconds, vcCredentialConfigurationCreationModel.expiryInSeconds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, configurationId, scope, format, credentialSigningAlgValuesSupported, credentialType, credentialMetadata, claimMappings, expiryInSeconds);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCCredentialConfigurationCreationModel {\n");
        
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    configurationId: ").append(toIndentedString(configurationId)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
        sb.append("    credentialSigningAlgValuesSupported: ").append(toIndentedString(credentialSigningAlgValuesSupported)).append("\n");
        sb.append("    credentialType: ").append(toIndentedString(credentialType)).append("\n");
        sb.append("    credentialMetadata: ").append(toIndentedString(credentialMetadata)).append("\n");
        sb.append("    claimMappings: ").append(toIndentedString(claimMappings)).append("\n");
        sb.append("    expiryInSeconds: ").append(toIndentedString(expiryInSeconds)).append("\n");
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

