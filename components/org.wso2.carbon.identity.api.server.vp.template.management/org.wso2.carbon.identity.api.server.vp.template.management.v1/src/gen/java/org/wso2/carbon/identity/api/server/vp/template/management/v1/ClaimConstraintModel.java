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

package org.wso2.carbon.identity.api.server.vp.template.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Claim Constraint Model — represents a single claim requirement within a requested credential.
 */
@ApiModel(description = "Claim Constraint Model")
public class ClaimConstraintModel {

    private String path;
    private Boolean mandatory = Boolean.TRUE;
    @ApiModelProperty(value = "Dot-notation claim path or claim name, e.g. \"given_name\" or \"address.street_address\".")
    @JsonProperty("path")
    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;
    }

    @ApiModelProperty(value = "Whether this claim is mandatory. Defaults to true.")
    @JsonProperty("mandatory")
    public Boolean getMandatory() {

        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {

        this.mandatory = mandatory;
    }

}
