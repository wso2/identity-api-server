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

package org.wso2.carbon.identity.api.server.cors.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.cors.common.CORSServiceHolder;
import org.wso2.carbon.identity.api.server.cors.v1.core.CORSService;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;

/**
 * Factory class for CORSService.
 */
public class CORSServiceFactory {

    private static final Log log = LogFactory.getLog(CORSServiceFactory.class);
    private static final CORSService SERVICE;

    static {
        if (log.isDebugEnabled()) {
            log.debug("Initializing CORS service factory.");
        }
        CORSManagementService corsManagementService = CORSServiceHolder.getCorsManagementService();

        if (corsManagementService == null) {
            log.error("CORSManagementService is not available from OSGi context.");
            throw new IllegalStateException("CORSManagementService is not available from OSGi context.");
        }

        SERVICE = new CORSService(corsManagementService);
        if (log.isDebugEnabled()) {
            log.debug("CORS service factory initialized successfully.");
        }
    }

    /**
     * Get CORSService instance.
     *
     * @return CORSService.
     */
    public static CORSService getCORSService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving CORS service instance.");
        }
        return SERVICE;
    }
}
