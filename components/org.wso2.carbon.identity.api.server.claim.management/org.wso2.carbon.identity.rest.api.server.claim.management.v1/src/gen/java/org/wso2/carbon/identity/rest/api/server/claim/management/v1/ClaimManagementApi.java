/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.server.claim.management.v1;

import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.*;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.ClaimManagementApiService;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.factories.ClaimManagementApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimReqDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ClaimDialectResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.ExternalClaimResDTO;
import org.wso2.carbon.identity.rest.api.server.claim.management.v1.dto.LocalClaimResDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/claim-dialects")
@io.swagger.annotations.Api(value = "/claim-dialects", description = "the claim-dialects API")
public class ClaimManagementApi  {

    private final ClaimManagementApiService delegate;

    public ClaimManagementApi() {

        this.delegate = ClaimManagementApiServiceFactory.getClaimManagementApi();
    }

    @Valid
    @POST
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Add a claim dialect.",
            notes = "Add a new claim dialect. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/create <br> <b>Scope required:</b> <br> * internal_claim_meta_create",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response addClaimDialect(@ApiParam(value = "claim dialect to add."  ) @Valid ClaimDialectReqDTO claimDialect) {

        return delegate.addClaimDialect(claimDialect);
    }

    @Valid
    @POST
    @Path("/{dialect-id}/claims")
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Add an external claim.",
            notes = "Add a new external claim. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/create <br> <b>Scope required:</b> <br> * internal_claim_meta_create",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response addExternalClaim(@ApiParam(value = "Id of the claim dialect.",required=true ) @PathParam("dialect-id")  String dialectId,
    @ApiParam(value = "External claim to add."  ) @Valid ExternalClaimReqDTO externalClaim) {

        return delegate.addExternalClaim(dialectId,externalClaim);
    }

    @Valid
    @POST
    @Path("/local/claims")
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Add a local claim.",
            notes = "Add a new claim. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/create <br> <b>Scope required:</b> <br> * internal_claim_meta_create",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response addLocalClaim(@ApiParam(value = "Local claim to be added."  ) @Valid LocalClaimReqDTO localClaim) {

        return delegate.addLocalClaim(localClaim);
    }

    @Valid
    @DELETE
    @Path("/{dialect-id}")
    @io.swagger.annotations.ApiOperation(value = "Delete a claim dialect.",
            notes = "Delete a claim dialect by claim ID. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/delete <br> <b>Scope required:</b> <br> * internal_claim_meta_delete",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response deleteClaimDialect(@ApiParam(value = "Id of the claim dialect.",required=true ) @PathParam("dialect-id")  String dialectId) {

        return delegate.deleteClaimDialect(dialectId);
    }

    @Valid
    @DELETE
    @Path("/{dialect-id}/claims/{claim-id}")
    @io.swagger.annotations.ApiOperation(value = "Delete an external claim.",
            notes = "Delete a claim by dialect ID and claim ID. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/delete <br> <b>Scope required:</b> <br> * internal_claim_meta_delete",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response deleteExternalClaim(@ApiParam(value = "Id of the claim dialect.",required=true ) @PathParam("dialect-id")  String dialectId,
    @ApiParam(value = "Id of the claim.",required=true ) @PathParam("claim-id")  String claimId) {

        return delegate.deleteExternalClaim(dialectId,claimId);
    }

    @Valid
    @DELETE
    @Path("/local/claims/{claim-id}")
    @io.swagger.annotations.ApiOperation(value = "Delete a local claim.",
            notes = "Delete a claim by claim ID. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/delete <br> <b>Scope required:</b> <br> * internal_claim_meta_delete",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 204, message = "No Content."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response deleteLocalClaim(@ApiParam(value = "Id of the claim.",required=true ) @PathParam("claim-id")  String claimId) {

        return delegate.deleteLocalClaim(claimId);
    }

    @Valid
    @GET
    @Path("/{dialect-id}/export")
    @Produces({ "application/json", "application/xml", "application/yaml" })
    @io.swagger.annotations.ApiOperation(value = "Export a claim dialect with related claims in XML, YAML, or JSON format",
            notes = "This API provides the capability to retrieve a claim dialect for a given dialect ID along with all related claims as a XML, YAML, or JSON file. <br><b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/view <br> <b>Scope required:</b> <br> * internal_claim_meta_view",
            response = String.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful response"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response exportClaimDialectToFile(@ApiParam(value = "Id of the claim dialect.",required=true ) @PathParam("dialect-id")  String dialectId,
    @ApiParam(value = "Content type of the file.\n"  , allowableValues="{values=[application/json, application/xml, application/yaml, application/x-yaml, text/yaml, text/xml, text/json]}", defaultValue="application/yaml")@HeaderParam("Accept") String accept) {

        return delegate.exportClaimDialectToFile(dialectId,accept);
    }

    @Valid
    @GET
    @Path("/{dialect-id}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve claim dialect.",
            notes = "Retrieve a claim dialect matching the given dialect id. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/view <br> <b>Scope required:</b> <br> * internal_claim_meta_view <br>",
            response = ClaimDialectResDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Requested claim dialect."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response getClaimDialect(@ApiParam(value = "Id of the claim dialect.",required=true ) @PathParam("dialect-id")  String dialectId) {

        return delegate.getClaimDialect(dialectId);
    }

    @Valid
    @GET
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve claim dialects.",
            notes = "Retrieve claim dialects. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/view <br> <b>Scope required:</b> <br> * internal_claim_meta_view",
            response = ClaimDialectResDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Claim dialects."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error."),
        
        @io.swagger.annotations.ApiResponse(code = 501, message = "Not Implemented.") })

