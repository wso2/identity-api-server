/*
* Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.image.service.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;

import java.io.File;
import org.wso2.carbon.identity.api.server.image.service.v1.ImagesApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/images")
@Api(description = "The images API")

public class ImagesApi  {

    @Autowired
    private ImagesApiService delegate;

    @Valid
    @DELETE
    @Path("/{type}/{id}")
    
    
    @ApiOperation(value = "Deletes an image file.", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Image Delete", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successful deletion", response = Void.class),
        @ApiResponse(code = 500, message = "Unexpected error.", response = Void.class)
    })
    public Response deleteImage(@ApiParam(value = "unique id for the image",required=true) @PathParam("id") String id, @ApiParam(value = "type of uploading image",required=true, allowableValues="i, a, u") @PathParam("type") String type) {

        return delegate.deleteImage(id,  type );
    }

    @Valid
    @GET
    @Path("/{type}/{id}")
    
    @Produces({ "image/png" })
    @ApiOperation(value = "Downloads an image file.", notes = "", response = File.class, tags={ "Image Download", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfull operation", response = File.class),
        @ApiResponse(code = 400, message = "Bad request. Id must be an integer and larger than 0.", response = Void.class),
        @ApiResponse(code = 404, message = "An image with the specified ID was not found.", response = Void.class),
        @ApiResponse(code = 500, message = "Unexpected error.", response = Void.class)
    })
    public Response downloadImage(@ApiParam(value = "unique id for the image",required=true) @PathParam("id") String id, @ApiParam(value = "type of uploading image",required=true, allowableValues="i, a, u") @PathParam("type") String type) {

        return delegate.downloadImage(id,  type );
    }

    @Valid
    @POST
    
    @Consumes({ "multipart/form-data" })
    
    @ApiOperation(value = "Uploads an image (image can be of idp,app or user type).", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Image Upload" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfull upload", response = Void.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid", response = Void.class),
        @ApiResponse(code = 500, message = "Unexpected error.", response = Void.class)
    })
    public Response uploadImage( @Multipart(value = "file") InputStream fileInputStream, @Multipart(value = "file" ) Attachment fileDetail, @Multipart(value = "type", required = false)  String type) {

        return delegate.uploadImage(fileInputStream, fileDetail,  type );
    }

}
