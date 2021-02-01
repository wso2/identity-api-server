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

package org.wso2.carbon.identity.api.server.fetch.remote.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class PushEventWebHookPOSTRequestAuthor  {
  
    private String username;
    private String name;
    private String email;

    /**
    **/
    public PushEventWebHookPOSTRequestAuthor username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("username")
    @Valid
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestAuthor name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    **/
    public PushEventWebHookPOSTRequestAuthor email(String email) {

        this.email = email;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("email")
    @Valid
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PushEventWebHookPOSTRequestAuthor pushEventWebHookPOSTRequestAuthor = (PushEventWebHookPOSTRequestAuthor) o;
        return Objects.equals(this.username, pushEventWebHookPOSTRequestAuthor.username) &&
            Objects.equals(this.name, pushEventWebHookPOSTRequestAuthor.name) &&
            Objects.equals(this.email, pushEventWebHookPOSTRequestAuthor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, email);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PushEventWebHookPOSTRequestAuthor {\n");
        
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
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

