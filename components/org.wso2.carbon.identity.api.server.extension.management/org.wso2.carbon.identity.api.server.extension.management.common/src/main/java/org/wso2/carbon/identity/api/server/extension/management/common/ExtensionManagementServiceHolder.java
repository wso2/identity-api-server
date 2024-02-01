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

package org.wso2.carbon.identity.api.server.extension.management.common;

import org.wso2.carbon.identity.extension.mgt.ExtensionManager;

/**
 * Extension management service holder.
 */
public class ExtensionManagementServiceHolder {

    private static ExtensionManagementServiceHolder instance = new ExtensionManagementServiceHolder();

    private ExtensionManager extensionManager;

    private ExtensionManagementServiceHolder() {}

    /**
     * Get instance of ExtensionManagementServiceHolder.
     *
     * @return ExtensionManagementServiceHolder.
     */
    public static ExtensionManagementServiceHolder getInstance() {

        return instance;
    }

    /**
     * Get ExtensionManager osgi service.
     *
     * @return ApplicationManagementService
     */
    public ExtensionManager getExtensionManager() {

        return ExtensionManagementServiceHolder.getInstance().extensionManager;
    }

    /**
     * Set ExtensionManager osgi service.
     *
     * @param extensionManager ExtensionManager.
     */
    public void setExtensionManager(ExtensionManager extensionManager) {

        ExtensionManagementServiceHolder.getInstance().extensionManager = extensionManager;
    }
}
