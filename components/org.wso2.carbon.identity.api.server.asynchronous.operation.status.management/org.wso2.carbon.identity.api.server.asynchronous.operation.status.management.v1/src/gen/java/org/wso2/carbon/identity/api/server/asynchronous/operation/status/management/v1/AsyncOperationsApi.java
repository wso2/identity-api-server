/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Operation;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Operations;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperation;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperations;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.AsyncOperationsApiService;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.factories.AsyncOperationsApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/async-operations")
@Api(description = "The async-operations API")

public class AsyncOperationsApi  {

    private final AsyncOperationsApiService delegate;

    public AsyncOperationsApi() {

        this.delegate = AsyncOperationsApiServiceFactory.getAsyncOperationsApi();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List all Asynchronous Operations", notes = "This API returns the list of asynchronous operations.  <b>Scope(Permission) required:</b> `internal_async_operation_status_view` ", response = Operations.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Asynchronous Operations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Operations.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response asyncOperationsGet(    @Valid@ApiParam(value = "Base64 encoded cursor value for forward pagination.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Base64 encoded cursor value for backward pagination.")  @QueryParam("before") String before,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Can be filtered by correlationId, operationType, subjectType, initiatedOrgId, and initiatedUserId.  Supports operators such as 'eq', 'sw', 'ge', 'le', 'gt', 'lt', and combines them using 'and'.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationsGet(after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/{operationId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Asynchronous Operation by the id", notes = "This API returns the list of unit operation statuses for a given unique ID.   <b>Scope (Permission) required:</b> ``internal_async_operation_status_view``  ", response = Operation.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Asynchronous Operations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Operation.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response asyncOperationsOperationIdGet(@ApiParam(value = "The unique identifier of the operation.",required=true) @PathParam("operationId") String operationId) {

        return delegate.asyncOperationsOperationIdGet(operationId );
    }

    @Valid
    @GET
    @Path("/{operationId}/unit-operations")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List all Asynchronous Unit Operations by id", notes = "This API returns the list of unit operation statuses for a given operation ID.  <b>Scope(Permission) required:</b> `internal_async_operation_status_view` ", response = UnitOperations.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Asynchronous Unit Operations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = UnitOperations.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response asyncOperationsOperationIdUnitOperationsGet(@ApiParam(value = "The unique identifier of the operation.",required=true) @PathParam("operationId") String operationId,     @Valid@ApiParam(value = "Base64 encoded cursor value for forward pagination.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Base64 encoded cursor value for backward pagination.")  @QueryParam("before") String before,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Can be filtered by residentResourceId, targetOrgId, and status.  Supports operators such as 'eq', 'sw', 'ge', 'le', 'gt', 'lt', and combines them using 'and'.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationsOperationIdUnitOperationsGet(operationId,  after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/{operationId}/unit-operations/{unitOperationId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Asynchronous Unit Operation by unique ID", notes = "This API returns the unit operation status for a given unit operation ID.   <b>Scope (Permission) required:</b> ``internal_async_operation_status_view``  ", response = UnitOperation.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Asynchronous Unit Operations" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = UnitOperation.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response asyncOperationsOperationIdUnitOperationsUnitOperationIdGet(@ApiParam(value = "The unique identifier of the operation.",required=true) @PathParam("operationId") String operationId, @ApiParam(value = "The unique identifier of the unit operation.",required=true) @PathParam("unitOperationId") String unitOperationId) {

        return delegate.asyncOperationsOperationIdUnitOperationsUnitOperationIdGet(operationId,  unitOperationId );
    }

}
