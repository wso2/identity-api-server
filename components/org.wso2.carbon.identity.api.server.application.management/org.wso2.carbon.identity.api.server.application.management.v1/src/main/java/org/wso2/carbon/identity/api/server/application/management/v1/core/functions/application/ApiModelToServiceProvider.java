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

import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.function.Function;

/**
 * Converts the API model object into a ServiceProvider object.
 */
public class ApiModelToServiceProvider implements Function<ApplicationModel, ServiceProvider> {

    @Override
    public ServiceProvider apply(ApplicationModel applicationModel) {

        ServiceProvider application = new ServiceProvider();

        application.setApplicationName(applicationModel.getName());
        application.setDescription(applicationModel.getDescription());
        application.setImageUrl(applicationModel.getImageUrl());
        application.setLoginUrl(applicationModel.getLoginUrl());

        addAdvancedConfigurationToApplication(applicationModel.getAdvancedConfigurations(), application);
        addClaimConfigurationToApplication(applicationModel.getClaimConfiguration(), application);
        addInboundAuthenticationProtocolsToApplication(applicationModel.getInboundProtocolConfiguration(), application);
        addAuthenticationSequence(applicationModel.getAuthenticationSequence(), application);
        addProvisioningConfiguration(applicationModel.getProvisioningConfigurations(), application);

        return application;
    }

    private void addInboundAuthenticationProtocolsToApplication(InboundProtocols inboundProtocolsModel,
                                                                ServiceProvider application) {

        InboundAuthenticationConfig inboundAuthConfig =
                new ApiModelToInboundAuthenticatorConfig().apply(inboundProtocolsModel);
        application.setInboundAuthenticationConfig(inboundAuthConfig);
    }

