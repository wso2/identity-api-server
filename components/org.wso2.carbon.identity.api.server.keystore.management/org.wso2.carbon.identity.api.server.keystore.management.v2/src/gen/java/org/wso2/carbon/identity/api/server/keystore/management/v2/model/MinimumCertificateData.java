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

package org.wso2.carbon.identity.api.server.keystore.management.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class MinimumCertificateData  {
  
    private String subjectDN;
    private String issurDN;
    private String notAfter;

    /**
    **/
    public MinimumCertificateData subjectDN(String subjectDN) {

        this.subjectDN = subjectDN;
        return this;
    }
    
    @ApiModelProperty(example = "C=None, O=None L, OU=None, CN=cert5", value = "")
    @JsonProperty("subjectDN")
    @Valid
    public String getSubjectDN() {
        return subjectDN;
    }
    public void setSubjectDN(String subjectDN) {
        this.subjectDN = subjectDN;
    }

    /**
    **/
    public MinimumCertificateData issurDN(String issurDN) {

        this.issurDN = issurDN;
        return this;
    }
    
    @ApiModelProperty(example = "C=None, O=None L, OU=None, CN=cert5", value = "")
    @JsonProperty("issurDN")
    @Valid
    public String getIssurDN() {
        return issurDN;
    }
    public void setIssurDN(String issurDN) {
        this.issurDN = issurDN;
    }

    /**
    **/
    public MinimumCertificateData notAfter(String notAfter) {

        this.notAfter = notAfter;
        return this;
    }
    
    @ApiModelProperty(example = "10/01/2031", value = "")
    @JsonProperty("notAfter")
    @Valid @Pattern(regexp="^\\d{3}-\\d{2}-\\d{4}$")
    public String getNotAfter() {
        return notAfter;
    }
    public void setNotAfter(String notAfter) {
        this.notAfter = notAfter;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MinimumCertificateData minimumCertificateData = (MinimumCertificateData) o;
        return Objects.equals(this.subjectDN, minimumCertificateData.subjectDN) &&
            Objects.equals(this.issurDN, minimumCertificateData.issurDN) &&
            Objects.equals(this.notAfter, minimumCertificateData.notAfter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectDN, issurDN, notAfter);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MinimumCertificateData {\n");
        
        sb.append("    subjectDN: ").append(toIndentedString(subjectDN)).append("\n");
        sb.append("    issurDN: ").append(toIndentedString(issurDN)).append("\n");
        sb.append("    notAfter: ").append(toIndentedString(notAfter)).append("\n");
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

