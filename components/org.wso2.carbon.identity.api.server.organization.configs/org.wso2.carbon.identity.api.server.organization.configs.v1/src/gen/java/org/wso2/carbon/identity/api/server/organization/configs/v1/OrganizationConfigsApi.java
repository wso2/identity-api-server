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

package org.wso2.carbon.identity.api.server.organization.configs.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.organization.configs.v1.factories.OrganizationConfigsApiServiceFactory;
import org.wso2.carbon.identity.api.server.organization.configs.v1.model.Config;
import org.wso2.carbon.identity.api.server.organization.configs.v1.model.Error;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/organization-configs")
@Api(description = "The organization-configs API")

public class OrganizationConfigsApi  {

    private final OrganizationConfigsApiService delegate;

    public OrganizationConfigsApi() {

        this.delegate = OrganizationConfigsApiServiceFactory.getOrganizationConfigsApi();
    }

    @Valid
    @POST
    @Path("/discovery")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add organization discovery configuration.", notes = "This API provides the capability to add discovery configuration for the primary organization. <br>  The `defaultParam` attribute sets the discovery parameter used to resolve the organization. (e.g., `orgHandle`, `orgName`) <br>    <b>Scope(Permission) required:</b> <br>     * internal_config_mgt_add ", response = Config.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Discovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful Response", response = Config.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 409, message = "Resource already exists.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response createDiscoveryConfig(@ApiParam(value = "" ) @Valid Config config) {

        return delegate.createDiscoveryConfig(config );
    }

    @Valid
    @DELETE
    @Path("/discovery")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete organization discovery configuration.", notes = "This API provides the capability to delete discovery configuration of the primary organization.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/delete <br>   <b>Scope required:</b> <br>     * internal_config_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Discovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No content.", response = Void.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response deleteDiscoveryConfig() {

        return delegate.deleteDiscoveryConfig();
    }

    @Valid
    @GET
    @Path("/discovery")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get organization discovery configuration.", notes = "This API facilitates the retrieval of discovery configuration of the primary organization. <br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/view <br>   <b>Scope required:</b> <br>     * internal_config_mgt_view ", response = Config.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Discovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response.", response = Config.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response getDiscoveryConfig() {

        return delegate.getDiscoveryConfig();
    }

    @Valid
    @PUT
    @Path("/discovery")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update organization discovery configuration.", notes = "This API provides the capability to update discovery configuration of the primary organization. <br>  The `defaultParam` attribute sets the discovery parameter used to resolve the organization. (e.g., `orgHandle`, `orgName`) <br>    <b>Scope(Permission) required:</b> <br>     * internal_config_mgt_update ", response = Config.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Discovery" })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful Response", response = Config.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response updateDiscoveryConfig(@ApiParam(value = "" ) @Valid Config config) {

        return delegate.updateDiscoveryConfig(config );
    }

}
