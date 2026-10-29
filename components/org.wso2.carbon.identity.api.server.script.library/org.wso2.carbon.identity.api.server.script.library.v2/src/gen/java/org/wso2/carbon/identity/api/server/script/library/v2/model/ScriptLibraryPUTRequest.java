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

package org.wso2.carbon.identity.api.server.script.library.v2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.File;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ScriptLibraryPUTRequest  {
  
    private String description;
    private File content;

    /**
    * Description of the script library.
    **/
    public ScriptLibraryPUTRequest description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "script library description", value = "Description of the script library.")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Content or the code file of the script library.
    **/
    public ScriptLibraryPUTRequest content(File content) {

        this.content = content;
        return this;
    }
    
    @ApiModelProperty(required = true, value = "Content or the code file of the script library.")
    @JsonProperty("content")
    @Valid
    @NotNull(message = "Property content cannot be null.")

    public File getContent() {
        return content;
    }
    public void setContent(File content) {
        this.content = content;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptLibraryPUTRequest scriptLibraryPUTRequest = (ScriptLibraryPUTRequest) o;
        return Objects.equals(this.description, scriptLibraryPUTRequest.description) &&
            Objects.equals(this.content, scriptLibraryPUTRequest.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, content);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ScriptLibraryPUTRequest {\n");
        
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

