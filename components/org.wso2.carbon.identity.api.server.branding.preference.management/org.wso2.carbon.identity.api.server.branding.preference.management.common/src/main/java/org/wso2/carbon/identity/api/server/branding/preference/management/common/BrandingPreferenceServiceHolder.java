/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.branding.preference.management.common;

import org.wso2.carbon.identity.branding.preference.management.core.BrandingPreferenceManager;
import org.wso2.carbon.identity.branding.preference.management.core.ai.BrandingAIPreferenceManager;

/**
 * Service holder class for branding preference management.
 */
public class BrandingPreferenceServiceHolder {

    private static BrandingPreferenceManager brandingPreferenceManager;
    private static BrandingAIPreferenceManager brandingPreferenceAiManager;

    /**
     * Get BrandingPreferenceManager OSGi service.
     *
     * @return BrandingPreference Manager.
     */
    public static BrandingPreferenceManager getBrandingPreferenceManager() {

        return brandingPreferenceManager;
    }

    /**
     * Set BrandingPreferenceManager OSGi service.
     *
     * @param brandingPreferenceManager Branding Preference Manager.
     */
    public static void setBrandingPreferenceManager(BrandingPreferenceManager brandingPreferenceManager) {

        BrandingPreferenceServiceHolder.brandingPreferenceManager = brandingPreferenceManager;
    }

    /**
     * Get AIBrandingPreferenceManager OSGi service.
     *
     * @return AI Branding Preference Manager.
     */
    public static BrandingAIPreferenceManager getBrandingPreferenceAiManager() {

        return brandingPreferenceAiManager;
    }

    /**
     * Set AIBrandingPreferenceManager OSGi service.
     *
     * @param brandingPreferenceAIManager AI Branding Preference Manager.
     */
    public static void setBrandingPreferenceAiManager(BrandingAIPreferenceManager brandingPreferenceAIManager) {

        BrandingPreferenceServiceHolder.brandingPreferenceAiManager = brandingPreferenceAIManager;
    }
}
