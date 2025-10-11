/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.expired.password.identification.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.expired.password.identification.common.PasswordExpiryServiceHolder;
import org.wso2.carbon.identity.api.expired.password.identification.v1.core.PasswordExpiredUsersManagementApiService;
import org.wso2.carbon.identity.password.expiry.services.ExpiredPasswordIdentificationService;

/**
 * Factory class for PasswordExpiredUsersManagementApiService.
 */
public class PasswordExpiredUsersManagementApiServiceFactory {

    private static final Log LOG = LogFactory.getLog(PasswordExpiredUsersManagementApiServiceFactory.class);
    private static final PasswordExpiredUsersManagementApiService SERVICE;

    static {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing PasswordExpiredUsersManagementApiServiceFactory.");
        }
        ExpiredPasswordIdentificationService expiredPasswordIdentificationService =
                PasswordExpiryServiceHolder.getExpiredPasswordIdentificationService();

        if (expiredPasswordIdentificationService == null) {
            LOG.error("ExpiredPasswordIdentificationService is not available from OSGi context.");
            throw new IllegalStateException("ExpiredPasswordIdentificationService is not available from OSGi context.");
        }

        SERVICE = new PasswordExpiredUsersManagementApiService(expiredPasswordIdentificationService);
        LOG.info("PasswordExpiredUsersManagementApiService initialized successfully.");
    }

    /**
     * Get PasswordExpiredUsersManagementApiService service instance.
     *
     * @return PasswordExpiredUsersManagementApiService service.
     */
    public static PasswordExpiredUsersManagementApiService getExpiredPasswordIdentificationService() {

        return SERVICE;
    }
}
