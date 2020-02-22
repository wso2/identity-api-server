/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.keystore.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.net.URI;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class CertificateResponse  {
  
    private String alias;
    private URI certificate;

    /**
    **/
    public CertificateResponse alias(String alias) {

        this.alias = alias;
        return this;
    }
    
    @ApiModelProperty(example = "wso2carbon", required = true, value = "")
    @JsonProperty("alias")
    @Valid
    @NotNull(message = "Property alias cannot be null.")

    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
    **/
    public CertificateResponse certificate(URI certificate) {

        this.certificate = certificate;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/t/carbon.super/api/server/v1/keystores/certs/wso2carbon", required = true, value = "")
    @JsonProperty("certificate")
    @Valid
    @NotNull(message = "Property certificate cannot be null.")

    public URI getCertificate() {
        return certificate;
    }
    public void setCertificate(URI certificate) {
        this.certificate = certificate;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CertificateResponse certificateResponse = (CertificateResponse) o;
        return Objects.equals(this.alias, certificateResponse.alias) &&
            Objects.equals(this.certificate, certificateResponse.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, certificate);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CertificateResponse {\n");
        
        sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
        sb.append("    certificate: ").append(toIndentedString(certificate)).append("\n");
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

