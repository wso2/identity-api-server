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
import org.wso2.carbon.identity.openid4vc.template.management.PresentationDefinitionManager;

/**
 * Service holder for {@link PresentationDefinitionManager} OSGi service.
 */
public final class VPDefinitionManagementServiceHolder {

    private VPDefinitionManagementServiceHolder() {

    }

    private static class ServiceHolder {

        static final PresentationDefinitionManager PRESENTATION_DEFINITION_MANAGER =
                (PresentationDefinitionManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(PresentationDefinitionManager.class, null);
    }

    /**
     * Get the {@link PresentationDefinitionManager} OSGi service.
     *
     * @return The PresentationDefinitionManager service instance.
     */
    public static PresentationDefinitionManager getPresentationDefinitionManager() {

        return ServiceHolder.PRESENTATION_DEFINITION_MANAGER;
    }
}