    private void addAuthenticationSequence(AuthenticationSequence authSequenceApiModel, ServiceProvider application) {

        new PatchAuthenticationSequence().accept(application, authSequenceApiModel);
//        if (authSequenceApiModel != null) {
//            updateRequestPathAuthenticatorConfigs(authSequenceApiModel, application);
//            // Authentication steps
//            LocalAndOutboundAuthenticationConfig localAndOutboundConfig = getLocalAndOutboundConfig(application);
//            updateAuthenticationSteps(authSequenceApiModel, localAndOutboundConfig);
//            updateAdaptiveAuthenticationScript(authSequenceApiModel, localAndOutboundConfig);
//        }
    }

//    private void updateAuthenticationSteps(AuthenticationSequence authSequenceApiModel,
//                                           LocalAndOutboundAuthenticationConfig localAndOutboundConfig) {
//
//        if (authSequenceApiModel.getType() != AuthenticationSequence.TypeEnum.DEFAULT) {
//
//            AuthenticationStep[] authenticationSteps = getAuthenticationSteps(authSequenceApiModel);
//
//            localAndOutboundConfig.setAuthenticationType(ApplicationConstants.AUTH_TYPE_FLOW);
//            localAndOutboundConfig.setAuthenticationSteps(authenticationSteps);
//        } else {
//            // We don't have to worry about setting authentication steps and related configs
//        }
//    }

//    private void updateAdaptiveAuthenticationScript(AuthenticationSequence authSequenceApiModel,
//                                                    LocalAndOutboundAuthenticationConfig localAndOutboundConfig) {
//
//        if (StringUtils.isNotBlank(authSequenceApiModel.getScript())) {
//            AuthenticationScriptConfig adaptiveScript = new AuthenticationScriptConfig();
//            adaptiveScript.setContent(authSequenceApiModel.getScript());
//
//            localAndOutboundConfig.setAuthenticationScriptConfig(adaptiveScript);
//        }
//    }
//
//    private void updateRequestPathAuthenticatorConfigs(AuthenticationSequence authSequenceApiModel,
//                                                       ServiceProvider application) {
//
//        Optional.ofNullable(authSequenceApiModel.getRequestPathAuthenticators())
//                .ifPresent(authenticators -> {
//                    RequestPathAuthenticatorConfig[] requestPathAuthenticatorConfigs =
//                            authenticators.stream()
//                                    .map(this::buildRequestPathConfig)
//                                    .toArray(RequestPathAuthenticatorConfig[]::new);
//
//                    // Set request path authenticator config to application
//                    application.setRequestPathAuthenticatorConfigs(requestPathAuthenticatorConfigs);
//                });
//    }

//    private AuthenticationStep[] getAuthenticationSteps(AuthenticationSequence authSequenceApiModel) {
//
//        if (CollectionUtils.isEmpty(authSequenceApiModel.getSteps())) {
//            throw Utils.buildClientError("Authentication steps cannot be empty for user defined " +
//                    "authentication type: " + AuthenticationSequence.TypeEnum.USER_DEFINED);
//        }
//
//        // Sort the authentication steps.
//        List<AuthenticationStepModel> sortedStepModelList = Optional.of(authSequenceApiModel.getSteps())
//                .map(steps -> {
//                    steps.sort(Comparator.comparingInt(AuthenticationStepModel::getId));
//                    return steps;
//                }).orElse(Collections.emptyList());
//
//        int numSteps = sortedStepModelList.size();
//        if (numSteps != sortedStepModelList.get(numSteps - 1).getId()) {
//            // This means the steps are not consecutive. ie. For steps to be consecutive the largest id needs
//            // to be equal to number of steps.
//            throw Utils.buildClientError("Step ids need to be consecutive in the authentication sequence steps.");
//        }
//
//        int subjectStepId = getSubjectStepId(authSequenceApiModel.getSubjectStepId(), numSteps);
//        int attributeStepId = getSubjectStepId(authSequenceApiModel.getAttributeStepId(), numSteps);
//
//        // We create a array of size (numSteps + 1) since step order starts from 1.
//        AuthenticationStep[] authenticationSteps = new AuthenticationStep[numSteps];
//
//        int stepOrder = 1;
//        for (AuthenticationStepModel stepModel : sortedStepModelList) {
//            AuthenticationStep authenticationStep = buildAuthenticationStep(stepModel);
//
//            authenticationStep.setStepOrder(stepOrder);
//            if (subjectStepId == stepOrder) {
//                authenticationStep.setSubjectStep(true);
//            }
//            if (attributeStepId == stepOrder) {
//                authenticationStep.setAttributeStep(true);
//            }
//            authenticationSteps[stepOrder - 1] = authenticationStep;
//
//            stepOrder++;
//        }
//        return authenticationSteps;
//    }

//    private AuthenticationStep buildAuthenticationStep(AuthenticationStepModel stepModel) {
//
//        AuthenticationStep authenticationStep = new AuthenticationStep();
//        // iteration the options, divide in to federated and local and add the configs
//        if (CollectionUtils.isEmpty(stepModel.getOptions())) {
//            throw Utils.buildClientError("Authentication Step options cannot be empty.");
//        }
//
//        List<LocalAuthenticatorConfig> localAuthOptions = new ArrayList<>();
//        List<IdentityProvider> federatedAuthOptions = new ArrayList<>();
//
//        stepModel.getOptions().forEach(option -> {
//            // TODO : add validations to swagger so that we don't need to check inputs here.
//            if (FrameworkConstants.LOCAL_IDP_NAME.equals(option.getIdp())) {
//                LocalAuthenticatorConfig localAuthOption = new LocalAuthenticatorConfig();
//                localAuthOption.setEnabled(true);
//                localAuthOption.setName(option.getAuthenticator());
//                localAuthOptions.add(localAuthOption);
//            } else {
//                FederatedAuthenticatorConfig federatedAuthConfig = new FederatedAuthenticatorConfig();
//                federatedAuthConfig.setEnabled(true);
//                federatedAuthConfig.setName(option.getAuthenticator());
//
//                IdentityProvider federatedIdp = new IdentityProvider();
//                federatedIdp.setIdentityProviderName(option.getIdp());
//                federatedIdp.setFederatedAuthenticatorConfigs(new
//                FederatedAuthenticatorConfig[]{federatedAuthConfig});
//                federatedIdp.setDefaultAuthenticatorConfig(federatedAuthConfig);
//                federatedAuthOptions.add(federatedIdp);
//            }
//        });
//
//        authenticationStep.setLocalAuthenticatorConfigs(localAuthOptions.toArray(new LocalAuthenticatorConfig[0]));
//        authenticationStep.setFederatedIdentityProviders(federatedAuthOptions.toArray(new IdentityProvider[0]));
//
//        return authenticationStep;
//    }

//    private int getSubjectStepId(Integer subjectStepId, int numSteps) {
//
//        return subjectStepId != null && subjectStepId <= numSteps ? subjectStepId : 1;
//    }
//
//    private RequestPathAuthenticatorConfig buildRequestPathConfig(String authenticator) {
//
//        RequestPathAuthenticatorConfig requestPath = new RequestPathAuthenticatorConfig();
//        requestPath.setName(authenticator);
//        requestPath.setEnabled(true);
//        return requestPath;
//    }

