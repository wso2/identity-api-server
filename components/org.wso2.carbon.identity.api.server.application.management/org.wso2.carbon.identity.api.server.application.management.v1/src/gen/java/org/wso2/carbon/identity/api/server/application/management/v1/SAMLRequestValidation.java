/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class SAMLRequestValidation  {
  
    private Boolean enableSignatureValidation = true;
    private String signatureValidationCertAlias;

    /**
    **/
    public SAMLRequestValidation enableSignatureValidation(Boolean enableSignatureValidation) {

        this.enableSignatureValidation = enableSignatureValidation;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enableSignatureValidation")
    @Valid
    public Boolean getEnableSignatureValidation() {
        return enableSignatureValidation;
    }
    public void setEnableSignatureValidation(Boolean enableSignatureValidation) {
        this.enableSignatureValidation = enableSignatureValidation;
    }

    /**
    **/
    public SAMLRequestValidation signatureValidationCertAlias(String signatureValidationCertAlias) {

        this.signatureValidationCertAlias = signatureValidationCertAlias;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("signatureValidationCertAlias")
    @Valid
    public String getSignatureValidationCertAlias() {
        return signatureValidationCertAlias;
    }
    public void setSignatureValidationCertAlias(String signatureValidationCertAlias) {
        this.signatureValidationCertAlias = signatureValidationCertAlias;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SAMLRequestValidation saMLRequestValidation = (SAMLRequestValidation) o;
        return Objects.equals(this.enableSignatureValidation, saMLRequestValidation.enableSignatureValidation) &&
            Objects.equals(this.signatureValidationCertAlias, saMLRequestValidation.signatureValidationCertAlias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableSignatureValidation, signatureValidationCertAlias);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class SAMLRequestValidation {\n");
        
        sb.append("    enableSignatureValidation: ").append(toIndentedString(enableSignatureValidation)).append("\n");
        sb.append("    signatureValidationCertAlias: ").append(toIndentedString(signatureValidationCertAlias)).append("\n");
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

