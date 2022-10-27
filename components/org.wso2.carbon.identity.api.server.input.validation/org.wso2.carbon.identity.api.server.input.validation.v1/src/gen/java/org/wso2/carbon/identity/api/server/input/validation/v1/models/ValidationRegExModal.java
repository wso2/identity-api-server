/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.input.validation.v1.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ValidationRegExModal  {
  
    private String jsRegExValidator;
    private String javaRegExValidator;

    /**
    **/
    public ValidationRegExModal jsRegExValidator(String jsRegExValidator) {

        this.jsRegExValidator = jsRegExValidator;
        return this;
    }
    
    @ApiModelProperty(example = "/^[a-zA-Z0-9!@#$%^&*]{6,16}$/", value = "")
    @JsonProperty("jsRegExValidator")
    @Valid
    public String getJsRegExValidator() {
        return jsRegExValidator;
    }
    public void setJsRegExValidator(String jsRegExValidator) {
        this.jsRegExValidator = jsRegExValidator;
    }

    /**
    **/
    public ValidationRegExModal javaRegExValidator(String javaRegExValidator) {

        this.javaRegExValidator = javaRegExValidator;
        return this;
    }
    
    @ApiModelProperty(example = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{6, 16}$", value = "")
    @JsonProperty("javaRegExValidator")
    @Valid
    public String getJavaRegExValidator() {
        return javaRegExValidator;
    }
    public void setJavaRegExValidator(String javaRegExValidator) {
        this.javaRegExValidator = javaRegExValidator;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValidationRegExModal validationRegExModal = (ValidationRegExModal) o;
        return Objects.equals(this.jsRegExValidator, validationRegExModal.jsRegExValidator) &&
            Objects.equals(this.javaRegExValidator, validationRegExModal.javaRegExValidator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsRegExValidator, javaRegExValidator);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ValidationRegExModal {\n");
        
        sb.append("    jsRegExValidator: ").append(toIndentedString(jsRegExValidator)).append("\n");
        sb.append("    javaRegExValidator: ").append(toIndentedString(javaRegExValidator)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(Object o) {

        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n");
    }
}

