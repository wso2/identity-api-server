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

public class RepositoryManagerAttributes  {
  
    private String accessToken;
    private String username;
    private String uri;
    private String branch;
    private String directory;

    /**
    **/
    public RepositoryManagerAttributes accessToken(String accessToken) {

        this.accessToken = accessToken;
        return this;
    }
    
    @ApiModelProperty(example = "sample access token", value = "")
    @JsonProperty("accessToken")
    @Valid
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
    **/
    public RepositoryManagerAttributes username(String username) {

        this.username = username;
        return this;
    }
    
    @ApiModelProperty(example = "Jhon Doe", value = "")
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
    public RepositoryManagerAttributes uri(String uri) {

        this.uri = uri;
        return this;
    }
    
    @ApiModelProperty(example = "https://github.com/TestUser/TestGit.git", value = "")
    @JsonProperty("uri")
    @Valid
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
    **/
    public RepositoryManagerAttributes branch(String branch) {

        this.branch = branch;
        return this;
    }
    
    @ApiModelProperty(example = "master", value = "")
    @JsonProperty("branch")
    @Valid
    public String getBranch() {
        return branch;
    }
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
    **/
    public RepositoryManagerAttributes directory(String directory) {

        this.directory = directory;
        return this;
    }
    
    @ApiModelProperty(example = "SP/", value = "")
    @JsonProperty("directory")
    @Valid
    public String getDirectory() {
        return directory;
    }
    public void setDirectory(String directory) {
        this.directory = directory;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RepositoryManagerAttributes repositoryManagerAttributes = (RepositoryManagerAttributes) o;
        return Objects.equals(this.accessToken, repositoryManagerAttributes.accessToken) &&
            Objects.equals(this.username, repositoryManagerAttributes.username) &&
            Objects.equals(this.uri, repositoryManagerAttributes.uri) &&
            Objects.equals(this.branch, repositoryManagerAttributes.branch) &&
            Objects.equals(this.directory, repositoryManagerAttributes.directory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, username, uri, branch, directory);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RepositoryManagerAttributes {\n");
        
        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
        sb.append("    branch: ").append(toIndentedString(branch)).append("\n");
        sb.append("    directory: ").append(toIndentedString(directory)).append("\n");
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

