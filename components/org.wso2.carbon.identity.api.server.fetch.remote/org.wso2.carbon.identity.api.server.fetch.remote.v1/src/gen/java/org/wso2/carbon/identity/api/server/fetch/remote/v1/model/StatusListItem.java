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

public class StatusListItem  {
  
    private String itemName;
    private String deployedTime;
    private String deployedStatus;
    private String deploymentErrorReport;

    /**
    **/
    public StatusListItem itemName(String itemName) {

        this.itemName = itemName;
        return this;
    }
    
    @ApiModelProperty(example = "pickup", value = "")
    @JsonProperty("itemName")
    @Valid
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
    **/
    public StatusListItem deployedTime(String deployedTime) {

        this.deployedTime = deployedTime;
        return this;
    }
    
    @ApiModelProperty(example = "2020-03-29 07:36:08.0", value = "")
    @JsonProperty("deployedTime")
    @Valid
    public String getDeployedTime() {
        return deployedTime;
    }
    public void setDeployedTime(String deployedTime) {
        this.deployedTime = deployedTime;
    }

    /**
    **/
    public StatusListItem deployedStatus(String deployedStatus) {

        this.deployedStatus = deployedStatus;
        return this;
    }
    
    @ApiModelProperty(example = "SUCCESS", value = "")
    @JsonProperty("deployedStatus")
    @Valid
    public String getDeployedStatus() {
        return deployedStatus;
    }
    public void setDeployedStatus(String deployedStatus) {
        this.deployedStatus = deployedStatus;
    }

    /**
    **/
    public StatusListItem deploymentErrorReport(String deploymentErrorReport) {

        this.deploymentErrorReport = deploymentErrorReport;
        return this;
    }
    
    @ApiModelProperty(example = "Service Provider Deployment Error Occured", value = "")
    @JsonProperty("deploymentErrorReport")
    @Valid
    public String getDeploymentErrorReport() {
        return deploymentErrorReport;
    }
    public void setDeploymentErrorReport(String deploymentErrorReport) {
        this.deploymentErrorReport = deploymentErrorReport;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusListItem statusListItem = (StatusListItem) o;
        return Objects.equals(this.itemName, statusListItem.itemName) &&
            Objects.equals(this.deployedTime, statusListItem.deployedTime) &&
            Objects.equals(this.deployedStatus, statusListItem.deployedStatus) &&
            Objects.equals(this.deploymentErrorReport, statusListItem.deploymentErrorReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, deployedTime, deployedStatus, deploymentErrorReport);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class StatusListItem {\n");
        
        sb.append("    itemName: ").append(toIndentedString(itemName)).append("\n");
        sb.append("    deployedTime: ").append(toIndentedString(deployedTime)).append("\n");
        sb.append("    deployedStatus: ").append(toIndentedString(deployedStatus)).append("\n");
        sb.append("    deploymentErrorReport: ").append(toIndentedString(deploymentErrorReport)).append("\n");
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

