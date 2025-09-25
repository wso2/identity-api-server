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

package org.wso2.carbon.identity.api.server.flow.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.flow.management.common.FlowMgtServiceHolder;
import org.wso2.carbon.identity.api.server.flow.management.v1.core.ServerFlowMgtService;
import org.wso2.carbon.identity.flow.mgt.FlowMgtService;

/**
 * Factory class for FlowMgtService.
 */
public class ServerFlowMgtServiceFactory {

    private static final Log log = LogFactory.getLog(ServerFlowMgtServiceFactory.class);
    private static final ServerFlowMgtService SERVICE;

    static {
        log.info("Initializing ServerFlowMgtService factory.");
        FlowMgtService flowMgtService = FlowMgtServiceHolder
                .getMgtService();

        if (flowMgtService == null) {
            log.error("FlowMgtService is not available from OSGi context.");
            throw new IllegalStateException("FlowMgtService is not available from OSGi context.");
        }

        SERVICE = new ServerFlowMgtService(flowMgtService);
        log.info("ServerFlowMgtService factory initialized successfully.");
    }

    /**
     * Get FlowMgtService instance.
     *
     * @return FlowMgtService.
     */
    public static ServerFlowMgtService getFlowMgtService() {

        return SERVICE;
    }
}
