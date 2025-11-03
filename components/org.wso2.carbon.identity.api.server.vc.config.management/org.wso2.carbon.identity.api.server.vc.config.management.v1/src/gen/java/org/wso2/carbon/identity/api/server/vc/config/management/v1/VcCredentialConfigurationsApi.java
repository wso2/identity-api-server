/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vc.config.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.vc.config.management.v1.Error;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfiguration;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationCreationModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationList;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCCredentialConfigurationUpdateModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VcCredentialConfigurationsApiService;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.factories.VcCredentialConfigurationsApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/vc-credential-configurations")
@Api(description = "The vc-credential-configurations API")

public class VcCredentialConfigurationsApi  {

    private final VcCredentialConfigurationsApiService delegate;

    public VcCredentialConfigurationsApi() {

        this.delegate = VcCredentialConfigurationsApiServiceFactory.getVcCredentialConfigurationsApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add new configuration", notes = "", response = VCCredentialConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Credential Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created", response = VCCredentialConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class)
    })
    public Response addVCCredentialConfiguration(@ApiParam(value = "" ,required=true) @Valid VCCredentialConfigurationCreationModel vcCredentialConfigurationCreationModel) {

        return delegate.addVCCredentialConfiguration(vcCredentialConfigurationCreationModel );
    }

    @Valid
    @DELETE
    @Path("/{config-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete configuration", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Credential Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Action Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteVCCredentialConfiguration(@ApiParam(value = "Server-generated UUID of the configuration.",required=true) @PathParam("config-id") String configId) {

        return delegate.deleteVCCredentialConfiguration(configId );
    }

    @Valid
    @GET
    @Path("/{config-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get configuration", notes = "", response = VCCredentialConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Credential Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Configuration", response = VCCredentialConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getVCCredentialConfiguration(@ApiParam(value = "Server-generated UUID of the configuration.",required=true) @PathParam("config-id") String configId) {

        return delegate.getVCCredentialConfiguration(configId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List configurations", notes = "", response = VCCredentialConfigurationList.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Credential Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Array of configs", response = VCCredentialConfigurationList.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response listVCCredentialConfigurations() {

        return delegate.listVCCredentialConfigurations();
    }

    @Valid
    @PUT
    @Path("/{config-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update configuration", notes = "", response = VCCredentialConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Credential Configurations" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Updated", response = VCCredentialConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateVCCredentialConfiguration(@ApiParam(value = "Server-generated UUID of the configuration.",required=true) @PathParam("config-id") String configId, @ApiParam(value = "" ,required=true) @Valid VCCredentialConfigurationUpdateModel vcCredentialConfigurationUpdateModel) {

        return delegate.updateVCCredentialConfiguration(configId,  vcCredentialConfigurationUpdateModel );
    }

}
