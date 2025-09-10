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

package org.wso2.carbon.identity.api.server.organization.management.v1.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.management.v1.model.Error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Custom exception for organization management endpoint.
 */
public class OrganizationManagementEndpointException extends WebApplicationException {

    private static final Log LOG = LogFactory.getLog(OrganizationManagementEndpointException.class);

    public OrganizationManagementEndpointException(Response.Status status, Error error) {

        super(Response.status(status).entity(error).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .build());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Organization management endpoint exception created with status: " + status + 
                    " and error code: " + (error != null ? error.getCode() : "null"));
        }
    }

    public OrganizationManagementEndpointException(Response.Status status) {

        super(Response.status(status).build());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Organization management endpoint exception created with status: " + status);
        }
    }
}
