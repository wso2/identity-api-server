/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.identity.api.server.cors.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class CORSApplicationObject  {
  
    private String id = "";
    private String name = "";

    /**
    * The application resource ID.
    **/
    public CORSApplicationObject id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "74070bae-df8c-42bf-8754-5173c237c936", required = true, value = "The application resource ID.")
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
    * The application name.
    **/
    public CORSApplicationObject name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Developer Portal", required = true, value = "The application name.")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CORSApplicationObject coRSApplicationObject = (CORSApplicationObject) o;
        return Objects.equals(this.id, coRSApplicationObject.id) &&
            Objects.equals(this.name, coRSApplicationObject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class CORSApplicationObject {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

