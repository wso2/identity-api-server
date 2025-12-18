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

package org.wso2.carbon.identity.api.server.vc.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * Fields allowed to change after creation.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Fields allowed to change after creation.")
public class VCTemplateUpdateModel  {
  
    private String displayName;
    private String format;
    private List<String> claims = null;

    private Integer expiresIn;

    /**
    * If not provided, server sets to &#x60;identifier&#x60;.
    **/
    public VCTemplateUpdateModel displayName(String displayName) {

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
    public VCTemplateUpdateModel format(String format) {

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
    public VCTemplateUpdateModel claims(List<String> claims) {

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

    public VCTemplateUpdateModel addClaimsItem(String claimsItem) {
        if (this.claims == null) {
            this.claims = new ArrayList<String>();
        }
        this.claims.add(claimsItem);
        return this;
    }

        /**
    * minimum: 60
    **/
    public VCTemplateUpdateModel expiresIn(Integer expiresIn) {

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
        VCTemplateUpdateModel vcTemplateUpdateModel = (VCTemplateUpdateModel) o;
        return Objects.equals(this.displayName, vcTemplateUpdateModel.displayName) &&
            Objects.equals(this.format, vcTemplateUpdateModel.format) &&
            Objects.equals(this.claims, vcTemplateUpdateModel.claims) &&
            Objects.equals(this.expiresIn, vcTemplateUpdateModel.expiresIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, format, claims, expiresIn);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class VCTemplateUpdateModel {\n");
        
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    format: ").append(toIndentedString(format)).append("\n");
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