    private void addProvisioningConfiguration(ProvisioningConfiguration provisioningConfigApiModel,
                                              ServiceProvider application) {

        new PatchProvisioningConfiguration().accept(application, provisioningConfigApiModel);
//        if (provisioningConfigApiModel != null) {
//
//            Optional.ofNullable(provisioningConfigApiModel.getInboundProvisioning())
//                    .ifPresent(config -> {
//                        InboundProvisioningConfig inboundProvisioningConfig = getInboundProvisioningConfig
//                        (application);
//                        inboundProvisioningConfig.setDumbMode(Utils.getBooleanValue(config.getProxyMode()));
//                        inboundProvisioningConfig.setProvisioningUserStore(config.getProvisioningUserstoreDomain());
//
//                        application.setInboundProvisioningConfig(inboundProvisioningConfig);
//                    });
//
//            Optional.ofNullable(provisioningConfigApiModel.getOutboundProvisioningIdps())
//                    .ifPresent(idps -> {
//                        OutboundProvisioningConfig outboundProvisioningConfig = getOutboundProvisionConfig
//                        (application);
//                        IdentityProvider[] identityProviders =
//                                idps.stream()
//                                        .map(this::getProvisioningIdentityProvider)
//                                        .toArray(IdentityProvider[]::new);
//                        outboundProvisioningConfig.setProvisioningIdentityProviders(identityProviders);
//
//                        application.setOutboundProvisioningConfig(outboundProvisioningConfig);
//                    });
//        }
    }

//    private IdentityProvider getProvisioningIdentityProvider(OutboundProvisioningConfiguration config) {
//
//        IdentityProvider identityProvider = new IdentityProvider();
//        identityProvider.setIdentityProviderName(config.getIdp());
//
//        JustInTimeProvisioningConfig jitProvisioningConfig =
//                new JustInTimeProvisioningConfig();
//        jitProvisioningConfig.setProvisioningEnabled(Utils.getBooleanValue(config.getJit()));
//
//        identityProvider.setJustInTimeProvisioningConfig(jitProvisioningConfig);
//
//        ProvisioningConnectorConfig provisioningConfig = new ProvisioningConnectorConfig();
//        provisioningConfig.setName(config.getConnector());
//        provisioningConfig.setBlocking(config.getBlocking());
//        provisioningConfig.setRulesEnabled(config.getRules());
//
//        identityProvider.setDefaultProvisioningConnectorConfig(provisioningConfig);
//
//        return identityProvider;
//    }

    private void addClaimConfigurationToApplication(ClaimConfiguration claimApiModel, ServiceProvider application) {

        new PatchClaimConfiguration().accept(application, claimApiModel);
//        if (claimApiModel != null) {
//            ClaimConfig claimConfigs = getClaimConfig(application);
//            if (claimApiModel.getDialect() == ClaimConfiguration.DialectEnum.LOCAL) {
//                claimConfigs.setLocalClaimDialect(true);
//            }
//
//            // Requested claims / Claim mappings.
//            claimConfigs.setClaimMappings(getClaimMappings(claimApiModel));
//            // Role claim.
//            updateRoleClaimConfigs(claimApiModel.getRole(), application);
//            // Subject claim.
//            updateSubjectClaimConfigs(claimApiModel.getSubject(), application);
//        }
    }

//    private void updateSubjectClaimConfigs(SubjectConfig subject, ServiceProvider application) {
//
//        if (subject != null) {
//            ClaimConfig claimConfig = getClaimConfig(application);
//            claimConfig.setUserClaimURI(subject.getClaimId());
//            claimConfig.setAlwaysSendMappedLocalSubjectId(subject.getUseMappedLocalSubject());
//
//            LocalAndOutboundAuthenticationConfig authConfig = getLocalAndOutboundConfig(application);
//            authConfig.setUseTenantDomainInLocalSubjectIdentifier(subject.getIncludeTenantDomain());
//            authConfig.setUseUserstoreDomainInLocalSubjectIdentifier(subject.getIncludeUserDomain());
//        }
//    }
//
//    private void updateRoleClaimConfigs(RoleConfig roleApiModel, ServiceProvider application) {
//
//        if (roleApiModel != null) {
//            ClaimConfig claimConfig = getClaimConfig(application);
//            claimConfig.setRoleClaimURI(roleApiModel.getClaimId());
//
//            PermissionsAndRoleConfig permissionAndRoleConfig = getPermissionAndRoleConfig(application);
//            permissionAndRoleConfig.setRoleMappings(getRoleMappings(roleApiModel));
//
//            LocalAndOutboundAuthenticationConfig localAndOutboundConfig = getLocalAndOutboundConfig(application);
//            localAndOutboundConfig.setUseUserstoreDomainInRoles(roleApiModel.getIncludeUserDomain());
//        }
//    }

//    private RoleMapping[] getRoleMappings(RoleConfig roleClaimApiModel) {
//
//        return Optional.ofNullable(roleClaimApiModel.getMappings())
//                .map(roleMappings -> roleMappings.stream()
//                        .map(this::buildRoleMapping)
//                        .toArray(RoleMapping[]::new)
//                )
//                .orElse(new RoleMapping[0]);
//    }
//
//    private RoleMapping buildRoleMapping(
//            org.wso2.carbon.identity.api.server.application.management.v1.RoleMapping roleMapping) {
//
//        return new RoleMapping(new LocalRole(roleMapping.getLocalRole()), roleMapping.getApplicationRole());
//    }

//    private ClaimMapping[] getClaimMappings(ClaimConfiguration claimConfig) {
//
//        if (CollectionUtils.isEmpty(claimConfig.getClaimMappings())) {
//            return Optional.ofNullable(claimConfig.getRequestedClaims())
//                    .map(requestedClaims ->
//                            requestedClaims.stream()
//                                    .map(claim -> ClaimMapping.build(claim.getClaimUri(), claim.getClaimUri(), null,
//                                            claim.getMandatory()))
//                                    .toArray(ClaimMapping[]::new))
//                    .orElse(new ClaimMapping[0]);
//
//        } else {
//            Map<String, ClaimMapping> claimMappings = new HashMap<>();
//
//            // First add the claim mappings.
//            Optional.ofNullable(claimConfig.getClaimMappings())
//                    .ifPresent(mappings -> mappings.forEach(mapping ->
//                            claimMappings.put(mapping.getApplicationClaimUri(),
//                                    buildClaimMapping(mapping))));
//
//            // Set the request/mandatory claims from the defined claim mappings.
//            Optional.ofNullable(claimConfig.getRequestedClaims())
//                    .ifPresent(requestClaims -> {
//                                requestClaims.forEach(requestedClaim -> {
//                                    ClaimMapping claimMapping = claimMappings.get(requestedClaim.getClaimUri());
//                                    if (claimMapping != null) {
//                                        claimMapping.setRequested(true);
//                                        claimMapping.setMandatory(requestedClaim.getMandatory());
//                                    }
//                                });
//                            }
//                    );
//
//            return claimMappings.values().toArray(new ClaimMapping[0]);
//        }
//    }

//    private ClaimMapping buildClaimMapping(ClaimMappings mapping) {
//
//        return ClaimMapping.build(mapping.getApplicationClaimUri(), mapping.getLocalClaimUri(), null, false);
//    }

