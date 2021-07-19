/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.core;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.wso2.carbon.core.util.CryptoException;
import org.wso2.carbon.core.util.CryptoUtil;

import org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementDataHolder;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.error.APIError;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.EndpointConfiguration;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.EndpointConfigurationAdd;
import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.EndpointConfigurationUpdateRequest;

import org.wso2.carbon.identity.api.server.endpoint.configuration.management.v1.model.Secret;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementClientException;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementServerException;
import org.wso2.carbon.identity.configuration.mgt.core.model.Attribute;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resource;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resources;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.AUTH_TYPE;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.CONFIGURATION_RESOURCE_TYPE;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.CONFIG_MGT_ERROR_CODE_DELIMITER;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ENDPOINT_URL;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ERROR_PREFIX;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ErrorMessage.ERROR_CODE_AUTH_TYPE_URI_NOT_SPECIFIED;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ErrorMessage.ERROR_CODE_CONFLICT_CONFIGURATION;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ErrorMessage.ERROR_CODE_ENDPOINT_URI_NOT_SPECIFIED;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_ADDING_ENDPOINT_CONFIGURATION;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_DELETING_ENDPOINT_CONFIGURATION;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_ENDPOINT_CONFIGURATION;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_GETTING_ENDPOINT_CONFIGURATION_BY_TYPE;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ErrorMessage.ERROR_CODE_ERROR_UPDATING_ENDPOINT_CONFIGURATION;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.ErrorMessage.ERROR_CODE_REFERENCE_NAME_NOT_SPECIFIED;
import static org.wso2.carbon.identity.api.server.endpoint.configuration.management.common.EndpointConfigurationManagementConstants.RESOURCE_NOT_EXISTS_ERROR_CODE;

/**
 * Invoke internal OSGi service to perform endpoint configuration management operations.
 */
public class ConfigurationManagementService {

    private static final Log log = LogFactory.getLog(ConfigurationManagementService.class);

    /**
     * Create a endpoint configuration resource with a resource file.
     *
     * @param endpointConfigurationAdd endpoint configuration post request.
     * @return endpoint configuration.
     */
    public EndpointConfiguration addEndpointConfiguration(EndpointConfigurationAdd endpointConfigurationAdd) {

        validateEndpointConfigurationAdd(endpointConfigurationAdd);
        Resource endpointConfigurationResource;
        try {
            endpointConfigurationResource = buildResourceFromEndpointConfigurationAdd(endpointConfigurationAdd,
                    null);
            EndpointConfigurationManagementDataHolder.getConfigurationConfigManager()
                    .addResource(CONFIGURATION_RESOURCE_TYPE, endpointConfigurationResource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_ADDING_ENDPOINT_CONFIGURATION,
                    endpointConfigurationAdd.getReferenceName());
        }
        return buildEndpointConfigurationFromResource(endpointConfigurationResource);
    }

    /**
     * Build a endpoint configuration response from endpoint configuration's resource object.
     *
     * @param resource endpoint configuration resource object.
     * @return endpoint configuration response.
     */
    private EndpointConfiguration buildEndpointConfigurationFromResource(Resource resource) {

        EndpointConfiguration endpointConfiguration = new EndpointConfiguration();
        endpointConfiguration.setReferenceName(resource.getResourceName());
        endpointConfiguration.setUrl(resource.getAttributes().get(0).getValue());
        endpointConfiguration.setAuthType(resource.getAttributes().get(1).getValue());
        Secret secrets = new Secret();
        secrets.key(resource.getAttributes().get(2).getKey());
        secrets.value(getDecryptedSecret(resource.getAttributes().get(2).getValue()));
        endpointConfiguration.setSecret(secrets);
        return endpointConfiguration;
    }

