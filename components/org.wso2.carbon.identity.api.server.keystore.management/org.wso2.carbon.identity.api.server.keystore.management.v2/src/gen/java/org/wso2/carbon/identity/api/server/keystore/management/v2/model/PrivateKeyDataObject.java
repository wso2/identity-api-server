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

public class PrivateKeyDataObject  {
  
    private String alias;
    private String subjectDN;
    private String issurDN;
    private Integer serialNumber;
    private Integer version;
    private String notAfter;
    private String notBefore;

    /**
    * Return the alias of the private key
    **/
    public PrivateKeyDataObject alias(String alias) {

        this.alias = alias;
        return this;
    }
    
    @ApiModelProperty(example = "newKey", value = "Return the alias of the private key")
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
    public PrivateKeyDataObject subjectDN(String subjectDN) {

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
    public PrivateKeyDataObject issurDN(String issurDN) {

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
    public PrivateKeyDataObject serialNumber(Integer serialNumber) {

        this.serialNumber = serialNumber;
        return this;
    }
    
    @ApiModelProperty(example = "1763023298", value = "")
    @JsonProperty("serialNumber")
    @Valid
    public Integer getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
    **/
    public PrivateKeyDataObject version(Integer version) {

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
    public PrivateKeyDataObject notAfter(String notAfter) {

        this.notAfter = notAfter;
        return this;
    }
    
    @ApiModelProperty(example = "10/01/2031", value = "")
    @JsonProperty("NotAfter")
    @Valid @Pattern(regexp="^\\d{3}-\\d{2}-\\d{4}$")
    public String getNotAfter() {
        return notAfter;
    }
    public void setNotAfter(String notAfter) {
        this.notAfter = notAfter;
    }

    /**
    **/
    public PrivateKeyDataObject notBefore(String notBefore) {

        this.notBefore = notBefore;
        return this;
    }
    
    @ApiModelProperty(example = "10/01/2014", value = "")
    @JsonProperty("NotBefore")
    @Valid @Pattern(regexp="^\\d{3}-\\d{2}-\\d{4}$")
    public String getNotBefore() {
        return notBefore;
    }
    public void setNotBefore(String notBefore) {
        this.notBefore = notBefore;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrivateKeyDataObject privateKeyDataObject = (PrivateKeyDataObject) o;
        return Objects.equals(this.alias, privateKeyDataObject.alias) &&
            Objects.equals(this.subjectDN, privateKeyDataObject.subjectDN) &&
            Objects.equals(this.issurDN, privateKeyDataObject.issurDN) &&
            Objects.equals(this.serialNumber, privateKeyDataObject.serialNumber) &&
            Objects.equals(this.version, privateKeyDataObject.version) &&
            Objects.equals(this.notAfter, privateKeyDataObject.notAfter) &&
            Objects.equals(this.notBefore, privateKeyDataObject.notBefore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, subjectDN, issurDN, serialNumber, version, notAfter, notBefore);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PrivateKeyDataObject {\n");
        
        sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
        sb.append("    subjectDN: ").append(toIndentedString(subjectDN)).append("\n");
        sb.append("    issurDN: ").append(toIndentedString(issurDN)).append("\n");
        sb.append("    serialNumber: ").append(toIndentedString(serialNumber)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    notAfter: ").append(toIndentedString(notAfter)).append("\n");
        sb.append("    notBefore: ").append(toIndentedString(notBefore)).append("\n");
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

