/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.extension.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.extension.management.v1.factories.ExtensionsApiServiceFactory;
import org.wso2.carbon.identity.api.server.extension.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.extension.management.v1.model.ExtensionListItem;
import org.wso2.carbon.identity.api.server.extension.management.v1.model.ExtensionResponseModel;
import java.util.Map;
import org.wso2.carbon.identity.api.server.extension.management.v1.ExtensionsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/extensions")
@Api(description = "The extensions API")

public class ExtensionsApi  {

    private final ExtensionsApiService delegate;

    public ExtensionsApi() {

        this.delegate = ExtensionsApiServiceFactory.getExtensionsApi();
    }

    @Valid
    @GET
    @Path("/{extensionType}/{extensionId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the extension info by type and id.", notes = "This API provides the capability to retrieve the extension that is registered in the system.<br>   <b>Permission required:</b> <br>       * TBD <br>   <b>Scope required:</b> <br>       * TBD ", response = ExtensionResponseModel.class, tags={ "Browse", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ExtensionResponseModel.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getExtensionInfoById(@ApiParam(value = "Type of the extension",required=true) @PathParam("extensionType") String extensionType, @ApiParam(value = "ID of the extension.",required=true) @PathParam("extensionId") String extensionId) {

        return delegate.getExtensionInfoById(extensionType,  extensionId );
    }

    @Valid
    @GET
    @Path("/{extensionType}/{extensionId}/metadata")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the extension metadata by type and id.", notes = "This API provides the capability to retrieve the extension metadata that is registered in the system.<br>   <b>Permission required:</b> <br>       * TBD <br>   <b>Scope required:</b> <br>       * TBD ", response = Map.class, responseContainer = "List", tags={ "Browse", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = String.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getMetadataById(@ApiParam(value = "Type of the extension",required=true) @PathParam("extensionType") String extensionType, @ApiParam(value = "ID of the extension.",required=true) @PathParam("extensionId") String extensionId) {

        return delegate.getMetadataById(extensionType,  extensionId );
    }

    @Valid
    @GET
    @Path("/{extensionType}/{extensionId}/template")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the extension template by type and id.", notes = "This API provides the capability to retrieve the extension template that is registered in the system.<br>   <b>Permission required:</b> <br>       * TBD <br>   <b>Scope required:</b> <br>       * TBD ", response = String.class, responseContainer = "Map", tags={ "Browse", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Map.class, responseContainer = "Map"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getTemplateById(@ApiParam(value = "Type of the extension",required=true) @PathParam("extensionType") String extensionType, @ApiParam(value = "ID of the extension.",required=true) @PathParam("extensionId") String extensionId) {

        return delegate.getTemplateById(extensionType,  extensionId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List the extensions.", notes = "This API provides the capability to retrieve the extensions that are registered in the system.<br>   <b>Permission required:</b> <br>       * TBD <br>   <b>Scope required:</b> <br>       * TBD ", response = ExtensionListItem.class, responseContainer = "List", tags={ "Browse", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful operation", response = ExtensionListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response listExtensions() {

        return delegate.listExtensions();
    }

    @Valid
    @GET
    @Path("/{extensionType}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List the extensions by type.", notes = "This API provides the capability to retrieve the extensions that are registered in the system.<br>   <b>Permission required:</b> <br>       * TBD <br>   <b>Scope required:</b> <br>       * TBD ", response = ExtensionListItem.class, responseContainer = "List", tags={ "Browse" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful operation", response = ExtensionListItem.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response listExtensionsByType(@ApiParam(value = "ID of the extension",required=true) @PathParam("extensionType") String extensionType) {

        return delegate.listExtensionsByType(extensionType );
    }

}
