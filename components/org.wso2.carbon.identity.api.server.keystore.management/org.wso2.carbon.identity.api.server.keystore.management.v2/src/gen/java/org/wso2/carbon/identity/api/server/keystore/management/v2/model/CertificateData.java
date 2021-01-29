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
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.Fingerprints;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

/**
 * This class contains all the data related to public certificate of a key.
 */
public class CertificateData  {
  
    private String subjectDN;
    private String issurDN;
    private String serialNumber;
    private Integer version;
    private String notAfter;
    private String notBefore;
    private String signatureAlgorithm;
    private Fingerprints certificateFingerprints;
    private String publicKey;

    /**
    **/
    public CertificateData subjectDN(String subjectDN) {

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
    public CertificateData issurDN(String issurDN) {

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
    public CertificateData serialNumber(String serialNumber) {

        this.serialNumber = serialNumber;
        return this;
    }
    
    @ApiModelProperty(example = "1763023298", value = "")
    @JsonProperty("serialNumber")
    @Valid
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
    **/
    public CertificateData version(Integer version) {

        this.version = version;
        return this;
    }
    
    @ApiModelProperty(example = "3", value = "")
    @JsonProperty("version")
    @Valid
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
    **/
    public CertificateData notAfter(String notAfter) {

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

    /**
    **/
    public CertificateData notBefore(String notBefore) {

        this.notBefore = notBefore;
        return this;
    }
    
    @ApiModelProperty(example = "10/01/2014", value = "")
    @JsonProperty("notBefore")
    @Valid @Pattern(regexp="^\\d{3}-\\d{2}-\\d{4}$")
    public String getNotBefore() {
        return notBefore;
    }
    public void setNotBefore(String notBefore) {
        this.notBefore = notBefore;
    }

    /**
    **/
    public CertificateData signatureAlgorithm(String signatureAlgorithm) {

        this.signatureAlgorithm = signatureAlgorithm;
        return this;
    }
    
    @ApiModelProperty(example = "SHA256withRSA", value = "")
    @JsonProperty("signatureAlgorithm")
    @Valid
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }
    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
    **/
    public CertificateData certificateFingerprints(Fingerprints certificateFingerprints) {

        this.certificateFingerprints = certificateFingerprints;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("certificateFingerprints")
    @Valid
    public Fingerprints getCertificateFingerprints() {
        return certificateFingerprints;
    }
    public void setCertificateFingerprints(Fingerprints certificateFingerprints) {
        this.certificateFingerprints = certificateFingerprints;
    }

    /**
    **/
    public CertificateData publicKey(String publicKey) {

        this.publicKey = publicKey;
        return this;
    }
    
    @ApiModelProperty(example = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvgJ68OTTx7q3wP3JDH4jkfH9w9SkHb7mhP1Ual4V+BE1jP15J+tKw3NTK4ekPudiognmvAujJimnJRS36VvqMjUx/9Cp9lHg15SMrFfBA+60RtnUTwSjactGEXQ0/vHUj5F5xzvvY2gBJTbC88aWDdP7O0zqjF3O8grSayQJwaEYK73awEsM1H0hEkKXZIzeZvZY5QHPeG0i6WIdOd9N2fM+kHuU//2vKoDwXdClnxXr0+JviKM/GFrYQPu9ikeQlHF87ZMmcqagkHeyQ3Q0bzEBbsG1kMfiiYgRiMkK0AqD3zSnM0KGXz5ziU1v8Axxqk3B0y0UHDi3NgjmqsWLMwIDAQAB", value = "")
    @JsonProperty("publicKey")
    @Valid
    public String getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CertificateData certificateData = (CertificateData) o;
        return Objects.equals(this.subjectDN, certificateData.subjectDN) &&
            Objects.equals(this.issurDN, certificateData.issurDN) &&
            Objects.equals(this.serialNumber, certificateData.serialNumber) &&
            Objects.equals(this.version, certificateData.version) &&
            Objects.equals(this.notAfter, certificateData.notAfter) &&
            Objects.equals(this.notBefore, certificateData.notBefore) &&
            Objects.equals(this.signatureAlgorithm, certificateData.signatureAlgorithm) &&
            Objects.equals(this.certificateFingerprints, certificateData.certificateFingerprints) &&
            Objects.equals(this.publicKey, certificateData.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectDN, issurDN, serialNumber, version, notAfter, notBefore, signatureAlgorithm, certificateFingerprints, publicKey);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CertificateData {\n");
        
        sb.append("    subjectDN: ").append(toIndentedString(subjectDN)).append("\n");
        sb.append("    issurDN: ").append(toIndentedString(issurDN)).append("\n");
        sb.append("    serialNumber: ").append(toIndentedString(serialNumber)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    notAfter: ").append(toIndentedString(notAfter)).append("\n");
        sb.append("    notBefore: ").append(toIndentedString(notBefore)).append("\n");
        sb.append("    signatureAlgorithm: ").append(toIndentedString(signatureAlgorithm)).append("\n");
        sb.append("    certificateFingerprints: ").append(toIndentedString(certificateFingerprints)).append("\n");
        sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
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

