/*
 * Copyright (c) 2026, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.vp.template.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.vp.template.management.common.VPDefinitionManagementServiceHolder;
import org.wso2.carbon.identity.api.server.vp.template.management.v1.core.ServerVPDefinitionManagementService;
import org.wso2.carbon.identity.openid4vc.template.management.PresentationDefinitionManager;

/**
 * Factory class for {@link ServerVPDefinitionManagementService}.
 */
public final class ServerVPDefinitionManagementServiceFactory {

    private static final Log LOG = LogFactory.getLog(ServerVPDefinitionManagementServiceFactory.class);
    private static final ServerVPDefinitionManagementService SERVICE;

    static {
        PresentationDefinitionManager presentationDefinitionManager = VPDefinitionManagementServiceHolder
                .getPresentationDefinitionManager();

        if (presentationDefinitionManager == null) {
            throw new IllegalStateException("PresentationDefinitionManager is not available from OSGi context.");
        }

        SERVICE = new ServerVPDefinitionManagementService(presentationDefinitionManager);
        if (LOG.isDebugEnabled()) {
            LOG.debug("ServerVPDefinitionManagementService initialized successfully.");
        }
    }

    /**
     * Get VP Definition Management Service.
     *
     * @return ServerVPDefinitionManagementService.
     */
    public static ServerVPDefinitionManagementService getServerVPDefinitionManagementService() {

        return SERVICE;
    }
}
