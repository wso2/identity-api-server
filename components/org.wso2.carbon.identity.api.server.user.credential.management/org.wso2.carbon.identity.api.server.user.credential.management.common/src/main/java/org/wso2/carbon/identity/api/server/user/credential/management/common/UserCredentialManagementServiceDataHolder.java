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

package org.wso2.carbon.identity.api.server.user.credential.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.application.authenticator.fido2.core.WebAuthnService;
import org.wso2.carbon.identity.notification.push.device.handler.DeviceHandlerService;

/**
 * Service holder class for credential management related services.
 */
public class UserCredentialManagementServiceDataHolder {

    private UserCredentialManagementServiceDataHolder() {

    }

    private static class WebAuthnServiceHolder {

        private static final WebAuthnService SERVICE = new WebAuthnService();
    }

    private static class PushDeviceHandlerHolder {

        private static final DeviceHandlerService SERVICE = (DeviceHandlerService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(DeviceHandlerService.class, null);
    }

    /**
     * Get WebAuthnService OSGi service.
     *
     * @return WebAuthnService
     */
    public static WebAuthnService getWebAuthnService() {

        return WebAuthnServiceHolder.SERVICE;
    }

    /**
     * Get Push DeviceHandler OSGi service.
     *
     * @return DeviceHandler
     */
    public static DeviceHandlerService getPushDeviceHandler() {

        return PushDeviceHandlerHolder.SERVICE;
    }
}
