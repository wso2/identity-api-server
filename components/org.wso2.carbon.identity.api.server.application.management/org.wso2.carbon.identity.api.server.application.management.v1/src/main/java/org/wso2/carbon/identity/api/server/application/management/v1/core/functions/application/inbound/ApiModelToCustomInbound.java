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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound;

import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PropertyModel;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.Property;

import java.util.Optional;
import java.util.function.Function;

/**
 * Converts CustomInboundProtocolConfiguration api model to a InboundAuthenticationRequestConfig object.
 */
public class ApiModelToCustomInbound implements
        Function<CustomInboundProtocolConfiguration, InboundAuthenticationRequestConfig> {

    @Override
    public InboundAuthenticationRequestConfig apply(CustomInboundProtocolConfiguration customInbound) {

        InboundAuthenticationRequestConfig inboundRequestConfig = new InboundAuthenticationRequestConfig();
        inboundRequestConfig.setInboundAuthType(customInbound.getName());
        inboundRequestConfig.setInboundAuthKey(customInbound.getInboundKey());
        inboundRequestConfig.setProperties(getProperties(customInbound));
        return inboundRequestConfig;
    }

    private Property[] getProperties(CustomInboundProtocolConfiguration inboundConfigModel) {

        return Optional.of(inboundConfigModel.getProperties())
                .map(modelProperties -> modelProperties.stream().map(this::buildProperty).toArray(Property[]::new))
                .orElse(new Property[0]);
    }

    private Property buildProperty(PropertyModel modelProperty) {

        Property property = new Property();
        property.setName(modelProperty.getKey());
        property.setValue(modelProperty.getValue());
        return property;
    }
}
