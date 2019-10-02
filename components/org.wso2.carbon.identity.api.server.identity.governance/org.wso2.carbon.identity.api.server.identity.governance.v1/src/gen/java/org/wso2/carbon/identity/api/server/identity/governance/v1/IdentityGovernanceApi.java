package org.wso2.carbon.identity.api.server.identity.governance.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoriesRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.CategoryRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorRes;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.ConnectorsPatchReq;
import org.wso2.carbon.identity.api.server.identity.governance.v1.model.Error;
import org.wso2.carbon.identity.api.server.identity.governance.v1.IdentityGovernanceApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;
import java.io.InputStream;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
@Path("/identity-governance")

@Api(description = "the identity-governance API")




public class IdentityGovernanceApi  {

  @Autowired
  private IdentityGovernanceApiService delegate;

    @Valid
    @GET
    @Path("/")
    
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve governance connector categories.", notes = "Retrieve governance connector categories.  <b>Permission required:</b>   * /permission/admin/manage ", response = CategoriesRes.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Governance connector categories.", response = CategoriesRes.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class) })
    public Response getCategories(  @Valid
@ApiParam(value = "maximum number of records to return")  @QueryParam("limit") Integer limit,   @Valid
@ApiParam(value = "number of records to skip for pagination")  @QueryParam("offset") Integer offset,   @Valid
@ApiParam(value = "Condition to filter the retrival of records.")  @QueryParam("filter") String filter,   @Valid
@ApiParam(value = "Define the order how the retrieved records should be sorted.")  @QueryParam("sort") String sort) {
        return delegate.getCategories(limit,  offset,  filter,  sort );
    }
    @Valid
    @GET
    @Path("/{category-id}/connectors/{connector-id}")
    
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve governance connector.", notes = "Retrieve governance connector.  <b>Permission required:</b>   * /permission/admin/manage ", response = ConnectorRes.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Requested governance connector.", response = ConnectorRes.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class) })
    public Response getConnector(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId, @ApiParam(value = "Id of the connector.",required=true) @PathParam("connector-id") String connectorId) {
        return delegate.getConnector(categoryId,  connectorId );
    }
    @Valid
    @GET
    @Path("/{category-id}")
    
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve governance connectors of a category.", notes = "Retrieve governance connectors of a category.  <b>Permission required:</b>   * /permission/admin/manage ", response = CategoryRes.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Requested governance connector category.", response = CategoryRes.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class) })
    public Response getConnectorCategory(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId) {
        return delegate.getConnectorCategory(categoryId );
    }
    @Valid
    @GET
    @Path("/{category-id}/connectors")
    
    @Produces({ "application/json", "*/*" })
    @ApiOperation(value = "Retrieve governance connectors of a category.", notes = "Retrieve governance connectors of a category.  <b>Permission required:</b>   * /permission/admin/manage ", response = ConnectorRes.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Requested governance connector category.", response = ConnectorRes.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class) })
    public Response getConnectorsOfCategory(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId) {
        return delegate.getConnectorsOfCategory(categoryId );
    }
    @Valid
    @PATCH
    @Path("/{category-id}/connectors/{connector-id}")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    @ApiOperation(value = "Patch governance connector.", notes = "Patch governance connector.  <b>Permission required:</b>   * /permission/admin/manage ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Management" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class) })
    public Response patchConnector(@ApiParam(value = "Id of the connector category.",required=true) @PathParam("category-id") String categoryId, @ApiParam(value = "Id of the connector.",required=true) @PathParam("connector-id") String connectorId, @ApiParam(value = "governance-connector to update" ) @Valid ConnectorsPatchReq connectorsPatchReq) {
        return delegate.patchConnector(categoryId,  connectorId,  connectorsPatchReq );
    }
}
