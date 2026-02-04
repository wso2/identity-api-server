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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Compatibility settings with dynamic groups and attributes.
 * This class uses additionalProperties pattern for flat JSON serialization.
 */
@ApiModel(description = "Compatibility settings with dynamic groups and attributes.")
public class CompatibilitySettings {

    private Map<String, Map<String, Object>> additionalProperties = new HashMap<>();

    /**
     * Get all setting groups as a map.
     *
     * @return Map of setting groups with their settings.
     */
    @JsonAnyGetter
    public Map<String, Map<String, Object>> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Set a setting group dynamically.
     *
     * @param key   Setting group name (e.g., "scim2", "oauth").
     * @param value Map of settings for the group.
     */
    @JsonAnySetter
    public void setAdditionalProperty(String key, Map<String, Object> value) {
        this.additionalProperties.put(key, value);
    }

    /**
     * Add a setting group.
     *
     * @param settingGroup Setting group name.
     * @param settings     Map of settings for the group.
     * @return This instance for chaining.
     */
    public CompatibilitySettings addSettingGroup(String settingGroup, Map<String, Object> settings) {
        this.additionalProperties.put(settingGroup, settings);
        return this;
    }

    /**
     * Get settings for a specific group.
     *
     * @param settingGroup Setting group name.
     * @return Map of settings for the group, or null if not found.
     */
    public Map<String, Object> getSettingGroup(String settingGroup) {
        return this.additionalProperties.get(settingGroup);
    }

    /**
     * Set all setting groups.
     *
     * @param settings Map of all setting groups.
     */
    public void setAllSettings(Map<String, Map<String, Object>> settings) {
        if (settings != null) {
            this.additionalProperties = settings;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompatibilitySettings that = (CompatibilitySettings) o;
        return Objects.equals(this.additionalProperties, that.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(additionalProperties);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CompatibilitySettings {\n");
        sb.append("    ").append(toIndentedString(additionalProperties)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
