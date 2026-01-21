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

package org.wso2.carbon.identity.api.server.common.file;

/**
 * Unified configuration for file serialization and deserialization operations.
 */
public class FileSerializationConfig {

    private XmlConfig xmlConfig = new XmlConfig();
    private JsonConfig jsonConfig = new JsonConfig();
    private YamlConfig yamlConfig = new YamlConfig();

    public FileSerializationConfig() {
    }

    public XmlConfig getXmlConfig() {
        return xmlConfig;
    }

    public void setXmlConfig(XmlConfig config) {
        this.xmlConfig = config;
    }

    public JsonConfig getJsonConfig() {
        return jsonConfig;
    }

    public void setJsonConfig(JsonConfig config) {
        this.jsonConfig = config;
    }

    public YamlConfig getYamlConfig() {
        return yamlConfig;
    }

    public void setYamlConfig(YamlConfig config) {
        this.yamlConfig = config;
    }
}
