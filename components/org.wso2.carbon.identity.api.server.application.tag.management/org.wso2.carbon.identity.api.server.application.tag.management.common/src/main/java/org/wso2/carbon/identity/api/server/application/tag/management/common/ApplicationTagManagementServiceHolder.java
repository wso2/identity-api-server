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

package org.wso2.carbon.identity.api.server.application.tag.management.common;

import org.wso2.carbon.identity.application.tag.mgt.ApplicationTagManager;

/**
 * Service holder class for Application Tags Management.
 */
public class ApplicationTagManagementServiceHolder {

    private static ApplicationTagManager applicationTagManager;

    /**
     * Get ApplicationTagManager osgi service.
     *
     * @return ApplicationTagManager.
     */
    public static ApplicationTagManager getApplicationTagManager() {

        return applicationTagManager;
    }

    /**
     * Set ApplicationTagManager osgi service.
     *
     * @param applicationTagManager ApplicationTagManager.
     */
    public static void setApplicationTagManager(ApplicationTagManager applicationTagManager) {

        ApplicationTagManagementServiceHolder.applicationTagManager = applicationTagManager;
    }
}
