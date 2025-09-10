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

package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.v1.AssociatedRolesConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.application.common.model.RoleV2;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Updates the associated roles configurations defined by the API model in the Service Provider model.
 */
public class UpdateAssociatedRoles implements UpdateFunction<ServiceProvider, AssociatedRolesConfig> {

    private static final Log log = LogFactory.getLog(UpdateAssociatedRoles.class);

    @Override
    public void apply(ServiceProvider serviceProvider, AssociatedRolesConfig associatedRolesConfig) {

        if (log.isDebugEnabled()) {
            log.debug("Updating associated roles config for application: " + 
                serviceProvider.getApplicationName());
        }
        org.wso2.carbon.identity.application.common.model.AssociatedRolesConfig rolesConfig =
                new org.wso2.carbon.identity.application.common.model.AssociatedRolesConfig();
        if (associatedRolesConfig != null) {
            rolesConfig.setAllowedAudience(associatedRolesConfig.getAllowedAudience().toString());
            if (associatedRolesConfig.getRoles() != null) {
                List<org.wso2.carbon.identity.application.common.model.RoleV2> listOfRoles =
                        associatedRolesConfig.getRoles().stream()
                                .map(role -> new org.wso2.carbon.identity.application.common.model.RoleV2(role.getId()))
                                .collect(Collectors.toList());
                rolesConfig.setRoles(listOfRoles.toArray(new RoleV2[0]));
            }
        }
        serviceProvider.setAssociatedRolesConfig(rolesConfig);
        if (log.isDebugEnabled()) {
            log.debug("Successfully updated associated roles config for application: " + 
                serviceProvider.getApplicationName());
        }
    }
}
