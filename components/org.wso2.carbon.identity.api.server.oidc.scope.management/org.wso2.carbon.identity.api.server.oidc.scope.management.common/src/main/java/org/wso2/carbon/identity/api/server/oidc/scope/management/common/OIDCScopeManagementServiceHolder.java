/*
 * Copyright (c) 2020-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.oidc.scope.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;

/**
 * OIDCScopeManagementService OSGi service holder.
 */
public class OIDCScopeManagementServiceHolder {

    private static final Log log = LogFactory.getLog(OIDCScopeManagementServiceHolder.class);

    private OIDCScopeManagementServiceHolder () {}

    private static class OAuthAdminServiceImplServiceHolder {

        static final OAuthAdminServiceImpl SERVICE = (OAuthAdminServiceImpl) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OAuthAdminServiceImpl.class, null);
    }

    /**
     * Get OAuthAdminService OSGi service.
     *
     * @return OAuthAdminServiceImpl
     */
    public static OAuthAdminServiceImpl getOAuthAdminService() {

        OAuthAdminServiceImpl service = OAuthAdminServiceImplServiceHolder.SERVICE;
        if (log.isDebugEnabled()) {
            log.debug("Retrieved OAuthAdminService from OSGi context.");
        }
        if (service == null) {
            log.warn("OAuthAdminService is not available in the OSGi context.");
        }
        return service;
    }
}
