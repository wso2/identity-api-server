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

package org.wso2.carbon.identity.api.server.vp.verification.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.openid4vc.presentation.authenticator.service.VPFlowService;

/**
 * Service holder for VP Verification API.
 * Obtains the VPFlowService OSGi service via PrivilegedCarbonContext.
 */
public class VPVerificationServiceHolder {

    private VPVerificationServiceHolder() {

    }

    /**
     * Get VPFlowService OSGi service.
     *
     * @return the {@link VPFlowService} registered in the OSGi context, or {@code null} if not available.
     */
    public static VPFlowService getVPFlowService() {

        return (VPFlowService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext()
                .getOSGiService(VPFlowService.class, null);
    }
}
