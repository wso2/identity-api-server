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

public class RemoteFetchConfigurationListItem  {
  
    private String id;
    private Boolean isEnabled = true;
    private String name;
    private String repositoryManagerType;
    private String actionListenerType;
    private String configurationDeployerType;
    private Integer successfulDeployments;
    private Integer failedDeployments;
    private String lastDeployed;

    /**
    **/
    public RemoteFetchConfigurationListItem id(String id) {

        this.id = id;
        return this;
    }
    
    @ApiModelProperty(example = "00000000-13e9-4ed5-afaf-000000000000", value = "")
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
    public RemoteFetchConfigurationListItem isEnabled(Boolean isEnabled) {

        this.isEnabled = isEnabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "")
    @JsonProperty("isEnabled")
    @Valid
    public Boolean getIsEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
    **/
    public RemoteFetchConfigurationListItem name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "testSP", value = "")
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
    public RemoteFetchConfigurationListItem repositoryManagerType(String repositoryManagerType) {

        this.repositoryManagerType = repositoryManagerType;
        return this;
    }
    
    @ApiModelProperty(example = "GIT", value = "")
    @JsonProperty("repositoryManagerType")
    @Valid
    public String getRepositoryManagerType() {
        return repositoryManagerType;
    }
    public void setRepositoryManagerType(String repositoryManagerType) {
        this.repositoryManagerType = repositoryManagerType;
    }

    /**
    **/
    public RemoteFetchConfigurationListItem actionListenerType(String actionListenerType) {

        this.actionListenerType = actionListenerType;
        return this;
    }
    
    @ApiModelProperty(example = "POOLING", value = "")
    @JsonProperty("actionListenerType")
    @Valid
    public String getActionListenerType() {
        return actionListenerType;
    }
    public void setActionListenerType(String actionListenerType) {
        this.actionListenerType = actionListenerType;
    }

    /**
    **/
    public RemoteFetchConfigurationListItem configurationDeployerType(String configurationDeployerType) {

        this.configurationDeployerType = configurationDeployerType;
        return this;
    }
    
    @ApiModelProperty(example = "SP", value = "")
    @JsonProperty("configurationDeployerType")
    @Valid
    public String getConfigurationDeployerType() {
        return configurationDeployerType;
    }
    public void setConfigurationDeployerType(String configurationDeployerType) {
        this.configurationDeployerType = configurationDeployerType;
    }

    /**
    **/
    public RemoteFetchConfigurationListItem successfulDeployments(Integer successfulDeployments) {

        this.successfulDeployments = successfulDeployments;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "")
    @JsonProperty("successfulDeployments")
    @Valid
    public Integer getSuccessfulDeployments() {
        return successfulDeployments;
    }
    public void setSuccessfulDeployments(Integer successfulDeployments) {
        this.successfulDeployments = successfulDeployments;
    }

    /**
    **/
    public RemoteFetchConfigurationListItem failedDeployments(Integer failedDeployments) {

        this.failedDeployments = failedDeployments;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "")
    @JsonProperty("failedDeployments")
    @Valid
    public Integer getFailedDeployments() {
        return failedDeployments;
    }
    public void setFailedDeployments(Integer failedDeployments) {
        this.failedDeployments = failedDeployments;
    }

    /**
    **/
    public RemoteFetchConfigurationListItem lastDeployed(String lastDeployed) {

        this.lastDeployed = lastDeployed;
        return this;
    }
    
    @ApiModelProperty(example = "10-04-2020", value = "")
    @JsonProperty("lastDeployed")
    @Valid
    public String getLastDeployed() {
        return lastDeployed;
    }
    public void setLastDeployed(String lastDeployed) {
        this.lastDeployed = lastDeployed;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RemoteFetchConfigurationListItem remoteFetchConfigurationListItem = (RemoteFetchConfigurationListItem) o;
        return Objects.equals(this.id, remoteFetchConfigurationListItem.id) &&
            Objects.equals(this.isEnabled, remoteFetchConfigurationListItem.isEnabled) &&
            Objects.equals(this.name, remoteFetchConfigurationListItem.name) &&
            Objects.equals(this.repositoryManagerType, remoteFetchConfigurationListItem.repositoryManagerType) &&
            Objects.equals(this.actionListenerType, remoteFetchConfigurationListItem.actionListenerType) &&
            Objects.equals(this.configurationDeployerType, remoteFetchConfigurationListItem.configurationDeployerType) &&
            Objects.equals(this.successfulDeployments, remoteFetchConfigurationListItem.successfulDeployments) &&
            Objects.equals(this.failedDeployments, remoteFetchConfigurationListItem.failedDeployments) &&
            Objects.equals(this.lastDeployed, remoteFetchConfigurationListItem.lastDeployed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isEnabled, name, repositoryManagerType, actionListenerType, configurationDeployerType, successfulDeployments, failedDeployments, lastDeployed);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RemoteFetchConfigurationListItem {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    repositoryManagerType: ").append(toIndentedString(repositoryManagerType)).append("\n");
        sb.append("    actionListenerType: ").append(toIndentedString(actionListenerType)).append("\n");
        sb.append("    configurationDeployerType: ").append(toIndentedString(configurationDeployerType)).append("\n");
        sb.append("    successfulDeployments: ").append(toIndentedString(successfulDeployments)).append("\n");
        sb.append("    failedDeployments: ").append(toIndentedString(failedDeployments)).append("\n");
        sb.append("    lastDeployed: ").append(toIndentedString(lastDeployed)).append("\n");
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

