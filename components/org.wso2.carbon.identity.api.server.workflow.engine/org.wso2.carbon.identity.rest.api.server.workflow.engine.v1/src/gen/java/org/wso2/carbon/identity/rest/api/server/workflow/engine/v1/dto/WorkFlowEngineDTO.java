/*
 * Copyright (c) 2019, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.rest.api.server.workflow.engine.v1.dto;


import io.swagger.annotations.*;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;





@ApiModel(description = "")
public class WorkFlowEngineDTO  {
  
  
  @NotNull 
  private String profileName = null;
  
  
  private String workerHostURL = null;
  
  
  private String managerHostURL = null;
  
  
  private String userName = null;

  
  /**
   * A unique name for the workflow engine.
   **/
  @ApiModelProperty(required = true, value = "A unique name for the workflow engine.")
  @JsonProperty("profileName")
  public String getProfileName() {
    return profileName;
  }
  public void setProfileName(String profileName) {
    this.profileName = profileName;
  }

  
  /**
   * URL of the workflow worker node.
   **/
  @ApiModelProperty(value = "URL of the workflow worker node.")
  @JsonProperty("workerHostURL")
  public String getWorkerHostURL() {
    return workerHostURL;
  }
  public void setWorkerHostURL(String workerHostURL) {
    this.workerHostURL = workerHostURL;
  }

  
  /**
   * URL of the workflow manager node.
   **/
  @ApiModelProperty(value = "URL of the workflow manager node.")
  @JsonProperty("managerHostURL")
  public String getManagerHostURL() {
    return managerHostURL;
  }
  public void setManagerHostURL(String managerHostURL) {
    this.managerHostURL = managerHostURL;
  }

  
  /**
   * Username of the creator of the workflow engine.
   **/
  @ApiModelProperty(value = "Username of the creator of the workflow engine.")
  @JsonProperty("userName")
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorkFlowEngineDTO {\n");
    
    sb.append("  profileName: ").append(profileName).append("\n");
    sb.append("  workerHostURL: ").append(workerHostURL).append("\n");
    sb.append("  managerHostURL: ").append(managerHostURL).append("\n");
    sb.append("  userName: ").append(userName).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
