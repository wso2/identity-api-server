/*
 * Copyright (c) 2023-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.extension.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.extension.mgt.ExtensionManager;

/**
 * Extension management service holder.
 */
public class ExtensionManagementServiceHolder {

    private static final Log log = LogFactory.getLog(ExtensionManagementServiceHolder.class);

    private ExtensionManagementServiceHolder() {}

    private static class ExtensionManagerHolder {

        static final ExtensionManager SERVICE = (ExtensionManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ExtensionManager.class, null);
    }

    /**
     * Get ExtensionManager osgi service.
     *
     * @return ExtensionManager
     */
    public static ExtensionManager getExtensionManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving ExtensionManager OSGi service.");
        }
        ExtensionManager extensionManager = ExtensionManagerHolder.SERVICE;
        if (extensionManager == null) {
            log.warn("ExtensionManager OSGi service is not available.");
        }
        return extensionManager;
    }
}
