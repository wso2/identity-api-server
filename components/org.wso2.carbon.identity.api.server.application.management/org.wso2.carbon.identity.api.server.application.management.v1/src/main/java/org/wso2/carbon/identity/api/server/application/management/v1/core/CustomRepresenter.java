/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1.core;

import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.representer.Representer;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The CustomRepresenter class for YAML serialization.
 */
public class CustomRepresenter extends Representer {

    private static final String[] PROPERTIES_TO_REMOVE = {"inboundConfiguration", "applicationID", "owner",
            "tenantDomain", "id", "idpProperties", "resourceId", "spProperties", "applicationResourceId"};

    @Override
    protected Set<Property> getProperties(Class<?> type) {

        Set<Property> properties = super.getProperties(type);

        // Remove the specified properties.
        properties = properties.stream()
                .filter(property -> !Arrays.asList(PROPERTIES_TO_REMOVE).contains(property.getName()))
                .collect(Collectors.toSet());

        return properties;
    }
}
