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

package org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.factories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.common.UserInvitationMgtServiceHolder;
import org.wso2.carbon.identity.api.server.organization.user.invitation.management.v1.core.GuestApiServiceCore;
import org.wso2.carbon.identity.organization.user.invitation.management.InvitationCoreService;

/**
 * The factory class for GuestApiServiceCore.
 */
public class GuestApiServiceCoreFactory {

    private static final Log log = LogFactory.getLog(GuestApiServiceCoreFactory.class);
    private static final GuestApiServiceCore SERVICE;

    static {
        if (log.isDebugEnabled()) {
            log.debug("Initializing GuestApiServiceCoreFactory.");
        }
        InvitationCoreService invitationCoreService = UserInvitationMgtServiceHolder.getInvitationCoreService();

        if (invitationCoreService == null) {
            log.error("InvitationCoreService is not available from OSGi context.");
            throw new IllegalStateException("InvitationCoreService is not available from OSGi context.");
        }

        SERVICE = new GuestApiServiceCore(invitationCoreService);
        log.info("GuestApiServiceCoreFactory initialized successfully.");
    }

    /**
     * Get GuestApiServiceCore.
     *
     * @return GuestApiServiceCore.
     */
    public static GuestApiServiceCore getGuestApiServiceCore() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving GuestApiServiceCore instance.");
        }
        return SERVICE;
    }
}
