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
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.OperationRecordsInner;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperationRecordsInner;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.AsyncOperationStatusApiService;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.factories.AsyncOperationStatusApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

@Path("/async-operation-status")
@Api(description = "The async-operation-status API")

public class AsyncOperationStatusApi  {

    private final AsyncOperationStatusApiService delegate;

    public AsyncOperationStatusApi() {

        this.delegate = AsyncOperationStatusApiServiceFactory.getAsyncOperationStatusApi();
    }

    @Valid
    @GET
    @Path("/{correlationId}/operations")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Retrieve a list of operation statuses for a given correlation ID.", response = OperationRecordsInner.class, responseContainer = "List", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OperationRecordsInner.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response asyncOperationStatusCorrelationIdOperationsGet(@ApiParam(value = "The unique identifier of the operation.",required=true) @PathParam("correlationId") String correlationId,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item after which the next page of results should be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item before which the previous page of results should be returned.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "The maximum number of results to return per page.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "A filter to apply to the results, such as by unit operation status or other criteria.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusCorrelationIdOperationsGet(correlationId,  after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/operations/{operationId}/unit-operations")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Retrieve a list of unit operation statuses for a given operation ID.", response = UnitOperationRecordsInner.class, responseContainer = "List", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = UnitOperationRecordsInner.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response asyncOperationStatusOperationsOperationIdUnitOperationsGet(@ApiParam(value = "The unique identifier of the operation.",required=true) @PathParam("operationId") String operationId,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item after which the next page of results should be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item before which the previous page of results should be returned.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "The maximum number of results to return per page.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "A filter to apply to the results, such as by unit operation status or other criteria.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusOperationsOperationIdUnitOperationsGet(operationId,  after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/subject-types/{operationSubjectType}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Retrieve a list of asynchronous operations based on the subject type.", response = OperationRecordsInner.class, responseContainer = "List", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OperationRecordsInner.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response asyncOperationStatusSubjectTypesOperationSubjectTypeGet(@ApiParam(value = "The type of subject the operation pertains to (e.g., APPLICATION, USER).",required=true) @PathParam("operationSubjectType") String operationSubjectType,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item after which the next page of results should be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item before which the previous page of results should be returned.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "The maximum number of results to return per page.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "A filter to apply to the results, such as by organization name or other criteria.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusSubjectTypesOperationSubjectTypeGet(operationSubjectType,  after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/subject-types/{operationSubjectType}/subject/{operationSubjectId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Retrieve a list of asynchronous operations based on the subject type and subject ID.", response = OperationRecordsInner.class, responseContainer = "List", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OperationRecordsInner.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdGet(@ApiParam(value = "The type of subject the operation pertains to (e.g., APPLICATION, USER).",required=true) @PathParam("operationSubjectType") String operationSubjectType, @ApiParam(value = "The unique identifier of the subject.",required=true) @PathParam("operationSubjectId") String operationSubjectId,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item after which the next page of results should be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item before which the previous page of results should be returned.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "The maximum number of results to return per page.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "A filter to apply to the results, such as by organization name or other criteria.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdGet(operationSubjectType,  operationSubjectId,  after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/subject-types/{operationSubjectType}/subject/{operationSubjectId}/operation-type/{operationType}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Retrieve a list of asynchronous operations based on the subject type, subject ID, and operation type.", response = OperationRecordsInner.class, responseContainer = "List", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OperationRecordsInner.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdOperationTypeOperationTypeGet(@ApiParam(value = "The type of subject the operation pertains to (e.g., APPLICATION, USER).",required=true) @PathParam("operationSubjectType") String operationSubjectType, @ApiParam(value = "The unique identifier of the subject.",required=true) @PathParam("operationSubjectId") String operationSubjectId, @ApiParam(value = "The specific type of asynchronous operation (e.g., B2B_APPLICATION_SHARE).",required=true) @PathParam("operationType") String operationType,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item after which the next page of results should be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Cursor for pagination, pointing to the item before which the previous page of results should be returned.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "The maximum number of results to return per page.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "A filter to apply to the results, such as by operation status or other criteria.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdOperationTypeOperationTypeGet(operationSubjectType,  operationSubjectId,  operationType,  after,  before,  limit,  filter );
    }

}
