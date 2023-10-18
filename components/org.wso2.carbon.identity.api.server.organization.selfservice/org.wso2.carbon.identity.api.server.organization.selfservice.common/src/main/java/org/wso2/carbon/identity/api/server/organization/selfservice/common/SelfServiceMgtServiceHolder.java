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

import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;

/**
 * Service holder class for self-service management services.
 */
public class SelfServiceMgtServiceHolder {

    private static IdentityGovernanceService identityGovernanceService;

    private static ApplicationManagementService applicationManagementService;

    /**
     * Get Application Management OSGI service.
     *
     * @return Application management service..
     */
    public static ApplicationManagementService getApplicationManagementService() {

        return applicationManagementService;
    }

    /**
     * Set Application Management OSGI service.
     *
     * @param applicationManagementService Application management service.
     */
    public static void setApplicationManagementService(
            ApplicationManagementService applicationManagementService) {

        SelfServiceMgtServiceHolder.applicationManagementService = applicationManagementService;
    }

    /**
     * Get Identity Governance OSGI service.
     *
     * @return Identity Governance service.
     */
    public static IdentityGovernanceService getIdentityGovernanceService() {

        return identityGovernanceService;
    }

    /**
     * Set Identity Governance OSGI service.
     *
     * @param identityGovernanceService Identity Governance service.
     */
    public static void setIdentityGovernanceService(IdentityGovernanceService identityGovernanceService) {

        SelfServiceMgtServiceHolder.identityGovernanceService = identityGovernanceService;
    }
}
