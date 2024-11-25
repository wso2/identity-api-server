/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

public class OwnerInfoResponse  {
  
    private String id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private List<AdditionalClaims> additionalClaims = null;


    /**
    * id of the tenant owner.
    **/
    public OwnerInfoResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "4875-jhgr-454hb", value = "id of the tenant owner.")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    * Username for the tenant owner.
    **/
    public OwnerInfoResponse username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "kim", value = "Username for the tenant owner.")
    @JsonProperty("username")
    @Valid
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    /**
    * Email address of the owner.
    **/
    public OwnerInfoResponse email(String email) {

        this.email = email;
        return this;
    }
    
    @ApiModelProperty(example = "kim@wso2.com", value = "Email address of the owner.")
    @JsonProperty("email")
    @Valid
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /**
    * First name of the owner.
    **/
    public OwnerInfoResponse firstname(String firstname) {

        this.firstname = firstname;
        return this;
    }
    
    @ApiModelProperty(example = "kim", value = "First name of the owner.")
    @JsonProperty("firstname")
    @Valid
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
    * Last name of the owner.
    **/
    public OwnerInfoResponse lastname(String lastname) {

        this.lastname = lastname;
        return this;
    }
    
    @ApiModelProperty(example = "kim", value = "Last name of the owner.")
    @JsonProperty("lastname")
    @Valid
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
    **/
    public OwnerInfoResponse additionalClaims(List<AdditionalClaims> additionalClaims) {

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

    public OwnerInfoResponse addAdditionalClaimsItem(AdditionalClaims additionalClaimsItem) {
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
        OwnerInfoResponse ownerInfoResponse = (OwnerInfoResponse) o;
        return Objects.equals(this.id, ownerInfoResponse.id) &&
            Objects.equals(this.username, ownerInfoResponse.username) &&
            Objects.equals(this.email, ownerInfoResponse.email) &&
            Objects.equals(this.firstname, ownerInfoResponse.firstname) &&
            Objects.equals(this.lastname, ownerInfoResponse.lastname) &&
            Objects.equals(this.additionalClaims, ownerInfoResponse.additionalClaims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, firstname, lastname, additionalClaims);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OwnerInfoResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

