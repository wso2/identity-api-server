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

package org.wso2.carbon.identity.api.server.organization.role.management.v1.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.role.management.common.OrganizationRoleManagementServiceHolder;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.exception.RoleManagementEndpointException;
import org.wso2.carbon.identity.api.server.organization.role.management.v1.model.Error;
import org.wso2.carbon.identity.core.ServiceURLBuilder;
import org.wso2.carbon.identity.core.URLBuilderException;
import org.wso2.carbon.identity.organization.management.role.management.service.RoleManager;
import org.wso2.carbon.identity.organization.management.service.OrganizationUserResidentResolverService;
import org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementClientException;
import org.wso2.carbon.identity.organization.management.service.exception.OrganizationManagementException;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.organization.role.management.v1.constant.RoleManagementEndpointConstants.ORGANIZATION_PATH;
import static org.wso2.carbon.identity.api.server.organization.role.management.v1.constant.RoleManagementEndpointConstants.PATH_SEPARATOR;
import static org.wso2.carbon.identity.api.server.organization.role.management.v1.constant.RoleManagementEndpointConstants.SCIM2_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.organization.role.management.v1.constant.RoleManagementEndpointConstants.V1_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_ORGANIZATION;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_INVALID_ROLE;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_REMOVING_REQUIRED_ATTRIBUTE;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_DISPLAY_NAME_ALREADY_EXISTS;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.ErrorMessages.ERROR_CODE_ROLE_DISPLAY_NAME_MULTIPLE_VALUES;
import static org.wso2.carbon.identity.organization.management.service.constant.OrganizationManagementConstants.SERVER_API_PATH_COMPONENT;

/**
 * The utility class for role management endpoints.
 */
public class RoleManagementEndpointUtils {

    private static final Log LOG = LogFactory.getLog(RoleManagementEndpointUtils.class);

    /**
     * Get an instance of role manager.
     */
    public static RoleManager getRoleManager() {

        return OrganizationRoleManagementServiceHolder.getRoleManager();
    }

    /**
     * Get an instance of Organization User Resident Resolver Service.
     */
    public static OrganizationUserResidentResolverService getOrganizationUserResidentResolverService() {

        return OrganizationRoleManagementServiceHolder.getOrganizationUserResidentResolverService();
    }

    /**
     * Handles client exception for role management.
     *
     * @param e   The role management exception.
     * @param log The Log instance.
     * @return The response for the error.
     */
    public static Response handleClientErrorResponse(OrganizationManagementException e, Log log) {

        if (isNotFoundError(e)) {
            throw buildException(Response.Status.NOT_FOUND, log, e);
        }
        if (isForbiddenError(e)) {
            throw buildException(Response.Status.FORBIDDEN, log, e);
        }
        if (isConflictError(e)) {
            throw buildException(Response.Status.CONFLICT, log, e);
        }
        throw buildException(Response.Status.BAD_REQUEST, log, e);
    }

    /**
     * Handles server exception for role management.
     *
     * @param e   The role management exception.
     * @param log The Log instance.
     * @return The response for the error.
     */
    public static Response handleServerErrorResponse(OrganizationManagementException e, Log log) {

        throw buildException(Response.Status.INTERNAL_SERVER_ERROR, log, e);
    }

    /**
     * Returns a generic error object.
     *
     * @param errorCode        The error code.
     * @param errorMessage     The error message.
     * @param errorDescription The error description.
     * @return A generic error with specified details.
     */
    public static Error getError(String errorCode, String errorMessage, String errorDescription) {

        Error error = new Error();
        error.setCode(errorCode);
        error.setMessage(errorMessage);
        error.setDescription(errorDescription);
        return error;
    }

    /**
     * Get the URI from context.
     *
     * @param organizationId The organization ID.
     * @param id             The id of the resource.
     * @param resourcePath   The path for the resource.
     * @param errorMessage   The error message specific to the resources.
     * @return The URI.
     */
    public static URI getUri(String organizationId, String id, String resourcePath, ErrorMessages errorMessage) {

        String endpoint = SERVER_API_PATH_COMPONENT + V1_API_PATH_COMPONENT + PATH_SEPARATOR + ORGANIZATION_PATH +
                PATH_SEPARATOR + organizationId + PATH_SEPARATOR + resourcePath + PATH_SEPARATOR + id;
        try {
            return URI.create(ServiceURLBuilder.create().addPath(endpoint)
                    .build().getAbsolutePublicURL());
        } catch (URLBuilderException e) {
            Error error = getError(errorMessage.getCode(), errorMessage.getMessage(),
                    String.format(errorMessage.getDescription(), id));
            LOG.error(String.format("Server encountered an error while building URL for %s ",
                    resourcePath.substring(0, resourcePath.length() - 1)) + id);
            throw new RoleManagementEndpointException(Response.Status.INTERNAL_SERVER_ERROR, error);
        }
    }

