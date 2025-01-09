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

package org.wso2.carbon.identity.rest.api.server.notification.template.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.EmailTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.Error;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SMSTemplateWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SimpleTemplate;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.SimpleTemplateTypeID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeOverview;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.model.TemplateTypeWithID;
import org.wso2.carbon.identity.rest.api.server.notification.template.v1.NotificationApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/notification")
@Api(description = "The notification API")

public class NotificationApi  {

    @Autowired
    private NotificationApiService delegate;

    @Valid
    @POST
    @Path("/email/template-types/{template-type-id}/app-templates/{app-uuid}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new application email template to an existing email template type.", notes = "Another application email template with the same locale should not already exist in the respective email template type. <br>  <b>Scope required:</b> <br> * internal_email_mgt_create / internal_template_mgt_update ", response = SimpleTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = SimpleTemplate.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Item Already Exists.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response addAppEmailTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid, @ApiParam(value = "Email template to be added." ) @Valid EmailTemplateWithID emailTemplateWithID) {

        return delegate.addAppEmailTemplate(templateTypeId,  appUuid,  emailTemplateWithID );
    }

    @Valid
    @POST
    @Path("/sms/template-types/{template-type-id}/app-templates/{app-uuid}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new application SMS template to an existing SMS template type.", notes = "Another application SMS template with the same locale should not already exist in the respective SMS template type. <br>  <b>Scope required:</b><br> * internal_template_mgt_create ", response = SimpleTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application SMS Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = SimpleTemplate.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Item Already Exists.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response addAppSMSTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid, @ApiParam(value = "SMS template to be added." ) @Valid SMSTemplateWithID smSTemplateWithID) {

        return delegate.addAppSMSTemplate(templateTypeId,  appUuid,  smSTemplateWithID );
    }

    @Valid
    @POST
    @Path("/email/template-types")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new email template type.", notes = "Adds a new email template type to the system. An email template type can have any number of  organization or application email templates. <br>  * Attribute _**displayName**_ of the template type should be unique. <br>  <b>Scope required:</b>  <br>* internal_email_mgt_create / internal_template_mgt_update ", response = TemplateTypeWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = TemplateTypeWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 409, message = "Item Already Exists.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response addEmailTemplateType(@ApiParam(value = "Email template type to be added." ) @Valid TemplateTypeOverview templateTypeOverview) {

        return delegate.addEmailTemplateType(templateTypeOverview );
    }

