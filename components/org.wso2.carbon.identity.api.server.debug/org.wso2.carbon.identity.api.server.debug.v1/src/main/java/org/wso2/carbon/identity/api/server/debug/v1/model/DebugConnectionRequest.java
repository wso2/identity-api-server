/**
 * Copyright (c) 2026, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.debug.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Debug request model for testing authentication flows and resources.
 * The connectionId is no longer a top-level required field. It can be provided
 * inside the properties map for resource types that require it (e.g., IDP).
 */
@ApiModel(description = "Debug request for authentication flow and resource testing")
public class DebugConnectionRequest {

    @NotNull(message = "Resource type is required")
    @Size(min = 1, max = 50, message = "Resource type must be between 1 and 50 characters")
    @ApiModelProperty(value = "Resource type to debug", required = true, example = "IDP")
    @JsonProperty("resourceType")
    private String resourceType;

    @ApiModelProperty(value = "Generic properties for resource debugging, can include connectionId for types")
    @JsonProperty("properties")
    private java.util.Map<String, String> properties;

    /**
     * Default constructor.
     */
    public DebugConnectionRequest() {

        // Default constructor.
    }

    /**
     * Constructor with resource type.
     *
     * @param resourceType The type of resource to debug.
     */
    public DebugConnectionRequest(String resourceType) {

        this.resourceType = resourceType;
    }

    /**
     * Constructor with resource type and properties.
     *
     * @param resourceType The type of resource to debug.
     * @param properties   Generic properties for debugging.
     */
    public DebugConnectionRequest(String resourceType, java.util.Map<String, String> properties) {

        this.resourceType = resourceType;
        this.properties = properties;
    }

    /**
     * Gets the resource type.
     *
     * @return Resource type.
     */
    public String getResourceType() {

        return resourceType;
    }

    /**
     * Sets the resource type.
     *
     * @param resourceType Resource type to set.
     */
    public void setResourceType(String resourceType) {

        this.resourceType = resourceType;
    }

    /**
     * Gets the properties map.
     *
     * @return Properties map.
     */
    public java.util.Map<String, String> getProperties() {

        return properties;
    }

    /**
     * Sets the properties map.
     *
     * @param properties Properties map to set.
     */
    public void setProperties(java.util.Map<String, String> properties) {

        this.properties = properties;
    }

    /**
     * Gets the connectionId from the properties map if present.
     *
     * @return Resource ID from properties, or null if not set.
     */
    public String getConnectionId() {

        if (properties != null) {
            return properties.get("connectionId");
        }
        return null;
    }

    @Override
    public String toString() {
        
        return "DebugConnectionRequest{" +
                "resourceType='" + resourceType + '\'' +
                ", properties=" + properties +
                '}';
    }
}

