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

package org.wso2.carbon.identity.api.server.configs.v1.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.annotations.ApiModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Compatibility settings for a specific setting group.
 * This class uses additionalProperties pattern for flat JSON serialization.
 */
@ApiModel(description = "Compatibility settings for a specific setting group.")
public class CompatibilitySettingsGroup {

    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * Get all settings as a map.
     *
     * @return Map of settings.
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    /**
     * Set a setting dynamically.
     *
     * @param key   Setting name.
     * @param value Setting value.
     */
    @JsonAnySetter
    public void setAdditionalProperty(String key, Object value) {
        this.additionalProperties.put(key, value);
    }

    /**
     * Add a setting.
     *
     * @param key   Setting name.
     * @param value Setting value.
     * @return This instance for chaining.
     */
    public CompatibilitySettingsGroup addSetting(String key, Object value) {
        this.additionalProperties.put(key, value);
        return this;
    }

    /**
     * Get a specific setting.
     *
     * @param key Setting name.
     * @return Setting value, or null if not found.
     */
    public Object getSetting(String key) {
        return this.additionalProperties.get(key);
    }

    /**
     * Set all settings.
     *
     * @param settings Map of all settings.
     */
    public void setAllSettings(Map<String, Object> settings) {
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
        CompatibilitySettingsGroup that = (CompatibilitySettingsGroup) o;
        return Objects.equals(this.additionalProperties, that.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(additionalProperties);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GroupCompatibilitySettings {\n");
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
