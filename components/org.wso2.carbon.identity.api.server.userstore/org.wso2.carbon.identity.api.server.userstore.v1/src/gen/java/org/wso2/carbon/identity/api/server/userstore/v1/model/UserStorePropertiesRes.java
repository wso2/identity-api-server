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

package org.wso2.carbon.identity.api.server.userstore.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.userstore.v1.model.PropertiesRes;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class UserStorePropertiesRes  {
  
    private List<PropertiesRes> mandatory = null;

    private List<PropertiesRes> optional = null;

    private List<PropertiesRes> advanced = null;


    /**
    **/
    public UserStorePropertiesRes mandatory(List<PropertiesRes> mandatory) {

        this.mandatory = mandatory;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("Mandatory")
    @Valid
    public List<PropertiesRes> getMandatory() {
        return mandatory;
    }
    public void setMandatory(List<PropertiesRes> mandatory) {
        this.mandatory = mandatory;
    }

    public UserStorePropertiesRes addMandatoryItem(PropertiesRes mandatoryItem) {
        if (this.mandatory == null) {
            this.mandatory = new ArrayList<>();
        }
        this.mandatory.add(mandatoryItem);
        return this;
    }

        /**
    **/
    public UserStorePropertiesRes optional(List<PropertiesRes> optional) {

        this.optional = optional;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("Optional")
    @Valid
    public List<PropertiesRes> getOptional() {
        return optional;
    }
    public void setOptional(List<PropertiesRes> optional) {
        this.optional = optional;
    }

    public UserStorePropertiesRes addOptionalItem(PropertiesRes optionalItem) {
        if (this.optional == null) {
            this.optional = new ArrayList<>();
        }
        this.optional.add(optionalItem);
        return this;
    }

        /**
    **/
    public UserStorePropertiesRes advanced(List<PropertiesRes> advanced) {

        this.advanced = advanced;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("Advanced")
    @Valid
    public List<PropertiesRes> getAdvanced() {
        return advanced;
    }
    public void setAdvanced(List<PropertiesRes> advanced) {
        this.advanced = advanced;
    }

    public UserStorePropertiesRes addAdvancedItem(PropertiesRes advancedItem) {
        if (this.advanced == null) {
            this.advanced = new ArrayList<>();
        }
        this.advanced.add(advancedItem);
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
        UserStorePropertiesRes userStorePropertiesRes = (UserStorePropertiesRes) o;
        return Objects.equals(this.mandatory, userStorePropertiesRes.mandatory) &&
            Objects.equals(this.optional, userStorePropertiesRes.optional) &&
            Objects.equals(this.advanced, userStorePropertiesRes.advanced);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mandatory, optional, advanced);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UserStorePropertiesRes {\n");
        
        sb.append("    mandatory: ").append(toIndentedString(mandatory)).append("\n");
        sb.append("    optional: ").append(toIndentedString(optional)).append("\n");
        sb.append("    advanced: ").append(toIndentedString(advanced)).append("\n");
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

