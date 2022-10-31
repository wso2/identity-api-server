/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
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

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.input.validation.v1.models.Error;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidateRequest;
import org.wso2.carbon.identity.api.server.input.validation.v1.models.ValidationConfigModal;
import org.wso2.carbon.identity.api.server.input.validation.v1.ValidationRulesApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/validation-rules")
@Api(description = "The validation-rules API")

public class ValidationRulesApi  {

    @Autowired
    private ValidationRulesApiService delegate;

    @Valid
    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get input validation Rules ", notes = "Get input validation configurations. ", response = ValidationConfigModal.class, tags={ "Input Validations Rules", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = ValidationConfigModal.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response getValidationConfiguration() {

        return delegate.getValidationConfiguration();
    }

    @Valid
    @PUT
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update Input Validation Rules ", notes = "This API provides the capability to update the input validation rules.<br> ", response = Void.class, tags={ "Input Validations Rules", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Successful response.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response updateInputValidationConfiguration(@ApiParam(value = "This represents the input validation rules." ,required=true) @Valid ValidationConfigModal validationConfigModal) {

        return delegate.updateInputValidationConfiguration(validationConfigModal );
    }

    @Valid
    @POST
    @Path("/validate")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Validate user input values ", notes = "", response = Void.class, tags={ "Validate input value" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented", response = Error.class)
    })
    public Response validateValues(@ApiParam(value = "This represents the input validation rules." ,required=true) @Valid ValidateRequest validateRequest) {

        return delegate.validateValues(validateRequest );
    }

}
