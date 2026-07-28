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

package org.wso2.carbon.identity.api.server.policy.v1.factories;

import org.wso2.carbon.identity.api.server.policy.common.PolicyServiceHolder;
import org.wso2.carbon.identity.api.server.policy.v1.core.PolicyManagementService;
import org.wso2.carbon.identity.policy.management.api.service.PolicyManagementService;

/**
 * Factory class for Policy Service.
 */
public class PolicyServiceFactory {

    private static final PolicyManagementService SERVICE;

    static {
        PolicyManagementService policyManagementService =
                PolicyServiceHolder.getPolicyManagementService();

        if (policyManagementService == null) {
            throw new IllegalStateException("PolicyManagementService is not available from OSGi context.");
        }

        SERVICE = new PolicyManagementService(policyManagementService);
    }

    /**
     * Get Policy Service.
     *
     * @return PolicyService.
     */
    public static PolicyManagementService getPolicyService() {

        return SERVICE;
    }
}
