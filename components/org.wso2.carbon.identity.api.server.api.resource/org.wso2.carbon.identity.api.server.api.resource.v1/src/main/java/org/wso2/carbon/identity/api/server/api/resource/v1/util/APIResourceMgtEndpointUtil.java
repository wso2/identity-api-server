/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.api.resource.v1.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceMgtClientException;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceMgtException;
import org.wso2.carbon.identity.api.server.api.resource.common.APIResourceManagementServiceHolder;
import org.wso2.carbon.identity.api.server.api.resource.v1.APIResourceCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.Error;
import org.wso2.carbon.identity.api.server.api.resource.v1.ScopeCreationModel;
import org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants;
import org.wso2.carbon.identity.api.server.api.resource.v1.exception.APIResourceMgtEndpointException;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.resource.mgt.constant.APIResourceManagementConstants.ErrorMessages.ERROR_CODE_API_RESOURCE_ALREADY_EXISTS;
import static org.wso2.carbon.identity.api.resource.mgt.constant.APIResourceManagementConstants.ErrorMessages.ERROR_CODE_SCOPE_ALREADY_EXISTS;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_API_RESOURCE_IDENTIFIER;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_API_RESOURCE_NAME;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_SCOPE_NAME;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_INVALID_SEARCH_ATTRIBUTE;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_RESTRICTED_OIDC_SCOPES;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_RESTRICTED_SCOPE_NAME;
import static org.wso2.carbon.identity.api.server.api.resource.v1.constants.APIResourceMgtEndpointConstants.ErrorMessage.ERROR_CODE_VALIDATE_SCOPES;
import static org.wso2.carbon.identity.api.server.common.Constants.ERROR_CODE_DELIMITER;

/**
 * Utility class for API resource management endpoint.
 */
public class APIResourceMgtEndpointUtil {

    private static final Log log = LogFactory.getLog(APIResourceMgtEndpointUtil.class);

    public static void validateAPIResource(APIResourceCreationModel apiResource) {

        if (StringUtils.isBlank(apiResource.getName())) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_API_RESOURCE_NAME);
        }
        if (StringUtils.isBlank(apiResource.getIdentifier())) {
            throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_API_RESOURCE_IDENTIFIER);
        }
    }

    public static void validateScopes(List<ScopeCreationModel> scopes) {

        if (scopes == null || scopes.isEmpty()) {
            return;
        }
        for (ScopeCreationModel scope : scopes) {
            // Validate scope name.
            if (StringUtils.isBlank(scope.getName())) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_SCOPE_NAME);
            }
            // Validate restricted scope names.
            List<String> restrictedScopes =
                    IdentityUtil.getPropertyAsList(APIResourceMgtEndpointConstants.RESTRICTED_OAUTH2_SCOPES);
            for (String restrictedScope : restrictedScopes) {
                if (scope.getName().startsWith(restrictedScope)) {
                    throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_RESTRICTED_SCOPE_NAME);
                }
            }

            // Validate OIDC scopes.
            try {
                List<String> registeredOIDCScopes = APIResourceManagementServiceHolder.getOAuthAdminServiceImpl()
                        .getRegisteredOIDCScope(ContextLoader.getTenantDomainFromContext());
                if (registeredOIDCScopes.contains(scope.getName())) {
                    throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_RESTRICTED_OIDC_SCOPES);
                }
            } catch (IdentityOAuthAdminException e) {
                throw handleException(Response.Status.INTERNAL_SERVER_ERROR, ERROR_CODE_VALIDATE_SCOPES);
            }
        }
    }

    /**
     * Validate the attributes provided for search.
     *
     * @param attributes List of attributes to be validated.
     * @return List of validated attributes.
     */
    public static List<String> validateAndConvertToLowerCase(List<String> attributes) {

        List<String> validatedAttributes = new ArrayList<>();

        if (attributes == null || attributes.isEmpty()) {
            return validatedAttributes;
        }

        for (String attribute : attributes) {
            String lowerCaseAttribute = attribute.toLowerCase(Locale.ENGLISH);
            if (!APIResourceMgtEndpointConstants.ALLOWED_SEARCH_ATTRIBUTES.contains(lowerCaseAttribute)) {
                throw handleException(Response.Status.BAD_REQUEST, ERROR_CODE_INVALID_SEARCH_ATTRIBUTE);
            }
            validatedAttributes.add(lowerCaseAttribute);
        }
        return validatedAttributes;
    }

    public static APIResourceMgtEndpointException handleException(Response.Status status,
                                                                  APIResourceMgtEndpointConstants.ErrorMessage error) {

        return new APIResourceMgtEndpointException(status, getError(error.getCode(), error.getMessage(),
                error.getDescription()));
    }

    public static APIResourceMgtEndpointException handleException(Response.Status status,
                                                                  APIResourceMgtEndpointConstants.ErrorMessage error,
                                                                  String data) {

        return new APIResourceMgtEndpointException(status, getError(error.getCode(), error.getMessage(),
                String.format(error.getDescription(), data)));
    }

    public static APIResourceMgtEndpointException handleException(Response.Status status, String errorCode,
                                                                  String message, String description) {

        return new APIResourceMgtEndpointException(status, getError(errorCode, message, description));
    }

    public static APIResourceMgtEndpointException handleAPIResourceMgtException(APIResourceMgtException e) {

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        if (e instanceof APIResourceMgtClientException) {
            log.debug(e.getMessage(), e);
            if (ERROR_CODE_API_RESOURCE_ALREADY_EXISTS.getCode().equals(e.getErrorCode()) ||
                    ERROR_CODE_SCOPE_ALREADY_EXISTS.getCode().equals(e.getErrorCode())) {
                status = Response.Status.CONFLICT;
            } else {
                status = Response.Status.BAD_REQUEST;
            }
        } else {
            log.error(e.getMessage(), e);
        }
        String errorCode = e.getErrorCode();
        errorCode = errorCode.contains(ERROR_CODE_DELIMITER) ? errorCode :
                APIResourceMgtEndpointConstants.API_RESOURCE_MANAGEMENT_PREFIX + errorCode;
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
    public static Error getError(String errorCode, String errorMessage, String errorDescription) {

        Error error = new Error();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }
}
