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
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VcApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.factories.VcApiServiceFactory;

import javax.validation.constraints.*;

@Path("/vc")
@Api(description = "The vc API")

public class VcApi  {

    private final VcApiService delegate;

    public VcApi() {

        this.delegate = VcApiServiceFactory.getVcApi();
    }

    @Valid
    @POST
    @Path("/credential-configurations")
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
    @Path("/credential-configurations/{config-id}")
    
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
    @POST
    @Path("/credential-configurations/{config-id}/offer")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Generate or regenerate credential offer", notes = "Creates a new credential offer with a backend-generated UUID, or regenerates an existing one. The offer URL is constructed according to OIDC4VCI specification. Expiration is managed by the backend using system-configured defaults.", response = VCCredentialConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Credential Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Offer generated or regenerated successfully", response = VCCredentialConfiguration.class),
        @ApiResponse(code = 404, message = "Configuration not found", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response generateVCCredentialOffer(@ApiParam(value = "Server-generated UUID of the configuration.",required=true) @PathParam("config-id") String configId) {

        return delegate.generateVCCredentialOffer(configId );
    }

    @Valid
    @GET
    @Path("/credential-configurations/{config-id}")
    
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
    @Path("/credential-configurations")
    
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
    public Response listVCCredentialConfigurations(    @Valid@ApiParam(value = "Base64 encoded cursor value for backward pagination. ")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Base64 encoded cursor value for forward pagination. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Specifies the required attributes in the response. Only 'properties' attribute is currently supported.")  @QueryParam("attributes") String attributes) {

        return delegate.listVCCredentialConfigurations(before,  after,  filter,  limit,  attributes );
    }

    @Valid
    @DELETE
    @Path("/credential-configurations/{config-id}/offer")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Revoke credential offer", notes = "Revokes and deletes the existing credential offer. The offerId field will be set to null. Returns 404 if no offer exists.", response = VCCredentialConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Credential Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Offer revoked successfully", response = VCCredentialConfiguration.class),
        @ApiResponse(code = 404, message = "Configuration or offer not found", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response revokeVCCredentialOffer(@ApiParam(value = "Server-generated UUID of the configuration.",required=true) @PathParam("config-id") String configId) {

        return delegate.revokeVCCredentialOffer(configId );
    }

    @Valid
    @PUT
    @Path("/credential-configurations/{config-id}")
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
