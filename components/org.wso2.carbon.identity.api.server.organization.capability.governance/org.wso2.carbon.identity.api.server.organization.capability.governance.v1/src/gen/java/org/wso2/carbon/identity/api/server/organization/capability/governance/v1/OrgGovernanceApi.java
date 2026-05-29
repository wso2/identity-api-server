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

package org.wso2.carbon.identity.api.server.organization.capability.governance.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.organization.capability.governance.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.capability.governance.v1.model.GovernancePolicyEvaluateRequest;
import org.wso2.carbon.identity.api.server.organization.capability.governance.v1.model.GovernancePolicyEvaluateResponse;
import org.wso2.carbon.identity.api.server.organization.capability.governance.v1.OrgGovernanceApiService;
import org.wso2.carbon.identity.api.server.organization.capability.governance.v1.factories.OrgGovernanceApiServiceFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/org-governance")
@Api(description = "The org-governance API")

public class OrgGovernanceApi  {

    private final OrgGovernanceApiService delegate;

    public OrgGovernanceApi() {

        this.delegate = OrgGovernanceApiServiceFactory.getOrgGovernanceApi();
    }

    @Valid
    @POST
    @Path("/evaluate")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Evaluate whether the calling org has a given capability", notes = "Evaluates governance policies to determine whether the organization resolved from the auth token is permitted to use the specified capability.  The hierarchy is walked from the calling org up to the root.  **Scope required:** `internal_org_governance_evaluate`", response = GovernancePolicyEvaluateResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Governance Policy Evaluation" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Evaluation completed.", response = GovernancePolicyEvaluateResponse.class),
        @ApiResponse(code = 400, message = "Missing required fields (`resourceType` or `capability`).", response = Error.class),
        @ApiResponse(code = 401, message = "Missing or invalid authentication token.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal evaluation failure.", response = Error.class)
    })
    public Response evaluateGovernancePolicy(@ApiParam(value = "" ,required=true) @Valid GovernancePolicyEvaluateRequest governancePolicyEvaluateRequest) {

        return delegate.evaluateGovernancePolicy(governancePolicyEvaluateRequest );
    }

}
