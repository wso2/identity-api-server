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
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.api.server.idv.provider.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.extension.identity.verification.provider.IdVProviderManager;
import org.wso2.carbon.identity.api.server.idv.provider.common.IdentityVerificationServiceHolder;
import org.wso2.carbon.identity.api.server.idv.provider.v1.core.IdVProviderService;

/**
 * Factory class for IdVProviderService.
 */
public class IdVProviderServiceFactory {

    private static final Log log = LogFactory.getLog(IdVProviderServiceFactory.class);
    private static final IdVProviderService SERVICE;

    static {
        if (log.isDebugEnabled()) {
            log.debug("Initializing IdVProviderService factory");
        }
        IdVProviderManager idvProviderManager = IdentityVerificationServiceHolder.getIdVProviderManager();

        if (idvProviderManager == null) {
            log.error("IdVProviderManager is not available from OSGi context during factory initialization");
            throw new IllegalStateException("IdVProviderManager is not available from OSGi context.");
        }

        SERVICE = new IdVProviderService(idvProviderManager);
        if (log.isDebugEnabled()) {
            log.debug("IdVProviderService factory initialized successfully");
        }
    }

    /**
     * Get IdVProviderService.
     *
     * @return IdVProviderService.
     */
    public static IdVProviderService getIdVProviderService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving IdVProviderService instance from factory");
        }
        return SERVICE;
    }
}
