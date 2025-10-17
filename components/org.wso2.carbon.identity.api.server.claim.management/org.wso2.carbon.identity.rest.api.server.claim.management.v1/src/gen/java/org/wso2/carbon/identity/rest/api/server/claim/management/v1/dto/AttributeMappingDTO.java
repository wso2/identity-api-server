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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
    * Claim userstore attribute mapping.
    **/
@ApiModel(description = "Claim userstore attribute mapping.")
public class AttributeMappingDTO {

    @Valid 
    @NotNull(message = "Property mappedAttribute cannot be null.") 
    private String mappedAttribute = null;

    @Valid 
    @NotNull(message = "Property userstore cannot be null.") 
    private String userstore = null;

    /**
    * Userstore attribute to be mapped to.
    **/
    @ApiModelProperty(required = true, value = "Userstore attribute to be mapped to.")
    @JsonProperty("mappedAttribute")
    public String getMappedAttribute() {
        return mappedAttribute;
    }
    public void setMappedAttribute(String mappedAttribute) {
        this.mappedAttribute = mappedAttribute;
    }

    /**
    * Userstore domain name.
    **/
    @ApiModelProperty(required = true, value = "Userstore domain name.")
    @JsonProperty("userstore")
    public String getUserstore() {
        return userstore;
    }
    public void setUserstore(String userstore) {
        this.userstore = userstore;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AttributeMappingDTO {\n");
        
        sb.append("    mappedAttribute: ").append(mappedAttribute).append("\n");
        sb.append("    userstore: ").append(userstore).append("\n");
        
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
        AttributeMappingDTO attributeMappingDTO = (AttributeMappingDTO) o;
        return Objects.equals(this.mappedAttribute, attributeMappingDTO.mappedAttribute)
                && Objects.equals(this.userstore, attributeMappingDTO.userstore);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mappedAttribute, userstore);
    }
}
