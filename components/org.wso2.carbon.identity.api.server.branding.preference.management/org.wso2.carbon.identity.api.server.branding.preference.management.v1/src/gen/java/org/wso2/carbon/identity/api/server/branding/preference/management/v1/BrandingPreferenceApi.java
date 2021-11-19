/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.BrandingPreferenceModel;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.BrandingPreferenceApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/branding-preference")
@Api(description = "The branding-preference API")

public class BrandingPreferenceApi  {

    @Autowired
    private BrandingPreferenceApiService delegate;

    @Valid
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add branding preferences for Tenant.", notes = "This API provides the capability to Add a custom branding preference of the tenant.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/add <br>   <b>Scope required:</b> <br>     * internal_config_mgt_add ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Branding Preference", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successfully created.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response addBrandingPreference(@ApiParam(value = "This represents the branding preferences to be added." ,required=true) @Valid BrandingPreferenceModel brandingPreferenceModel) {

        return delegate.addBrandingPreference(brandingPreferenceModel );
    }

    @Valid
    @DELETE
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Deletes branding preferences of Tenant.", notes = "This API provides the capability to delete the branding preferences of the tenant.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/delete <br>   <b>Scope required:</b> <br>     * internal_config_mgt_delete ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Branding Preference", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response deleteBrandingPreference(    @Valid@ApiParam(value = "Resource Type to filter the retrieval of themes.", allowableValues="ORG, APP, CUSTOM")  @QueryParam("type") String type,     @Valid@ApiParam(value = "Application name/ Tenant name to filter the retrieval of themes.")  @QueryParam("name") String name,     @Valid@ApiParam(value = "Resource Localye to filter the retrieval of themes.")  @QueryParam("locale") String locale) {

        return delegate.deleteBrandingPreference(type,  name,  locale );
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get branding preference of Tenant.", notes = "This API provides the capability to retrieve the branding preference of the tenant. ", response = BrandingPreferenceModel.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Branding Preference", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = BrandingPreferenceModel.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response getBrandingPreference(    @Valid@ApiParam(value = "Resource Type to filter the retrieval of themes.", allowableValues="ORG, APP, CUSTOM")  @QueryParam("type") String type,     @Valid@ApiParam(value = "Application name/ Tenant name to filter the retrieval of themes.")  @QueryParam("name") String name,     @Valid@ApiParam(value = "Resource Localye to filter the retrieval of themes.")  @QueryParam("locale") String locale) {

        return delegate.getBrandingPreference(type,  name,  locale );
    }

    @Valid
    @PUT
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update branding preferences of Tenant.", notes = "This API provides the capability to update the branding preference of the tenant.<br>   <b>Permission required:</b> <br>     * /permission/admin/manage/identity/configmgt/update <br>   <b>Scope required:</b> <br>     * internal_config_mgt_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Branding Preference" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successfully updated", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class)
    })
    public Response updateBrandingPreference(@ApiParam(value = "This represents the branding preferences to be updated." ,required=true) @Valid BrandingPreferenceModel brandingPreferenceModel) {

        return delegate.updateBrandingPreference(brandingPreferenceModel );
    }

}
