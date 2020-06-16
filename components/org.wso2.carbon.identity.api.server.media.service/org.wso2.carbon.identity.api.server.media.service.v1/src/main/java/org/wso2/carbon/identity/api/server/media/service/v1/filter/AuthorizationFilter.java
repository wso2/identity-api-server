/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.api.server.media.service.v1.filter;

import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants;
import org.wso2.carbon.identity.api.server.media.service.common.MediaServiceDataHolder;
import org.wso2.carbon.identity.auth.service.AuthenticationContext;
import org.wso2.carbon.identity.media.StorageSystemManager;
import org.wso2.carbon.identity.media.exception.StorageSystemException;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.media.service.common.MediaServiceConstants.ACCESS_LEVEL_USER_PATH_COMPONENT;

/**
 * Filter to evaluate access level security for media download requests.
 */
public class AuthorizationFilter implements ContainerRequestFilter {

    private static final Log LOG = LogFactory.getLog(AuthorizationFilter.class);
    private static final String OAUTH2_ALLOWED_SCOPES = "oauth2-allowed-scopes";
    private static final String AUTH_CONTEXT = "auth-context";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        if (containerRequestContext.getMethod().equals(HTTPConstants.HTTP_METHOD_GET)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Evaluating access based security for media download request.");
            }
            String accessLevel = containerRequestContext.getUriInfo().getPathSegments().get(1).getPath();
            String type = containerRequestContext.getUriInfo().getPathSegments().get(2).getPath();
            String uuid = containerRequestContext.getUriInfo().getPathSegments().get(3).getPath();

            String[] oauth2AllowedScopes = null;
            if (ACCESS_LEVEL_USER_PATH_COMPONENT.equals(accessLevel) && containerRequestContext.getSecurityContext()
                    .getAuthenticationScheme().equalsIgnoreCase("Bearer")) {

                if (containerRequestContext.getProperty(AUTH_CONTEXT) instanceof AuthenticationContext) {
                    AuthenticationContext authenticationContext = (AuthenticationContext) containerRequestContext
                            .getProperty(AUTH_CONTEXT);
                    oauth2AllowedScopes = (String[]) authenticationContext.getParameter(OAUTH2_ALLOWED_SCOPES);
                }
            }

            StorageSystemManager storageSystemManager = MediaServiceDataHolder.getStorageSystemManager();
            String tenantDomain = ContextLoader.getTenantDomainFromContext();
            try {
                boolean isAuthorized = storageSystemManager.evaluateSecurity(accessLevel, uuid, type, tenantDomain,
                        oauth2AllowedScopes);
                if (!isAuthorized) {
                    Response response = Response.status(HttpServletResponse.SC_UNAUTHORIZED).build();
                    containerRequestContext.abortWith(response);
                }
            } catch (StorageSystemException e) {
                MediaServiceConstants.ErrorMessage errorMessage = MediaServiceConstants.ErrorMessage.
                        ERROR_CODE_ERROR_EVALUATING_ACCESS_SECURITY;
                throw handleServerException(e, errorMessage);
            }
        }
    }

    private APIError handleServerException(Exception e, MediaServiceConstants.ErrorMessage errorEnum) {

        ErrorResponse errorResponse = getErrorBuilder(errorEnum).build(LOG, e, errorEnum.getDescription());
        return new APIError(Response.Status.INTERNAL_SERVER_ERROR, errorResponse);
    }

    private ErrorResponse.Builder getErrorBuilder(MediaServiceConstants.ErrorMessage errorMsg) {

        return new ErrorResponse.Builder().withCode(errorMsg.getCode()).withMessage(errorMsg.getMessage())
                .withDescription(errorMsg.getDescription());
    }

}
