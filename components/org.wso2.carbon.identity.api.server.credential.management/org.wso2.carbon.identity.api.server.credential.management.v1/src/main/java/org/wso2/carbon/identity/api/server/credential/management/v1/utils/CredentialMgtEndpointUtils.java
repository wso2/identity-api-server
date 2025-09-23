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

package org.wso2.carbon.identity.api.server.credential.management.v1.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtClientException;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtException;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtServerException;
import org.wso2.carbon.identity.api.server.credential.management.v1.Credential;
import org.wso2.carbon.identity.api.server.credential.management.v1.constants.CredentialMgtEndpointConstants;

import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class CredentialMgtEndpointUtils {

    private static final Log LOG = LogFactory.getLog(CredentialMgtEndpointUtils.class);

    /**
     * Handles exceptions and returns an APIError object.
     *
     * @param status      Response status.
     * @param errorCode   Error code.
     * @param message     Error message.
     * @param description Error description.
     * @return APIError object.
     */
    public static APIError handleException(Response.Status status, String errorCode,
                                           String message, String description) {

        return new APIError(status, getError(errorCode, message, description));
    }

    /**
     * Handles exceptions and returns an APIError object.
     *
     * @param e AdminCredentialMgtServerException object.
     * @return APIError object.
     */
    public static APIError handleCredentialMgtException(AdminCredentialMgtException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof AdminCredentialMgtClientException) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e.getMessage(), e);
            }
            status = Response.Status.BAD_REQUEST;
        } else {
            LOG.error(e.getMessage(), e);
        }

        String errorCode = e.getErrorCode();
        if (StringUtils.isBlank(errorCode)) {
            errorCode = CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_GET_CREDENTIALS.getCode();
        } else if (!errorCode.contains(ERROR_CODE_DELIMITER)) {
            errorCode = CredentialMgtEndpointConstants.CREDENTIAL_MGT_PREFIX + errorCode;
        }

        String description = StringUtils.isNotBlank(e.getDescription()) ? e.getDescription() : e.getMessage();
        return handleException(status, errorCode, e.getMessage(), description);
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

    /**
     * Validate the credential type.
     *
     * @param value Credential type.
     * @throws AdminCredentialMgtClientException Invalid credential type.
     */
    public static void validateCredentialType(String value) throws AdminCredentialMgtClientException {
        if (StringUtils.isBlank(value) || !CredentialTypes.fromString(value).isPresent()) {
            throw new AdminCredentialMgtClientException(
                    CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE.getCode(),
                    CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE.getMessage(),
                    CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE.getDescription());
        }
    }

    /**
     * Validate the user ID.
     *
     * @param userId User ID.
     * @throws AdminCredentialMgtClientException Invalid user ID.
     */
    public static void validateUserId(String userId) throws AdminCredentialMgtClientException {
        if (StringUtils.isBlank(userId)) {
            throw new AdminCredentialMgtClientException(
                    CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_USER_ID.getCode(),
                    CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_USER_ID.getMessage(),
                    CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_USER_ID.getDescription());
        }
    }

    /**
     * Validate the credential ID.
     *
     * @param credentialId Credential ID.
     * @throws AdminCredentialMgtClientException Invalid credential ID.
     */
    public static void validateCredentialId(String credentialId) throws AdminCredentialMgtClientException {
        if (StringUtils.isBlank(credentialId)) {
            throw new AdminCredentialMgtClientException(
                    CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_ID.getCode(),
                    CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_ID.getMessage(),
                    CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_ID.getDescription());
        }
    }

    public static List<Credential> toCredentialResponse(List<CredentialDTO> credentialDTOS) {

        if (credentialDTOS == null || credentialDTOS.isEmpty()) {
            return Collections.emptyList();
        }

        return credentialDTOS.stream()
                .map(CredentialMgtEndpointUtils::toCredential)
                .collect(Collectors.toList());
    }

    private static Credential toCredential(CredentialDTO dto) {

        Credential credential = new Credential()
                .credentialId(dto.getCredentialId())
                .displayName(dto.getDisplayName());

        if (StringUtils.isNotBlank(dto.getType())) {
            try {
                credential.setType(Credential.TypeEnum.fromValue(dto.getType()));
            } catch (IllegalArgumentException ex) {
                throw handleCredentialMgtException(new AdminCredentialMgtServerException(
                        CredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_GET_CREDENTIALS.getCode(),
                        "Unsupported credential type returned by the backend service.",
                        "Unable to map credential type: " + dto.getType(),
                        ex
                ));
            }
        }

        return credential;
    }
}
