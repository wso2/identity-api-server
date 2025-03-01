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

package org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowEngine  {
  
    private String profileName;
    private String workerHostURL;
    private String managerHostURL;
    private String userName;

    /**
    * A unique name for the workflow engine.
    **/
    public WorkflowEngine profileName(String profileName) {

        this.profileName = profileName;
        return this;
    }
    
    @ApiModelProperty(example = "embedded_bps", required = true, value = "A unique name for the workflow engine.")
    @JsonProperty("profileName")
    @Valid
    @NotNull(message = "Property profileName cannot be null.")

    public String getProfileName() {
        return profileName;
    }
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
    * URL of the workflow worker node.
    **/
    public WorkflowEngine workerHostURL(String workerHostURL) {

        this.workerHostURL = workerHostURL;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/services", value = "URL of the workflow worker node.")
    @JsonProperty("workerHostURL")
    @Valid
    public String getWorkerHostURL() {
        return workerHostURL;
    }
    public void setWorkerHostURL(String workerHostURL) {
        this.workerHostURL = workerHostURL;
    }

    /**
    * URL of the workflow manager node.
    **/
    public WorkflowEngine managerHostURL(String managerHostURL) {

        this.managerHostURL = managerHostURL;
        return this;
    }
    
    @ApiModelProperty(example = "https://localhost:9443/services", value = "URL of the workflow manager node.")
    @JsonProperty("managerHostURL")
    @Valid
    public String getManagerHostURL() {
        return managerHostURL;
    }
    public void setManagerHostURL(String managerHostURL) {
        this.managerHostURL = managerHostURL;
    }

    /**
    * Username of the creator of the workflow engine.
    **/
    public WorkflowEngine userName(String userName) {

        this.userName = userName;
        return this;
    }
    
    @ApiModelProperty(example = "challengeQuestion1", value = "Username of the creator of the workflow engine.")
    @JsonProperty("userName")
    @Valid
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowEngine workflowEngine = (WorkflowEngine) o;
        return Objects.equals(this.profileName, workflowEngine.profileName) &&
            Objects.equals(this.workerHostURL, workflowEngine.workerHostURL) &&
            Objects.equals(this.managerHostURL, workflowEngine.managerHostURL) &&
            Objects.equals(this.userName, workflowEngine.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileName, workerHostURL, managerHostURL, userName);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowEngine {\n");
        
        sb.append("    profileName: ").append(toIndentedString(profileName)).append("\n");
        sb.append("    workerHostURL: ").append(toIndentedString(workerHostURL)).append("\n");
        sb.append("    managerHostURL: ").append(toIndentedString(managerHostURL)).append("\n");
        sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
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

