/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vc.template.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.vc.template.management.v1.Error;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplate;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateCreationModel;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateList;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VCTemplateUpdateModel;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.VcTemplatesApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.factories.VcTemplatesApiServiceFactory;

import javax.validation.constraints.*;

@Path("/vc-templates")
@Api(description = "The vc-templates API")

public class VcTemplatesApi  {

    private final VcTemplatesApiService delegate;

    public VcTemplatesApi() {

        this.delegate = VcTemplatesApiServiceFactory.getVcTemplatesApi();
    }

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add new template", notes = "", response = VCTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created", response = VCTemplate.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class)
    })
    public Response addVCTemplate(@ApiParam(value = "" ,required=true) @Valid VCTemplateCreationModel vcTemplateCreationModel) {

        return delegate.addVCTemplate(vcTemplateCreationModel );
    }

    @Valid
    @DELETE
    @Path("/{template-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete template", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Template Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteVCTemplate(@ApiParam(value = "Server-generated UUID of the template.",required=true) @PathParam("template-id") String templateId) {

        return delegate.deleteVCTemplate(templateId );
    }

    @Valid
    @POST
    @Path("/{template-id}/offer")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Generate or regenerate credential offer", notes = "Creates a new credential offer.", response = VCTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Offer generated or regenerated successfully", response = VCTemplate.class),
        @ApiResponse(code = 404, message = "Template not found", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response generateVCCredentialOffer(@ApiParam(value = "Server-generated UUID of the template.",required=true) @PathParam("template-id") String templateId) {

        return delegate.generateVCCredentialOffer(templateId );
    }

    @Valid
    @GET
    @Path("/{template-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get template", notes = "", response = VCTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Template", response = VCTemplate.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getVCTemplate(@ApiParam(value = "Server-generated UUID of the template.",required=true) @PathParam("template-id") String templateId) {

        return delegate.getVCTemplate(templateId );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List templates", notes = "", response = VCTemplateList.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Array of templates", response = VCTemplateList.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response listVCTemplates(    @Valid@ApiParam(value = "Base64 encoded cursor value for backward pagination. ")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Base64 encoded cursor value for forward pagination. ")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. ")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Maximum number of records to return. ")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Specifies the required attributes in the response. Only 'properties' attribute is currently supported.")  @QueryParam("attributes") String attributes) {

        return delegate.listVCTemplates(before,  after,  filter,  limit,  attributes );
    }

    @Valid
    @DELETE
    @Path("/{template-id}/offer")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Revoke credential offer", notes = "Revokes and deletes the existing credential offer.", response = VCTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Offer revoked successfully", response = VCTemplate.class),
        @ApiResponse(code = 404, message = "Template or offer not found", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response revokeVCCredentialOffer(@ApiParam(value = "Server-generated UUID of the template.",required=true) @PathParam("template-id") String templateId) {

        return delegate.revokeVCCredentialOffer(templateId );
    }

    @Valid
    @PUT
    @Path("/{template-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update template", notes = "", response = VCTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "VC Templates" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Updated", response = VCTemplate.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateVCTemplate(@ApiParam(value = "Server-generated UUID of the template.",required=true) @PathParam("template-id") String templateId, @ApiParam(value = "" ,required=true) @Valid VCTemplateUpdateModel vcTemplateUpdateModel) {

        return delegate.updateVCTemplate(templateId,  vcTemplateUpdateModel );
    }

}
