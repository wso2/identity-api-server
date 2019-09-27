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

package org.wso2.carbon.identity.rest.api.server.email.template.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.*;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.EmailApiService;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.factories.EmailApiServiceFactory;

import io.swagger.annotations.ApiParam;

import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateRequestDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.ErrorDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.SimpleEmailTemplateResponseDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.SimpleEmailTemplateTypeDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateTypeDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateResponseDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.CompleteEmailTemplateTypeResponseDTO;
import org.wso2.carbon.identity.rest.api.server.email.template.v1.dto.LocaleResponseDTO;
import java.util.List;

import java.util.List;

import java.io.InputStream;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;

@Path("/email")
@io.swagger.annotations.Api(value = "/email", description = "the email API")
public class EmailApi  {

    @Autowired
    private EmailApiService delegate;

    @Valid
    @POST
    @Path("/templates/{email-template-type-id}")
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Add a new email template to an existing email template type.",
            notes = "Adds a new email template an existing email template type in the system. The locale of the new email template should not already exists in the respective email template type.\n\n<b>Permission required:</b>\n  * /permission/admin/manage/?\n",
            response = SimpleEmailTemplateResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Item Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addEmailTemplate(@ApiParam(value = "Email Template Type ID",required=true ) @PathParam("email-template-type-id")  String emailTemplateTypeId,
    @ApiParam(value = "Email template to be added."  ) @Valid CompleteEmailTemplateRequestDTO type) {

        return delegate.addEmailTemplate(emailTemplateTypeId,type);
    }

    @Valid
    @POST
    @Path("/templates")
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Add a new email template type.",
            notes = "Adds a new email template type to the system. An email template type can have any number of email templates. Attribute _**name**_ should be unique.\n\n<b>Permission required:</b>\n  * /permission/admin/manage/?\n",
            response = SimpleEmailTemplateTypeDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Item Created"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 409, message = "Item Already Exists"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response addEmailTemplateType(@ApiParam(value = "Email template type to be added."  ) @Valid CompleteEmailTemplateTypeDTO type) {

