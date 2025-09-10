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

package org.wso2.carbon.identity.api.idle.account.identification.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.idle.account.identification.common.IdleAccountIdentificationServiceHolder;
import org.wso2.carbon.identity.api.idle.account.identification.v1.core.InactiveUsersManagementApiService;
import org.wso2.carbon.identity.idle.account.identification.services.IdleAccountIdentificationService;

/**
 * Factory class for InactiveUsersManagementApiService.
 */
public class InactiveUsersManagementApiServiceFactory {

    private static final Log LOG = LogFactory.getLog(InactiveUsersManagementApiServiceFactory.class);
    private static final InactiveUsersManagementApiService SERVICE;

    static {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Initializing InactiveUsersManagementApiServiceFactory.");
        }
        IdleAccountIdentificationService idleAccountIdentificationService = IdleAccountIdentificationServiceHolder
                .getIdleAccountIdentificationService();
        if (idleAccountIdentificationService == null) {
            LOG.error("IdleAccountIdentificationService is not available from OSGi context.");
            throw new IllegalStateException("IdleAccountIdentificationService is not available from OSGi context.");
        }
        SERVICE = new InactiveUsersManagementApiService(idleAccountIdentificationService);
        LOG.info("InactiveUsersManagementApiService initialized successfully.");
    }

    /**
     * Get InactiveUsersManagementApiService.
     *
     * @return InactiveUsersManagementApiService
     */
    public static InactiveUsersManagementApiService getInactiveUsersManagementApiService() {
        return SERVICE;
    }
}
