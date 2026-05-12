/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.consent.management.v2;

import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementCreateRequest;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ElementListResponse;
import org.wso2.carbon.identity.api.server.consent.management.v2.model.ErrorDTO;
import org.wso2.carbon.identity.api.server.consent.management.v2.factories.ElementsApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/elements")
@Api(description = "The elements API")

public class ElementsApi  {

    private final ElementsApiService delegate;

    public ElementsApi() {

        this.delegate = ElementsApiServiceFactory.getElementsApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a consent element", notes = "Create a new consent element (e.g., \"email_address\", \"phone_number\"). ", response = ElementDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Elements", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Element created successfully", response = ElementDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 409, message = "Conflict", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response elementsCreate(@ApiParam(value = "" ,required=true) @Valid ElementCreateRequest elementCreateRequest) {

        return delegate.elementsCreate(elementCreateRequest );
    }

    @Valid
    @DELETE
    @Path("/{elementId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a consent element", notes = "Delete a consent element by ID. ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Elements", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Element deleted successfully", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 403, message = "Forbidden", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 409, message = "Conflict", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response elementsDelete(@ApiParam(value = "ID of the consent element.",required=true) @PathParam("elementId") String elementId) {

        return delegate.elementsDelete(elementId );
    }

    @Valid
    @GET
    @Path("/{elementId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a consent element", notes = "Retrieve a specific consent element by ID. ", response = ElementDTO.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Elements", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Element details", response = ElementDTO.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response elementsGet(@ApiParam(value = "ID of the consent element.",required=true) @PathParam("elementId") String elementId) {

        return delegate.elementsGet(elementId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List consent elements", notes = "Retrieve all consent elements with optional filtering. ", response = ElementListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Elements" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "List of elements", response = ElementListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorDTO.class)
    })
    public Response elementsList(    @Valid@ApiParam(value = "Filter elements by supported attributes: name. Supports 'sw' (starts with), 'co' (contains), 'ew' (ends with), and 'eq' (equals) operations. Combine multiple conditions with 'and', 'or' logical operators. Examples: - filter=name eq email_address - filter=name co email - filter=name sw phone ")  @QueryParam("filter") String filter,     @Valid @Min(1)@ApiParam(value = "Maximum number of records to return.", defaultValue="10") @DefaultValue("10")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Cursor for forward pagination. Pass the base64-encoded UUID of the last item received from the previous page to retrieve the next page of results. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for backward pagination. Pass the base64-encoded UUID of the first item received from the current page to retrieve the previous page of results. ")  @QueryParam("before") String before) {

        return delegate.elementsList(filter,  limit,  after,  before );
    }

}
