/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.custom;

import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PropertyModel;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Helper class for custom inbound management.
 */
public class CustomInboundUtils {

    private CustomInboundUtils() {

    }

    public static InboundAuthenticationRequestConfig putCustomInbound(ServiceProvider application,
                                                                      CustomInboundProtocolConfiguration inboundModel) {

        return createCustomInbound(inboundModel);
    }

    public static InboundAuthenticationRequestConfig createCustomInbound(CustomInboundProtocolConfiguration custom) {

        InboundAuthenticationRequestConfig inboundRequestConfig = new InboundAuthenticationRequestConfig();
        inboundRequestConfig.setInboundAuthType(custom.getName());
        inboundRequestConfig.setInboundConfigType(custom.getConfigName());
        inboundRequestConfig.setProperties(getProperties(custom));

        return inboundRequestConfig;
    }

    private static Property[] getProperties(CustomInboundProtocolConfiguration inboundConfigModel) {

        return Optional.of(inboundConfigModel.getProperties())
                .map(modelProperties ->
                        modelProperties.stream().map(CustomInboundUtils::buildProperty).toArray(Property[]::new))
                .orElse(new Property[0]);
    }

    private static Property buildProperty(PropertyModel modelProperty) {

        Property property = new Property();
        property.setName(modelProperty.getKey());
        property.setValue(modelProperty.getValue());
        return property;
    }

    public static CustomInboundProtocolConfiguration getCustomInbound(InboundAuthenticationRequestConfig inbound) {

        return new CustomInboundProtocolConfiguration()
                .name(inbound.getInboundAuthType())
                .configName(inbound.getInboundConfigType())
                .properties(
                        Optional.ofNullable(inbound.getProperties())
                                .map(inboundProperties -> Arrays.stream(inboundProperties)
                                        .map(CustomInboundUtils::buildPropertyModel).collect(Collectors.toList())
                                ).orElse(Collections.emptyList())
                );
    }

    private static PropertyModel buildPropertyModel(Property inboundProperty) {

        return new PropertyModel()
                .key(inboundProperty.getName())
                .value(inboundProperty.getValue())
                .friendlyName(inboundProperty.getDisplayName());
    }
}
