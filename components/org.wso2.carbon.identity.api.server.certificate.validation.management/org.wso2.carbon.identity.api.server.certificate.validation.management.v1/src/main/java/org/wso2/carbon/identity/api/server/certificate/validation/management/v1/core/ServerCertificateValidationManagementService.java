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

import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificate;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificates;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validator;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validators;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.util.CertificateValidationMgtEndpointUtil;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.x509Certificate.validation.exception.CertificateValidationManagementException;
import org.wso2.carbon.identity.x509Certificate.validation.model.CACertificateInfo;
import org.wso2.carbon.identity.x509Certificate.validation.service.CertificateValidationManagementService;

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

    private final CertificateValidationManagementService certificateValidationManagementService;

    public ServerCertificateValidationManagementService(CertificateValidationManagementService
                                                                certificateValidationManagementService) {

        this.certificateValidationManagementService = certificateValidationManagementService;
    }

    /**
     * Get the logger instance.
     */
    public Validators getValidators() {

        try {
            List<org.wso2.carbon.identity.x509Certificate.validation.model.Validator> validators =
                    certificateValidationManagementService.getValidators(ContextLoader.getTenantDomainFromContext());

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

        try {
            org.wso2.carbon.identity.x509Certificate.validation.model.Validator validator =
                    certificateValidationManagementService
                            .getValidator(name, ContextLoader.getTenantDomainFromContext());

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

        try {
            org.wso2.carbon.identity.x509Certificate.validation.model.Validator updatedValidator =
                    certificateValidationManagementService
                            .updateValidator(mapApiModelToCertificateValidatorObject(validator, validatorName),
                                    ContextLoader.getTenantDomainFromContext());

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

        try {
            List<CACertificateInfo> caCertificates = certificateValidationManagementService
                    .getCACertificates(ContextLoader.getTenantDomainFromContext());

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

        try {
            CACertificateInfo caCertificate = certificateValidationManagementService
                    .getCACertificate(certificateId, ContextLoader.getTenantDomainFromContext());

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

        try {
            CACertificateInfo addedCACertificate = certificateValidationManagementService
                    .addCACertificate(caCertificate,
                            ContextLoader.getTenantDomainFromContext());

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

        try {
            CACertificateInfo updatedCACertificate = certificateValidationManagementService
                    .updateCACertificate(certificateId, caCertificate,
                            ContextLoader.getTenantDomainFromContext());

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

        try {
            certificateValidationManagementService.deleteCACertificate(certificateId,
                    ContextLoader.getTenantDomainFromContext());
        } catch (CertificateValidationManagementException e) {
            throw CertificateValidationMgtEndpointUtil.handleCertificateValidationMgtException(e);
        }
    }
}
