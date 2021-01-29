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

/**
 * This class contains fingerprints of certificate.
 */
public class Fingerprints  {
  
    private String MD5;
    private String SHA1;
    private String SHA_256;

    /**
    **/
    public Fingerprints MD5(String MD5) {

        this.MD5 = MD5;
        return this;
    }
    
    @ApiModelProperty(example = "CC:24:8C:9D:16:EA:2A:97:EC:45:78:79:32:7C:18:84", value = "")
    @JsonProperty("MD5")
    @Valid
    public String getMD5() {
        return MD5;
    }
    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    /**
    **/
    public Fingerprints SHA1(String SHA1) {

        this.SHA1 = SHA1;
        return this;
    }
    
    @ApiModelProperty(example = "CC:24:8C:9D:16:EA:2A:97:EC:45:78:79:32:7C:18:84:8C:27:65:2F", value = "")
    @JsonProperty("SHA1")
    @Valid
    public String getSHA1() {
        return SHA1;
    }
    public void setSHA1(String SHA1) {
        this.SHA1 = SHA1;
    }

    /**
    **/
    public Fingerprints SHA_256(String SHA_256) {

        this.SHA_256 = SHA_256;
        return this;
    }
    
    @ApiModelProperty(example = "61:55:FD:0D:C8:40:19:C0:10:42:4B:FD:45:87:26:8E:B7:96:E0:C9:C0:F5:DC:23:A6:1D:AB:8B:62:6E:6B:08", value = "")
    @JsonProperty("SHA-256")
    @Valid
    public String getSHA256() {
        return SHA_256;
    }
    public void setSHA256(String SHA_256) {
        this.SHA_256 = SHA_256;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fingerprints fingerprints = (Fingerprints) o;
        return Objects.equals(this.MD5, fingerprints.MD5) &&
            Objects.equals(this.SHA1, fingerprints.SHA1) &&
            Objects.equals(this.SHA_256, fingerprints.SHA_256);
    }

    @Override
    public int hashCode() {
        return Objects.hash(MD5, SHA1, SHA_256);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Fingerprints {\n");
        
        sb.append("    MD5: ").append(toIndentedString(MD5)).append("\n");
        sb.append("    SHA1: ").append(toIndentedString(SHA1)).append("\n");
        sb.append("    SHA_256: ").append(toIndentedString(SHA_256)).append("\n");
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

