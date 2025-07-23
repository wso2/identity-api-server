/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.input.validation.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.input.validation.v1.factories.ValidationRulesApiServiceFactory;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.Error;
import java.util.List;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.RevertFields;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidationConfigModel;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidationConfigModelForField;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidatorModel;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/validation-rules")
@Api(description = "The validation-rules API")

public class ValidationRulesApi  {

    private final ValidationRulesApiService delegate;

    public ValidationRulesApi() {

        this.delegate = ValidationRulesApiServiceFactory.getValidationRulesApi();
    }

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Get validation rules for user inputs", response = ValidationConfigModel.class, responseContainer = "List", tags={ "Get Validation Rules", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Configurations successfully updated.", response = ValidationConfigModel.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getValidationRules() {

        return delegate.getValidationRules();
    }

    @Valid
    @GET
    @Path("/{field}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Get validation rules for user inputs", response = ValidationConfigModel.class, tags={ "Get Validation Rules for a field", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Configurations successfully updated for the field.", response = ValidationConfigModel.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Field not found", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getValidationRulesForField(@ApiParam(value = "name of the field",required=true) @PathParam("field") String field) {

        return delegate.getValidationRulesForField(field );
    }

    @Valid
    @GET
    @Path("/validators")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Get all validators", response = ValidatorModel.class, responseContainer = "List", tags={ "Get all validators", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Configurations successfully updated.", response = ValidatorModel.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getValidators() {

        return delegate.getValidators();
    }

    @Valid
    @POST
    @Path("/revert")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Revert validation rules of given fields to the default configuration.", response = Void.class, tags={ "Revert Validation Rules", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Field not found", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response revertValidationRulesForFields(@ApiParam(value = "Represents the fields to revert." ,required=true) @Valid RevertFields revertFields) {

        return delegate.revertValidationRulesForFields(revertFields );
    }

    @Valid
    @PUT
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Update validation rules for user inputs", response = ValidationConfigModel.class, responseContainer = "List", tags={ "Update Validation Rules" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Configurations successfully updated.", response = ValidationConfigModel.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updateValidationRules(@ApiParam(value = "Represents the password validation criteria." ,required=true) @Valid List<ValidationConfigModel> validationConfigModel) {

        return delegate.updateValidationRules(validationConfigModel );
    }

    @Valid
    @PUT
    @Path("/{field}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "Update validation rules for user inputs for a field", response = ValidationConfigModel.class, tags={ "Update Validation Rules for a field" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Configurations successfully updated for the field.", response = ValidationConfigModel.class),
        @ApiResponse(code = 400, message = "Invalid Input Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Field not found", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response updateValidationRulesForField(@ApiParam(value = "name of the field",required=true) @PathParam("field") String field, @ApiParam(value = "Represents the password validation criteria." ,required=true) @Valid ValidationConfigModelForField validationConfigModelForField) {

        return delegate.updateValidationRulesForField(field,  validationConfigModelForField );
    }

}
