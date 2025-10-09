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

package org.wso2.carbon.identity.api.server.application.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ScriptUpdateModel  {
  
    private String script;

    /**
    * Authentication script to be updated.
    **/
    public ScriptUpdateModel script(String script) {

        this.script = script;
        return this;
    }
    
    @ApiModelProperty(example = "var onLoginRequest = function(context) {     executeStep(1); }; ", required = true, value = "Script to be updated.")
    @JsonProperty("script")
    @Valid
    @NotNull(message = "Property script cannot be null.")

    public String getScript() {
        return script;
    }
    public void setScript(String script) {
        this.script = script;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptUpdateModel scriptUpdateModel = (ScriptUpdateModel) o;
        return Objects.equals(this.script, scriptUpdateModel.script);
    }

    @Override
    public int hashCode() {
        return Objects.hash(script);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ScriptUpdateModel {\n");
        
        sb.append("    script: ").append(toIndentedString(script)).append("\n");
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

