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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.factory;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.UserSharingPolicyHandlerService;

/**
 * Factory Beans serves as a factory for creating other beans within the IOC container. This factory bean is used to
 * instantiate the User Sharing Policy Handler Service inside the container.
 */
public class UserSharingMgtOSGIServiceFactory extends AbstractFactoryBean<UserSharingPolicyHandlerService> {

    private UserSharingPolicyHandlerService userSharingPolicyHandlerService;

    @Override
    public Class<?> getObjectType() {

        return Object.class;
    }

    @Override
    protected UserSharingPolicyHandlerService createInstance() throws Exception {

        if (this.userSharingPolicyHandlerService == null) {
            UserSharingPolicyHandlerService userSharingPolicyHandlerService = (UserSharingPolicyHandlerService)
                    PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getOSGiService(UserSharingPolicyHandlerService.class, null);
            if (userSharingPolicyHandlerService == null) {
                throw new Exception("Unable to retrieve InvitationCoreService.");
            }
            this.userSharingPolicyHandlerService = userSharingPolicyHandlerService;
        }
        return this.userSharingPolicyHandlerService;
    }
}
