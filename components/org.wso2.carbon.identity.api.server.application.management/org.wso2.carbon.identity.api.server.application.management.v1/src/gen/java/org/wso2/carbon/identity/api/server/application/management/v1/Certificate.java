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

public class Certificate  {
  
    private String type;
    private String value;

    /**
    * Certificate type. This should be either JWKS or PEM.
    **/
    public Certificate type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(value = "Certificate type. This should be either JWKS or PEM.")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * Certificate value. If type is JWKS, value should be jwks URL. If type is PEM, value should be the certificate in PEM format.
    **/
    public Certificate value(String value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(value = "Certificate value. If type is JWKS, value should be jwks URL. If type is PEM, value should be the certificate in PEM format.")
    @JsonProperty("value")
    @Valid
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Certificate certificate = (Certificate) o;
        return Objects.equals(this.type, certificate.type) &&
            Objects.equals(this.value, certificate.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Certificate {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

