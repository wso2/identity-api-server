/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.registration.execution.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.wso2.carbon.identity.api.server.registration.execution.v1.factories.RegistrationApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/registration")
@Api(description = "The registration API")

public class RegistrationApi {

    private final RegistrationApiService delegate;

    public RegistrationApi() {

        this.delegate = RegistrationApiServiceFactory.getRegistrationApi();
    }

    @Valid
    @POST
    @Path("/initiate")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Initiate a registration flow", notes = "", response = RegistrationSubmissionResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Initiate Registration",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully initiated registration flow", response = RegistrationSubmissionResponse.class)
    })
    public Response registrationInitiatePost(@ApiParam(value = "", required = true) @Valid RegistrationInitiationRequest registrationInitiationRequest) {

        return delegate.registrationInitiatePost(registrationInitiationRequest);
    }

    @Valid
    @POST
    @Path("/submit")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @ApiOperation(value = "Submit a registration step", notes = "", response = RegistrationSubmissionResponse.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags = {"Submit Registration Step"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully submitted registration step", response = RegistrationSubmissionResponse.class)
    })
    public Response registrationSubmitPost(@ApiParam(value = "", required = true) @Valid RegistrationSubmissionRequest registrationSubmissionRequest) {

        return delegate.registrationSubmitPost(registrationSubmissionRequest);
    }

}
