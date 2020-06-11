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

package org.wso2.carbon.identity.api.server.media.service.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class ResourceFilesMetadataFileSecurity  {
  
    private Boolean allowedAll;
    private List<String> allowedUsers = null;

    private List<String> allowedScopes = null;


    /**
    * Defines whether the file is publically available for access or has restricted access.
    **/
    public ResourceFilesMetadataFileSecurity allowedAll(Boolean allowedAll) {

        this.allowedAll = allowedAll;
        return this;
    }
    
    @ApiModelProperty(example = "false", value = "Defines whether the file is publically available for access or has restricted access.")
    @JsonProperty("allowedAll")
    @Valid
    public Boolean getAllowedAll() {
        return allowedAll;
    }
    public void setAllowedAll(Boolean allowedAll) {
        this.allowedAll = allowedAll;
    }

    /**
    * The set of users entitled to access the file.
    **/
    public ResourceFilesMetadataFileSecurity allowedUsers(List<String> allowedUsers) {

        this.allowedUsers = allowedUsers;
        return this;
    }
    
    @ApiModelProperty(example = "[\"user1\",\"user2\"]", value = "The set of users entitled to access the file.")
    @JsonProperty("allowedUsers")
    @Valid
    public List<String> getAllowedUsers() {
        return allowedUsers;
    }
    public void setAllowedUsers(List<String> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public ResourceFilesMetadataFileSecurity addAllowedUsersItem(String allowedUsersItem) {
        if (this.allowedUsers == null) {
            this.allowedUsers = new ArrayList<>();
        }
        this.allowedUsers.add(allowedUsersItem);
        return this;
    }

        /**
    * Allowed set of scopes to access the file.
    **/
    public ResourceFilesMetadataFileSecurity allowedScopes(List<String> allowedScopes) {

        this.allowedScopes = allowedScopes;
        return this;
    }
    
    @ApiModelProperty(example = "[\"internal_login\"]", value = "Allowed set of scopes to access the file.")
    @JsonProperty("allowedScopes")
    @Valid
    public List<String> getAllowedScopes() {
        return allowedScopes;
    }
    public void setAllowedScopes(List<String> allowedScopes) {
        this.allowedScopes = allowedScopes;
    }

    public ResourceFilesMetadataFileSecurity addAllowedScopesItem(String allowedScopesItem) {
        if (this.allowedScopes == null) {
            this.allowedScopes = new ArrayList<>();
        }
        this.allowedScopes.add(allowedScopesItem);
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
        ResourceFilesMetadataFileSecurity resourceFilesMetadataFileSecurity = (ResourceFilesMetadataFileSecurity) o;
        return Objects.equals(this.allowedAll, resourceFilesMetadataFileSecurity.allowedAll) &&
            Objects.equals(this.allowedUsers, resourceFilesMetadataFileSecurity.allowedUsers) &&
            Objects.equals(this.allowedScopes, resourceFilesMetadataFileSecurity.allowedScopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedAll, allowedUsers, allowedScopes);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResourceFilesMetadataFileSecurity {\n");
        
        sb.append("    allowedAll: ").append(toIndentedString(allowedAll)).append("\n");
        sb.append("    allowedUsers: ").append(toIndentedString(allowedUsers)).append("\n");
        sb.append("    allowedScopes: ").append(toIndentedString(allowedScopes)).append("\n");
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

