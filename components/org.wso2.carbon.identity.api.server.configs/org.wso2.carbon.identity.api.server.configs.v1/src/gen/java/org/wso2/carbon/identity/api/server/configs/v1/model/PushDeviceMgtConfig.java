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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PushDeviceMgtConfig  {
  
    private Boolean enableMultipleDeviceEnrollment;
    private BigDecimal maximumDeviceLimit;

    /**
    * Whether to enable push device management related features.
    **/
    public PushDeviceMgtConfig enableMultipleDeviceEnrollment(Boolean enableMultipleDeviceEnrollment) {

        this.enableMultipleDeviceEnrollment = enableMultipleDeviceEnrollment;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether to enable push device management related features.")
    @JsonProperty("enableMultipleDeviceEnrollment")
    @Valid
    public Boolean getEnableMultipleDeviceEnrollment() {
        return enableMultipleDeviceEnrollment;
    }
    public void setEnableMultipleDeviceEnrollment(Boolean enableMultipleDeviceEnrollment) {
        this.enableMultipleDeviceEnrollment = enableMultipleDeviceEnrollment;
    }

    /**
    * Maximum number of devices a user can enroll. This property becomes only applicable if the enableMultipleDeviceEnrollment config is set to true.
    **/
    public PushDeviceMgtConfig maximumDeviceLimit(BigDecimal maximumDeviceLimit) {

        this.maximumDeviceLimit = maximumDeviceLimit;
        return this;
    }
    
    @ApiModelProperty(example = "5", value = "Maximum number of devices a user can enroll. This property becomes only applicable if the enableMultipleDeviceEnrollment config is set to true.")
    @JsonProperty("maximumDeviceLimit")
    @Valid
    public BigDecimal getMaximumDeviceLimit() {
        return maximumDeviceLimit;
    }
    public void setMaximumDeviceLimit(BigDecimal maximumDeviceLimit) {
        this.maximumDeviceLimit = maximumDeviceLimit;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PushDeviceMgtConfig pushDeviceMgtConfig = (PushDeviceMgtConfig) o;
        return Objects.equals(this.enableMultipleDeviceEnrollment, pushDeviceMgtConfig.enableMultipleDeviceEnrollment) &&
            Objects.equals(this.maximumDeviceLimit, pushDeviceMgtConfig.maximumDeviceLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableMultipleDeviceEnrollment, maximumDeviceLimit);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PushDeviceMgtConfig {\n");
        
        sb.append("    enableMultipleDeviceEnrollment: ").append(toIndentedString(enableMultipleDeviceEnrollment)).append("\n");
        sb.append("    maximumDeviceLimit: ").append(toIndentedString(maximumDeviceLimit)).append("\n");
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

