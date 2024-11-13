/*
 * Copyright (c) 2023-2024, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.api.resource.common;

import org.wso2.carbon.identity.api.resource.collection.mgt.APIResourceCollectionManager;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.api.resource.mgt.AuthorizationDetailsTypeManager;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;

/**
 * Service holder class for api resource management.
 */
public class APIResourceManagementServiceHolder {

    private static APIResourceManager apiResourceManager;
    private static APIResourceCollectionManager apiResourceCollectionManager;
    private static OAuthAdminServiceImpl oAuthAdminServiceImpl;
    private static AuthorizationDetailsTypeManager authorizationDetailsTypeManager;

    /**
     * Get APIResourceManager osgi service.
     *
     * @return APIResourceManager.
     */
    public static APIResourceManager getApiResourceManager() {

        return apiResourceManager;
    }

    /**
     * Set APIResourceManager osgi service.
     *
     * @param apiResourceManager APIResourceManager.
     */
    public static void setApiResourceManager(APIResourceManager apiResourceManager) {

        APIResourceManagementServiceHolder.apiResourceManager = apiResourceManager;
    }

    /**
     * Get APIResourceCollectionManager osgi service.
     *
     * @return APIResourceCollectionManager.
     */
    public static APIResourceCollectionManager getApiResourceCollectionManager() {

        return apiResourceCollectionManager;
    }

    /**
     * Set APIResourceCollectionManager osgi service.
     *
     * @param apiResourceCollectionManager APIResourceCollectionManager.
     */
    public static void setApiResourceCollectionManager(APIResourceCollectionManager apiResourceCollectionManager) {

        APIResourceManagementServiceHolder.apiResourceCollectionManager = apiResourceCollectionManager;
    }

    /**
     * Get OAuthAdminServiceImpl instance.
     *
     * @return OAuthAdminServiceImpl instance.
     */
    public static OAuthAdminServiceImpl getOAuthAdminServiceImpl() {

        return oAuthAdminServiceImpl;
    }

    /**
     * Set OAuthAdminServiceImpl instance.
     *
     * @param oAuthAdminServiceImpl OAuthAdminServiceImpl instance.
     */
    public static void setOAuthAdminServiceImpl(OAuthAdminServiceImpl oAuthAdminServiceImpl) {

        APIResourceManagementServiceHolder.oAuthAdminServiceImpl = oAuthAdminServiceImpl;
    }

    /**
     * Set {@link AuthorizationDetailsTypeManager} instance.
     *
     * @return AuthorizationDetailsTypeManager instance.
     */
    public static AuthorizationDetailsTypeManager getAuthorizationDetailsTypeManager() {

        return authorizationDetailsTypeManager;
    }

    /**
     * Set {@link AuthorizationDetailsTypeManager} instance.
     *
     * @param authorizationDetailsTypeManager AuthorizationDetailsTypeManager instance.
     */
    public static void setAuthorizationDetailsTypeManager(
            AuthorizationDetailsTypeManager authorizationDetailsTypeManager) {

        APIResourceManagementServiceHolder.authorizationDetailsTypeManager = authorizationDetailsTypeManager;
    }
}
