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

package org.wso2.carbon.identity.api.server.media.service.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.media.service.v1.model.Error;
import java.io.File;
import org.wso2.carbon.identity.api.server.media.service.v1.model.MultipleFilesUploadResponse;
import org.wso2.carbon.identity.api.server.media.service.v1.model.ResourceFilesMetadata;
import org.wso2.carbon.identity.api.server.media.service.v1.MediaApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/media")
@Api(description = "The media API")

public class MediaApi  {

    @Autowired
    private MediaApiService delegate;

    @Valid
    @DELETE
    @Path("/user/{type}/{id}/data")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "A privileged user deletes a resource file.", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Media", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Resource file and metadata deleted successfully.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not implemented.", response = Error.class)
    })
    public Response deleteFile(@ApiParam(value = "The file type.",required=true) @PathParam("type") String type, @ApiParam(value = "Unique identifier for the file.",required=true) @PathParam("id") String id) {

        return delegate.deleteFile(type,  id );
    }

    @Valid
    @DELETE
    @Path("/me/{type}/{id}/data")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "A user deletes a file.", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Media", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Resource file and metadata deleted successfully.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not implemented.", response = Error.class)
    })
    public Response deleteMyFile(@ApiParam(value = "The file type.",required=true) @PathParam("type") String type, @ApiParam(value = "Unique identifier for the file.",required=true) @PathParam("id") String id) {

        return delegate.deleteMyFile(type,  id );
    }

    @Valid
    @GET
    @Path("/user/{type}/{id}/data")
    
    @Produces({ "application/octet-stream", "application/json" })
    @ApiOperation(value = "A privileged user downloads a file.", notes = "", response = File.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Media", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "File downloaded successfully.", response = File.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not implemented.", response = Error.class)
    })
    public Response downloadFile(@ApiParam(value = "The file type.",required=true) @PathParam("type") String type, @ApiParam(value = "Unique identifier for the file.",required=true) @PathParam("id") String id,     @Valid@ApiParam(value = "")  @QueryParam("identifier") String identifier) {

        return delegate.downloadFile(type,  id,  identifier );
    }

    @Valid
    @GET
    @Path("/me/{type}/{id}/data")
    
    @Produces({ "application/octet-stream", "application/json" })
    @ApiOperation(value = "A user downloads a file.", notes = "", response = File.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Media", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "File downloaded successfully.", response = File.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not implemented.", response = Error.class)
    })
    public Response downloadMyFile(@ApiParam(value = "The file type.",required=true) @PathParam("type") String type, @ApiParam(value = "Unique identifier for the file.",required=true) @PathParam("id") String id,     @Valid@ApiParam(value = "")  @QueryParam("identifier") String identifier) {

        return delegate.downloadMyFile(type,  id,  identifier );
    }

    @Valid
    @GET
    @Path("/public/{type}/{id}/data")
    
    @Produces({ "application/octet-stream", "application/json" })
    @ApiOperation(value = "Download a publically available file.", notes = "", response = File.class, tags={ "Media", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "File downloaded successfully.", response = File.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not implemented.", response = Error.class)
    })
    public Response downloadPublicFile(@ApiParam(value = "The file type.",required=true) @PathParam("type") String type, @ApiParam(value = "Unique identifier for the file.",required=true) @PathParam("id") String id,     @Valid@ApiParam(value = "")  @QueryParam("identifier") String identifier) {

        return delegate.downloadPublicFile(type,  id,  identifier );
    }

    @Valid
    @POST
    @Path("/user/{type}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "A privileged user uploads a single resource file or multiple representations of a single resource and metadata associated with the resource file(s).", notes = "", response = MultipleFilesUploadResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Media", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "File(s) uploaded successfully.", response = MultipleFilesUploadResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not implemented.", response = Error.class)
    })
    public Response uploadFile(@ApiParam(value = "The file type.",required=true) @PathParam("type") String type, @Multipart(value = "files") List<InputStream> filesInputStream, @Multipart(value = "files" ) List<Attachment> filesDetail, @Multipart(value = "metadata", required = false)  ResourceFilesMetadata metadata) {

        return delegate.uploadFile(type,  filesInputStream, filesDetail,  metadata );
    }

    @Valid
    @POST
    @Path("/me/{type}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "A user uploads a single resource file or multiple representations of a single resource and metadata associated with the resource file(s).", notes = "", response = MultipleFilesUploadResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Media" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "File(s) uploaded successfully.", response = MultipleFilesUploadResponse.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not implemented.", response = Error.class)
    })
    public Response uploadMyFile(@ApiParam(value = "The file type.",required=true) @PathParam("type") String type, @Multipart(value = "files") List<InputStream> filesInputStream, @Multipart(value = "files" ) List<Attachment> filesDetail, @Multipart(value = "metadata", required = false)  ResourceFilesMetadata metadata) {

        return delegate.uploadMyFile(type,  filesInputStream, filesDetail,  metadata );
    }

}
