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

package org.wso2.carbon.identity.api.server.organization.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.organization.management.v1.factories.OrganizationsApiServiceFactory;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.GetOrganizationResponse;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.MetaAttributesResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryAttributes;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryCheckPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryCheckPOSTResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationDiscoveryPostRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationMetadata;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationNameCheckPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationNameCheckPOSTResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPUTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPatchRequestItem;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationsDiscoveryResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.SharedApplicationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.SharedOrganizationsResponse;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/organizations")
@Api(description = "The organizations API")

public class OrganizationsApi  {

    private final OrganizationsApiService delegate;

    public OrganizationsApi() {

        this.delegate = OrganizationsApiServiceFactory.getOrganizationsApi();
    }

    @Valid
    @POST
    @Path("/check-discovery")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Check whether given discovery attribute exists among the organization hierarchy.", notes = "This API is used to verify whether a specific discovery attribute has already been associated with an organization within the hierarchy. It is available for use within any organization in the hierarchy.<br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/organizationmgt/view <br> <b>Scope required:</b> <br> * internal_organization_view", response = OrganizationDiscoveryCheckPOSTResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Discovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OrganizationDiscoveryCheckPOSTResponse.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationCheckDiscovery(@ApiParam(value = "" ,required=true) @Valid OrganizationDiscoveryCheckPOSTRequest organizationDiscoveryCheckPOSTRequest) {

