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

public class RepeatedCharactersValidatorModal  {
  
    private Boolean enabled;
    private Boolean caseSensitive;
    private Integer maxConsecutiveLength;

    /**
    **/
    public RepeatedCharactersValidatorModal enabled(Boolean enabled) {

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
    public RepeatedCharactersValidatorModal caseSensitive(Boolean caseSensitive) {

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
    public RepeatedCharactersValidatorModal maxConsecutiveLength(Integer maxConsecutiveLength) {

        this.maxConsecutiveLength = maxConsecutiveLength;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "")
    @JsonProperty("max-consecutive-length")
    @Valid
    public Integer getMaxConsecutiveLength() {
        return maxConsecutiveLength;
    }
    public void setMaxConsecutiveLength(Integer maxConsecutiveLength) {
        this.maxConsecutiveLength = maxConsecutiveLength;
    }



    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RepeatedCharactersValidatorModal repeatedCharactersValidatorModal = (RepeatedCharactersValidatorModal) o;
        return Objects.equals(this.enabled, repeatedCharactersValidatorModal.enabled) &&
            Objects.equals(this.caseSensitive, repeatedCharactersValidatorModal.caseSensitive) &&
            Objects.equals(this.maxConsecutiveLength, repeatedCharactersValidatorModal.maxConsecutiveLength);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, caseSensitive, maxConsecutiveLength);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RepeatedCharactersValidatorModal {\n");
        
        sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
        sb.append("    caseSensitive: ").append(toIndentedString(caseSensitive)).append("\n");
        sb.append("    maxConsecutiveLength: ").append(toIndentedString(maxConsecutiveLength)).append("\n");
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

