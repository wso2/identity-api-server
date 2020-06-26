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

public class AssertionEncryptionConfiguration  {
  
    private Boolean enabled = false;
    private String assertionEncryptionAlgorithm = "http://www.w3.org/2001/04/xmlenc#aes256-cbc";
    private String keyEncryptionAlgorithm = "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p";

    /**
    **/
    public AssertionEncryptionConfiguration enabled(Boolean enabled) {

        this.enabled = enabled;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("enabled")
    @Valid
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
    **/
    public AssertionEncryptionConfiguration assertionEncryptionAlgorithm(String assertionEncryptionAlgorithm) {

        this.assertionEncryptionAlgorithm = assertionEncryptionAlgorithm;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("assertionEncryptionAlgorithm")
    @Valid
    public String getAssertionEncryptionAlgorithm() {
        return assertionEncryptionAlgorithm;
    }
    public void setAssertionEncryptionAlgorithm(String assertionEncryptionAlgorithm) {
        this.assertionEncryptionAlgorithm = assertionEncryptionAlgorithm;
    }

    /**
    **/
    public AssertionEncryptionConfiguration keyEncryptionAlgorithm(String keyEncryptionAlgorithm) {

        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("keyEncryptionAlgorithm")
    @Valid
    public String getKeyEncryptionAlgorithm() {
        return keyEncryptionAlgorithm;
    }
    public void setKeyEncryptionAlgorithm(String keyEncryptionAlgorithm) {
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssertionEncryptionConfiguration assertionEncryptionConfiguration = (AssertionEncryptionConfiguration) o;
        return Objects.equals(this.enabled, assertionEncryptionConfiguration.enabled) &&
            Objects.equals(this.assertionEncryptionAlgorithm, assertionEncryptionConfiguration.assertionEncryptionAlgorithm) &&
            Objects.equals(this.keyEncryptionAlgorithm, assertionEncryptionConfiguration.keyEncryptionAlgorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, assertionEncryptionAlgorithm, keyEncryptionAlgorithm);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AssertionEncryptionConfiguration {\n");
        
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    assertionEncryptionAlgorithm: ").append(toIndentedString(assertionEncryptionAlgorithm)).append("\n");
        sb.append("    keyEncryptionAlgorithm: ").append(toIndentedString(keyEncryptionAlgorithm)).append("\n");
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

