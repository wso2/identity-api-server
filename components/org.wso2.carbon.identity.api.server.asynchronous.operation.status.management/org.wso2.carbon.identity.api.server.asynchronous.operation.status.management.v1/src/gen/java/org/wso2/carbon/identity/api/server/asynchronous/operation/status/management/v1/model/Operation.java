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
import java.net.URI;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;

public class Operation  {
  
    private String operationId;
    private String correlationId;
    private String operationType;
    private String subjectType;
    private String subjectId;
    private String initiatedOrgId;
    private String initiatedUserId;

@XmlType(name="StatusEnum")
@XmlEnum(String.class)
public enum StatusEnum {

    @XmlEnumValue("SUCCESS") SUCCESS(String.valueOf("SUCCESS")), @XmlEnumValue("FAIL") FAIL(String.valueOf("FAIL")), @XmlEnumValue("PARTIAL") PARTIAL(String.valueOf("PARTIAL")), @XmlEnumValue("ONGOING") ONGOING(String.valueOf("ONGOING"));


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
    private String policy;
    private String createdTime;
    private String modifiedTime;
    private URI unitOperationRef;

    /**
    **/
    public Operation operationId(String operationId) {

        this.operationId = operationId;
        return this;
    }
    
    @ApiModelProperty(example = "b60dd1f8-b774-49ee-94e2-55008d31a64b", required = true, value = "")
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
    public Operation correlationId(String correlationId) {

        this.correlationId = correlationId;
        return this;
    }
    
    @ApiModelProperty(example = "241f4aa0-86bc-44e4-a5de-af7eded9f176", required = true, value = "")
    @JsonProperty("correlationId")
    @Valid
    @NotNull(message = "Property correlationId cannot be null.")

    public String getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    /**
    **/
    public Operation operationType(String operationType) {

        this.operationType = operationType;
        return this;
    }
    
    @ApiModelProperty(example = "B2B_APPLICATION_SHARE", required = true, value = "")
    @JsonProperty("operationType")
    @Valid
    @NotNull(message = "Property operationType cannot be null.")

    public String getOperationType() {
        return operationType;
    }
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    /**
    **/
    public Operation subjectType(String subjectType) {

        this.subjectType = subjectType;
        return this;
    }
    
    @ApiModelProperty(example = "B2B_APPLICATION", required = true, value = "")
    @JsonProperty("subjectType")
    @Valid
    @NotNull(message = "Property subjectType cannot be null.")

    public String getSubjectType() {
        return subjectType;
    }
    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    /**
    **/
    public Operation subjectId(String subjectId) {

        this.subjectId = subjectId;
        return this;
    }
    
    @ApiModelProperty(example = "23d7ab3f-023e-43ba-980b-c0fd59aeacf9", required = true, value = "")
    @JsonProperty("subjectId")
    @Valid
    @NotNull(message = "Property subjectId cannot be null.")

    public String getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    /**
    **/
    public Operation initiatedOrgId(String initiatedOrgId) {

        this.initiatedOrgId = initiatedOrgId;
        return this;
    }
    
    @ApiModelProperty(example = "10084a8d-113f-4211-a0d5-efe36b082211", required = true, value = "")
    @JsonProperty("initiatedOrgId")
    @Valid
    @NotNull(message = "Property initiatedOrgId cannot be null.")

    public String getInitiatedOrgId() {
        return initiatedOrgId;
    }
    public void setInitiatedOrgId(String initiatedOrgId) {
        this.initiatedOrgId = initiatedOrgId;
    }

    /**
    **/
    public Operation initiatedUserId(String initiatedUserId) {

        this.initiatedUserId = initiatedUserId;
        return this;
    }
    
    @ApiModelProperty(example = "53c191dd-3f9f-454b-8a56-9ad72b5e4f30", required = true, value = "")
    @JsonProperty("initiatedUserId")
    @Valid
    @NotNull(message = "Property initiatedUserId cannot be null.")

    public String getInitiatedUserId() {
        return initiatedUserId;
    }
    public void setInitiatedUserId(String initiatedUserId) {
        this.initiatedUserId = initiatedUserId;
    }

    /**
    **/
    public Operation status(StatusEnum status) {

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
    public Operation policy(String policy) {

        this.policy = policy;
        return this;
    }
    
    @ApiModelProperty(example = "SHARE_WITH_ALL", value = "")
    @JsonProperty("policy")
    @Valid
    public String getPolicy() {
        return policy;
    }
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    /**
    **/
    public Operation createdTime(String createdTime) {

        this.createdTime = createdTime;
        return this;
    }
    
    @ApiModelProperty(example = "2021-10-25T12:31:53.406Z", required = true, value = "")
    @JsonProperty("createdTime")
    @Valid
    @NotNull(message = "Property createdTime cannot be null.")

    public String getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    /**
    **/
    public Operation modifiedTime(String modifiedTime) {

        this.modifiedTime = modifiedTime;
        return this;
    }
    
    @ApiModelProperty(example = "2021-10-25T12:33:53.406Z", value = "")
    @JsonProperty("modifiedTime")
    @Valid
    public String getModifiedTime() {
        return modifiedTime;
    }
    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**
    * Reference that will return the corresponsing unitoperations.
    **/
    public Operation unitOperationRef(URI unitOperationRef) {

        this.unitOperationRef = unitOperationRef;
        return this;
    }
    
    @ApiModelProperty(example = "/api/server/v1/async-operation-status/8a92bb92-c754-4dfe-8563-15ba930de75e/unit-operations?limit=10", value = "Reference that will return the corresponsing unitoperations.")
    @JsonProperty("unitOperationRef")
    @Valid
    public URI getUnitOperationRef() {
        return unitOperationRef;
    }
    public void setUnitOperationRef(URI unitOperationRef) {
        this.unitOperationRef = unitOperationRef;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Operation operation = (Operation) o;
        return Objects.equals(this.operationId, operation.operationId) &&
            Objects.equals(this.correlationId, operation.correlationId) &&
            Objects.equals(this.operationType, operation.operationType) &&
            Objects.equals(this.subjectType, operation.subjectType) &&
            Objects.equals(this.subjectId, operation.subjectId) &&
            Objects.equals(this.initiatedOrgId, operation.initiatedOrgId) &&
            Objects.equals(this.initiatedUserId, operation.initiatedUserId) &&
            Objects.equals(this.status, operation.status) &&
            Objects.equals(this.policy, operation.policy) &&
            Objects.equals(this.createdTime, operation.createdTime) &&
            Objects.equals(this.modifiedTime, operation.modifiedTime) &&
            Objects.equals(this.unitOperationRef, operation.unitOperationRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, correlationId, operationType, subjectType, subjectId, initiatedOrgId, initiatedUserId, status, policy, createdTime, modifiedTime, unitOperationRef);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class Operation {\n");
        
        sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
        sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
        sb.append("    operationType: ").append(toIndentedString(operationType)).append("\n");
        sb.append("    subjectType: ").append(toIndentedString(subjectType)).append("\n");
        sb.append("    subjectId: ").append(toIndentedString(subjectId)).append("\n");
        sb.append("    initiatedOrgId: ").append(toIndentedString(initiatedOrgId)).append("\n");
        sb.append("    initiatedUserId: ").append(toIndentedString(initiatedUserId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    policy: ").append(toIndentedString(policy)).append("\n");
        sb.append("    createdTime: ").append(toIndentedString(createdTime)).append("\n");
        sb.append("    modifiedTime: ").append(toIndentedString(modifiedTime)).append("\n");
        sb.append("    unitOperationRef: ").append(toIndentedString(unitOperationRef)).append("\n");
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

