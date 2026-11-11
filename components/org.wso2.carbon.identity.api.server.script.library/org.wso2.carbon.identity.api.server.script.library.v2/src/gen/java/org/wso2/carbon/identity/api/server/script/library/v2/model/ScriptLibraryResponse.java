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
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ScriptLibraryResponse  {
  
    private String id;
    private String name;
    private String description;
    private String contentRef;

    /**
    * Id of the script library (base64url encoded name).
    **/
    public ScriptLibraryResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "bGVnYWN5IGxpYi5qcw", required = true, value = "Id of the script library (base64url encoded name).")
    @JsonProperty("id")
    @Valid
    @NotNull(message = "Property id cannot be null.")

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Name of the script library.
    **/
    public ScriptLibraryResponse name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "script library name", required = true, value = "Name of the script library.")
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
    * Description of the script library.
    **/
    public ScriptLibraryResponse description(String description) {

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
    * Reference of the content of the script library.
    **/
    public ScriptLibraryResponse contentRef(String contentRef) {

        this.contentRef = contentRef;
        return this;
    }
    
    @ApiModelProperty(example = "script library content reference", required = true, value = "Reference of the content of the script library.")
    @JsonProperty("content-ref")
    @Valid
    @NotNull(message = "Property contentRef cannot be null.")

    public String getContentRef() {
        return contentRef;
    }
    public void setContentRef(String contentRef) {
        this.contentRef = contentRef;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScriptLibraryResponse scriptLibraryResponse = (ScriptLibraryResponse) o;
        return Objects.equals(this.id, scriptLibraryResponse.id) &&
            Objects.equals(this.name, scriptLibraryResponse.name) &&
            Objects.equals(this.description, scriptLibraryResponse.description) &&
            Objects.equals(this.contentRef, scriptLibraryResponse.contentRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, contentRef);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ScriptLibraryResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    contentRef: ").append(toIndentedString(contentRef)).append("\n");
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

