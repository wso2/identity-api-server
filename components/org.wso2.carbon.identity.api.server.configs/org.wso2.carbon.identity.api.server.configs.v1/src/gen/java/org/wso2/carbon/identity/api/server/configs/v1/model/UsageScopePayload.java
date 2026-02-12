/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class UsageScopePayload  {
  

@XmlType(name="UsageScopeEnum")
@XmlEnum(String.class)
public enum UsageScopeEnum {

    @XmlEnumValue("ALL_EXISTING_AND_FUTURE_ORGS") ALL_EXISTING_AND_FUTURE_ORGS(String.valueOf("ALL_EXISTING_AND_FUTURE_ORGS")), @XmlEnumValue("NONE") NONE(String.valueOf("NONE"));


    private String value;

    UsageScopeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static UsageScopeEnum fromValue(String value) {
        for (UsageScopeEnum b : UsageScopeEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private UsageScopeEnum usageScope;

    /**
    * Scope defining how the issuer can be used in organizations
    **/
    public UsageScopePayload usageScope(UsageScopeEnum usageScope) {

        this.usageScope = usageScope;
        return this;
    }
    
    @ApiModelProperty(example = "ALL_EXISTING_AND_FUTURE_ORGS", value = "Scope defining how the issuer can be used in organizations")
    @JsonProperty("usageScope")
    @Valid
    public UsageScopeEnum getUsageScope() {
        return usageScope;
    }
    public void setUsageScope(UsageScopeEnum usageScope) {
        this.usageScope = usageScope;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsageScopePayload usageScopePayload = (UsageScopePayload) o;
        return Objects.equals(this.usageScope, usageScopePayload.usageScope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usageScope);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UsageScopePayload {\n");
        
        sb.append("    usageScope: ").append(toIndentedString(usageScope)).append("\n");
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