    /**
     * Get the SCIM2 URI from context.
     *
     * @param id           The id of the resource.
     * @param resourcePath The path for the resource.
     * @param errorMessage The error message specific to the resources.
     * @return The SCIM2 URI.
     */
    public static URI buildSCIM2Uri(String id, String resourcePath, ErrorMessages errorMessage) {

        String endpoint = SCIM2_API_PATH_COMPONENT + PATH_SEPARATOR + resourcePath + PATH_SEPARATOR + id;
        try {
            return URI.create(ServiceURLBuilder.create().addPath(endpoint)
                    .build().getAbsolutePublicURL());
        } catch (URLBuilderException e) {
            Error error = getError(errorMessage.getCode(), errorMessage.getMessage(),
                    String.format(errorMessage.getDescription(), id));
            throw new RoleManagementEndpointException(Response.Status.INTERNAL_SERVER_ERROR, error);
        }
    }

    /**
     * Checks the exception key code and returns true if it is a conflict error.
     *
     * @param e The role management exception.
     * @return If the exception is thrown due to conflict error it returns true, else false.
     */
    private static boolean isConflictError(OrganizationManagementException e) {

        return ERROR_CODE_ROLE_DISPLAY_NAME_MULTIPLE_VALUES.getCode().equals(e.getErrorCode())
                || ERROR_CODE_ROLE_DISPLAY_NAME_ALREADY_EXISTS.getCode().equals(e.getErrorCode());
    }

    /**
     * Checks the exception key code and returns true if it is a forbidden error.
     *
     * @param e The role management exception.
     * @return If the exception is thrown due to forbidden error it returns true, else false.
     */
    private static boolean isForbiddenError(OrganizationManagementException e) {

        return ERROR_CODE_REMOVING_REQUIRED_ATTRIBUTE.getCode().equals(e.getErrorCode());
    }

    /**
     * Checks the exception key code and returns true if it is a not-found error.
     *
     * @param e The role management exception.
     * @return If the exception is thrown due to not-found error it returns true, else false.
     */
    private static boolean isNotFoundError(OrganizationManagementException e) {

        return ERROR_CODE_INVALID_ORGANIZATION.getCode().equals(e.getErrorCode()) || ERROR_CODE_INVALID_ROLE.getCode()
                .equals(e.getErrorCode());
    }

    /**
     * Building the RoleManagementEndpointException according to the RoleManagementException.
     *
     * @param status The status of the response.
     * @param log    The Log instance.
     * @param e      The  role management exception.
     * @return A RoleManagementEndpointException.
     */
    private static RoleManagementEndpointException buildException(Response.Status status, Log log,
                                                                  OrganizationManagementException e) {

        if (e instanceof OrganizationManagementClientException) {
            logDebug(log, e);
        } else {
            logError(log, e);
        }
        return new RoleManagementEndpointException(status, getError(e.getErrorCode(), e.getMessage(),
                e.getDescription()));
    }

    /**
     * If debug is enabled, log the role management errors.
     *
     * @param log Log instance.
     * @param e   The role management exception.
     */
    private static void logDebug(Log log, OrganizationManagementException e) {

        if (log.isDebugEnabled()) {
            String errorMessageFormat = "errorCode: %s | message: %s";
            String errorMessage = String.format(errorMessageFormat, e.getErrorCode(), e.getDescription());
            log.debug(errorMessage, e);
        }
    }

    /**
     * Log the error if it is a role management exception.
     *
     * @param log Log instance.
     * @param e   The role management exception.
     */
    private static void logError(Log log, OrganizationManagementException e) {

        String errorMessageFormat = "errorCode: %s | message: %s";
        String errorMessage = String.format(errorMessageFormat, e.getErrorCode(), e.getDescription());
        log.error(errorMessage, e);
    }
}