    /**
     * Validate the endpoint configuration post request.
     *
     * @param endpointConfigurationAdd endpoint configuration post request.
     */
    private void validateEndpointConfigurationAdd(
            EndpointConfigurationAdd endpointConfigurationAdd) {

        String endpointConfigurationAddReferenceName = endpointConfigurationAdd.getReferenceName();
        if (StringUtils.isBlank(endpointConfigurationAddReferenceName)) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_REFERENCE_NAME_NOT_SPECIFIED,
                    null);
        }
        if (StringUtils.isBlank(endpointConfigurationAdd.getUrl())) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_ENDPOINT_URI_NOT_SPECIFIED,
                    null);
        }
        if (StringUtils.isBlank(endpointConfigurationAdd.getUrl())) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_AUTH_TYPE_URI_NOT_SPECIFIED,
                    null);
        }
        try {
            Resource resource =
                    EndpointConfigurationManagementDataHolder.getConfigurationConfigManager()
                            .getResource(CONFIGURATION_RESOURCE_TYPE, endpointConfigurationAddReferenceName);
            if (resource != null) {
                throw handleException(Response.Status.CONFLICT, ERROR_CODE_CONFLICT_CONFIGURATION,
                        endpointConfigurationAddReferenceName);
            }
        } catch (ConfigurationManagementException e) {
            if (!RESOURCE_NOT_EXISTS_ERROR_CODE.equals(e.getErrorCode())) {
                throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_ENDPOINT_CONFIGURATION,
                        endpointConfigurationAddReferenceName);
            }
        }
    }

    /**
     * Build Resource by endpoint configuration body request.
     *
     * @param endpointConfigurationAdd endpoint configuration post body.
     * @param inputStream              endpoint configuration publisher file stream.
     * @return Resource object.
     */
    private Resource buildResourceFromEndpointConfigurationAdd(EndpointConfigurationAdd endpointConfigurationAdd,
                                                               InputStream inputStream) {

        Resource resource = new Resource();
        resource.setResourceName(endpointConfigurationAdd.getReferenceName());
        List<Attribute> attributeList = new ArrayList();
        attributeList.add(new Attribute(ENDPOINT_URL, endpointConfigurationAdd.getUrl()));
        attributeList.add(new Attribute(AUTH_TYPE, endpointConfigurationAdd.getAuthType()));
        attributeList.add(new Attribute(endpointConfigurationAdd.getSecret().getKey(),
                getEncryptedSecret(endpointConfigurationAdd.getSecret().getValue())));
        resource.setAttributes(attributeList);
        return resource;
    }

    /**
     * Delete a endpoint Configuration sender by name.
     *
     * @param configurationReferenceName Name of the endpoint configuration.
     */
    public void deleteEndpointConfiguration(String configurationReferenceName) {

        try {
            EndpointConfigurationManagementDataHolder.getConfigurationConfigManager()
                    .deleteResource(CONFIGURATION_RESOURCE_TYPE, configurationReferenceName);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_DELETING_ENDPOINT_CONFIGURATION,
                    configurationReferenceName);
        }
    }

    /**
     * Retrieve the endpoint Configuration details by name.
     *
     * @param referenceName endpoint configuration's reference name.
     * @return endpoint configuration.
     */
    public EndpointConfiguration getEndpointConfiguration(String referenceName) {

        try {
            Resource resource = EndpointConfigurationManagementDataHolder.getConfigurationConfigManager()
                    .getResource(CONFIGURATION_RESOURCE_TYPE, referenceName);
            return buildEndpointConfigurationFromResource(resource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_ENDPOINT_CONFIGURATION, referenceName);
        }
    }

    /**
     * Retrieve all endpoint Configurations of the tenant.
     *
     * @return endpoint Configurations of the tenant.
     */
    public List<EndpointConfiguration> getEndpointConfigurations() {

        try {
            Resources configurationResources =
                    EndpointConfigurationManagementDataHolder.getConfigurationConfigManager()
                            .getResourcesByType(CONFIGURATION_RESOURCE_TYPE);
            List<Resource> endpointConfigurationResources = configurationResources.
                    getResources();
            return endpointConfigurationResources.stream().map(resource ->
                    buildEndpointConfigurationFromResource(resource)).collect(
                    Collectors.toList());
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_GETTING_ENDPOINT_CONFIGURATION_BY_TYPE,
                    CONFIGURATION_RESOURCE_TYPE);
        }
    }

    /**
     * Update endpoint configuration details by name.
     *
     * @param referenceName                      endpoint configuration's name.
     * @param endpointConfigurationUpdateRequest endpoint configuration's updated configurations.
     * @return Updated endpoint configuration.
     */
    public EndpointConfiguration updateEndpointConfiguration(String referenceName, EndpointConfigurationUpdateRequest
            endpointConfigurationUpdateRequest) {

        Resource endpointConfigurationResource = null;
        EndpointConfigurationAdd endpointConfigurationAdd =
                buildEndpointConfigurationAddFromEndpointConfigurationUpdateRequest
                        (referenceName, endpointConfigurationUpdateRequest);
        try {
            endpointConfigurationResource = buildResourceFromEndpointConfigurationAdd(endpointConfigurationAdd,
                    null);
            EndpointConfigurationManagementDataHolder.getConfigurationConfigManager()
                    .replaceResource(CONFIGURATION_RESOURCE_TYPE, endpointConfigurationResource);
        } catch (ConfigurationManagementException e) {
            throw handleConfigurationMgtException(e, ERROR_CODE_ERROR_UPDATING_ENDPOINT_CONFIGURATION, referenceName);
        }
        return buildEndpointConfigurationFromResource(endpointConfigurationResource);
    }

    /**
     * Build endpoint configuration add object from endpoint configuration update request.
     *
     * @param referenceName                      endpoint configuration's reference name.
     * @param endpointConfigurationUpdateRequest endpoint configuration's update request body.
     * @return endpoint configuration add object
     */
    private EndpointConfigurationAdd buildEndpointConfigurationAddFromEndpointConfigurationUpdateRequest(
            String referenceName, EndpointConfigurationUpdateRequest endpointConfigurationUpdateRequest) {

        EndpointConfigurationAdd endpointConfigurationAdd = new EndpointConfigurationAdd();
        endpointConfigurationAdd.setReferenceName(referenceName);
        endpointConfigurationAdd.setAuthType(endpointConfigurationUpdateRequest.getAuthType());
        endpointConfigurationAdd.setUrl(endpointConfigurationUpdateRequest.getUrl());
        endpointConfigurationAdd.secret(endpointConfigurationUpdateRequest.getSecret());
        return endpointConfigurationAdd;
    }

    private APIError handleConfigurationMgtException(ConfigurationManagementException e,
                                                     EndpointConfigurationManagementConstants.ErrorMessage errorEnum,
                                                     String data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.getDescription());
        Response.Status status;
        if (e instanceof ConfigurationManagementClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode = errorCode.contains(CONFIG_MGT_ERROR_CODE_DELIMITER) ? errorCode :
                        ERROR_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof ConfigurationManagementServerException) {
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
    private APIError handleException(Response.Status status, EndpointConfigurationManagementConstants.
            ErrorMessage error, String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(EndpointConfigurationManagementConstants.ErrorMessage errorMsg,
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
    private static String includeData(EndpointConfigurationManagementConstants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.getDescription(), data);
        } else {
            message = error.getDescription();
        }
        return message;
    }

    public String getEncryptedSecret(String clientSecret) {

        try {
            return encrypt(clientSecret);
        } catch (CryptoException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_ERROR_ADDING_ENDPOINT_CONFIGURATION,
                    null);
        }
    }

    public String getDecryptedSecret(String clientSecret) {

        try {
            return decrypt(clientSecret);
        } catch (CryptoException e) {
            throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_ERROR_GETTING_ENDPOINT_CONFIGURATION
                    , null);
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

    /**
     * Encrypt secret.
     *
     * @param cipherText cipher text secret.
     * @return decrypted secret.
     */
    private String decrypt(String cipherText) throws CryptoException {

        return new String(CryptoUtil.getDefaultCryptoUtil().base64DecodeAndDecrypt(
                cipherText), Charsets.UTF_8);
    }
}
