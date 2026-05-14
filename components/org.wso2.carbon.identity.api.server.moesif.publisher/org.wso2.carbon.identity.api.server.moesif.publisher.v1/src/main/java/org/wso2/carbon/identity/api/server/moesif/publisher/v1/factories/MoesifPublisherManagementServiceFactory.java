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

package org.wso2.carbon.identity.api.server.moesif.publisher.v1.factories;

import org.wso2.carbon.identity.api.server.moesif.publisher.common.MoesifPublisherServiceHolder;
import org.wso2.carbon.identity.api.server.moesif.publisher.v1.core.MoesifPublisherManagementService;
import org.wso2.carbon.identity.moesif.configuration.MoesifConfigurationManagementService;

/**
 * Factory for creating MoesifPublisherManagementService instances.
 */
public class MoesifPublisherManagementServiceFactory {

    private static final MoesifPublisherManagementService SERVICE;

    static {
        MoesifConfigurationManagementService osgiService =
                MoesifPublisherServiceHolder.getMoesifConfigurationManagementService();
        if (osgiService == null) {
            throw new IllegalStateException("MoesifConfigurationManagementService is not available.");
        }
        SERVICE = new MoesifPublisherManagementService(osgiService);
    }

    public static MoesifPublisherManagementService getMoesifPublisherManagementService() {

        return SERVICE;
    }
}
