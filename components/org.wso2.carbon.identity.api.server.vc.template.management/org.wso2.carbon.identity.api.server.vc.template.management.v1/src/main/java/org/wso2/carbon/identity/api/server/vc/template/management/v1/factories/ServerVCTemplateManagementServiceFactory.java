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

package org.wso2.carbon.identity.api.server.vc.template.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.vc.template.management.common.VCTemplateManagementServiceHolder;
import org.wso2.carbon.identity.api.server.vc.template.management.v1.core.ServerVCTemplateManagementService;
import org.wso2.carbon.identity.openid4vc.template.management.VCTemplateManager;

/**
 * Factory class for {@link ServerVCTemplateManagementService}.
 */
public final class ServerVCTemplateManagementServiceFactory {

    private static final Log LOG = LogFactory.getLog(ServerVCTemplateManagementServiceFactory.class);
    private static final ServerVCTemplateManagementService SERVICE;

    static {
        VCTemplateManager vcTemplateManager = VCTemplateManagementServiceHolder
                .getVCTemplateManager();

        if (vcTemplateManager == null) {
            throw new IllegalStateException("VCTemplateManager is not available from OSGi context.");
        }

        SERVICE = new ServerVCTemplateManagementService(vcTemplateManager);
        if (LOG.isDebugEnabled()) {
            LOG.debug("ServerVCTemplateManagementService initialized successfully.");
        }
    }

    /**
     * Get VCTemplate Management Service.
     *
     * @return ServerVCTemplateManagementService.
     */
    public static ServerVCTemplateManagementService getServerVCTemplateManagementService() {

        return SERVICE;
    }
}
