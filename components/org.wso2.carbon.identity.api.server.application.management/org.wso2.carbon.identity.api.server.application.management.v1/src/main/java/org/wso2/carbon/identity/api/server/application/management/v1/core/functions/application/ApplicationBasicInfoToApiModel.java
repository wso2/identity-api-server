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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.core.ServerApplicationManagementService;
import org.wso2.carbon.identity.api.server.common.Constants;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ApplicationBasicInfo;
import org.wso2.carbon.identity.application.mgt.ApplicationConstants;
import org.wso2.carbon.identity.application.mgt.ApplicationMgtUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.util.Set;
import java.util.function.Function;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.APPLICATION_MANAGEMENT_PATH_COMPONENT;

/**
 * Converts the backend model ApplicationBasicInfo into the corresponding API model object.
 */
public class ApplicationBasicInfoToApiModel implements Function<ApplicationBasicInfo, ApplicationListItem> {

    private static final Log log = LogFactory.getLog(ServerApplicationManagementService.class);

    private static final Set<String> systemApplications =
            ApplicationManagementServiceHolder.getApplicationManagementService().getSystemApplications();

    @Override
    public ApplicationListItem apply(ApplicationBasicInfo applicationBasicInfo) {

        return new ApplicationListItem()
                .id(applicationBasicInfo.getApplicationResourceId())
                .name(applicationBasicInfo.getApplicationName())
                .description(applicationBasicInfo.getDescription())
                .image(applicationBasicInfo.getImageUrl())
                .accessUrl(applicationBasicInfo.getAccessUrl())
                .access(getAccess(applicationBasicInfo.getApplicationName()))
                .self(getApplicationLocation(applicationBasicInfo.getApplicationResourceId()));
    }

    private String getApplicationLocation(String resourceId) {

        return ContextLoader.buildURIForBody(
                Constants.V1_API_PATH_COMPONENT + APPLICATION_MANAGEMENT_PATH_COMPONENT + "/" + resourceId).toString();
    }

    private ApplicationListItem.AccessEnum getAccess(String applicationName) {

        String username = ContextLoader.getUsernameFromContext();
        String tenantDomain = ContextLoader.getTenantDomainFromContext();

        try {
            if (ApplicationConstants.LOCAL_SP.equals(applicationName) ||
                    (MultitenantConstants.SUPER_TENANT_DOMAIN_NAME.equals(tenantDomain) && systemApplications != null
                            && systemApplications.stream().anyMatch(applicationName::equalsIgnoreCase)) ||
                    !ApplicationMgtUtil.isUserAuthorized(applicationName, username)) {
                return ApplicationListItem.AccessEnum.READ;
            }
        } catch (IdentityApplicationManagementException e) {
            log.error("Failed to check user authorization for the application: " + applicationName, e);
            return ApplicationListItem.AccessEnum.READ;
        }

        return ApplicationListItem.AccessEnum.WRITE;
    }
}
