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

package org.wso2.carbon.identity.api.server.certificate.validation.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.CertificateValidationApiService;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.core.ServerCertificateValidationManagementService;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.factories.ServerCertificateValidationManagementServiceFactory;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificate;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificateAddRequest;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificateUpdateRequest;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validator;
import org.wso2.carbon.identity.api.server.common.ContextLoader;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.certificate.validation.management.v1.constants.CertificateValidationMgtEndpointConstants.CERTIFICATE_MANAGEMENT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.certificate.validation.management.v1.constants.CertificateValidationMgtEndpointConstants.CERTIFICATE_VALIDATION_MANAGEMENT_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.certificate.validation.management.v1.constants.CertificateValidationMgtEndpointConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;

/**
 * Certificate Validation API service implementation.
 */
public class CertificateValidationApiServiceImpl implements CertificateValidationApiService {

    private static final Log LOG = LogFactory.getLog(CertificateValidationApiServiceImpl.class);
    private final ServerCertificateValidationManagementService certificateValidationManagementService;

    public CertificateValidationApiServiceImpl() {

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initializing CertificateValidationApiServiceImpl");
            }
            certificateValidationManagementService = ServerCertificateValidationManagementServiceFactory
                    .getServerCertificateValidationManagementService();
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating the certificate validation management service", e);
            throw new RuntimeException("Error occurred while initiating the certificate validation management service.",
                    e);
        }
    }

    @Override
    public Response addCACertificate(CACertificateAddRequest caCertificateAddRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Add CA certificate API endpoint called");
        }
        CACertificate caCertificate = certificateValidationManagementService
                .addCACertificate(caCertificateAddRequest.getCertificate());
        URI location = ContextLoader.buildURIForHeader(V1_API_PATH_COMPONENT +
                CERTIFICATE_VALIDATION_MANAGEMENT_PATH_COMPONENT + CERTIFICATE_MANAGEMENT_PATH_COMPONENT +
                PATH_SEPARATOR + caCertificate.getId());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully processed add CA certificate request");
        }
        return Response.created(location).entity(caCertificate).build();
    }

    @Override
    public Response deleteCACertificateById(String certificateId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Delete CA certificate API endpoint called for certificateId: " + certificateId);
        }
        certificateValidationManagementService.deleteCACertificate(certificateId);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully processed delete CA certificate request for certificateId: " + certificateId);
        }
        return Response.noContent().build();
    }

    @Override
    public Response getCACertificateById(String certificateId) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get CA certificate API endpoint called for certificateId: " + certificateId);
        }
        return Response.ok().entity(certificateValidationManagementService.getCertificate(certificateId)).build();
    }

    @Override
    public Response getCACertificates() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get CA certificates API endpoint called");
        }
        return Response.ok().entity(certificateValidationManagementService.getCACertificates()).build();
    }

    @Override
    public Response getCertificateRevocationValidator(String validatorName) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get certificate revocation validator API endpoint called for validatorName: " + validatorName);
        }
        return Response.ok().entity(certificateValidationManagementService.getValidator(validatorName)).build();
    }

    @Override
    public Response getCertificateRevocationValidators() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Get certificate revocation validators API endpoint called");
        }
        return Response.ok().entity(certificateValidationManagementService.getValidators()).build();
    }

    @Override
    public Response updateCACertificateById(String certificateId,
                                            CACertificateUpdateRequest caCertificateUpdateRequest) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Update CA certificate API endpoint called for certificateId: " + certificateId);
        }
        return Response.ok().entity(certificateValidationManagementService.updateCACertificate(certificateId,
                caCertificateUpdateRequest.getCertificate())).build();
    }

    @Override
    public Response updateCertificateRevocationValidator(String validatorName, Validator validator) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Update certificate revocation validator API endpoint called for validatorName: " + 
                     validatorName);
        }
        return Response.ok().entity(certificateValidationManagementService
                .updateValidator(validatorName, validator)).build();
    }
}
