/*
 * Copyright (c) 2023-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.idle.account.identification.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.idle.account.identification.common.ContextLoader;
import org.wso2.carbon.identity.api.idle.account.identification.v1.InactiveUsersApiService;
import org.wso2.carbon.identity.api.idle.account.identification.v1.core.InactiveUsersManagementApiService;
import org.wso2.carbon.identity.api.idle.account.identification.v1.factories.InactiveUsersManagementApiServiceFactory;
import org.wso2.carbon.identity.idle.account.identification.exception.IdleAccountIdentificationClientException;

import javax.ws.rs.core.Response;

/**
 * Implementation of the Inactive Users API Service.
 */
public class InactiveUsersApiServiceImpl implements InactiveUsersApiService {

    private static final Log LOG = LogFactory.getLog(InactiveUsersApiServiceImpl.class);
    private final InactiveUsersManagementApiService inactiveUsersManagementApiService;

    public InactiveUsersApiServiceImpl() {
        LOG.info("Initializing InactiveUsersApiServiceImpl");
        try {
            this.inactiveUsersManagementApiService = InactiveUsersManagementApiServiceFactory
                    .getInactiveUsersManagementApiService();
            LOG.info("InactiveUsersApiServiceImpl initialized successfully");
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating inactive users management service", e);
            throw new RuntimeException("Error occurred while initiating inactive users management service.", e);
        }
    }

    @Override
    public Response getInactiveUsers(String inactiveAfter, String excludeBefore) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Getting inactive users for tenant: %s, inactiveAfter: %s, excludeBefore: %s",
                    tenantDomain, inactiveAfter, excludeBefore));
        }
        return Response.ok().entity(inactiveUsersManagementApiService
                .getInactiveUsers(inactiveAfter, excludeBefore, tenantDomain)).build();
    }

    @Override
    public Response getInactiveUsers(String inactiveAfter, String excludeBefore, String filter)
            throws IdleAccountIdentificationClientException {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Getting filtered inactive users for tenant: %s, filter: %s",
                    tenantDomain, filter));
        }
        if (filter != null) {
            return Response.ok().entity(
                    inactiveUsersManagementApiService.getInactiveUsers(inactiveAfter, excludeBefore, tenantDomain,
                            filter)).build();
        }
        return getInactiveUsers(inactiveAfter, excludeBefore);
    }
}
