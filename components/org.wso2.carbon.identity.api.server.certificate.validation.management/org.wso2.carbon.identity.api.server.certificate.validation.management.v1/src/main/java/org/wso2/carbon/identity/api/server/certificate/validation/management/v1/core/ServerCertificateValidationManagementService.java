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

package org.wso2.carbon.identity.api.server.certificate.validation.management.v1.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificate;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificates;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validator;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validators;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.util.CertificateValidationMgtEndpointUtil;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.x509Certificate.validation.exception.CertificateValidationManagementException;
import org.wso2.carbon.identity.x509Certificate.validation.model.CACertificateInfo;
import org.wso2.carbon.identity.x509Certificate.validation.service.CertificateValidationManagementService;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.certificate.validation.management.v1.util.ModelMapperUtil.mapApiModelToCertificateValidatorObject;
import static org.wso2.carbon.identity.api.server.certificate.validation.management.v1.util.ModelMapperUtil.mapCACertificateToApiModel;
import static org.wso2.carbon.identity.api.server.certificate.validation.management.v1.util.ModelMapperUtil.mapCACertificatesToApiModel;
import static org.wso2.carbon.identity.api.server.certificate.validation.management.v1.util.ModelMapperUtil.mapValidatorToApiModel;
import static org.wso2.carbon.identity.api.server.certificate.validation.management.v1.util.ModelMapperUtil.mapValidatorsToApiModel;

/**
 * Call internal osgi services to perform server certificate validation management operations.
 */
public class ServerCertificateValidationManagementService {

    private static final Log LOG = LogFactory.getLog(ServerCertificateValidationManagementService.class);
    private final CertificateValidationManagementService certificateValidationManagementService;

    public ServerCertificateValidationManagementService(CertificateValidationManagementService
                                                                certificateValidationManagementService) {

        this.certificateValidationManagementService = certificateValidationManagementService;
    }

