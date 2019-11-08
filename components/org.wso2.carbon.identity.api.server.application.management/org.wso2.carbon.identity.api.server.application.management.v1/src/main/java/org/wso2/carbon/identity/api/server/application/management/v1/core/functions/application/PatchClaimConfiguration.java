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

import org.apache.commons.collections.CollectionUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimMappings;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.SubjectConfig;
import org.wso2.carbon.identity.application.common.model.ClaimConfig;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.LocalRole;
import org.wso2.carbon.identity.application.common.model.PermissionsAndRoleConfig;
import org.wso2.carbon.identity.application.common.model.RoleMapping;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.setIfNotNull;

/**
 * Updates the claim configurations defined by the API model in the Service Provider model.
 */
public class PatchClaimConfiguration implements BiConsumer<ServiceProvider, ClaimConfiguration> {

    @Override
    public void accept(ServiceProvider application, ClaimConfiguration claimApiModel) {

        if (claimApiModel != null) {
            ClaimConfig claimConfigs = getClaimConfig(application);
            if (claimApiModel.getDialect() == ClaimConfiguration.DialectEnum.LOCAL) {
                claimConfigs.setLocalClaimDialect(true);
            }

            // Requested claims / Claim mappings.
            claimConfigs.setClaimMappings(getClaimMappings(claimApiModel));
            // Role claim.
            updateRoleClaimConfigs(claimApiModel.getRole(), application);
            // Subject claim.
            updateSubjectClaimConfigs(claimApiModel.getSubject(), application);
        }
    }

    private ClaimConfig getClaimConfig(ServiceProvider application) {

        if (application.getClaimConfig() == null) {
            application.setClaimConfig(new ClaimConfig());
        }
        return application.getClaimConfig();
    }

    private ClaimMapping[] getClaimMappings(ClaimConfiguration claimConfig) {

        if (CollectionUtils.isEmpty(claimConfig.getClaimMappings())) {
            return Optional.ofNullable(claimConfig.getRequestedClaims())
                    .map(requestedClaims ->
                            requestedClaims.stream()
                                    .map(claim -> ClaimMapping.build(claim.getClaimUri(), claim.getClaimUri(), null,
                                            claim.getMandatory()))
                                    .toArray(ClaimMapping[]::new))
                    .orElse(new ClaimMapping[0]);

        } else {
            Map<String, ClaimMapping> claimMappings = new HashMap<>();

            // First add the claim mappings.
            Optional.ofNullable(claimConfig.getClaimMappings())
                    .ifPresent(mappings -> mappings.forEach(mapping ->
                            claimMappings.put(mapping.getApplicationClaimUri(),
                                    buildClaimMapping(mapping))));

            // Set the request/mandatory claims from the defined claim mappings.
            Optional.ofNullable(claimConfig.getRequestedClaims())
                    .ifPresent(requestClaims -> {
                                requestClaims.forEach(requestedClaim -> {
                                    ClaimMapping claimMapping = claimMappings.get(requestedClaim.getClaimUri());
                                    if (claimMapping != null) {
                                        claimMapping.setRequested(true);
                                        claimMapping.setMandatory(requestedClaim.getMandatory());
                                    }
                                });
                            }
                    );

            return claimMappings.values().toArray(new ClaimMapping[0]);
        }
    }

    private ClaimMapping buildClaimMapping(ClaimMappings mapping) {

        return ClaimMapping.build(mapping.getApplicationClaimUri(), mapping.getLocalClaimUri(), null, false);
    }

    private void updateSubjectClaimConfigs(SubjectConfig subject, ServiceProvider application) {

        if (subject != null) {
            ClaimConfig claimConfig = getClaimConfig(application);
            setIfNotNull(subject.getClaimId(), claimConfig::setUserClaimURI);
            setIfNotNull(subject.getUseMappedLocalSubject(), claimConfig::setAlwaysSendMappedLocalSubjectId);

            LocalAndOutboundAuthenticationConfig authConfig = getLocalAndOutboundConfig(application);
            setIfNotNull(subject.getIncludeTenantDomain(), authConfig::setUseTenantDomainInLocalSubjectIdentifier);
            setIfNotNull(subject.getIncludeUserDomain(), authConfig::setUseUserstoreDomainInLocalSubjectIdentifier);
        }
    }

    private void updateRoleClaimConfigs(RoleConfig roleApiModel, ServiceProvider application) {

        if (roleApiModel != null) {
            ClaimConfig claimConfig = getClaimConfig(application);
            claimConfig.setRoleClaimURI(roleApiModel.getClaimId());

            PermissionsAndRoleConfig permissionAndRoleConfig = getPermissionAndRoleConfig(application);
            permissionAndRoleConfig.setRoleMappings(getRoleMappings(roleApiModel));

            LocalAndOutboundAuthenticationConfig localAndOutboundConfig = getLocalAndOutboundConfig(application);
            setIfNotNull(roleApiModel.getIncludeUserDomain(), localAndOutboundConfig::setUseUserstoreDomainInRoles);
        }
    }

    private LocalAndOutboundAuthenticationConfig getLocalAndOutboundConfig(ServiceProvider application) {

        if (application.getLocalAndOutBoundAuthenticationConfig() == null) {
            application.setLocalAndOutBoundAuthenticationConfig(new LocalAndOutboundAuthenticationConfig());
        }

        return application.getLocalAndOutBoundAuthenticationConfig();
    }

    private RoleMapping[] getRoleMappings(RoleConfig roleClaimApiModel) {

        return Optional.ofNullable(roleClaimApiModel.getMappings())
                .map(roleMappings -> roleMappings.stream()
                        .map(this::buildRoleMapping)
                        .toArray(RoleMapping[]::new)
                )
                .orElse(new RoleMapping[0]);
    }

    private RoleMapping buildRoleMapping(
            org.wso2.carbon.identity.api.server.application.management.v1.RoleMapping roleMapping) {

        return new RoleMapping(new LocalRole(roleMapping.getLocalRole()), roleMapping.getApplicationRole());
    }

    private PermissionsAndRoleConfig getPermissionAndRoleConfig(ServiceProvider application) {

        if (application.getPermissionAndRoleConfig() == null) {
            application.setPermissionAndRoleConfig(new PermissionsAndRoleConfig());
        }

        return application.getPermissionAndRoleConfig();
    }
}
