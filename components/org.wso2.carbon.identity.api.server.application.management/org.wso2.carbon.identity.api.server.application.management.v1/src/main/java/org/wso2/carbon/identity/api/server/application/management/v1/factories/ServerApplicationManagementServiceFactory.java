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

package org.wso2.carbon.identity.api.server.application.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementService;
import org.wso2.carbon.identity.template.mgt.TemplateManager;

/**
 * Factory class for ServerApplicationManagementService.
 */
public class ServerApplicationManagementServiceFactory {

    private static final Log log = LogFactory.getLog(ServerApplicationManagementServiceFactory.class);
    private  static final ServerApplicationManagementService SERVICE;

    static {
        if (log.isDebugEnabled()) {
            log.debug("Initializing ServerApplicationManagementService from OSGi context.");
        }
        ApplicationManagementService applicationManagementService = ApplicationManagementServiceHolder
                .getApplicationManagementService();
        AuthorizedAPIManagementService authorizedAPIManagementService = ApplicationManagementServiceHolder
                .getAuthorizedAPIManagementService();
        TemplateManager templateManager = ApplicationManagementServiceHolder.getTemplateManager();

        if (applicationManagementService == null) {
            log.error("ApplicationManagementService is not available from OSGi context.");
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }

        if (authorizedAPIManagementService == null) {
            log.error("AuthorizedAPIManagementService is not available from OSGi context.");
            throw new IllegalStateException("AuthorizedAPIManagementService is not available from OSGi context.");
        }

        if (templateManager == null) {
            log.error("TemplateManager is not available from OSGi context.");
            throw new IllegalStateException("TemplateManager is not available from OSGi context.");
        }

        SERVICE = new ServerApplicationManagementService(applicationManagementService, authorizedAPIManagementService,
                templateManager);
        if (log.isDebugEnabled()) {
            log.debug("Successfully initialized ServerApplicationManagementService.");
        }
    }

    /**
     * Get ServerAPIResourceManagementService instance.
     *
     * @return ServerAPIResourceManagementService.
     */
    public static ServerApplicationManagementService getServerApplicationManagementService() {

        return SERVICE;
    }
}
