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

package org.wso2.carbon.identity.api.server.certificate.validation.management.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificate;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.CACertificates;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validator;
import org.wso2.carbon.identity.api.server.certificate.validation.management.v1.model.Validators;
import org.wso2.carbon.identity.x509Certificate.validation.model.CACertificateInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for Certificate Validation Management Model Mapping.
 */
public class ModelMapperUtil {

    private static final Log LOG = LogFactory.getLog(ModelMapperUtil.class);

    private ModelMapperUtil() {

    }

    /**
     * Map Validator model to API model.
     *
     * @param validators List of validators.
     * @return List of API validators.
     */
    public static Validators mapValidatorsToApiModel
    (List<org.wso2.carbon.identity.x509Certificate.validation.model.Validator> validators) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Mapping " + (validators != null ? validators.size() : 0) + " validators to API model");
        }
        List<String> validatorList = validators.stream()
                .map(org.wso2.carbon.identity.x509Certificate.validation.model.Validator::getDisplayName)
                .collect(Collectors.toList());
        return new Validators().validators(validatorList);
    }

    /**
     * Map Validator model to API model.
     *
     * @param validator Validator.
     * @return API validator.
     */
    public static Validator mapValidatorToApiModel
    (org.wso2.carbon.identity.x509Certificate.validation.model.Validator validator) {

        Validator apiValidator = new Validator();
        apiValidator.setEnable(validator.isEnabled());
        apiValidator.setFullChainValidation(validator.isFullChainValidationEnabled());
        apiValidator.setPriority(validator.getPriority());
        apiValidator.setRetryCount(validator.getRetryCount());
        return apiValidator;
    }

    /**
     * Map API model to Certificate Validator object.
     *
     * @param validator API Validator.
     * @return Certificate Validator object.
     */
    public static org.wso2.carbon.identity.x509Certificate.validation.model.Validator
    mapApiModelToCertificateValidatorObject(Validator validator, String validatorName) {

        org.wso2.carbon.identity.x509Certificate.validation.model.Validator certificateValidator =
                new org.wso2.carbon.identity.x509Certificate.validation.model.Validator();
        certificateValidator.setDisplayName(validatorName);
        certificateValidator.setEnabled(validator.getEnable());
        certificateValidator.setFullChainValidationEnabled(validator.getFullChainValidation());
        certificateValidator.setPriority(validator.getPriority());
        certificateValidator.setRetryCount(validator.getRetryCount());
        return certificateValidator;
    }

    /**
     * Map CA Certificates model to API model.
     *
     * @param caCertificateInfoList List of CA Certificates.
     * @return API CA Certificates.
     */
    public static CACertificates mapCACertificatesToApiModel(List<CACertificateInfo> caCertificateInfoList) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Mapping " + (caCertificateInfoList != null ? caCertificateInfoList.size() : 0) +
                    " CA certificates to API model");
        }
        List<CACertificate> caCertificates = caCertificateInfoList.stream()
                .map(ModelMapperUtil::mapCACertificateToApiModel)
                .collect(Collectors.toList());
        return new CACertificates().certificates(caCertificates);
    }

    /**
     * Map CA Certificate model to API model.
     *
     * @param caCertificateInfo CA Certificate.
     * @return API CA Certificate.
     */
    public static CACertificate mapCACertificateToApiModel(CACertificateInfo caCertificateInfo) {

        CACertificate caCertificate = new CACertificate();
        caCertificate.setId(caCertificateInfo.getCertId());
        caCertificate.setCrlUrls(caCertificateInfo.getCrlUrls());
        caCertificate.setIssuerDN(caCertificateInfo.getIssuerDN());
        caCertificate.setSerialNumber(caCertificateInfo.getSerialNumber());
        caCertificate.setOcspUrls(caCertificateInfo.getOcspUrls());
        return caCertificate;
    }
}
