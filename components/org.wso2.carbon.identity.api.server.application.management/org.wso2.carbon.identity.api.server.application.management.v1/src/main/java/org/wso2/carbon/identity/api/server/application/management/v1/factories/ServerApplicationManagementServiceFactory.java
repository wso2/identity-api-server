/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementService;
import org.wso2.carbon.identity.template.mgt.TemplateManager;

/**
 * Factory class for ServerApplicationManagementService.
 */
public class ServerApplicationManagementServiceFactory {

    private ServerApplicationManagementServiceFactory() {

    }

    private static class ServerApplicationManagementServiceHolder {

        private static final ServerApplicationManagementService SERVICE = createServiceInstance();
    }

    private static ServerApplicationManagementService createServiceInstance() {

        ApplicationManagementService applicationManagementService = getApplicationManagementService();
        AuthorizedAPIManagementService authorizedAPIManagementService = getAuthorizedAPIManagementService();
        TemplateManager templateManager = getTemplateManager();

        return new ServerApplicationManagementService(applicationManagementService, authorizedAPIManagementService,
                templateManager);
    }

    /**
     * Get ServerAPIResourceManagementService instance.
     *
     * @return ServerAPIResourceManagementService.
     */
    public static ServerApplicationManagementService getServerApplicationManagementService() {

        return ServerApplicationManagementServiceHolder.SERVICE;
    }

    private static ApplicationManagementService getApplicationManagementService() {

        ApplicationManagementService service = ApplicationManagementServiceHolder.getApplicationManagementService();
        if (service == null) {
            throw new IllegalStateException("ApplicationManagementService is not available from OSGi context.");
        }

        return service;
    }

    private static AuthorizedAPIManagementService getAuthorizedAPIManagementService() {

        AuthorizedAPIManagementService service = ApplicationManagementServiceHolder.getAuthorizedAPIManagementService();
        if (service == null) {
            throw new IllegalStateException("AuthorizedAPIManagementService is not available from OSGi context.");
        }

        return service;
    }

    private static TemplateManager getTemplateManager() {

        TemplateManager service = ApplicationManagementServiceHolder.getTemplateManager();
        if (service == null) {
            throw new IllegalStateException("TemplateManager is not available from OSGi context.");
        }

        return service;
    }
}
