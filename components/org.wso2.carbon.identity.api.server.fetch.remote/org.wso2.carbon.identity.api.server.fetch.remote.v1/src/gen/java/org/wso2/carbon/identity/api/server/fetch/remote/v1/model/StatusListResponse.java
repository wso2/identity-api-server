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
import org.wso2.carbon.identity.api.server.fetch.remote.v1.model.StatusListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class StatusListResponse  {
  
    private Integer count;
    private Integer successfulDeployments;
    private Integer failedDeployments;
    private String lastSynchronizedTime;
    private List<StatusListItem> remoteFetchRevisionStatuses = null;


    /**
    **/
    public StatusListResponse count(Integer count) {

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
    public StatusListResponse successfulDeployments(Integer successfulDeployments) {

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
    public StatusListResponse failedDeployments(Integer failedDeployments) {

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
    public StatusListResponse lastSynchronizedTime(String lastSynchronizedTime) {

        this.lastSynchronizedTime = lastSynchronizedTime;
        return this;
    }
    
    @ApiModelProperty(example = "2020-03-29 07:36:08.0", value = "")
    @JsonProperty("lastSynchronizedTime")
    @Valid
    public String getLastSynchronizedTime() {
        return lastSynchronizedTime;
    }
    public void setLastSynchronizedTime(String lastSynchronizedTime) {
        this.lastSynchronizedTime = lastSynchronizedTime;
    }

    /**
    **/
    public StatusListResponse remoteFetchRevisionStatuses(List<StatusListItem> remoteFetchRevisionStatuses) {

        this.remoteFetchRevisionStatuses = remoteFetchRevisionStatuses;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("remoteFetchRevisionStatuses")
    @Valid
    public List<StatusListItem> getRemoteFetchRevisionStatuses() {
        return remoteFetchRevisionStatuses;
    }
    public void setRemoteFetchRevisionStatuses(List<StatusListItem> remoteFetchRevisionStatuses) {
        this.remoteFetchRevisionStatuses = remoteFetchRevisionStatuses;
    }

    public StatusListResponse addRemoteFetchRevisionStatusesItem(StatusListItem remoteFetchRevisionStatusesItem) {
        if (this.remoteFetchRevisionStatuses == null) {
            this.remoteFetchRevisionStatuses = new ArrayList<>();
        }
        this.remoteFetchRevisionStatuses.add(remoteFetchRevisionStatusesItem);
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
        StatusListResponse statusListResponse = (StatusListResponse) o;
        return Objects.equals(this.count, statusListResponse.count) &&
            Objects.equals(this.successfulDeployments, statusListResponse.successfulDeployments) &&
            Objects.equals(this.failedDeployments, statusListResponse.failedDeployments) &&
            Objects.equals(this.lastSynchronizedTime, statusListResponse.lastSynchronizedTime) &&
            Objects.equals(this.remoteFetchRevisionStatuses, statusListResponse.remoteFetchRevisionStatuses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, successfulDeployments, failedDeployments, lastSynchronizedTime, remoteFetchRevisionStatuses);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class StatusListResponse {\n");
        
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    successfulDeployments: ").append(toIndentedString(successfulDeployments)).append("\n");
        sb.append("    failedDeployments: ").append(toIndentedString(failedDeployments)).append("\n");
        sb.append("    lastSynchronizedTime: ").append(toIndentedString(lastSynchronizedTime)).append("\n");
        sb.append("    remoteFetchRevisionStatuses: ").append(toIndentedString(remoteFetchRevisionStatuses)).append("\n");
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

