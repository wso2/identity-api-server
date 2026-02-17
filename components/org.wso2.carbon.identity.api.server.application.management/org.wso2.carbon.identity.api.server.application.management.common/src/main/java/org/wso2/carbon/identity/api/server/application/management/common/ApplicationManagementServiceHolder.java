/*
 * Copyright (c) 2019-2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.application.management.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.resource.mgt.APIResourceManager;
import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.application.mgt.AuthorizedAPIManagementService;
import org.wso2.carbon.identity.application.mgt.ai.LoginFlowAIManager;
import org.wso2.carbon.identity.cors.mgt.core.CORSManagementService;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.oauth.ciba.api.CibaAuthService;
import org.wso2.carbon.identity.oauth.ciba.api.CibaAuthServiceImpl;
import org.wso2.carbon.identity.oauth.config.OAuthServerConfiguration;
import org.wso2.carbon.identity.organization.management.application.OrgApplicationManager;
import org.wso2.carbon.identity.sso.saml.SAMLSSOConfigServiceImpl;
import org.wso2.carbon.identity.template.mgt.TemplateManager;
import org.wso2.carbon.security.sts.service.STSAdminServiceInterface;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * ApplicationManagementService OSGi service holder.
 */
public class ApplicationManagementServiceHolder {

    private ApplicationManagementServiceHolder() {

    }

    private static class ApplicationServiceHolder {

        static final ApplicationManagementService SERVICE = (ApplicationManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(ApplicationManagementService.class, null);
    }

    private static class OAuthAdminServiceImplHolder {

        static final OAuthAdminServiceImpl SERVICE = (OAuthAdminServiceImpl) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OAuthAdminServiceImpl.class, null);
    }

    private static class STSAdminServiceInterfaceHolder {

        private static final Log log = LogFactory.getLog(STSAdminServiceInterfaceHolder.class);
        static final STSAdminServiceInterface SERVICE = initializeService();

        /**
         * Initializes the STSAdminServiceInterface from the OSGi context.
         *
         * @return STSAdminServiceInterface or null if the service is unavailable.
         */
        private static STSAdminServiceInterface initializeService() {

            try {

                return (STSAdminServiceInterface) PrivilegedCarbonContext
                        .getThreadLocalCarbonContext().getOSGiService(STSAdminServiceInterface.class, null);
            } catch (NullPointerException e) {
                // Catch NullPointerException if the context or the service isn't properly set.
                log.debug("NullPointerException occurred while retrieving STSAdminServiceInterface. " +
                        "Context or service might not be available.");
                return null;
            }
        }
    }

    private static class SAMLSSOConfigServiceImplHolder {

        static final SAMLSSOConfigServiceImpl SERVICE = (SAMLSSOConfigServiceImpl) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(SAMLSSOConfigServiceImpl.class, null);
    }

    private static class LoginFlowAIManagerServiceHolder {

        static final LoginFlowAIManager SERVICE =
                (LoginFlowAIManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                        .getOSGiService(LoginFlowAIManager.class, null);
    }

    private static class OAuthServerConfigurationHolder {

        static final OAuthServerConfiguration SERVICE = (OAuthServerConfiguration) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OAuthServerConfiguration.class, null);
    }

    private static class TemplateManagerHolder {

        static final TemplateManager SERVICE = (TemplateManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(TemplateManager.class, null);
    }

    private static class CORSManagementServiceHolder {

        static final CORSManagementService SERVICE = (CORSManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(CORSManagementService.class, null);
    }

    private static class RealmServiceHolder {

        static final RealmService SERVICE = (RealmService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(RealmService.class, null);
    }

    private static class APIResourceManagerHolder {

        static final APIResourceManager SERVICE = (APIResourceManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(APIResourceManager.class, null);
    }

    private static class AuthorizedAPIManagementServiceHolder {

        static final AuthorizedAPIManagementService SERVICE = (AuthorizedAPIManagementService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(AuthorizedAPIManagementService.class, null);
    }

    private static class OrgApplicationManagerHolder {

        static final OrgApplicationManager SERVICE = (OrgApplicationManager) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(OrgApplicationManager.class, null);
    }

    private static class CibaAuthServiceHolder {

        static final CibaAuthServiceImpl SERVICE = (CibaAuthServiceImpl) PrivilegedCarbonContext
                .getThreadLocalCarbonContext().getOSGiService(CibaAuthService.class, null);
    }

    /**
     * Get ApplicationManagementService.
     *
     * @return ApplicationManagementService.
     */
    public static ApplicationManagementService getApplicationManagementService() {

        return ApplicationServiceHolder.SERVICE;
    }

    /**
     * Get OAuthAdminServiceImpl.
     *
     * @return OAuthAdminServiceImpl.
     */
    public static OAuthAdminServiceImpl getOAuthAdminService() {

        return OAuthAdminServiceImplHolder.SERVICE;
    }

    /**
     * Get STSAdminServiceInterface.
     *
     * @return STSAdminServiceInterface.
     */
    public static STSAdminServiceInterface getStsAdminService() {

        return STSAdminServiceInterfaceHolder.SERVICE;
    }

    /**
     * Get SAMLSSOConfigServiceImpl.
     *
     * @return SAMLSSOConfigServiceImpl.
     */
    public static SAMLSSOConfigServiceImpl getSamlssoConfigService() {

        return SAMLSSOConfigServiceImplHolder.SERVICE;
    }

    /**
     * Get OAuthServerConfiguration.
     *
     * @return OAuthServerConfiguration.
     */
    public static OAuthServerConfiguration getoAuthServerConfiguration() {

        return OAuthServerConfigurationHolder.SERVICE;
    }

    /**
     * Get TemplateManager.
     *
     * @return TemplateManager.
     */
    public static TemplateManager getTemplateManager() {

        return TemplateManagerHolder.SERVICE;
    }

    /**
     * Get CORSManagementService.
     *
     * @return CORSManagementService.
     */
    public static CORSManagementService getCorsManagementService() {

        return CORSManagementServiceHolder.SERVICE;
    }

    /**
     * Get RealmService.
     *
     * @return RealmService.
     */
    public static RealmService getRealmService() {

        return RealmServiceHolder.SERVICE;
    }

    /**
     * Get APIResourceManager.
     *
     * @return APIResourceManager.
     */
    public static APIResourceManager getApiResourceManager() {

        return APIResourceManagerHolder.SERVICE;
    }

    /**
     * Get AuthorizedAPIManagementService.
     *
     * @return AuthorizedAPIManagementService.
     */
    public static AuthorizedAPIManagementService getAuthorizedAPIManagementService() {

        return AuthorizedAPIManagementServiceHolder.SERVICE;
    }

    /**
     * Get OrgApplicationManager OSGi service.
     *
     * @return OrgApplicationManager.
     */
    public static OrgApplicationManager getOrgApplicationManager() {

        return OrgApplicationManagerHolder.SERVICE;
    }

    /**
     * Get LoginFlowAIManagementService.
     * @return LoginFlowAIManagementService.
     */
    public static LoginFlowAIManager getLoginFlowAIManagementService() {

        return LoginFlowAIManagerServiceHolder.SERVICE;
    }

    /**
     * Get CibaAuthService.
     *
     * @return CibaAuthService.
     */
    public static CibaAuthServiceImpl getCibaAuthService() {

        return CibaAuthServiceHolder.SERVICE;
    }
}
