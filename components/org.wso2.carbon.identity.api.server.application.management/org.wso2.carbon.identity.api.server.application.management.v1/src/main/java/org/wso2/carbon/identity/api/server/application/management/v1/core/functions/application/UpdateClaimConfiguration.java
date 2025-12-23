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

import org.wso2.carbon.CarbonConstants;
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
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants.ErrorMessage.ERROR_ASSERT_LOCAL_SUBJECT_IDENTIFIER_DISABLED;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.buildBadRequestError;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.setIfNotNull;

/**
 * Updates the claim configurations defined by the API model in the Service Provider model.
 */
public class UpdateClaimConfiguration implements UpdateFunction<ServiceProvider, ClaimConfiguration> {

    private static final Log log = LogFactory.getLog(UpdateClaimConfiguration.class);

    @Override
    public void apply(ServiceProvider application, ClaimConfiguration claimApiModel) {

        if (log.isDebugEnabled()) {
            log.debug("Updating claim configuration for application: " + 
                application.getApplicationName());
        }
        if (claimApiModel != null) {
            ClaimConfig applicationClaimConfiguration = getClaimConfig(application);

            // Check if dialect is local or a custom one.
            applicationClaimConfiguration.setLocalClaimDialect(isLocalDialect(claimApiModel.getDialect()));
            // Requested claims / Claim mappings.
            applicationClaimConfiguration.setClaimMappings(getClaimMappings(claimApiModel));
            // Role claim.
            updateRoleClaimConfigs(claimApiModel.getRole(), application);
            // Subject claim.
            updateSubjectClaimConfigs(claimApiModel.getSubject(), application);
            if (log.isDebugEnabled()) {
                log.debug("Successfully updated claim configuration for application: " + 
                    application.getApplicationName());
            }
        }
    }

    private boolean isLocalDialect(ClaimConfiguration.DialectEnum dialect) {

        return dialect == null || dialect == ClaimConfiguration.DialectEnum.LOCAL;
    }

    private ClaimConfig getClaimConfig(ServiceProvider application) {

        if (application.getClaimConfig() == null) {
            application.setClaimConfig(new ClaimConfig());
        }
        return application.getClaimConfig();
    }

    private ClaimMapping[] getClaimMappings(ClaimConfiguration claimConfigApiModel) {

        if (claimConfigApiModel.getClaimMappings() == null) {
            // No application level claim mappings. So simply mark requested claims if any.
            return Optional.ofNullable(claimConfigApiModel.getRequestedClaims())
                    .map(requestedClaims ->
                            requestedClaims.stream()
                                    .map(this::buildRequestClaimMapping).toArray(ClaimMapping[]::new))
                    .orElse(new ClaimMapping[0]);

        } else {
            // Application claim mappings defined. First build a map of application claim URI -> claim mapping.
            Map<String, ClaimMapping> claimMappings = claimConfigApiModel.getClaimMappings().stream()
                    .collect(Collectors.toMap(ClaimMappings::getApplicationClaim, this::buildClaimMapping));

            // Set the request/mandatory claims from the defined claim mappings.
            Optional.ofNullable(claimConfigApiModel.getRequestedClaims())
                    .ifPresent(requestedClaims -> {
                                requestedClaims.forEach(requestedClaim -> {
                                    // Check if claim mapping defined for requested claim.
                                    ClaimMapping claimMapping = claimMappings.get(getClaimUri(requestedClaim));
                                    if (claimMapping != null) {
                                        claimMapping.setRequested(true);
                                        // Mark claim as mandatory if the flag is set.
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

        return ClaimMapping.build(mapping.getLocalClaim().getUri(), mapping.getApplicationClaim(), null, false);
    }

    private void updateSubjectClaimConfigs(SubjectConfig subjectApiModel, ServiceProvider application) {

        if (subjectApiModel != null) {

            if (Boolean.TRUE.equals(subjectApiModel.getMappedLocalSubjectMandatory()) &&
                    Boolean.FALSE.equals(subjectApiModel.getUseMappedLocalSubject())) {
                log.warn("Invalid subject claim configuration: mapped local subject mandatory is true but " +
                    "use mapped local subject is false");
                throw buildBadRequestError(ERROR_ASSERT_LOCAL_SUBJECT_IDENTIFIER_DISABLED.getCode(),
                        ERROR_ASSERT_LOCAL_SUBJECT_IDENTIFIER_DISABLED.getDescription());
            }

            LocalAndOutboundAuthenticationConfig authConfig = getLocalAndOutboundConfig(application);
            if (subjectApiModel.getClaim() != null) {
                setIfNotNull(subjectApiModel.getClaim().getUri(), authConfig::setSubjectClaimUri);
            }
            setIfNotNull(subjectApiModel.getIncludeTenantDomain(),
                    authConfig::setUseTenantDomainInLocalSubjectIdentifier);
            setIfNotNull(subjectApiModel.getIncludeUserDomain(),
                    authConfig::setUseUserstoreDomainInLocalSubjectIdentifier);

            ClaimConfig claimConfig = getClaimConfig(application);
            setIfNotNull(subjectApiModel.getUseMappedLocalSubject(), claimConfig::setAlwaysSendMappedLocalSubjectId);
            setIfNotNull(subjectApiModel.getMappedLocalSubjectMandatory(), claimConfig::setMappedLocalSubjectMandatory);
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

        String localRoleName = roleMapping.getLocalRole();
        /*
        For the local roles with userstore domain prepended to the role name, the domain name should not be
        removed from the role name since userstore domain of a role is identified via the given role name. If the
        domain name is not available in the role, the role's domain will be considered as PRIMARY.
        */
        if (localRoleName.contains(CarbonConstants.DOMAIN_SEPARATOR)) {
            String userStoreId = IdentityUtil.extractDomainFromName(localRoleName);
            return new RoleMapping(new LocalRole(userStoreId, localRoleName), roleMapping.getApplicationRole());
        }
        return new RoleMapping(new LocalRole(localRoleName), roleMapping.getApplicationRole());
    }

    private PermissionsAndRoleConfig getPermissionAndRoleConfig(ServiceProvider application) {

        if (application.getPermissionAndRoleConfig() == null) {
            application.setPermissionAndRoleConfig(new PermissionsAndRoleConfig());
        }

        return application.getPermissionAndRoleConfig();
    }
}
