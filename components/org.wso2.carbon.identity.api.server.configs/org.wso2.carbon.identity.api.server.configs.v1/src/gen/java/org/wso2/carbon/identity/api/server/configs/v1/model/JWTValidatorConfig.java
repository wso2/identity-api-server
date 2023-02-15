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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class JWTValidatorConfig  {
  
    private Boolean enableTokenReuse;

    /**
    * If true, the JTI in the JWT will be unique per the request if the previously used JWT is not already expired. JTI (JWT ID) is a claim that provides a unique identifier for the JWT.
    **/
    public JWTValidatorConfig enableTokenReuse(Boolean enableTokenReuse) {

        this.enableTokenReuse = enableTokenReuse;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "If true, the JTI in the JWT will be unique per the request if the previously used JWT is not already expired. JTI (JWT ID) is a claim that provides a unique identifier for the JWT.")
    @JsonProperty("enableTokenReuse")
    @Valid
    public Boolean getEnableTokenReuse() {
        return enableTokenReuse;
    }
    public void setEnableTokenReuse(Boolean enableTokenReuse) {
        this.enableTokenReuse = enableTokenReuse;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JWTValidatorConfig jwTValidatorConfig = (JWTValidatorConfig) o;
        return Objects.equals(this.enableTokenReuse, jwTValidatorConfig.enableTokenReuse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableTokenReuse);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class JWTValidatorConfig {\n");
        
        sb.append("    enableTokenReuse: ").append(toIndentedString(enableTokenReuse)).append("\n");
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

