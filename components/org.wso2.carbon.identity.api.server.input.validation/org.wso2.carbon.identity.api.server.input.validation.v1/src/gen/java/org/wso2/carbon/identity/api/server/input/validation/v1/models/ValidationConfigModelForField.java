/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.RuleModel;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ValidationConfigModelForField  {
  
    private List<RuleModel> rules = null;

    private List<RuleModel> regEx = null;


    /**
    **/
    public ValidationConfigModelForField rules(List<RuleModel> rules) {

        this.rules = rules;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("rules")
    @Valid
    public List<RuleModel> getRules() {
        return rules;
    }
    public void setRules(List<RuleModel> rules) {
        this.rules = rules;
    }

    public ValidationConfigModelForField addRulesItem(RuleModel rulesItem) {
        if (this.rules == null) {
            this.rules = new ArrayList<>();
        }
        this.rules.add(rulesItem);
        return this;
    }

        /**
    **/
    public ValidationConfigModelForField regEx(List<RuleModel> regEx) {

        this.regEx = regEx;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("regEx")
    @Valid
    public List<RuleModel> getRegEx() {
        return regEx;
    }
    public void setRegEx(List<RuleModel> regEx) {
        this.regEx = regEx;
    }

    public ValidationConfigModelForField addRegExItem(RuleModel regExItem) {
        if (this.regEx == null) {
            this.regEx = new ArrayList<>();
        }
        this.regEx.add(regExItem);
        return this;
    }

    

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValidationConfigModelForField validationConfigModelForField = (ValidationConfigModelForField) o;
        return Objects.equals(this.rules, validationConfigModelForField.rules) &&
            Objects.equals(this.regEx, validationConfigModelForField.regEx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rules, regEx);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ValidationConfigModelForField {\n");
        
        sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
        sb.append("    regEx: ").append(toIndentedString(regEx)).append("\n");
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

