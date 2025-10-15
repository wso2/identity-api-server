/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto;

import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@ApiModel(description = "")
public class PropertyDTO {

    @Valid 
    @NotNull(message = "Property key cannot be null.") 
    private String key = null;

    @Valid 
    @NotNull(message = "Property value cannot be null.") 
    private String value = null;

    /**
    **/
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("key")
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    /**
    **/
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("value")
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PropertyDTO {\n");
        
        sb.append("    key: ").append(key).append("\n");
        sb.append("    value: ").append(value).append("\n");
        
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PropertyDTO propertyDTO = (PropertyDTO) o;
        return Objects.equals(this.key, propertyDTO.key) && Objects.equals(this.value, propertyDTO.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, value);
    }
}
