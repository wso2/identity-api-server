/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.organization.user.invitation.management.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.organization.user.invitation.management.InvitationCoreService;

/**
 * Holds the services which the shared user invitation management API component is using.
 */
public class UserInvitationMgtServiceHolder {

    private UserInvitationMgtServiceHolder() {

    }

    private static class InvitationCoreServiceHolder {

        static final InvitationCoreService SERVICE = (InvitationCoreService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(InvitationCoreService.class, null);
    }

    /**
     * Get Invitation Core osgi service.
     *
     * @return InvitationCoreService.
     */
    public static InvitationCoreService getInvitationCoreService() {

        return InvitationCoreServiceHolder.SERVICE;
    }
}
