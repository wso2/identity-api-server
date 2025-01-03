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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "")
public class ProfileAttributesDTO {

    @Valid 
    private Boolean readOnly = null;

    @Valid 
    private Boolean required = null;

    @Valid
    private Boolean supportedByDefault = null;

    /**
     * Specifies if the claim is read-only in given profile.
    **/
    @ApiModelProperty(value = "Specifies if the claim is read-only in given profile.")
    @JsonProperty("readOnly")
    public Boolean getReadOnly() {
        return readOnly;
    }
    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Specifies if the claim is required in given profile.
    **/
    @ApiModelProperty(value = "Specifies if the claim is required in given profile.")
    @JsonProperty("required")
    public Boolean getRequired() {
        return required;
    }
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
     * Specifies if the claim will be displayed on the given profile.
    **/
    @ApiModelProperty(value = "Specifies if the claim will be displayed on the given profile.")
    @JsonProperty("supportedByDefault")
    public Boolean getSupportedByDefault() {
        return supportedByDefault;
    }
    public void setSupportedByDefault(Boolean supportedByDefault) {
        this.supportedByDefault = supportedByDefault;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ProfileAttributesDTO {\n");

        sb.append("    readOnly: ").append(readOnly).append("\n");
        sb.append("    required: ").append(required).append("\n");
        sb.append("    supportedByDefault: ").append(supportedByDefault).append("\n");

        sb.append("}\n");
        return sb.toString();
    }
}