    @Valid
    @POST
    @Path("/email/template-types/{template-type-id}/org-templates")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new organization email template to an existing email template type.", notes = "Another organization email template with the same locale should not already exist in the respective email template type. <br>  <b>Scope required:</b> <br> * internal_email_mgt_create / internal_template_mgt_update ", response = SimpleTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = SimpleTemplate.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Item Already Exists.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response addOrgEmailTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Email template to be added." ) @Valid EmailTemplateWithID emailTemplateWithID) {

        return delegate.addOrgEmailTemplate(templateTypeId,  emailTemplateWithID );
    }

    @Valid
    @POST
    @Path("/sms/template-types/{template-type-id}/org-templates")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new organization SMS template to an existing SMS template type.", notes = "Another organization SMS template with the same locale should not already exist in the respective SMS template type. <br>  <b>Scope required:</b><br> * internal_template_mgt_create ", response = SimpleTemplate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = SimpleTemplate.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Item Already Exists.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response addOrgSMSTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "SMS template to be added." ) @Valid SMSTemplateWithID smSTemplateWithID) {

        return delegate.addOrgSMSTemplate(templateTypeId,  smSTemplateWithID );
    }

    @Valid
    @POST
    @Path("/sms/template-types")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new SMS template type.", notes = "Adds a new SMS template type to the system. A SMS template type can have any number of  organization or application SMS templates. <br>  * Attribute _**displayName**_ of the template type should be unique. <br>  <b>Scope required:</b><br> * internal_template_mgt_create ", response = TemplateTypeWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Item Created", response = TemplateTypeWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 409, message = "Item Already Exists.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response addSMSTemplateType(@ApiParam(value = "SMS template type to be added." ) @Valid TemplateTypeOverview templateTypeOverview) {

        return delegate.addSMSTemplateType(templateTypeOverview );
    }

    @Valid
    @DELETE
    @Path("/email/template-types/{template-type-id}/app-templates/{app-uuid}/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes an application email template.", notes = "Removes an application email template identified by the template-type-id and the locale. <br>    <b>Scope required:</b><br> * internal_email_mgt_delete / internal_template_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item/s Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response deleteAppEmailTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale) {

        return delegate.deleteAppEmailTemplate(templateTypeId,  appUuid,  locale );
    }

    @Valid
    @DELETE
    @Path("/sms/template-types/{template-type-id}/app-templates/{app-uuid}/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes a SMS template.", notes = "Removes a SMS template identified by the template-type-id and the locale. <br>    <b>Scope required:</b><br>   * internal_template_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application SMS Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item/s Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response deleteAppSMSTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale) {

        return delegate.deleteAppSMSTemplate(templateTypeId,  appUuid,  locale );
    }

    @Valid
    @DELETE
    @Path("/email/template-types/{template-type-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes an email template type.", notes = "Removes an existing email template type with all its email templates from the system. <br> **Note : System resources are not eligible for deletion.** <br> Attempt to delete a system resource will result in a 403 Forbidden error. <br>  <b>Scope required:</b><br> * internal_email_mgt_delete / internal_template_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item/s Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response deleteEmailTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId) {

        return delegate.deleteEmailTemplateType(templateTypeId );
    }

    @Valid
    @DELETE
    @Path("/email/template-types/{template-type-id}/org-templates/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes an organization email template.", notes = "Removes an organization email template identified by the template-type-id and the locale. <br>    <b>Scope required:</b><br> * internal_email_mgt_delete / internal_template_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item/s Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response deleteOrgEmailTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale) {

        return delegate.deleteOrgEmailTemplate(templateTypeId,  locale );
    }

    @Valid
    @DELETE
    @Path("/sms/template-types/{template-type-id}/org-templates/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes an organization SMS template.", notes = "Removes an organization SMS template identified by the template-type-id and the locale. <br>    <b>Scope required:</b><br>   * internal_template_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item/s Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response deleteOrgSMSTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale) {

        return delegate.deleteOrgSMSTemplate(templateTypeId,  locale );
    }

    @Valid
    @DELETE
    @Path("/sms/template-types/{template-type-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Removes a SMS template type.", notes = "Removes an existing SMS template type with all its SMS templates from the system. <br> **Note : System resources are not eligible for deletion.** <br> Attempt to delete a system resource will result in a 403 Forbidden error. <br>  <b>Scope required:</b><br> * internal_template_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item/s Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response deleteSMSTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId) {

        return delegate.deleteSMSTemplateType(templateTypeId );
    }

    @Valid
    @GET
    @Path("/email/template-types/{template-type-id}/app-templates/{app-uuid}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the list of application email templates under the provided template type.", notes = "Retrieves the list of application email templates under the provided template type. <br>    <b>Scope required:</b><br>   * internal_email_mgt_view / internal_template_mgt_view<br> ", response = SimpleTemplate.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SimpleTemplate.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAllAppTemplatesOfEmailTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid,     @Valid@ApiParam(value = "Specifies whether to return resolved template/s throughout the ancestor organization hierarchy.", defaultValue="false") @DefaultValue("false")  @QueryParam("resolve") Boolean resolve) {

        return delegate.getAllAppTemplatesOfEmailTemplateType(templateTypeId,  appUuid,  resolve );
    }

    @Valid
    @GET
    @Path("/sms/template-types/{template-type-id}/app-templates/{app-uuid}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the list of application SMS templates under the provided template type.", notes = "Retrieves the list of application SMS templates under the provided template type. <br>    <b>Scope required:</b><br>   * internal_template_mgt_view<br> ", response = SimpleTemplate.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application SMS Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SimpleTemplate.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAllAppTemplatesOfSMSTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid,     @Valid@ApiParam(value = "Specifies whether to return resolved template/s throughout the ancestor organization hierarchy.", defaultValue="false") @DefaultValue("false")  @QueryParam("resolve") Boolean resolve) {

        return delegate.getAllAppTemplatesOfSMSTemplateType(templateTypeId,  appUuid,  resolve );
    }

    @Valid
    @GET
    @Path("/email/template-types")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves all the email template types.", notes = "Retrieves all the email template types in the system. <br>  <br> <b>Scope required:</b> <br>* internal_email_mgt_view / internal_template_mgt_view ", response = TemplateTypeWithID.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = TemplateTypeWithID.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAllEmailTemplateTypes() {

        return delegate.getAllEmailTemplateTypes();
    }

    @Valid
    @GET
    @Path("/email/template-types/{template-type-id}/org-templates")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the list of organization email templates under the provided template type.", notes = "Retrieves the list of organization email templates under the provided template type. <br>    <b>Scope required:</b><br>   * internal_email_mgt_view / internal_template_mgt_view<br> ", response = SimpleTemplate.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SimpleTemplate.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAllOrgTemplatesOfEmailTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId,     @Valid@ApiParam(value = "Specifies whether to return resolved template/s throughout the ancestor organization hierarchy.", defaultValue="false") @DefaultValue("false")  @QueryParam("resolve") Boolean resolve) {

        return delegate.getAllOrgTemplatesOfEmailTemplateType(templateTypeId,  resolve );
    }

    @Valid
    @GET
    @Path("/sms/template-types/{template-type-id}/org-templates")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the list of organization SMS templates under the provided template type.", notes = "Retrieves the list of organization SMS templates under the provided templalte type. <br>    <b>Scope required:</b><br>   * internal_template_mgt_view<br> ", response = SimpleTemplate.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SimpleTemplate.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAllOrgTemplatesOfSMSTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId,     @Valid@ApiParam(value = "Specifies whether to return resolved template/s throughout the ancestor organization hierarchy.", defaultValue="false") @DefaultValue("false")  @QueryParam("resolve") Boolean resolve) {

        return delegate.getAllOrgTemplatesOfSMSTemplateType(templateTypeId,  resolve );
    }

    @Valid
    @GET
    @Path("/sms/template-types")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves all the SMS template types.", notes = "Retrieves all the SMS template types in the system. <br>  <b>Scope required:</b><br> * internal_template_mgt_view ", response = TemplateTypeWithID.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = TemplateTypeWithID.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAllSMSTemplateTypes() {

        return delegate.getAllSMSTemplateTypes();
    }

    @Valid
    @GET
    @Path("/email/template-types/{template-type-id}/system-templates")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the system email templates of template type.", notes = "Retrieves the list of system default email templates under the provided template type. <br>    <b>Scope required:</b><br>   * internal_email_mgt_view / internal_template_mgt_view<br> ", response = SimpleTemplate.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "System Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SimpleTemplate.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAllSystemTemplatesOfEmailTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId) {

        return delegate.getAllSystemTemplatesOfEmailTemplateType(templateTypeId );
    }

    @Valid
    @GET
    @Path("/sms/template-types/{template-type-id}/system-templates")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the list of system SMS templates with the template type id.", notes = "Retrieves the list of system default SMS templates under the provided template type. <br>    <b>Scope required:</b><br>   * internal_template_mgt_view<br> ", response = SimpleTemplate.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "System Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SimpleTemplate.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAllSystemTemplatesOfSMSTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId) {

        return delegate.getAllSystemTemplatesOfSMSTemplateType(templateTypeId );
    }

    @Valid
    @GET
    @Path("/email/template-types/{template-type-id}/app-templates/{app-uuid}/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves a single email template of application.", notes = "Retrieves the application email template that matches to the template-type-id and the locale. <br>  <b>Scope required:</b><br>   * internal_email_mgt_view / internal_template_mgt_view ", response = EmailTemplateWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = EmailTemplateWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAppEmailTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale,     @Valid@ApiParam(value = "Specifies whether to return resolved template/s throughout the ancestor organization hierarchy.", defaultValue="false") @DefaultValue("false")  @QueryParam("resolve") Boolean resolve) {

        return delegate.getAppEmailTemplate(templateTypeId,  appUuid,  locale,  resolve );
    }

    @Valid
    @GET
    @Path("/sms/template-types/{template-type-id}/app-templates/{app-uuid}/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves a single SMS template of application.", notes = "Retrieves the application SMS template that matches to the template-type-id and the locale. <br>  <b>Scope required:</b><br>   * internal_template_mgt_view ", response = SMSTemplateWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application SMS Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SMSTemplateWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getAppSMSTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale,     @Valid@ApiParam(value = "Specifies whether to return resolved template/s throughout the ancestor organization hierarchy.", defaultValue="false") @DefaultValue("false")  @QueryParam("resolve") Boolean resolve) {

        return delegate.getAppSMSTemplate(templateTypeId,  appUuid,  locale,  resolve );
    }

    @Valid
    @GET
    @Path("/email/template-types/{template-type-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the email template type corresponding to the template type id.", notes = "Retrieves the email template type in the system identified by the template-type-id. <br>  <b>Scope required:</b><br> * internal_email_mgt_view / internal_template_mgt_view<br> ", response = TemplateTypeWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = TemplateTypeWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getEmailTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId) {

        return delegate.getEmailTemplateType(templateTypeId );
    }

    @Valid
    @GET
    @Path("/email/template-types/{template-type-id}/org-templates/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves a single organization email template.", notes = "Retrieves the organization email template that matches to the template-type-id and the locale. <br>  <b>Scope required:</b><br>   * internal_email_mgt_view / internal_template_mgt_view ", response = EmailTemplateWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = EmailTemplateWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getOrgEmailTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale,     @Valid@ApiParam(value = "Specifies whether to return resolved template/s throughout the ancestor organization hierarchy.", defaultValue="false") @DefaultValue("false")  @QueryParam("resolve") Boolean resolve) {

        return delegate.getOrgEmailTemplate(templateTypeId,  locale,  resolve );
    }

    @Valid
    @GET
    @Path("/sms/template-types/{template-type-id}/org-templates/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves a single organization SMS template.", notes = "Retrieves the organization SMS template that matches to the template-type-id and the locale. <br>  <b>Scope required:</b><br>   * internal_template_mgt_view ", response = SMSTemplateWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SMSTemplateWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getOrgSMSTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale,     @Valid@ApiParam(value = "Specifies whether to return resolved template/s throughout the ancestor organization hierarchy.", defaultValue="false") @DefaultValue("false")  @QueryParam("resolve") Boolean resolve) {

        return delegate.getOrgSMSTemplate(templateTypeId,  locale,  resolve );
    }

    @Valid
    @GET
    @Path("/sms/template-types/{template-type-id}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the SMS template type corresponding to the template type id.", notes = "Retrieves the SMS template type in the system identified by the template-type-id. <br>  <b>Scope required:</b><br> * internal_template_mgt_view<br> ", response = TemplateTypeWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Template Types", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = TemplateTypeWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getSMSTemplateType(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId) {

        return delegate.getSMSTemplateType(templateTypeId );
    }

    @Valid
    @GET
    @Path("/email/template-types/{template-type-id}/system-templates/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the default email template for given type and locale.", notes = "Retrieves the system default email template that matches the template-type-id and the locale. <br>  <b>Scope required:</b><br>   * internal_email_mgt_view / internal_template_mgt_view ", response = EmailTemplateWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "System Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = EmailTemplateWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getSystemEmailTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale) {

        return delegate.getSystemEmailTemplate(templateTypeId,  locale );
    }

    @Valid
    @GET
    @Path("/sms/template-types/{template-type-id}/system-templates/{locale}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the default organization SMS template.", notes = "Retrieves the system default SMS template that matches to the template-type-id and the locale. <br>  <b>Scope required:</b><br>   * internal_template_mgt_view ", response = SMSTemplateWithID.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "System Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Search results matching the given criteria.", response = SMSTemplateWithID.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response getSystemSMSTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale) {

        return delegate.getSystemSMSTemplate(templateTypeId,  locale );
    }

    @Valid
    @POST
    @Path("/reset-template-type")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Resets an template type.", notes = "Resets an existing template type by deleting all its **user defined** templates from the system. <br>  <b>Scope required:</b><br> * internal_template_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Functions", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Item/s Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response resetTemplateType(@ApiParam(value = "" ) @Valid SimpleTemplateTypeID simpleTemplateTypeID) {

        return delegate.resetTemplateType(simpleTemplateTypeID );
    }

    @Valid
    @PUT
    @Path("/email/template-types/{template-type-id}/app-templates/{app-uuid}/{locale}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Replaces an existing application email template.", notes = "Replaces the application email template identified by the template-type-id and the locale. <br>    <b>Scope required:</b><br>   * internal_email_mgt_update / internal_template_mgt_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Item Updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response updateAppEmailTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale, @ApiParam(value = "Email templates for the template type." ) @Valid EmailTemplate emailTemplate) {

        return delegate.updateAppEmailTemplate(templateTypeId,  appUuid,  locale,  emailTemplate );
    }

    @Valid
    @PUT
    @Path("/sms/template-types/{template-type-id}/app-templates/{app-uuid}/{locale}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Replaces an existing application SMS template.", notes = "Replaces the application SMS template identified by the template-type-id and the locale. <br>    <b>Scope required:</b><br>   * internal_template_mgt_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Application SMS Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Item Updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response updateAppSMSTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "Application UUID.",required=true) @PathParam("app-uuid") String appUuid, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale, @ApiParam(value = "Application SMS template for the template type and locale." ) @Valid SMSTemplate smSTemplate) {

        return delegate.updateAppSMSTemplate(templateTypeId,  appUuid,  locale,  smSTemplate );
    }

    @Valid
    @PUT
    @Path("/email/template-types/{template-type-id}/org-templates/{locale}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Replaces an existing organization email template.", notes = "Replaces the organization email template identified by the template-type-id and the locale. <br>    <b>Scope required:</b><br>   * internal_email_mgt_update / internal_template_mgt_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Email Templates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Item Updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response updateOrgEmailTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale, @ApiParam(value = "Updated email template for the template type." ) @Valid EmailTemplate emailTemplate) {

        return delegate.updateOrgEmailTemplate(templateTypeId,  locale,  emailTemplate );
    }

    @Valid
    @PUT
    @Path("/sms/template-types/{template-type-id}/org-templates/{locale}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Replaces an existing organization SMS template.", notes = "Replaces the organization SMS template identified by the template-type-id and the locale. <br>    <b>Scope required:</b><br>   * internal_template_mgt_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "SMS Templates" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Item Updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "The specified resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class)
    })
    public Response updateOrgSMSTemplate(@ApiParam(value = "Template Type ID.",required=true) @PathParam("template-type-id") String templateTypeId, @ApiParam(value = "This should be a valid locale.",required=true) @PathParam("locale") String locale, @ApiParam(value = "Updated SMS template for the template type." ) @Valid SMSTemplate smSTemplate) {

        return delegate.updateOrgSMSTemplate(templateTypeId,  locale,  smSTemplate );
    }

}
