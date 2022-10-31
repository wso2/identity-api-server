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
import org.wso2.carbon.identity.api.server.input.validation.v1.models.RepeatedCharactersValidatorModal;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.UniqueCharactersValidatorModal;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AdvancedConfigurationModal  {
  
    private RepeatedCharactersValidatorModal repeatedCharactersValidator;
    private UniqueCharactersValidatorModal uniqueCharactersValidator;

    /**
    **/
    public AdvancedConfigurationModal repeatedCharactersValidator(RepeatedCharactersValidatorModal repeatedCharactersValidator) {

        this.repeatedCharactersValidator = repeatedCharactersValidator;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("repeatedCharactersValidator")
    @Valid
    public RepeatedCharactersValidatorModal getRepeatedCharactersValidator() {
        return repeatedCharactersValidator;
    }
    public void setRepeatedCharactersValidator(RepeatedCharactersValidatorModal repeatedCharactersValidator) {
        this.repeatedCharactersValidator = repeatedCharactersValidator;
    }

    /**
    **/
    public AdvancedConfigurationModal uniqueCharactersValidator(UniqueCharactersValidatorModal uniqueCharactersValidator) {

        this.uniqueCharactersValidator = uniqueCharactersValidator;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("uniqueCharactersValidator")
    @Valid
    public UniqueCharactersValidatorModal getUniqueCharactersValidator() {
        return uniqueCharactersValidator;
    }
    public void setUniqueCharactersValidator(UniqueCharactersValidatorModal uniqueCharactersValidator) {
        this.uniqueCharactersValidator = uniqueCharactersValidator;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdvancedConfigurationModal advancedConfigurationModal = (AdvancedConfigurationModal) o;
        return Objects.equals(this.repeatedCharactersValidator, advancedConfigurationModal.repeatedCharactersValidator) &&
            Objects.equals(this.uniqueCharactersValidator, advancedConfigurationModal.uniqueCharactersValidator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repeatedCharactersValidator, uniqueCharactersValidator);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AdvancedConfigurationModal {\n");
        
        sb.append("    repeatedCharactersValidator: ").append(toIndentedString(repeatedCharactersValidator)).append("\n");
        sb.append("    uniqueCharactersValidator: ").append(toIndentedString(uniqueCharactersValidator)).append("\n");
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

