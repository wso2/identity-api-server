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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationStep;
import org.wso2.carbon.identity.api.server.application.management.v1.Authenticator;
import org.wso2.carbon.identity.api.server.application.management.v1.Certificate;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimMappings;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundSCIMProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OpenIDConnectConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.OutboundProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.PassiveStsConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.RequestedClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.api.server.application.management.v1.SubjectConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.WSTrustConfiguration;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.StandardInboundProtocols;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.InboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.OutboundProvisioningConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.RequestPathAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.RoleMapping;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.PassiveSTS;
import org.wso2.carbon.identity.application.mgt.ApplicationConstants;
import org.wso2.carbon.identity.base.IdentityConstants;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.oauth.IdentityOAuthAdminException;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOServiceProviderDTO;
import org.wso2.carbon.security.SecurityConfigException;
import org.wso2.carbon.security.sts.service.util.TrustedServiceData;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.JWKS_URI_SP_PROPERTY_NAME;

/**
 * Converts the backend model ServiceProvider into the corresponding API model object.
 */
public class ServiceProviderToApiModel implements Function<ServiceProvider, ApplicationModel> {

    private static final Log log = LogFactory.getLog(ServiceProviderToApiModel.class);

    @Override
    public ApplicationModel apply(ServiceProvider application) {

        // TODO: if local do not fill claim mappings.
        if (isResidentSp(application)) {
            return new ApplicationModel()
                    .id(application.getApplicationResourceId())
                    .name(application.getApplicationName())
                    .description(application.getDescription())
                    .provisioningConfigurations(buildProvisioningConfiguration(application));
        } else {
            return new ApplicationModel()
                    .id(application.getApplicationResourceId())
                    .name(application.getApplicationName())
                    .description(application.getDescription())
                    .imageUrl(application.getImageUrl())
                    .loginUrl(application.getLoginUrl())
                    .claimConfiguration(buildClaimConfiguration(application))
                    .inboundProtocolConfiguration(buildInboundProtocols(application.getInboundAuthenticationConfig()))
                    .advancedConfigurations(buildAdvancedAppConfiguration(application))
                    .provisioningConfigurations(buildProvisioningConfiguration(application))
                    .authenticationSequence(buildAuthenticationSequence(application));
        }
    }

    private boolean isResidentSp(ServiceProvider application) {

        return ApplicationConstants.LOCAL_SP.equalsIgnoreCase(application.getApplicationName());
    }

    private AuthenticationSequence buildAuthenticationSequence(ServiceProvider application) {

        LocalAndOutboundAuthenticationConfig authConfig = application.getLocalAndOutBoundAuthenticationConfig();

        if (authConfig == null || ApplicationConstants.AUTH_TYPE_DEFAULT.equals(authConfig.getAuthenticationType())) {
            ServiceProvider defaultSP = getDefaultServiceProvider();
            authConfig = defaultSP.getLocalAndOutBoundAuthenticationConfig();
        }

        AuthenticationSequence authSequence = new AuthenticationSequence();
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
        }

        RequestPathAuthenticatorConfig[] requestPathConfigs = application.getRequestPathAuthenticatorConfigs();
        if (requestPathConfigs != null) {
            Arrays.stream(requestPathConfigs)
                    .forEach(requestPathConfig ->
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
            throw handleServerError(e, "Error while loading default SP configurations.");
        }
        return defaultSP;
    }

