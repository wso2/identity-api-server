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

package org.wso2.carbon.identity.api.server.organization.selfservice.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.SelfServiceApiService;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.core.SelfServiceMgtService;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.factories.SelfServiceMgtServiceFactory;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.model.PropertyPatchReq;

import javax.ws.rs.core.Response;

/**
 * Implementation of SelfServiceApi.
 */
public class SelfServiceApiServiceImpl implements SelfServiceApiService {

    private static final Log LOG = LogFactory.getLog(SelfServiceApiServiceImpl.class);
    private final SelfServiceMgtService selfServiceMgtService;

    public SelfServiceApiServiceImpl() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing SelfServiceApiServiceImpl.");
        }
        try {
            this.selfServiceMgtService = SelfServiceMgtServiceFactory.getSelfServiceMgtService();
            if (LOG.isDebugEnabled()) {
                LOG.debug("SelfServiceMgtService initialized successfully.");
            }
        } catch (IllegalStateException e) {
            LOG.error("SelfServiceMgtService is not available from OSGi context.", e);
            throw new RuntimeException("SelfServiceMgtService is not available from OSGi context.", e);
        }
    }

    @Override
    public Response organizationPreferenceGet() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving organization governance configurations.");
        }
        Object body = selfServiceMgtService.getOrganizationGovernanceConfigs();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved organization governance configurations.");
        }
        return Response.ok().entity(body).build();
    }

    @Override
    public Response organizationPreferencePatch(PropertyPatchReq propertyPatchReq) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating organization governance configurations.");
        }
        selfServiceMgtService.updateOrganizationGovernanceConfigs(propertyPatchReq, true);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully updated organization governance configurations.");
        }
        return Response.ok().build();
    }
}
