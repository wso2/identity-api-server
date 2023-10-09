/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.tag.management.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagCreationResponse;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagModel;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagResponse;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.ApplicationTagsListResponse;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.application.tag.management.v1.ApplicationsTagsApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/applications-tags")
@Api(description = "The applications-tags API")

public class ApplicationsTagsApi  {

    @Autowired
    private ApplicationsTagsApiService delegate;

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a Tag ", notes = "This API provides the capability to store the tag provided by users.<br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/tagmgt/add <br>   <b>Scope required:</b> <br>       * internal_tag_mgt_add ", response = ApplicationTagCreationResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response.", response = ApplicationTagCreationResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response createApplicationTag(@ApiParam(value = "This represents the application template to be created." ,required=true) @Valid ApplicationTagModel applicationTagModel) {

        return delegate.createApplicationTag(applicationTagModel );
    }

    @Valid
    @DELETE
    @Path("/{tag-id}")
    
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Delete a tag by tag ID. ", notes = "This API provides the capability to partially update a tag by ID.<br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/tagmgt/delete <br>   <b>Scope required:</b> <br>       * internal_tag_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteApplicationTag(@ApiParam(value = "Application tag ID. ",required=true) @PathParam("tag-id") String tagId) {

        return delegate.deleteApplicationTag(tagId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List all Tags ", notes = "This API provides the capability to retrieve the list of tags available. ", response = ApplicationTagsListResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationTagsListResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getAllApplicationTags(    @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Number of records to skip for pagination. ")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. <br> /applications-tags?filter=name+eq+engineering <br> /applications-tags?filter=name+co+eng ")  @QueryParam("filter") String filter) {

        return delegate.getAllApplicationTags(limit,  offset,  filter );
    }

    @Valid
    @GET
    @Path("/{tag-id}")
    
    @Produces({ "application/json", "application/xml",  })
    @ApiOperation(value = "Retrieve a tag by ID ", notes = "This API provides the capability to retrieve a tag from the tag id. ", response = ApplicationTagResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ApplicationTagResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getApplicationTag(@ApiParam(value = "Application tag ID. ",required=true) @PathParam("tag-id") String tagId) {

        return delegate.getApplicationTag(tagId );
    }

    @Valid
    @PATCH
    @Path("/{tag-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Partially update a tag by ID ", notes = "This API provides the capability to partially update a tag by ID.<br>   <b>Permission required:</b> <br>       * /permission/admin/manage/identity/tagmgt/update <br>   <b>Scope required:</b> <br>       * internal_tag_mgt_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Applications Tags" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response patchTag(@ApiParam(value = "Application tag ID. ",required=true) @PathParam("tag-id") String tagId, @ApiParam(value = "This represents the application details to be updated." ,required=true) @Valid ApplicationTagModel applicationTagModel) {

        return delegate.patchTag(tagId,  applicationTagModel );
    }

}
