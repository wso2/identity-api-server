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

/**
 * Debug request model for testing authentication flows and resources.
 * Supports both IdP OAuth 2.0 authentication testing and generic resource
 * debugging.
 */
@ApiModel(description = "Debug request for authentication flow and resource testing")
public class DebugConnectionRequest {

    @ApiModelProperty(value = "Resource ID to debug", required = true)
    @JsonProperty("resourceId")
    private String resourceId;

    @ApiModelProperty(value = "Resource type to debug", required = true)
    @JsonProperty("resourceType")
    private String resourceType;

    @ApiModelProperty(value = "Generic properties for resource debugging")
    @JsonProperty("properties")
    private java.util.Map<String, String> properties;

    /**
     * Default constructor.
     */
    public DebugConnectionRequest() {

        // Default constructor.
    }

    /**
     * Constructor with resource ID and type.
     *
     * @param resourceId   The resource ID to debug.
     * @param resourceType The type of resource to debug.
     */
    public DebugConnectionRequest(String resourceId, String resourceType) {

        this.resourceId = resourceId;
        this.resourceType = resourceType;
    }

    /**
     * Constructor with all fields.
     *
     * @param resourceId   The resource ID to debug.
     * @param resourceType The type of resource to debug.
     * @param properties   Generic properties for debugging.
     */
    public DebugConnectionRequest(String resourceId, String resourceType, java.util.Map<String, String> properties) {

        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.properties = properties;
    }

    /**
     * Gets the resource ID.
     *
     * @return Resource ID.
     */
    public String getResourceId() {

        return resourceId;
    }

    /**
     * Sets the resource ID.
     *
     * @param resourceId Resource ID to set.
     */
    public void setResourceId(String resourceId) {

        this.resourceId = resourceId;
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

    @Override
    public String toString() {
        
        return "DebugConnectionRequest{" +
                "resourceId='" + resourceId + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", properties=" + properties +
                '}';
    }
}
