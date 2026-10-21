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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

/**
 * A field used in a condition.
 **/
@ApiModel(description = "A field used in a condition.")
public class FieldReference  {
  

    private String name;
    private String qualifier;

    /**
    * Name of the field, as listed in the rule metadata. For example, user.claims is a claim stored for the user, and user.collectedClaims is a claim collected during the flow.
    **/
    public FieldReference name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "user.collectedClaims", required = true, value = "Name of the field, as listed in the rule metadata. For example, user.claims is a claim stored for the user, and user.collectedClaims is a claim collected during the flow.")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Identifies a value within the field, such as a claim URI. Required for fields that have a qualifier in the rule metadata, and not allowed for other fields.
    **/
    public FieldReference qualifier(String qualifier) {

        this.qualifier = qualifier;
        return this;
    }
    
    @ApiModelProperty(example = "http://wso2.org/claims/country", value = "Identifies a value within the field, such as a claim URI. Required for fields that have a qualifier in the rule metadata, and not allowed for other fields.")
    @JsonProperty("qualifier")
    @Valid
    public String getQualifier() {
        return qualifier;
    }
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FieldReference other = (FieldReference) o;
        return Objects.equals(this.name, other.name) &&
            Objects.equals(this.qualifier, other.qualifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, qualifier);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FieldReference {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    qualifier: ").append(toIndentedString(qualifier)).append("\n");
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
