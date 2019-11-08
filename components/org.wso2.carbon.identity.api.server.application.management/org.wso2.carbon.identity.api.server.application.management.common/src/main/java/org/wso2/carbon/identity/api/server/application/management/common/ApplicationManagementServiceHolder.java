/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.identity.api.server.application.management.common;

import org.wso2.carbon.identity.application.mgt.ApplicationManagementService;
import org.wso2.carbon.identity.oauth.OAuthAdminServiceImpl;
import org.wso2.carbon.identity.sso.saml.SAMLSSOConfigServiceImpl;
import org.wso2.carbon.security.sts.service.STSAdminServiceInterface;

/**
 * ApplicationManagementService OSGi service holder.
 */
public class ApplicationManagementServiceHolder {

    private static ApplicationManagementService applicationManagementService;

    private static OAuthAdminServiceImpl oauthAdminService;

    private static STSAdminServiceInterface stsAdminService;

    private static SAMLSSOConfigServiceImpl samlssoConfigService;

    public static ApplicationManagementService getApplicationManagementService() {

        return applicationManagementService;
    }

    public static void setApplicationManagementService(ApplicationManagementService applicationManagementService) {

        ApplicationManagementServiceHolder.applicationManagementService = applicationManagementService;
    }

    public static OAuthAdminServiceImpl getOAuthAdminService() {

        return oauthAdminService;
    }

    public static void setOauthAdminService(OAuthAdminServiceImpl oauthAdminService) {

        ApplicationManagementServiceHolder.oauthAdminService = oauthAdminService;
    }

    public static STSAdminServiceInterface getStsAdminService() {

        return stsAdminService;
    }

    public static void setStsAdminService(STSAdminServiceInterface stsAdminService) {

        ApplicationManagementServiceHolder.stsAdminService = stsAdminService;
    }

    public static SAMLSSOConfigServiceImpl getSamlssoConfigService() {

        return samlssoConfigService;
    }

    public static void setSamlssoConfigService(SAMLSSOConfigServiceImpl samlssoConfigService) {

        ApplicationManagementServiceHolder.samlssoConfigService = samlssoConfigService;
    }
}
