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

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Configuration for YAML serialization/deserialization.
 */
public class YamlConfig {

    private Consumer<DumperOptions> dumperOptionsCustomizer = null;
    private Consumer<Constructor> constructorCustomizer = null;
    private Consumer<Representer> representerCustomizer = null;
    private Function<DumperOptions, Representer> representerFactory = null;
    private List<String> additionalTrustedClassNames = new ArrayList<>();

    public YamlConfig() {}

    public Consumer<DumperOptions> getDumperOptionsCustomizer() {
        return dumperOptionsCustomizer;
    }

    public void setDumperOptionsCustomizer(Consumer<DumperOptions> customizer) {

        this.dumperOptionsCustomizer = customizer;
    }

    public Consumer<Constructor> getConstructorCustomizer() {
        return constructorCustomizer;
    }

    public void setConstructorCustomizer(Consumer<Constructor> customizer) {

        this.constructorCustomizer = customizer;
    }

    public Consumer<Representer> getRepresenterCustomizer() {
        return representerCustomizer;
    }

    public void setRepresenterCustomizer(Consumer<Representer> customizer) {

        this.representerCustomizer = customizer;
    }

    public Function<DumperOptions, Representer> getRepresenterFactory() {
        return representerFactory;
    }

    public void setRepresenterFactory(Function<DumperOptions, Representer> factory) {

        this.representerFactory = factory;
    }

    public List<String> getAdditionalTrustedClassNames() {

        return new ArrayList<>(additionalTrustedClassNames);
    }

    public void setAdditionalTrustedClasses(Class<?>... classes) {

        this.additionalTrustedClassNames = new ArrayList<>();
        if (classes == null) {
            return;
        }
        for (Class<?> clazz : classes) {
            this.additionalTrustedClassNames.add(clazz.getName());
        }
    }
}
