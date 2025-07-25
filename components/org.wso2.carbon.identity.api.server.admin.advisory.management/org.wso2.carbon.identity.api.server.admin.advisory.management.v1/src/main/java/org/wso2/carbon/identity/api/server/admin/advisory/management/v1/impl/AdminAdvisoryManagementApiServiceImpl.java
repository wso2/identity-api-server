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

package org.wso2.carbon.identity.api.server.admin.advisory.management.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.AdminAdvisoryManagementApiService;
import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.core.ServerAdminAdvisoryManagementService;
import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.factories
        .ServerAdminAdvisoryManagementServiceFactory;
import org.wso2.carbon.identity.api.server.admin.advisory.management.v1.model.AdminAdvisoryConfig;

import javax.ws.rs.core.Response;

/**
 * Admin Advisory Management API Service Implementation.
 **/
public class AdminAdvisoryManagementApiServiceImpl implements AdminAdvisoryManagementApiService {

    private static final Log LOG = LogFactory.getLog(AdminAdvisoryManagementApiServiceImpl.class);
    private final ServerAdminAdvisoryManagementService adminAdvisoryManagementService;

    public AdminAdvisoryManagementApiServiceImpl() {

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initializing AdminAdvisoryManagementApiServiceImpl.");
            }
            this.adminAdvisoryManagementService = ServerAdminAdvisoryManagementServiceFactory
                    .getServerAdminAdvisoryManagementService();
        } catch (Exception e) {
            LOG.error("Error occurred while initiating admin advisory management service.", e);
            throw new RuntimeException("Error occurred while initiating admin advisory management service.", e);
        }
    }

    /**
     * Endpoint to get the admin advisory banner configuration.
     *
     * @return Response instance.
     */
    @Override
    public Response getAdminAdvisoryConfig() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Received request to get admin advisory banner configuration.");
        }
        return Response.ok().entity(adminAdvisoryManagementService.getAdminAdvisoryConfig()).build();
    }

    /**
     * Endpoint to update the admin advisory banner configuration.
     *
     * @param adminAdvisoryConfig   Admin advisory configuration.
     *
     * @return Response instance.
     */
    @Override
    public Response updateAdminAdvisoryConfig(AdminAdvisoryConfig adminAdvisoryConfig) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Received request to update admin advisory banner configuration.");
        }
        adminAdvisoryManagementService.saveAdminAdvisoryConfig(adminAdvisoryConfig);
        return Response.ok().build();
    }
}
