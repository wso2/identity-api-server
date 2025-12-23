/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.cors.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.cors.v1.CorsApiService;
import org.wso2.carbon.identity.api.server.cors.v1.core.CORSService;
import org.wso2.carbon.identity.api.server.cors.v1.factories.CORSServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of the CORS Rest API.
 */
public class CorsApiServiceImpl implements CorsApiService {

    private static final Log log = LogFactory.getLog(CorsApiServiceImpl.class);
    private final CORSService corsService;

    public CorsApiServiceImpl() {

        try {
            if (log.isDebugEnabled()) {
                log.debug("Initializing CORS API service implementation.");
            }
            this.corsService = CORSServiceFactory.getCORSService();
            if (log.isDebugEnabled()) {
                log.debug("CORS API service implementation initialized successfully.");
            }
        } catch (IllegalStateException e) {
            log.error("Error occurred while initiating API CORS management service.", e);
            throw new RuntimeException("Error occurred while initiating API CORS management service.", e);
        }
    }

    @Override
    public Response getAssociatedAppsByCORSOrigin(String corsOriginId) {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving associated applications for CORS origin ID: " + corsOriginId);
        }
        return Response.ok().entity(corsService.getAssociatedAppsByCORSOrigin(corsOriginId)).build();
    }

    @Override
    public Response getCORSOrigins() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving all CORS origins for the tenant.");
        }
        return Response.ok().entity(corsService.getCORSOrigins()).build();
    }
}
