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

public class UnitOperation  {
  
    private String unitOperationId;
    private String operationId;
    private String residentResourceId;
    private String targetOrgId;
    private String targetOrgName;

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("SUCCESS") SUCCESS(String.valueOf("SUCCESS")), @XmlEnumValue("FAILED") FAILED(String.valueOf("FAILED")), @XmlEnumValue("PARTIALLY_COMPLETED") PARTIALLY_COMPLETED(String.valueOf("PARTIALLY_COMPLETED"));


    private String value;

    StatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
        for (StatusEnum b : StatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

    private StatusEnum status;
    private String statusMessage;
    private String createdTime;

    /**
    **/
    public UnitOperation unitOperationId(String unitOperationId) {

        this.unitOperationId = unitOperationId;
        return this;
    }
    
    @ApiModelProperty(example = "0cad98fa-16a7-430f-9e94-763bb57bbfbc", required = true, value = "")
    @JsonProperty("unitOperationId")
    @Valid
    @NotNull(message = "Property unitOperationId cannot be null.")

    public String getUnitOperationId() {
        return unitOperationId;
    }
    public void setUnitOperationId(String unitOperationId) {
        this.unitOperationId = unitOperationId;
    }

    /**
    **/
    public UnitOperation operationId(String operationId) {

        this.operationId = operationId;
        return this;
    }
    
    @ApiModelProperty(example = "f0880ebf-9634-498f-9213-614953563aa9", required = true, value = "")
    @JsonProperty("operationId")
    @Valid
    @NotNull(message = "Property operationId cannot be null.")

    public String getOperationId() {
        return operationId;
    }
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    /**
    **/
    public UnitOperation residentResourceId(String residentResourceId) {

        this.residentResourceId = residentResourceId;
        return this;
    }
    
    @ApiModelProperty(example = "9e810e28-b9cd-4ae2-bdb2-4de95846c46e", required = true, value = "")
    @JsonProperty("residentResourceId")
    @Valid
    @NotNull(message = "Property residentResourceId cannot be null.")

    public String getResidentResourceId() {
        return residentResourceId;
    }
    public void setResidentResourceId(String residentResourceId) {
        this.residentResourceId = residentResourceId;
    }

    /**
    **/
    public UnitOperation targetOrgId(String targetOrgId) {

        this.targetOrgId = targetOrgId;
        return this;
    }
    
    @ApiModelProperty(example = "c87d873c-fe76-42a2-b1ce-c90475e13b82", required = true, value = "")
    @JsonProperty("targetOrgId")
    @Valid
    @NotNull(message = "Property targetOrgId cannot be null.")

    public String getTargetOrgId() {
        return targetOrgId;
    }
    public void setTargetOrgId(String targetOrgId) {
        this.targetOrgId = targetOrgId;
    }

    /**
    **/
    public UnitOperation targetOrgName(String targetOrgName) {

        this.targetOrgName = targetOrgName;
        return this;
    }
    
    @ApiModelProperty(example = "org-1", required = true, value = "")
    @JsonProperty("targetOrgName")
    @Valid
    @NotNull(message = "Property targetOrgName cannot be null.")

    public String getTargetOrgName() {
        return targetOrgName;
    }
    public void setTargetOrgName(String targetOrgName) {
        this.targetOrgName = targetOrgName;
    }

    /**
    **/
    public UnitOperation status(StatusEnum status) {

        this.status = status;
        return this;
    }
    
    @ApiModelProperty(example = "SUCCESS", required = true, value = "")
    @JsonProperty("status")
    @Valid
    @NotNull(message = "Property status cannot be null.")

    public StatusEnum getStatus() {
        return status;
    }
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
    **/
    public UnitOperation statusMessage(String statusMessage) {

        this.statusMessage = statusMessage;
        return this;
    }
    
    @ApiModelProperty(example = "Unit operation is pending execution.", required = true, value = "")
    @JsonProperty("statusMessage")
    @Valid
    @NotNull(message = "Property statusMessage cannot be null.")

    public String getStatusMessage() {
        return statusMessage;
    }
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
    **/
    public UnitOperation createdTime(String createdTime) {

        this.createdTime = createdTime;
        return this;
    }
    
    @ApiModelProperty(example = "2024-10-27T10:00:00Z", required = true, value = "")
    @JsonProperty("createdTime")
    @Valid
    @NotNull(message = "Property createdTime cannot be null.")

    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnitOperation unitOperation = (UnitOperation) o;
        return Objects.equals(this.unitOperationId, unitOperation.unitOperationId) &&
            Objects.equals(this.operationId, unitOperation.operationId) &&
            Objects.equals(this.residentResourceId, unitOperation.residentResourceId) &&
            Objects.equals(this.targetOrgId, unitOperation.targetOrgId) &&
            Objects.equals(this.targetOrgName, unitOperation.targetOrgName) &&
            Objects.equals(this.status, unitOperation.status) &&
            Objects.equals(this.statusMessage, unitOperation.statusMessage) &&
            Objects.equals(this.createdTime, unitOperation.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitOperationId, operationId, residentResourceId, targetOrgId, targetOrgName, status, statusMessage, createdTime);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class UnitOperation {\n");
        
        sb.append("    unitOperationId: ").append(toIndentedString(unitOperationId)).append("\n");
        sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
        sb.append("    residentResourceId: ").append(toIndentedString(residentResourceId)).append("\n");
        sb.append("    targetOrgId: ").append(toIndentedString(targetOrgId)).append("\n");
        sb.append("    targetOrgName: ").append(toIndentedString(targetOrgName)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
        sb.append("    createdTime: ").append(toIndentedString(createdTime)).append("\n");
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

