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

package org.wso2.carbon.identity.api.server.flow.management.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.identity.api.server.flow.management.v1.AttributeMetadata;
import org.wso2.carbon.identity.api.server.flow.management.v1.ExecutorConnections;
import javax.validation.constraints.*;

/**
 * General metadata for a flow type
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "General metadata for a flow type")
public class FlowMetaResponse  {
  
    private String flowType;
    private List<String> supportedExecutors = null;

    private Object connectorConfigs;
    private String attributeProfile;
    private List<String> supportedFlowCompletionConfigs = null;

    private List<AttributeMetadata> attributeMetadata = null;

    private List<ExecutorConnections> executorConnections = null;


    /**
    **/
    public FlowMetaResponse flowType(String flowType) {

        this.flowType = flowType;
        return this;
    }
    
    @ApiModelProperty(example = "PASSWORD_RECOVERY", value = "")
    @JsonProperty("flowType")
    @Valid
    public String getFlowType() {
        return flowType;
    }
    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    /**
    **/
    public FlowMetaResponse supportedExecutors(List<String> supportedExecutors) {

        this.supportedExecutors = supportedExecutors;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("supportedExecutors")
    @Valid
    public List<String> getSupportedExecutors() {
        return supportedExecutors;
    }
    public void setSupportedExecutors(List<String> supportedExecutors) {
        this.supportedExecutors = supportedExecutors;
    }

    public FlowMetaResponse addSupportedExecutorsItem(String supportedExecutorsItem) {
        if (this.supportedExecutors == null) {
            this.supportedExecutors = new ArrayList<String>();
        }
        this.supportedExecutors.add(supportedExecutorsItem);
        return this;
    }

        /**
    **/
    public FlowMetaResponse connectorConfigs(Object connectorConfigs) {

        this.connectorConfigs = connectorConfigs;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("connectorConfigs")
    @Valid
    public Object getConnectorConfigs() {
        return connectorConfigs;
    }
    public void setConnectorConfigs(Object connectorConfigs) {
        this.connectorConfigs = connectorConfigs;
    }

    /**
    **/
    public FlowMetaResponse attributeProfile(String attributeProfile) {

        this.attributeProfile = attributeProfile;
        return this;
    }
    
    @ApiModelProperty(example = "End-User-Profile", value = "")
    @JsonProperty("attributeProfile")
    @Valid
    public String getAttributeProfile() {
        return attributeProfile;
    }
    public void setAttributeProfile(String attributeProfile) {
        this.attributeProfile = attributeProfile;
    }

    /**
    **/
    public FlowMetaResponse supportedFlowCompletionConfigs(List<String> supportedFlowCompletionConfigs) {

        this.supportedFlowCompletionConfigs = supportedFlowCompletionConfigs;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("supportedFlowCompletionConfigs")
    @Valid
    public List<String> getSupportedFlowCompletionConfigs() {
        return supportedFlowCompletionConfigs;
    }
    public void setSupportedFlowCompletionConfigs(List<String> supportedFlowCompletionConfigs) {
        this.supportedFlowCompletionConfigs = supportedFlowCompletionConfigs;
    }

    public FlowMetaResponse addSupportedFlowCompletionConfigsItem(String supportedFlowCompletionConfigsItem) {
        if (this.supportedFlowCompletionConfigs == null) {
            this.supportedFlowCompletionConfigs = new ArrayList<String>();
        }
        this.supportedFlowCompletionConfigs.add(supportedFlowCompletionConfigsItem);
        return this;
    }

        /**
    **/
    public FlowMetaResponse attributeMetadata(List<AttributeMetadata> attributeMetadata) {

        this.attributeMetadata = attributeMetadata;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("attributeMetadata")
    @Valid
    public List<AttributeMetadata> getAttributeMetadata() {
        return attributeMetadata;
    }
    public void setAttributeMetadata(List<AttributeMetadata> attributeMetadata) {
        this.attributeMetadata = attributeMetadata;
    }

    public FlowMetaResponse addAttributeMetadataItem(AttributeMetadata attributeMetadataItem) {
        if (this.attributeMetadata == null) {
            this.attributeMetadata = new ArrayList<AttributeMetadata>();
        }
        this.attributeMetadata.add(attributeMetadataItem);
        return this;
    }

        /**
    **/
    public FlowMetaResponse executorConnections(List<ExecutorConnections> executorConnections) {

        this.executorConnections = executorConnections;
        return this;
    }
    
    @ApiModelProperty(value = "")
    @JsonProperty("executorConnections")
    @Valid
    public List<ExecutorConnections> getExecutorConnections() {
        return executorConnections;
    }
    public void setExecutorConnections(List<ExecutorConnections> executorConnections) {
        this.executorConnections = executorConnections;
    }

    public FlowMetaResponse addExecutorConnectionsItem(ExecutorConnections executorConnectionsItem) {
        if (this.executorConnections == null) {
            this.executorConnections = new ArrayList<ExecutorConnections>();
        }
        this.executorConnections.add(executorConnectionsItem);
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
        FlowMetaResponse flowMetaResponse = (FlowMetaResponse) o;
        return Objects.equals(this.flowType, flowMetaResponse.flowType) &&
            Objects.equals(this.supportedExecutors, flowMetaResponse.supportedExecutors) &&
            Objects.equals(this.connectorConfigs, flowMetaResponse.connectorConfigs) &&
            Objects.equals(this.attributeProfile, flowMetaResponse.attributeProfile) &&
            Objects.equals(this.supportedFlowCompletionConfigs, flowMetaResponse.supportedFlowCompletionConfigs) &&
            Objects.equals(this.attributeMetadata, flowMetaResponse.attributeMetadata) &&
            Objects.equals(this.executorConnections, flowMetaResponse.executorConnections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowType, supportedExecutors, connectorConfigs, attributeProfile, supportedFlowCompletionConfigs, attributeMetadata, executorConnections);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class FlowMetaResponse {\n");
        
        sb.append("    flowType: ").append(toIndentedString(flowType)).append("\n");
        sb.append("    supportedExecutors: ").append(toIndentedString(supportedExecutors)).append("\n");
        sb.append("    connectorConfigs: ").append(toIndentedString(connectorConfigs)).append("\n");
        sb.append("    attributeProfile: ").append(toIndentedString(attributeProfile)).append("\n");
        sb.append("    supportedFlowCompletionConfigs: ").append(toIndentedString(supportedFlowCompletionConfigs)).append("\n");
        sb.append("    attributeMetadata: ").append(toIndentedString(attributeMetadata)).append("\n");
        sb.append("    executorConnections: ").append(toIndentedString(executorConnections)).append("\n");
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

