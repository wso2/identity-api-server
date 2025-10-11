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

package org.wso2.carbon.identity.api.server.organization.selfservice.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.api.server.organization.selfservice.common.SelfServiceMgtServiceHolder;
import org.wso2.carbon.identity.api.server.organization.selfservice.v1.core.SelfServiceMgtService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementService;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Factory class for SelfServiceMgtService.
 */
public class SelfServiceMgtServiceFactory {

    private static final Log LOG = LogFactory.getLog(SelfServiceMgtServiceFactory.class);

    private SelfServiceMgtServiceFactory() {

    }

    private static class ServiceHolder {

        private static final SelfServiceMgtService SERVICE = createServiceInstance();
    }

    /**
     * Get SelfServiceMgtService instance.
     *
     * @return SelfServiceMgtService.
     */
    public static SelfServiceMgtService getSelfServiceMgtService() {

        return ServiceHolder.SERVICE;
    }

    private static SelfServiceMgtService createServiceInstance() {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating SelfServiceMgtService instance.");
        }
        IdentityGovernanceService identityGovernanceService = getIdentityGovernanceService();
        ApplicationManagementService applicationManagementService = getApplicationManagementService();
        APIResourceManager apiResourceManager = getAPIResourceManager();
        AuthorizedAPIManagementService authorizedAPIManagementService = getAuthorizedAPIManagementService();

        SelfServiceMgtService service = new SelfServiceMgtService(identityGovernanceService,
                applicationManagementService, apiResourceManager, authorizedAPIManagementService);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully created SelfServiceMgtService instance.");
        }
        return service;
    }

    private static IdentityGovernanceService getIdentityGovernanceService() {

        IdentityGovernanceService service = SelfServiceMgtServiceHolder.getIdentityGovernanceService();
        if (service == null) {
            LOG.error("IdentityGovernanceService is not available from OSGi context.");
            throw new IllegalStateException("IdentityGovernanceService is not available from OSGi context.");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved IdentityGovernanceService from OSGi context.");
        }
        return service;
    }

    private static ApplicationManagementService getApplicationManagementService() {

        ApplicationManagementService service = SelfServiceMgtServiceHolder.getApplicationManagementService();
        if (service == null) {
            LOG.error("ApplicationManagementService is not available from OSGi context.");
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved ApplicationManagementService from OSGi context.");
        }
        return service;
    }

    private static APIResourceManager getAPIResourceManager() {

        APIResourceManager service = SelfServiceMgtServiceHolder.getAPIResourceManager();
        if (service == null) {
            LOG.error("APIResourceManager is not available from OSGi context.");
            throw new IllegalStateException("APIResourceManager is not available from OSGi context.");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved APIResourceManager from OSGi context.");
        }
        return service;
    }

    private static AuthorizedAPIManagementService getAuthorizedAPIManagementService() {

        AuthorizedAPIManagementService service = SelfServiceMgtServiceHolder.getAuthorizedAPIManagementService();
        if (service == null) {
            LOG.error("AuthorizedAPIManagementService is not available from OSGi context.");
            throw new IllegalStateException("AuthorizedAPIManagementService is not available from OSGi context.");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Successfully retrieved AuthorizedAPIManagementService from OSGi context.");
        }
        return service;
    }
}
