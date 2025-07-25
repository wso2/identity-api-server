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

    private static final Log log = LogFactory.getLog(ApplicationManagementServiceHolder.class);

    private ApplicationManagementServiceHolder() {

    }

    private static class ApplicationServiceHolder {

        private static final Log log = LogFactory.getLog(ApplicationServiceHolder.class);
        static final ApplicationManagementService SERVICE = initializeService();

        private static ApplicationManagementService initializeService() {

            ApplicationManagementService service = (ApplicationManagementService) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(ApplicationManagementService.class, null);
            if (service == null) {
                log.warn("ApplicationManagementService is not available in the OSGi context.");
            }
            return service;
        }
    }

    private static class OAuthAdminServiceImplHolder {

        private static final Log log = LogFactory.getLog(OAuthAdminServiceImplHolder.class);
        static final OAuthAdminServiceImpl SERVICE = initializeService();

        private static OAuthAdminServiceImpl initializeService() {

            OAuthAdminServiceImpl service = (OAuthAdminServiceImpl) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(OAuthAdminServiceImpl.class, null);
            if (service == null) {
                log.warn("OAuthAdminServiceImpl is not available in the OSGi context.");
            }
            return service;
        }
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
                STSAdminServiceInterface service = (STSAdminServiceInterface) PrivilegedCarbonContext
                        .getThreadLocalCarbonContext().getOSGiService(STSAdminServiceInterface.class, null);
                if (service == null) {
                    log.warn("STSAdminServiceInterface is not available in the OSGi context.");
                }
                return service;
            } catch (NullPointerException e) {
                log.debug("NullPointerException occurred while retrieving STSAdminServiceInterface. " +
                        "Context or service might not be available.", e);
                return null;
            }
        }
    }

    private static class SAMLSSOConfigServiceImplHolder {

        private static final Log log = LogFactory.getLog(SAMLSSOConfigServiceImplHolder.class);
        static final SAMLSSOConfigServiceImpl SERVICE = initializeService();

        private static SAMLSSOConfigServiceImpl initializeService() {

            SAMLSSOConfigServiceImpl service = (SAMLSSOConfigServiceImpl) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(SAMLSSOConfigServiceImpl.class, null);
            if (service == null) {
                log.warn("SAMLSSOConfigServiceImpl is not available in the OSGi context.");
            }
            return service;
        }
    }

    private static class LoginFlowAIManagerServiceHolder {

        private static final Log log = LogFactory.getLog(LoginFlowAIManagerServiceHolder.class);
        static final LoginFlowAIManager SERVICE = initializeService();

        private static LoginFlowAIManager initializeService() {

            LoginFlowAIManager service = (LoginFlowAIManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                    .getOSGiService(LoginFlowAIManager.class, null);
            if (service == null) {
                log.warn("LoginFlowAIManager is not available in the OSGi context.");
            }
            return service;
        }
    }

    private static class OAuthServerConfigurationHolder {

        private static final Log log = LogFactory.getLog(OAuthServerConfigurationHolder.class);
        static final OAuthServerConfiguration SERVICE = initializeService();

        private static OAuthServerConfiguration initializeService() {

            OAuthServerConfiguration service = (OAuthServerConfiguration) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(OAuthServerConfiguration.class, null);
            if (service == null) {
                log.warn("OAuthServerConfiguration is not available in the OSGi context.");
            }
            return service;
        }
    }

    private static class TemplateManagerHolder {

        private static final Log log = LogFactory.getLog(TemplateManagerHolder.class);
        static final TemplateManager SERVICE = initializeService();

        private static TemplateManager initializeService() {

            TemplateManager service = (TemplateManager) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(TemplateManager.class, null);
            if (service == null) {
                log.warn("TemplateManager is not available in the OSGi context.");
            }
            return service;
        }
    }

    private static class CORSManagementServiceHolder {

        private static final Log log = LogFactory.getLog(CORSManagementServiceHolder.class);
        static final CORSManagementService SERVICE = initializeService();

        private static CORSManagementService initializeService() {

            CORSManagementService service = (CORSManagementService) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(CORSManagementService.class, null);
            if (service == null) {
                log.warn("CORSManagementService is not available in the OSGi context.");
            }
            return service;
        }
    }

    private static class RealmServiceHolder {

        private static final Log log = LogFactory.getLog(RealmServiceHolder.class);
        static final RealmService SERVICE = initializeService();

        private static RealmService initializeService() {

            RealmService service = (RealmService) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(RealmService.class, null);
            if (service == null) {
                log.warn("RealmService is not available in the OSGi context.");
            }
            return service;
        }
    }

    private static class APIResourceManagerHolder {

        private static final Log log = LogFactory.getLog(APIResourceManagerHolder.class);
        static final APIResourceManager SERVICE = initializeService();

        private static APIResourceManager initializeService() {

            APIResourceManager service = (APIResourceManager) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(APIResourceManager.class, null);
            if (service == null) {
                log.warn("APIResourceManager is not available in the OSGi context.");
            }
            return service;
        }
    }

    private static class AuthorizedAPIManagementServiceHolder {

        private static final Log log = LogFactory.getLog(AuthorizedAPIManagementServiceHolder.class);
        static final AuthorizedAPIManagementService SERVICE = initializeService();

        private static AuthorizedAPIManagementService initializeService() {

            AuthorizedAPIManagementService service = (AuthorizedAPIManagementService) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(AuthorizedAPIManagementService.class, null);
            if (service == null) {
                log.warn("AuthorizedAPIManagementService is not available in the OSGi context.");
            }
            return service;
        }
    }

    private static class OrgApplicationManagerHolder {

        private static final Log log = LogFactory.getLog(OrgApplicationManagerHolder.class);
        static final OrgApplicationManager SERVICE = initializeService();

        private static OrgApplicationManager initializeService() {

            OrgApplicationManager service = (OrgApplicationManager) PrivilegedCarbonContext
                    .getThreadLocalCarbonContext().getOSGiService(OrgApplicationManager.class, null);
            if (service == null) {
                log.warn("OrgApplicationManager is not available in the OSGi context.");
            }
            return service;
        }
    }

    /**
     * Get ApplicationManagementService.
     *
     * @return ApplicationManagementService.
     */
    public static ApplicationManagementService getApplicationManagementService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving ApplicationManagementService from OSGi context.");
        }
        return ApplicationServiceHolder.SERVICE;
    }

    /**
     * Get OAuthAdminServiceImpl.
     *
     * @return OAuthAdminServiceImpl.
     */
    public static OAuthAdminServiceImpl getOAuthAdminService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving OAuthAdminServiceImpl from OSGi context.");
        }
        return OAuthAdminServiceImplHolder.SERVICE;
    }

    /**
     * Get STSAdminServiceInterface.
     *
     * @return STSAdminServiceInterface.
     */
    public static STSAdminServiceInterface getStsAdminService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving STSAdminServiceInterface from OSGi context.");
        }
        return STSAdminServiceInterfaceHolder.SERVICE;
    }

    /**
     * Get SAMLSSOConfigServiceImpl.
     *
     * @return SAMLSSOConfigServiceImpl.
     */
    public static SAMLSSOConfigServiceImpl getSamlssoConfigService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving SAMLSSOConfigServiceImpl from OSGi context.");
        }
        return SAMLSSOConfigServiceImplHolder.SERVICE;
    }

    /**
     * Get OAuthServerConfiguration.
     *
     * @return OAuthServerConfiguration.
     */
    public static OAuthServerConfiguration getoAuthServerConfiguration() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving OAuthServerConfiguration from OSGi context.");
        }
        return OAuthServerConfigurationHolder.SERVICE;
    }

    /**
     * Get TemplateManager.
     *
     * @return TemplateManager.
     */
    public static TemplateManager getTemplateManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving TemplateManager from OSGi context.");
        }
        return TemplateManagerHolder.SERVICE;
    }

    /**
     * Get CORSManagementService.
     *
     * @return CORSManagementService.
     */
    public static CORSManagementService getCorsManagementService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving CORSManagementService from OSGi context.");
        }
        return CORSManagementServiceHolder.SERVICE;
    }

    /**
     * Get RealmService.
     *
     * @return RealmService.
     */
    public static RealmService getRealmService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving RealmService from OSGi context.");
        }
        return RealmServiceHolder.SERVICE;
    }

    /**
     * Get APIResourceManager.
     *
     * @return APIResourceManager.
     */
    public static APIResourceManager getApiResourceManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving APIResourceManager from OSGi context.");
        }
        return APIResourceManagerHolder.SERVICE;
    }

    /**
     * Get AuthorizedAPIManagementService.
     *
     * @return AuthorizedAPIManagementService.
     */
    public static AuthorizedAPIManagementService getAuthorizedAPIManagementService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving AuthorizedAPIManagementService from OSGi context.");
        }
        return AuthorizedAPIManagementServiceHolder.SERVICE;
    }

    /**
     * Get OrgApplicationManager OSGi service.
     *
     * @return OrgApplicationManager.
     */
    public static OrgApplicationManager getOrgApplicationManager() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving OrgApplicationManager from OSGi context.");
        }
        return OrgApplicationManagerHolder.SERVICE;
    }

    /**
     * Get LoginFlowAIManagementService.
     * @return LoginFlowAIManagementService.
     */
    public static LoginFlowAIManager getLoginFlowAIManagementService() {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving LoginFlowAIManager from OSGi context.");
        }
        return LoginFlowAIManagerServiceHolder.SERVICE;
    }
}
