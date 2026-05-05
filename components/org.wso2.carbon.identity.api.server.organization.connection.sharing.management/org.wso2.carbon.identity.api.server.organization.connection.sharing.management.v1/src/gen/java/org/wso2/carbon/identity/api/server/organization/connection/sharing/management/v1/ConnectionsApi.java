/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1;

import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.factories.ConnectionsApiServiceFactory;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionShareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionShareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionSharedOrganizationsResponse;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionUnshareSelectedRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ConnectionUnshareWithAllRequestBody;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.organization.connection.sharing.management.v1.model.ProcessSuccessResponse;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

@Path("/connections")
@Api(description = "The connections API")

public class ConnectionsApi  {

    private final ConnectionsApiService delegate;

    public ConnectionsApi() {

        this.delegate = ConnectionsApiServiceFactory.getConnectionsApi();
    }

    @Valid
    @GET
    @Path("/{connectionId}/share")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "List organizations a connection has been shared with", notes = "Retrieve the list of organizations that the specified connection has been shared with, including per-organization sharing mode where applicable.  The response shape depends on how the connection was shared: - If the connection was shared via **`ALL_EXISTING_AND_FUTURE_ORGS`** policy → a top-level   `sharingMode` is returned in the response (only when `attributes=sharingMode` is requested),   and no per-org `sharingMode` is present on each organization entry. - If the connection was shared via **`SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN`**   policy → no top-level `sharingMode` is returned, and each matching organization entry   includes its own `sharingMode` (only when `attributes=sharingMode` is requested). - If the connection was shared via **`SELECTED_ORG_ONLY`** policy → no `sharingMode` is   returned at either level, even if `attributes=sharingMode` is requested.  **Scope required:** `internal_connection_shared_access_view`", response = ConnectionSharedOrganizationsResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Connection Shared Organizations", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Successful response with the list of organizations the connection is shared with.", response = ConnectionSharedOrganizationsResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found — the specified connection does not exist.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response getConnectionSharedOrganizations(@ApiParam(value = "The unique identifier (UUID) of the connection.",required=true) @PathParam("connectionId") String connectionId,     @Valid@ApiParam(value = "Base64 encoded cursor value for backward pagination.")  @QueryParam("before") String before,     @Valid@ApiParam(value = "Base64 encoded cursor value for forward pagination.")  @QueryParam("after") String after,     @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports `sw`, `co`, `ew`, and `eq` operations.")  @QueryParam("filter") String filter,     @Valid@ApiParam(value = "Maximum number of records to return. If not specified, all shared organizations are returned.")  @QueryParam("limit") Integer limit,     @Valid@ApiParam(value = "Determines whether a recursive search should happen. If set to `true`, includes shared organizations at all levels of the hierarchy; if set to `false`, includes only shared organizations at the next level of the hierarchy.")  @QueryParam("recursive") Boolean recursive,     @Valid@ApiParam(value = "Specifies the additional parameters to include in the response. Supported value: `sharingMode`.  When `sharingMode` is requested: - For connections shared via `ALL_EXISTING_AND_FUTURE_ORGS`, a top-level `sharingMode`   is included in the response. - For connections shared via `SELECTED_ORG_WITH_ALL_EXISTING_AND_FUTURE_CHILDREN`,   a `sharingMode` is included on each applicable organization entry. - For connections shared via `SELECTED_ORG_ONLY`, no `sharingMode` is returned.")  @QueryParam("attributes") String attributes) {

        return delegate.getConnectionSharedOrganizations(connectionId,  before,  after,  filter,  limit,  recursive,  attributes );
    }

    @Valid
    @POST
    @Path("/share-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share connections with all organizations", notes = "Share one or more federated identity provider connections with **all child organizations** according to the specified policy.  Once shared, a shadow connection is created in each matching sub-organization. Sub-organization users can then add the shared connection to their application login flows and authenticate through it.  This endpoint is treated as a **processing function** and responds with `202 Accepted`.  **Scope required:** `internal_connection_share`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Connection Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Connection share-with-all process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response shareConnectionsWithAll(@ApiParam(value = "" ,required=true) @Valid ConnectionShareWithAllRequestBody connectionShareWithAllRequestBody) {

        return delegate.shareConnectionsWithAll(connectionShareWithAllRequestBody );
    }

    @Valid
    @POST
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Share connections with specific organizations", notes = "Share one or more federated identity provider connections with a selected set of child organizations.  Once shared, a shadow connection is created in each target sub-organization. Sub-organization users can then add the shared connection to their application login flows and authenticate through it.  This endpoint is treated as a **processing function**: it triggers a sharing process and responds with `202 Accepted`.  **Scope required:** `internal_connection_share`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Connection Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Connection sharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response shareConnectionsWithSelected(@ApiParam(value = "" ,required=true) @Valid ConnectionShareSelectedRequestBody connectionShareSelectedRequestBody) {

        return delegate.shareConnectionsWithSelected(connectionShareSelectedRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare-with-all")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare connections from all organizations", notes = "Completely remove shared access for one or more federated identity provider connections from **all child organizations**.  This endpoint is treated as a **processing function** and responds with `202 Accepted`.  **Scope required:** `internal_connection_unshare`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Connection Sharing", })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Connection unshare-with-all process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response unshareConnectionsFromAll(@ApiParam(value = "" ,required=true) @Valid ConnectionUnshareWithAllRequestBody connectionUnshareWithAllRequestBody) {

        return delegate.unshareConnectionsFromAll(connectionUnshareWithAllRequestBody );
    }

    @Valid
    @POST
    @Path("/unshare")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Unshare connections from specific organizations", notes = "Unshare one or more federated identity provider connections from a selected set of child organizations.  > **Note:** > This only removes the shared connection from the specified organizations. If a connection > was shared with a parent org and its children via a broader policy, you must explicitly > include all relevant organization IDs when unsharing.  This endpoint is treated as a **processing function** and responds with `202 Accepted`.  **Scope required:** `internal_connection_unshare`", response = ProcessSuccessResponse.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Connection Sharing" })
    @ApiResponses(value = { 
        @ApiResponse(code = 202, message = "Connection unsharing process triggered successfully.", response = ProcessSuccessResponse.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    })
    public Response unshareConnectionsFromSelected(@ApiParam(value = "" ,required=true) @Valid ConnectionUnshareSelectedRequestBody connectionUnshareSelectedRequestBody) {

        return delegate.unshareConnectionsFromSelected(connectionUnshareSelectedRequestBody );
    }

}
