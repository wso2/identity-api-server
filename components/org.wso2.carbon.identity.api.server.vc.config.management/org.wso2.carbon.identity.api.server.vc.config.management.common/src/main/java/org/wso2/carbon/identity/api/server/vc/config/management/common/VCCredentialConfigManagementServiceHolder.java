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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.vc.config.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.vc.config.management.VCCredentialConfigManager;
import org.wso2.carbon.identity.vc.config.management.VCOfferManager;

/**
 * Service holder for {@link VCCredentialConfigManager} OSGi service.
 */
public final class VCCredentialConfigManagementServiceHolder {

    private VCCredentialConfigManagementServiceHolder() {
    }

    private static class ServiceHolder {

        static final VCCredentialConfigManager CONFIG_SERVICE = (VCCredentialConfigManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(VCCredentialConfigManager.class, null);
        static final VCOfferManager OFFER_SERVICE = (VCOfferManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(VCOfferManager.class, null);

    }

    /**
     * Get the {@link VCCredentialConfigManager} OSGi service.
     *
     * @return The VCCredentialConfigManager service instance.
     */
    public static VCCredentialConfigManager getVCCredentialConfigManager() {

        return ServiceHolder.CONFIG_SERVICE;
    }

    /**
     * Get the {@link VCOfferManager} OSGi service.
     *
     * @return The VCOfferManager service instance.
     */
    public static VCOfferManager getVCOfferManager() {

        return ServiceHolder.OFFER_SERVICE;
    }
}
