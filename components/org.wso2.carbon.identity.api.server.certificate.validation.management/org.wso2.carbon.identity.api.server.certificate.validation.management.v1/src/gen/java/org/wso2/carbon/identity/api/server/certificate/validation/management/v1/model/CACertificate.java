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

package org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model;

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

public class CACertificate  {

    private String issuerDN;
    private List<String> crlUrls = null;

    private List<String> ocspUrls = null;

    private String id;
    private String serialNumber;

    /**
    **/
    public CACertificate issuerDN(String issuerDN) {

        this.issuerDN = issuerDN;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("issuerDN")
    @Valid
    public String getIssuerDN() {
        return issuerDN;
    }
    public void setIssuerDN(String issuerDN) {
        this.issuerDN = issuerDN;
    }

    /**
    **/
    public CACertificate crlUrls(List<String> crlUrls) {

        this.crlUrls = crlUrls;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("crlUrls")
    @Valid
    public List<String> getCrlUrls() {
        return crlUrls;
    }
    public void setCrlUrls(List<String> crlUrls) {
        this.crlUrls = crlUrls;
    }

    public CACertificate addCrlUrlsItem(String crlUrlsItem) {
        if (this.crlUrls == null) {
            this.crlUrls = new ArrayList<String>();
        }
        this.crlUrls.add(crlUrlsItem);
        return this;
    }

        /**
    **/
    public CACertificate ocspUrls(List<String> ocspUrls) {

        this.ocspUrls = ocspUrls;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("ocspUrls")
    @Valid
    public List<String> getOcspUrls() {
        return ocspUrls;
    }
    public void setOcspUrls(List<String> ocspUrls) {
        this.ocspUrls = ocspUrls;
    }

    public CACertificate addOcspUrlsItem(String ocspUrlsItem) {
        if (this.ocspUrls == null) {
            this.ocspUrls = new ArrayList<String>();
        }
        this.ocspUrls.add(ocspUrlsItem);
        return this;
    }

        /**
    **/
    public CACertificate id(String id) {

        this.id = id;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public CACertificate serialNumber(String serialNumber) {

        this.serialNumber = serialNumber;
        return this;
    }

    @ApiModelProperty(value = "")
    @JsonProperty("serialNumber")
    @Valid
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CACertificate caCertificate = (CACertificate) o;
        return Objects.equals(this.issuerDN, caCertificate.issuerDN) &&
            Objects.equals(this.crlUrls, caCertificate.crlUrls) &&
            Objects.equals(this.ocspUrls, caCertificate.ocspUrls) &&
            Objects.equals(this.id, caCertificate.id) &&
            Objects.equals(this.serialNumber, caCertificate.serialNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuerDN, crlUrls, ocspUrls, id, serialNumber);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CACertificate {\n");

        sb.append("    issuerDN: ").append(toIndentedString(issuerDN)).append("\n");
        sb.append("    crlUrls: ").append(toIndentedString(crlUrls)).append("\n");
        sb.append("    ocspUrls: ").append(toIndentedString(ocspUrls)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    serialNumber: ").append(toIndentedString(serialNumber)).append("\n");
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

