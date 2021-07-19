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

package org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1;

import org.springframework.beans.factory.annotation.Autowired;

import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.EndpointConfiguration;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.EndpointConfigurationAdd;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.EndpointConfigurationUpdateRequest;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.Error;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

@Path("/endpoint-configurations")
@Api(description = "The endpoint-configurations API")

public class EndpointConfigurationsApi {

    @Autowired
    private EndpointConfigurationsApiService delegate;

    @Valid
    @POST

    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Create an endpoint Configuration", notes = "This API provides the capability to create an endpoint configuration ", response = EndpointConfiguration.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Configuration",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful Response", response = EndpointConfiguration.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 409, message = "Conflict", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response createEndpointConfiguration(@ApiParam(value = "") @Valid EndpointConfigurationAdd endpointConfigurationAdd) {

        return delegate.createEndpointConfiguration(endpointConfigurationAdd);
    }

    @Valid
    @DELETE
    @Path("/{reference-name}")

    @Produces({"application/json"})
    @ApiOperation(value = "Delete an endpoint confuguration by name", notes = "This API provides the capability to delete a endpoint configuration by name. ", response = Void.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Configuration",})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content", response = Void.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteEndPointConfiguration(@ApiParam(value = "reference-name of the endpoint configuration", required = true) @PathParam("reference-name") String referenceName) {

        return delegate.deleteEndPointConfiguration(referenceName);
    }

    @Valid
    @GET
    @Path("/{reference-name}")

    @Produces({"application/json"})
    @ApiOperation(value = "Retrieve endpoint configuration by reference name", notes = "This API provides the capability to retrieve an endpoint configuration by name. ", response = EndpointConfiguration.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Configuration",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = EndpointConfiguration.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getEndpointConfiguration(@ApiParam(value = "reference-name of the endpoint configuration", required = true) @PathParam("reference-name") String referenceName) {

        return delegate.getEndpointConfiguration(referenceName);
    }

    @Valid
    @GET

    @Produces({"application/json"})
    @ApiOperation(value = "Get a list of configured endpoints", notes = "This API provides the capability to retrieve the list of configured endponit URLs. ", response = EndpointConfiguration.class, responseContainer = "List", authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Configuration",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = EndpointConfiguration.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getEndpointConfigurationList() {

        return delegate.getEndpointConfigurationList();
    }

    @Valid
    @PUT
    @Path("/{reference-name}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Update a endpoint configuration", notes = "This API provides the capability to update an endpoint configuration by reference-name. ", response = EndpointConfiguration.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Configuration"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = EndpointConfiguration.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 405, message = "Method Not Allowed.", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateEndpointConfiguration(@ApiParam(value = "reference-name of the endpoint configuration", required = true) @PathParam("reference-name") String referenceName, @ApiParam(value = "", required = true) @Valid EndpointConfigurationUpdateRequest endpointConfigurationUpdateRequest) {

        return delegate.updateEndpointConfiguration(referenceName, endpointConfigurationUpdateRequest);
    }
}
