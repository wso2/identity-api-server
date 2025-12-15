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

package org.wso2.carbon.identity.api.server.vc.template.management.v1;

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

public class VCTemplate  {
  
    private String id;
    private String identifier;
    private String displayName;
    private String description;
    private String format;
    private List<String> claims = new ArrayList<String>();

    private Integer expiresIn;
    private String offerId;

    /**
    **/
    public VCTemplate id(String id) {

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
    public VCTemplate identifier(String identifier) {

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
    * Display name of the template
    **/
    public VCTemplate displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "EmployeeBadge", required = true, value = "Display name of the template")
    @JsonProperty("displayName")
    @Valid
    @NotNull(message = "Property displayName cannot be null.")

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * Description name of the template
    **/
    public VCTemplate description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "EmployeeBadge", value = "Description name of the template")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    **/
    public VCTemplate format(String format) {

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
    **/
    public VCTemplate claims(List<String> claims) {

        this.claims = claims;
        return this;
    }
    
    @ApiModelProperty(example = "[\"given_name\",\"email\"]", required = true, value = "")
    @JsonProperty("claims")
    @Valid
    @NotNull(message = "Property claims cannot be null.")

    public List<String> getClaims() {
        return claims;
    }
    public void setClaims(List<String> claims) {
        this.claims = claims;
    }

    public VCTemplate addClaimsItem(String claimsItem) {
        this.claims.add(claimsItem);
        return this;
    }

        /**
    **/
    public VCTemplate expiresIn(Integer expiresIn) {

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

    /**
    * Backend-generated UUID for the credential offer. Null if no offer has been generated.
    **/
    public VCTemplate offerId(String offerId) {

        this.offerId = offerId;
        return this;
    }
    
    @ApiModelProperty(example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890", value = "Backend-generated UUID for the credential offer. Null if no offer has been generated.")
    @JsonProperty("offerId")
    @Valid
    public String getOfferId() {
        return offerId;
    }
    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VCTemplate vcTemplate = (VCTemplate) o;
        return Objects.equals(this.id, vcTemplate.id) &&
            Objects.equals(this.identifier, vcTemplate.identifier) &&
            Objects.equals(this.displayName, vcTemplate.displayName) &&
            Objects.equals(this.description, vcTemplate.description) &&
            Objects.equals(this.format, vcTemplate.format) &&
            Objects.equals(this.claims, vcTemplate.claims) &&
            Objects.equals(this.expiresIn, vcTemplate.expiresIn) &&
            Objects.equals(this.offerId, vcTemplate.offerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, displayName, description, format, claims, expiresIn, offerId);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCTemplate {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
        sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
        sb.append("    offerId: ").append(toIndentedString(offerId)).append("\n");
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

