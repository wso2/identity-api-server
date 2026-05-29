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

package org.wso2.carbon.identity.api.server.moesif.publisher.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.moesif.publisher.v1.model.Error;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.model.MoesifPublisher;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.model.MoesifPublisherPatchReq;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.model.MoesifPublisherReq;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.MoesifPublishersApiService;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.factories.MoesifPublishersApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/moesif-publishers")
@Api(description = "The moesif-publishers API")

public class MoesifPublishersApi  {

    private final MoesifPublishersApiService delegate;

    public MoesifPublishersApi() {

        this.delegate = MoesifPublishersApiServiceFactory.getMoesifPublishersApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Create a Moesif event publisher", notes = "", response = MoesifPublisher.class, tags={ "Moesif Publishers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfully created", response = MoesifPublisher.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response createMoesifPublisher(@ApiParam(value = "" ,required=true) @Valid MoesifPublisherReq moesifPublisherReq) {

        return delegate.createMoesifPublisher(moesifPublisherReq );
    }

    @Valid
    @DELETE
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a Moesif event publisher", notes = "", response = Void.class, tags={ "Moesif Publishers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response deleteMoesifPublisher() {

        return delegate.deleteMoesifPublisher();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get Moesif event publisher", notes = "", response = MoesifPublisher.class, tags={ "Moesif Publishers", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful operation", response = MoesifPublisher.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getMoesifPublisher() {

        return delegate.getMoesifPublisher();
    }

    @Valid
    @PATCH

    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Partially update the Moesif event publisher configuration", notes = "", response = MoesifPublisher.class, tags={ "Moesif Publishers" })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated", response = MoesifPublisher.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response patchMoesifPublisher(@ApiParam(value = "" ,required=true) @Valid MoesifPublisherPatchReq moesifPublisherPatchReq) {

        return delegate.patchMoesifPublisher(moesifPublisherPatchReq );
    }

}
