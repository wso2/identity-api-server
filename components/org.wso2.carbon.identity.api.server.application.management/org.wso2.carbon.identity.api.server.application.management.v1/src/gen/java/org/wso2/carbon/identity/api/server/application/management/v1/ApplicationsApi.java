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

package org.wso2.carbon.identity.api.server.application.management.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.Error;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ResidentApplication;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/applications")
@Api(description = "The applications API")

public class ApplicationsApi  {

    @Autowired
    private ApplicationsApiService delegate;

    @Valid
    @POST
    
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add application ", notes = "This API provides the capability to store the application information provided by users. ", response = ApplicationModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response.", response = ApplicationModel.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response createApplication(@ApiParam(value = "This represents the application to be created." ,required=true) @Valid ApplicationModel applicationModel,     @Valid@ApiParam(value = "Pre-defined template to use when creating the application. ")  @QueryParam("template") String template) {

        return delegate.createApplication(applicationModel,  template );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/advanced-configs")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete advanced configurations of an application by id ", notes = "This API provides the capability to delete advanced configurations an application by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Advanced Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteAdvancedConfigurations(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteAdvancedConfigurations(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an application by id ", notes = "This API provides the capability to delete an application by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteApplication(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteApplication(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/authentication-sequences")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete authentication sequence of an application by id ", notes = "This API provides the capability to delete the authentication sequence an application by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authentication Sequence", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteAuthenticationSequence(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteAuthenticationSequence(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/claims")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete claim configuration of an application by id ", notes = "This API provides the capability to delete an application by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Claims", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteClaimConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteClaimConfiguration(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/auth-protocols/{inboundProtocolId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete Custom Inbound authentication protocol parameters of an application. ", notes = "This API provides the capability to delete Custom Inbound authentication protocol of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Custom", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete Success", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteCustomInboundConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "Inbound Authentication Protocol ID",required=true) @PathParam("inboundProtocolId") String inboundProtocolId) {

        return delegate.deleteCustomInboundConfiguration(applicationId,  inboundProtocolId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/auth-protocols/")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete inbound protocol configurations of an application. ", notes = "This API provides the capability to delete inbound protocol configurations of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete successful", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteInboundAuthenticationConfigurations(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteInboundAuthenticationConfigurations(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/auth-protocols/oidc")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete OIDC authentication protocol parameters of an application. ", notes = "This API provides the capability to delete OIDC authentication protocol parameters of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete Success", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteInboundOAuthConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteInboundOAuthConfiguration(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/auth-protocols/saml")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete SAML2 authentication protocol parameters of an application. ", notes = "This API provides the capability to delete SAML2 authentication protocol parameters of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - SAML", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete successful", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteInboundSAMLConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteInboundSAMLConfiguration(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/auth-protocols/passive-sts")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete Passive STS authentication protocol parameters of an application. ", notes = "This API provides the capability to delete Passive STS authentication protocol parameters of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Passive STS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete Success", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deletePassiveStsConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deletePassiveStsConfiguration(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/provisioning-configs")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete provisioning configurations of an application by id ", notes = "This API provides the capability to delete provisioning configurations an application by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Provisioning Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteProvisioningConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteProvisioningConfiguration(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/auth-protocols/ws-trust")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete WS Trust authentication protocol parameters of an application. ", notes = "This API provides the capability to delete WS Trust authentication protocol parameters of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - WS Trust", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete Success", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteWSTrustConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteWSTrustConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/advanced-configs")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrive advanced configurations of an application by id ", notes = "This API provides the capability to retrive the advanced configurations of an application by id. ", response = AdvancedApplicationConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Advanced Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AdvancedApplicationConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAdvancedConfigurations(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getAdvancedConfigurations(applicationId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List applications ", notes = "This API provides the capability to retrive the list of applications. ", response = ApplicationListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAllApplications(    @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrival of records. Supports 'sw', 'co', 'ew' and 'eq' operations. Currently supports only filtering based on the 'name' attribute.  /applications?filter=name+eq+user_portal /applications?filter=name+co+prod ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Define the order in which the retrieved records should be sorted. _This parameter is not supported yet._ ", allowableValues="ascending, descending")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. _This parameter is not supported yet._ ")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "Specifies the required parameters in the response _This parameter is not supported yet_ ")  @QueryParam("attributes") String attributes) {

        return delegate.getAllApplications(limit,  offset,  filter,  sortOrder,  sortBy,  attributes );
    }

    @Valid
    @GET
    @Path("/{applicationId}")
    
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Retrive application by id ", notes = "This API provides the capability to retrive the application information by id. ", response = ApplicationModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationModel.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getApplication(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getApplication(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/authentication-sequences")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrive authentication sequence of application by id ", notes = "This API provides the capability to retrive authentication sequence of an application by id. ", response = AuthenticationSequence.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authentication Sequence", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuthenticationSequence.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAuthenticationSequence(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getAuthenticationSequence(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/claims")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrive claim configuration application by id ", notes = "This API provides the capability to retrive the application claim configuration by id. ", response = ClaimConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Claims", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ClaimConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getClaimConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getClaimConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/auth-protocols/{inboundProtocolId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Custom Inbound authentication protocol parameters of an application. ", notes = "This API provides the capability to retrieve Custom Inbound authentication protocol parameters of an application. ", response = CustomInboundProtocolConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Custom", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = CustomInboundProtocolConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCustomInboundConfiguration(@ApiParam(value = "Id of the application",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "Inbound Authentication Protocol ID",required=true) @PathParam("inboundProtocolId") String inboundProtocolId) {

        return delegate.getCustomInboundConfiguration(applicationId,  inboundProtocolId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/auth-protocols/")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve inbound protocol configurations of an application. ", notes = "This API provides the capability to retrive authentication protocol configurations of an application. ", response = InboundProtocols.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = InboundProtocols.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getInboundAuthenticationConfigurations(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getInboundAuthenticationConfigurations(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/auth-protocols/oidc")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve OIDC authentication protocol parameters of an application. ", notes = "This API provides the capability to retrieve OIDC authentication protocol parameters of an application. ", response = OpenIDConnectConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OpenIDConnectConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getInboundOAuthConfiguration(@ApiParam(value = "Id of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getInboundOAuthConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/auth-protocols/saml")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve SAML2 authentication protocol parameters of an application. ", notes = "This API provides the capability to retrive SAML2 authentication protocol parameters of an application. ", response = SAML2Configuration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - SAML", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SAML2Configuration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getInboundSAMLConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getInboundSAMLConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/auth-protocols/passive-sts")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Passive STS authentication protocol parameters of an application. ", notes = "This API provides the capability to retrieve Passive STS authentication protocol parameters of an application. ", response = PassiveStsConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Passive STS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = PassiveStsConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getPassiveStsConfiguration(@ApiParam(value = "Id of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getPassiveStsConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/provisioning-configs")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrive provisioning configurations of application by id ", notes = "This API provides the capability to retrive the provisioning configurations of an application by id. ", response = ProvisioningConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Provisioning Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ProvisioningConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getProvisioningConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getProvisioningConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/resident")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Resident Service Provider Information. ", notes = "This API provides the capability to retrive the resident SP information. ", response = ResidentApplication.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Resident Application", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ResidentApplication.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getResidentApplication() {

        return delegate.getResidentApplication();
    }

    @Valid
    @GET
    @Path("/{applicationId}/auth-protocols/ws-trust")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve WS Trust authentication protocol parameters of an application. ", notes = "This API provides the capability to retrieve Passive STS authentication protocol parameters of an application. ", response = WSTrustConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - WS Trust", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = WSTrustConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getWSTrustConfiguration(@ApiParam(value = "Id of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getWSTrustConfiguration(applicationId );
    }

    @Valid
    @POST
    @Path("/{applicationId}/auth-protocols/oidc/regenerate-secret")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Regenerate the OAuth2/OIDC client secret. ", notes = "This API provides regenerate the OAuth2/OIDC client secret. ", response = OpenIDConnectConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OpenIDConnectConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response regenerateOAuthApplicationSecret(@ApiParam(value = "Id of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.regenerateOAuthApplicationSecret(applicationId );
    }

    @Valid
    @POST
    @Path("/{applicationId}/auth-protocols/oidc/revoke")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Revoke the OAuth2/OIDC client configuration of an application. ", notes = "This API provides revoke the OAuth2/OIDC client configuration of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response revokeOAuthApplication(@ApiParam(value = "Id of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.revokeOAuthApplication(applicationId );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/advanced-configs")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the the advanced configurations of an application by id ", notes = "This API provides the capability to update the advanced configurations of an application by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Advanced Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = AdvancedApplicationConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateAdvancedConfigurations(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents advanced configurations of the application to be updated." ,required=true) @Valid AdvancedApplicationConfiguration advancedApplicationConfiguration) {

        return delegate.updateAdvancedConfigurations(applicationId,  advancedApplicationConfiguration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an application by id ", notes = "This API provides the capability to update an application. ", response = ApplicationModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response.", response = ApplicationModel.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateApplication(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the application to be updated." ,required=true) @Valid ApplicationModel applicationModel) {

        return delegate.updateApplication(applicationId,  applicationModel );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/authentication-sequences")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the the authentication sequence of an application by id ", notes = "This API provides the capability to update the authentication sequence of an application by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authentication Sequence", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = AuthenticationSequence.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateAuthenticationSequence(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents provisioning configurations of the application to be updated." ,required=true) @Valid AuthenticationSequence authenticationSequence) {

        return delegate.updateAuthenticationSequence(applicationId,  authenticationSequence );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/claims")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the claim configuration of an application by id ", notes = "This API provides the capability to update claim configuration of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Claims", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = ClaimConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateClaimConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the application to be updated." ,required=true) @Valid ClaimConfiguration claimConfiguration) {

        return delegate.updateClaimConfiguration(applicationId,  claimConfiguration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/auth-protocols/{inboundProtocolId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update WS Trust authentication protocol parameters of an application. ", notes = "This API provides the capability to store WS Trust authentication protocol parameters of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Custom", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = CustomInboundProtocolConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateCustomInboundConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "Inbound Authentication Protocol ID",required=true) @PathParam("inboundProtocolId") String inboundProtocolId, @ApiParam(value = "This represents the Custom Inbound authentication protocol parameters of an application." ,required=true) @Valid CustomInboundProtocolConfiguration customInboundProtocolConfiguration) {

        return delegate.updateCustomInboundConfiguration(applicationId,  inboundProtocolId,  customInboundProtocolConfiguration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/auth-protocols/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update inbound protocol configurations of an application. ", notes = "This API provides the capability to update inbound protocol configurations of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = InboundProtocols.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateInboundAuthenticationConfigurations(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the inbound protocol configurations of the application." ,required=true) @Valid InboundProtocols inboundProtocols) {

        return delegate.updateInboundAuthenticationConfigurations(applicationId,  inboundProtocols );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/auth-protocols/oidc")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update OIDC authentication protocol parameters of an application. ", notes = "This API provides the capability to store OIDC authentication protocol parameters of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = OpenIDConnectConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateInboundOAuthConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the OIDC authentication protocol parameters of an application." ,required=true) @Valid OpenIDConnectConfiguration openIDConnectConfiguration) {

        return delegate.updateInboundOAuthConfiguration(applicationId,  openIDConnectConfiguration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/auth-protocols/saml")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update SAML2 authentication protocol parameters of an application. ", notes = "This API provides the capability to store SAML2 authentication protocol parameters of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - SAML", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = SAML2Configuration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateInboundSAMLConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the SAML2 protocol attributes of the application." ,required=true) @Valid SAML2Configuration saML2Configuration) {

        return delegate.updateInboundSAMLConfiguration(applicationId,  saML2Configuration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/auth-protocols/passive-sts")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Passive STS authentication protocol parameters of an application. ", notes = "This API provides the capability to store passive STS authentication protocol parameters of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Passive STS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = PassiveStsConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updatePassiveStsConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the Passive STS authentication protocol parameters of an application." ,required=true) @Valid PassiveStsConfiguration passiveStsConfiguration) {

        return delegate.updatePassiveStsConfiguration(applicationId,  passiveStsConfiguration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/provisioning-configs")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the the provisioning configurations of an application by id ", notes = "This API provides the capability to update the provisioning configurations of an application by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Provisioning Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = ProvisioningConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateProvisioningConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents provisioning configurations of the application to be updated." ,required=true) @Valid ProvisioningConfiguration provisioningConfiguration) {

        return delegate.updateProvisioningConfiguration(applicationId,  provisioningConfiguration );
    }

    @Valid
    @PUT
    @Path("/resident")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Resident Application's provisioning configuration. ", notes = "This API provides the capability to update Resident Service Provider Configuration. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Resident Application", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = ProvisioningConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateResidentApplication(@ApiParam(value = "This represents the provisioning configuration of the resident application." ,required=true) @Valid ProvisioningConfiguration provisioningConfiguration) {

        return delegate.updateResidentApplication(provisioningConfiguration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/auth-protocols/ws-trust")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update WS Trust authentication protocol parameters of an application. ", notes = "This API provides the capability to store WS Trust authentication protocol parameters of an application. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - WS Trust" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = WSTrustConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateWSTrustConfiguration(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the Passive STS authentication protocol parameters of an application." ,required=true) @Valid WSTrustConfiguration wsTrustConfiguration) {

        return delegate.updateWSTrustConfiguration(applicationId,  wsTrustConfiguration );
    }

}
