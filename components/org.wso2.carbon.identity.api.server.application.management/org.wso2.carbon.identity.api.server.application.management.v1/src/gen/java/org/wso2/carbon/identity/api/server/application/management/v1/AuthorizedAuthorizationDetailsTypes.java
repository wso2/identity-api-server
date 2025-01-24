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

public class AuthorizedAuthorizationDetailsTypes  {
  
    private String id;
    private String type;
    private String name;

    /**
    * an unique id of the registered authorization details type
    **/
    public AuthorizedAuthorizationDetailsTypes id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "a9403470-dd11-46b4-8db9-aaa31f1d4423", value = "an unique id of the registered authorization details type")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * an unique type of the authorization details type
    **/
    public AuthorizedAuthorizationDetailsTypes type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(example = "payment_initiation", required = true, value = "an unique type of the authorization details type")
    @JsonProperty("type")
    @Valid
    @NotNull(message = "Property type cannot be null.")

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    * display name of the authorization details type
    **/
    public AuthorizedAuthorizationDetailsTypes name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "Payment Initiation", required = true, value = "display name of the authorization details type")
    @JsonProperty("name")
    @Valid
    @NotNull(message = "Property name cannot be null.")
 @Size(min=3,max=255)
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
        AuthorizedAuthorizationDetailsTypes authorizedAuthorizationDetailsTypes = (AuthorizedAuthorizationDetailsTypes) o;
        return Objects.equals(this.id, authorizedAuthorizationDetailsTypes.id) &&
            Objects.equals(this.type, authorizedAuthorizationDetailsTypes.type) &&
            Objects.equals(this.name, authorizedAuthorizationDetailsTypes.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthorizedAuthorizationDetailsTypes {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

