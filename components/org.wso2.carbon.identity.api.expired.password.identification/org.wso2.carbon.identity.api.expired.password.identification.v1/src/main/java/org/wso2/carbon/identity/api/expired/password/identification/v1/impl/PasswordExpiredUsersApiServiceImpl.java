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

package org.wso2.carbon.identity.api.expired.password.identification.v1.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.expired.password.identification.common.ContextLoader;
import org.wso2.carbon.identity.api.expired.password.identification.v1.PasswordExpiredUsersApiService;
import org.wso2.carbon.identity.api.expired.password.identification.v1.core.PasswordExpiredUsersManagementApiService;
import org.wso2.carbon.identity.api.expired.password.identification.v1.factories.PasswordExpiredUsersManagementApiServiceFactory;

import javax.ws.rs.core.Response;

/**
 * Implementation of the password expired User retrieval API Service.
 */
public class PasswordExpiredUsersApiServiceImpl implements PasswordExpiredUsersApiService {

    private static final Log LOG = LogFactory.getLog(PasswordExpiredUsersApiServiceImpl.class);
    private final PasswordExpiredUsersManagementApiService passwordExpiredUsersManagementApiService;

    public PasswordExpiredUsersApiServiceImpl() {

        try {
            this.passwordExpiredUsersManagementApiService = PasswordExpiredUsersManagementApiServiceFactory
                    .getExpiredPasswordIdentificationService();
            if (LOG.isDebugEnabled()) {
                LOG.debug("PasswordExpiredUsersApiServiceImpl initialized successfully.");
            }
        } catch (IllegalStateException e) {
            LOG.error("Error occurred while initiating password expired users management service.", e);
            throw new RuntimeException("Error occurred while initiating password expired users management service.", e);
        }
    }

    @Override
    public Response getPasswordExpiredUsers(String expiredAfter, String excludeAfter) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving password expired users for tenant: " + tenantDomain + 
                    ", expiredAfter: " + expiredAfter + ", excludeAfter: " + excludeAfter);
        }
        return Response.ok().entity(passwordExpiredUsersManagementApiService.getPasswordExpiredUsers(
                expiredAfter, excludeAfter, tenantDomain)).build();
    }
}
