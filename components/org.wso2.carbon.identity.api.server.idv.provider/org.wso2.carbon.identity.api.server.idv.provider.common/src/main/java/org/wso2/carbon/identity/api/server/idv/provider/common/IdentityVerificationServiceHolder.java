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

package org.wso2.carbon.identity.api.server.idv.provider.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.extension.identity.verification.provider.IdVProviderManager;

/**
 * Service holder class for identity verification Rest API.
 */
public class IdentityVerificationServiceHolder {

    private static final Log log = LogFactory.getLog(IdentityVerificationServiceHolder.class);

    private IdentityVerificationServiceHolder() {};

    private static class IdVProviderManagerHolder {
        static final IdVProviderManager SERVICE = (IdVProviderManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(IdVProviderManager.class, null);
    }

    /**
     * Get IdVProviderManager osgi service.
     *
     * @return IdVProviderManager
     */
    public static IdVProviderManager getIdVProviderManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdVProviderManager OSGi service.");
        }
        
        IdVProviderManager service = IdVProviderManagerHolder.SERVICE;
        if (service == null) {
            log.warn("IdVProviderManager OSGi service is not available.");
        }
        return service;
    }
}
