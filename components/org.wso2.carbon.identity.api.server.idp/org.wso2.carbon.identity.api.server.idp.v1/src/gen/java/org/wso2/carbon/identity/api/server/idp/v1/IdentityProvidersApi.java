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

package org.wso2.carbon.identity.api.server.idp.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;

import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.ConnectedApps;
import org.wso2.carbon.identity.api.server.idp.v1.model.Error;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.JustInTimeProvisioning;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticatorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnectorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Patch;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;
import org.wso2.carbon.identity.api.server.idp.v1.IdentityProvidersApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/identity-providers")
@Api(description = "The identity-providers API")

public class IdentityProvidersApi  {

    @Autowired
    private IdentityProvidersApiService delegate;

    @Valid
    @POST
    
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Add a new Identity Provider ", notes = "This API provides the capability to create a new Identity Provider ", response = IdentityProviderResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = IdentityProviderResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addIDP(@ApiParam(value = "This represents the identity provider to be created" ,required=true) @Valid IdentityProviderPOSTRequest identityProviderPOSTRequest) {

        return delegate.addIDP(identityProviderPOSTRequest );
    }

    @Valid
    @DELETE
    @Path("/{identity-provider-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an identity provider by using identity provider's id ", notes = "This API provides the capability to delete an identity provider by giving it id ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteIDP(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId,     @Valid@ApiParam(value = "Enforce the forceful deletetion of either an identity provider, federated authenticator or an outbound provisioning connector eventhough it is being reffered by a service provider ", defaultValue="false") @DefaultValue("false")  @QueryParam("force") Boolean force) {

        return delegate.deleteIDP(identityProviderId,  force );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/claims")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Claim config of an identity provider ", notes = "This API provides the claim config for an identity provider. This includes idp-to-local claim mappings, claims to be outbound provisioning, userID claim URI and role claim URI ", response = Claims.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Claims", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Claims.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getClaimConfig(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getClaimConfig(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/connected-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Connected applications of an identity provider ", notes = "This API provides the list of applications which are using this identity provider for federated/provisioning. ", response = ConnectedApps.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Connected Apps", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = ConnectedApps.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getConnectedApps(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId,     @Valid@ApiParam(value = "Maximum number of records to return ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination ")  @QueryParam("offset") Integer offset) {

        return delegate.getConnectedApps(identityProviderId,  limit,  offset );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/federated-authenticators/{federated-authenticator-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrive federated authenticator config of an identity provider ", notes = "This API provides the capability to retrive the federated authenticator information of an identity provider by giving the federated authenticator id ", response = FederatedAuthenticator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Federated Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = FederatedAuthenticator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getFederatedAuthenticator(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "Id of the federated authenticator",required=true) @PathParam("federated-authenticator-id") String federatedAuthenticatorId) {

        return delegate.getFederatedAuthenticator(identityProviderId,  federatedAuthenticatorId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/federated-authenticators")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Federated authenticators of an identity provider ", notes = "This API provides a list of federated authenticators enabled for a specific identity provider identified by its id ", response = FederatedAuthenticatorListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Federated Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = FederatedAuthenticatorListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getFederatedAuthenticators(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getFederatedAuthenticators(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}")
    
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Retrive identity provider by identity provider's id ", notes = "'This API provides the capability to retrive the identity provider details by using its id. Furthermore, by specifying the \"Accept : application/xml\" header, it provides the ability to export IDP data as XML' ", response = IdentityProviderResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = IdentityProviderResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getIDP(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getIDP(identityProviderId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List identity providers ", notes = "This API provides the capability to retrive the list of identity providers ", response = IdentityProviderListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = IdentityProviderListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getIDPs(    @Valid@ApiParam(value = "Maximum number of records to return ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination ")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrival of records. Supports 'sw', 'co', 'ew' and 'eq' operations and also complex queries with 'and' operations. E.g. /identity-providers?filter=name+sw+\"google\"+and+isEnabled+eq+\"true\" ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Define the order in which the retrieved records should be sorted _This parameter is not supported yet_ ", allowableValues="ASC, DESC")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted _This parameter is not supported yet_ ")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "Specifies the required parameters in the response _This parameter is not supported yet_ ")  @QueryParam("attributes") String attributes) {

        return delegate.getIDPs(limit,  offset,  filter,  sortOrder,  sortBy,  attributes );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/provisioning/jit")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Just-In-Time provisioning config of an identity provider ", notes = "This API retrives the Just-In-Time provisioning config of an identity provider by specifying identity provider id ", response = JustInTimeProvisioning.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Provisioning", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = JustInTimeProvisioning.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getJITConfig(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getJITConfig(identityProviderId );
    }

    @Valid
    @GET
    @Path("/meta/federated-authenticators/{federated-authenticator-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Metadata about a supported federated authenticator ", notes = "'This API provides the details of a single supported federated authenticator for an identity provider in Identity Server' ", response = MetaFederatedAuthenticator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = MetaFederatedAuthenticator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getMetaFederatedAuthenticator(@ApiParam(value = "ID of a federated authenticator",required=true) @PathParam("federated-authenticator-id") String federatedAuthenticatorId) {

        return delegate.getMetaFederatedAuthenticator(federatedAuthenticatorId );
    }

    @Valid
    @GET
    @Path("/meta/federated-authenticators")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Metadata about supported federated authenticators of identity providers ", notes = "This API provides the list of supported federated authenticators for an identity provider in Identity Server", response = MetaFederatedAuthenticatorListItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = MetaFederatedAuthenticatorListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getMetaFederatedAuthenticators() {

        return delegate.getMetaFederatedAuthenticators();
    }

    @Valid
    @GET
    @Path("/meta/outbound-provisioning-connectors/{outbound-provisioning-connector-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Metadata about supported outbound provisioning connectors ", notes = "This API provides the details of a single supported outbound provisioning connectors for an IDP in Identity Server", response = MetaOutboundConnector.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = MetaOutboundConnector.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getMetaOutboundConnector(@ApiParam(value = "ID of a Outbound Provisioning Connector",required=true) @PathParam("outbound-provisioning-connector-id") String outboundProvisioningConnectorId) {

        return delegate.getMetaOutboundConnector(outboundProvisioningConnectorId );
    }

    @Valid
    @GET
    @Path("/meta/outbound-provisioning-connectors")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Metadata about supported outbound provisioning connectors by identity providers in Identity Server ", notes = "This API provides the list of supported federated authenticators for an IDP in Identity Server", response = MetaOutboundConnectorListItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = MetaOutboundConnectorListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getMetaOutboundConnectors() {

        return delegate.getMetaOutboundConnectors();
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/provisioning/outbound-connectors/{outbound-provisioning-connector-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrive outbount provisioning connector of an identity provider ", notes = "This API provides the capability to retrive the outbount provisioning connector information of an identity provider by specifying provisioning connector id ", response = OutboundConnector.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Provisioning", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OutboundConnector.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getOutboundConnector(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "Id of the outbound provisioning connector",required=true) @PathParam("outbound-provisioning-connector-id") String outboundProvisioningConnectorId) {

        return delegate.getOutboundConnector(identityProviderId,  outboundProvisioningConnectorId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/provisioning/outbound-connectors")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Outbound provisioning connectors of an identity provider ", notes = "This API provides a list of outbound provisioning connectors enabled for an identity provider ", response = OutboundConnectorListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Provisioning", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OutboundConnectorListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getOutboundConnectors(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getOutboundConnectors(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/provisioning")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Provisioning entities of an identity provider ", notes = "'This API provides a list of available provisioning entities of an identity provider. This includes just-in-time provisioning config and outbound provisioning connectors' ", response = ProvisioningResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Provisioning", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = ProvisioningResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getProvisioningConfig(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getProvisioningConfig(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/roles")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Role config of an identity provider ", notes = "This API provides the role config of an identity provider. This includes idp-to-local role mappings and/or a list of roles to be outbound-provisioned ", response = Roles.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Roles", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Roles.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getRoleConfig(@ApiParam(value = "Id of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getRoleConfig(identityProviderId );
    }

    @Valid
    @PATCH
    @Path("/{identity-provider-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch an identity provider property by id. Patch is supported only for key-value pairs ", notes = "This API provides the capability to update an identity provider property using patch request. IDP patch is supported only for key-value pairs ", response = IdentityProviderResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = IdentityProviderResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchIDP(@ApiParam(value = "Id of the Identity Provider",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "" ,required=true) @Valid List<Patch> patch) {

        return delegate.patchIDP(identityProviderId,  patch );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/claims")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the claims of an identity provider ", notes = "This API provides the capability to update the claim config of an existing identity provider ", response = Claims.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Claims", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Claims.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateClaimConfig(@ApiParam(value = "Id of the Identity Provider",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the claim config to be updated" ,required=true) @Valid Claims claims) {

        return delegate.updateClaimConfig(identityProviderId,  claims );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/federated-authenticators/{federated-authenticator-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a federated authenticator of an identity provider by using authenticator id ", notes = "This API provides the capability to update an identity provider's federated authenticator config by specifying authenticator id ", response = FederatedAuthenticator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Federated Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = FederatedAuthenticator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateFederatedAuthenticator(@ApiParam(value = "Id of the Identity Provider",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "Id of the federated authenticator",required=true) @PathParam("federated-authenticator-id") String federatedAuthenticatorId, @ApiParam(value = "This represents the federated authenticator to be updated" ,required=true) @Valid FederatedAuthenticatorPUTRequest federatedAuthenticatorPUTRequest) {

        return delegate.updateFederatedAuthenticator(identityProviderId,  federatedAuthenticatorId,  federatedAuthenticatorPUTRequest );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/provisioning/jit")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the just-in-time provisioning config of an identity provider ", notes = "This API provides the capability to update the just-in-time provisioning config of an identity provider by specifying identity provider id. This includes the ability to enable/disable JIT provisioning, change provisioning userstore and enable/disable user prompts for username, password and consent ", response = JustInTimeProvisioning.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Provisioning", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = JustInTimeProvisioning.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateJITConfig(@ApiParam(value = "Id of the Identity Provider",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the just-in-time provisioning config to be updated" ,required=true) @Valid JustInTimeProvisioning justInTimeProvisioning) {

        return delegate.updateJITConfig(identityProviderId,  justInTimeProvisioning );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/provisioning/outbound-connectors/{outbound-provisioning-connector-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an outbound provisioning connector of an identity provider ", notes = "This API provides the capability to update an outbound provisioning connector config of an identity provider by specifying the provisioning connector id ", response = OutboundConnector.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Provisioning", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OutboundConnector.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateOutboundConnector(@ApiParam(value = "Id of the Identity Provider",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "Id of the outbound provisioning connector",required=true) @PathParam("outbound-provisioning-connector-id") String outboundProvisioningConnectorId, @ApiParam(value = "This represents the outbound provisioning connector to be updated" ,required=true) @Valid OutboundConnectorPUTRequest outboundConnectorPUTRequest) {

        return delegate.updateOutboundConnector(identityProviderId,  outboundProvisioningConnectorId,  outboundConnectorPUTRequest );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/roles")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the role config of an identity provider ", notes = "This API provides the capability to update the role config of an identity provider by specifying identity provider id ", response = Roles.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Roles" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Roles.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateRoleConfig(@ApiParam(value = "Id of the Identity Provider",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the role config to be updated" ,required=true) @Valid Roles roles) {

        return delegate.updateRoleConfig(identityProviderId,  roles );
    }

}
