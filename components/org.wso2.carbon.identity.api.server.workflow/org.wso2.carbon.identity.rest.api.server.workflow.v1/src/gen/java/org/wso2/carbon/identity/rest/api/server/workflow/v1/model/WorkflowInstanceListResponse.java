/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.server.workflow.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowInstanceListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowInstanceListResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<WorkflowInstanceListItem> instances = null;


    /**
    **/
    public WorkflowInstanceListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("totalResults")
    @Valid
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
    **/
    public WorkflowInstanceListResponse count(Integer count) {

        this.count = count;
        return this;
    }
    
    @ApiModelProperty(value = "")
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
    public WorkflowInstanceListResponse instances(List<WorkflowInstanceListItem> instances) {

        this.instances = instances;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("instances")
    @Valid
    public List<WorkflowInstanceListItem> getInstances() {
        return instances;
    }
    public void setInstances(List<WorkflowInstanceListItem> instances) {
        this.instances = instances;
    }

    public WorkflowInstanceListResponse addInstancesItem(WorkflowInstanceListItem instancesItem) {
        if (this.instances == null) {
            this.instances = new ArrayList<>();
        }
        this.instances.add(instancesItem);
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
        WorkflowInstanceListResponse workflowInstanceListResponse = (WorkflowInstanceListResponse) o;
        return Objects.equals(this.totalResults, workflowInstanceListResponse.totalResults) &&
            Objects.equals(this.count, workflowInstanceListResponse.count) &&
            Objects.equals(this.instances, workflowInstanceListResponse.instances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, count, instances);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowInstanceListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    instances: ").append(toIndentedString(instances)).append("\n");
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

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }
}

