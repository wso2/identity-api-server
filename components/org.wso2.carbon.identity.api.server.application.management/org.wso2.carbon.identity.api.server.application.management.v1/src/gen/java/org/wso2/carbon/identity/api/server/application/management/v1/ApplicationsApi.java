/*
 * Copyright (c) 2023-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.v1;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.application.management.v1.AdaptiveAuthTemplates;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationOwner;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationResponseModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationShareAllRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationSharingPatchRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplateModel;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationTemplatesList;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationUnshareAllRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthProtocolMetadata;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedAPICreationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedAPIPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthorizedAPIResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ConfiguredAuthenticatorsModal;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.CustomInboundProtocolMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.Error;
import java.io.File;
import org.wso2.carbon.identity.api.server.application.management.v1.GroupBasicInfo;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocolListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowGenerateRequest;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowGenerateResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowResultResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.LoginFlowStatusResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.OIDCMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProcessSuccessResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ResidentApplication;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2ServiceProvider;
import org.wso2.carbon.identity.api.server.application.management.v1.SAMLMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.ScriptUpdateModel;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedApplicationsResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.SharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.api.server.application.management.v1.factories.ApplicationsApiServiceFactory;

import javax.validation.constraints.*;

@Path("/applications")
@Api(description = "The applications API")

public class ApplicationsApi  {

    private final ApplicationsApiService delegate;

    public ApplicationsApi() {

        this.delegate = ApplicationsApiServiceFactory.getApplicationsApi();
    }

    @Valid
    @POST
    @Path("/{applicationId}/authorized-apis")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Authorized an API to the application. ", notes = "This API provides the capability to authorized an API to the application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authorized APIs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addAuthorizedAPI(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "" ) @Valid AuthorizedAPICreationModel authorizedAPICreationModel) {

        return delegate.addAuthorizedAPI(applicationId,  authorizedAPICreationModel );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/owner")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Change application owner. ", notes = "This API provides the capability to change the application owner. <br>   <b>Scope(Permission) required:</b> `internal_organization_admin` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response changeApplicationOwner(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "" ) @Valid ApplicationOwner applicationOwner) {

        return delegate.changeApplicationOwner(applicationId,  applicationOwner );
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add application. ", notes = "This API provides the capability to store the application information that is provided by users. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_create` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response createApplication(@ApiParam(value = "This represents the application to be created." ,required=true) @Valid ApplicationModel applicationModel,     @Valid@ApiParam(value = "Pre-defined template to use when creating the application. ")  @QueryParam("template") String template) {

        return delegate.createApplication(applicationModel,  template );
    }

    @Valid
    @POST
    @Path("/templates")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add application template. ", notes = "This API provides the capability to store the application template provided by users. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_create` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response createApplicationTemplate(@ApiParam(value = "This represents the application template to be created." ,required=true) @Valid ApplicationTemplateModel applicationTemplateModel) {

        return delegate.createApplicationTemplate(applicationTemplateModel );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete application by ID. ", notes = "This API provides the capability to delete an application by ID. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_delete` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteApplication(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteApplication(applicationId );
    }

    @Valid
    @DELETE
    @Path("/templates/{template-id}")
    
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Delete application template by template ID. ", notes = "This API provides the capability to delete an application template by template ID. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_delete` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteApplicationTemplate(@ApiParam(value = "Application template ID. This should be a valid locale. ",required=true) @PathParam("template-id") String templateId) {

        return delegate.deleteApplicationTemplate(templateId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/authorized-apis/{apiId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Remove API authorization from the application. ", notes = "This API provides the capability to delete an authorized API of the application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authorized APIs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "No Content", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteAuthorizedAPI(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "ID of the API resource.",required=true) @PathParam("apiId") String apiId) {

        return delegate.deleteAuthorizedAPI(applicationId,  apiId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/inbound-protocols/{inboundProtocolId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete custom inbound authentication protocol parameters. ", notes = "This API provides the capability to delete custom inbound authentication protocol of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_delete` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Custom", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete Success", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteCustomInboundConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "Inbound Authentication Protocol ID",required=true) @PathParam("inboundProtocolId") String inboundProtocolId) {

        return delegate.deleteCustomInboundConfiguration(applicationId,  inboundProtocolId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/inbound-protocols/oidc")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete OIDC authentication protocol parameters. ", notes = "This API provides the capability to delete OIDC authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_delete` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete Success", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteInboundOAuthConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteInboundOAuthConfiguration(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/inbound-protocols/saml")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete SAML2 authentication protocol parameters. ", notes = "This API provides the capability to delete SAML2 authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_delete` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - SAML", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete successful", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteInboundSAMLConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteInboundSAMLConfiguration(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/inbound-protocols/passive-sts")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete Passive STS authentication protocol parameters. ", notes = "This API provides the capability to delete Passive STS authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_delete` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Passive STS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete Success", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deletePassiveStsConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deletePassiveStsConfiguration(applicationId );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/inbound-protocols/ws-trust")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete WS Trust authentication protocol parameters. ", notes = "This API provides the capability to delete WS Trust authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_delete` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - WS Trust", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete Success", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteWSTrustConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.deleteWSTrustConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/export")
    
    @Produces({ "application/octet-stream", "application/json" })
    @ApiOperation(value = "Export application as an XML file. ", notes = "This API provides the capability to retrieve the application as an XML file. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = Object.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Object.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response exportApplication(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId,     @Valid@ApiParam(value = "Specifies whether to export secrets when exporting an application. ", defaultValue="false") @DefaultValue("false")  @QueryParam("exportSecrets") Boolean exportSecrets) {

        return delegate.exportApplication(applicationId,  exportSecrets );
    }

    @Valid
    @GET
    @Path("/{applicationId}/exportFile")
    
    @Produces({ "application/json", "application/yaml", "application/xml", "application/octet-stream" })
    @ApiOperation(value = "Export application in XML, YAML, or JSON file formats. ", notes = "This API provides the capability to retrieve the application in XML, YAML, or JSON format. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = String.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = String.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response exportApplicationAsFile(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId,     @Valid@ApiParam(value = "Specifies whether to export secrets when exporting an application. ", defaultValue="false") @DefaultValue("false")  @QueryParam("exportSecrets") Boolean exportSecrets,     @Valid @ApiParam(value = "Content type of the file. " , allowableValues="application/json, application/xml, application/yaml, application/x-yaml, text/yaml", defaultValue="application/xml")@HeaderParam("Accept") String accept) {

        return delegate.exportApplicationAsFile(applicationId,  exportSecrets,  accept );
    }

    @Valid
    @POST
    @Path("/loginflow/generate")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Initiate login flow generation", notes = "This API provides the capability to initiate the generation of a login flow. <br> <b>Scope required:</b> * internal_application_mgt_update ", response = LoginFlowGenerateResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "LoginFlow", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Accepted", response = LoginFlowGenerateResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response generateLoginFlow(@ApiParam(value = "" ,required=true) @Valid LoginFlowGenerateRequest loginFlowGenerateRequest) {

        return delegate.generateLoginFlow(loginFlowGenerateRequest );
    }

    @Valid
    @GET
    @Path("/meta/adaptive-auth-templates")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve adaptive authentication sample templates. ", notes = "This API provides the capability to retrieve the sample adaptive authentication templates. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = AdaptiveAuthTemplates.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AdaptiveAuthTemplates.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAdaptiveAuthTemplates() {

        return delegate.getAdaptiveAuthTemplates();
    }

    @Valid
    @GET
    @Path("/templates")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List application templates. ", notes = "This API provides the capability to retrieve the list of templates available. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = ApplicationTemplatesList.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationTemplatesList.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getAllApplicationTemplates(    @Valid@ApiParam(value = "Maximum number of records to return. ")
                                                       @QueryParam("limit") Integer limit,     @Valid@ApiParam(value
            = "Number of records to skip for pagination. ")  @QueryParam("offset") Integer offset, @Context
                                                   SearchContext searchContext) {

        return delegate.getAllApplicationTemplates(limit,  offset, searchContext );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List applications. ", notes = "This API provides the capability to retrieve the list of applications. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = ApplicationListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getAllApplications(    @Valid @Min(1)@ApiParam(value = "Maximum number of records to return. ", defaultValue="30") @DefaultValue("30")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ", defaultValue="0") @DefaultValue("0")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew', and 'eq' operations with 'and', 'or' logical operators. Note that 'and' and 'or' operators in filters follow the general precedence of logical operators. For example, A and B or C and D = (A and B) or (C and D)). Currently supports only filtering based on the 'name', the 'clientId', and the 'issuer' attributes.  /applications?filter=name+eq+user_portal <br> /applications?filter=name+co+prod+or+clientId+co+123 ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Define the order in which the retrieved records should be sorted. _This parameter is not supported yet._ ", allowableValues="ASC, DESC")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. _This parameter is not supported yet._ ")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "Specifies the required parameters in the response. Only 'advancedConfigurations', 'templateId', 'templateVersion', 'clientId', 'issuer',  and 'associatedRoles.allowedAudience' attributes are currently supported.  /applications?attributes=advancedConfigurations,templateId,templateVersion,clientId,issuer, associatedRoles.allowedAudience ")  @QueryParam("attributes") String attributes,     @Valid@ApiParam(value = "Specifies whether to include or exclude system portals in the response.  Default will be treated as false if parameter is not preset in the request.  /applications?excludeSystemPortals=true ")  @QueryParam("excludeSystemPortals") Boolean excludeSystemPortals) {

        return delegate.getAllApplications(limit,  offset,  filter,  sortOrder,  sortBy,  attributes,  excludeSystemPortals );
    }

    @Valid
    @GET
    @Path("/{applicationId}")
    
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Retrieve application by ID. ", notes = "This API provides the capability to retrieve the application information by ID. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = ApplicationResponseModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationResponseModel.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getApplication(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getApplication(applicationId );
    }

    @Valid
    @GET
    @Path("/templates/{template-id}")
    
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Retrieve application template by ID. ", notes = "This API provides the capability to retrieve the application template from the template id. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = ApplicationTemplateModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationTemplateModel.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getApplicationTemplate(@ApiParam(value = "Application template ID. This should be a valid locale. ",required=true) @PathParam("template-id") String templateId) {

        return delegate.getApplicationTemplate(templateId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/authorized-apis")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get authorized APIs of the application. ", notes = "This API provides the capability to retrieve all the authorized APIs of the application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = AuthorizedAPIResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authorized APIs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuthorizedAPIResponse.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAuthorizedAPIs(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getAuthorizedAPIs(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/authenticators")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get configured authenticators. ", notes = "This API provides the capability to retrieve the configured authenticators. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = ConfiguredAuthenticatorsModal.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ConfiguredAuthenticatorsModal.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getConfiguredAuthenticators(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getConfiguredAuthenticators(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/inbound-protocols/{inboundProtocolId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve custom inbound authentication protocol parameters. ", notes = "This API provides the capability to retrieve custom inbound authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = CustomInboundProtocolConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Custom", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = CustomInboundProtocolConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCustomInboundConfiguration(@ApiParam(value = "ID of the application",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "Inbound Authentication Protocol ID",required=true) @PathParam("inboundProtocolId") String inboundProtocolId) {

        return delegate.getCustomInboundConfiguration(applicationId,  inboundProtocolId );
    }

    @Valid
    @GET
    @Path("/meta/inbound-protocols/{inboundProtocolId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the metadata related to the custom auth protocol identified by the inboundProtocolId. ", notes = "This API provides the capability to retrieve all the metadata related to the custom auth protocol identified by the inboundProtocolId. The URL encoded inbound protocol name is used as inboundProtocolId. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = CustomInboundProtocolMetaData.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = CustomInboundProtocolMetaData.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCustomProtocolMetadata(@ApiParam(value = "Inbound Authentication Protocol ID",required=true) @PathParam("inboundProtocolId") String inboundProtocolId) {

        return delegate.getCustomProtocolMetadata(inboundProtocolId );
    }

    @Valid
    @GET
    @Path("/meta/groups")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the list of groups available for the application. ", notes = "This API provides the capability to retrieve the list of groups available for the application. <br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/applicationmgt/view <br>   <b>Scope required:</b> <br>       * internal_application_mgt_view ", response = GroupBasicInfo.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = GroupBasicInfo.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getGroups(    @Valid@ApiParam(value = "The domain name of the user store used to filter the groups.  /applications/meta/groups?domain=PRIMARY ")  @QueryParam("domain") String domain,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew', and 'eq' operations with 'and', 'or' logical operators. Please note that 'and' and 'or' operators in filters follow the general precedence of logical operators ex: A and B or C and D = (A and B) or (C and D)). Currently supports only filtering based on the 'name', the 'clientId', and the 'issuer' attributes.  /applications?filter=name+eq+user_portal <br> /applications?filter=name+co+prod+or+clientId+co+123 ")  @QueryParam("filter") String filter) {

        return delegate.getGroups(domain,  filter );
    }

    @Valid
    @GET
    @Path("/{applicationId}/inbound-protocols/")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve inbound protocol configurations. ", notes = "This API provides the capability to retrieve authentication protocol configurations of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = InboundProtocolListItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = InboundProtocolListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getInboundAuthenticationConfigurations(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getInboundAuthenticationConfigurations(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/inbound-protocols/oidc")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve OIDC authentication protocol parameters. ", notes = "This API provides the capability to retrieve OIDC authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = OpenIDConnectConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OpenIDConnectConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getInboundOAuthConfiguration(@ApiParam(value = "ID of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getInboundOAuthConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/meta/inbound-protocols")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the list of inbound authentication protocols available. ", notes = "This API provides the capability to retrieve the list of inbound authentication protocols available. If the query parameter 'customOnly' is set to true, only custom inbound protocols will be listed. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = AuthProtocolMetadata.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = AuthProtocolMetadata.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
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
    @ApiOperation(value = "Retrieve SAML2 authentication protocol parameters. ", notes = "This API provides the capability to retrieve SAML2 authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = SAML2ServiceProvider.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - SAML", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = SAML2ServiceProvider.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getInboundSAMLConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getInboundSAMLConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/loginflow/result/{operation_id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the final login flow result", notes = "This API provides the capability to retrieve the final login flow result. <br> <b>Scope required:</b> * internal_application_mgt_update ", response = LoginFlowResultResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "LoginFlow", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = LoginFlowResultResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class)
    })
    public Response getLoginFlowGenerationResult(@ApiParam(value = "",required=true) @PathParam("operation_id") String operationId) {

        return delegate.getLoginFlowGenerationResult(operationId );
    }

    @Valid
    @GET
    @Path("/loginflow/status/{operation_id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the status of the login flow generation process", notes = "This API provides the capability to retrieve the status of the login flow generation process. <br> <b>Scope required:</b> * internal_application_mgt_update ", response = LoginFlowStatusResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "LoginFlow", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = LoginFlowStatusResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class)
    })
    public Response getLoginFlowGenerationStatus(@ApiParam(value = "",required=true) @PathParam("operation_id") String operationId) {

        return delegate.getLoginFlowGenerationStatus(operationId );
    }

    @Valid
    @GET
    @Path("/meta/inbound-protocols/oidc")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the metadata related to the authentication protocol OAuth / OIDC. ", notes = "This API provides the capability to retrieve all the metadata related  to the authentication protocol OAuth / OIDC. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = OIDCMetaData.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OIDCMetaData.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
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
    @ApiOperation(value = "Retrieve Passive STS authentication protocol parameters. ", notes = "This API provides the capability to retrieve Passive STS authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = PassiveStsConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Passive STS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = PassiveStsConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getPassiveStsConfiguration(@ApiParam(value = "ID of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getPassiveStsConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/resident")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get resident application. ", notes = "This API provides the capability to retrieve the resident application information. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = ResidentApplication.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Resident Application", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ResidentApplication.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
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
    @ApiOperation(value = "Retrieve all the metadata related to the auth protocol SAML. ", notes = "This API provides the capability to retrieve all the metadata related to the auth protocol SAML. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = SAMLMetaData.class, authorizations = {
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
    @ApiOperation(value = "Retrieve WS Trust authentication protocol parameters. ", notes = "This API provides the capability to retrieve Passive STS authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = WSTrustConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - WS Trust", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = WSTrustConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getWSTrustConfiguration(@ApiParam(value = "ID of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.getWSTrustConfiguration(applicationId );
    }

    @Valid
    @GET
    @Path("/meta/inbound-protocols/ws-trust")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve all the metadata related to the auth protocol WS Trust. ", notes = "This API provides the capability to retrieve all the metadata related to the auth protocol WS_Trust. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_view` ", response = WSTrustMetaData.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = WSTrustMetaData.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
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
    @ApiOperation(value = "Create application from an exported XML, YAML, or JSON file. ", notes = "This API provides the capability to create an application based on the information provided in an XML, YAML, or JSON file. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_create` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfully created.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response importApplication(@Multipart(value = "file", required = false) InputStream fileInputStream,@Multipart(value = "file" , required = false) Attachment fileDetail) {

        return delegate.importApplication(fileInputStream, fileDetail );
    }

    @Valid
    @PUT
    @Path("/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update application from an exported XML, YAML, or JSON file. ", notes = "This API provides the capability to update an application based on the information provided in an XML, YAML, or JSON file. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response importApplicationForUpdate(@Multipart(value = "file", required = false) InputStream fileInputStream,@Multipart(value = "file" , required = false) Attachment fileDetail) {

        return delegate.importApplicationForUpdate(fileInputStream, fileDetail );
    }

    @Valid
    @PATCH
    @Path("/{applicationId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Partially update application by ID. ", notes = "This API provides the capability to partially update an application by ID. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchApplication(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the application details to be updated." ,required=true) @Valid ApplicationPatchModel applicationPatchModel) {

        return delegate.patchApplication(applicationId,  applicationPatchModel );
    }

    @Valid
    @PATCH
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Perform incremental sharing operations on an already shared application.", notes = "", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Operation processed successfully", response = ProcessSuccessResponse.class)
    })
    public Response patchApplicationSharing(@ApiParam(value = "" ,required=true) @Valid ApplicationSharingPatchRequest applicationSharingPatchRequest) {

        return delegate.patchApplicationSharing(applicationSharingPatchRequest );
    }

    @Valid
    @PATCH
    @Path("/{applicationId}/authorized-apis/{apiId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update authorized API scopes. ", notes = "This API provides the capability to update an authorized API of the application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Authorized APIs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchAuthorizedAPI(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "ID of the API resource.",required=true) @PathParam("apiId") String apiId, @ApiParam(value = "" ) @Valid AuthorizedAPIPatchModel authorizedAPIPatchModel) {

        return delegate.patchAuthorizedAPI(applicationId,  apiId,  authorizedAPIPatchModel );
    }

    @Valid
    @POST
    @Path("/{applicationId}/inbound-protocols/oidc/regenerate-secret")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Regenerate the OAuth2/OIDC client secret. ", notes = "This API regenerates the OAuth2/OIDC client secret. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_create` ", response = OpenIDConnectConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OpenIDConnectConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response regenerateOAuthClientSecret(@ApiParam(value = "ID of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.regenerateOAuthClientSecret(applicationId );
    }

    @Valid
    @POST
    @Path("/{applicationId}/inbound-protocols/oidc/revoke")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Revoke the OAuth2/OIDC client of application. ", notes = "This API revokes the OAuth2/OIDC client secret. To re-activate the client, the client secret needs to be regenerated. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_create` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Application Revoked", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response revokeOAuthClient(@ApiParam(value = "ID of the application",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.revokeOAuthClient(applicationId );
    }

    @Valid
    @POST
    @Path("/share-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share a single application with all organizations", notes = "", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Sharing process triggered successfully", response = ProcessSuccessResponse.class)
    })
    public Response shareApplicationWithAll(@ApiParam(value = "" ,required=true) @Valid ApplicationShareAllRequestBody applicationShareAllRequestBody) {

        return delegate.shareApplicationWithAll(applicationShareAllRequestBody );
    }

    @Valid
    @POST
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share a single application with specific organizations", notes = "", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Sharing process triggered successfully", response = ProcessSuccessResponse.class)
    })
    public Response shareApplicationWithSelected(@ApiParam(value = "" ,required=true) @Valid ApplicationShareSelectedRequestBody applicationShareSelectedRequestBody) {

        return delegate.shareApplicationWithSelected(applicationShareSelectedRequestBody );
    }

    @Valid
    @POST
    @Path("/{applicationId}/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share the application from the root organization to the given organization(s). ", notes = "This API provides the capability to share an application with organizations. <br>   <b>Scope(Permission) required:</b> `internal_shared_application_create` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response shareOrgApplication(@ApiParam(value = "ID of the application which will be shared to organizations.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "" ,required=true) @Valid ApplicationSharePOSTRequest applicationSharePOSTRequest) {

        return delegate.shareOrgApplication(applicationId,  applicationSharePOSTRequest );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/share/{shared-organization-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Stop sharing an application to a organization. ", notes = "This API provides the capability to stop sharing an application to an organization by providing its ID. <br>   <b>Scope(Permission) required:</b> `internal_shared_application_delete` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response shareOrgApplicationDelete(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "ID of the organization to stop sharing.",required=true) @PathParam("shared-organization-id") String sharedOrganizationId) {

        return delegate.shareOrgApplicationDelete(applicationId,  sharedOrganizationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/share")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List of organizations that the application is shared to. ", notes = "This API returns the list of organizations that the application is shared to. <br>   <b>Scope(Permission) required:</b> `internal_shared_application_view` ", response = SharedOrganizationsResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = SharedOrganizationsResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response shareOrgApplicationGet(@ApiParam(value = "ID of the application which is shared to organizations.",required=true) @PathParam("applicationId") String applicationId,     @Valid@ApiParam(value = "Base64 encoded cursor value for backward pagination. ")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Base64 encoded cursor value for forward pagination. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Determines whether a recursive search should happen. If set to true, will include shared organizations in all levels of the hierarchy; If set to false, includes only shared organizations in the next level of the hierarchy. ")  @QueryParam("recursive") Boolean recursive,     @Valid@ApiParam(value = "excludedAttribute parameter. ")  @QueryParam("excludedAttributes") String excludedAttributes,     @Valid@ApiParam(value = "Specifies the required parameters in the response. Only 'sharingMode' attribute is currently supported.  /applications/{applicationId}/share?attributes=sharingMode ")  @QueryParam("attributes") String attributes) {

        return delegate.shareOrgApplicationGet(applicationId,  before,  after,  filter,  limit,  recursive,  excludedAttributes,  attributes );
    }

    @Valid
    @DELETE
    @Path("/{applicationId}/shared-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Stop sharing an application with all organizations. ", notes = "This API provides the capability to stop sharing an application to all organizations the application is shared to. <br>   <b>Scope(Permission) required:</b> `internal_shared_application_delete` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response sharedApplicationsAllDelete(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.sharedApplicationsAllDelete(applicationId );
    }

    @Valid
    @GET
    @Path("/{applicationId}/shared-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List of shared applications along with its organization. ", notes = "This API returns the list of shared app ids along with the shared organization id. <br>   <b>Scope(Permission) required:</b> `internal_shared_application_view` ", response = SharedApplicationsResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = SharedApplicationsResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response sharedApplicationsGet(@ApiParam(value = "ID of the application which is shared to organizations.",required=true) @PathParam("applicationId") String applicationId) {

        return delegate.sharedApplicationsGet(applicationId );
    }

    @Valid
    @POST
    @Path("/unshare-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare an application from all organizations", notes = "", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Unsharing process triggered successfully", response = ProcessSuccessResponse.class)
    })
    public Response unshareApplicationFromAll(@ApiParam(value = "" ,required=true) @Valid ApplicationUnshareAllRequestBody applicationUnshareAllRequestBody) {

        return delegate.unshareApplicationFromAll(applicationUnshareAllRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare an application from specific orgs", notes = "", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Unsharing process triggered successfully", response = ProcessSuccessResponse.class)
    })
    public Response unshareApplicationFromSelected(@ApiParam(value = "" ,required=true) @Valid ApplicationUnshareSelectedRequestBody applicationUnshareSelectedRequestBody) {

        return delegate.unshareApplicationFromSelected(applicationUnshareSelectedRequestBody );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/authenticationSequence/script")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update authentication script of the application", notes = "This API provides the capability to update the authentication script of the application. <br>   <b>Scope(Permission) required:</b> `internal_application_script_update` </br> ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateAuthenticationScript(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the authentication script to be updated." ,required=true) @Valid ScriptUpdateModel scriptUpdateModel) {

        return delegate.updateAuthenticationScript(applicationId,  scriptUpdateModel );
    }

    @Valid
    @PUT
    @Path("/templates/{template-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the application template by the template ID. ", notes = "This API provides the capability to update an application template by the template ID. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateApplicationTemplate(@ApiParam(value = "Application template ID. This should be a valid locale. ",required=true) @PathParam("template-id") String templateId, @ApiParam(value = "This represents the new application template." ,required=true) @Valid ApplicationTemplateModel applicationTemplateModel) {

        return delegate.updateApplicationTemplate(templateId,  applicationTemplateModel );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/inbound-protocols/{inboundProtocolId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the custom inbound authentication protocol parameters. ", notes = "This API provides the capability to store custom inbound authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Custom", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateCustomInboundConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "Inbound Authentication Protocol ID",required=true) @PathParam("inboundProtocolId") String inboundProtocolId, @ApiParam(value = "This represents the Custom Inbound authentication protocol parameters of an application." ,required=true) @Valid CustomInboundProtocolConfiguration customInboundProtocolConfiguration) {

        return delegate.updateCustomInboundConfiguration(applicationId,  inboundProtocolId,  customInboundProtocolConfiguration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/inbound-protocols/oidc")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update OIDC authentication protocol parameters. ", notes = "This API provides the capability to store OIDC authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - OAuth / OIDC", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Created", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateInboundOAuthConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the OIDC authentication protocol parameters of an application." ,required=true) @Valid OpenIDConnectConfiguration openIDConnectConfiguration) {

        return delegate.updateInboundOAuthConfiguration(applicationId,  openIDConnectConfiguration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/inbound-protocols/saml")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update SAML2 authentication protocol parameters. ", notes = "This API provides the capability to store SAML2 authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update`  - There are three methods to create/update SAML2 authentication protocol configuration.     1. Metadata File (by sending the Base64 encoded content of the metadata file.)     2. Metadata URL     3. Manual configuration ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - SAML", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateInboundSAMLConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the SAML2 protocol attributes of the application." ,required=true) @Valid SAML2Configuration saML2Configuration) {

        return delegate.updateInboundSAMLConfiguration(applicationId,  saML2Configuration );
    }

    @Valid
    @PUT
    @Path("/{applicationId}/inbound-protocols/passive-sts")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Passive STS authentication protocol parameters. ", notes = "This API provides the capability to store passive STS authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - Passive STS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updatePassiveStsConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the Passive STS authentication protocol parameters of an application." ,required=true) @Valid PassiveStsConfiguration passiveStsConfiguration) {

        return delegate.updatePassiveStsConfiguration(applicationId,  passiveStsConfiguration );
    }

    @Valid
    @PUT
    @Path("/resident")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update resident application. ", notes = "This API provides the capability to update the resident application configuration. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Resident Application", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
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
    @ApiOperation(value = "Update WS Trust authentication protocol parameters. ", notes = "This API provides the capability to store WS Trust authentication protocol parameters of an application. <br>   <b>Scope(Permission) required:</b> `internal_application_mgt_update` ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Protocols - WS Trust" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful", response = Void.class),
        @ApiResponse(code = 201, message = "Successful response.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateWSTrustConfiguration(@ApiParam(value = "ID of the application.",required=true) @PathParam("applicationId") String applicationId, @ApiParam(value = "This represents the Passive STS authentication protocol parameters of an application." ,required=true) @Valid WSTrustConfiguration wsTrustConfiguration) {

        return delegate.updateWSTrustConfiguration(applicationId,  wsTrustConfiguration );
    }

}
