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

package org.wso2.carbon.identity.api.server.configs.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.configs.v1.factories.ConfigsApiServiceFactory;
import org.wso2.carbon.identity.api.server.configs.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticatorListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.CORSPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.DCRConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.DCRPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.Error;
import org.wso2.carbon.identity.api.server.configs.v1.model.ImpersonationConfiguration;
import org.wso2.carbon.identity.api.server.configs.v1.model.ImpersonationPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthPassiveSTSConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.InboundAuthSAML2Config;
import org.wso2.carbon.identity.api.server.configs.v1.model.JWTKeyValidatorPatch;
import org.wso2.carbon.identity.api.server.configs.v1.model.JWTValidatorConfig;
import java.util.List;
import org.wso2.carbon.identity.api.server.configs.v1.model.Patch;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.RemoteLoggingConfigListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.Schema;
import org.wso2.carbon.identity.api.server.configs.v1.model.SchemaListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.ScimConfig;
import org.wso2.carbon.identity.api.server.configs.v1.model.ServerConfig;
import org.wso2.carbon.identity.api.server.configs.v1.ConfigsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/configs")
@Api(description = "The configs API")

public class ConfigsApi  {

    private final ConfigsApiService delegate;

    public ConfigsApi() {

        this.delegate = ConfigsApiServiceFactory.getConfigsApi();
    }

    @Valid
    @DELETE
    @Path("/authentication/inbound/passivests")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete WS Federation (Passive STS) inbound authentication configurations.", notes = "Delete all WS Federation (Passive STS) inbound authentication configurations of the tenant. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Authentication Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successful deletion", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deletePassiveSTSInboundAuthConfig() {

