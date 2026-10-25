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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.wso2.carbon.identity.api.server.flow.management.v1.RuleValue;
import javax.validation.constraints.*;

/**
 * A field that conditions can use.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "A field that conditions can use.")
public class RuleField  {
  
    private String name;
    private String displayName;
    private RuleValue qualifier;

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
    * Display name of the field.
    **/
    public RuleField displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Claim collected in this flow", value = "Display name of the field.")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public RuleField qualifier(RuleValue qualifier) {

        this.qualifier = qualifier;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("qualifier")
    @Valid
    public RuleValue getQualifier() {
        return qualifier;
    }
    public void setQualifier(RuleValue qualifier) {
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
        RuleField ruleField = (RuleField) o;
        return Objects.equals(this.name, ruleField.name) &&
            Objects.equals(this.displayName, ruleField.displayName) &&
            Objects.equals(this.qualifier, ruleField.qualifier);
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

