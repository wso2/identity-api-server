/*
 * Copyright (c) 2019-2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.MultitenantConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.core.ServiceURLBuilder;
import org.wso2.carbon.identity.core.URLBuilderException;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.net.URI;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.common.Constants.SERVER_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.Constants.TENANT_NAME_FROM_CONTEXT;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.Error.UNEXPECTED_SERVER_ERROR;

/**
 * Load information from context.
 */
public class ContextLoader {

    private static final Log LOG = LogFactory.getLog(ContextLoader.class);

    /**
     * Retrieves loaded tenant domain from carbon context.
     *
     * @return tenant domain of the request is being served.
     */
    public static String getTenantDomainFromContext() {

        String tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
        if (IdentityUtil.threadLocalProperties.get().get(TENANT_NAME_FROM_CONTEXT) != null) {
            tenantDomain = (String) IdentityUtil.threadLocalProperties.get().get(TENANT_NAME_FROM_CONTEXT);
        }
        return tenantDomain;
    }

    /**
     * Retrieves authenticated username from carbon context.
     *
     * @return username of the authenticated user.
     */
    public static String getUsernameFromContext() {

        return PrivilegedCarbonContext.getThreadLocalCarbonContext().getUsername();
    }

    /**
     * Build URI prepending the server API context with the proxy context path to the endpoint.
     * Ex: /t/<tenant-domain>/api/server/<endpoint> or /t/<tenant-domain>/o/api/server/<endpoint>
     *
     * @param endpoint relative endpoint path.
     * @return Relative URI.
     */
    public static URI buildURIForBody(String endpoint) {

        String url;
        String context = getContext(endpoint);

        try {
            url = ServiceURLBuilder.create().addPath(context).build().getRelativePublicURL();
        } catch (URLBuilderException e) {
            String errorDescription = "Server encountered an error while building URL for response body.";
            throw buildInternalServerError(e, errorDescription);
        }
        return URI.create(url);
    }

    /**
     * Build the complete URI prepending the server API context without the proxy context path, to the endpoint.
     * Ex: https://localhost:9443/t/<tenant-domain>/api/server/<endpoint> or
     *     https://localhost:9443/t/<tenant-domain>/o/api/server/<endpoint>
     *
     * @param endpoint relative endpoint path.
     * @return Fully qualified and complete URI.
     */
    public static URI buildURIForHeader(String endpoint) {

        URI loc;
        String context = getContext(endpoint);

        try {
            String url = ServiceURLBuilder.create().addPath(context).build().getAbsolutePublicURL();
            loc = URI.create(url);
        } catch (URLBuilderException e) {
            String errorDescription = "Server encountered an error while building URL for response header.";
            throw buildInternalServerError(e, errorDescription);
        }
        return loc;
    }

    /**
     * Builds the API context based on whether it is an organization specific or tenant specific path.
     *
     * @param endpoint Relative endpoint path.
     * @return Context of the API.
     */
    private static String getContext(String endpoint) {

        return SERVER_API_PATH_COMPONENT + endpoint;
    }

    /**
     * Builds APIError to be thrown if the URL building fails.
     *
     * @param e Exception occurred while building the URL.
     * @param errorDescription Description of the error.
     * @return APIError object which contains the error description.
     */
    private static APIError buildInternalServerError(Exception e, String errorDescription) {

        String errorCode = UNEXPECTED_SERVER_ERROR.getCode();
        String errorMessage = "Error while building response.";

        ErrorResponse errorResponse = new ErrorResponse.Builder().
                withCode(errorCode)
                .withMessage(errorMessage)
                .withDescription(errorDescription)
                .build(LOG, e, errorDescription);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }
}
