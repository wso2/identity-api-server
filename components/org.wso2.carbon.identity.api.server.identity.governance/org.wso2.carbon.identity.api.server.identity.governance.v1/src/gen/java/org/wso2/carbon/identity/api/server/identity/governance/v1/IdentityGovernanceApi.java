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

package org.wso2.carbon.identity.api.server.identity.governance.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.*;
import org.wso2.carbon.identity.api.server.identity.governance.v1.IdentityGovernanceApiService;
import org.wso2.carbon.identity.api.server.identity.governance.v1.factories.IdentityGovernanceApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.CategoriesResDTO;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.ConnectorResDTO;
import org.wso2.carbon.identity.api.server.identity.governance.v1.dto.ConnectorsPatchReqDTO;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/identity-governance")


@io.swagger.annotations.Api(value = "/identity-governance", description = "the identity-governance API")
public class IdentityGovernanceApi  {

   @Autowired
   private IdentityGovernanceApiService delegate;

    @GET
    @Path("/")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve governance connector categories.", notes = "Retrieve governance connector categories.\n\n<b>Permission required:</b>\n  * /permission/admin/manage\n", response = CategoriesResDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Governance connector categories."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error."),
        
        @io.swagger.annotations.ApiResponse(code = 501, message = "Not Implemented.") })

    public Response getCategories(@ApiParam(value = "maximum number of records to return") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "number of records to skip for pagination") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Condition to filter the retrival of records.") @QueryParam("filter")  String filter,
    @ApiParam(value = "Define the order how the retrieved records should be sorted.") @QueryParam("sort")  String sort)
    {
    return delegate.getCategories(limit,offset,filter,sort);
    }
    @GET
    @Path("/{category-id}/{connector-id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve governance connector.", notes = "Retrieve governance connector.\n\n<b>Permission required:</b>\n  * /permission/admin/manage\n", response = ConnectorResDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Requested governance connector."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response getConnector(@ApiParam(value = "Id of the connector category.",required=true ) @PathParam("category-id")  String categoryId,
    @ApiParam(value = "Id of the connector.",required=true ) @PathParam("connector-id")  String connectorId)
    {
    return delegate.getConnector(categoryId,connectorId);
    }
    @GET
    @Path("/{category-id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve governance connectors of a category.", notes = "Retrieve governance connectors of a category.\n\n<b>Permission required:</b>\n  * /permission/admin/manage\n", response = ConnectorResDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Requested governance connector category."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response getConnectorCategory(@ApiParam(value = "Id of the connector category.",required=true ) @PathParam("category-id")  String categoryId)
    {
    return delegate.getConnectorCategory(categoryId);
    }
    @PATCH
    @Path("/{category-id}/{connector-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Patch governance connector.", notes = "Patch governance connector.\n\n<b>Permission required:</b>\n  * /permission/admin/manage\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized."),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found."),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error.") })

    public Response patchConnector(@ApiParam(value = "Id of the connector category.",required=true ) @PathParam("category-id")  String categoryId,
    @ApiParam(value = "Id of the connector.",required=true ) @PathParam("connector-id")  String connectorId,
    @ApiParam(value = "governance-connector to update"  ) ConnectorsPatchReqDTO governanceConnector)
    {
    return delegate.patchConnector(categoryId,connectorId,governanceConnector);
    }
}

