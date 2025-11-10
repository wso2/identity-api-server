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
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOffer;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferCreationModel;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferList;
import org.wso2.carbon.identity.api.server.vc.config.management.v1.VCOfferUpdateModel;
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
    @Path("/credential-configs")
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
    @POST
    @Path("/offers")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a credential offer", notes = "", response = VCOffer.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Offers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created", response = VCOffer.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response createVCOffer(@ApiParam(value = "" ,required=true) @Valid VCOfferCreationModel vcOfferCreationModel) {

        return delegate.createVCOffer(vcOfferCreationModel );
    }

    @Valid
    @DELETE
    @Path("/credential-configs/{config-id}")
    
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
    @DELETE
    @Path("/offers/{offer-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete offer by ID", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Offers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteVCOffer(@ApiParam(value = "Server-generated offer identifier (opaque).",required=true) @PathParam("offer-id") String offerId) {

        return delegate.deleteVCOffer(offerId );
    }

    @Valid
    @GET
    @Path("/credential-configs/{config-id}")
    
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
    @Path("/offers/{offer-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get offer by ID", notes = "", response = VCOffer.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Offers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Offer", response = VCOffer.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getVCOffer(@ApiParam(value = "Server-generated offer identifier (opaque).",required=true) @PathParam("offer-id") String offerId) {

        return delegate.getVCOffer(offerId );
    }

    @Valid
    @GET
    @Path("/credential-configs")
    
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
    @GET
    @Path("/offers")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List credential offers", notes = "", response = VCOfferList.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Offers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Offer list", response = VCOfferList.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response listVCOffers() {

        return delegate.listVCOffers();
    }

    @Valid
    @PUT
    @Path("/credential-configs/{config-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update configuration", notes = "", response = VCCredentialConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Credential Configurations", })
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

    @Valid
    @PUT
    @Path("/offers/{offer-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update offer by ID", notes = "", response = VCOffer.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Offers" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Updated offer", response = VCOffer.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateVCOffer(@ApiParam(value = "Server-generated offer identifier (opaque).",required=true) @PathParam("offer-id") String offerId, @ApiParam(value = "" ,required=true) @Valid VCOfferUpdateModel vcOfferUpdateModel) {

        return delegate.updateVCOffer(offerId,  vcOfferUpdateModel );
    }

}
