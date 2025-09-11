/*
 * Copyright (c) 2021-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.branding.preference.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.branding.preference.management.core.BrandingPreferenceManager;
import org.wso2.carbon.identity.branding.preference.management.core.ai.BrandingAIPreferenceManager;

/**
 * Service holder class for branding preference management.
 */
public class BrandingPreferenceServiceHolder {

    private static final Log log = LogFactory.getLog(BrandingPreferenceServiceHolder.class);

    private static class BrandingPreferenceManagerHolder {

        static final BrandingPreferenceManager SERVICE = (BrandingPreferenceManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(BrandingPreferenceManager.class, null);
    }

    private static class BrandingAIPreferenceManagerServiceHolder {

        static final BrandingAIPreferenceManager SERVICE = (BrandingAIPreferenceManager) PrivilegedCarbonContext.
                getThreadLocalCarbonContext().getOSGiService(BrandingAIPreferenceManager.class, null);
    }

    /**
     * Get BrandingPreferenceManager OSGi service.
     *
     * @return BrandingPreference Manager.
     */
    public static BrandingPreferenceManager getBrandingPreferenceManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving BrandingPreferenceManager OSGi service.");
        }
        BrandingPreferenceManager manager = BrandingPreferenceManagerHolder.SERVICE;
        if (manager == null && log.isDebugEnabled()) {
            log.debug("BrandingPreferenceManager OSGi service is not available.");
        }
        return manager;
    }

    /**
     * Get AIBrandingPreferenceManager OSGi service.
     *
     * @return AI Branding Preference Manager.
     */
    public static BrandingAIPreferenceManager getBrandingPreferenceAiManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving BrandingAIPreferenceManager OSGi service.");
        }
        BrandingAIPreferenceManager aiManager = BrandingAIPreferenceManagerServiceHolder.SERVICE;
        if (aiManager == null && log.isDebugEnabled()) {
            log.debug("BrandingAIPreferenceManager OSGi service is not available.");
        }
        return aiManager;
    }
}
