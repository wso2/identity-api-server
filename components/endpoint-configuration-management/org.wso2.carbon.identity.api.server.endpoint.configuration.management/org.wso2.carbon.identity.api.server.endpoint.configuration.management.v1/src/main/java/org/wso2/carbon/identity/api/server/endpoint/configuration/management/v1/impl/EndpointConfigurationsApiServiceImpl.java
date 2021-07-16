/*
* Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.ContextLoader;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.error.APIError;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.EndpointConfigurationsApiService;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.core.ConfigurationManagementService;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.EndpointConfiguration;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.EndpointConfigurationAdd;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.EndpointConfigurationUpdateRequest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.CONFIGURATION_CONTEXT_PATH;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.PLUS;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.URL_ENCODED_SPACE;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.V1_API_PATH_COMPONENT;


/**
 * Implementation of endpoint Configuration Management REST API.
 */
public class EndpointConfigurationsApiServiceImpl implements EndpointConfigurationsApiService {

    @Autowired
    private ConfigurationManagementService configurationManagementService;

    @Override
    public Response createEndpointConfiguration(EndpointConfigurationAdd endpointConfigurationAdd) {

        EndpointConfiguration endpointConfiguration = configurationManagementService.
                addEndpointConfiguration(endpointConfigurationAdd);
        URI location = null;
        try {
            location = ContextLoader.buildURIForHeader(
                    V1_API_PATH_COMPONENT + CONFIGURATION_CONTEXT_PATH + "/" +
                            URLEncoder.encode(endpointConfiguration.getReferenceName(), StandardCharsets.UTF_8.name())
                                    .replace(PLUS, URL_ENCODED_SPACE));
        } catch (UnsupportedEncodingException e) {
            ErrorResponse errorResponse =
                    new ErrorResponse.Builder().withMessage("Error due to unsupported encoding.").build();
            throw new APIError(Response.Status.METHOD_NOT_ALLOWED, errorResponse);
        }
        return Response.created(location).entity(endpointConfiguration).build();
    }

    @Override
    public Response deleteEndPointConfiguration(String referenceName) {

        configurationManagementService.deleteEndpointConfiguration(referenceName);
        return Response.noContent().build();
    }

    @Override
    public Response getEndpointConfiguration(String referenceName) {

        return Response.ok().entity(configurationManagementService.getEndpointConfiguration(referenceName))
                .build();
    }

    @Override
    public Response getEndpointConfigurationList() {

        return Response.ok().entity(configurationManagementService.getEndpointConfigurations()).build();

    }

    @Override
    public Response updateEndpointConfiguration(String referenceName, EndpointConfigurationUpdateRequest
            endpointConfigurationUpdateRequest) {

        return Response.ok()
                .entity(configurationManagementService.updateEndpointConfiguration
                        (referenceName, endpointConfigurationUpdateRequest)).build();
    }
}
