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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationResponseModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationStepModel;
import org.wso2.carbon.identity.api.server.application.management.v1.Authenticator;
import org.wso2.carbon.identity.api.server.application.management.v1.Certificate;
import org.wso2.carbon.identity.api.server.application.management.v1.Claim;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimMappings;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocolListItem;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.RequestedClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.SubjectConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundsToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.provisioning.BuildProvisioningConfiguration;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.AuthenticationStep;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.RequestPathAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.RoleMapping;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.application.mgt.ApplicationConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converts the backend model ServiceProvider into the corresponding API model object.
 */
public class ServiceProviderToApiModel implements Function<ServiceProvider, ApplicationResponseModel> {

    private static final Log log = LogFactory.getLog(ServiceProviderToApiModel.class);

    @Override
    public ApplicationResponseModel apply(ServiceProvider application) {

        if (isResidentSp(application)) {
            return new ApplicationResponseModel()
                    .id(application.getApplicationResourceId())
                    .name(application.getApplicationName())
                    .description(application.getDescription())
                    .provisioningConfigurations(buildProvisioningConfiguration(application));
        } else {
            return new ApplicationResponseModel()
                    .id(application.getApplicationResourceId())
                    .name(application.getApplicationName())
                    .description(application.getDescription())
                    .imageUrl(application.getImageUrl())
                    .loginUrl(application.getLoginUrl())
                    .claimConfiguration(buildClaimConfiguration(application))
                    .inboundProtocols(buildInboundProtocols(application))
                    .advancedConfigurations(buildAdvancedAppConfiguration(application))
                    .provisioningConfigurations(buildProvisioningConfiguration(application))
                    .authenticationSequence(buildAuthenticationSequence(application));
        }
    }

    private List<InboundProtocolListItem> buildInboundProtocols(ServiceProvider application) {

        return new InboundsToApiModel().apply(application);
    }

    private boolean isResidentSp(ServiceProvider application) {

        return ApplicationConstants.LOCAL_SP.equalsIgnoreCase(application.getApplicationName());
    }

    private AuthenticationSequence buildAuthenticationSequence(ServiceProvider application) {

        LocalAndOutboundAuthenticationConfig authConfig = application.getLocalAndOutBoundAuthenticationConfig();

        AuthenticationSequence.TypeEnum authenticationType;
        if (authConfig == null || ApplicationConstants.AUTH_TYPE_DEFAULT.equals(authConfig.getAuthenticationType())) {
            ServiceProvider defaultSP = getDefaultServiceProvider();
            authConfig = defaultSP.getLocalAndOutBoundAuthenticationConfig();
            authenticationType = AuthenticationSequence.TypeEnum.DEFAULT;
        } else {
            // This means a user defined authentication sequence.
            authenticationType = AuthenticationSequence.TypeEnum.USER_DEFINED;
        }

        AuthenticationSequence authSequence = new AuthenticationSequence();
        authSequence.setType(authenticationType);
        if (authConfig.getAuthenticationScriptConfig() != null) {
            authSequence.script(authConfig.getAuthenticationScriptConfig().getContent());
        }

        if (authConfig.getAuthenticationSteps() != null) {
            Arrays.stream(authConfig.getAuthenticationSteps()).forEach(authenticationStep -> {
                authSequence.addStepsItem(buildAuthStep(authenticationStep));
                if (authenticationStep.isSubjectStep()) {
                    authSequence.setSubjectStepId(authenticationStep.getStepOrder());
                }
                if (authenticationStep.isAttributeStep()) {
                    authSequence.setAttributeStepId(authenticationStep.getStepOrder());
                }
            });

            if (authSequence.getSubjectStepId() == null) {
                // Set first step as default subject step.
                authSequence.setSubjectStepId(1);
            }

            if (authSequence.getAttributeStepId() == null) {
                // Set first step as default attribute step.
                authSequence.setAttributeStepId(1);
            }
        }

        RequestPathAuthenticatorConfig[] requestPathConfigs = application.getRequestPathAuthenticatorConfigs();
        if (requestPathConfigs != null) {
            Arrays.stream(requestPathConfigs).forEach(requestPathConfig ->
                    authSequence.addRequestPathAuthenticatorsItem(requestPathConfig.getName()));
        }

        return authSequence;
    }

