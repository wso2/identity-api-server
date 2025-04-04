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
import java.time.OffsetDateTime;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class UnitOperationRecordsInner  {
  
    private String unitOperationId;
    private String operationId;
    private String residentResourceId;
    private String targetOrgId;
    private String unitOperationStatus;
    private String unitOperationStatusMessage;
    private OffsetDateTime createdAt;

    /**
    **/
    public UnitOperationRecordsInner unitOperationId(String unitOperationId) {

        this.unitOperationId = unitOperationId;
        return this;
    }
    
    @ApiModelProperty(example = "123", value = "")
    @JsonProperty("unitOperationId")
    @Valid
    public String getUnitOperationId() {
        return unitOperationId;
    }
    public void setUnitOperationId(String unitOperationId) {
        this.unitOperationId = unitOperationId;
    }

    /**
    **/
    public UnitOperationRecordsInner operationId(String operationId) {

        this.operationId = operationId;
        return this;
    }
    
    @ApiModelProperty(example = "56", value = "")
    @JsonProperty("operationId")
    @Valid
    public String getOperationId() {
        return operationId;
    }
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
    **/
    public UnitOperationRecordsInner residentResourceId(String residentResourceId) {

        this.residentResourceId = residentResourceId;
        return this;
    }
    
    @ApiModelProperty(example = "resource-123", value = "")
    @JsonProperty("residentResourceId")
    @Valid
    public String getResidentResourceId() {
        return residentResourceId;
    }
    public void setResidentResourceId(String residentResourceId) {
        this.residentResourceId = residentResourceId;
    }

    /**
    **/
    public UnitOperationRecordsInner targetOrgId(String targetOrgId) {

        this.targetOrgId = targetOrgId;
        return this;
    }
    
    @ApiModelProperty(example = "org-456", value = "")
    @JsonProperty("targetOrgId")
    @Valid
    public String getTargetOrgId() {
        return targetOrgId;
    }
    public void setTargetOrgId(String targetOrgId) {
        this.targetOrgId = targetOrgId;
    }

    /**
    **/
    public UnitOperationRecordsInner unitOperationStatus(String unitOperationStatus) {

        this.unitOperationStatus = unitOperationStatus;
        return this;
    }
    
    @ApiModelProperty(example = "PENDING", value = "")
    @JsonProperty("unitOperationStatus")
    @Valid
    public String getUnitOperationStatus() {
        return unitOperationStatus;
    }
    public void setUnitOperationStatus(String unitOperationStatus) {
        this.unitOperationStatus = unitOperationStatus;
    }

    /**
    **/
    public UnitOperationRecordsInner unitOperationStatusMessage(String unitOperationStatusMessage) {

        this.unitOperationStatusMessage = unitOperationStatusMessage;
        return this;
    }
    
    @ApiModelProperty(example = "Unit operation is pending execution.", value = "")
    @JsonProperty("unitOperationStatusMessage")
    @Valid
    public String getUnitOperationStatusMessage() {
        return unitOperationStatusMessage;
    }
    public void setUnitOperationStatusMessage(String unitOperationStatusMessage) {
        this.unitOperationStatusMessage = unitOperationStatusMessage;
    }

    /**
    **/
    public UnitOperationRecordsInner createdAt(OffsetDateTime createdAt) {

        this.createdAt = createdAt;
        return this;
    }
    
    @ApiModelProperty(example = "2024-10-27T10:00Z", value = "")
    @JsonProperty("createdAt")
    @Valid
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnitOperationRecordsInner unitOperationRecordsInner = (UnitOperationRecordsInner) o;
        return Objects.equals(this.unitOperationId, unitOperationRecordsInner.unitOperationId) &&
            Objects.equals(this.operationId, unitOperationRecordsInner.operationId) &&
            Objects.equals(this.residentResourceId, unitOperationRecordsInner.residentResourceId) &&
            Objects.equals(this.targetOrgId, unitOperationRecordsInner.targetOrgId) &&
            Objects.equals(this.unitOperationStatus, unitOperationRecordsInner.unitOperationStatus) &&
            Objects.equals(this.unitOperationStatusMessage, unitOperationRecordsInner.unitOperationStatusMessage) &&
            Objects.equals(this.createdAt, unitOperationRecordsInner.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitOperationId, operationId, residentResourceId, targetOrgId, unitOperationStatus, unitOperationStatusMessage, createdAt);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UnitOperationRecordsInner {\n");
        
        sb.append("    unitOperationId: ").append(toIndentedString(unitOperationId)).append("\n");
        sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
        sb.append("    residentResourceId: ").append(toIndentedString(residentResourceId)).append("\n");
        sb.append("    targetOrgId: ").append(toIndentedString(targetOrgId)).append("\n");
        sb.append("    unitOperationStatus: ").append(toIndentedString(unitOperationStatus)).append("\n");
        sb.append("    unitOperationStatusMessage: ").append(toIndentedString(unitOperationStatusMessage)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

