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

package org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.common.UserSharingMgtServiceHolder;
import org.wso2.carbon.identity.api.server.organization.user.sharing.management.v2.core.UsersApiServiceCore;
import org.wso2.carbon.identity.organization.management.organization.user.sharing.UserSharingPolicyHandlerServiceV2;

/**
 * Factory class for UsersApiService V2.
 */
public class UsersApiServiceCoreFactory {

    private static final Log LOG = LogFactory.getLog(UsersApiServiceCoreFactory.class);
    private static final UsersApiServiceCore SERVICE;

    static {
        LOG.debug("Initializing UsersApiServiceCoreFactory");
        UserSharingPolicyHandlerServiceV2 userSharingPolicyHandlerServiceV2 = UserSharingMgtServiceHolder
                .getUserSharingPolicyHandlerServiceV2();
        if (userSharingPolicyHandlerServiceV2 == null) {
            LOG.error(
                    "Failed to initialize UsersApiServiceCoreFactory: UserSharingPolicyHandlerServiceV2 is not " +
                            "available from OSGi context");
            throw new IllegalStateException(
                    "UserSharingPolicyHandlerServiceV2 is not available from the OSGi context.");
        }
        SERVICE = new UsersApiServiceCore(userSharingPolicyHandlerServiceV2);
    }

    /**
     * Get UsersApiServiceCore.
     *
     * @return UsersApiServiceCore.
     */
    public static UsersApiServiceCore getUsersApiServiceCore() {

        return SERVICE;
    }
}
