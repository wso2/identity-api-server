/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.secret.management.v1.core;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.util.CryptoException;
import org.wso2.carbon.core.util.CryptoUtil;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants;
import org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementServiceHolder;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.Secret;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretAdd;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretUpdateRequest;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementClientException;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementException;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementServerException;
import org.wso2.carbon.identity.secret.mgt.core.model.Secrets;

import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.CONFIG_MGT_ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants.ERROR_PREFIX;

/**
 * Invoke internal OSGi service to perform secret management operations.
 */
public class SecretManagementService {

    private static final Log log = LogFactory.getLog(SecretManagementService.class);

    /**
     * Create a secret.
     *
     * @param secretAdd secret post request.
     * @return secret.
     */
    public Secret addSecret(SecretAdd secretAdd) {

        validateSecretAdd(secretAdd);
        org.wso2.carbon.identity.secret.mgt.core.model.Secret requestDTO, responseDTO;
        try {
            requestDTO = buildSecretRequestDTOFromSecretAdd(secretAdd);
            responseDTO = SecretManagementServiceHolder.getSecretConfigManager().addSecret(requestDTO);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_SECRET,
                    secretAdd.getName());
        }
        return buildSecretFromResponseDTO(responseDTO);
    }

    /**
     * To create Secret Response object for the post request
     *
     * @param secretReq secret object.
     * @return {@link Secret} .
     */
    private Secret buildSecretFromResponseDTO(org.wso2.carbon.identity.secret.mgt.core.model.Secret secretReq) {

        Secret secret = new Secret();
        secret.secretName(secretReq.getSecretName());
        secret.setCreated(secretReq.getCreatedTime());
        secret.setLastModified(secretReq.getLastModified());
        secret.setSecretId(secretReq.getSecretId());
        return secret;
    }

    /**
     * Validate the secret post request.
     *
     * @param secretAdd secret post request.
     */
    private void validateSecretAdd(SecretAdd secretAdd) {

        String secretAddName = secretAdd.getName();
        if (StringUtils.isBlank(secretAddName)) {
            throw handleException(Response.Status.BAD_REQUEST, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_REFERENCE_NAME_NOT_SPECIFIED, null);
        }
        if (StringUtils.isBlank(secretAdd.getValue())) {
            throw handleException(Response.Status.BAD_REQUEST, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_SECRET_VALUE_NOT_SPECIFIED, null);
        }
        try {
            org.wso2.carbon.identity.secret.mgt.core.model.Secret secret =
                    SecretManagementServiceHolder.getSecretConfigManager().getSecret(secretAddName);
            if (secret != null) {
                throw handleException(Response.Status.CONFLICT, SecretManagementConstants.ErrorMessage.
                        ERROR_CODE_CONFLICT_SECRET, secretAddName);
            }
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_GETTING_SECRET, secretAddName);
        }
    }

    /**
     * Build secret requestDTO by secret body request.
     *
     * @param secretAdd secret post body.
     * @return Secret requestDTO object.
     */
    private org.wso2.carbon.identity.secret.mgt.core.model.Secret buildSecretRequestDTOFromSecretAdd
    (SecretAdd secretAdd) {

        org.wso2.carbon.identity.secret.mgt.core.model.Secret requestDTO = new org.wso2.carbon.identity.secret.mgt.core.
                model.Secret();
        requestDTO.setSecretName(secretAdd.getName());
        requestDTO.setValue(getEncryptedSecret(secretAdd.getValue()));
        return requestDTO;
    }

    /**
     * Delete a secret sender by name.
     *
     * @param name Name of the secret.
     */
    public void deleteSecret(String name) {

        try {
            SecretManagementServiceHolder.getSecretConfigManager().deleteSecret(name);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_DELETING_SECRET, name);
        }
    }

    /**
     * Retrieve the secret details by name.
     *
     * @param name secret name.
     * @return secret.
     */
    public Secret getSecret(String name) {

        try {
            org.wso2.carbon.identity.secret.mgt.core.model.Secret responseDTO = SecretManagementServiceHolder.
                    getSecretConfigManager().getSecret(name);
            return buildSecretFromResponseDTO(responseDTO);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_GETTING_SECRET, name);
        }
    }

    /**
     * Retrieve all the secrets of the tenant.
     *
     * @return secrets of the tenant.
     */

    public List<Secret> getSecretsList() {

        try {
            Secrets secrets = SecretManagementServiceHolder.getSecretConfigManager()
                    .getSecrets();
            List<org.wso2.carbon.identity.secret.mgt.core.model.Secret> secretsList =
                    secrets.getSecrets();
            return secretsList.stream().map(secret ->
                    buildSecretFromResponseDTO(secret)).collect(Collectors.toList());
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_GETTING_SECRETS, null);
        }
    }

    /**
     * Update secret details by name.
     *
     * @param name                secret name.
     * @param secretUpdateRequest secret's updated details.
     * @return Updated secret.
     */
    public Secret updateSecret(String name, SecretUpdateRequest secretUpdateRequest) {

        org.wso2.carbon.identity.secret.mgt.core.model.Secret requestDTO, responseDTO;
        SecretAdd secretAdd =
                buildSecretAddFromSecretUpdateRequest(name, secretUpdateRequest);
        try {
            requestDTO = buildSecretRequestDTOFromSecretAdd(secretAdd);
            responseDTO = SecretManagementServiceHolder.getSecretConfigManager().replaceSecret(requestDTO);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SECRET,
                    name);
        }
        return buildSecretFromResponseDTO(responseDTO);
    }

    /**
     * Build secretAdd object from secret update request.
     *
     * @param name                secret name.
     * @param secretUpdateRequest secret's update request body.
     * @return secretAdd object
     */
    private SecretAdd buildSecretAddFromSecretUpdateRequest(String name,
                                                            SecretUpdateRequest secretUpdateRequest) {

        SecretAdd secretAdd = new SecretAdd();
        secretAdd.setName(name);
        secretAdd.setValue(secretUpdateRequest.getValue());
        return secretAdd;
    }

    private APIError handleSecretMgtException(SecretManagementException e, SecretManagementConstants.ErrorMessage
            errorEnum, String data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
        Response.Status status;
        if (e instanceof SecretManagementClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(CONFIG_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof SecretManagementServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(CONFIG_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
        }
        return new APIError(status, errorResponse);
    }

    /**
     * Handle exceptions generated in API.
     *
     * @param status HTTP Status.
     * @param error  Error Message information.
     * @return APIError.
     */
    private APIError handleException(Response.Status status, SecretManagementConstants.ErrorMessage error,
                                     String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(SecretManagementConstants.ErrorMessage errorMsg,
                                                  String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Error message.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(SecretManagementConstants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getDescription(), data);
        } else {
            message = error.getDescription();
        }
        return message;
    }

    private String getEncryptedSecret(String clientSecret) {

        try {
            return encrypt(clientSecret);
        } catch (CryptoException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, SecretManagementConstants.ErrorMessage.
                            ERROR_CODE_ERROR_ADDING_SECRET,
                    null);
        }
    }

    /**
     * Encrypt secret.
     *
     * @param plainText plain text secret.
     * @return encrypted secret.
     */
    private String encrypt(String plainText) throws CryptoException {

        return CryptoUtil.getDefaultCryptoUtil().encryptAndBase64Encode(
                plainText.getBytes(Charsets.UTF_8));
    }
}
