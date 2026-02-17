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

package org.wso2.carbon.identity.api.server.idp.v1;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.idp.v1.factories.IdentityProvidersApiServiceFactory;
import org.wso2.carbon.identity.api.server.idp.v1.model.AssociationRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.AssociationResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Claims;
import org.wso2.carbon.identity.api.server.idp.v1.model.ConnectedApps;
import org.wso2.carbon.identity.api.server.idp.v1.model.Error;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.FederatedAuthenticatorRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdPGroup;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderPOSTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderTemplate;
import org.wso2.carbon.identity.api.server.idp.v1.model.IdentityProviderTemplateListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.JustInTimeProvisioning;
import java.util.List;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticator;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaFederatedAuthenticatorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.MetaOutboundConnectorListItem;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnector;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorListResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundConnectorPUTRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.OutboundProvisioningRequest;
import org.wso2.carbon.identity.api.server.idp.v1.model.Patch;
import org.wso2.carbon.identity.api.server.idp.v1.model.ProvisioningResponse;
import org.wso2.carbon.identity.api.server.idp.v1.model.Roles;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/identity-providers")
@Api(description = "The identity-providers API")

public class IdentityProvidersApi  {

    private final IdentityProvidersApiService delegate;

    public IdentityProvidersApi() {

        this.delegate = IdentityProvidersApiServiceFactory.getIdentityProvidersApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Add a new identity provider ", notes = "This API provides the capability to create a new identity provider. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/create <br> <b>Scope required:</b> <br>     * internal_idp_create ", response = IdentityProviderResponse.class, authorizations = {
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
    public Response addIDP(@ApiParam(value = "This represents the identity provider to be created." ,required=true) @Valid IdentityProviderPOSTRequest identityProviderPOSTRequest) {

        return delegate.addIDP(identityProviderPOSTRequest );
    }

    @Valid
    @POST
    @Path("/templates")
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Create a new IdP template ", notes = "This API provides the capability to create a new IdP template. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Template management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addIDPTemplate(@ApiParam(value = "This represents the identity provider template to be created." ,required=true) @Valid IdentityProviderTemplate identityProviderTemplate) {

        return delegate.addIDPTemplate(identityProviderTemplate );
    }

    @Valid
    @DELETE
    @Path("/{identity-provider-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an identity provider by using the identity provider's ID.  ", notes = "This API provides the capability to delete an identity provider by giving its ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/delete <br> <b>Scope required:</b> <br>     * internal_idp_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Identity Providers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteIDP(@ApiParam(value = "ID of the identity provider",required=true) @PathParam("identity-provider-id") String identityProviderId,     @Valid@ApiParam(value = "Enforces the forceful deletion of an identity provider, federated authenticator or an outbound provisioning connector even though it is referred by a service provider. ", defaultValue="false") @DefaultValue("false")  @QueryParam("force") Boolean force) {

        return delegate.deleteIDP(identityProviderId,  force );
    }

    @Valid
    @DELETE
    @Path("/templates/{template-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an IdP template using the template ID. ", notes = "This API provides the capability to delete an IdP template using the template ID. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Template management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteIDPTemplate(@ApiParam(value = "ID of the IdP template.",required=true) @PathParam("template-id") String templateId) {

        return delegate.deleteIDPTemplate(templateId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/export")

    @Produces({ "application/json", "application/yaml", "application/xml", "application/octet-stream" })
    @ApiOperation(value = "Export identity provider in XML, YAML, or JSON file formats ", notes = "This API provides the capability to retrieve the identity provider if the given ID as a XML, YAML, or JSON file. Use LOCAL as the ID to export resident IDP configurations.<br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/idpmgt/view <br>   <b>Scope required:</b> <br>       * internal_idp_view ", response = String.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Identity Providers", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful response", response = String.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response exportIDPToFile(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId,     @Valid@ApiParam(value = "Specifies whether to exclude secrets when exporting an identity provider. ", defaultValue="true") @DefaultValue("true")  @QueryParam("excludeSecrets") Boolean excludeSecrets,     @Valid @ApiParam(value = "Content type of the file. " , allowableValues="application/json, application/xml, application/yaml, application/x-yaml, text/yaml, text/xml, text/json", defaultValue="application/yaml")@HeaderParam("Accept") String accept) {

        return delegate.exportIDPToFile(identityProviderId,  excludeSecrets,  accept );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/claims")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Claim config of an identity provider ", notes = "This API provides the claim config for an identity provider. This includes idp-to-local claim mappings, claims to be outbound provisioned, userID claim URI, and role claim URI. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = Claims.class, authorizations = {
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
    public Response getClaimConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getClaimConfig(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/connected-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Connected applications of an identity provider ", notes = "This API provides the list of applications that use this identity provider for federated authentication/provisioning. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = ConnectedApps.class, authorizations = {
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
    public Response getConnectedApps(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId,     @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations and also complex queries with 'and' operations. E.g. /identity-providers?filter=name+sw+\"google\"+and+isEnabled+eq+\"true\" ")  @QueryParam("filter") String filter) {

        return delegate.getConnectedApps(identityProviderId,  limit,  offset,  filter );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/implicit-association")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Federated association config of an identity provider ", notes = "This API provides the federated association config of an identity provider. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = AssociationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Association", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = AssociationResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getFederatedAssociationConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getFederatedAssociationConfig(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/federated-authenticators/{federated-authenticator-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve federated authenticator config of an identity provider ", notes = "This API provides the capability to retrieve the federated authenticator information of an identity provider by giving the federated authenticator's ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = FederatedAuthenticator.class, authorizations = {
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
    public Response getFederatedAuthenticator(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "ID of the federated authenticator.",required=true) @PathParam("federated-authenticator-id") String federatedAuthenticatorId) {

        return delegate.getFederatedAuthenticator(identityProviderId,  federatedAuthenticatorId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/federated-authenticators")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Federated authenticators of an identity provider ", notes = "This API provides a list of federated authenticators enabled for a specific identity provider identified by its ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = FederatedAuthenticatorListResponse.class, authorizations = {
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
    public Response getFederatedAuthenticators(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getFederatedAuthenticators(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/groups")

    @Produces({ "application/json" })
    @ApiOperation(value = "Group config of an identity provider ", notes = "This API provides the group config of an identity provider. This is a list of groups the idp can return.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = IdPGroup.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Groups", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful response", response = IdPGroup.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getGroupConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getGroupConfig(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}")
    
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Retrieve identity provider by identity provider's ID ", notes = "This API provides the capability to retrieve the identity provider details by using its ID. Furthermore, by specifying the \"Accept : application/xml\" header, it provides the ability to export IdP data as XML. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = IdentityProviderResponse.class, authorizations = {
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
    public Response getIDP(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getIDP(identityProviderId );
    }

    @Valid
    @GET
    @Path("/templates/{template-id}")
    
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Retrieve identity provider template by ID ", notes = "This API provides the capability to retrieve an identity provider template using its ID. ", response = IdentityProviderTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Template management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = IdentityProviderTemplate.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getIDPTemplate(@ApiParam(value = "ID of the IdP template.",required=true) @PathParam("template-id") String templateId) {

        return delegate.getIDPTemplate(templateId );
    }

    @Valid
    @GET
    @Path("/templates")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List identity provider templates ", notes = "This API provides the list of available identity provider templates. ", response = IdentityProviderTemplateListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Template management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = IdentityProviderTemplateListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getIDPTemplates(    @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam
            ("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ")
    @QueryParam("offset") Integer offset, @Context SearchContext searchContext) {

        return delegate.getIDPTemplates(limit,  offset, searchContext );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List identity providers ", notes = "This API provides the capability to retrieve the list of identity providers.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = IdentityProviderListResponse.class, authorizations = {
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
    public Response getIDPs(    @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations and also complex queries with 'and' operations. E.g. /identity-providers?filter=name+sw+\"google\"+and+isEnabled+eq+\"true\" ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Defines the order in which the retrieved records should be sorted. _This parameter is not supported yet_ ", allowableValues="ASC, DESC")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. _This parameter is not supported yet_ ")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "Specifies the required parameters in the response. _This parameter is not supported yet_ ")  @QueryParam("requiredAttributes") String requiredAttributes) {

        return delegate.getIDPs(limit,  offset,  filter,  sortOrder,  sortBy,  requiredAttributes );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/provisioning/jit")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Just-In-Time provisioning config of an identity provider ", notes = "This API retrieves the Just-In-Time provisioning config of an identity provider by specifying the identity provider ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = JustInTimeProvisioning.class, authorizations = {
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
    public Response getJITConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getJITConfig(identityProviderId );
    }

    @Valid
    @GET
    @Path("/meta/federated-authenticators/{federated-authenticator-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Metadata about a supported federated authenticator ", notes = "This API provides the details of a single supported federated authenticator for an identity provider in the the identity server. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = MetaFederatedAuthenticator.class, authorizations = {
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
    @ApiOperation(value = "Metadata about supported federated authenticators of identity providers ", notes = "This API provides the list of supported federated authenticators for an identity provider in the the identity server. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view", response = MetaFederatedAuthenticatorListItem.class, responseContainer = "List", authorizations = {
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
    @ApiOperation(value = "Metadata about supported outbound provisioning connectors ", notes = "This API provides the details of a single supported outbound provisioning connector for an IdP in the identity server. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view", response = MetaOutboundConnector.class, authorizations = {
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
    @ApiOperation(value = "Metadata about supported outbound provisioning connectors by identity providers in the identity server ", notes = "This API provides the list of supported federated authenticators for an IdP in the identity server. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view", response = MetaOutboundConnectorListItem.class, responseContainer = "List", authorizations = {
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
    @ApiOperation(value = "Retrieve outbound provisioning connector of an identity provider ", notes = "This API provides the capability to retrieve the outbound provisioning connector information of an identity provider by specifying the provisioning connector's ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = OutboundConnector.class, authorizations = {
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
    public Response getOutboundConnector(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "ID of the outbound provisioning connector.",required=true) @PathParam("outbound-provisioning-connector-id") String outboundProvisioningConnectorId) {

        return delegate.getOutboundConnector(identityProviderId,  outboundProvisioningConnectorId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/provisioning/outbound-connectors")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Outbound provisioning connectors of an identity provider ", notes = "This API provides a list of outbound provisioning connectors enabled for an identity provider. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = OutboundConnectorListResponse.class, authorizations = {
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
    public Response getOutboundConnectors(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getOutboundConnectors(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/provisioning")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Provisioning entities of an identity provider ", notes = "This API provides a list of available provisioning entities for an identity provider. This includes just-in-time provisioning config and outbound provisioning connectors <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = ProvisioningResponse.class, authorizations = {
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
    public Response getProvisioningConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getProvisioningConfig(identityProviderId );
    }

    @Valid
    @GET
    @Path("/{identity-provider-id}/roles")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Role config of an identity provider ", notes = "This API provides the role config of an identity provider. This includes idp-to-local role mappings and/or a list of roles to be outbound-provisioned <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = Roles.class, authorizations = {
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
    public Response getRoleConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId) {

        return delegate.getRoleConfig(identityProviderId );
    }

    @Valid
    @POST
    @Path("/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create identity provider from an exported XML, YAML or JSON file ", notes = "This API provides the capability to import an identity provider from the information provided as a file.<br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/idpmgt/create <br>   <b>Scope required:</b> <br>       * internal_idp_create ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Identity Providers", })
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response importIDPFromFile(@Multipart(value = "file", required = false) InputStream fileInputStream,@Multipart(value = "file" , required = false) Attachment fileDetail) {

        return delegate.importIDPFromFile(fileInputStream, fileDetail );
    }

    @Valid
    @PATCH
    @Path("/{identity-provider-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch an identity provider property by ID. Patch is supported only for key-value pairs ", notes = "This API provides the capability to update an identity provider property using patch request. IdP patch is supported only for key-value pairs. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = IdentityProviderResponse.class, authorizations = {
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
    public Response patchIDP(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "" ,required=true) @Valid List<Patch> patch) {

        return delegate.patchIDP(identityProviderId,  patch );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/claims")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the claims of an identity provider ", notes = "This API provides the capability to update the claim config of an existing identity provider. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = Claims.class, authorizations = {
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
    public Response updateClaimConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the claim config to be updated" ,required=true) @Valid Claims claims) {

        return delegate.updateClaimConfig(identityProviderId,  claims );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/implicit-association")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the federated association config of an identity provider ", notes = "This API provides the capability to update the federated association config of an identity provider by specifying the identity provider ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = AssociationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Association", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = AssociationResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateFederatedAssociationConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the federated association config to be updated." ,required=true) @Valid AssociationRequest associationRequest) {

        return delegate.updateFederatedAssociationConfig(identityProviderId,  associationRequest );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/federated-authenticators/{federated-authenticator-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a federated authenticator of an identity provider by using authenticator id ", notes = "This API provides the capability to update an identity provider's federated authenticator config by specifying the authenticator ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = FederatedAuthenticator.class, authorizations = {
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
    public Response updateFederatedAuthenticator(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "ID of the federated authenticator.",required=true) @PathParam("federated-authenticator-id") String federatedAuthenticatorId, @ApiParam(value = "This represents the federated authenticator to be updated" ,required=true) @Valid FederatedAuthenticatorPUTRequest federatedAuthenticatorPUTRequest) {

        return delegate.updateFederatedAuthenticator(identityProviderId,  federatedAuthenticatorId,  federatedAuthenticatorPUTRequest );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/federated-authenticators")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Federated authenticators of an identity provider ", notes = "This API updates federated authenticators enabled for a specific identity provider identified by its ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = FederatedAuthenticatorListResponse.class, authorizations = {
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
    public Response updateFederatedAuthenticators(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the federated authenticator to be updated" ,required=true) @Valid FederatedAuthenticatorRequest federatedAuthenticatorRequest) {

        return delegate.updateFederatedAuthenticators(identityProviderId,  federatedAuthenticatorRequest );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/groups")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the group config of an identity provider ", notes = "This API provides the capability to update the group config of an identity provider by specifying the identity provider ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = IdPGroup.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Groups", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful response", response = IdPGroup.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateGroupConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the group config to be updated." ,required=true) @Valid List<IdPGroup> idPGroup) {

        return delegate.updateGroupConfig(identityProviderId,  idPGroup );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update identity provider from an exported YAML, XML or JSON file ", notes = "This API provides the capability to update an existing identity provider from the information provided as a file. Use LOCAL as the ID to update resident IDP configurations.<br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/idpmgt/update <br>   <b>Scope required:</b> <br>       * internal_idp_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Identity Providers", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully Updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateIDPFromFile(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @Multipart(value = "file", required = false) InputStream fileInputStream,@Multipart(value = "file" , required = false) Attachment fileDetail) {

        return delegate.updateIDPFromFile(identityProviderId,  fileInputStream, fileDetail );
    }

    @Valid
    @PUT
    @Path("/templates/{template-id}")
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Update the IdP template of a given template ID. ", notes = "This API provides the capability to update the IdP template of a given template ID. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Template management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully updated", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateIDPTemplate(@ApiParam(value = "ID of the IdP template.",required=true) @PathParam("template-id") String templateId, @ApiParam(value = "This represents the identity provider template to be created." ,required=true) @Valid IdentityProviderTemplate identityProviderTemplate) {

        return delegate.updateIDPTemplate(templateId,  identityProviderTemplate );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/provisioning/jit")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the just-in-time provisioning config of an identity provider ", notes = "This API provides the capability to update the just-in-time provisioning config of an identity provider by specifying the identity provider's ID. This includes the ability to enable/disable JIT provisioning, change provisioning userstore and enable/disable user prompts for username, password and consent. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = JustInTimeProvisioning.class, authorizations = {
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
    public Response updateJITConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the just-in-time provisioning config to be updated." ,required=true) @Valid JustInTimeProvisioning justInTimeProvisioning) {

        return delegate.updateJITConfig(identityProviderId,  justInTimeProvisioning );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/provisioning/outbound-connectors/{outbound-provisioning-connector-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an outbound provisioning connector of an identity provider ", notes = "This API provides the capability to update an outbound provisioning connector config of an identity provider by specifying the provisioning connector's ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = OutboundConnector.class, authorizations = {
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
    public Response updateOutboundConnector(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "ID of the outbound provisioning connector.",required=true) @PathParam("outbound-provisioning-connector-id") String outboundProvisioningConnectorId, @ApiParam(value = "This represents the outbound provisioning connector to be updated" ,required=true) @Valid OutboundConnectorPUTRequest outboundConnectorPUTRequest) {

        return delegate.updateOutboundConnector(identityProviderId,  outboundProvisioningConnectorId,  outboundConnectorPUTRequest );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/provisioning/outbound-connectors")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update outbound provisioning connectors of an identity provider ", notes = "This API provides updates the list of outbound provisioning connectors enabled for an identity provider. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/view <br> <b>Scope required:</b> <br>     * internal_idp_view ", response = OutboundConnectorListResponse.class, authorizations = {
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
    public Response updateOutboundConnectors(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the outbound provisioning connector to be updated" ,required=true) @Valid OutboundProvisioningRequest outboundProvisioningRequest) {

        return delegate.updateOutboundConnectors(identityProviderId,  outboundProvisioningRequest );
    }

    @Valid
    @PUT
    @Path("/{identity-provider-id}/roles")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the role config of an identity provider ", notes = "This API provides the capability to update the role config of an identity provider by specifying the identity provider ID. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/identity/idpmgt/update <br> <b>Scope required:</b> <br>     * internal_idp_update ", response = Roles.class, authorizations = {
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
    public Response updateRoleConfig(@ApiParam(value = "ID of the identity provider.",required=true) @PathParam("identity-provider-id") String identityProviderId, @ApiParam(value = "This represents the role config to be updated." ,required=true) @Valid Roles roles) {

        return delegate.updateRoleConfig(identityProviderId,  roles );
    }

}
