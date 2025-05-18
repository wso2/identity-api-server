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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class OperationUnitOperationDetailSummary  {
  
    private Integer success;
    private Integer failed;
    private Integer partiallyCompleted;

    /**
    **/
    public OperationUnitOperationDetailSummary success(Integer success) {

        this.success = success;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("success")
    @Valid
    public Integer getSuccess() {
        return success;
    }
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
    **/
    public OperationUnitOperationDetailSummary failed(Integer failed) {

        this.failed = failed;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("failed")
    @Valid
    public Integer getFailed() {
        return failed;
    }
    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    /**
    **/
    public OperationUnitOperationDetailSummary partiallyCompleted(Integer partiallyCompleted) {

        this.partiallyCompleted = partiallyCompleted;
        return this;
    }
    
    @ApiModelProperty(example = "10", value = "")
    @JsonProperty("partiallyCompleted")
    @Valid
    public Integer getPartiallyCompleted() {
        return partiallyCompleted;
    }
    public void setPartiallyCompleted(Integer partiallyCompleted) {
        this.partiallyCompleted = partiallyCompleted;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OperationUnitOperationDetailSummary operationUnitOperationDetailSummary = (OperationUnitOperationDetailSummary) o;
        return Objects.equals(this.success, operationUnitOperationDetailSummary.success) &&
            Objects.equals(this.failed, operationUnitOperationDetailSummary.failed) &&
            Objects.equals(this.partiallyCompleted, operationUnitOperationDetailSummary.partiallyCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, failed, partiallyCompleted);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OperationUnitOperationDetailSummary {\n");
        
        sb.append("    success: ").append(toIndentedString(success)).append("\n");
        sb.append("    failed: ").append(toIndentedString(failed)).append("\n");
        sb.append("    partiallyCompleted: ").append(toIndentedString(partiallyCompleted)).append("\n");
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

