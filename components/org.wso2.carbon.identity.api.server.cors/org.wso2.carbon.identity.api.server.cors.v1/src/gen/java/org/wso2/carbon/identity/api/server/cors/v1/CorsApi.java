/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.cors.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.cors.v1.model.CORSApplicationObject;
import org.wso2.carbon.identity.api.server.cors.v1.model.CORSOriginObject;
import org.wso2.carbon.identity.api.server.cors.v1.model.Error;
import org.wso2.carbon.identity.api.server.cors.v1.CorsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/cors")
@Api(description = "The cors API")

public class CorsApi  {

    @Autowired
    private CorsApiService delegate;

    @Valid
    @GET
    @Path("/origins/{cors-origin-id}/applications")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the Applications associated with a CORS origin by ID.", notes = "Retrieve the Applications associated with a CORS origin by ID.", response = CORSApplicationObject.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application associations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = CORSApplicationObject.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getAssociatedAppsByCORSOrigin(@ApiParam(value = "ID of a CORS origin",required=true) @PathParam("cors-origin-id") String corsOriginId) {

        return delegate.getAssociatedAppsByCORSOrigin(corsOriginId );
    }

    @Valid
    @GET
    @Path("/origins")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieve the CORS origins.", notes = "Retrieve the CORS origins.", response = CORSOriginObject.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "CORS origins" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = CORSOriginObject.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCORSOrigins() {

        return delegate.getCORSOrigins();
    }

}
