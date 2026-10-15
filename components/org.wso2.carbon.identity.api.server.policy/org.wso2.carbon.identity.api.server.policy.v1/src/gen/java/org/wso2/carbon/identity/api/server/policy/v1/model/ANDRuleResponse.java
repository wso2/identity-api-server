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

package org.wso2.carbon.identity.api.server.policy.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.policy.v1.model.ExpressionResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ANDRuleResponse  {
  
    private List<ExpressionResponse> expressions = null;


    /**
    * Expressions combined with AND logic.
    **/
    public ANDRuleResponse expressions(List<ExpressionResponse> expressions) {

        this.expressions = expressions;
        return this;
    }
    
    @ApiModelProperty(value = "Expressions combined with AND logic.")
    @JsonProperty("expressions")
    @Valid
    public List<ExpressionResponse> getExpressions() {
        return expressions;
    }
    public void setExpressions(List<ExpressionResponse> expressions) {
        this.expressions = expressions;
    }

    public ANDRuleResponse addExpressionsItem(ExpressionResponse expressionsItem) {
        if (this.expressions == null) {
            this.expressions = new ArrayList<>();
        }
        this.expressions.add(expressionsItem);
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
        ANDRuleResponse anDRuleResponse = (ANDRuleResponse) o;
        return Objects.equals(this.expressions, anDRuleResponse.expressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressions);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ANDRuleResponse {\n");
        
        sb.append("    expressions: ").append(toIndentedString(expressions)).append("\n");
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

