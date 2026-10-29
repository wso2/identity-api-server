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

package org.wso2.carbon.identity.api.server.policy.v1.function.resource;

import org.wso2.carbon.identity.api.server.policy.common.Constants;
import org.wso2.carbon.identity.api.server.policy.v1.function.resource.rule.RulePolicyResourceHandler;
import org.wso2.carbon.identity.api.server.policy.v1.util.PolicyManagementAPIErrorBuilder;
import org.wso2.carbon.identity.policy.management.api.model.PolicyResource.ResourceType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import javax.ws.rs.core.Response;

/**
 * Holds the {@link PolicyResourceHandler} of every resource type the Policy API supports.
 */
public class PolicyResourceHandlerRegistry {

    private static final PolicyResourceHandlerRegistry INSTANCE =
            new PolicyResourceHandlerRegistry(new RulePolicyResourceHandler());

    private final Map<ResourceType, PolicyResourceHandler> handlers;

    private PolicyResourceHandlerRegistry(PolicyResourceHandler... resourceHandlers) {

        Map<ResourceType, PolicyResourceHandler> handlerMap = new EnumMap<>(ResourceType.class);
        for (PolicyResourceHandler resourceHandler : resourceHandlers) {
            handlerMap.put(resourceHandler.getResourceType(), resourceHandler);
        }
        this.handlers = Collections.unmodifiableMap(handlerMap);
    }

    /**
     * Returns the registry instance.
     *
     * @return Policy resource handler registry.
     */
    public static PolicyResourceHandlerRegistry getInstance() {

        return INSTANCE;
    }

    /**
     * Returns the handler of a resource type.
     *
     * @param resourceType Resource type.
     * @return Resource handler.
     */
    public PolicyResourceHandler getHandler(ResourceType resourceType) {

        PolicyResourceHandler handler = resourceType != null ? handlers.get(resourceType) : null;
        if (handler == null) {
            throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.BAD_REQUEST,
                    Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_RESOURCE_TYPE, String.valueOf(resourceType));
        }
        return handler;
    }

    /**
     * Returns the handler of a resource type given by its name in a request.
     *
     * @param resourceTypeName Resource type name.
     * @return Resource handler.
     */
    public PolicyResourceHandler getHandler(String resourceTypeName) {

        for (ResourceType resourceType : handlers.keySet()) {
            if (resourceType.name().equals(resourceTypeName)) {
                return handlers.get(resourceType);
            }
        }
        throw PolicyManagementAPIErrorBuilder.handleException(Response.Status.BAD_REQUEST,
                Constants.ErrorMessage.ERROR_CODE_UNSUPPORTED_RESOURCE_TYPE, resourceTypeName);
    }
}
