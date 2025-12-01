/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

/**
 * Fields allowed to change after creation.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Fields allowed to change after creation.")
public class VCCredentialConfigurationUpdateModel  {
  
    private String displayName;
    private String scope;
    private String format;
    private String type;
    private Metadata metadata;
    private List<String> claims = null;

    private Integer expiresIn;

    /**
    * If not provided, server sets to &#x60;identifier&#x60;.
    **/
    public VCCredentialConfigurationUpdateModel displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(value = "If not provided, server sets to `identifier`.")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public VCCredentialConfigurationUpdateModel scope(String scope) {

        this.scope = scope;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("scope")
    @Valid
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
    **/
    public VCCredentialConfigurationUpdateModel format(String format) {

        this.format = format;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("format")
    @Valid
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }

    /**
    **/
    public VCCredentialConfigurationUpdateModel type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    **/
    public VCCredentialConfigurationUpdateModel metadata(Metadata metadata) {

        this.metadata = metadata;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("metadata")
    @Valid
    public Metadata getMetadata() {
        return metadata;
    }
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
    **/
    public VCCredentialConfigurationUpdateModel claims(List<String> claims) {

        this.claims = claims;
        return this;
    }
    
    @ApiModelProperty(example = "[\"givenname\",\"surname\",\"email\"]", value = "")
    @JsonProperty("claims")
    @Valid
    public List<String> getClaims() {
        return claims;
    }
    public void setClaims(List<String> claims) {
        this.claims = claims;
    }

    public VCCredentialConfigurationUpdateModel addClaimsItem(String claimsItem) {
        if (this.claims == null) {
            this.claims = new ArrayList<String>();
        }
        this.claims.add(claimsItem);
        return this;
    }

        /**
    * minimum: 60
    **/
    public VCCredentialConfigurationUpdateModel expiresIn(Integer expiresIn) {

        this.expiresIn = expiresIn;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("expiresIn")
    @Valid @Min(60)
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
        VCCredentialConfigurationUpdateModel vcCredentialConfigurationUpdateModel = (VCCredentialConfigurationUpdateModel) o;
        return Objects.equals(this.displayName, vcCredentialConfigurationUpdateModel.displayName) &&
            Objects.equals(this.scope, vcCredentialConfigurationUpdateModel.scope) &&
            Objects.equals(this.format, vcCredentialConfigurationUpdateModel.format) &&
            Objects.equals(this.type, vcCredentialConfigurationUpdateModel.type) &&
            Objects.equals(this.metadata, vcCredentialConfigurationUpdateModel.metadata) &&
            Objects.equals(this.claims, vcCredentialConfigurationUpdateModel.claims) &&
            Objects.equals(this.expiresIn, vcCredentialConfigurationUpdateModel.expiresIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, scope, format, type, metadata, claims, expiresIn);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCCredentialConfigurationUpdateModel {\n");
        
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
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

