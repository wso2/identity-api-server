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
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.ActionListener;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.ConfigurationDeployer;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RepositoryManager;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RemoteFetchConfigurationPOSTRequest  {
  
    private String remoteFetchName;
    private Boolean isEnabled = true;
    private RepositoryManager repositoryManager;
    private ActionListener actionListener;
    private ConfigurationDeployer configurationDeployer;

    /**
    **/
    public RemoteFetchConfigurationPOSTRequest remoteFetchName(String remoteFetchName) {

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
    public RemoteFetchConfigurationPOSTRequest isEnabled(Boolean isEnabled) {

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
    public RemoteFetchConfigurationPOSTRequest repositoryManager(RepositoryManager repositoryManager) {

        this.repositoryManager = repositoryManager;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("repositoryManager")
    @Valid
    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }
    public void setRepositoryManager(RepositoryManager repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    /**
    **/
    public RemoteFetchConfigurationPOSTRequest actionListener(ActionListener actionListener) {

        this.actionListener = actionListener;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("actionListener")
    @Valid
    public ActionListener getActionListener() {
        return actionListener;
    }
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
    **/
    public RemoteFetchConfigurationPOSTRequest configurationDeployer(ConfigurationDeployer configurationDeployer) {

        this.configurationDeployer = configurationDeployer;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("configurationDeployer")
    @Valid
    public ConfigurationDeployer getConfigurationDeployer() {
        return configurationDeployer;
    }
    public void setConfigurationDeployer(ConfigurationDeployer configurationDeployer) {
        this.configurationDeployer = configurationDeployer;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RemoteFetchConfigurationPOSTRequest remoteFetchConfigurationPOSTRequest = (RemoteFetchConfigurationPOSTRequest) o;
        return Objects.equals(this.remoteFetchName, remoteFetchConfigurationPOSTRequest.remoteFetchName) &&
            Objects.equals(this.isEnabled, remoteFetchConfigurationPOSTRequest.isEnabled) &&
            Objects.equals(this.repositoryManager, remoteFetchConfigurationPOSTRequest.repositoryManager) &&
            Objects.equals(this.actionListener, remoteFetchConfigurationPOSTRequest.actionListener) &&
            Objects.equals(this.configurationDeployer, remoteFetchConfigurationPOSTRequest.configurationDeployer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remoteFetchName, isEnabled, repositoryManager, actionListener, configurationDeployer);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RemoteFetchConfigurationPOSTRequest {\n");
        
        sb.append("    remoteFetchName: ").append(toIndentedString(remoteFetchName)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    repositoryManager: ").append(toIndentedString(repositoryManager)).append("\n");
        sb.append("    actionListener: ").append(toIndentedString(actionListener)).append("\n");
        sb.append("    configurationDeployer: ").append(toIndentedString(configurationDeployer)).append("\n");
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