    /**
     * Get validators.
     *
     * @return Validators.
     */
    public Validators getValidators() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving certificate validators for tenant: " + tenantDomain);
        }
        try {
            List<org.wso2.carbon.identity.x509Certificate.validation.model.Validator> validators =
                    certificateValidationManagementService.getValidators(tenantDomain);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved " + (validators != null ? validators.size() : 0) + 
                         " validators for tenant: " + tenantDomain);
            }
            if (validators == null) {
                validators = new ArrayList<>();
            }
            return mapValidatorsToApiModel(validators);
        } catch (CertificateValidationManagementException e) {
            throw CertificateValidationMgtEndpointUtil.handleCertificateValidationMgtException(e);
        }
    }

    /**
     * Get the validator by name.
     *
     * @param name Name of the validator.
     * @return Validator.
     */
    public Validator getValidator(String name) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving certificate validator with name: " + name + " for tenant: " + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.x509Certificate.validation.model.Validator validator =
                    certificateValidationManagementService.getValidator(name, tenantDomain);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved validator: " + name + " for tenant: " + tenantDomain);
            }
            return mapValidatorToApiModel(validator);
        } catch (CertificateValidationManagementException e) {
            throw CertificateValidationMgtEndpointUtil.handleCertificateValidationMgtException(e);
        }
    }

    /**
     * Update the validator.
     *
     * @param validatorName Name of the validator.
     * @param validator     Validator.
     * @return Updated validator.
     */
    public Validator updateValidator(String validatorName, Validator validator) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating certificate validator: " + validatorName + " for tenant: " + tenantDomain);
        }
        try {
            org.wso2.carbon.identity.x509Certificate.validation.model.Validator updatedValidator =
                    certificateValidationManagementService
                            .updateValidator(mapApiModelToCertificateValidatorObject(validator, validatorName),
                                    tenantDomain);

            LOG.info("Successfully updated certificate validator: " + validatorName + " for tenant: " + tenantDomain);
            return mapValidatorToApiModel(updatedValidator);
        } catch (CertificateValidationManagementException e) {
            throw CertificateValidationMgtEndpointUtil.handleCertificateValidationMgtException(e);
        }
    }

    /**
     * Get CA Certificates.
     *
     * @return List of CA certificates.
     */
    public CACertificates getCACertificates() {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving CA certificates for tenant: " + tenantDomain);
        }
        try {
            List<CACertificateInfo> caCertificates = certificateValidationManagementService
                    .getCACertificates(tenantDomain);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved " + (caCertificates != null ? caCertificates.size() : 0) + 
                         " CA certificates for tenant: " + tenantDomain);
            }
            if (caCertificates == null) {
                caCertificates = new ArrayList<>();
            }
            return mapCACertificatesToApiModel(caCertificates);
        } catch (CertificateValidationManagementException e) {
            throw CertificateValidationMgtEndpointUtil.handleCertificateValidationMgtException(e);
        }
    }

    /**
     * Get CA Certificate.
     *
     * @param certificateId Certificate Id.
     * @return CA Certificate.
     */
    public CACertificate getCertificate(String certificateId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving CA certificate with ID: " + certificateId + " for tenant: " + tenantDomain);
        }
        try {
            CACertificateInfo caCertificate = certificateValidationManagementService
                    .getCACertificate(certificateId, tenantDomain);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved CA certificate: " + certificateId + " for tenant: " + tenantDomain);
            }
            return mapCACertificateToApiModel(caCertificate);
        } catch (CertificateValidationManagementException e) {
            throw CertificateValidationMgtEndpointUtil.handleCertificateValidationMgtException(e);
        }
    }

    /**
     * Add CA Certificate.
     *
     * @param caCertificate CA Certificate.
     * @return Added CA Certificate.
     */
    public CACertificate addCACertificate(String caCertificate) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding CA certificate for tenant: " + tenantDomain);
        }
        try {
            CACertificateInfo addedCACertificate = certificateValidationManagementService
                    .addCACertificate(caCertificate, tenantDomain);

            LOG.info("Successfully added CA certificate with ID: " + (addedCACertificate != null ? 
                     addedCACertificate.getCertId() : "null") + " for tenant: " + tenantDomain);
            if (addedCACertificate == null) {
                throw new CertificateValidationManagementException(
                    "Failed to retrieve added CA certificate information");
            }
            return mapCACertificateToApiModel(addedCACertificate);
        } catch (CertificateValidationManagementException e) {
            throw CertificateValidationMgtEndpointUtil.handleCertificateValidationMgtException(e);
        }
    }

    /**
     * Update CA Certificate.
     *
     * @param certificateId Certificate Id.
     * @param caCertificate CA Certificate.
     * @return Updated CA Certificate.
     */
    public CACertificate updateCACertificate(String certificateId, String caCertificate) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating CA certificate with ID: " + certificateId + " for tenant: " + tenantDomain);
        }
        try {
            CACertificateInfo updatedCACertificate = certificateValidationManagementService
                    .updateCACertificate(certificateId, caCertificate, tenantDomain);

            LOG.info("Successfully updated CA certificate: " + certificateId + " for tenant: " + tenantDomain);
            return mapCACertificateToApiModel(updatedCACertificate);
        } catch (CertificateValidationManagementException e) {
            throw CertificateValidationMgtEndpointUtil.handleCertificateValidationMgtException(e);
        }
    }

    /**
     * Delete CA Certificate.
     *
     * @param certificateId Certificate Id.
     */
    public void deleteCACertificate(String certificateId) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting CA certificate with ID: " + certificateId + " for tenant: " + tenantDomain);
        }
        try {
            certificateValidationManagementService.deleteCACertificate(certificateId, tenantDomain);
            LOG.info("Successfully deleted CA certificate: " + certificateId + " for tenant: " + tenantDomain);
        } catch (CertificateValidationManagementException e) {
            throw CertificateValidationMgtEndpointUtil.handleCertificateValidationMgtException(e);
        }
    }
}
