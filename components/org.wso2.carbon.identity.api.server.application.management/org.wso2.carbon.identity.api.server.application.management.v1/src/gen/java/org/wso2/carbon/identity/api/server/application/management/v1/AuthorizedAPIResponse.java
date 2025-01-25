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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedAuthorizationDetailsTypes;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedScope;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class AuthorizedAPIResponse  {
  
    private String id;
    private String identifier;
    private String displayName;
    private String policyId;
    private String type;
    private List<AuthorizedScope> authorizedScopes = null;

    private List<AuthorizedAuthorizationDetailsTypes> authorizedAuthorizationDetailsTypes = null;


    /**
    **/
    public AuthorizedAPIResponse id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "012df-232gf-545fg-dff23", value = "")
    @JsonProperty("id")
    @Valid
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    /**
    **/
    public AuthorizedAPIResponse identifier(String identifier) {

        this.identifier = identifier;
        return this;
    }
    
    @ApiModelProperty(example = "https://greetings.io/v1/greet", value = "")
    @JsonProperty("identifier")
    @Valid
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
    **/
    public AuthorizedAPIResponse displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Greetings API", value = "")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    **/
    public AuthorizedAPIResponse policyId(String policyId) {

        this.policyId = policyId;
        return this;
    }
    
    @ApiModelProperty(example = "RBAC", value = "")
    @JsonProperty("policyId")
    @Valid
    public String getPolicyId() {
        return policyId;
    }
    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    /**
    **/
    public AuthorizedAPIResponse type(String type) {

        this.type = type;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("type")
    @Valid
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    /**
    **/
    public AuthorizedAPIResponse authorizedScopes(List<AuthorizedScope> authorizedScopes) {

        this.authorizedScopes = authorizedScopes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("authorizedScopes")
    @Valid
    public List<AuthorizedScope> getAuthorizedScopes() {
        return authorizedScopes;
    }
    public void setAuthorizedScopes(List<AuthorizedScope> authorizedScopes) {
        this.authorizedScopes = authorizedScopes;
    }

    public AuthorizedAPIResponse addAuthorizedScopesItem(AuthorizedScope authorizedScopesItem) {
        if (this.authorizedScopes == null) {
            this.authorizedScopes = new ArrayList<>();
        }
        this.authorizedScopes.add(authorizedScopesItem);
        return this;
    }

        /**
    **/
    public AuthorizedAPIResponse authorizedAuthorizationDetailsTypes(List<AuthorizedAuthorizationDetailsTypes> authorizedAuthorizationDetailsTypes) {

        this.authorizedAuthorizationDetailsTypes = authorizedAuthorizationDetailsTypes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("authorizedAuthorizationDetailsTypes")
    @Valid
    public List<AuthorizedAuthorizationDetailsTypes> getAuthorizedAuthorizationDetailsTypes() {
        return authorizedAuthorizationDetailsTypes;
    }
    public void setAuthorizedAuthorizationDetailsTypes(List<AuthorizedAuthorizationDetailsTypes> authorizedAuthorizationDetailsTypes) {
        this.authorizedAuthorizationDetailsTypes = authorizedAuthorizationDetailsTypes;
    }

    public AuthorizedAPIResponse addAuthorizedAuthorizationDetailsTypesItem(AuthorizedAuthorizationDetailsTypes authorizedAuthorizationDetailsTypesItem) {
        if (this.authorizedAuthorizationDetailsTypes == null) {
            this.authorizedAuthorizationDetailsTypes = new ArrayList<>();
        }
        this.authorizedAuthorizationDetailsTypes.add(authorizedAuthorizationDetailsTypesItem);
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
        AuthorizedAPIResponse authorizedAPIResponse = (AuthorizedAPIResponse) o;
        return Objects.equals(this.id, authorizedAPIResponse.id) &&
            Objects.equals(this.identifier, authorizedAPIResponse.identifier) &&
            Objects.equals(this.displayName, authorizedAPIResponse.displayName) &&
            Objects.equals(this.policyId, authorizedAPIResponse.policyId) &&
            Objects.equals(this.type, authorizedAPIResponse.type) &&
            Objects.equals(this.authorizedScopes, authorizedAPIResponse.authorizedScopes) &&
            Objects.equals(this.authorizedAuthorizationDetailsTypes, authorizedAPIResponse.authorizedAuthorizationDetailsTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, displayName, policyId, type, authorizedScopes, authorizedAuthorizationDetailsTypes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AuthorizedAPIResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    policyId: ").append(toIndentedString(policyId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    authorizedScopes: ").append(toIndentedString(authorizedScopes)).append("\n");
        sb.append("    authorizedAuthorizationDetailsTypes: ").append(toIndentedString(authorizedAuthorizationDetailsTypes)).append("\n");
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

