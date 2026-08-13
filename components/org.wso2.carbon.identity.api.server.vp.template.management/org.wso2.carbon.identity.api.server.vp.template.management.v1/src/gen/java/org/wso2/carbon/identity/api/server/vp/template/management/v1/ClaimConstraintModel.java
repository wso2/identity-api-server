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

import java.util.List;

/**
 * Claim Constraint Model — represents a single claim requirement within a requested credential.
 */
@ApiModel(description = "Claim Constraint Model")
public class ClaimConstraintModel {

    private String id;
    private List<String> path;
    private String name;
    private Boolean mandatory = Boolean.TRUE;
    private List<String> allowedValues;

    @ApiModelProperty(value = "DCQL claim id — used to reference this claim in claim_sets.")
    @JsonProperty("id")
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    @ApiModelProperty(value = "DCQL path array, e.g. [\"address\", \"street_address\"].")
    @JsonProperty("path")
    public List<String> getPath() {

        return path;
    }

    public void setPath(List<String> path) {

        this.path = path;
    }

    @ApiModelProperty(value = "The claim name (e.g. given_name). Used when path is absent.")
    @JsonProperty("name")
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    @ApiModelProperty(value = "Whether this claim is mandatory. Defaults to true.")
    @JsonProperty("mandatory")
    public Boolean getMandatory() {

        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {

        this.mandatory = mandatory;
    }

    @ApiModelProperty(value = "Allowed values for this claim. When set, the credential value must be one of these.")
    @JsonProperty("allowedValues")
    public List<String> getAllowedValues() {

        return allowedValues;
    }

    public void setAllowedValues(List<String> allowedValues) {

        this.allowedValues = allowedValues;
    }
}
