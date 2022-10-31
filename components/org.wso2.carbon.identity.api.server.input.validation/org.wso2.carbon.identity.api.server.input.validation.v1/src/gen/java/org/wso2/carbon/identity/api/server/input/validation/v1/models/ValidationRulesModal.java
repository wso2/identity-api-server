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
import org.wso2.carbon.identity.api.server.input.validation.v1.models.AdvancedConfigurationModal;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.BasicValidatorModal;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.RepeatedCharactersValidatorModal;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.UniqueCharactersValidatorModal;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ValidationRulesModal  {
  
    private BasicValidatorModal lengthValidator;
    private BasicValidatorModal specialCharactersValidator;
    private BasicValidatorModal numeralsValidator;
    private BasicValidatorModal upperCaseValidator;
    private BasicValidatorModal lowercaseValidator;
    private AdvancedConfigurationModal advancedConfiguration;

    /**
    **/
    public ValidationRulesModal lengthValidator(BasicValidatorModal lengthValidator) {

        this.lengthValidator = lengthValidator;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("lengthValidator")
    @Valid
    public BasicValidatorModal getLengthValidator() {
        return lengthValidator;
    }
    public void setLengthValidator(BasicValidatorModal lengthValidator) {
        this.lengthValidator = lengthValidator;
    }

    /**
    **/
    public ValidationRulesModal specialCharactersValidator(BasicValidatorModal specialCharactersValidator) {

        this.specialCharactersValidator = specialCharactersValidator;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("specialCharactersValidator")
    @Valid
    public BasicValidatorModal getSpecialCharactersValidator() {
        return specialCharactersValidator;
    }
    public void setSpecialCharactersValidator(BasicValidatorModal specialCharactersValidator) {
        this.specialCharactersValidator = specialCharactersValidator;
    }

    /**
    **/
    public ValidationRulesModal numeralsValidator(BasicValidatorModal numeralsValidator) {

        this.numeralsValidator = numeralsValidator;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("numeralsValidator")
    @Valid
    public BasicValidatorModal getNumeralsValidator() {
        return numeralsValidator;
    }
    public void setNumeralsValidator(BasicValidatorModal numeralsValidator) {
        this.numeralsValidator = numeralsValidator;
    }

    /**
    **/
    public ValidationRulesModal upperCaseValidator(BasicValidatorModal upperCaseValidator) {

        this.upperCaseValidator = upperCaseValidator;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("upperCaseValidator")
    @Valid
    public BasicValidatorModal getUpperCaseValidator() {
        return upperCaseValidator;
    }
    public void setUpperCaseValidator(BasicValidatorModal upperCaseValidator) {
        this.upperCaseValidator = upperCaseValidator;
    }

    /**
    **/
    public ValidationRulesModal lowercaseValidator(BasicValidatorModal lowercaseValidator) {

        this.lowercaseValidator = lowercaseValidator;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("lowercaseValidator")
    @Valid
    public BasicValidatorModal getLowercaseValidator() {
        return lowercaseValidator;
    }
    public void setLowercaseValidator(BasicValidatorModal lowercaseValidator) {
        this.lowercaseValidator = lowercaseValidator;
    }

    /**
    **/
    public ValidationRulesModal advancedConfiguration(AdvancedConfigurationModal advancedConfiguration) {

        this.advancedConfiguration = advancedConfiguration;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("advancedConfiguration")
    @Valid
    public AdvancedConfigurationModal getAdvancedConfiguration() {
        return advancedConfiguration;
    }
    public void setAdvancedConfiguration(AdvancedConfigurationModal advancedConfiguration) {
        this.advancedConfiguration = advancedConfiguration;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValidationRulesModal validationRulesModal = (ValidationRulesModal) o;
        return Objects.equals(this.lengthValidator, validationRulesModal.lengthValidator) &&
            Objects.equals(this.specialCharactersValidator, validationRulesModal.specialCharactersValidator) &&
            Objects.equals(this.numeralsValidator, validationRulesModal.numeralsValidator) &&
            Objects.equals(this.upperCaseValidator, validationRulesModal.upperCaseValidator) &&
            Objects.equals(this.lowercaseValidator, validationRulesModal.lowercaseValidator) &&
            Objects.equals(this.advancedConfiguration, validationRulesModal.advancedConfiguration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lengthValidator, specialCharactersValidator, numeralsValidator, upperCaseValidator, lowercaseValidator, advancedConfiguration);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ValidationRulesModal {\n");
        
        sb.append("    lengthValidator: ").append(toIndentedString(lengthValidator)).append("\n");
        sb.append("    specialCharactersValidator: ").append(toIndentedString(specialCharactersValidator)).append("\n");
        sb.append("    numeralsValidator: ").append(toIndentedString(numeralsValidator)).append("\n");
        sb.append("    upperCaseValidator: ").append(toIndentedString(upperCaseValidator)).append("\n");
        sb.append("    lowercaseValidator: ").append(toIndentedString(lowercaseValidator)).append("\n");
        sb.append("    advancedConfiguration: ").append(toIndentedString(advancedConfiguration)).append("\n");
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

