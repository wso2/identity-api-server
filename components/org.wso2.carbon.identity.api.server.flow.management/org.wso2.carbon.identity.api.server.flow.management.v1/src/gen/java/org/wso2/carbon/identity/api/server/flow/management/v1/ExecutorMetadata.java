/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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
import javax.validation.constraints.*;

/**
 * Metadata describing an executor contributed by a deployed extension
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Metadata describing an executor contributed by a deployed extension")
public class ExecutorMetadata  {
  
    private String name;
    private String displayName;
    private String description;
    private Boolean isAuthenticationExecutor;
    private String icon;
    private Boolean requiresConnection;
    private String associatedAuthenticator;

    /**
    * Unique name of the executor, as registered with the flow execution engine. Matches the entry in supportedExecutors and the executorName in executorConnections.
    **/
    public ExecutorMetadata name(String name) {

        this.name = name;
        return this;
    }
    
    @ApiModelProperty(example = "CustomVerificationExecutor", value = "Unique name of the executor, as registered with the flow execution engine. Matches the entry in supportedExecutors and the executorName in executorConnections.")
    @JsonProperty("name")
    @Valid
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Human readable name for the executor
    **/
    public ExecutorMetadata displayName(String displayName) {

        this.displayName = displayName;
        return this;
    }
    
    @ApiModelProperty(example = "Custom Identity Verification", value = "Human readable name for the executor")
    @JsonProperty("displayName")
    @Valid
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
    * Short explanation of what this step does
    **/
    public ExecutorMetadata description(String description) {

        this.description = description;
        return this;
    }
    
    @ApiModelProperty(example = "Verifies the user's identity with an external verification provider before the flow continues.", value = "Short explanation of what this step does")
    @JsonProperty("description")
    @Valid
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /**
    * Whether this executor authenticates the user. Such an executor satisfies a flow requirement for an authentication factor, for example the password recovery flow requirement of having at least one recovery factor.
    **/
    public ExecutorMetadata isAuthenticationExecutor(Boolean isAuthenticationExecutor) {

        this.isAuthenticationExecutor = isAuthenticationExecutor;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether this executor authenticates the user. Such an executor satisfies a flow requirement for an authentication factor, for example the password recovery flow requirement of having at least one recovery factor.")
    @JsonProperty("isAuthenticationExecutor")
    @Valid
    public Boolean getIsAuthenticationExecutor() {
        return isAuthenticationExecutor;
    }
    public void setIsAuthenticationExecutor(Boolean isAuthenticationExecutor) {
        this.isAuthenticationExecutor = isAuthenticationExecutor;
    }

    /**
    * Icon path or URL for the executor
    **/
    public ExecutorMetadata icon(String icon) {

        this.icon = icon;
        return this;
    }
    
    @ApiModelProperty(example = "assets/images/logos/custom-verification.svg", value = "Icon path or URL for the executor")
    @JsonProperty("icon")
    @Valid
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
    * Whether a connection must be selected for a step using this executor. The applicable connections are listed against this executor name in executorConnections.
    **/
    public ExecutorMetadata requiresConnection(Boolean requiresConnection) {

        this.requiresConnection = requiresConnection;
        return this;
    }
    
    @ApiModelProperty(example = "true", value = "Whether a connection must be selected for a step using this executor. The applicable connections are listed against this executor name in executorConnections.")
    @JsonProperty("requiresConnection")
    @Valid
    public Boolean getRequiresConnection() {
        return requiresConnection;
    }
    public void setRequiresConnection(Boolean requiresConnection) {
        this.requiresConnection = requiresConnection;
    }

    /**
    * Name of the authenticator backing this executor
    **/
    public ExecutorMetadata associatedAuthenticator(String associatedAuthenticator) {

        this.associatedAuthenticator = associatedAuthenticator;
        return this;
    }
    
    @ApiModelProperty(example = "CustomVerificationAuthenticator", value = "Name of the authenticator backing this executor")
    @JsonProperty("associatedAuthenticator")
    @Valid
    public String getAssociatedAuthenticator() {
        return associatedAuthenticator;
    }
    public void setAssociatedAuthenticator(String associatedAuthenticator) {
        this.associatedAuthenticator = associatedAuthenticator;
    }



    @Override
    public boolean equals(java.lang.Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecutorMetadata executorMetadata = (ExecutorMetadata) o;
        return Objects.equals(this.name, executorMetadata.name) &&
            Objects.equals(this.displayName, executorMetadata.displayName) &&
            Objects.equals(this.description, executorMetadata.description) &&
            Objects.equals(this.isAuthenticationExecutor, executorMetadata.isAuthenticationExecutor) &&
            Objects.equals(this.icon, executorMetadata.icon) &&
            Objects.equals(this.requiresConnection, executorMetadata.requiresConnection) &&
            Objects.equals(this.associatedAuthenticator, executorMetadata.associatedAuthenticator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, displayName, description, isAuthenticationExecutor, icon, requiresConnection, associatedAuthenticator);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ExecutorMetadata {\n");
        
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    isAuthenticationExecutor: ").append(toIndentedString(isAuthenticationExecutor)).append("\n");
        sb.append("    icon: ").append(toIndentedString(icon)).append("\n");
        sb.append("    requiresConnection: ").append(toIndentedString(requiresConnection)).append("\n");
        sb.append("    associatedAuthenticator: ").append(toIndentedString(associatedAuthenticator)).append("\n");
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