        return delegate.deletePassiveSTSInboundAuthConfig();
    }

    @Valid
    @DELETE
    @Path("/authentication/inbound/saml2")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete SAML2 inbound authentication configurations.", notes = "Delete all SAML2 inbound authentication configurations of the tenant. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Authentication Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successful deletion", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteSAMLInboundAuthConfig() {

        return delegate.deleteSAMLInboundAuthConfig();
    }

    @Valid
    @GET
    @Path("/authenticators/{authenticator-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get authenticator by ID", notes = "By passing in the appropriate authenticator ID, you can retrieve authenticator details ", response = Authenticator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Local Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Authenticator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAuthenticator(@ApiParam(value = "ID of an authenticator",required=true) @PathParam("authenticator-id") String authenticatorId) {

        return delegate.getAuthenticator(authenticatorId );
    }

    @Valid
    @GET
    @Path("/cors")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the tenant CORS configuration.", notes = "Retrieve the tenant CORS configuration.", response = CORSConfig.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "CORS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = CORSConfig.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCORSConfiguration() {

        return delegate.getCORSConfiguration();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Server Configs", notes = "Retrieve Server Configs ", response = ServerConfig.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = ServerConfig.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getConfigs() {

        return delegate.getConfigs();
    }

    @Valid
    @GET
    @Path("/home-realm-identifiers")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the Home Realm Identifiers.", notes = "Retrieve the Home Realm Identifiers.", response = String.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Home Realm Identifiers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = String.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getHomeRealmIdentifiers() {

        return delegate.getHomeRealmIdentifiers();
    }

    @Valid
    @GET
    @Path("/impersonation")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the tenant impersonation configuration.", notes = "Retrieve the tenant impersonation configuration.", response = ImpersonationConfiguration.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Impersonation Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = ImpersonationConfiguration.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getImpersonationConfiguration() {

        return delegate.getImpersonationConfiguration();
    }

    @Valid
    @GET
    @Path("/provisioning/inbound/scim")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Server Inbound SCIM configs", notes = "Retrieve Server Inbound SCIM Configs ", response = ScimConfig.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Inbound SCIM", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = ScimConfig.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getInboundScimConfigs() {

        return delegate.getInboundScimConfigs();
    }

    @Valid
    @GET
    @Path("/authentication/inbound/passivests")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve WS Federation (Passive STS) inbound authentication configurations.", notes = "Retrieve WS Federation (Passive STS) inbound authentication configurations. ", response = InboundAuthPassiveSTSConfig.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Authentication Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = InboundAuthPassiveSTSConfig.class),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getPassiveSTSInboundAuthConfig() {

        return delegate.getPassiveSTSInboundAuthConfig();
    }

    @Valid
    @GET
    @Path("/jwt-key-validator")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the tenant private key jwt authentication configuration.", notes = "Retrieve the tenant private key jwt authentication configuration.", response = JWTValidatorConfig.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Private Key JWY validation Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = JWTValidatorConfig.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getPrivatKeyJWTValidationConfiguration() {

        return delegate.getPrivatKeyJWTValidationConfiguration();
    }

    @Valid
    @GET
    @Path("/dcr")

    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the tenant dcr configuration.", notes = "Retrieve the tenant dcr configuration.", response = DCRConfig.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags={ "DCR Configurations", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = DCRConfig.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getDCRConfiguration() {

        return delegate.getDCRConfiguration();
    }

    @Valid
    @GET
    @Path("/remote-logging/{log-type}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Remote Logging Configuration", notes = "Retrieve Remote Logging Configurations ", response = RemoteLoggingConfig.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Remote Logging Configuration", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = RemoteLoggingConfig.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getRemoteLoggingConfig(@ApiParam(value = "Log Type",required=true) @PathParam("log-type") String logType) {

        return delegate.getRemoteLoggingConfig(logType );
    }

    @Valid
    @GET
    @Path("/remote-logging")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Remote Logging Configuration", notes = "Retrieve Remote Logging Configurations ", response = RemoteLoggingConfigListItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Remote Logging Configuration", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = RemoteLoggingConfigListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getRemoteLoggingConfigs() {

        return delegate.getRemoteLoggingConfigs();
    }

    @Valid
    @GET
    @Path("/authentication/inbound/saml2")

    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve SAML2 inbound authentication configurations.", notes = "Retrieve server SAML2 inbound authentication configurations. ", response = InboundAuthSAML2Config.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Authentication Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = InboundAuthSAML2Config.class),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSAMLInboundAuthConfig() {

        return delegate.getSAMLInboundAuthConfig();
    }

    @Valid
    @GET
    @Path("/schemas/{schema-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Schema by ID", notes = "By passing in the appropriate schema ID, you can retrieve attributes of a schema supported by the Server. ", response = Schema.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Schemas", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Schema.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSchema(@ApiParam(value = "Schema ID",required=true) @PathParam("schema-id") String schemaId) {

        return delegate.getSchema(schemaId );
    }

    @Valid
    @GET
    @Path("/schemas")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve Schemas supported By Server.", notes = "Retrieve Schemas supported by Server. ", response = SchemaListItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Schemas", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = SchemaListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getSchemas() {

        return delegate.getSchemas();
    }

    @Valid
    @GET
    @Path("/authenticators")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List local authenticators in the server", notes = "List available local authenticators of the server ", response = AuthenticatorListItem.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Local Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = AuthenticatorListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response listAuthenticators(    @Valid@ApiParam(value = "Type of authenticators. Can be either 'LOCAL' or 'REQUEST_PATH' ")  @QueryParam("type") String type) {

        return delegate.listAuthenticators(type );
    }

    @Valid
    @PATCH
    @Path("/cors")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch the tenant CORS configuration.", notes = "A JSONPatch as defined by RFC 6902.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "CORS", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchCORSConfiguration(@ApiParam(value = "" ,required=true) @Valid List<CORSPatch> coRSPatch) {

        return delegate.patchCORSConfiguration(coRSPatch );
    }

    @Valid
    @PATCH
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch Server Configs", notes = "Patch Server Configs. Patch operation is supported only for primary attributes (homeRealmIdentifier, idleSessionTimeoutPeriod and rememberMePeriod) ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Configs", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchConfigs(@ApiParam(value = "" ,required=true) @Valid List<Patch> patch) {

        return delegate.patchConfigs(patch );
    }

    @Valid
    @PATCH
    @Path("/impersonation")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch the tenant impersonation configuration.", notes = "Patch the tenant impersonation configuration.  A JSONPatch as defined by RFC 6902.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Impersonation Configurations", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful Response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchImpersonationConfiguration(@ApiParam(value = "" ,required=true) @Valid List<ImpersonationPatch> impersonationPatch) {

        return delegate.patchImpersonationConfiguration(impersonationPatch );
    }

    @Valid
    @PATCH
    @Path("/jwt-key-validator")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch the tenant private key jwt authentication configuration.", notes = "Patch the tenant private key jwt authentication configuration.  A JSONPatch as defined by RFC 6902.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Private Key JWY validation Authenticators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchPrivatKeyJWTValidationConfiguration(@ApiParam(value = "" ,required=true) @Valid List<JWTKeyValidatorPatch> jwTKeyValidatorPatch) {

        return delegate.patchPrivatKeyJWTValidationConfiguration(jwTKeyValidatorPatch );
    }

    @Valid
    @PATCH
    @Path("/dcr")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch the tenant dcr configuration.", notes = "Patch the tenant dcr authentication configuration.  A JSONPatch as defined by RFC 6902.", response = Void.class, authorizations = {
            @Authorization(value = "BasicAuth"),
            @Authorization(value = "OAuth2", scopes = {

            })
    }, tags={ "DCR Configurations", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = Void.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchDCRConfiguration(@ApiParam(value = "" ,required=true) @Valid List<DCRPatch> dcrPatch) {

        return delegate.patchDCRConfiguration(dcrPatch );
    }

    @Valid
    @DELETE
    @Path("/remote-logging/{log-type}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Restore Server Remote Logging Configuration to Default setting ", notes = "Restore Remote Logging Configuration to Default Configuration ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Remote Logging Configuration", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete successful", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response restoreServerRemoteLoggingConfiguration(@ApiParam(value = "Log Type",required=true) @PathParam("log-type") String logType) {

        return delegate.restoreServerRemoteLoggingConfiguration(logType );
    }

    @Valid
    @DELETE
    @Path("/remote-logging")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Restore Server Remote Logging Configuration to Default setting ", notes = "Restore Remote Logging Configuration to Default Configuration ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Remote Logging Configuration", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Delete successful", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response restoreServerRemoteLoggingConfigurations() {

        return delegate.restoreServerRemoteLoggingConfigurations();
    }

    @Valid
    @DELETE
    @Path("/impersonation")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Revert the tenant impersonation configuration.", notes = "Revert the tenant impersonation configuration. <b>Scope (Permission) required:</b> <br>   * internal_config_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Impersonation Configurations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successful deletion", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteImpersonationConfiguration() {

        return delegate.deleteImpersonationConfiguration();
    }

    @Valid
    @PUT
    @Path("/provisioning/inbound/scim")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Server Inbound SCIM configs", notes = "Update Server Inbound SCIM configs ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Inbound SCIM", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateInboundScimConfigs(@ApiParam(value = "" ,required=true) @Valid ScimConfig scimConfig) {

        return delegate.updateInboundScimConfigs(scimConfig );
    }

    @Valid
    @PATCH
    @Path("/authentication/inbound/passivests")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update WS Federation (Passive STS) inbound authentication configurations.", notes = "Patch WS Federation (Passive STS) inbound authentication configurations. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Authentication Configurations", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updatePassiveSTSInboundAuthConfig(@ApiParam(value = "" ) @Valid InboundAuthPassiveSTSConfig inboundAuthPassiveSTSConfig) {

        return delegate.updatePassiveSTSInboundAuthConfig(inboundAuthPassiveSTSConfig );
    }

    @Valid
    @PUT
    @Path("/remote-logging/{log-type}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Remote Logging Configuration", notes = "Update Remote Logging Configuration ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Remote Logging Configuration", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Accepted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateRemoteLoggingConfig(@ApiParam(value = "Log Type",required=true) @PathParam("log-type") String logType, @ApiParam(value = "" ,required=true) @Valid RemoteLoggingConfig remoteLoggingConfig) {

        return delegate.updateRemoteLoggingConfig(logType,  remoteLoggingConfig );
    }

    @Valid
    @PUT
    @Path("/remote-logging")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Remote Logging Configuration", notes = "Update Remote Logging Configuration ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Server Remote Logging Configuration" })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Accepted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateRemoteLoggingConfigs(@ApiParam(value = "" ,required=true) @Valid List<RemoteLoggingConfigListItem> remoteLoggingConfigListItem) {

        return delegate.updateRemoteLoggingConfigs(remoteLoggingConfigListItem );
    }

    @Valid
    @PATCH
    @Path("/authentication/inbound/saml2")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update SAML2 inbound authentication configurations.", notes = "Patch server SAML2 inbound authentication configurations. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Inbound Authentication Configurations" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateSAMLInboundAuthConfig(@ApiParam(value = "" ) @Valid InboundAuthSAML2Config inboundAuthSAML2Config) {

        return delegate.updateSAMLInboundAuthConfig(inboundAuthSAML2Config );
    }
}
