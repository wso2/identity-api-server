/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Validator  {

    private Boolean enable;
    private Integer priority;
    private Boolean fullChainValidation;
    private Integer retryCount;

    /**
    * Validator is enabled or not
    **/
    public Validator enable(Boolean enable) {

        this.enable = enable;
        return this;
    }

    @ApiModelProperty(example = "true", value = "Validator is enabled or not")
    @JsonProperty("enable")
    @Valid
    public Boolean getEnable() {
        return enable;
    }
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    /**
    * Validator priority
    **/
    public Validator priority(Integer priority) {

        this.priority = priority;
        return this;
    }

    @ApiModelProperty(example = "1", value = "Validator priority")
    @JsonProperty("priority")
    @Valid
    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
    * Full chain validation is enabled or not
    **/
    public Validator fullChainValidation(Boolean fullChainValidation) {

        this.fullChainValidation = fullChainValidation;
        return this;
    }

    @ApiModelProperty(example = "true", value = "Full chain validation is enabled or not")
    @JsonProperty("fullChainValidation")
    @Valid
    public Boolean getFullChainValidation() {
        return fullChainValidation;
    }
    public void setFullChainValidation(Boolean fullChainValidation) {
        this.fullChainValidation = fullChainValidation;
    }

    /**
    * Retry count
    **/
    public Validator retryCount(Integer retryCount) {

        this.retryCount = retryCount;
        return this;
    }

    @ApiModelProperty(example = "3", value = "Retry count")
    @JsonProperty("retryCount")
    @Valid
    public Integer getRetryCount() {
        return retryCount;
    }
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Validator validator = (Validator) o;
        return Objects.equals(this.enable, validator.enable) &&
            Objects.equals(this.priority, validator.priority) &&
            Objects.equals(this.fullChainValidation, validator.fullChainValidation) &&
            Objects.equals(this.retryCount, validator.retryCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enable, priority, fullChainValidation, retryCount);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Validator {\n");

        sb.append("    enable: ").append(toIndentedString(enable)).append("\n");
        sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
        sb.append("    fullChainValidation: ").append(toIndentedString(fullChainValidation)).append("\n");
        sb.append("    retryCount: ").append(toIndentedString(retryCount)).append("\n");
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

