/*
 * Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.configs.v1.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.configs.common.ConfigsServiceHolder;
import org.wso2.carbon.identity.api.server.configs.common.Constants;
import org.wso2.carbon.identity.api.server.configs.v1.model.Authenticator;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticatorListItem;
import org.wso2.carbon.identity.api.server.configs.v1.model.AuthenticatorProperty;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementClientException;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementServerException;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLDecode;
import static org.wso2.carbon.identity.api.server.common.Util.base64URLEncode;
import static org.wso2.carbon.identity.api.server.configs.common.Constants.CONFIGS_AUTHENTICATOR_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.configs.common.Constants.PATH_SEPERATOR;

/**
 * Call internal osgi services to perform server configuration management.
 */
public class ServerConfigManagementService {

    private static final Log log = LogFactory.getLog(ServerConfigManagementService.class);

    /**
     * Get list of local authenticators supported by the server.
     *
     * @return List of authenticator basic information.
     */
    public List<AuthenticatorListItem> getAuthenticators() {

        try {
            LocalAuthenticatorConfig[] authenticatorConfigs = ConfigsServiceHolder.getApplicationManagementService()
                    .getAllLocalAuthenticators(ContextLoader.getTenantDomainFromContext());
            return buildAuthenticatorListResponse(authenticatorConfigs);
        } catch (IdentityApplicationManagementException e) {
            throw handleApplicationMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_LISTING_AUTHENTICATORS,
                    null);
        }
    }

    /**
     * Get an authenticator identified by its resource ID.
     *
     * @param authenticatorId Authenticator resource ID.
     * @return Authenticator.
     */
    public Authenticator getAuthenticator(String authenticatorId) {

        try {
            LocalAuthenticatorConfig authenticatorConfig = getAuthenticatorById(
                    ConfigsServiceHolder.getApplicationManagementService().getAllLocalAuthenticators(
                            ContextLoader.getTenantDomainFromContext()), authenticatorId);
            if (authenticatorConfig == null) {
                throw handleException(Response.Status.NOT_FOUND,
                        Constants.ErrorMessage.ERROR_CODE_AUTHENTICATOR_NOT_FOUND, authenticatorId);
            }
            return buildAuthenticatorResponse(authenticatorConfig);
        } catch (IdentityApplicationManagementException e) {
            throw handleApplicationMgtException(e, Constants.ErrorMessage.ERROR_CODE_ERROR_RETRIEVING_AUTHENTICATOR,
                    authenticatorId);
        }
    }

    private List<AuthenticatorListItem> buildAuthenticatorListResponse(
            LocalAuthenticatorConfig[] authenticatorConfigs) {

        List<AuthenticatorListItem> authenticatorListItems = new ArrayList<>();
        if (authenticatorConfigs != null) {
            for (LocalAuthenticatorConfig config : authenticatorConfigs) {
                AuthenticatorListItem authenticatorListItem = new AuthenticatorListItem();
                String authenticatorId = base64URLEncode(config.getName());
                authenticatorListItem.setId(authenticatorId);
                authenticatorListItem.setName(config.getName());
                authenticatorListItem.setDisplayName(config.getDisplayName());
                authenticatorListItem.setIsEnabled(config.isEnabled());
                authenticatorListItem.setSelf(ContextLoader.buildURIForBody(String.format(V1_API_PATH_COMPONENT +
                        CONFIGS_AUTHENTICATOR_PATH_COMPONENT + PATH_SEPERATOR + "%s", authenticatorId)).toString());
                authenticatorListItems.add(authenticatorListItem);
            }
        }
        return authenticatorListItems;
    }

    private LocalAuthenticatorConfig getAuthenticatorById(LocalAuthenticatorConfig[] authenticatorConfigs,
                                                          String authenticatorId) {

        String authenticatorName = base64URLDecode(authenticatorId);
        for (LocalAuthenticatorConfig config : authenticatorConfigs) {
            if (StringUtils.equals(authenticatorName, config.getName())) {
                return config;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Unable to find an authenticator with the name: " + authenticatorName);
        }
        return null;
    }

    private Authenticator buildAuthenticatorResponse(LocalAuthenticatorConfig config) {

        Authenticator authenticator = new Authenticator();
        authenticator.setId(base64URLEncode(config.getName()));
        authenticator.setName(config.getName());
        authenticator.setDisplayName(config.getDisplayName());
        authenticator.setIsEnabled(config.isEnabled());
        List<AuthenticatorProperty> authenticatorProperties =
                Arrays.stream(config.getProperties()).map(propertyToExternal)
                        .collect(Collectors.toList());
        authenticator.setProperties(authenticatorProperties);
        return authenticator;
    }

    private Function<Property, AuthenticatorProperty> propertyToExternal = property -> {

        AuthenticatorProperty authenticatorProperty = new AuthenticatorProperty();
        authenticatorProperty.setKey(property.getName());
        authenticatorProperty.setValue(property.getValue());
        return authenticatorProperty;
    };

    /**
     * Handle IdentityApplicationManagementException, extract error code, error description and status code to be sent
     * in the response.
     *
     * @param e         IdentityApplicationManagementException
     * @param errorEnum Error Message information.
     * @return APIError.
     */
    private APIError handleApplicationMgtException(IdentityApplicationManagementException e,
                                                   Constants.ErrorMessage errorEnum, String data) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum, data).build(log, e, errorEnum.description());

        Response.Status status;

        if (e instanceof IdentityApplicationManagementClientException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_PREFIX + errorCode;
                errorResponse.setCode(errorCode);
            }
            errorResponse.setDescription(e.getMessage());
            status = Response.Status.BAD_REQUEST;
        } else if (e instanceof IdentityApplicationManagementServerException) {
            if (e.getErrorCode() != null) {
                String errorCode = e.getErrorCode();
                errorCode =
                        errorCode.contains(org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER) ?
                                errorCode : Constants.CONFIG_PREFIX + errorCode;
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
    private APIError handleException(Response.Status status, Constants.ErrorMessage error, String data) {

        return new APIError(status, getErrorBuilder(error, data).build());
    }

    /**
     * Return error builder.
     *
     * @param errorMsg Error Message information.
     * @return ErrorResponse.Builder.
     */
    private ErrorResponse.Builder getErrorBuilder(Constants.ErrorMessage errorMsg, String data) {

        return new ErrorResponse.Builder().withCode(errorMsg.code()).withMessage(errorMsg.message())
                .withDescription(includeData(errorMsg, data));
    }

    /**
     * Include context data to error message.
     *
     * @param error Constant.ErrorMessage.
     * @param data  Context data.
     * @return Formatted error message.
     */
    private static String includeData(Constants.ErrorMessage error, String data) {

        String message;
        if (StringUtils.isNotBlank(data)) {
            message = String.format(error.description(), data);
        } else {
            message = error.description();
        }
        return message;
    }
}
