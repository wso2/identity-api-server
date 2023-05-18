/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class JWTIssuerPOSTRequest  {
  
    private String name;
    private String description;
    private String image;
    private String templateId;
    private Certificate certificate;
    private String alias;
    private String idpIssuerName;

    /**
    **/
    public JWTIssuerPOSTRequest name(String name) {

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
    public JWTIssuerPOSTRequest description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "JWT Issuer", value = "")
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
    public JWTIssuerPOSTRequest image(String image) {

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
    public JWTIssuerPOSTRequest templateId(String templateId) {

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
    public JWTIssuerPOSTRequest certificate(Certificate certificate) {

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
    public JWTIssuerPOSTRequest alias(String alias) {

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
    public JWTIssuerPOSTRequest idpIssuerName(String idpIssuerName) {

        this.idpIssuerName = idpIssuerName;
        return this;
    }
    
    @ApiModelProperty(example = "https://www.issuer.com", required = true, value = "")
    @JsonProperty("idpIssuerName")
    @Valid
    @NotNull(message = "Property idpIssuerName cannot be null.")

    public String getIdpIssuerName() {
        return idpIssuerName;
    }
    public void setIdpIssuerName(String idpIssuerName) {
        this.idpIssuerName = idpIssuerName;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JWTIssuerPOSTRequest jwTIssuerPOSTRequest = (JWTIssuerPOSTRequest) o;
        return Objects.equals(this.name, jwTIssuerPOSTRequest.name) &&
            Objects.equals(this.description, jwTIssuerPOSTRequest.description) &&
            Objects.equals(this.image, jwTIssuerPOSTRequest.image) &&
            Objects.equals(this.templateId, jwTIssuerPOSTRequest.templateId) &&
            Objects.equals(this.certificate, jwTIssuerPOSTRequest.certificate) &&
            Objects.equals(this.alias, jwTIssuerPOSTRequest.alias) &&
            Objects.equals(this.idpIssuerName, jwTIssuerPOSTRequest.idpIssuerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, image, templateId, certificate, alias, idpIssuerName);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class JWTIssuerPOSTRequest {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    image: ").append(toIndentedString(image)).append("\n");
        sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
        sb.append("    certificate: ").append(toIndentedString(certificate)).append("\n");
        sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
        sb.append("    idpIssuerName: ").append(toIndentedString(idpIssuerName)).append("\n");
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

