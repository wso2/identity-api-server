/*
 * Copyright (c) 2023-2025, WSO2 LLC. (http://www.wso2.com).
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

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.resource.collection.mgt.APIResourceCollectionManager;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.api.resource.mgt.AuthorizationDetailsTypeManager;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth.rar.core.AuthorizationDetailsSchemaValidator;

/**
 * Service holder class for api resource management.
 */
public class APIResourceManagementServiceHolder {

    private APIResourceManagementServiceHolder() {}

    private static class APIResourceManagerHolder {

        static final APIResourceManager SERVICE = (APIResourceManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(APIResourceManager.class, null);
    }

    private static class APIResourceCollectionManagerHolder {

        static final APIResourceCollectionManager SERVICE = (APIResourceCollectionManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(APIResourceCollectionManager.class, null);
    }

    private static class OAuthAdminServiceImplHolder {

        static final OAuthAdminServiceImpl SERVICE = (OAuthAdminServiceImpl) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OAuthAdminServiceImpl.class, null);
    }

    private static class AuthorizationDetailsTypeManagerHolder {

        static final AuthorizationDetailsTypeManager SERVICE = (AuthorizationDetailsTypeManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(AuthorizationDetailsTypeManager.class, null);
    }

    private static class AuthorizationDetailsSchemaValidatorHolder {

        static final AuthorizationDetailsSchemaValidator SERVICE =
                (AuthorizationDetailsSchemaValidator) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(AuthorizationDetailsSchemaValidator.class, null);
    }

    /**
     * Get APIResourceManager osgi service.
     *
     * @return APIResourceManager.
     */
    public static APIResourceManager getApiResourceManager() {

        return APIResourceManagerHolder.SERVICE;
    }

    /**
     * Get APIResourceCollectionManager osgi service.
     *
     * @return APIResourceCollectionManager.
     */
    public static APIResourceCollectionManager getApiResourceCollectionManager() {

        return APIResourceCollectionManagerHolder.SERVICE;
    }

    /**
     * Get OAuthAdminServiceImpl instance.
     *
     * @return OAuthAdminServiceImpl instance.
     */
    public static OAuthAdminServiceImpl getOAuthAdminServiceImpl() {

        return OAuthAdminServiceImplHolder.SERVICE;
    }

    /**
     * Set {@link AuthorizationDetailsTypeManager} instance.
     *
     * @return AuthorizationDetailsTypeManager instance.
     */
    public static AuthorizationDetailsTypeManager getAuthorizationDetailsTypeManager() {

        return AuthorizationDetailsTypeManagerHolder.SERVICE;
    }

    /**
     * Set {@link AuthorizationDetailsSchemaValidator} instance.
     *
     * @return AuthorizationDetailsSchemaValidator instance.
     */
    public static AuthorizationDetailsSchemaValidator getAuthorizationDetailsSchemaValidator() {

        return AuthorizationDetailsSchemaValidatorHolder.SERVICE;
    }
}
