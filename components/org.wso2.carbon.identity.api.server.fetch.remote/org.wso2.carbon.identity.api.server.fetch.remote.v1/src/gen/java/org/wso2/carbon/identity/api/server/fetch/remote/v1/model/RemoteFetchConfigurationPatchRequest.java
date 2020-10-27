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

public class RemoteFetchConfigurationPatchRequest  {
  
    private String remoteFetchName;
    private Boolean isEnabled = true;

    /**
    **/
    public RemoteFetchConfigurationPatchRequest remoteFetchName(String remoteFetchName) {

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
    public RemoteFetchConfigurationPatchRequest isEnabled(Boolean isEnabled) {

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



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RemoteFetchConfigurationPatchRequest remoteFetchConfigurationPatchRequest = (RemoteFetchConfigurationPatchRequest) o;
        return Objects.equals(this.remoteFetchName, remoteFetchConfigurationPatchRequest.remoteFetchName) &&
            Objects.equals(this.isEnabled, remoteFetchConfigurationPatchRequest.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remoteFetchName, isEnabled);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class RemoteFetchConfigurationPatchRequest {\n");
        
        sb.append("    remoteFetchName: ").append(toIndentedString(remoteFetchName)).append("\n");
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
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

