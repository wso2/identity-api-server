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

package org.wso2.carbon.identity.api.server.organization.selfservice.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementService;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Service holder class for self-service management services.
 */
public class SelfServiceMgtServiceHolder {

    private SelfServiceMgtServiceHolder() {

    }

    private static class IdentityGovernanceServiceHolder {

        static final IdentityGovernanceService SERVICE = (IdentityGovernanceService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(IdentityGovernanceService.class, null);
    }

    private static class ApplicationManagementServiceHolder {

        static final ApplicationManagementService SERVICE = (ApplicationManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ApplicationManagementService.class, null);
    }

    private static class APIResourceManagerServiceHolder {

        static final APIResourceManager SERVICE = (APIResourceManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(APIResourceManager.class, null);
    }

    private static class AuthorizedAPIManagementServiceHolder {

        static final AuthorizedAPIManagementService SERVICE = (AuthorizedAPIManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(AuthorizedAPIManagementService.class, null);
    }

    /**
     * Get Application Management OSGI service.
     *
     * @return Application management service.
     */
    public static ApplicationManagementService getApplicationManagementService() {

        return ApplicationManagementServiceHolder.SERVICE;
    }

    /**
     * Get Identity Governance OSGI service.
     *
     * @return Identity Governance service.
     */
    public static IdentityGovernanceService getIdentityGovernanceService() {

        return IdentityGovernanceServiceHolder.SERVICE;
    }

    /**
     * Get APIResourceManager.
     *
     * @return APIResourceManager.
     */
    public static APIResourceManager getAPIResourceManager() {

        return APIResourceManagerServiceHolder.SERVICE;
    }

    /**
     * Get AuthorizedAPIManagementService.
     *
     * @return AuthorizedAPIManagementService.
     */
    public static AuthorizedAPIManagementService getAuthorizedAPIManagementService() {

        return AuthorizedAPIManagementServiceHolder.SERVICE;
    }
}
