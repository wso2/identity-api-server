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
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.x509Certificate.validation.exception.CertificateValidationManagementClientException;
import org.wso2.carbon.identity.x509Certificate.validation.exception.CertificateValidationManagementException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.x509Certificate.validation.constant.error.ErrorMessage.ERROR_CERTIFICATE_DOES_NOT_EXIST;
import static org.wso2.carbon.identity.x509Certificate.validation.constant.error.ErrorMessage.ERROR_INVALID_VALIDATOR_NAME;
import static org.wso2.carbon.identity.x509Certificate.validation.constant.error.ErrorMessage.ERROR_NO_CA_CERTIFICATES_CONFIGURED_ON_TENANT;
import static org.wso2.carbon.identity.x509Certificate.validation.constant.error.ErrorMessage.ERROR_NO_VALIDATORS_CONFIGURED_ON_TENANT;

/**
 * Utility class for Certificate Validation Management endpoint.
 */
public class CertificateValidationMgtEndpointUtil {

    private static final Log LOG = LogFactory.getLog(CertificateValidationMgtEndpointUtil.class);
    private static final Set<String> NOT_FOUND_ERRORS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            ERROR_INVALID_VALIDATOR_NAME.getCode(),
            ERROR_CERTIFICATE_DOES_NOT_EXIST.getCode(),
            ERROR_NO_CA_CERTIFICATES_CONFIGURED_ON_TENANT.getCode(),
            ERROR_NO_VALIDATORS_CONFIGURED_ON_TENANT.getCode()
    )));

    public static APIError handleException(Response.Status status, String errorCode,
                                           String message, String description) {

        return new APIError(status, getError(errorCode, message, description));
    }

    /**
     * Handle CertificateValidationManagementException.
     *
     * @param e CertificateValidationManagementException.
     * @return APIError.
     */
    public static APIError handleCertificateValidationMgtException(CertificateValidationManagementException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof CertificateValidationManagementClientException) {
            LOG.debug(e.getMessage(), e);
            if (NOT_FOUND_ERRORS.contains(e.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            LOG.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        return handleException(status, errorCode, e.getMessage(), e.getDescription());
    }

    /**
     * Returns a generic error object.
     *
     * @param errorCode        Error code.
     * @param errorMessage     Error message.
     * @param errorDescription Error description.
     * @return A generic error with the specified details.
     */
    public static ErrorDTO getError(String errorCode, String errorMessage, String errorDescription) {

        ErrorDTO error = new ErrorDTO();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }
}
