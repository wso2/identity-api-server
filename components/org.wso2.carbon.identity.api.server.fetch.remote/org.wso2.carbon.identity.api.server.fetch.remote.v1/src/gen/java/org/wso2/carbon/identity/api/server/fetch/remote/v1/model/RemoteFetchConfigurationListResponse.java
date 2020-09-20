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
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.RemoteFetchConfigurationListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class RemoteFetchConfigurationListResponse  {
  
    private Integer count;
    private List<RemoteFetchConfigurationListItem> remotefetchConfigurations = null;


    /**
    **/
    public RemoteFetchConfigurationListResponse count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("count")
    @Valid
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
    **/
    public RemoteFetchConfigurationListResponse remotefetchConfigurations(List<RemoteFetchConfigurationListItem> remotefetchConfigurations) {

        this.remotefetchConfigurations = remotefetchConfigurations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("remotefetchConfigurations")
    @Valid
    public List<RemoteFetchConfigurationListItem> getRemotefetchConfigurations() {
        return remotefetchConfigurations;
    }
    public void setRemotefetchConfigurations(List<RemoteFetchConfigurationListItem> remotefetchConfigurations) {
        this.remotefetchConfigurations = remotefetchConfigurations;
    }

    public RemoteFetchConfigurationListResponse addRemotefetchConfigurationsItem(RemoteFetchConfigurationListItem remotefetchConfigurationsItem) {
        if (this.remotefetchConfigurations == null) {
            this.remotefetchConfigurations = new ArrayList<>();
        }
        this.remotefetchConfigurations.add(remotefetchConfigurationsItem);
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
        RemoteFetchConfigurationListResponse remoteFetchConfigurationListResponse = (RemoteFetchConfigurationListResponse) o;
        return Objects.equals(this.count, remoteFetchConfigurationListResponse.count) &&
            Objects.equals(this.remotefetchConfigurations, remoteFetchConfigurationListResponse.remotefetchConfigurations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, remotefetchConfigurations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RemoteFetchConfigurationListResponse {\n");
        
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    remotefetchConfigurations: ").append(toIndentedString(remotefetchConfigurations)).append("\n");
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

