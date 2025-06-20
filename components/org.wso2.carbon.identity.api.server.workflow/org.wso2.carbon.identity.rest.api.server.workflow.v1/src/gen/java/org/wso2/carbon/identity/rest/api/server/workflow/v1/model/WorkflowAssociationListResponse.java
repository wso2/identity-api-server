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
import org.wso2.carbon.identity.rest.api.server.workflow.v1.model.WorkflowAssociationListItem;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class WorkflowAssociationListResponse  {
  
    private Integer totalResults;
    private Integer startIndex;
    private Integer count;
    private List<WorkflowAssociationListItem> workflowAssociations = null;


    /**
    **/
    public WorkflowAssociationListResponse totalResults(Integer totalResults) {

        this.totalResults = totalResults;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
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
    public WorkflowAssociationListResponse startIndex(Integer startIndex) {

        this.startIndex = startIndex;
        return this;
    }
    
    @ApiModelProperty(example = "1", value = "")
    @JsonProperty("startIndex")
    @Valid
    public Integer getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
    **/
    public WorkflowAssociationListResponse count(Integer count) {

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
    public WorkflowAssociationListResponse workflowAssociations(List<WorkflowAssociationListItem> workflowAssociations) {

        this.workflowAssociations = workflowAssociations;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("workflowAssociations")
    @Valid
    public List<WorkflowAssociationListItem> getWorkflowAssociations() {
        return workflowAssociations;
    }
    public void setWorkflowAssociations(List<WorkflowAssociationListItem> workflowAssociations) {
        this.workflowAssociations = workflowAssociations;
    }

    public WorkflowAssociationListResponse addWorkflowAssociationsItem(WorkflowAssociationListItem workflowAssociationsItem) {
        if (this.workflowAssociations == null) {
            this.workflowAssociations = new ArrayList<>();
        }
        this.workflowAssociations.add(workflowAssociationsItem);
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
        WorkflowAssociationListResponse workflowAssociationListResponse = (WorkflowAssociationListResponse) o;
        return Objects.equals(this.totalResults, workflowAssociationListResponse.totalResults) &&
            Objects.equals(this.startIndex, workflowAssociationListResponse.startIndex) &&
            Objects.equals(this.count, workflowAssociationListResponse.count) &&
            Objects.equals(this.workflowAssociations, workflowAssociationListResponse.workflowAssociations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalResults, startIndex, count, workflowAssociations);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class WorkflowAssociationListResponse {\n");
        
        sb.append("    totalResults: ").append(toIndentedString(totalResults)).append("\n");
        sb.append("    startIndex: ").append(toIndentedString(startIndex)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
        sb.append("    workflowAssociations: ").append(toIndentedString(workflowAssociations)).append("\n");
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

