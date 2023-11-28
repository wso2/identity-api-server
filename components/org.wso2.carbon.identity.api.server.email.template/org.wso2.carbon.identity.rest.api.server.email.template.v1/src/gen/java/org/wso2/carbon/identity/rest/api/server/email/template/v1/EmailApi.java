/*
 * Copyright (c) 2019, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.rest.api.server.email.template.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;

import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateType;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateTypeWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateTypeWithoutTemplates;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.Error;
import java.util.List;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.model.SimpleEmailTemplate;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.EmailApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/email")
@Api(description = "The email API")

public class EmailApi  {

    @Autowired
    private EmailApiService delegate;

    @Valid
    @POST
    @Path("/template-types/{template-type-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new email template to an existing email template type.", notes = "Another email template with the same locale should not already exist in the respective email template type. <br>  <b>Permission required:</b> * /permission/admin/manage/identity/emailmgt/create<br> <b>Scopes required:</b> <br>* internal_email_mgt_create ", response = SimpleEmailTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = SimpleEmailTemplate.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Item Already Exists", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response addEmailTemplate(@ApiParam(value = "Email Template Type ID",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Email template to be added." ) @Valid EmailTemplateWithID emailTemplateWithID) {

        return delegate.addEmailTemplate(templateTypeId,  emailTemplateWithID );
    }

    @Valid
    @POST
    @Path("/template-types")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new email template type.", notes = "Adds a new email template type to the system. An email template type can have any number of email templates. <br>  * Attribute _**displayName**_ of the template type should be unique. <br>  <b>Permission required:</b> <br> * /permission/admin/manage/identity/emailmgt/create <br>  <b>Scopes required:</b>  <br>* internal_email_mgt_create ", response = EmailTemplateTypeWithoutTemplates.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = EmailTemplateTypeWithoutTemplates.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Item Already Exists", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response addEmailTemplateType(@ApiParam(value = "Email template type to be added." ) @Valid EmailTemplateType emailTemplateType) {

        return delegate.addEmailTemplateType(emailTemplateType );
    }

    @Valid
    @DELETE
    @Path("/template-types/{template-type-id}/templates/{template-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes an email template.", notes = "Removes an email template identified by the template-type-id and the template-id. <br>  <b>Permission required:</b> <br>   * /permission/admin/manage/identity/emailmgt/delete <br>   <b>Scopes required:</b><br> * internal_email_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response deleteEmailTemplate(@ApiParam(value = "Email Template Type ID",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Email template ID. This should be a valid locale.",required=true) @PathParam("template-id") String templateId) {

        return delegate.deleteEmailTemplate(templateTypeId,  templateId );
    }

    @Valid
    @DELETE
    @Path("/template-types/{template-type-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes an email template type.", notes = "Removes an existing email template type with all its email templates from the system. <br>  <b>Permission required:</b><br> * /permission/admin/manage/identity/emailmgt/delete <br> <b>Scopes required:</b><br> * internal_email_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response deleteEmailTemplateType(@ApiParam(value = "Email Template Type ID",required=true) @PathParam("template-type-id") String templateTypeId) {

        return delegate.deleteEmailTemplateType(templateTypeId );
    }

    @Valid
    @GET
    @Path("/template-types")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves all the email template types.", notes = "Retrieves all the email template types in the system, with limited details of the email templates.  <b>Permission required:</b><br> * /permission/admin/manage/identity/emailmgt/view <br>  <b>Scopes required:</b>  <br>* internal_email_mgt_view ", response = EmailTemplateTypeWithoutTemplates.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = EmailTemplateTypeWithoutTemplates.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getAllEmailTemplateTypes(    @Valid @Min(0)@ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Define the order in which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_", allowableValues="asc, desc")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_")  @QueryParam("sortBy") String sortBy,     @Valid@ApiParam(value = "Specifies the required parameters in the response.")  @QueryParam("requiredAttributes") String requiredAttributes) {

        return delegate.getAllEmailTemplateTypes(limit,  offset,  sortOrder,  sortBy,  requiredAttributes );
    }

    @Valid
    @GET
    @Path("/template-types/{template-type-id}/templates/{template-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves a single email template.", notes = "Retrieves the email template that matches to the template-type-id and the template-id. <br>  <b>Permission required:</b> <br>   * /permission/admin/manage/identity/emailmgt/view <br>   <b>Scopes required:</b><br>* internal_email_mgt_view ", response = EmailTemplateWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = EmailTemplateWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getEmailTemplate(@ApiParam(value = "Email Template Type ID",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Email template ID. This should be a valid locale.",required=true) @PathParam("template-id") String templateId,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Define the order in which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_", allowableValues="asc, desc")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_")  @QueryParam("sortBy") String sortBy) {

        return delegate.getEmailTemplate(templateTypeId,  templateId,  limit,  offset,  sortOrder,  sortBy );
    }

    @Valid
    @GET
    @Path("/template-types/{template-type-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the email template type corresponding to the template type id.", notes = "Retrieves the email template type in the system identified by the template-type-id. <br>  <b>Permission required:</b> <br> * /permission/admin/manage/identity/emailmgt/view <br><b>Scopes required:</b><br> * internal_email_mgt_view<br> ", response = EmailTemplateTypeWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = EmailTemplateTypeWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getEmailTemplateType(@ApiParam(value = "Email Template Type ID",required=true) @PathParam("template-type-id") String templateTypeId,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Define the order in which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_", allowableValues="asc, desc")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_")  @QueryParam("sortBy") String sortBy) {

        return delegate.getEmailTemplateType(templateTypeId,  limit,  offset,  sortOrder,  sortBy );
    }

    @Valid
    @GET
    @Path("/template-types/{template-type-id}/templates")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the list of email templates in the template type id.", notes = "Retrieves the list of email templates in the template type id. <br>   <b>Permission required:</b> <br>   * /permission/admin/manage/identity/emailmgt/view <br>   <b>Scopes required:</b><br>   * internal_email_mgt_view<br> ", response = SimpleEmailTemplate.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SimpleEmailTemplate.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getTemplatesListOfEmailTemplateType(@ApiParam(value = "Email Template Type ID",required=true) @PathParam("template-type-id") String templateTypeId,     @Valid @Min(0)@ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_")  @QueryParam("limit") Integer limit,     @Valid @Min(0)@ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_")  @QueryParam("offset") Integer offset,     @Valid@ApiParam(value = "Define the order in which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_", allowableValues="asc, desc")  @QueryParam("sortOrder") String sortOrder,     @Valid@ApiParam(value = "Attribute by which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_")  @QueryParam("sortBy") String sortBy) {

        return delegate.getTemplatesListOfEmailTemplateType(templateTypeId,  limit,  offset,  sortOrder,  sortBy );
    }

    @Valid
    @PUT
    @Path("/template-types/{template-type-id}/templates/{template-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Replaces an existing email template.", notes = "Replaces the email template identified by the template-type-id and the template-id. <br>  <b>Permission required:</b> <br>   * /permission/admin/manage/identity/emailmgt/update <br>   <b>Scopes required:</b><br>   * internal_email_mgt_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Item Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updateEmailTemplate(@ApiParam(value = "Email Template Type ID",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Email template ID. This should be a valid locale.",required=true) @PathParam("template-id") String templateId, @ApiParam(value = "Email templates for the template type" ) @Valid EmailTemplateWithID emailTemplateWithID) {

        return delegate.updateEmailTemplate(templateTypeId,  templateId,  emailTemplateWithID );
    }

    @Valid
    @PUT
    @Path("/template-types/{template-type-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Replaces all email templates of the respective email template type", notes = "Replaces all email templates of the respective email template type with the newly provided email templates. <br>  <b>Permission required:</b> <br> * /permission/admin/manage/identity/emailmgt/update <br> <b>Scopes required:</b><br> * internal_email_mgt_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Template Types" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Item Updated", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updateEmailTemplateType(@ApiParam(value = "Email Template Type ID",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Email templates for the template type" ) @Valid List<EmailTemplateWithID> emailTemplateWithID) {

        return delegate.updateEmailTemplateType(templateTypeId,  emailTemplateWithID );
    }

}
