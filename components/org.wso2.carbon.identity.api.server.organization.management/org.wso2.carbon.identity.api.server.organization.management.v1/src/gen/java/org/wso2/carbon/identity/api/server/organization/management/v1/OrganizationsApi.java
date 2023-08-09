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

package org.wso2.carbon.identity.api.server.organization.management.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.organization.management.v1.model.ApplicationSharePOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.GetOrganizationResponse;
import java.util.List;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationNameCheckPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationNameCheckPOSTResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPOSTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPUTRequest;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationPatchRequestItem;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.OrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.SharedApplicationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.SharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.management.v1.OrganizationsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/organizations")
@Api(description = "The organizations API")

public class OrganizationsApi  {

    @Autowired
    private OrganizationsApiService delegate;

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
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve organizations created for this tenant which matches the defined search criteria, if any.", notes = "This API is used to search and retrieve organizations created for this tenant.", response = OrganizationsResponse.class, authorizations = {
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
    public Response organizationsGet(    @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Points to the next range of data to be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Points to the previous range of data that can be retrieved.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Determines whether a recursive search should happen.", defaultValue="false") @DefaultValue("false")  @QueryParam("recursive") Boolean recursive) {

        return delegate.organizationsGet(filter,  limit,  after,  before,  recursive );
    }

    @Valid
    @GET
    @Path("/me")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "This API is used to search and retrieve child organizations which are authorized for the user.", notes = "Retrieve authorized sub organizations which matches the defined search criteria, if any.", response = OrganizationsResponse.class, authorizations = {
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
    public Response organizationsGetMe(    @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Points to the next range of data to be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Points to the previous range of data that can be retrieved.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Determines whether a recursive search should happen.", defaultValue="false") @DefaultValue("false")  @QueryParam("recursive") Boolean recursive) {

        return delegate.organizationsGetMe(filter,  limit,  after,  before,  recursive );
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