    public Response getClaimDialects(@ApiParam(value = "Maximum number of records to return.") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination.") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Condition to filter the retrieval of records.") @QueryParam("filter")  String filter,
    @ApiParam(value = "Define the order by which the retrieved records should be sorted.") @QueryParam("sort")  String sort) {

        return delegate.getClaimDialects(limit,offset,filter,sort);
    }

    @Valid
    @GET
    @Path("/{dialect-id}/claims/{claim-id}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve external claim.",
            notes = "Retrieve an external claim matching the given dialect ID and claim ID. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/view <br> <b>Scope required:</b> <br> * internal_claim_meta_view",
            response = ExternalClaimResDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Requested claim."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response getExternalClaim(@ApiParam(value = "Id of the claim dialect.",required=true ) @PathParam("dialect-id")  String dialectId,
    @ApiParam(value = "Id of the claim.",required=true ) @PathParam("claim-id")  String claimId) {

        return delegate.getExternalClaim(dialectId,claimId);
    }

    @Valid
    @GET
    @Path("/{dialect-id}/claims")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve external claims.",
            notes = "Retrieve External claims. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/view <br> <b>Scope required:</b> <br> * internal_claim_meta_view",
            response = ExternalClaimResDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "External claims."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error."),
        
        @io.swagger.annotations.ApiResponse(code = 501, message = "Not Implemented.") })

    public Response getExternalClaims(@ApiParam(value = "Id of the claim dialect.",required=true ) @PathParam("dialect-id")  String dialectId,
    @ApiParam(value = "Maximum number of records to return.") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination.") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Condition to filter the retrieval of records.") @QueryParam("filter")  String filter,
    @ApiParam(value = "Define the order by which the retrieved records should be sorted.") @QueryParam("sort")  String sort) {

        return delegate.getExternalClaims(dialectId,limit,offset,filter,sort);
    }

    @Valid
    @GET
    @Path("/local/claims/{claim-id}")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve local claim.",
            notes = "Retrieve a local claim matching the given claim ID. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/view <br> <b>Scope required:</b> <br> * internal_claim_meta_view",
            response = LocalClaimResDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Requested claim."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response getLocalClaim(@ApiParam(value = "Id of the claim.",required=true ) @PathParam("claim-id")  String claimId) {

        return delegate.getLocalClaim(claimId);
    }

    @Valid
    @GET
    @Path("/local/claims")
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve local claims.",
            notes = "Retrieve local claims. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/view <br> <b>Scope required:</b> <br> * internal_claim_meta_view",
            response = LocalClaimResDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Local claims."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error."),
        
        @io.swagger.annotations.ApiResponse(code = 501, message = "Not Implemented.") })

    public Response getLocalClaims(@ApiParam(value = "Define only the required attributes to be sent in the response object.") @QueryParam("attributes")  String attributes,
    @ApiParam(value = "Maximum number of records to return.") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination.") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Condition to filter the retrieval of records.") @QueryParam("filter")  String filter,
    @ApiParam(value = "Define the order by which the retrieved records should be sorted.") @QueryParam("sort")  String sort,
    @ApiParam(value = "Exclude identity claims when listing local claims.") @QueryParam("exclude-identity-claims")  Boolean excludeIdentityClaims,
    @ApiParam(value = "Exclude hidden claims when listing local claims.") @QueryParam("exclude-hidden-claims") Boolean excludeHiddenClaims,
    @ApiParam(value = "The claims belongs to given profile") @QueryParam("profile") String profile) {

        return delegate.getLocalClaims(attributes,limit,offset,filter,sort,excludeIdentityClaims,excludeHiddenClaims,profile);
    }

    @Valid
    @POST
    @Path("/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Import an external claim dialect with related claims from XML, YAML, or JSON file",
            notes = "This API provides the capability to import claim dialect with related claims from a file in XML, YAML, or JSON format. <br><b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/create <br> <b>Scope required:</b> <br> * internal_claim_meta_create",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response importClaimDialectFromFile(@ApiParam(value = "The file to be uploaded.") @Multipart(value = "file") InputStream fileInputStream,
    @ApiParam(value = "The file to be uploaded. : details") @Multipart(value = "file" ) Attachment fileDetail) {

        return delegate.importClaimDialectFromFile(fileInputStream,fileDetail);
    }

    @Valid
    @PUT
    @Path("/{dialect-id}")
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update a claim dialect.",
            notes = "Update a claim dialect. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/update <br> <b>Scope required:</b> <br> * internal_claim_meta_update",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response updateClaimDialect(@ApiParam(value = "Id of the claim dialect.",required=true ) @PathParam("dialect-id")  String dialectId,
    @ApiParam(value = "Updated claim dialect."  ) @Valid ClaimDialectReqDTO claimDialect) {

        return delegate.updateClaimDialect(dialectId,claimDialect);
    }

    @Valid
    @PUT
    @Path("/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update claim dialect with related claims from XML, YAML, or JSON file",
            notes = "This API provides the capability to update a claim dialect and all related claims from a file in XML, YAML, or JSON format. <br><b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/create <br> <b>Scope required:</b> <br> * internal_claim_meta_create",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response updateClaimDialectFromFile(@ApiParam(value = "The file to be uploaded.") @Multipart(value = "file") InputStream fileInputStream,
    @ApiParam(value = "The file to be uploaded. : details") @Multipart(value = "file" ) Attachment fileDetail,
    @ApiParam(value = "Specify whether to merge and preserve the claims or completely replace the existing claims set.", defaultValue="false") @QueryParam("preserveClaims")  Boolean preserveClaims) {

        return delegate.updateClaimDialectFromFile(fileInputStream,fileDetail,preserveClaims);
    }

    @Valid
    @PUT
    @Path("/{dialect-id}/claims/{claim-id}")
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update an external claim.",
            notes = "Update an external claim. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/update <br> <b>Scope required:</b> <br> * internal_claim_meta_update",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response updateExternalClaim(@ApiParam(value = "Id of the claim dialect.",required=true ) @PathParam("dialect-id")  String dialectId,
    @ApiParam(value = "Id of the claim.",required=true ) @PathParam("claim-id")  String claimId,
    @ApiParam(value = "Updated external claim."  ) @Valid ExternalClaimReqDTO externalClaim) {

        return delegate.updateExternalClaim(dialectId,claimId,externalClaim);
    }

    @Valid
    @PUT
    @Path("/local/claims/{claim-id}")
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update a local claim.",
            notes = "Update a local claim. <br> <b>Permission required:</b> <br> * /permission/admin/manage/identity/claimmgt/metadata/update <br> <b>Scope required:</b> <br> * internal_claim_meta_update",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Resource Forbidden."),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Element Already Exists."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response updateLocalClaim(@ApiParam(value = "Id of the claim.",required=true ) @PathParam("claim-id")  String claimId,
    @ApiParam(value = "Updated local claim."  ) @Valid LocalClaimReqDTO localClaim) {

        return delegate.updateLocalClaim(claimId,localClaim);
    }

}
