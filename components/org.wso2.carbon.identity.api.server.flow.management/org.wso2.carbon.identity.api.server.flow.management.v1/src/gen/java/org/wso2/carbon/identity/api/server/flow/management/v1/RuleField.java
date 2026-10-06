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
 * A field a condition can be written against.
 **/
@ApiModel(description = "A field a condition can be written against.")
public class RuleField  {
  

    private String name;
    private String displayName;
    private org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value qualifier;

    /**
    * Name of the field.
    **/
    public RuleField name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "user.collectedClaims", required = true, value = "Name of the field.")
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
    * Label for the field.
    **/
    public RuleField displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Claim collected in this flow", value = "Label for the field.")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * How a value within the field is picked. Absent on a field that names a single value.
    **/
    public RuleField qualifier(org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value qualifier) {

        this.qualifier = qualifier;
        return this;
    }
    
    @ApiModelProperty(value = "How a value within the field is picked. Absent on a field that names a single value.")
    @JsonProperty("qualifier")
    @Valid
    public org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value getQualifier() {
        return qualifier;
    }
    public void setQualifier(org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value qualifier) {
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
        RuleField other = (RuleField) o;
        return Objects.equals(this.name, other.name) &&
            Objects.equals(this.displayName, other.displayName) &&
            Objects.equals(this.qualifier, other.qualifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, displayName, qualifier);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RuleField {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
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
