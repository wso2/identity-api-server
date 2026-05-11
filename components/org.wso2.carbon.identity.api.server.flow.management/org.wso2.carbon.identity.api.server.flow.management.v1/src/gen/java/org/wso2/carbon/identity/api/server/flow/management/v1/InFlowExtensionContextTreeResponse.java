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
import org.wso2.carbon.identity.api.server.flow.management.v1.InFlowExtensionContextTreeNode;
import javax.validation.constraints.*;

/**
 * Controlled In-Flow Extension context tree for a given flow type, plus per-flow-type policy flags consumed by the Console UI. 
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Controlled In-Flow Extension context tree for a given flow type, plus per-flow-type policy flags consumed by the Console UI. ")
public class InFlowExtensionContextTreeResponse  {
  
    private String flowType;
    private List<InFlowExtensionContextTreeNode> contextTree = null;

    private Boolean redirectionEnabled;
    private Boolean allowReadOnlyClaimsModification;

    /**
    * Echoed flow type. &#x60;null&#x60; when no flowType was supplied (default tree).
    **/
    public InFlowExtensionContextTreeResponse flowType(String flowType) {

        this.flowType = flowType;
        return this;
    }
    
    @ApiModelProperty(example = "PASSWORD_RECOVERY", value = "Echoed flow type. `null` when no flowType was supplied (default tree).")
    @JsonProperty("flowType")
    @Valid
    public String getFlowType() {
        return flowType;
    }
    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    /**
    * Tree of context fields available for the active flow type. Fields disabled at the deployment.toml whitelist level are omitted entirely. 
    **/
    public InFlowExtensionContextTreeResponse contextTree(List<InFlowExtensionContextTreeNode> contextTree) {

        this.contextTree = contextTree;
        return this;
    }
    
    @ApiModelProperty(value = "Tree of context fields available for the active flow type. Fields disabled at the deployment.toml whitelist level are omitted entirely. ")
    @JsonProperty("contextTree")
    @Valid
    public List<InFlowExtensionContextTreeNode> getContextTree() {
        return contextTree;
    }
    public void setContextTree(List<InFlowExtensionContextTreeNode> contextTree) {
        this.contextTree = contextTree;
    }

    public InFlowExtensionContextTreeResponse addContextTreeItem(InFlowExtensionContextTreeNode contextTreeItem) {
        if (this.contextTree == null) {
            this.contextTree = new ArrayList<InFlowExtensionContextTreeNode>();
        }
        this.contextTree.add(contextTreeItem);
        return this;
    }

        /**
    * Whether REDIRECT is advertised in &#x60;allowedOperations&#x60; for this flow type.
    **/
    public InFlowExtensionContextTreeResponse redirectionEnabled(Boolean redirectionEnabled) {

        this.redirectionEnabled = redirectionEnabled;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether REDIRECT is advertised in `allowedOperations` for this flow type.")
    @JsonProperty("redirectionEnabled")
    @Valid
    public Boolean getRedirectionEnabled() {
        return redirectionEnabled;
    }
    public void setRedirectionEnabled(Boolean redirectionEnabled) {
        this.redirectionEnabled = redirectionEnabled;
    }

    /**
    * Whether the Console UI may permit MODIFY on read-only claims for this flow type. Hardcoded enumerative mapping in the engine. 
    **/
    public InFlowExtensionContextTreeResponse allowReadOnlyClaimsModification(Boolean allowReadOnlyClaimsModification) {

        this.allowReadOnlyClaimsModification = allowReadOnlyClaimsModification;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether the Console UI may permit MODIFY on read-only claims for this flow type. Hardcoded enumerative mapping in the engine. ")
    @JsonProperty("allowReadOnlyClaimsModification")
    @Valid
    public Boolean getAllowReadOnlyClaimsModification() {
        return allowReadOnlyClaimsModification;
    }
    public void setAllowReadOnlyClaimsModification(Boolean allowReadOnlyClaimsModification) {
        this.allowReadOnlyClaimsModification = allowReadOnlyClaimsModification;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InFlowExtensionContextTreeResponse inFlowExtensionContextTreeResponse = (InFlowExtensionContextTreeResponse) o;
        return Objects.equals(this.flowType, inFlowExtensionContextTreeResponse.flowType) &&
            Objects.equals(this.contextTree, inFlowExtensionContextTreeResponse.contextTree) &&
            Objects.equals(this.redirectionEnabled, inFlowExtensionContextTreeResponse.redirectionEnabled) &&
            Objects.equals(this.allowReadOnlyClaimsModification, inFlowExtensionContextTreeResponse.allowReadOnlyClaimsModification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flowType, contextTree, redirectionEnabled, allowReadOnlyClaimsModification);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class InFlowExtensionContextTreeResponse {\n");
        
        sb.append("    flowType: ").append(toIndentedString(flowType)).append("\n");
        sb.append("    contextTree: ").append(toIndentedString(contextTree)).append("\n");
        sb.append("    redirectionEnabled: ").append(toIndentedString(redirectionEnabled)).append("\n");
        sb.append("    allowReadOnlyClaimsModification: ").append(toIndentedString(allowReadOnlyClaimsModification)).append("\n");
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

