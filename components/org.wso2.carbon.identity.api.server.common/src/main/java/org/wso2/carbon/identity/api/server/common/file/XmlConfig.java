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

import java.util.function.Consumer;

import javax.xml.bind.Marshaller;

/**
 * Configuration for XML serialization/deserialization.
 */
public class XmlConfig {

    private Class<?>[] additionalJaxbClasses = new Class<?>[0];
    private Consumer<Marshaller> marshallerCustomizer = null;

    public XmlConfig() {
    }

    public Class<?>[] getAdditionalJaxbClasses() {
        return additionalJaxbClasses != null ? additionalJaxbClasses.clone() : new Class<?>[0];
    }

    public void setAdditionalJaxbClasses(Class<?>... classes) {
        this.additionalJaxbClasses = classes;
    }

    public Consumer<Marshaller> getMarshallerCustomizer() {
        return marshallerCustomizer;
    }

    public void setMarshallerCustomizer(Consumer<Marshaller> customizer) {
        this.marshallerCustomizer = customizer;
    }
}
