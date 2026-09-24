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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;

/**
 * Config store resource details to retrieve preferences for.
 **/

import io.swagger.annotations.*;
import java.util.Objects;
import javax.validation.Valid;
import javax.xml.bind.annotation.*;
@ApiModel(description = "Config store resource details to retrieve preferences for.")
public class ConfigPreferenceRequestDTO  {
  
    private String resourceType;
    private String resourceName;
    private List<String> attributeNames = null;


    /**
    * The resource type in the config store.
    **/
    public ConfigPreferenceRequestDTO resourceType(String resourceType) {

        this.resourceType = resourceType;
        return this;
    }

    @ApiModelProperty(example = "PUSH_AUTH", required = true, value = "The resource type in the config store.")
    @JsonProperty("resourceType")
    @Valid
    @NotNull(message = "Property resourceType cannot be null.")

    public String getResourceType() {
        return resourceType;
    }
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
    * The resource name in the config store.
    **/
    public ConfigPreferenceRequestDTO resourceName(String resourceName) {

        this.resourceName = resourceName;
        return this;
    }

    @ApiModelProperty(example = "push-device-mgt", required = true, value = "The resource name in the config store.")
    @JsonProperty("resourceName")
    @Valid
    @NotNull(message = "Property resourceName cannot be null.")

    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
    * Specific attribute names to retrieve. If omitted, all attributes are returned.
    **/
    public ConfigPreferenceRequestDTO attributeNames(List<String> attributeNames) {

        this.attributeNames = attributeNames;
        return this;
    }

    @ApiModelProperty(example = "[\"enableMultipleDeviceEnrollment\",\"maximumDeviceLimit\"]", value = "Specific attribute names to retrieve. If omitted, all attributes are returned.")
    @JsonProperty("attributeNames")
    @Valid
    public List<String> getAttributeNames() {
        return attributeNames;
    }
    public void setAttributeNames(List<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    public ConfigPreferenceRequestDTO addAttributeNamesItem(String attributeNamesItem) {
        if (this.attributeNames == null) {
            this.attributeNames = new ArrayList<>();
        }
        this.attributeNames.add(attributeNamesItem);
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
        ConfigPreferenceRequestDTO configPreferenceRequestDTO = (ConfigPreferenceRequestDTO) o;
        return Objects.equals(this.resourceType, configPreferenceRequestDTO.resourceType) &&
            Objects.equals(this.resourceName, configPreferenceRequestDTO.resourceName) &&
            Objects.equals(this.attributeNames, configPreferenceRequestDTO.attributeNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceType, resourceName, attributeNames);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ConfigPreferenceRequestDTO {\n");
        
        sb.append("    resourceType: ").append(toIndentedString(resourceType)).append("\n");
        sb.append("    resourceName: ").append(toIndentedString(resourceName)).append("\n");
        sb.append("    attributeNames: ").append(toIndentedString(attributeNames)).append("\n");
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

