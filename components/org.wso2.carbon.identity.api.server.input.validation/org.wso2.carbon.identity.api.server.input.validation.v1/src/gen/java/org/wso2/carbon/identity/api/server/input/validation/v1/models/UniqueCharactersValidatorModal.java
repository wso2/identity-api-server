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

public class UniqueCharactersValidatorModal  {
  
    private Boolean enabled;
    private Boolean caseSensitive;
    private Integer minUniqueCharacters;

    /**
    **/
    public UniqueCharactersValidatorModal enabled(Boolean enabled) {

        this.enabled = enabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
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
    public UniqueCharactersValidatorModal caseSensitive(Boolean caseSensitive) {

        this.caseSensitive = caseSensitive;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("case-sensitive")
    @Valid
    public Boolean getCaseSensitive() {
        return caseSensitive;
    }
    public void setCaseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
    **/
    public UniqueCharactersValidatorModal minUniqueCharacters(Integer minUniqueCharacters) {

        this.minUniqueCharacters = minUniqueCharacters;
        return this;
    }
    
    @ApiModelProperty(example = "5", value = "")
    @JsonProperty("min-unique-characters")
    @Valid
    public Integer getMinUniqueCharacters() {
        return minUniqueCharacters;
    }
    public void setMinUniqueCharacters(Integer minUniqueCharacters) {
        this.minUniqueCharacters = minUniqueCharacters;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UniqueCharactersValidatorModal uniqueCharactersValidatorModal = (UniqueCharactersValidatorModal) o;
        return Objects.equals(this.enabled, uniqueCharactersValidatorModal.enabled) &&
            Objects.equals(this.caseSensitive, uniqueCharactersValidatorModal.caseSensitive) &&
            Objects.equals(this.minUniqueCharacters, uniqueCharactersValidatorModal.minUniqueCharacters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, caseSensitive, minUniqueCharacters);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UniqueCharactersValidatorModal {\n");
        
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    caseSensitive: ").append(toIndentedString(caseSensitive)).append("\n");
        sb.append("    minUniqueCharacters: ").append(toIndentedString(minUniqueCharacters)).append("\n");
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

