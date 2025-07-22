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

package org.wso2.carbon.identity.api.server.flow.execution.v1.factories;

import org.wso2.carbon.identity.api.server.flow.execution.common.FlowExecutionServiceHolder;
import org.wso2.carbon.identity.api.server.flow.execution.v1.core.FlowExecutionServiceCore;
import org.wso2.carbon.identity.flow.execution.engine.FlowExecutionService;

/**
 * Factory class for FlowExecutionService.
 */
public class FlowExecutionServiceFactory {

    private static final FlowExecutionServiceCore SERVICE;

    static {
        FlowExecutionService flowExecutionService = FlowExecutionServiceHolder.getInstance();
        if (flowExecutionService == null) {
            throw new IllegalStateException("UserRegistrationFlowService is not available from OSGi context.");
        }
        SERVICE = new FlowExecutionServiceCore(flowExecutionService);
    }

    /**
     * Get FlowExecutionServiceCore instance.
     *
     * @return FlowExecutionServiceCore instance
     */
    public static FlowExecutionServiceCore getFlowExecutionService() {

        return SERVICE;
    }
}