        return delegate.addEmailTemplateType(type);
    }

    @Valid
    @DELETE
    @Path("/templates/{email-template-type-id}/locale/{locale-code}")
    @io.swagger.annotations.ApiOperation(value = "Removes an email template that corresponds to the email template type ID and the locale.",
            notes = "Removes an email template that corresponds to the email template type ID and the locale.\n\n<b>Permission required:</b>\n* /permission/admin/manage/identity/?\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteEmailTemplate(@ApiParam(value = "Email Template Type ID",required=true ) @PathParam("email-template-type-id")  String emailTemplateTypeId,
    @ApiParam(value = "Unique code of the locale.",required=true ) @PathParam("locale-code")  String localeCode) {

        return delegate.deleteEmailTemplate(emailTemplateTypeId,localeCode);
    }

    @Valid
    @DELETE
    @Path("/templates/{email-template-type-id}")
    @io.swagger.annotations.ApiOperation(value = "Removes an email template type.",
            notes = "Removes an existing email template type with all it's email templates from the system.\n\n<b>Permission required:</b>\n* /permission/admin/manage/identity/challenge/delete\n",
            response = void.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item Deleted"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response deleteEmailTemplateType(@ApiParam(value = "Email Template Type ID",required=true ) @PathParam("email-template-type-id")  String emailTemplateTypeId) {

        return delegate.deleteEmailTemplateType(emailTemplateTypeId);
    }

    @Valid
    @GET
    @Path("/templates/{email-template-type-id}/locale/{locale-code}")
    @io.swagger.annotations.ApiOperation(value = "Retrieves the email template of the provided locale of the corresponding email template type id.",
            notes = "Retrieves the email template of the provided locale of the corresponding email template type.\n\n<b>Permission required:</b>\n* /permission/admin/manage/identity/?\n",
            response = CompleteEmailTemplateResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Search results matching the given criteria."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getAllEmailTemplateTypeLocale(@ApiParam(value = "Email Template Type ID",required=true ) @PathParam("email-template-type-id")  String emailTemplateTypeId,
    @ApiParam(value = "Unique code of the locale.",required=true ) @PathParam("locale-code")  String localeCode,
    @ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Define the order how the retrieved records should be sorted. _<b>This option is not yet supported.<b>_", allowableValues="{values=[asc, desc]}") @QueryParam("sort")  String sort,
    @ApiParam(value = "Attribute by which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_") @QueryParam("sortBy")  String sortBy) {

        return delegate.getAllEmailTemplateTypeLocale(emailTemplateTypeId,localeCode,limit,offset,sort,sortBy);
    }

    @Valid
    @GET
    @Path("/templates")
    @io.swagger.annotations.ApiOperation(value = "Retrieve all the email template types.",
            notes = "Retrieve all the email template types in the system, with limited details of the email templates.\n\n<b>Permission required:</b>\n  * /permission/admin/manage/?\n",
            response = SimpleEmailTemplateTypeDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Search results matching the given criteria."),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getAllEmailTemplateTypes(@ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Define the order how the retrieved records should be sorted. _<b>This option is not yet supported.<b>_", allowableValues="{values=[asc, desc]}") @QueryParam("sort")  String sort,
    @ApiParam(value = "Attribute by which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_") @QueryParam("sortBy")  String sortBy) {

        return delegate.getAllEmailTemplateTypes(limit,offset,sort,sortBy);
    }

    @Valid
    @GET
    @Path("/templates/{email-template-type-id}")
    @io.swagger.annotations.ApiOperation(value = "Retrieve the email template type corresponds to the template type id.",
            notes = "Retrieve the email template type in the system identified by the email-template-type-id.\n\n<b>Permission required:</b>\n* /permission/admin/manage/identity/challenge/view\n",
            response = CompleteEmailTemplateTypeResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Search results matching the given criteria."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getEmailTemplateType(@ApiParam(value = "Email Template Type ID",required=true ) @PathParam("email-template-type-id")  String emailTemplateTypeId,
    @ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Define the order how the retrieved records should be sorted. _<b>This option is not yet supported.<b>_", allowableValues="{values=[asc, desc]}") @QueryParam("sort")  String sort,
    @ApiParam(value = "Attribute by which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_") @QueryParam("sortBy")  String sortBy) {

        return delegate.getEmailTemplateType(emailTemplateTypeId,limit,offset,sort,sortBy);
    }

    @Valid
    @GET
    @Path("/templates/{email-template-type-id}/locale")
    @io.swagger.annotations.ApiOperation(value = "Retrieve the set of locale objects corresponds to the template type id.",
            notes = "Retrieve the set of locale objects corresponds to the template type id.\n\n<b>Permission required:</b>\n* /permission/admin/manage/identity/challenge/view\n",
            response = LocaleResponseDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Search results matching the given criteria."),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response getEmailTemplateTypeForLocale(@ApiParam(value = "Email Template Type ID",required=true ) @PathParam("email-template-type-id")  String emailTemplateTypeId,
    @ApiParam(value = "Maximum number of records to return. _<b>This option is not yet supported.<b>_") @QueryParam("limit")  Integer limit,
    @ApiParam(value = "Number of records to skip for pagination. _<b>This option is not yet supported.<b>_") @QueryParam("offset")  Integer offset,
    @ApiParam(value = "Define the order how the retrieved records should be sorted. _<b>This option is not yet supported.<b>_", allowableValues="{values=[asc, desc]}") @QueryParam("sort")  String sort,
    @ApiParam(value = "Attribute by which the retrieved records should be sorted. _<b>This option is not yet supported.<b>_") @QueryParam("sortBy")  String sortBy) {

        return delegate.getEmailTemplateTypeForLocale(emailTemplateTypeId,limit,offset,sort,sortBy);
    }

    @Valid
    @PUT
    @Path("/templates/{email-template-type-id}/locale/{locale-code}")
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Replace the email template identified by the locale, of the respective email template type.",
            notes = "Replace the email template identified by the locale, of the respective email template type.\n\n<b>Permission required:</b>\n* /permission/admin/manage/?\n",
            response = SimpleEmailTemplateResponseDTO.class)
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item Updated"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateEmailTemplate(@ApiParam(value = "Email Template Type ID",required=true ) @PathParam("email-template-type-id")  String emailTemplateTypeId,
    @ApiParam(value = "Unique code of the locale.",required=true ) @PathParam("locale-code")  String localeCode,
    @ApiParam(value = "Email templates for the template type"  ) @Valid CompleteEmailTemplateRequestDTO templates) {

        return delegate.updateEmailTemplate(emailTemplateTypeId,localeCode,templates);
    }

    @Valid
    @PUT
    @Path("/templates/{email-template-type-id}")
    @Consumes({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Replace all email templates of the respective email template type with the newly provided email templates.",
            notes = "Replace all email templates of the respective email template type with the newly provided email templates.\n\n<b>Permission required:</b>\n* /permission/admin/manage/?\n",
            response = SimpleEmailTemplateResponseDTO.class, responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Item Updated"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid input request"),
        
        @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "The specified resource is not found"),
        
        @io.swagger.annotations.ApiResponse(code = 500, message = "Internal Server Error") })

    public Response updateEmailTemplateType(@ApiParam(value = "Email Template Type ID",required=true ) @PathParam("email-template-type-id")  String emailTemplateTypeId,
    @ApiParam(value = "Email templates for the template type"  ) @Valid List<CompleteEmailTemplateRequestDTO> templates) {

        return delegate.updateEmailTemplateType(emailTemplateTypeId,templates);
    }

}
