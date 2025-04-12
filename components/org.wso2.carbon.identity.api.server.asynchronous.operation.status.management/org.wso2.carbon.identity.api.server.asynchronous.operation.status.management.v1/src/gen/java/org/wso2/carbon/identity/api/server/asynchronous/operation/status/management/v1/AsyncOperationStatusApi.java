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

import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.OperationRecordsResponse;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.model.UnitOperationRecordsResponse;
import org.wso2.carbon.identity.api.server.asynchronous.operation.status.management.v1.factories.AsyncOperationStatusApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

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
    @ApiOperation(value = "", notes = "Retrieve a list of operation statuses for a given correlation ID.", response = OperationRecordsResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OperationRecordsResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response asyncOperationStatusCorrelationIdOperationsGet(@ApiParam(value = "The unique identifier of the operation.",required=true) @PathParam("correlationId") String correlationId,     @Valid@ApiParam(value = "Points to the next range of data to be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Points to the previous range of data that can be retrieved.")  @QueryParam("before") String before,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusCorrelationIdOperationsGet(correlationId,  after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/operations/{operationId}/unit-operations")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Retrieve a list of unit operation statuses for a given operation ID.", response = UnitOperationRecordsResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = UnitOperationRecordsResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response asyncOperationStatusOperationsOperationIdUnitOperationsGet(@ApiParam(value = "The unique identifier of the operation.",required=true) @PathParam("operationId") String operationId,     @Valid@ApiParam(value = "Points to the next range of data to be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Points to the previous range of data that can be retrieved.")  @QueryParam("before") String before,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusOperationsOperationIdUnitOperationsGet(operationId,  after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/subject-types/{operationSubjectType}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Retrieve a list of asynchronous operations based on the subject type.", response = OperationRecordsResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response", response = OperationRecordsResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response asyncOperationStatusSubjectTypesOperationSubjectTypeGet(@ApiParam(value = "The type of subject the operation pertains to (e.g., APPLICATION, USER).",required=true) @PathParam("operationSubjectType") String operationSubjectType,     @Valid@ApiParam(value = "Points to the next range of data to be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Points to the previous range of data that can be retrieved.")  @QueryParam("before") String before,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusSubjectTypesOperationSubjectTypeGet(operationSubjectType,  after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/subject-types/{operationSubjectType}/subject/{operationSubjectId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Retrieve a list of asynchronous operations based on the subject type and subject ID.", response = OperationRecordsResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OperationRecordsResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdGet(@ApiParam(value = "The type of subject the operation pertains to (e.g., APPLICATION, USER).",required=true) @PathParam("operationSubjectType") String operationSubjectType, @ApiParam(value = "The unique identifier of the subject.",required=true) @PathParam("operationSubjectId") String operationSubjectId,     @Valid@ApiParam(value = "Points to the next range of data to be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Points to the previous range of data that can be retrieved.")  @QueryParam("before") String before,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdGet(operationSubjectType,  operationSubjectId,  after,  before,  limit,  filter );
    }

    @Valid
    @GET
    @Path("/subject-types/{operationSubjectType}/subject/{operationSubjectId}/operation-type/{operationType}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Retrieve a list of asynchronous operations based on the subject type, subject ID, and operation type.", response = OperationRecordsResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = OperationRecordsResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdOperationTypeOperationTypeGet(@ApiParam(value = "The type of subject the operation pertains to (e.g., APPLICATION, USER).",required=true) @PathParam("operationSubjectType") String operationSubjectType, @ApiParam(value = "The unique identifier of the subject.",required=true) @PathParam("operationSubjectId") String operationSubjectId, @ApiParam(value = "The specific type of asynchronous operation (e.g., B2B_APPLICATION_SHARE).",required=true) @PathParam("operationType") String operationType,     @Valid@ApiParam(value = "Points to the next range of data to be returned.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Points to the previous range of data that can be retrieved.")  @QueryParam("before") String before,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to be returned. (Should be greater than 0)")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Condition to filter the retrieval of records.")  @QueryParam("filter") String filter) {

        return delegate.asyncOperationStatusSubjectTypesOperationSubjectTypeSubjectOperationSubjectIdOperationTypeOperationTypeGet(operationSubjectType,  operationSubjectId,  operationType,  after,  before,  limit,  filter );
    }

}
