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

import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimMappings;
import org.wso2.carbon.identity.api.server.application.management.v1.RequestedClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.SubjectConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.application.common.model.ClaimConfig;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.LocalRole;
import org.wso2.carbon.identity.application.common.model.PermissionsAndRoleConfig;
import org.wso2.carbon.identity.application.common.model.RoleMapping;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.setIfNotNull;

/**
 * Updates the claim configurations defined by the API model in the Service Provider model.
 */
public class UpdateClaimConfiguration implements UpdateFunction<ServiceProvider, ClaimConfiguration> {

    @Override
    public void apply(ServiceProvider application, ClaimConfiguration claimApiModel) {

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

        if (claimConfig.getClaimMappings() == null) {
            return Optional.ofNullable(claimConfig.getRequestedClaims())
                    .map(requestedClaims ->
                            requestedClaims.stream()
                                    .map(this::buildRequestClaimMapping).toArray(ClaimMapping[]::new))
                    .orElse(new ClaimMapping[0]);

        } else {

            Map<String, ClaimMapping> claimMappings = claimConfig.getClaimMappings().stream()
                    .collect(Collectors.toMap(ClaimMappings::getApplicationClaim, this::buildClaimMapping));

            // Set the request/mandatory claims from the defined claim mappings.
            Optional.ofNullable(claimConfig.getRequestedClaims())
                    .ifPresent(requestClaims -> {
                                requestClaims.forEach(requestedClaim -> {
                                    ClaimMapping claimMapping = claimMappings.get(getClaimUri(requestedClaim));
                                    if (claimMapping != null) {
                                        claimMapping.setRequested(true);
                                        setIfNotNull(requestedClaim.getMandatory(), claimMapping::setMandatory);
                                    }
                                });
                            }
                    );

            return claimMappings.values().toArray(new ClaimMapping[0]);
        }
    }

    private String getClaimUri(RequestedClaimConfiguration requestedClaim) {

        // Request claim config cannot have a null claim object.
        return requestedClaim.getClaim().getUri();
    }

    private ClaimMapping buildRequestClaimMapping(RequestedClaimConfiguration requestedClaimConfiguration) {

        String claimUri = getClaimUri(requestedClaimConfiguration);
        ClaimMapping claimMapping = ClaimMapping.build(claimUri, claimUri, null, true);
        // Set whether claim is mandatory.
        setIfNotNull(requestedClaimConfiguration.getMandatory(), claimMapping::setMandatory);

        return claimMapping;
    }

    private ClaimMapping buildClaimMapping(ClaimMappings mapping) {

        return ClaimMapping.build(mapping.getApplicationClaim(), mapping.getLocalClaim().getUri(), null, false);
    }

    private void updateSubjectClaimConfigs(SubjectConfig subject, ServiceProvider application) {

        if (subject != null) {
            ClaimConfig claimConfig = getClaimConfig(application);
            if (subject.getClaim() != null) {
                setIfNotNull(subject.getClaim().getUri(), claimConfig::setUserClaimURI);
            }
            setIfNotNull(subject.getUseMappedLocalSubject(), claimConfig::setAlwaysSendMappedLocalSubjectId);

            LocalAndOutboundAuthenticationConfig authConfig = getLocalAndOutboundConfig(application);
            setIfNotNull(subject.getIncludeTenantDomain(), authConfig::setUseTenantDomainInLocalSubjectIdentifier);
            setIfNotNull(subject.getIncludeUserDomain(), authConfig::setUseUserstoreDomainInLocalSubjectIdentifier);
        }
    }

    private void updateRoleClaimConfigs(RoleConfig roleApiModel, ServiceProvider application) {

        if (roleApiModel != null) {
            ClaimConfig claimConfig = getClaimConfig(application);

            if (roleApiModel.getClaim() != null) {
                claimConfig.setRoleClaimURI(roleApiModel.getClaim().getUri());
            }

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

        LocalRole localRole = new LocalRole();
        localRole.setLocalRoleName(roleMapping.getLocalRole());

        return new RoleMapping(localRole, roleMapping.getApplicationRole());
    }

    private PermissionsAndRoleConfig getPermissionAndRoleConfig(ServiceProvider application) {

        if (application.getPermissionAndRoleConfig() == null) {
            application.setPermissionAndRoleConfig(new PermissionsAndRoleConfig());
        }

        return application.getPermissionAndRoleConfig();
    }
}
