/*
* Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.keystore.management.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.keystore.management.v2.model.AddPrivateKeyRequest;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.CertificateData;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.ErrorResponse;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.KeysData;
import org.wso2.carbon.identity.api.server.keystore.management.v2.KeystoreApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/keystore")
@Api(description = "The keystore API")

/*
  REST API for managing keystore related functionalities.
 */
public class KeystoreApi  {

    @Autowired
    private KeystoreApiService delegate;

    @Valid
    @DELETE
    @Path("/keys/{alias}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Deletes the privatekey", notes = "This REST API can be used to delete the private key with the given alias from the tenant keystore. This API is not supported for super tenant. <br> <b>Permission required:</b> <br>   * /permission/admin/manage/identity/keystoremgt/update <br> <b>Scope required:</b> <br>   * internal_keystore_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Private Keys", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Void.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response deletePrivateKey(@ApiParam(value = "alias of the certificate",required=true) @PathParam("alias") String alias) {

        return delegate.deletePrivateKey(alias );
    }

    @Valid
    @GET
    @Path("/keys/{alias}/certificate")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the certificate details of the provided alias of a private key", notes = "This REST API can be used to get the certificate details of the private key of specified alias from the keystore. <br> <b>Permission required:</b> <br>   * /permission/admin/manage/identity/keystoremgt/view <br> <b>Scope required:</b> <br>   * internal_keystore_view ", response = CertificateData.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Private Keys", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = CertificateData.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response getPrivateKey(@ApiParam(value = "alias of the certificate",required=true) @PathParam("alias") String alias) {

        return delegate.getPrivateKey(alias );
    }

    @Valid
    @GET
    @Path("/keys")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Retrieves the list of private keys data", notes = "This REST API can be used to get the private keys data from the keystore.<br> <b>Permission required:</b> <br>   * /permission/admin/manage/identity/keystoremgt/view <br> <b>Scope required:</b> <br>   * internal_keystore_view ", response = KeysData.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Keys", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = KeysData.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response getPrivateKeyAliases(    @Valid@ApiParam(value = "Condition to filter the retrieval of records. Supports 'sw', 'co', 'ew' and 'eq' operations. E.g. keystore/keys?filter=alias+eq+wso2carbon")  @QueryParam("filter") String filter) {

        return delegate.getPrivateKeyAliases(filter );
    }

    @Valid
    @POST
    @Path("/keys")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Uploads the certificate with the given alias", notes = "This REST API can be used to upload the private key to the tenant keystore. This API is not supported for super tenant. <br> <b>Permission required:</b> <br>   * /permission/admin/manage/identity/keystoremgt/update <br> <b>Scope required:</b> <br>   * internal_keystore_update ", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Private Keys" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = Void.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response uploadPrivateKey(@ApiParam(value = "" ) @Valid AddPrivateKeyRequest addPrivateKeyRequest) {

        return delegate.uploadPrivateKey(addPrivateKeyRequest );
    }

}