    private AuthenticationStep buildAuthStep(org.wso2.carbon.identity.application.common.model.AuthenticationStep
                                                     authenticationStep) {

        AuthenticationStep authStep = new AuthenticationStep();
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
                                .applicationClaimUri(claimMapping.getRemoteClaim().getClaimUri())
                                .localClaimUri(claimMapping.getLocalClaim().getClaimUri()))
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
                                .claimUri(claimMapping.getRemoteClaim().getClaimUri())
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
                subjectConfig.setClaimId(FrameworkConstants.USERNAME_CLAIM);
            } else {
                subjectConfig.setClaimId(localAndOutboundAuthConfig.getSubjectClaimUri());
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
            roleConfig.setClaimId(roleClaimId);
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

        ProvisioningConfiguration config = new ProvisioningConfiguration();

        if (application.getInboundAuthenticationConfig() != null) {
            config.inboundProvisioning(buildInboundProvisioningConfig(application.getInboundProvisioningConfig()));
        }

        if (application.getOutboundProvisioningConfig() != null) {
            List<OutboundProvisioningConfiguration> provisioningIdps =
                    buildOutboundProvisioningConfig(application.getOutboundProvisioningConfig());

            if (provisioningIdps != null && !provisioningIdps.isEmpty()) {
                config.outboundProvisioningIdps(provisioningIdps);
            }
        }

        return config;
    }

    private List<OutboundProvisioningConfiguration> buildOutboundProvisioningConfig(
            OutboundProvisioningConfig outboundProvisioningConfig) {

        if (outboundProvisioningConfig.getProvisioningIdentityProviders() != null) {
            return Arrays.stream(outboundProvisioningConfig.getProvisioningIdentityProviders())
                    .map(new ProvisioningIdpToApiModel())
                    .collect(Collectors.toList());
        }

        return null;
    }

    private InboundSCIMProvisioningConfiguration buildInboundProvisioningConfig(
            InboundProvisioningConfig inboundProvisioningConfig) {

        if (inboundProvisioningConfig.isDumbMode()) {
            return new InboundSCIMProvisioningConfiguration().proxyMode(true);
        } else if (StringUtils.isNotBlank(inboundProvisioningConfig.getProvisioningUserStore())) {
            return new InboundSCIMProvisioningConfiguration()
                    .provisioningUserstoreDomain(inboundProvisioningConfig.getProvisioningUserStore());
        }

        return null;
    }

    private AdvancedApplicationConfiguration buildAdvancedAppConfiguration(ServiceProvider serviceProvider) {

        LocalAndOutboundAuthenticationConfig authConfig = serviceProvider.getLocalAndOutBoundAuthenticationConfig();
        if (authConfig == null) {
            authConfig = new LocalAndOutboundAuthenticationConfig();
        }

        return new AdvancedApplicationConfiguration()
                .saas(serviceProvider.isSaasApp())
                .enableAuthorization(authConfig.isEnableAuthorization())
                .returnAuthenticatedIdpList(authConfig.isAlwaysSendBackAuthenticatedListOfIdPs())
                .skipConsent(getSkipConsent(serviceProvider))
                .certificate(getCertificate(serviceProvider));
    }

    private Certificate getCertificate(ServiceProvider serviceProvider) {

        if (StringUtils.isNotBlank(serviceProvider.getCertificateContent())) {
            return new Certificate().type(Certificate.TypeEnum.PEM).value(serviceProvider.getCertificateContent());
        } else {
            // Check whether JWKS URI is configured
            return Arrays.stream(serviceProvider.getSpProperties())
                    .filter(spProperty -> StringUtils.equals(spProperty.getName(), JWKS_URI_SP_PROPERTY_NAME))
                    .findAny()
                    .map(spProperty -> new Certificate().type(Certificate.TypeEnum.JWKS).value(spProperty.getValue()))
                    .orElse(null);
        }
    }

    private boolean getSkipConsent(ServiceProvider serviceProvider) {

        return Arrays.stream(serviceProvider.getSpProperties())
                .filter(spProperty -> StringUtils.equals(spProperty.getName(), IdentityConstants.SKIP_CONSENT))
                .findAny()
                .map(spProperty -> Boolean.parseBoolean(spProperty.getValue()))
                .orElse(false);
    }

    private InboundProtocols buildInboundProtocols(InboundAuthenticationConfig inboundAuthenticationConfig) {

        InboundProtocols inboundProtocols = new InboundProtocols();

        InboundAuthenticationRequestConfig[] inboundAuthConfigs =
                inboundAuthenticationConfig.getInboundAuthenticationRequestConfigs();

        if (inboundAuthConfigs != null) {
            for (InboundAuthenticationRequestConfig inboundAuth : inboundAuthConfigs) {

                switch (inboundAuth.getInboundAuthType()) {
                    case StandardInboundProtocols.SAML2:
                        inboundProtocols.setSaml(buildSaml2Configuration(inboundAuth));
                        break;
                    case StandardInboundProtocols.OAUTH2:
                        inboundProtocols.setOidc(buildOpenIdConnectConfiguration(inboundAuth));
                        break;
                    case StandardInboundProtocols.PASSIVE_STS:
                        inboundProtocols.setPassiveSts(buildPassiveSTSConfiguration(inboundAuth));
                        break;
                    case StandardInboundProtocols.WS_TRUST:
                        inboundProtocols.setWsTrust(buildWsTrustConfiguration(inboundAuth));
                        break;
                    default:
                        break;

                }
            }
        }

        return inboundProtocols;
    }

    private WSTrustConfiguration buildWsTrustConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        String audience = inboundAuth.getInboundAuthKey();
        try {
            TrustedServiceData[] trustedServices =
                    ApplicationManagementServiceHolder.getStsAdminService().getTrustedServices();

            // TODO : check whether we need to throw an exception if we can't find a wstrust service
            return Arrays.stream(trustedServices)
                    .filter(trustedServiceData -> StringUtils.equals(trustedServiceData.getServiceAddress(), audience))
                    .findAny()
                    .map(trustedServiceData -> new WSTrustConfiguration()
                            .audience(trustedServiceData.getServiceAddress())
                            .certificateAlias(trustedServiceData.getCertAlias()))
                    .orElse(new WSTrustConfiguration());

        } catch (SecurityConfigException e) {
            throw handleServerError(e, "Error while retrieving wsTrust configuration for audience: " + audience);
        }
    }

    private PassiveStsConfiguration buildPassiveSTSConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        return new PassiveStsConfiguration()
                .realm(inboundAuth.getInboundAuthKey())
                .replyTo(getPassiveSTSWReply(inboundAuth.getProperties()));
    }

    private String getPassiveSTSWReply(Property[] properties) {

        // TODO : null check on property array
        return Arrays.stream(properties)
                .filter(property -> StringUtils.equals(property.getName(), PassiveSTS.PASSIVE_STS_REPLY_URL))
                .findAny()
                .map(Property::getValue).orElse(null);
    }

    private OpenIDConnectConfiguration buildOpenIdConnectConfiguration(InboundAuthenticationRequestConfig inboundAuth) {

        String clientId = inboundAuth.getInboundAuthKey();
        try {
            OAuthConsumerAppDTO oauthApp =
                    ApplicationManagementServiceHolder.getOAuthAdminService().getOAuthApplicationData(clientId);
            return new OAuthConsumerAppToApiModel().apply(oauthApp);

        } catch (IdentityOAuthAdminException e) {
            throw handleServerError(e, "Error while retrieving oauth application data for clientId: " + clientId);
        }
    }

    private SAML2Configuration buildSaml2Configuration(InboundAuthenticationRequestConfig inboundAuth) {

        String issuer = inboundAuth.getInboundAuthKey();
        try {
            SAMLSSOServiceProviderDTO serviceProvider =
                    ApplicationManagementServiceHolder.getSamlssoConfigService().getServiceProvider(issuer);

            if (serviceProvider != null) {
                return new SAMLSSOServiceProviderToAPIModel().apply(serviceProvider);
            } else {
                return null;
            }
        } catch (IdentityException e) {
            throw handleServerError(e, "Error while retrieving service provider data for issuer: " + issuer);
        }
    }

    private APIError handleServerError(Exception e, String message) {

        // TODO handle errors properly.
        ErrorResponse.Builder builder = new ErrorResponse.Builder();
        ErrorResponse errorResponse = builder.build(log, e, message);

        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
        return new APIError(status, errorResponse);
    }
}
