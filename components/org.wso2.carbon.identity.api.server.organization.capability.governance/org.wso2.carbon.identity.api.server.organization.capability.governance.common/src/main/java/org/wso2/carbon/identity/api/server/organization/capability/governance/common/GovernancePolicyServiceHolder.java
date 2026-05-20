/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.capability.governance.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.organization.management.capability.governance.GovernancePolicyEvaluator;
import org.wso2.carbon.identity.organization.management.capability.governance.GovernancePolicyService;
import org.wso2.carbon.identity.organization.management.service.OrganizationManager;

/**
 * Holds the services used by the governance policy management API component.
 */
public class GovernancePolicyServiceHolder {

    private GovernancePolicyServiceHolder() {

    }

    private static class GovernancePolicyServiceHolderInstance {

        private static final GovernancePolicyService SERVICE =
                (GovernancePolicyService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(GovernancePolicyService.class, null);
    }

    private static class GovernancePolicyEvaluatorHolderInstance {

        private static final GovernancePolicyEvaluator SERVICE =
                (GovernancePolicyEvaluator) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(GovernancePolicyEvaluator.class, null);
    }

    public static GovernancePolicyService getGovernancePolicyService() {

        return GovernancePolicyServiceHolderInstance.SERVICE;
    }

    public static GovernancePolicyEvaluator getGovernancePolicyEvaluator() {

        return GovernancePolicyEvaluatorHolderInstance.SERVICE;
    }

    private static class OrganizationManagerHolderInstance {

        private static final OrganizationManager SERVICE =
                (OrganizationManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(OrganizationManager.class, null);
    }

    public static OrganizationManager getOrganizationManager() {

        return OrganizationManagerHolderInstance.SERVICE;
    }
}
