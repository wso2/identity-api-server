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

package org.wso2.carbon.identity.api.server.keystore.management.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.wso2.carbon.identity.api.server.keystore.management.v1.model.CertificateRequest;
import org.wso2.carbon.identity.api.server.keystore.management.v1.model.CertificateResponse;
import org.wso2.carbon.identity.api.server.keystore.management.v1.model.ErrorResponse;
import java.io.File;
import org.wso2.carbon.identity.api.server.keystore.management.v1.KeystoresApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/keystores")
@Api(description = "The keystores API")

public class KeystoresApi  {

    @Autowired
    private KeystoresApiService delegate;

    @Valid
    @DELETE
    @Path("/certs/{alias}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "deletes the certificate", notes = "This REST API can be used to delete the certificate with the given alias from the tenant keystore. This API is not supported for super tenant.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Certificates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Successfully Deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response deleteCertificate(@ApiParam(value = "alias of the certificate",required=true) @PathParam("alias") String alias) {

        return delegate.deleteCertificate(alias );
    }

    @Valid
    @GET
    @Path("/certs/{alias}")
    
    @Produces({ "application/pkix-cert", "application/json" })
    @ApiOperation(value = "retrieves the certificate of the provided alias", notes = "This REST API can be used to download the certificate from the keystore", response = File.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Certificates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = File.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response getCertificate(@ApiParam(value = "alias of the certificate",required=true) @PathParam("alias") String alias,     @Valid@ApiParam(value = "")  @QueryParam("encode-cert") Boolean encodeCert) {

        return delegate.getCertificate(alias,  encodeCert );
    }

    @Valid
    @GET
    @Path("/certs")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "retrieves the list of certificate aliases", notes = "This REST API can be used to get the certificate aliases from the keystore", response = CertificateResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Certificates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = CertificateResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response getCertificateAliases(    @Valid@ApiParam(value = "Condition to filter the retrival of records. Supports 'sw', 'co', 'ew' and 'eq' operations. E.g. keystores/certs?filter=alias+eq+wso2carbon")  @QueryParam("filter") String filter) {

        return delegate.getCertificateAliases(filter );
    }

    @Valid
    @GET
    @Path("/client-certs/{alias}")
    
    @Produces({ "application/pkix-cert", "application/json" })
    @ApiOperation(value = "retrieves the certificate of the provided alias", notes = "This REST API can be used to download the certificate of specified alias from the client-truststore", response = File.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Certificates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = File.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response getClientCertificate(@ApiParam(value = "alias of the certificate",required=true) @PathParam("alias") String alias,     @Valid@ApiParam(value = "")  @QueryParam("encode-cert") Boolean encodeCert) {

        return delegate.getClientCertificate(alias,  encodeCert );
    }

    @Valid
    @GET
    @Path("/client-certs")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "retrieves the list of certificate aliases from the client truststore", notes = "This REST API can be used to get the list of certificate aliases from the client truststore", response = CertificateResponse.class, responseContainer = "List", authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Certificates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = CertificateResponse.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response getClientCertificateAliases(    @Valid@ApiParam(value = "Condition to filter the retrival of records. Supports 'sw', 'co', 'ew' and 'eq' operations. E.g. keystores/certs?filter=alias+eq+wso2carbon")  @QueryParam("filter") String filter) {

        return delegate.getClientCertificateAliases(filter );
    }

    @Valid
    @GET
    @Path("/certs/public")
    
    @Produces({ "application/pkix-cert", "application/json" })
    @ApiOperation(value = "retrieves the public certificate", notes = "This REST API can be used to download the public certificate from the keystore", response = File.class, tags={ "Certificates", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK.", response = File.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response getPublicCertificate(    @Valid@ApiParam(value = "")  @QueryParam("encode-cert") Boolean encodeCert) {

        return delegate.getPublicCertificate(encodeCert );
    }

    @Valid
    @POST
    @Path("/certs")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "uploads the certificate with the given alias", notes = "This REST API can be used to upload the certifate to the tenant keystore. This API is not supported for super tenant.", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {
            
        })
    }, tags={ "Certificates" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request.", response = ErrorResponse.class),
        @ApiResponse(code = 401, message = "Unauthorized.", response = ErrorResponse.class),
        @ApiResponse(code = 403, message = "Resource Forbidden.", response = ErrorResponse.class),
        @ApiResponse(code = 404, message = "Resource Not Found.", response = ErrorResponse.class),
        @ApiResponse(code = 405, message = "Method Not Allowed.", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorResponse.class)
    })
    public Response uploadCertificate(@ApiParam(value = "" ) @Valid CertificateRequest certificateRequest) {

        return delegate.uploadCertificate(certificateRequest );
    }

}
