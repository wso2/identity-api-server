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

package org.wso2.carbon.identity.api.server.idp.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.idp.v1.model.Certificate;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class TrustedTokenIssuerPOSTRequest  {
  
    private String name;
    private String description;
    private String image;
    private String templateId;
    private Certificate certificate;
    private String alias;
    private String issuer;
    private Claims claims;

    /**
    **/
    public TrustedTokenIssuerPOSTRequest name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "google", required = true, value = "")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public TrustedTokenIssuerPOSTRequest description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Trusted Token Issuer", value = "")
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
    public TrustedTokenIssuerPOSTRequest image(String image) {

        this.image = image;
        return this;
    }
    
    @ApiModelProperty(example = "issuer-logo-url", value = "")
    @JsonProperty("image")
    @Valid
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    /**
    **/
    public TrustedTokenIssuerPOSTRequest templateId(String templateId) {

        this.templateId = templateId;
        return this;
    }
    
    @ApiModelProperty(example = "8ea23303-49c0-4253-b81f-82c0fe6fb4a0", value = "")
    @JsonProperty("templateId")
    @Valid
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
    **/
    public TrustedTokenIssuerPOSTRequest certificate(Certificate certificate) {

        this.certificate = certificate;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("certificate")
    @Valid
    @NotNull(message = "Property certificate cannot be null.")

    public Certificate getCertificate() {
        return certificate;
    }
    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    /**
    **/
    public TrustedTokenIssuerPOSTRequest alias(String alias) {

        this.alias = alias;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9444/oauth2/token", value = "")
    @JsonProperty("alias")
    @Valid
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
    **/
    public TrustedTokenIssuerPOSTRequest issuer(String issuer) {

        this.issuer = issuer;
        return this;
    }
    
    @ApiModelProperty(example = "https://www.issuer.com", required = true, value = "")
    @JsonProperty("issuer")
    @Valid
    @NotNull(message = "Property issuer cannot be null.")

    public String getIssuer() {
        return issuer;
    }
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
    **/
    public TrustedTokenIssuerPOSTRequest claims(Claims claims) {

        this.claims = claims;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("claims")
    @Valid
    public Claims getClaims() {
        return claims;
    }
    public void setClaims(Claims claims) {
        this.claims = claims;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrustedTokenIssuerPOSTRequest trustedTokenIssuerPOSTRequest = (TrustedTokenIssuerPOSTRequest) o;
        return Objects.equals(this.name, trustedTokenIssuerPOSTRequest.name) &&
            Objects.equals(this.description, trustedTokenIssuerPOSTRequest.description) &&
            Objects.equals(this.image, trustedTokenIssuerPOSTRequest.image) &&
            Objects.equals(this.templateId, trustedTokenIssuerPOSTRequest.templateId) &&
            Objects.equals(this.certificate, trustedTokenIssuerPOSTRequest.certificate) &&
            Objects.equals(this.alias, trustedTokenIssuerPOSTRequest.alias) &&
            Objects.equals(this.issuer, trustedTokenIssuerPOSTRequest.issuer) &&
            Objects.equals(this.claims, trustedTokenIssuerPOSTRequest.claims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, image, templateId, certificate, alias, issuer, claims);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class TrustedTokenIssuerPOSTRequest {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
        sb.append("    certificate: ").append(toIndentedString(certificate)).append("\n");
        sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
        sb.append("    issuer: ").append(toIndentedString(issuer)).append("\n");
        sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
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

