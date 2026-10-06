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
 * What a field can be compared against.
 **/
@ApiModel(description = "What a field can be compared against.")
public class RuleValueDefinition  {
  

    private org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value literal;
    private RuleValueFieldDefinition field;

    /**
    * How a literal value is entered.
    **/
    public RuleValueDefinition literal(org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value literal) {

        this.literal = literal;
        return this;
    }
    
    @ApiModelProperty(value = "How a literal value is entered.")
    @JsonProperty("literal")
    @Valid
    public org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value getLiteral() {
        return literal;
    }
    public void setLiteral(org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Value literal) {
        this.literal = literal;
    }

    /**
    * The fields a value may be read from. Absent when only literals are allowed.
    **/
    public RuleValueDefinition field(RuleValueFieldDefinition field) {

        this.field = field;
        return this;
    }
    
    @ApiModelProperty(value = "The fields a value may be read from. Absent when only literals are allowed.")
    @JsonProperty("field")
    @Valid
    public RuleValueFieldDefinition getField() {
        return field;
    }
    public void setField(RuleValueFieldDefinition field) {
        this.field = field;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleValueDefinition other = (RuleValueDefinition) o;
        return Objects.equals(this.literal, other.literal) &&
            Objects.equals(this.field, other.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literal, field);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RuleValueDefinition {\n");
        
        sb.append("    literal: ").append(toIndentedString(literal)).append("\n");
        sb.append("    field: ").append(toIndentedString(field)).append("\n");
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