    private void addAdvancedConfigurationToApplication(AdvancedApplicationConfiguration config,
                                                       ServiceProvider serviceProvider) {

        new PatchAdvancedConfigurations().accept(serviceProvider, config);
//        if (config != null) {
//            serviceProvider.setSaasApp(config.getSaas());
//
//            LocalAndOutboundAuthenticationConfig localAndOutboundConfig = getLocalAndOutboundConfig(serviceProvider);
//            localAndOutboundConfig.setEnableAuthorization(config.getEnableAuthorization());
//            localAndOutboundConfig.setAlwaysSendBackAuthenticatedListOfIdPs(config.getReturnAuthenticatedIdpList());
//            localAndOutboundConfig.setSkipConsent(config.getSkipConsent());
//            updateCertificate(config.getCertificate(), serviceProvider);
//        }
    }

//    private void updateCertificate(Certificate certificate, ServiceProvider serviceProvider) {
//
//        if (certificate != null) {
//            if (certificate.getType() == Certificate.TypeEnum.PEM) {
//                serviceProvider.setCertificateContent(certificate.getValue());
//            } else if (certificate.getType() == Certificate.TypeEnum.JWKS) {
//                serviceProvider.setJwksUri(certificate.getValue());
//            }
//        }
//    }

//    private ClaimConfig getClaimConfig(ServiceProvider application) {
//
//        if (application.getClaimConfig() == null) {
//            application.setClaimConfig(new ClaimConfig());
//        }
//        return application.getClaimConfig();
//    }

//    private LocalAndOutboundAuthenticationConfig getLocalAndOutboundConfig(ServiceProvider application) {
//
//        if (application.getLocalAndOutBoundAuthenticationConfig() == null) {
//            application.setLocalAndOutBoundAuthenticationConfig(new LocalAndOutboundAuthenticationConfig());
//        }
//
//        return application.getLocalAndOutBoundAuthenticationConfig();
//    }

//    private PermissionsAndRoleConfig getPermissionAndRoleConfig(ServiceProvider application) {
//
//        if (application.getPermissionAndRoleConfig() == null) {
//            application.setPermissionAndRoleConfig(new PermissionsAndRoleConfig());
//        }
//
//        return application.getPermissionAndRoleConfig();
//    }

//    private OutboundProvisioningConfig getOutboundProvisionConfig(ServiceProvider application) {
//
//        if (application.getOutboundProvisioningConfig() == null) {
//            return new OutboundProvisioningConfig();
//        }
//        return application.getOutboundProvisioningConfig();
//    }
//
//    private InboundProvisioningConfig getInboundProvisioningConfig(ServiceProvider application) {
//
//        if (application.getInboundProvisioningConfig() == null) {
//            return new InboundProvisioningConfig();
//        }
//        return application.getInboundProvisioningConfig();
//    }

}
