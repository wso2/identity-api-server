/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.script.library.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import java.io.InputStream;

import org.wso2.carbon.identity.api.server.script.library.v1.factories.ScriptLibrariesApiServiceFactory;
import org.wso2.carbon.identity.api.server.script.library.v1.model.Error;

import java.io.File;

import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryListResponse;
import org.wso2.carbon.identity.api.server.script.library.v1.model.ScriptLibraryResponse;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/script-libraries")
@Api(description = "The script-libraries API")

public class ScriptLibrariesApi {

    private final ScriptLibrariesApiService delegate;

    public ScriptLibrariesApi() {

        this.delegate = ScriptLibrariesApiServiceFactory.getScriptLibrariesApi();
    }

    @Valid
    @POST

    @Consumes({"multipart/form-data"})
    @Produces({"application/json"})
    @ApiOperation(value = "Add a new script library. ", notes = "This API provides the capability to create a new Script library. ", response = Void.class, tags = {
            "Script Libraries",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful response", response = Void.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 409, message = "Conflict", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addScriptLibrary(@Multipart(value = "name") String name,
                                     @Multipart(value = "content") InputStream contentInputStream,
                                     @Multipart(value = "content") Attachment contentDetail,
                                     @Multipart(value = "description", required = false) String description) {

        return delegate.addScriptLibrary(name, contentInputStream, contentDetail, description);
    }

    @Valid
    @DELETE
    @Path("/{script-library-name}")

    @Produces({"application/json"})
    @ApiOperation(value = "Delete a script library. ", notes = "This API provides the capability to delete an script library by  giving its name. ", response = Void.class, tags = {
            "Script Libraries",})
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteScriptLibrary(
            @ApiParam(value = "Name of the script library", required = true) @PathParam("script-library-name") String scriptLibraryName) {

        return delegate.deleteScriptLibrary(scriptLibraryName);
    }

    @Valid
    @GET

    @Produces({"application/json", "application/xml"})
    @ApiOperation(value = "List script libraries. ", notes = "This API provides the capability to list Script Libraries. ", response = ScriptLibraryListResponse.class, tags = {
            "Script Libraries",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = ScriptLibraryListResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getScriptLibraries(
            @Valid @Min(1) @ApiParam(value = "Maximum number of records to return. ", defaultValue = "30") @DefaultValue("30") @QueryParam("limit") Integer limit,
            @Valid @ApiParam(value = "Number of records to skip for pagination. ", defaultValue = "0") @DefaultValue("0") @QueryParam("offset") Integer offset) {

        return delegate.getScriptLibraries(limit, offset);
    }

    @Valid
    @GET
    @Path("/{script-library-name}")

    @Produces({"application/json"})
    @ApiOperation(value = "Get script library. ", notes = "This API provides the capability to retrive the script library details by using its name. ", response = ScriptLibraryResponse.class, tags = {
            "Script Libraries",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = ScriptLibraryResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getScriptLibraryByName(
            @ApiParam(value = "Name of the script library", required = true) @PathParam("script-library-name") String scriptLibraryName) {

        return delegate.getScriptLibraryByName(scriptLibraryName);
    }

    @Valid
    @GET
    @Path("/{script-library-name}/content")

    @Produces({"application/octet-stream", "application/json"})
    @ApiOperation(value = "Get the content of the script library. ", notes = "This API provides the capability to retrive the content of script library by using its name. ", response = Object.class, tags = {
            "Script Libraries",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Response", response = Object.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getScriptLibraryContentByName(
            @ApiParam(value = "Name of the script library", required = true) @PathParam("script-library-name") String scriptLibraryName) {

        return delegate.getScriptLibraryContentByName(scriptLibraryName);
    }

    @Valid
    @PUT
    @Path("/{script-library-name}")
    @Consumes({"multipart/form-data"})
    @Produces({"application/json"})
    @ApiOperation(value = "Update a script library. ", notes = "This API provides the capability to Update a script library of an script library by using name. ", response = Void.class, tags = {
            "Script Libraries"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Updated", response = Void.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 403, message = "Forbidden", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateScriptLibrary(
            @ApiParam(value = "Name of the script library", required = true) @PathParam("script-library-name") String scriptLibraryName,
            @Multipart(value = "content") InputStream contentInputStream,
            @Multipart(value = "content") Attachment contentDetail,
            @Multipart(value = "description", required = false) String description) {

        return delegate.updateScriptLibrary(scriptLibraryName, contentInputStream, contentDetail, description);
    }

}