        return delegate.organizationCheckDiscovery(organizationDiscoveryCheckPOSTRequest );
    }

    @Valid
    @GET
    @Path("/{organization-id}/discovery")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get discovery attributes of the organization.", notes = "This API facilitates the retrieval of discovery attributes for an organization. It currently provides the capability to retrieve these attributes only from the primary organization.<br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/organizationmgt/view <br> <b>Scope required:</b> <br> * internal_organization_view", response = OrganizationDiscoveryAttributes.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Discovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OrganizationDiscoveryAttributes.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response organizationDiscoveryGet(@ApiParam(value = "ID of the organization whose discovery attributes are to be fetched.",required=true) @PathParam("organization-id") String organizationId) {

        return delegate.organizationDiscoveryGet(organizationId );
    }

    @Valid
    @POST
    @Path("/discovery")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Map discovery attributes to the organization.", notes = "This API serves the purpose of adding discovery attributes to an organization, with the current restriction that only the primary organization has the capability to perform this action.<br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/organizationmgt/update <br> <b>Scope required:</b> <br> * internal_organization_update", response = OrganizationDiscoveryAttributes.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Discovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = OrganizationDiscoveryAttributes.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationDiscoveryPost(@ApiParam(value = "This represents the organization discovery attributes to be added." ,required=true) @Valid OrganizationDiscoveryPostRequest organizationDiscoveryPostRequest) {

        return delegate.organizationDiscoveryPost(organizationDiscoveryPostRequest );
    }

    @Valid
    @GET
    @Path("/metadata")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get metadata of the logged in organization.", notes = "This API facilitates the retrieval of metadata including discovery attributes of the logged in organization.<br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/organizationmgt/view <br> <b>Scope required:</b> <br> * internal_organization_view", response = OrganizationMetadata.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Metadata", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OrganizationMetadata.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationMetadataGet() {

        return delegate.organizationMetadataGet();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a new organization.", notes = "This API is used to create the organization defined in the user input.", response = OrganizationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = OrganizationResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationPost(@ApiParam(value = "This represents the organization to be added." ,required=true) @Valid OrganizationPOSTRequest organizationPOSTRequest) {

        return delegate.organizationPost(organizationPOSTRequest );
    }

    @Valid
    @POST
    @Path("/check-name")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Check organization with given name exist among the organizations hierarchy.", notes = "This API is used to check whether organization with particular name exist or not.", response = OrganizationNameCheckPOSTResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OrganizationNameCheckPOSTResponse.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationsCheckNamePost(@ApiParam(value = "OrganizationNameCheckPOSTRequest object containing the organization name." ,required=true) @Valid OrganizationNameCheckPOSTRequest organizationNameCheckPOSTRequest) {

        return delegate.organizationsCheckNamePost(organizationNameCheckPOSTRequest );
    }

    @Valid
    @GET
    @Path("/discovery")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get discovery attributes of organizations.", notes = "This API facilitates the retrieval of discovery attributes of organizations in the hierarchy, allowing filtering by discovery attribute type and value. It currently provides the capability to retrieve these attributes from only the primary organization.<br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/organizationmgt/view <br> <b>Scope required:</b> <br> * internal_organization_view", response = OrganizationsDiscoveryResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Discovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OrganizationsDiscoveryResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response organizationsDiscoveryGet(    @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination.")  @QueryParam("offset") Integer offset,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit) {

        return delegate.organizationsDiscoveryGet(filter,  offset,  limit );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve organizations created for this tenant which matches the defined search criteria, if any.", notes = "This API is used to search and retrieve organizations created for this tenant.  Organizations can be filtered by id, name, description, created, lastModified, status, parentId, and meta attributes.         Supported operators: \"eq\" (equals), \"co\" (contains), \"sw\" (starts with), \"ew\" (ends with), \"ge\" (greater than or equals), \"le\" (less than or equals), \"gt\" (greater than), \"lt\" (less than)  Multiple attributes can be combined using the \"and\" operator.  Examples:   - filter=name+eq+ABC Builders   - filter=attributes.Country+eq+Sri Lanka  <b>Scope(Permission) required:</b> `internal_organization_view` ", response = OrganizationsResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OrganizationsResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response organizationsGet(    @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Points to the next range of data to be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Points to the previous range of data that can be retrieved.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Determines whether a recursive search should happen. If set to true, will include organizations in all levels of the hierarchy; If set to false, includes only organizations in the next level of the hierarchy. ", defaultValue="false") @DefaultValue("false")  @QueryParam("recursive") Boolean recursive) {

        return delegate.organizationsGet(filter,  limit,  after,  before,  recursive );
    }

    @Valid
    @GET
    @Path("/meta-attributes")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get meta attributes of organizations with filter capabilities.", notes = "This API facilitates the retrieval of organization meta attributes which matches the defined search criteria, if any.  Supported operators: \"eq\"(equals), \"co\"(contains), \"sw\"(starts with), \"ew\"(ends with), \"ge\"(greater than or equals), \"le\"(less than or equals), \"gt\"(greater than), \"lt\"(less than)  Multiple filters can be combined using the \"and\" operator.  Example: filter=attributes+eq+Country  <b>Scope(Permission) required:</b> `internal_organization_view` ", response = MetaAttributesResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Meta Attributes", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = MetaAttributesResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response organizationsMetaAttributesGet(    @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Points to the next range of data to be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Points to the previous range of data that can be retrieved.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Determines whether a recursive search should happen. If set to true, will include organizations in all levels of the hierarchy; If set to false, includes only organizations in the next level of the hierarchy. ", defaultValue="false") @DefaultValue("false")  @QueryParam("recursive") Boolean recursive) {

        return delegate.organizationsMetaAttributesGet(filter,  limit,  after,  before,  recursive );
    }

    @Valid
    @DELETE
    @Path("/{organization-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete an organization by using the organization's ID.", notes = "This API provides the capability to delete an organization by giving its ID.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationsOrganizationIdDelete(@ApiParam(value = "ID of the organization to be deleted.",required=true) @PathParam("organization-id") String organizationId) {

        return delegate.organizationsOrganizationIdDelete(organizationId );
    }

    @Valid
    @DELETE
    @Path("/{organization-id}/discovery")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete discovery attributes of an organization.", notes = "This API serves the purpose of deleting discovery attributes of an organization, with the current restriction that only the primary organization has the capability to perform this action.<br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/organizationmgt/update <br> <b>Scope required:</b> <br> * internal_organization_update", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Discovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationsOrganizationIdDiscoveryDelete(@ApiParam(value = "ID of the organization whose discovery attributes are to be deleted.",required=true) @PathParam("organization-id") String organizationId) {

        return delegate.organizationsOrganizationIdDiscoveryDelete(organizationId );
    }

    @Valid
    @PUT
    @Path("/{organization-id}/discovery")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update discovery attributes of an organization.", notes = "This API serves the purpose of updating discovery attributes of an organization, with the current restriction that only the primary organization has the capability to perform this action.<br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/organizationmgt/update <br> <b>Scope required:</b> <br> * internal_organization_update", response = OrganizationDiscoveryAttributes.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Discovery", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OrganizationDiscoveryAttributes.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationsOrganizationIdDiscoveryPut(@ApiParam(value = "ID of the organization whose discovery attributes are to be updated.",required=true) @PathParam("organization-id") String organizationId, @ApiParam(value = "" ,required=true) @Valid OrganizationDiscoveryAttributes organizationDiscoveryAttributes) {

        return delegate.organizationsOrganizationIdDiscoveryPut(organizationId,  organizationDiscoveryAttributes );
    }

    @Valid
    @GET
    @Path("/{organization-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get an existing organization, identified by the organization ID.", notes = "This API is used to get an existing organization identified by the organization ID.", response = GetOrganizationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = GetOrganizationResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationsOrganizationIdGet(@ApiParam(value = "ID of the organization.",required=true) @PathParam("organization-id") String organizationId,     @Valid@ApiParam(value = "Returns the organization details along with permissions assigned for the requested user in this organization.", defaultValue="false") @DefaultValue("false")  @QueryParam("includePermissions") Boolean includePermissions) {

        return delegate.organizationsOrganizationIdGet(organizationId,  includePermissions );
    }

    @Valid
    @PATCH
    @Path("/{organization-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Patch an organization property by ID. Patch is supported only for key-value pairs.", notes = "This API provides the capability to update an organization property using patch request. Organization patch is supported only for key-value pairs.", response = OrganizationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OrganizationResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationsOrganizationIdPatch(@ApiParam(value = "ID of the organization to be patched.",required=true) @PathParam("organization-id") String organizationId, @ApiParam(value = "" ,required=true) @Valid List<OrganizationPatchRequestItem> organizationPatchRequestItem) {

        return delegate.organizationsOrganizationIdPatch(organizationId,  organizationPatchRequestItem );
    }

    @Valid
    @PUT
    @Path("/{organization-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update an organization by ID.", notes = "This API provides the capability to update an organization by its id.", response = OrganizationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OrganizationResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response organizationsOrganizationIdPut(@ApiParam(value = "ID of the organization to be updated.",required=true) @PathParam("organization-id") String organizationId, @ApiParam(value = "" ,required=true) @Valid OrganizationPUTRequest organizationPUTRequest) {

        return delegate.organizationsOrganizationIdPut(organizationId,  organizationPUTRequest );
    }

    @Valid
    @POST
    @Path("/{organization-id}/applications/{application-id}/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share application from the parent organization to given organization ", notes = "This API creates an internal application to delegate access from ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Application Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response shareOrgApplication(@ApiParam(value = "ID of the parent organization where the application is created.",required=true) @PathParam("organization-id") String organizationId, @ApiParam(value = "ID of the application which will be shared to child organizations.",required=true) @PathParam("application-id") String applicationId, @ApiParam(value = "ApplicationSharePOSTRequest object containing the sharing attributes." ,required=true) @Valid ApplicationSharePOSTRequest applicationSharePOSTRequest) {

        return delegate.shareOrgApplication(organizationId,  applicationId,  applicationSharePOSTRequest );
    }

    @Valid
    @DELETE
    @Path("/{organization-id}/applications/{application-id}/share/{shared-organization-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Stop sharing an application to a organization. ", notes = "This API provides the capability to stop sharing an application to an organization by providing its ID. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Application Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response shareOrgApplicationDelete(@ApiParam(value = "ID of the organization to be deleted.",required=true) @PathParam("organization-id") String organizationId, @ApiParam(value = "ID of the application.",required=true) @PathParam("application-id") String applicationId, @ApiParam(value = "ID of the organization to stop sharing.",required=true) @PathParam("shared-organization-id") String sharedOrganizationId) {

        return delegate.shareOrgApplicationDelete(organizationId,  applicationId,  sharedOrganizationId );
    }

    @Valid
    @DELETE
    @Path("/{organization-id}/applications/{application-id}/fragment-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Stop sharing an application with all sub-organizations. ", notes = "This API provides the capability to stop sharing an application to all organizations the application is shared to. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Application Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response shareOrgApplicationDeleteAll(@ApiParam(value = "ID of the parent organization where the application is created.",required=true) @PathParam("organization-id") String organizationId, @ApiParam(value = "ID of the application.",required=true) @PathParam("application-id") String applicationId) {

        return delegate.shareOrgApplicationDeleteAll(organizationId,  applicationId );
    }

    @Valid
    @GET
    @Path("/{organization-id}/applications/{application-id}/share")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List of organizations that the application is shared to. ", notes = "This API returns the list of organizations that the application is shared to. ", response = SharedOrganizationsResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Application Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = SharedOrganizationsResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response shareOrgApplicationGet(@ApiParam(value = "ID of the parent organization where the application is created.",required=true) @PathParam("organization-id") String organizationId, @ApiParam(value = "ID of the application which will be shared to child organizations.",required=true) @PathParam("application-id") String applicationId) {

        return delegate.shareOrgApplicationGet(organizationId,  applicationId );
    }

    @Valid
    @DELETE
    @Path("/{organization-id}/applications/{application-id}/shared-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Stop sharing an application with all sub-organizations. ", notes = "This API provides the capability to stop sharing an application to all organizations the application is shared to. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Application Management", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response sharedApplicationsAllDelete(@ApiParam(value = "ID of the parent organization where the application is created.",required=true) @PathParam("organization-id") String organizationId, @ApiParam(value = "ID of the application.",required=true) @PathParam("application-id") String applicationId) {

        return delegate.sharedApplicationsAllDelete(organizationId,  applicationId );
    }

    @Valid
    @GET
    @Path("/{organization-id}/applications/{application-id}/shared-apps")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List of shared applications along with its organization. ", notes = "This API returns the list of shared app ids along with the shared organization id. ", response = SharedApplicationsResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Organization Application Management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = SharedApplicationsResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response sharedApplicationsGet(@ApiParam(value = "ID of the parent organization where the application is created.",required=true) @PathParam("organization-id") String organizationId, @ApiParam(value = "ID of the application which is shared to child organizations.",required=true) @PathParam("application-id") String applicationId) {

        return delegate.sharedApplicationsGet(organizationId,  applicationId );
    }

}
