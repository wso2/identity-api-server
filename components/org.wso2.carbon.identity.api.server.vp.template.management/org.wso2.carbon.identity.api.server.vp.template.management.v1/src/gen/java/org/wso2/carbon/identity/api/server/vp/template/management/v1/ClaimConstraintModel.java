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
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Constraint applied to a single claim within a requested credential.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Constraint applied to a single claim within a requested credential.")
public class ClaimConstraintModel  {
  
    private String path;
    private Boolean mandatory = true;

    /**
    * The claim name identifying the claim within the credential.
    **/
    public ClaimConstraintModel path(String path) {

        this.path = path;
        return this;
    }

    @ApiModelProperty(example = "given_name", value = "The claim name identifying the claim within the credential.")
    @JsonProperty("path")
    @Valid
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    /**
    * Whether this claim must be present in the credential. Defaults to true.
    **/
    public ClaimConstraintModel mandatory(Boolean mandatory) {

        this.mandatory = mandatory;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether this claim must be present in the credential. Defaults to true.")
    @JsonProperty("mandatory")
    @Valid
    public Boolean getMandatory() {
        return mandatory;
    }
    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClaimConstraintModel claimConstraintModel = (ClaimConstraintModel) o;
        return Objects.equals(this.path, claimConstraintModel.path) &&
            Objects.equals(this.mandatory, claimConstraintModel.mandatory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, mandatory);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ClaimConstraintModel {\n");
        
        sb.append("    path: ").append(toIndentedString(path)).append("\n");
        sb.append("    mandatory: ").append(toIndentedString(mandatory)).append("\n");
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

