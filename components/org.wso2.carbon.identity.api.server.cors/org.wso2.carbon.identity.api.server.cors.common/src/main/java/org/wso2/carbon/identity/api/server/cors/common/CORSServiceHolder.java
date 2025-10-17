/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *                                                                         
 * Licensed under the Apache License, Version 2.0 (the "License");         
 * you may not use this file except in compliance with the License.        
 * You may obtain a copy of the License at                                 
 *                                                                         
 * http://www.apache.org/licenses/LICENSE-2.0                              
 *                                                                         
 * Unless required by applicable law or agreed to in writing, software     
 * distributed under the License is distributed on an "AS IS" BASIS,       
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and     
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.cors.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;

/**
 * Service holder class for server configuration related services.
 */
public class CORSServiceHolder {

    private static final Log log = LogFactory.getLog(CORSServiceHolder.class);

    private CORSServiceHolder() {}

    private static class CORSManagementServiceHolder {

        static final CORSManagementService SERVICE = (CORSManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(CORSManagementService.class, null);
    }

    /**
     * Get CORSManagementService osgi service.
     *
     * @return CORSManagementService
     */
    public static CORSManagementService getCorsManagementService() {

        CORSManagementService service = CORSManagementServiceHolder.SERVICE;
        if (service == null) {
            log.warn("CORSManagementService is not available. CORS configurations may not function properly.");
        } else if (log.isDebugEnabled()) {
            log.debug("CORSManagementService retrieved successfully.");
        }
        return service;
    }
}