    private ServiceProvider getDefaultServiceProvider() {

        ServiceProvider defaultSP;
        try {
            defaultSP = ApplicationManagementServiceHolder.getApplicationManagementService()
                    .getServiceProvider(IdentityApplicationConstants.DEFAULT_SP_CONFIG,
                            MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
        } catch (IdentityApplicationManagementException e) {
            throw Utils.buildServerErrorResponse(e, "Error while loading default SP configurations.");
        }
        return defaultSP;
    }

    private AuthenticationStepModel buildAuthStep(AuthenticationStep authenticationStep) {

        AuthenticationStepModel authStep = new AuthenticationStepModel();
        authStep.setId(authenticationStep.getStepOrder());

        if (authenticationStep.getFederatedIdentityProviders() != null) {
            Arrays.stream(authenticationStep.getFederatedIdentityProviders()).forEach(y -> authStep.addOptionsItem(
                    new Authenticator().idp(y.getIdentityProviderName())
                            .authenticator(y.getDefaultAuthenticatorConfig().getName()))
            );
        }

        if (authenticationStep.getLocalAuthenticatorConfigs() != null) {
            Arrays.stream(authenticationStep.getLocalAuthenticatorConfigs()).forEach(y -> authStep.addOptionsItem(
                    new Authenticator().idp(FrameworkConstants.LOCAL_IDP_NAME).authenticator(y.getName()))
            );
        }

        return authStep;
    }

    private ClaimConfiguration buildClaimConfiguration(ServiceProvider application) {

        return new ClaimConfiguration()
                .dialect(getDialect(application))
                .role(buildRoleConfig(application))
                .subject(buildSubjectClaimConfig(application))
                .requestedClaims(buildRequestClaims(application))
                .claimMappings(buildClaimMappings(application));
    }

    private List<ClaimMappings> buildClaimMappings(ServiceProvider application) {

        if (application.getClaimConfig() != null) {
            if (application.getClaimConfig().getClaimMappings() != null) {
                return Arrays.stream(application.getClaimConfig().getClaimMappings())
                        .map(claimMapping -> new ClaimMappings()
                                .applicationClaim(claimMapping.getRemoteClaim().getClaimUri())
                                .localClaim(buildClaimModel(claimMapping.getLocalClaim().getClaimUri())))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private List<RequestedClaimConfiguration> buildRequestClaims(ServiceProvider application) {

        if (application.getClaimConfig() != null) {
            if (application.getClaimConfig().getClaimMappings() != null) {
                return Arrays.stream(application.getClaimConfig().getClaimMappings())
                        .filter(ClaimMapping::isRequested)
                        .map(claimMapping -> new RequestedClaimConfiguration()
                                .claim(buildClaimModel(claimMapping.getRemoteClaim().getClaimUri()))
                                .mandatory(claimMapping.isMandatory()))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private SubjectConfig buildSubjectClaimConfig(ServiceProvider application) {

        SubjectConfig subjectConfig = new SubjectConfig();

        if (application.getClaimConfig() != null) {
            subjectConfig.useMappedLocalSubject(application.getClaimConfig().isAlwaysSendMappedLocalSubjectId());
        }

        LocalAndOutboundAuthenticationConfig localAndOutboundAuthConfig =
                application.getLocalAndOutBoundAuthenticationConfig();
        if (localAndOutboundAuthConfig != null) {
            subjectConfig.includeTenantDomain(localAndOutboundAuthConfig.isUseTenantDomainInLocalSubjectIdentifier());
            subjectConfig.includeUserDomain(localAndOutboundAuthConfig.isUseUserstoreDomainInLocalSubjectIdentifier());

            if (StringUtils.isBlank(localAndOutboundAuthConfig.getSubjectClaimUri())
                    && isLocalClaimDialectUsedBySp(application)) {
                subjectConfig.claim(buildClaimModel(FrameworkConstants.USERNAME_CLAIM));
            } else {
                subjectConfig.claim(buildClaimModel(localAndOutboundAuthConfig.getSubjectClaimUri()));
            }
        }

        return subjectConfig;
    }

    private ClaimConfiguration.DialectEnum getDialect(ServiceProvider application) {

        if (isLocalClaimDialectUsedBySp(application)) {
            return ClaimConfiguration.DialectEnum.LOCAL;
        } else {
            return ClaimConfiguration.DialectEnum.CUSTOM;
        }
    }

    private boolean isLocalClaimDialectUsedBySp(ServiceProvider application) {

        return application.getClaimConfig() != null && application.getClaimConfig().isLocalClaimDialect();
    }

    private RoleConfig buildRoleConfig(ServiceProvider application) {

        RoleConfig roleConfig = new RoleConfig();

        if (application.getClaimConfig() != null) {
            String roleClaimId = application.getClaimConfig().getRoleClaimURI();

            if (StringUtils.isBlank(roleClaimId) && application.getClaimConfig().isLocalClaimDialect()) {
                roleClaimId = FrameworkConstants.LOCAL_ROLE_CLAIM_URI;
            }
            roleConfig.claim(buildClaimModel(roleClaimId));
        }

        if (application.getLocalAndOutBoundAuthenticationConfig() != null) {
            roleConfig.includeUserDomain(
                    application.getLocalAndOutBoundAuthenticationConfig().isUseUserstoreDomainInRoles());
        }

        if (application.getPermissionAndRoleConfig() != null) {
            RoleMapping[] roleMappings = application.getPermissionAndRoleConfig().getRoleMappings();
            if (roleMappings != null) {
                Arrays.stream(roleMappings)
                        .forEach(roleMapping -> roleConfig.addMappingsItem(
                                new org.wso2.carbon.identity.api.server.application.management.v1.RoleMapping()
                                        .applicationRole(roleMapping.getRemoteRole())
                                        .localRole(roleMapping.getLocalRole().getLocalRoleName())
                        ));
            }
        }

        return roleConfig;
    }

    private ProvisioningConfiguration buildProvisioningConfiguration(ServiceProvider application) {

        return new BuildProvisioningConfiguration().apply(application);
    }

    private AdvancedApplicationConfiguration buildAdvancedAppConfiguration(ServiceProvider serviceProvider) {

        LocalAndOutboundAuthenticationConfig authConfig = serviceProvider.getLocalAndOutBoundAuthenticationConfig();
        if (authConfig == null) {
            authConfig = new LocalAndOutboundAuthenticationConfig();
        }

        return new AdvancedApplicationConfiguration()
                .saas(serviceProvider.isSaasApp())
                .discoverableByEndUsers(serviceProvider.isDiscoverable())
                .enableAuthorization(authConfig.isEnableAuthorization())
                .returnAuthenticatedIdpList(authConfig.isAlwaysSendBackAuthenticatedListOfIdPs())
                .skipConsent(authConfig.isSkipConsent())
                .certificate(getCertificate(serviceProvider));
    }

    private Certificate getCertificate(ServiceProvider serviceProvider) {

        Certificate certificate = new Certificate();
        if (StringUtils.isNotBlank(serviceProvider.getCertificateContent())) {
            certificate.type(Certificate.TypeEnum.PEM).value(serviceProvider.getCertificateContent());
        } else if (StringUtils.isNotBlank(serviceProvider.getJwksUri())) {
            certificate.type(Certificate.TypeEnum.JWKS).value(serviceProvider.getJwksUri());
        }

        return certificate;
    }

    private Claim buildClaimModel(String claimUri) {

        // TODO: will claim id and display name.
        return new Claim().uri(claimUri);
    }
}
