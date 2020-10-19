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
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.ActionListenerAttributes;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RepositoryManagerAttributes;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.StatusListResponse;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RemoteFetchConfigurationGetResponse  {
  
    private String id;
    private Boolean isEnabled = true;
    private String remoteFetchName;
    private String repositoryManagerType;
    private String actionListenerType;
    private String configurationDeployerType;
    private RepositoryManagerAttributes repositoryManagerAttributes;
    private ActionListenerAttributes actionListenerAttributes;
    private Object configurationDeployerAttributes;
    private StatusListResponse status;

    /**
    **/
    public RemoteFetchConfigurationGetResponse id(String id) {

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
    public RemoteFetchConfigurationGetResponse isEnabled(Boolean isEnabled) {

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
    public RemoteFetchConfigurationGetResponse remoteFetchName(String remoteFetchName) {

        this.remoteFetchName = remoteFetchName;
        return this;
    }
    
    @ApiModelProperty(example = "testSP", value = "")
    @JsonProperty("remoteFetchName")
    @Valid
    public String getRemoteFetchName() {
        return remoteFetchName;
    }
    public void setRemoteFetchName(String remoteFetchName) {
        this.remoteFetchName = remoteFetchName;
    }

    /**
    **/
    public RemoteFetchConfigurationGetResponse repositoryManagerType(String repositoryManagerType) {

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
    public RemoteFetchConfigurationGetResponse actionListenerType(String actionListenerType) {

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
    public RemoteFetchConfigurationGetResponse configurationDeployerType(String configurationDeployerType) {

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
    public RemoteFetchConfigurationGetResponse repositoryManagerAttributes(RepositoryManagerAttributes repositoryManagerAttributes) {

        this.repositoryManagerAttributes = repositoryManagerAttributes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("repositoryManagerAttributes")
    @Valid
    public RepositoryManagerAttributes getRepositoryManagerAttributes() {
        return repositoryManagerAttributes;
    }
    public void setRepositoryManagerAttributes(RepositoryManagerAttributes repositoryManagerAttributes) {
        this.repositoryManagerAttributes = repositoryManagerAttributes;
    }

    /**
    **/
    public RemoteFetchConfigurationGetResponse actionListenerAttributes(ActionListenerAttributes actionListenerAttributes) {

        this.actionListenerAttributes = actionListenerAttributes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("actionListenerAttributes")
    @Valid
    public ActionListenerAttributes getActionListenerAttributes() {
        return actionListenerAttributes;
    }
    public void setActionListenerAttributes(ActionListenerAttributes actionListenerAttributes) {
        this.actionListenerAttributes = actionListenerAttributes;
    }

    /**
    **/
    public RemoteFetchConfigurationGetResponse configurationDeployerAttributes(Object configurationDeployerAttributes) {

        this.configurationDeployerAttributes = configurationDeployerAttributes;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("configurationDeployerAttributes")
    @Valid
    public Object getConfigurationDeployerAttributes() {
        return configurationDeployerAttributes;
    }
    public void setConfigurationDeployerAttributes(Object configurationDeployerAttributes) {
        this.configurationDeployerAttributes = configurationDeployerAttributes;
    }

    /**
    **/
    public RemoteFetchConfigurationGetResponse status(StatusListResponse status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("status")
    @Valid
    public StatusListResponse getStatus() {
        return status;
    }
    public void setStatus(StatusListResponse status) {
        this.status = status;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RemoteFetchConfigurationGetResponse remoteFetchConfigurationGetResponse = (RemoteFetchConfigurationGetResponse) o;
        return Objects.equals(this.id, remoteFetchConfigurationGetResponse.id) &&
            Objects.equals(this.isEnabled, remoteFetchConfigurationGetResponse.isEnabled) &&
            Objects.equals(this.remoteFetchName, remoteFetchConfigurationGetResponse.remoteFetchName) &&
            Objects.equals(this.repositoryManagerType, remoteFetchConfigurationGetResponse.repositoryManagerType) &&
            Objects.equals(this.actionListenerType, remoteFetchConfigurationGetResponse.actionListenerType) &&
            Objects.equals(this.configurationDeployerType, remoteFetchConfigurationGetResponse.configurationDeployerType) &&
            Objects.equals(this.repositoryManagerAttributes, remoteFetchConfigurationGetResponse.repositoryManagerAttributes) &&
            Objects.equals(this.actionListenerAttributes, remoteFetchConfigurationGetResponse.actionListenerAttributes) &&
            Objects.equals(this.configurationDeployerAttributes, remoteFetchConfigurationGetResponse.configurationDeployerAttributes) &&
            Objects.equals(this.status, remoteFetchConfigurationGetResponse.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isEnabled, remoteFetchName, repositoryManagerType, actionListenerType, configurationDeployerType, repositoryManagerAttributes, actionListenerAttributes, configurationDeployerAttributes, status);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RemoteFetchConfigurationGetResponse {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    remoteFetchName: ").append(toIndentedString(remoteFetchName)).append("\n");
        sb.append("    repositoryManagerType: ").append(toIndentedString(repositoryManagerType)).append("\n");
        sb.append("    actionListenerType: ").append(toIndentedString(actionListenerType)).append("\n");
        sb.append("    configurationDeployerType: ").append(toIndentedString(configurationDeployerType)).append("\n");
        sb.append("    repositoryManagerAttributes: ").append(toIndentedString(repositoryManagerAttributes)).append("\n");
        sb.append("    actionListenerAttributes: ").append(toIndentedString(actionListenerAttributes)).append("\n");
        sb.append("    configurationDeployerAttributes: ").append(toIndentedString(configurationDeployerAttributes)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

