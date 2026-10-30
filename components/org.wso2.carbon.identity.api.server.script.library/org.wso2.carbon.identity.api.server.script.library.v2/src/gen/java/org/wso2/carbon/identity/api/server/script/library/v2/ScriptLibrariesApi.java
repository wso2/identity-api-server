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

package org.wso2.carbon.identity.api.server.script.library.v2;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.script.library.v2.model.Error;
import java.io.File;
import org.wso2.carbon.identity.api.server.script.library.v2.model.ScriptLibraryListResponse;
import org.wso2.carbon.identity.api.server.script.library.v2.model.ScriptLibraryResponse;
import org.wso2.carbon.identity.api.server.script.library.v2.ScriptLibrariesApiService;
import org.wso2.carbon.identity.api.server.script.library.v2.factories.ScriptLibrariesApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/script-libraries")
@Api(description = "The script-libraries API")

public class ScriptLibrariesApi  {

    private final ScriptLibrariesApiService delegate;

    public ScriptLibrariesApi() {

        this.delegate = ScriptLibrariesApiServiceFactory.getScriptLibrariesApi();
    }

    @Valid
    @POST
    
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a new script library. ", notes = "This API provides the capability to create a new Script library. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/functionsLibrarymgt/create <br> <b>Scope required:</b> <br>     * internal_functional_lib_create <br> ", response = Void.class, tags={ "Script Libraries", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addScriptLibrary(@Multipart(value = "name")  String name, @Multipart(value = "content") InputStream contentInputStream,@Multipart(value = "content" ) Attachment contentDetail, @Multipart(value = "description", required = false)  String description) {

        return delegate.addScriptLibrary(name,  contentInputStream, contentDetail,  description );
    }

    @Valid
    @DELETE
    @Path("/{script-library-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a script library. ", notes = "This API provides the capability to delete a script library using its id.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/functionsLibrarymgt/delete <br> <b>Scope required:</b> <br>     * internal_functional_lib_delete ", response = Void.class, tags={ "Script Libraries", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteScriptLibrary(@ApiParam(value = "Id of the script library, which is the base64url encoded script library name. ",required=true) @PathParam("script-library-id") String scriptLibraryId) {

        return delegate.deleteScriptLibrary(scriptLibraryId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json", "application/xml" })
    @ApiOperation(value = "List script libraries. ", notes = "This API provides the capability to list Script Libraries. <br> <b>Permission required:</b> <br>     * /permission/admin/manage/functionsLibrarymgt/view <br> <b>Scope required:</b> <br>     * internal_functional_lib_view ", response = ScriptLibraryListResponse.class, tags={ "Script Libraries", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = ScriptLibraryListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getScriptLibraries(    @Valid @Min(1)@ApiParam(value = "Maximum number of records to return. ", defaultValue="30") @DefaultValue("30")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ", defaultValue="0") @DefaultValue("0")  @QueryParam("offset") Integer offset) {

        return delegate.getScriptLibraries(limit,  offset );
    }

    @Valid
    @GET
    @Path("/{script-library-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get script library. ", notes = "This API provides the capability to retrieve the details of a script library using its id.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/functionsLibrarymgt/view <br> <b>Scope required:</b> <br>     * internal_functional_lib_view ", response = ScriptLibraryResponse.class, tags={ "Script Libraries", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = ScriptLibraryResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getScriptLibraryById(@ApiParam(value = "Id of the script library, which is the base64url encoded script library name. ",required=true) @PathParam("script-library-id") String scriptLibraryId) {

        return delegate.getScriptLibraryById(scriptLibraryId );
    }

    @Valid
    @GET
    @Path("/{script-library-id}/content")
    
    @Produces({ "application/octet-stream", "application/json" })
    @ApiOperation(value = "Get the content of the script library. ", notes = "This API provides the capability to retrieve the content of a script library using its id.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/functionsLibrarymgt/view <br> <b>Scope required:</b> <br>     * internal_functional_lib_view ", response = Object.class, tags={ "Script Libraries", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful Response", response = Object.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getScriptLibraryContentById(@ApiParam(value = "Id of the script library, which is the base64url encoded script library name. ",required=true) @PathParam("script-library-id") String scriptLibraryId) {

        return delegate.getScriptLibraryContentById(scriptLibraryId );
    }

    @Valid
    @PUT
    @Path("/{script-library-id}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a script library. ", notes = "This API provides the capability to update a script library using its id.<br> <b>Permission required:</b> <br>     * /permission/admin/manage/functionsLibrarymgt/update <br> <b>Scope required:</b> <br>     * internal_functional_lib_update ", response = Void.class, tags={ "Script Libraries" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateScriptLibrary(@ApiParam(value = "Id of the script library, which is the base64url encoded script library name. ",required=true) @PathParam("script-library-id") String scriptLibraryId, @Multipart(value = "content") InputStream contentInputStream,@Multipart(value = "content" ) Attachment contentDetail, @Multipart(value = "description", required = false)  String description) {

        return delegate.updateScriptLibrary(scriptLibraryId,  contentInputStream, contentDetail,  description );
    }

}
