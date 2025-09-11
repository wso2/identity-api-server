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
import org.wso2.carbon.identity.api.server.flow.management.v1.core.FlowAIServiceCore;

/**
 * Factory class for FlowAIServiceCore.
 */
public class FlowAIServiceFactory {

    private static final Log log = LogFactory.getLog(FlowAIServiceFactory.class);
    private static final FlowAIServiceCore SERVICE;

    static {
        log.info("Initializing FlowAIService factory.");
        SERVICE = new FlowAIServiceCore();
        log.info("FlowAIService factory initialized successfully.");
    }

    /**
     * Get FlowAIServiceCore instance.
     *
     * @return FlowAIServiceCore.
     */
    public static FlowAIServiceCore getFlowAIService() {

        return SERVICE;
    }

}
