/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.RolePatchOpValue;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RolePatchOp  {
  
    private String op;
    private List<RolePatchOpValue> value = new ArrayList<>();


    /**
    **/
    public RolePatchOp op(String op) {

        this.op = op;
        return this;
    }
    
    @ApiModelProperty(example = "add", required = true, value = "")
    @JsonProperty("op")
    @Valid
    @NotNull(message = "Property op cannot be null.")

    public String getOp() {
        return op;
    }
    public void setOp(String op) {
        this.op = op;
    }

    /**
    **/
    public RolePatchOp value(List<RolePatchOpValue> value) {

        this.value = value;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("value")
    @Valid
    @NotNull(message = "Property value cannot be null.")

    public List<RolePatchOpValue> getValue() {
        return value;
    }
    public void setValue(List<RolePatchOpValue> value) {
        this.value = value;
    }

    public RolePatchOp addValueItem(RolePatchOpValue valueItem) {
        this.value.add(valueItem);
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
        RolePatchOp rolePatchOp = (RolePatchOp) o;
        return Objects.equals(this.op, rolePatchOp.op) &&
            Objects.equals(this.value, rolePatchOp.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, value);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RolePatchOp {\n");
        
        sb.append("    op: ").append(toIndentedString(op)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

