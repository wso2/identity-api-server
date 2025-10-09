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

package org.wso2.carbon.identity.api.server.user.credential.management.v1.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.exception.CredentialMgtClientException;
import org.wso2.carbon.identity.api.server.user.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.user.credential.management.common.exception.CredentialMgtServerException;
import org.wso2.carbon.identity.api.server.user.credential.management.v1.UserCredential;
import org.wso2.carbon.identity.api.server.user.credential.management.v1.constants.UserCredentialMgtEndpointConstants;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 * Utility class of UserCredential Management Endpoint.
 */
public class UserCredentialMgtEndpointUtils {

    private static final Log LOG = LogFactory.getLog(UserCredentialMgtEndpointUtils.class);

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
     * @param e CredentialMgtServerException object.
     * @return APIError object.
     */
    public static APIError handleCredentialMgtException(CredentialMgtException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof CredentialMgtClientException) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e.getMessage(), e);
            }
            if (StringUtils.equals(e.getErrorCode(),
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_USER_NOT_FOUND.getCode())) {
                status = Response.Status.NO_CONTENT;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            LOG.error(e.getMessage(), e);
        }

        String errorCode = e.getErrorCode();
        if (StringUtils.isBlank(errorCode)) {
            errorCode = UserCredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_GET_CREDENTIALS.getCode();
        } else if (!errorCode.contains(UserCredentialMgtEndpointConstants.ERROR_CODE_DELIMITER)) {
            errorCode = UserCredentialMgtEndpointConstants.CREDENTIAL_MGT_PREFIX + errorCode;
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
     * @param value UserCredential type.
     * @throws CredentialMgtClientException Invalid credential type.
     */
    public static void validateCredentialType(String value) throws CredentialMgtClientException {

        if (!CredentialTypes.fromString(value).isPresent()) {
            throw new CredentialMgtClientException(UserCredentialManagementConstants
                    .ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_TYPE);
        }
    }

    /**
     * Validate the credential ID.
     *
     * @param credentialId UserCredential ID.
     * @throws CredentialMgtClientException Invalid credential ID.
     */
    public static void validateCredentialId(String credentialId) throws CredentialMgtClientException {

        if (StringUtils.isBlank(credentialId)) {
            throw new CredentialMgtClientException(UserCredentialManagementConstants
                    .ErrorMessages.ERROR_CODE_INVALID_CREDENTIAL_ID);
        }
    }

    /**
     * Convert a list of CredentialDTO to a list of UserCredential response objects.
     *
     * @param credentialDTOS List of CredentialDTO objects.
     * @return List of UserCredential response objects.
     */
    public static List<UserCredential> toCredentialResponse(List<UserCredentialDTO> credentialDTOS) {

        if (credentialDTOS == null || credentialDTOS.isEmpty()) {
            return Collections.emptyList();
        }

        return credentialDTOS.stream()
            .map(UserCredentialMgtEndpointUtils::toCredential)
            .collect(Collectors.toList());
    }

    /**
     * Convert a CredentialDTO to a UserCredential response object.
     *
     * @param dto CredentialDTO object.
     * @return UserCredential response object.
     */
    private static UserCredential toCredential(UserCredentialDTO dto) {

        UserCredential credential = new UserCredential()
            .credentialId(dto.getCredentialId())
            .displayName(dto.getDisplayName());

        if (StringUtils.isNotBlank(dto.getType())) {
            try {
                credential.setType(UserCredential.TypeEnum.fromValue(dto.getType()));
            } catch (IllegalArgumentException ex) {
                throw handleCredentialMgtException(new CredentialMgtServerException(
                        UserCredentialMgtEndpointConstants.ErrorMessages.ERROR_CODE_GET_CREDENTIALS.getCode(),
                        "Unsupported credential type returned by the backend service.",
                        "Unable to map credential type: " + dto.getType(), ex
                ));
            }
        }

        return credential;
    }
}
