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

package org.wso2.carbon.identity.api.server.registration.execution.v1.factories;

import org.wso2.carbon.identity.api.server.registration.execution.common.RegistrationExecutionServiceHolder;
import org.wso2.carbon.identity.api.server.registration.execution.v1.core.UserRegistrationFlowServiceCore;
import org.wso2.carbon.identity.user.registration.engine.UserRegistrationFlowService;

/**
 * Factory class for RegistrationFlowMgtService.
 */
public class UserRegistrationFlowServiceFactory {

    private static final UserRegistrationFlowServiceCore SERVICE;

    static {
        UserRegistrationFlowService userRegistrationMgtService = RegistrationExecutionServiceHolder.getInstance();
        if (userRegistrationMgtService == null) {
            throw new IllegalStateException("UserRegistrationMgtService is not available from OSGi context.");
        }
        SERVICE = new UserRegistrationFlowServiceCore(userRegistrationMgtService);
    }

    /**
     * Get RegistrationFlowMgtService instance.
     *
     * @return RegistrationFlowMgtService instance
     */
    public static UserRegistrationFlowServiceCore getRegistrationFlowMgtService() {

        return SERVICE;
    }
}
