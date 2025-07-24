/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.branding.preference.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.branding.preference.management.common.BrandingPreferenceServiceHolder;
import org.wso2.carbon.identity.api.server.branding.preference.management.v1.core.BrandingPreferenceManagementService;
import org.wso2.carbon.identity.branding.preference.management.core.BrandingPreferenceManager;

/**
 * Factory class for Branding Preference Management Service.
 */
public class BrandingPreferenceManagementServiceFactory {

    private static final Log log = LogFactory.getLog(BrandingPreferenceManagementServiceFactory.class);
    private static final BrandingPreferenceManagementService SERVICE;

    static {
        log.info("Initializing BrandingPreferenceManagementService factory");
        BrandingPreferenceManager brandingPreferenceManager = BrandingPreferenceServiceHolder
                .getBrandingPreferenceManager();

        if (brandingPreferenceManager == null) {
            log.error("BrandingPreferenceManager is not available from OSGi context");
            throw new IllegalStateException("BrandingPreferenceManager is not available from OSGi context.");
        }

        SERVICE = new BrandingPreferenceManagementService(brandingPreferenceManager);
        log.info("BrandingPreferenceManagementService factory initialized successfully");
    }

    /**
     * Get BrandingPreferenceManagementService.
     *
     * @return BrandingPreferenceManagementService
     */
    public static BrandingPreferenceManagementService getBrandingPreferenceManagementService() {

        return SERVICE;
    }
}
