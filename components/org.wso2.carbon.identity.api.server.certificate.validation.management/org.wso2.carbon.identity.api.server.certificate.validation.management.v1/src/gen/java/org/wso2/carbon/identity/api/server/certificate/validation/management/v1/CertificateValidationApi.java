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

package org.wso2.carbon.identity.api.server.certificate.validation.management.v1;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import java.io.InputStream;
import java.util.List;

import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.factories.CertificateValidationApiServiceFactory;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificate;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificateAddRequest;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificateUpdateRequest;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificates;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Error;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validator;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validators;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.CertificateValidationApiService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import io.swagger.annotations.*;

import javax.validation.constraints.*;

@Path("/certificate-validation")
@Api(description = "The certificate-validation API")

public class CertificateValidationApi  {

    private final CertificateValidationApiService delegate;

    public CertificateValidationApi() {
        this.delegate = CertificateValidationApiServiceFactory.getCertificateValidationApi();
    }

    @Valid
    @POST
    @Path("/ca")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a new ca certificate", notes = "Add a new ca certificate \\n\\n <b>Scope(Permission) required:</b> `internal_cert_validation_mgt_create` \\n\\n", response = CACertificate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Certificate Authority Certificates", })
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Certificate added successfully", response = CACertificate.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response addCACertificate(@ApiParam(value = "" ,required=true) @Valid CACertificateAddRequest caCertificateAddRequest) {

        return delegate.addCACertificate(caCertificateAddRequest );
    }

    @Valid
    @DELETE
    @Path("/ca/{certificate-id}")

    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a ca certificate by id", notes = "Delete ca certificate specified by the certificate id \\n\\n <b>Scope(Permission) required:</b> `internal_cert_validation_mgt_delete` \\n\\n", response = Void.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Certificate Authority Certificates", })
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully Deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response deleteCACertificateById(@ApiParam(value = "",required=true) @PathParam("certificate-id") String certificateId) {

        return delegate.deleteCACertificateById(certificateId );
    }

    @Valid
    @GET
    @Path("/ca/{certificate-id}")

    @Produces({ "application/json" })
    @ApiOperation(value = "Get a ca certificate by certificate id", notes = "Get ca certificate specified by the certificate id \\n\\n <b>Scope(Permission) required:</b> `internal_cert_validation_mgt_view` \\n\\n", response = CACertificate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Certificate Authority Certificates", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Certificate details", response = CACertificate.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCACertificateById(@ApiParam(value = "",required=true) @PathParam("certificate-id") String certificateId) {

        return delegate.getCACertificateById(certificateId );
    }

    @Valid
    @GET
    @Path("/ca")

    @Produces({ "application/json" })
    @ApiOperation(value = "Get all ca certificates", notes = "List all ca certificates \\n\\n <b>Scope(Permission) required:</b> `internal_cert_validation_mgt_view` \\n\\n", response = CACertificates.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Certificate Authority Certificates", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful response", response = CACertificates.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCACertificates() {

        return delegate.getCACertificates();
    }

    @Valid
    @GET
    @Path("/revocation-validators/{validatorName}")

    @Produces({ "application/json" })
    @ApiOperation(value = "Get a specific certificate validator configurations", notes = "Get certificate validator configuration specified by the name \\n\\n <b>Scope(Permission) required:</b> `internal_cert_validation_mgt_view` \\n\\n", response = Validator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Certificate Revocation Validators", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Validator details", response = Validator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response getCertificateRevocationValidator(@ApiParam(value = "",required=true) @PathParam("validatorName") String validatorName) {

        return delegate.getCertificateRevocationValidator(validatorName );
    }

    @Valid
    @GET
    @Path("/revocation-validators")

    @Produces({ "application/json" })
    @ApiOperation(value = "Get all certificate validator configurations", notes = "List all certificate validator configurations \\n\\n <b>Scope(Permission) required:</b> `internal_cert_validation_mgt_view` \\n\\n", response = Validators.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Certificate Revocation Validators", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful response", response = Validators.class),
        @ApiResponse(code = 400, message = "Invalid input in the request.", response = Error.class),
        @ApiResponse(code = 401, message = "Authentication information is missing or invalid.", response = Void.class),
        @ApiResponse(code = 403, message = "Access forbidden.", response = Void.class),
        @ApiResponse(code = 404, message = "Requested resource is not found.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal server error.", response = Error.class),
        @ApiResponse(code = 501, message = "Not Implemented.", response = Error.class)
    })
    public Response getCertificateRevocationValidators() {

        return delegate.getCertificateRevocationValidators();
    }

    @Valid
    @PUT
    @Path("/ca/{certificate-id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update a ca certificate by certificate id", notes = "Patch ca certificate specified by the certificate id. \\n\\n <b>Scope(Permission) required:</b> `internal_cert_validation_mgt_update` \\n\\n", response = CACertificate.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Certificate Authority Certificates", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Certificate updated successfully", response = CACertificate.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateCACertificateById(@ApiParam(value = "",required=true) @PathParam("certificate-id") String certificateId, @ApiParam(value = "" ,required=true) @Valid CACertificateUpdateRequest caCertificateUpdateRequest) {

        return delegate.updateCACertificateById(certificateId,  caCertificateUpdateRequest );
    }

    @Valid
    @PUT
    @Path("/revocation-validators/{validatorName}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Update the certificate validator configurations", notes = "Patch certificate validator specified by the name. \\n\\n <b>Scope(Permission) required:</b> `internal_cert_validation_mgt_update` \\n\\n", response = Validator.class, authorizations = {
        @Authorization(value = "BasicAuth"),
        @Authorization(value = "OAuth2", scopes = {

        })
    }, tags={ "Certificate Revocation Validators" })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Validator updated successfully", response = Validator.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden", response = Void.class),
        @ApiResponse(code = 500, message = "Server Error", response = Error.class)
    })
    public Response updateCertificateRevocationValidator(@ApiParam(value = "",required=true) @PathParam("validatorName") String validatorName, @ApiParam(value = "" ,required=true) @Valid Validator validator) {

        return delegate.updateCertificateRevocationValidator(validatorName,  validator );
    }

}
