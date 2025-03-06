/*
 * Copyright (c) 2021-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.secret.management.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.secret.management.common.SecretManagementConstants;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretAddRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretPatchRequest;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretResponse;
import org.wso2.carbon.identity.api.server.secret.management.v1.model.SecretUpdateRequest;
import org.wso2.carbon.identity.secret.mgt.core.SecretManager;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementClientException;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementException;
import org.wso2.carbon.identity.secret.mgt.core.exception.SecretManagementServerException;
import org.wso2.carbon.identity.secret.mgt.core.model.Secret;
import org.wso2.carbon.identity.secret.mgt.core.model.Secrets;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.secret.mgt.core.constant.SecretConstants.ErrorMessages.ERROR_CODE_SECRET_ALREADY_EXISTS;
import static org.wso2.carbon.identity.secret.mgt.core.constant.SecretConstants.ErrorMessages.ERROR_CODE_SECRET_DOES_NOT_EXISTS;

/**
 * Invoke internal OSGi service to perform secret management operations.
 */
public class SecretManagementService {

    private final SecretManager secretManager;
    private static final Log log = LogFactory.getLog(SecretManagementService.class);

    public SecretManagementService(SecretManager secretManager) {

        this.secretManager = secretManager;
    }

    /**
     * Create a secret.
     *
     * @param secretType       Secret type name.
     * @param secretAddRequest Secret post request.
     * @return secret.
     */
    public SecretResponse addSecret(String secretType, SecretAddRequest secretAddRequest) {

        validateSecretAddRequest(secretAddRequest);
        Secret requestDTO, responseDTO;
        try {
            requestDTO = buildSecretRequestDTOFromSecretAddRequest(secretAddRequest);
            responseDTO = secretManager.addSecret(secretType, requestDTO);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_SECRET,
                    secretAddRequest.getName());
        }
        return buildSecretResponseFromResponseDTO(responseDTO);
    }

    /**
     * To create Secret Response object for the post request
     *
     * @param responseDTO Secret object.
     * @return {@link SecretResponse} .
     */
    private SecretResponse buildSecretResponseFromResponseDTO(Secret responseDTO) {

        SecretResponse secretResponse = new SecretResponse();
        secretResponse.secretName(responseDTO.getSecretName());
        secretResponse.setCreated(responseDTO.getCreatedTime());
        secretResponse.setLastModified(responseDTO.getLastModified());
        secretResponse.setSecretId(responseDTO.getSecretId());
        secretResponse.setType(responseDTO.getSecretType());
        secretResponse.setDescription(responseDTO.getDescription());
        return secretResponse;
    }

    /**
     * Validate the secret post request.
     *
     * @param secretAddRequest Secret post request.
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
     * @param secretAddRequest Secret post body.
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
     * Delete a secret sender by name.
     *
     * @param secretType Secret type name.
     * @param name       Name of the secret.
     */
    public void deleteSecret(String secretType, String name) {

        try {
            secretManager.deleteSecret(secretType, name);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_DELETING_SECRET, name);
        }
    }

    /**
     * Retrieve the secret details by name.
     *
     * @param secretType Secret type name.
     * @param name       Secret name.
     * @return secret.
     */
    public SecretResponse getSecret(String secretType, String name) {

        try {
            Secret responseDTO = secretManager.getSecret(secretType, name);
            SecretResponse secretResponse = new SecretResponse();
            secretResponse.secretName(responseDTO.getSecretName());
            secretResponse.setCreated(responseDTO.getCreatedTime());
            secretResponse.setLastModified(responseDTO.getLastModified());
            secretResponse.setSecretId(responseDTO.getSecretId());
            secretResponse.setType(responseDTO.getSecretType());
            secretResponse.setDescription(responseDTO.getDescription());
            return secretResponse;
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_GETTING_SECRET, name);
        }
    }

    /**
     * Retrieve all the secrets of the tenant.
     *
     * @param secretType Secret type name.
     * @return Secrets of the tenant.
     */
    public List<SecretResponse> getSecretsList(String secretType) {

        try {
            Secrets secrets = secretManager.getSecrets(secretType);
            List<Secret> secretsList = secrets.getSecrets();
            return secretsList.stream().map(secret ->
                    buildSecretResponseFromResponseDTO(secret)).collect(Collectors.toList());
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.
                    ERROR_CODE_ERROR_GETTING_SECRET, null);
        }
    }

    /**
     * To make a partial update or update the specific property of the secret.
     *
     * @param secretType         Secret type name.
     * @param name               Secret name.
     * @param secretPatchRequest Secret's patch details.
     * @return Updated secret.
     */
    public SecretResponse patchSecret(String secretType, String name, SecretPatchRequest secretPatchRequest) {

        Secret secret, responseDTO;
        try {
            secret = secretManager.getSecret(secretType, name);
            if (secret == null) {
                throw handleException(Response.Status.NOT_FOUND, SecretManagementConstants.ErrorMessage.
                        ERROR_CODE_SECRET_NOT_FOUND, name);
            }
            String path = secretPatchRequest.getPath();
            SecretPatchRequest.OperationEnum operation = secretPatchRequest.getOperation();
            // Only the Replace operation supported with PATCH request.
            if (SecretPatchRequest.OperationEnum.REPLACE.equals(operation)) {
                if (SecretManagementConstants.VALUE_PATH.equals(path)) {
                    responseDTO = secretManager.updateSecretValue(secretType,
                            name, secretPatchRequest.getValue());

                } else if (SecretManagementConstants.DESCRIPTION_PATH.equals(path)) {
                    responseDTO = secretManager.updateSecretDescription
                            (secretType, name, secretPatchRequest.getValue());
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
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SECRET,
                    name);
        }
        return buildSecretResponseFromResponseDTO(responseDTO);
    }

    /**
     * Update secret details by name.
     *
     * @param secretType          Secret type name.
     * @param name                Secret name.
     * @param secretUpdateRequest Secret's updated details.
     * @return Updated secret.
     */
    public SecretResponse updateSecret(String secretType, String name, SecretUpdateRequest secretUpdateRequest) {

        Secret requestDTO, responseDTO;
        SecretAddRequest secretAddRequest = buildSecretAddFromSecretUpdateRequest(name, secretUpdateRequest);
        try {
            requestDTO = buildSecretRequestDTOFromSecretAddRequest(secretAddRequest);
            responseDTO = secretManager.replaceSecret(secretType, requestDTO);
        } catch (SecretManagementException e) {
            throw handleSecretMgtException(e, SecretManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_SECRET,
                    name);
        }
        return buildSecretResponseFromResponseDTO(responseDTO);
    }

    /**
     * Build secretAdd object from secret update request.
     *
     * @param name                Secret name.
     * @param secretUpdateRequest Secret's update request body.
     * @return secretAdd object
     */
    private SecretAddRequest buildSecretAddFromSecretUpdateRequest(String name,
                                                                   SecretUpdateRequest secretUpdateRequest) {

        SecretAddRequest secretAddRequest = new SecretAddRequest();
        secretAddRequest.setName(name);
        secretAddRequest.setValue(secretUpdateRequest.getValue());
        if (!StringUtils.isEmpty(secretUpdateRequest.getDescription())) {
            secretAddRequest.setDescription(secretUpdateRequest.getDescription());
        }
        return secretAddRequest;
    }

    private APIError handleSecretMgtException(SecretManagementException e, SecretManagementConstants.ErrorMessage
            errorEnum, String data) {

        ErrorResponse errorResponse;
        Response.Status status;
        if (e instanceof SecretManagementClientException) {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e.getMessage());
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
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
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

        if (StringUtils.isNotBlank(data)) {
            return String.format(error.getDescription(), data);
        } else {
            return error.getDescription();
        }
    }
}
