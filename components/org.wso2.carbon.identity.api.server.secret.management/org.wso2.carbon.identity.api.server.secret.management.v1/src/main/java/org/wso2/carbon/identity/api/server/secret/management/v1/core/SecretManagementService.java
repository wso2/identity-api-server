/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants;
import org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementServiceHolder;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretAddRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretPatchRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretResponse;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretUpdateRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretsListObject;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementClientException;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementException;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementServerException;
import org.wso2.carbon.identity.secret.mgt.core.model.Secret;
import org.wso2.carbon.identity.secret.mgt.core.model.Secrets;

import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.secret.mgt.core.constant.SecretConstants.ErrorMessages.ERROR_CODE_SECRET_ALREADY_EXISTS;
import static org.wso2.carbon.identity.secret.mgt.core.constant.SecretConstants.ErrorMessages.ERROR_CODE_SECRET_DOES_NOT_EXISTS;

/**
 * Invoke internal OSGi service to perform secret management operations.
 */
public class SecretManagementService {

    private static final Log log = LogFactory.getLog(SecretManagementService.class);

    /**
     * Create a secret.
     *
     * @param secretType Type of the secret.
     * @param secretAddRequest secret post request.
     * @return {@link SecretResponse} .
     */
    public SecretResponse addSecret(String secretType, SecretAddRequest secretAddRequest) {

        validateSecretAddRequest(secretAddRequest);
        Secret requestDTO, responseDTO;
        try {
            requestDTO = buildSecretRequestDTOFromSecretAddRequest(secretAddRequest);
            responseDTO = SecretManagementServiceHolder.getSecretConfigManager().addSecret(secretType, requestDTO);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_SECRET,
                    secretAddRequest.getName());
        }
        return buildSecretResponseFromResponseDTO(responseDTO);
    }

    /**
     * To create Secret Response object for the post request.
     *
     * @param secretReq secret object.
     * @return {@link SecretResponse} .
     */
    private SecretResponse buildSecretResponseFromResponseDTO(Secret secretReq) {

        SecretResponse secretResponse = new SecretResponse();
        secretResponse.secretName(secretReq.getSecretName());
        secretResponse.setCreated(secretReq.getCreatedTime());
        secretResponse.setLastModified(secretReq.getLastModified());
        secretResponse.setSecretId(secretReq.getSecretId());
        secretResponse.setDescription(secretReq.getDescription());
        return secretResponse;
    }

    /**
     * Validate the secret post request.
     *
     * @param secretAddRequest secret post request.
     */
    private void validateSecretAddRequest(SecretAddRequest secretAddRequest) {

        String secretAddName = secretAddRequest.getName();
        if (StringUtils.isBlank(secretAddName)) {
            throw handleException(Response.Status.BAD_REQUEST, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_REFERENCE_NAME_NOT_SPECIFIED, null);
        }
        if (StringUtils.isBlank(secretAddRequest.getValue())) {
            throw handleException(Response.Status.BAD_REQUEST, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_SECRET_VALUE_NOT_SPECIFIED, null);
        }
    }

    /**
     * Build secret requestDTO by secret body request.
     *
     * @param secretAddRequest secret post body.
     * @return Secret requestDTO object.
     */
    private Secret buildSecretRequestDTOFromSecretAddRequest(SecretAddRequest secretAddRequest) {

        Secret requestDTO = new Secret();
        requestDTO.setSecretName(secretAddRequest.getName());
        requestDTO.setSecretValue(secretAddRequest.getValue());
        requestDTO.setDescription(secretAddRequest.getDescription());
        return requestDTO;
    }

    /**
     * Delete a secret sender by secret id.
     *
     * @param secretId Id of the secret.
     */
    public void deleteSecret(String secretId) {

        try {
            SecretManagementServiceHolder.getSecretConfigManager().deleteSecretById(secretId);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_DELETING_SECRET, secretId);
        }
    }

    /**
     * Retrieve the secret details by id.
     *
     * @param secretId Id of the secret.
     * @return {@link SecretResponse} .
     */
    public SecretResponse getSecret(String secretId) {

        try {
            Secret responseDTO = SecretManagementServiceHolder.getSecretConfigManager().getSecretById(secretId);
            SecretResponse secretResponse = new SecretResponse();
            secretResponse.secretName(responseDTO.getSecretName());
            secretResponse.setCreated(responseDTO.getCreatedTime());
            secretResponse.setLastModified(responseDTO.getLastModified());
            secretResponse.setSecretId(responseDTO.getSecretId());
            secretResponse.setDescription(responseDTO.getDescription());
            return secretResponse;
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_GETTING_SECRET, secretId);
        }
    }

    /**
     * Retrieve all the secrets of the tenant.
     *
     * @param secretType Type of the secret.
     * @return {@link SecretsListObject} .
     */
    public SecretsListObject getSecretsList(String secretType) {

        try {
            Secrets secrets = SecretManagementServiceHolder.getSecretConfigManager().getSecrets(secretType);
            SecretsListObject secretsListObject = new SecretsListObject();
            secretsListObject.setSecrets(secrets.getSecrets().stream().map(secret ->
                    buildSecretResponseFromResponseDTO(secret)).collect(Collectors.toList())
            );
            return secretsListObject;
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_GETTING_SECRET, null);
        }
    }

    /**
     * To make a partial update or update the specific property of the secret.
     *
     * @param secretType Type of the secret.
     * @param secretId Id of the secret.
     * @param secretPatchRequest secret's patch details.
     * @return Updated secret.
     */
    public SecretResponse patchSecret(String secretType, String secretId, SecretPatchRequest secretPatchRequest) {

        Secret secret, responseDTO;
        try {
            secret = SecretManagementServiceHolder.getSecretConfigManager().getSecretById(secretId);
            if (secret == null) {
                throw handleException(Response.Status.NOT_FOUND, SecretManagementConstants.ErrorMessage.
                        ERROR_CODE_SECRET_NOT_FOUND, secretId);
            }
            String path = secretPatchRequest.getPath();
            SecretPatchRequest.OperationEnum operation = secretPatchRequest.getOperation();
            // Only the Replace operation supported with PATCH request.
            if (SecretPatchRequest.OperationEnum.REPLACE.equals(operation)) {
                if (SecretManagementConstants.VALUE_PATH.equals(path)) {
                    responseDTO = SecretManagementServiceHolder.getSecretConfigManager()
                            .updateSecretValueById(secretId, secretPatchRequest.getValue());

                } else if (SecretManagementConstants.DESCRIPTION_PATH.equals(path)) {
                    responseDTO = SecretManagementServiceHolder.getSecretConfigManager()
                            .updateSecretDescriptionById(secretId, secretPatchRequest.getValue());
                } else {
                    throw handleException(Response.Status.BAD_REQUEST, SecretManagementConstants.ErrorMessage
                            .ERROR_CODE_INVALID_INPUT, "Path");
                }
            } else {
                // Throw an error if any other patch operations are sent in the request.
                throw handleException(Response.Status.BAD_REQUEST, SecretManagementConstants.ErrorMessage
                        .ERROR_CODE_INVALID_INPUT, "Operation");
            }

        } catch (SecretManagementException e) {
            throw handleSecretMgtException(
                    e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SECRET, secretId
            );
        }
        return buildSecretResponseFromResponseDTO(responseDTO);
    }

    /**
     * Update secret details by the secret id.
     *
     * @param secretType Type of the secret.
     * @param secretId Id of the secret.
     * @param secretUpdateRequest secret's updated details.
     * @return Updated secret.
     */
    public SecretResponse updateSecret(String secretType, String secretId, SecretUpdateRequest secretUpdateRequest) {
        Secret requestDTO  = new Secret();
        Secret responseDTO = new Secret();

        requestDTO.setSecretId(secretId);
        requestDTO.setSecretValue(secretUpdateRequest.getValue());

        if (!StringUtils.isEmpty(secretUpdateRequest.getDescription())) {
            requestDTO.setDescription(secretUpdateRequest.getDescription());
        }

        try {
            responseDTO = SecretManagementServiceHolder.getSecretConfigManager().replaceSecret(secretType, requestDTO);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SECRET,
                    secretId);
        }

        return buildSecretResponseFromResponseDTO(responseDTO);
    }

    /**
     * Handle secret management exceptions.
     * @param e Secret management exception.
     * @param errorEnum Error.
     * @param data Context data.
     * @return
     */
    private APIError handleSecretMgtException(SecretManagementException e, SecretManagementConstants.ErrorMessage
            errorEnum, String data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
        Response.Status status;
        if (e instanceof SecretManagementClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            if (ERROR_CODE_SECRET_ALREADY_EXISTS.getCode().equals(e.getErrorCode())) {
                status = Response.Status.CONFLICT;
            } else if (ERROR_CODE_SECRET_DOES_NOT_EXISTS.getCode().equals(e.getErrorCode())) {
                status = Response.Status.NOT_FOUND;
            } else {
                status = Response.Status.BAD_REQUEST;
            }

        } else if (e instanceof SecretManagementServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
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
     * @param data Context data.
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
     * @param data Context data.
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

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        } else {
            return error.getDescription();
        }
    }
}
