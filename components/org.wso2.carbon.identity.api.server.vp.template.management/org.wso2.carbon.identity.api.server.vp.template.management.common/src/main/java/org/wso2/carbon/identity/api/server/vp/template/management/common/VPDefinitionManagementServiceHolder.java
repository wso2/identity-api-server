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

package org.wso2.carbon.identity.api.server.vp.template.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.openid4vc.template.management.service.PresentationDefinitionService;

/**
 * Service holder for VP Definition Management.
 * Obtains the PresentationDefinitionService OSGi service via PrivilegedCarbonContext.
 */
public class VPDefinitionManagementServiceHolder {

    private VPDefinitionManagementServiceHolder() {

    }

    /**
     * Get PresentationDefinitionService OSGi service.
     *
     * @return the {@link PresentationDefinitionService} registered in the OSGi context, 
     * or {@code null} if not available.
     */
    public static PresentationDefinitionService getPresentationDefinitionService() {

        try {
            return (PresentationDefinitionService) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext()
                    .getOSGiService(PresentationDefinitionService.class, null);
        } catch (NullPointerException e) {
            return null;
        }
    }
}
