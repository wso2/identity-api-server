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

package org.wso2.carbon.identity.api.server.identity.governance.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;

/**
 * Meta Data related to each property
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Meta Data related to each property")
public class MetaRes  {
  
    private String type;
    private String regex;

    /**
    * Data type of the property
    **/
    public MetaRes type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "boolean", value = "Data type of the property")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * regular expression
    **/
    public MetaRes regex(String regex) {

        this.regex = regex;
        return this;
    }
    
    @ApiModelProperty(example = "[a-zA-Z0-9]{6}", value = "regular expression")
    @JsonProperty("regex")
    @Valid
    public String getRegex() {
        return regex;
    }
    public void setRegex(String regex) {
        this.regex = regex;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MetaRes metaRes = (MetaRes) o;
        return Objects.equals(this.type, metaRes.type) &&
            Objects.equals(this.regex, metaRes.regex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, regex);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class MetaRes {\n");
        
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    regex: ").append(toIndentedString(regex)).append("\n");
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

