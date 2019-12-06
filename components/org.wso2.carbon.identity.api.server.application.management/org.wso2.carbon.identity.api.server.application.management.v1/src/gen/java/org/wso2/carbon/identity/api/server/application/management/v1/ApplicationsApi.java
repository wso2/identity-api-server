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
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;

import org.wso2.carbon.identity.api.server.application.management.v1.AdaptiveAuthTemplates;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationResponseModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthProtocolMetadata;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.Error;
import java.io.File;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.OIDCMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ResidentApplication;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustMetaData;
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
    @ApiOperation(value = "Add application ", notes = "This API provides the capability to store the application information provided by users. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response createApplication(@ApiParam(value = "This represents the application to be created." ,required=true) @Valid ApplicationModel applicationModel,     @Valid@ApiParam(value = "Pre-defined template to use when creating the application. ")  @QueryParam("template") String template) {

        return delegate.createApplication(applicationModel,  template );
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
    @Path("/{applicationId}/inbound-protocols/{inboundProtocolId}")
    
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
    @Path("/{applicationId}/inbound-protocols/oidc")
    
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
    @Path("/{applicationId}/inbound-protocols/saml")
    
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
    @Path("/{applicationId}/inbound-protocols/passive-sts")
    
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
    @Path("/{applicationId}/inbound-protocols/ws-trust")
    
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
    @Path("/{applicationId}/export")
    
    @Produces({ "application/octet-stream", "application/json" })
    @ApiOperation(value = "Export an application as an XML file. ", notes = "This API provides the capability to retrieve the application as an XML file. ", response = Object.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Object.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response exportApplication(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId,     @Valid@ApiParam(value = "Specifies whether to export secrets when exporting an application. ", defaultValue="false") @DefaultValue("false")  @QueryParam("exportSecrets") Boolean exportSecrets) {

        return delegate.exportApplication(applicationId,  exportSecrets );
    }

    @Valid
    @GET
    @Path("/meta/adaptive-auth-templates")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the sample adaptive authentication templates. ", notes = "This API provides the capability to retrieve the sample adaptive authentication templates. ", response = AdaptiveAuthTemplates.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AdaptiveAuthTemplates.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAdaptiveAuthTemplates() {

        return delegate.getAdaptiveAuthTemplates();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List applications ", notes = "This API provides the capability to retrieve the list of applications. ", response = ApplicationListResponse.class, authorizations = {
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
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getAllApplications(    @Valid @Min(1)@ApiParam(value = "Maximum number of records to return. ", defaultValue="30") @DefaultValue("30")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ", defaultValue="0") @DefaultValue("0")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. Currently supports only filtering based on the 'name' attribute.  /applications?filter=name+eq+user_portal /applications?filter=name+co+prod ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Define the order in which the retrieved records should be sorted. _This parameter is not supported yet._ ", allowableValues="ASC, DESC")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. _This parameter is not supported yet._ ")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "Specifies the required parameters in the response _This parameter is not supported yet_ ")  @QueryParam("attributes") String attributes) {

        return delegate.getAllApplications(limit,  offset,  filter,  sortOrder,  sortBy,  attributes );
    }

    @Valid
    @GET
    @Path("/{applicationId}")
    
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "Retrieve application by id ", notes = "This API provides the capability to retrieve the application information by id. ", response = ApplicationResponseModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationResponseModel.class),
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
    @Path("/{applicationId}/inbound-protocols/{inboundProtocolId}")
    
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
    @Path("/meta/inbound-protocols/{inboundProtocolId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the metadata related to the custom auth protocol identified by the inboundProtocolId ", notes = "This API provides the capability to retrieve all the metadata related to the custom auth protocol identified by the inboundProtocolId. ", response = CustomInboundProtocolMetaData.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = CustomInboundProtocolMetaData.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCustomProtocolMetadata(@ApiParam(value = "Inbound Authentication Protocol ID",required=true) @PathParam("inboundProtocolId") String inboundProtocolId) {

        return delegate.getCustomProtocolMetadata(inboundProtocolId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/inbound-protocols/")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve inbound protocol configurations of an application. ", notes = "This API provides the capability to retrieve authentication protocol configurations of an application. ", response = InboundProtocols.class, authorizations = {
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
    @Path("/{applicationId}/inbound-protocols/oidc")
    
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
    @Path("/meta/inbound-protocols")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the list of inbound authentication protocols available. ", notes = "This API provides the capability to retrieve the list of inbound authentication protocols available. If the query parameter 'customOnly' is set to true, only custom inbound protocols will be listed. ", response = AuthProtocolMetadata.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuthProtocolMetadata.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getInboundProtocols(    @Valid@ApiParam(value = "Send only the custom inbound protocols. ", defaultValue="false") @DefaultValue("false")  @QueryParam("customOnly") Boolean customOnly) {

        return delegate.getInboundProtocols(customOnly );
    }

    @Valid
    @GET
    @Path("/{applicationId}/inbound-protocols/saml")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve SAML2 authentication protocol parameters of an application. ", notes = "This API provides the capability to retrieve SAML2 authentication protocol parameters of an application. ", response = SAML2ServiceProvider.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - SAML", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SAML2ServiceProvider.class),
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
    @Path("/meta/inbound-protocols/oidc")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the metadata related to the authentication protocol OAuth / OIDC ", notes = "This API provides the capability to retrieve all the metadata related to the authentication  protocol OAuth / OIDC. ", response = OIDCMetaData.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OIDCMetaData.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getOIDCMetadata() {

        return delegate.getOIDCMetadata();
    }

    @Valid
    @GET
    @Path("/{applicationId}/inbound-protocols/passive-sts")
    
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
    @Path("/resident")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Resident Service Provider Information. ", notes = "This API provides the capability to retrieve the resident SP information. ", response = ResidentApplication.class, authorizations = {
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
    @Path("/meta/inbound-protocols/saml")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the metadata related to the auth protocol SAML ", notes = "This API provides the capability to retrieve all the metadata related to the auth protocol SAML. ", response = SAMLMetaData.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SAMLMetaData.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSAMLMetadata() {

        return delegate.getSAMLMetadata();
    }

    @Valid
    @GET
    @Path("/{applicationId}/inbound-protocols/ws-trust")
    
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
    @GET
    @Path("/meta/inbound-protocols/ws-trust")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the metadata related to the auth protocol WS_Trust ", notes = "This API provides the capability to retrieve all the metadata related to the auth protocol WS_Trust. ", response = WSTrustMetaData.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = WSTrustMetaData.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getWSTrustMetadata() {

        return delegate.getWSTrustMetadata();
    }

    @Valid
    @POST
    @Path("/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Import an application from file. ", notes = "This API provides the capability to store the application information, provided as a file. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfully created.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response importApplication( @Multipart(value = "file", required = false) InputStream fileInputStream, @Multipart(value = "file" , required = false) Attachment fileDetail) {

        return delegate.importApplication(fileInputStream, fileDetail );
    }

    @Valid
    @PUT
    @Path("/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an application from file. ", notes = "This API provides the capability to update an application from information exported as a file. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response importApplicationForUpdate( @Multipart(value = "file", required = false) InputStream fileInputStream, @Multipart(value = "file" , required = false) Attachment fileDetail) {

        return delegate.importApplicationForUpdate(fileInputStream, fileDetail );
    }

    @Valid
    @PATCH
    @Path("/{applicationId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Partially update an application by id ", notes = "This API provides the capability to partially update an application by id. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchApplication(@ApiParam(value = "Id of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the application details to be updated." ,required=true) @Valid ApplicationPatchModel applicationPatchModel) {

        return delegate.patchApplication(applicationId,  applicationPatchModel );
    }

    @Valid
    @POST
    @Path("/{applicationId}/inbound-protocols/oidc/regenerate-secret")
    
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
    public Response regenerateOAuthClientSecret(@ApiParam(value = "Id of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.regenerateOAuthClientSecret(applicationId );
    }

    @Valid
    @POST
    @Path("/{applicationId}/inbound-protocols/oidc/revoke")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Revokes the OAuth2/OIDC client. ", notes = "This API provides revokes the OAuth2/OIDC client secret. To re-activate the client the client secret needs to be regenerated. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Application Revoked", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response revokeOAuthClient(@ApiParam(value = "Id of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.revokeOAuthClient(applicationId );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/inbound-protocols/{inboundProtocolId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update WS Trust authentication protocol parameters of an application. ", notes = "This API provides the capability to store WS Trust authentication protocol parameters of an application. ", response = CustomInboundProtocolConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Custom", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = CustomInboundProtocolConfiguration.class),
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
    @Path("/{applicationId}/inbound-protocols/oidc")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update OIDC authentication protocol parameters of an application. ", notes = "This API provides the capability to store OIDC authentication protocol parameters of an application. ", response = OpenIDConnectConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = OpenIDConnectConfiguration.class),
        @ApiResponse(code = 201, message = "Created", response = OpenIDConnectConfiguration.class),
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
    @Path("/{applicationId}/inbound-protocols/saml")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update SAML2 authentication protocol parameters of an application. ", notes = "This API provides the capability to store SAML2 authentication protocol parameters of an application. - There are three methods to create/update SAML2 authentication protocol configuration.     1. Metadata File (Need to send Base64 encoded content of the metadata file.)     2. Metadata URL.     3. Manual configuration. ", response = SAML2ServiceProvider.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - SAML", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = SAML2ServiceProvider.class),
        @ApiResponse(code = 201, message = "Successful response.", response = SAML2ServiceProvider.class),
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
    @Path("/{applicationId}/inbound-protocols/passive-sts")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Passive STS authentication protocol parameters of an application. ", notes = "This API provides the capability to store passive STS authentication protocol parameters of an application. ", response = PassiveStsConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Passive STS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = PassiveStsConfiguration.class),
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
    @Path("/{applicationId}/inbound-protocols/ws-trust")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update WS Trust authentication protocol parameters of an application. ", notes = "This API provides the capability to store WS Trust authentication protocol parameters of an application. ", response = WSTrustConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - WS Trust" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = WSTrustConfiguration.class),
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
