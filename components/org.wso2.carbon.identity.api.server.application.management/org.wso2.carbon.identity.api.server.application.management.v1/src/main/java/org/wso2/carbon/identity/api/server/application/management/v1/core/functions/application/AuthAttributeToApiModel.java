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

package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application;

import org.wso2.carbon.identity.auth.attribute.handler.model.AuthAttribute;

import java.util.function.Function;

/**
 * Converts the backend model AuthAttribute into the corresponding API model object.
 */
public class AuthAttributeToApiModel implements Function<AuthAttribute,
        org.wso2.carbon.identity.api.server.application.management.v1.AuthAttribute> {

    @Override
    public org.wso2.carbon.identity.api.server.application.management.v1.AuthAttribute apply
            (AuthAttribute authAttribute) {

        return new org.wso2.carbon.identity.api.server.application.management.v1.AuthAttribute()
                .attribute(authAttribute.getAttribute())
                .attributeType(authAttribute.getType().toString())
                .isClaim(authAttribute.isClaim())
                .isCredential(authAttribute.isConfidential());
    }
}
