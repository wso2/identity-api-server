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

package org.wso2.carbon.identity.api.server.organization.configs.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.configs.v1.OrganizationConfigsApiService;
import org.wso2.carbon.identity.api.server.organization.configs.v1.core.OrganizationConfigsService;
import org.wso2.carbon.identity.api.server.organization.configs.v1.factories.OrganizationConfigsServiceFactory;
import org.wso2.carbon.identity.api.server.organization.configs.v1.model.Config;

import javax.ws.rs.core.Response;

/**
 * Implementation of the organization configuration REST API.
 */
public class OrganizationConfigsApiServiceImpl implements OrganizationConfigsApiService {

    private static final Log LOG = LogFactory.getLog(OrganizationConfigsApiServiceImpl.class);
    private final OrganizationConfigsService organizationConfigsService;

    public OrganizationConfigsApiServiceImpl() {

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Initializing OrganizationConfigsApiServiceImpl.");
            }
            this.organizationConfigsService = OrganizationConfigsServiceFactory.getOrganizationConfigsService();
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating organization configuration service.", e);
            throw new RuntimeException("Error occurred while initiating organization configuration service.", e);
        }
    }

    @Override
    public Response createDiscoveryConfig(Config config) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating discovery configuration via REST API.");
        }
        organizationConfigsService.addDiscoveryConfiguration(config);
        return Response.status(Response.Status.CREATED).entity(config).build();
    }

    @Override
    public Response deleteDiscoveryConfig() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting discovery configuration via REST API.");
        }
        organizationConfigsService.deleteDiscoveryConfiguration();
        return Response.noContent().build();
    }

    @Override
    public Response getDiscoveryConfig() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving discovery configuration via REST API.");
        }
        return Response.ok().entity(organizationConfigsService.getDiscoveryConfiguration()).build();
    }

    @Override
    public Response updateDiscoveryConfig(Config config) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating discovery configuration via REST API.");
        }
        organizationConfigsService.updateDiscoveryConfiguration(config);
        return Response.ok().entity(config).build();
    }
}
