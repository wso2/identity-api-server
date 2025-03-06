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

package org.wso2.carbon.identity.api.server.rule.metadata.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.rule.metadata.v1.factories.RulesApiServiceFactory;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.model.Error;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.model.FieldDefinition;
import org.wso2.carbon.identity.api.server.rule.metadata.v1.RulesApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/rules")
@Api(description = "The rules API")

public class RulesApi  {

    private final RulesApiService delegate;

    public RulesApi() {

        this.delegate = RulesApiServiceFactory.getRulesApi();
    }

    @Valid
    @GET
    @Path("/metadata")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get metadata for rule configuration.", notes = "This API provides a list of fields, associated metadata, and applicable operators for each field based on the specified flow type. This information is used to populate the UI for rule configuration.   <b>Scope (Permission) required:</b> ``internal_rule_metadata_view``  ", response = FieldDefinition.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Metadata" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Applicable fields and operators", response = FieldDefinition.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Void.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getExpressionMeta(    @Valid @NotNull(message = "Property  cannot be null.") @ApiParam(value = "Specifies the flow to retrieve rule metadata. This ensures that the metadata returned is relevant to the given flow.  Note: At present, only the 'preIssueAccessToken' and 'preUpdatePassword' flows are supported. ",required=true, allowableValues="preIssueAccessToken, preUpdatePassword, preProfileUpdate, preLogin, postLogin, inLogin, preRegistration, inRegistration, inPasswordExpiry")  @QueryParam("flow") String flow) {

        return delegate.getExpressionMeta(flow );
    }

}
