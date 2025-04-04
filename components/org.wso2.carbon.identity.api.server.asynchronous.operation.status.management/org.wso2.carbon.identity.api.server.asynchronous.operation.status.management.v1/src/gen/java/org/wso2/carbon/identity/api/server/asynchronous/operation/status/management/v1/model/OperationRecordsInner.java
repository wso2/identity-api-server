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

public class OperationRecordsInner  {
  
    private String operationId;
    private String correlationId;
    private String operationType;
    private String operationSubjectType;
    private String operationSubjectId;
    private String residentOrgId;
    private String initiatorId;
    private String operationStatus;
    private String operationPolicy;

    /**
    **/
    public OperationRecordsInner operationId(String operationId) {

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
    public OperationRecordsInner correlationId(String correlationId) {

        this.correlationId = correlationId;
        return this;
    }
    
    @ApiModelProperty(example = "241f4aa0-86bc-44e4-a5de-af7eded9f176", value = "")
    @JsonProperty("correlationId")
    @Valid
    public String getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    /**
    **/
    public OperationRecordsInner operationType(String operationType) {

        this.operationType = operationType;
        return this;
    }
    
    @ApiModelProperty(example = "B2B_APPLICATION_SHARE", value = "")
    @JsonProperty("operationType")
    @Valid
    public String getOperationType() {
        return operationType;
    }
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    /**
    **/
    public OperationRecordsInner operationSubjectType(String operationSubjectType) {

        this.operationSubjectType = operationSubjectType;
        return this;
    }
    
    @ApiModelProperty(example = "B2B_APPLICATION", value = "")
    @JsonProperty("operationSubjectType")
    @Valid
    public String getOperationSubjectType() {
        return operationSubjectType;
    }
    public void setOperationSubjectType(String operationSubjectType) {
        this.operationSubjectType = operationSubjectType;
    }

    /**
    **/
    public OperationRecordsInner operationSubjectId(String operationSubjectId) {

        this.operationSubjectId = operationSubjectId;
        return this;
    }
    
    @ApiModelProperty(example = "23d7ab3f-023e-43ba-980b-c0fd59aeacf9", value = "")
    @JsonProperty("operationSubjectId")
    @Valid
    public String getOperationSubjectId() {
        return operationSubjectId;
    }
    public void setOperationSubjectId(String operationSubjectId) {
        this.operationSubjectId = operationSubjectId;
    }

    /**
    **/
    public OperationRecordsInner residentOrgId(String residentOrgId) {

        this.residentOrgId = residentOrgId;
        return this;
    }
    
    @ApiModelProperty(example = "10084a8d-113f-4211-a0d5-efe36b082211", value = "")
    @JsonProperty("residentOrgId")
    @Valid
    public String getResidentOrgId() {
        return residentOrgId;
    }
    public void setResidentOrgId(String residentOrgId) {
        this.residentOrgId = residentOrgId;
    }

    /**
    **/
    public OperationRecordsInner initiatorId(String initiatorId) {

        this.initiatorId = initiatorId;
        return this;
    }
    
    @ApiModelProperty(example = "53c191dd-3f9f-454b-8a56-9ad72b5e4f30", value = "")
    @JsonProperty("initiatorId")
    @Valid
    public String getInitiatorId() {
        return initiatorId;
    }
    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    /**
    **/
    public OperationRecordsInner operationStatus(String operationStatus) {

        this.operationStatus = operationStatus;
        return this;
    }
    
    @ApiModelProperty(example = "SUCCESS", value = "")
    @JsonProperty("operationStatus")
    @Valid
    public String getOperationStatus() {
        return operationStatus;
    }
    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    /**
    **/
    public OperationRecordsInner operationPolicy(String operationPolicy) {

        this.operationPolicy = operationPolicy;
        return this;
    }
    
    @ApiModelProperty(example = "SHARE_WITH_ALL", value = "")
    @JsonProperty("operationPolicy")
    @Valid
    public String getOperationPolicy() {
        return operationPolicy;
    }
    public void setOperationPolicy(String operationPolicy) {
        this.operationPolicy = operationPolicy;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OperationRecordsInner operationRecordsInner = (OperationRecordsInner) o;
        return Objects.equals(this.operationId, operationRecordsInner.operationId) &&
            Objects.equals(this.correlationId, operationRecordsInner.correlationId) &&
            Objects.equals(this.operationType, operationRecordsInner.operationType) &&
            Objects.equals(this.operationSubjectType, operationRecordsInner.operationSubjectType) &&
            Objects.equals(this.operationSubjectId, operationRecordsInner.operationSubjectId) &&
            Objects.equals(this.residentOrgId, operationRecordsInner.residentOrgId) &&
            Objects.equals(this.initiatorId, operationRecordsInner.initiatorId) &&
            Objects.equals(this.operationStatus, operationRecordsInner.operationStatus) &&
            Objects.equals(this.operationPolicy, operationRecordsInner.operationPolicy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, correlationId, operationType, operationSubjectType, operationSubjectId, residentOrgId, initiatorId, operationStatus, operationPolicy);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class OperationRecordsInner {\n");
        
        sb.append("    operationId: ").append(toIndentedString(operationId)).append("\n");
        sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
        sb.append("    operationType: ").append(toIndentedString(operationType)).append("\n");
        sb.append("    operationSubjectType: ").append(toIndentedString(operationSubjectType)).append("\n");
        sb.append("    operationSubjectId: ").append(toIndentedString(operationSubjectId)).append("\n");
        sb.append("    residentOrgId: ").append(toIndentedString(residentOrgId)).append("\n");
        sb.append("    initiatorId: ").append(toIndentedString(initiatorId)).append("\n");
        sb.append("    operationStatus: ").append(toIndentedString(operationStatus)).append("\n");
        sb.append("    operationPolicy: ").append(toIndentedString(operationPolicy)).append("\n");
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

