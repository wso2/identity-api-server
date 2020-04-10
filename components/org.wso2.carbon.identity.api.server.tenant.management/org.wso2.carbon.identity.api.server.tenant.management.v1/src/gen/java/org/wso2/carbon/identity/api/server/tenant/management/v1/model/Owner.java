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

package org.wso2.carbon.identity.api.server.tenant.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.tenant.management.v1.model.AdditionalClaims;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Owner  {
  
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private List<AdditionalClaims> additionalClaims = null;


    /**
    * Username for the tenant owner.
    **/
    public Owner username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "kim", required = true, value = "Username for the tenant owner.")
    @JsonProperty("username")
    @Valid
    @NotNull(message = "Property username cannot be null.")

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    * Email address of the owner.
    **/
    public Owner email(String email) {

        this.email = email;
        return this;
    }
    
    @ApiModelProperty(example = "kim@wso2.com", required = true, value = "Email address of the owner.")
    @JsonProperty("email")
    @Valid
    @NotNull(message = "Property email cannot be null.")

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /**
    * First name of the owner.
    **/
    public Owner firstname(String firstname) {

        this.firstname = firstname;
        return this;
    }
    
    @ApiModelProperty(example = "kim", required = true, value = "First name of the owner.")
    @JsonProperty("firstname")
    @Valid
    @NotNull(message = "Property firstname cannot be null.")

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
    * Last name of the owner.
    **/
    public Owner lastname(String lastname) {

        this.lastname = lastname;
        return this;
    }
    
    @ApiModelProperty(example = "kim", required = true, value = "Last name of the owner.")
    @JsonProperty("lastname")
    @Valid
    @NotNull(message = "Property lastname cannot be null.")

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
    **/
    public Owner additionalClaims(List<AdditionalClaims> additionalClaims) {

        this.additionalClaims = additionalClaims;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("additionalClaims")
    @Valid
    public List<AdditionalClaims> getAdditionalClaims() {
        return additionalClaims;
    }
    public void setAdditionalClaims(List<AdditionalClaims> additionalClaims) {
        this.additionalClaims = additionalClaims;
    }

    public Owner addAdditionalClaimsItem(AdditionalClaims additionalClaimsItem) {
        if (this.additionalClaims == null) {
            this.additionalClaims = new ArrayList<>();
        }
        this.additionalClaims.add(additionalClaimsItem);
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
        Owner owner = (Owner) o;
        return Objects.equals(this.username, owner.username) &&
            Objects.equals(this.email, owner.email) &&
            Objects.equals(this.firstname, owner.firstname) &&
            Objects.equals(this.lastname, owner.lastname) &&
            Objects.equals(this.additionalClaims, owner.additionalClaims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, firstname, lastname, additionalClaims);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Owner {\n");
        
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
        sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
        sb.append("    additionalClaims: ").append(toIndentedString(additionalClaims)).append("\n");
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

