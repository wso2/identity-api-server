/*
 * Copyright (c) 2019-2023, WSO2 LLC. (http://www.wso2.com).
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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementConstants;
import org.wso2.carbon.identity.api.server.application.management.common.ApplicationManagementServiceHolder;
import org.wso2.carbon.identity.api.server.application.management.v1.AdditionalSpProperty;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.AdvancedApplicationConfigurationAttestationMetaData;
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationResponseModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AssociatedRolesConfig;
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
import org.wso2.carbon.identity.api.server.application.management.v1.Role;
import org.wso2.carbon.identity.api.server.application.management.v1.RoleConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.SubjectConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundAuthConfigToApiModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.provisioning.BuildProvisioningConfiguration;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
import org.wso2.carbon.identity.application.common.model.AuthenticationStep;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;
import org.wso2.carbon.identity.application.common.model.ClientAttestationMetaData;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.RequestPathAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.RoleMapping;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.ServiceProviderProperty;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.application.mgt.ApplicationConstants;
import org.wso2.carbon.identity.application.mgt.ApplicationMgtUtil;
import org.wso2.carbon.identity.role.v2.mgt.core.RoleConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.arrayToStream;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.UpdateAdvancedConfigurations.TYPE_JWKS;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.UpdateAdvancedConfigurations.TYPE_PEM;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.Application.CONSOLE_APP;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.Application.MY_ACCOUNT_APP;
import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.ORGANIZATION_AUTHENTICATOR;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.ALLOWED_ROLE_AUDIENCE_PROPERTY_NAME;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.ANDROID_PACKAGE_NAME_PROPERTY_NAME;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.APPLE_APP_ID_PROPERTY_NAME;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.IS_API_BASED_AUTHENTICATION_ENABLED_PROPERTY_NAME;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.IS_ATTESTATION_ENABLED_PROPERTY_NAME;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.IS_MANAGEMENT_APP_SP_PROPERTY_NAME;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.TEMPLATE_ID_SP_PROPERTY_NAME;
import static org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants.USE_USER_ID_FOR_DEFAULT_SUBJECT;
import static org.wso2.carbon.identity.application.mgt.dao.impl.ApplicationDAOImpl.USE_DOMAIN_IN_ROLES;
import static org.wso2.carbon.identity.base.IdentityConstants.SKIP_CONSENT;
import static org.wso2.carbon.identity.base.IdentityConstants.SKIP_LOGOUT_CONSENT;
import static org.wso2.carbon.identity.base.IdentityConstants.USE_EXTERNAL_CONSENT_PAGE;

/**
 * Converts the backend model ServiceProvider into the corresponding API model object.
 */
public class ServiceProviderToApiModel implements Function<ServiceProvider, ApplicationResponseModel> {

    private static final Log log = LogFactory.getLog(ServiceProviderToApiModel.class);

    private static final Set<String> systemApplications = ApplicationManagementServiceHolder
            .getApplicationManagementService().getSystemApplications();
    private static final String IS_FRAGMENT_APP = "isFragmentApp";
    private static final String useUserIdForDefaultSubject = "useUserIdForDefaultSubject";

    @Override
    public ApplicationResponseModel apply(ServiceProvider application) {

        if (isResidentSp(application)) {
            return new ApplicationResponseModel()
                    .id(application.getApplicationResourceId())
                    .name(application.getApplicationName())
                    .description(application.getDescription())
                    .provisioningConfigurations(buildProvisioningConfiguration(application))
                    .access(ApplicationResponseModel.AccessEnum.READ);
        } else {
            return new ApplicationResponseModel()
                    .id(application.getApplicationResourceId())
                    .name(application.getApplicationName())
                    .description(application.getDescription())
                    .imageUrl(application.getImageUrl())
                    .accessUrl(application.getAccessUrl())
                    .logoutReturnUrl(getLogoutReturnUrl(application))
                    .clientId(getInboundKey(application, "oauth2"))
                    .issuer(getInboundKey(application, "samlsso"))
                    .realm(getInboundKey(application, "passivests"))
                    .templateId(application.getTemplateId())
                    .isManagementApp(application.isManagementApp())
                    .associatedRoles(buildAssociatedRoles(application))
                    .claimConfiguration(buildClaimConfiguration(application))
                    .inboundProtocols(buildInboundProtocols(application))
                    .advancedConfigurations(buildAdvancedAppConfiguration(application))
                    .provisioningConfigurations(buildProvisioningConfiguration(application))
                    .authenticationSequence(buildAuthenticationSequence(application))
                    .access(getAccess(application.getApplicationName()));
        }
    }

    private String getLogoutReturnUrl(ServiceProvider application) {

        for (ServiceProviderProperty property : application.getSpProperties()) {
            if (ApplicationManagementConstants.PROP_LOGOUT_RETURN_URL.equals(property.getName())) {
                return property.getValue();
            }
        }
        return null; // null value returned to avoid API response returning an empty string.
    }

    private AssociatedRolesConfig buildAssociatedRoles(ServiceProvider application) {

        AssociatedRolesConfig associatedRolesConfig = new AssociatedRolesConfig();
        org.wso2.carbon.identity.application.common.model.AssociatedRolesConfig associatedRolesConfiguration =
                application.getAssociatedRolesConfig();
        if (associatedRolesConfiguration == null) {
            return null;
        }

        String allowedAudience = associatedRolesConfiguration.getAllowedAudience();
        AssociatedRolesConfig.AllowedAudienceEnum allowedAudienceEnum = null;

        switch (allowedAudience) {
            case RoleConstants.APPLICATION:
                allowedAudienceEnum = AssociatedRolesConfig.AllowedAudienceEnum.APPLICATION;
                break;
            case RoleConstants.ORGANIZATION:
                allowedAudienceEnum = AssociatedRolesConfig.AllowedAudienceEnum.ORGANIZATION;
                break;
            default:
                break;
        }
        associatedRolesConfig.setAllowedAudience(allowedAudienceEnum);
        if (RoleConstants.APPLICATION.equals(allowedAudience)) {
            Arrays.stream(associatedRolesConfiguration.getRoles())
                    .map(role -> new Role().id(role.getId()).name(role.getName()))
                    .forEach(associatedRolesConfig::addRolesItem);
        }
        return associatedRolesConfig;
    }

    private List<InboundProtocolListItem> buildInboundProtocols(ServiceProvider application) {

        return new InboundAuthConfigToApiModel().apply(application);
    }

    private boolean isResidentSp(ServiceProvider application) {

        return ApplicationConstants.LOCAL_SP.equalsIgnoreCase(application.getApplicationName());
    }

    private AuthenticationSequence buildAuthenticationSequence(ServiceProvider application) {

        LocalAndOutboundAuthenticationConfig authConfig = application.getLocalAndOutBoundAuthenticationConfig();

        AuthenticationSequence.TypeEnum authenticationType = getAuthenticationType(authConfig);
        if (authenticationType == AuthenticationSequence.TypeEnum.DEFAULT) {
            // If this is the default sequence we need to set the default tenant authentication sequence.
            if (log.isDebugEnabled()) {
                log.debug("Authentication type is set to 'DEFAULT'. Reading the authentication sequence from the " +
                        "'default' application and showing the effective authentication sequence for application " +
                        "with id: " + application.getApplicationResourceId());
            }
            authConfig = getDefaultAuthenticationConfig();
        }

        AuthenticationSequence authSequence = new AuthenticationSequence();
        authSequence.setType(authenticationType);
        if (authConfig.getAuthenticationScriptConfig() != null) {
            authSequence.script(authConfig.getAuthenticationScriptConfig().getContent());
        }

        boolean hideOrganizationSsoAuthenticator = StringUtils.equals(application.getApplicationName(), CONSOLE_APP) ||
                StringUtils.equals(application.getApplicationName(), MY_ACCOUNT_APP);
        addAuthenticationStepInformation(hideOrganizationSsoAuthenticator, authConfig, authSequence);

        List<String> requestPathAuthenticators = getRequestPathAuthenticators(application);
        authSequence.setRequestPathAuthenticators(requestPathAuthenticators);

        return authSequence;
    }

    private List<String> getRequestPathAuthenticators(ServiceProvider application) {

        RequestPathAuthenticatorConfig[] requestPathConfigs = application.getRequestPathAuthenticatorConfigs();
        return arrayToStream(requestPathConfigs).map(LocalAuthenticatorConfig::getName).collect(Collectors.toList());
    }

    private void addAuthenticationStepInformation(boolean hideOrganizationSsoAuthenticator,
                                                  LocalAndOutboundAuthenticationConfig authConfig,
                                                  AuthenticationSequence authSequence) {

        if (authConfig.getAuthenticationSteps() != null) {
            Arrays.stream(authConfig.getAuthenticationSteps()).forEach(authenticationStep -> {
                authSequence.addStepsItem(buildAuthStep(hideOrganizationSsoAuthenticator, authenticationStep));
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
    }

    private LocalAndOutboundAuthenticationConfig getDefaultAuthenticationConfig() {

        ServiceProvider defaultSP = getDefaultServiceProvider();
        return defaultSP.getLocalAndOutBoundAuthenticationConfig();
    }

    private AuthenticationSequence.TypeEnum getAuthenticationType(LocalAndOutboundAuthenticationConfig authConfig) {

        AuthenticationSequence.TypeEnum authenticationType;
        if (authConfig == null || ApplicationConstants.AUTH_TYPE_DEFAULT.equals(authConfig.getAuthenticationType())) {
            authenticationType = AuthenticationSequence.TypeEnum.DEFAULT;
        } else {
            // This means a user defined authentication sequence.
            authenticationType = AuthenticationSequence.TypeEnum.USER_DEFINED;
        }
        return authenticationType;
    }

    private ServiceProvider getDefaultServiceProvider() {

        ServiceProvider defaultSP;
        try {
            defaultSP = ApplicationManagementServiceHolder.getApplicationManagementService()
                    .getServiceProvider(IdentityApplicationConstants.DEFAULT_SP_CONFIG,
                            MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);
        } catch (IdentityApplicationManagementException e) {
            throw Utils.buildServerError("Error while loading default SP configurations.", e);
        }
        return defaultSP;
    }

    private AuthenticationStepModel buildAuthStep(boolean hideOrganizationSsoAuthenticator,
                                                  AuthenticationStep authenticationStep) {

        AuthenticationStepModel authStep = new AuthenticationStepModel();
        authStep.setId(authenticationStep.getStepOrder());

        arrayToStream(authenticationStep.getFederatedIdentityProviders()).filter(y ->
                        !hideOrganizationSsoAuthenticator ||
                                !(ORGANIZATION_AUTHENTICATOR.equals(y.getDefaultAuthenticatorConfig().getName())))
                .forEach(y -> authStep.addOptionsItem(new Authenticator().idp(y.getIdentityProviderName())
                        .authenticator(y.getDefaultAuthenticatorConfig().getName())));

        arrayToStream(authenticationStep.getLocalAuthenticatorConfigs()).forEach(y -> authStep.addOptionsItem(
                new Authenticator().idp(FrameworkConstants.LOCAL_IDP_NAME).authenticator(y.getName())));

        return authStep;
    }

    private ClaimConfiguration buildClaimConfiguration(ServiceProvider application) {

        return new ClaimConfiguration()
                .dialect(getDialect(application))
                .role(buildRoleConfig(application))
                .subject(buildSubjectClaimConfig(application))
                .requestedClaims(buildRequestedClaims(application))
                .claimMappings(buildClaimMappings(application));
    }

    private List<ClaimMappings> buildClaimMappings(ServiceProvider application) {

        if (application.getClaimConfig() != null) {
            return arrayToStream(application.getClaimConfig().getClaimMappings())
                    .map(claimMapping -> new ClaimMappings()
                            .applicationClaim(claimMapping.getRemoteClaim().getClaimUri())
                            .localClaim(buildClaimModel(claimMapping.getLocalClaim().getClaimUri())))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private List<RequestedClaimConfiguration> buildRequestedClaims(ServiceProvider application) {

        if (application.getClaimConfig() != null) {
            return arrayToStream(application.getClaimConfig().getClaimMappings())
                    .filter(ClaimMapping::isRequested)
                    .map(claimMapping -> new RequestedClaimConfiguration()
                            .claim(buildClaimModel(claimMapping.getRemoteClaim().getClaimUri()))
                            .mandatory(claimMapping.isMandatory()))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private SubjectConfig buildSubjectClaimConfig(ServiceProvider application) {

        SubjectConfig subjectConfig = new SubjectConfig();

        if (application.getClaimConfig() != null) {
            subjectConfig.useMappedLocalSubject(application.getClaimConfig().isAlwaysSendMappedLocalSubjectId());
            subjectConfig.mappedLocalSubjectMandatory(application.getClaimConfig().isMappedLocalSubjectMandatory());
        }

        LocalAndOutboundAuthenticationConfig localAndOutboundAuthConfig =
                application.getLocalAndOutBoundAuthenticationConfig();
        if (localAndOutboundAuthConfig != null) {
            subjectConfig.includeTenantDomain(localAndOutboundAuthConfig.isUseTenantDomainInLocalSubjectIdentifier());
            subjectConfig.includeUserDomain(localAndOutboundAuthConfig.isUseUserstoreDomainInLocalSubjectIdentifier());

            if (StringUtils.isBlank(localAndOutboundAuthConfig.getSubjectClaimUri())) {
                assignClaimForSubjectValue(application, subjectConfig);
            } else {
                subjectConfig.claim(buildClaimModel(localAndOutboundAuthConfig.getSubjectClaimUri()));
            }
        }

        return subjectConfig;
    }

    private void assignClaimForSubjectValue(ServiceProvider application, SubjectConfig subjectConfig) {

        if (isLocalClaimDialectUsedBySp(application)) {
            if (isUserIdUsedAsDefaultSubject(application.getSpProperties())) {
                subjectConfig.claim(buildClaimModel(FrameworkConstants.USER_ID_CLAIM));
            } else {
                subjectConfig.claim(buildClaimModel(FrameworkConstants.USERNAME_CLAIM));
            }
        }
    }

    private boolean isUserIdUsedAsDefaultSubject(ServiceProviderProperty[] spProperties) {

        for (ServiceProviderProperty spProperty : spProperties) {
            if (useUserIdForDefaultSubject.equals(spProperty.getName())) {
                return true;
            }
        }
        return false;
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

            if (StringUtils.isBlank(roleClaimId)) {
                if (application.getClaimConfig().isLocalClaimDialect()) {
                    roleConfig.claim(buildClaimModel(FrameworkConstants.LOCAL_ROLE_CLAIM_URI));
                }
            } else {
                roleConfig.claim(buildClaimModel(roleClaimId));
            }
        }

        if (application.getLocalAndOutBoundAuthenticationConfig() != null) {
            roleConfig.includeUserDomain(
                    application.getLocalAndOutBoundAuthenticationConfig().isUseUserstoreDomainInRoles());
        }

        if (application.getPermissionAndRoleConfig() != null) {
            RoleMapping[] roleMappings = application.getPermissionAndRoleConfig().getRoleMappings();
            arrayToStream(roleMappings)
                    .forEach(roleMapping -> roleConfig.addMappingsItem(
                            new org.wso2.carbon.identity.api.server.application.management.v1.RoleMapping()
                                    .applicationRole(roleMapping.getRemoteRole())
                                    .localRole(roleMapping.getLocalRole().getLocalRoleName())));

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
                .skipLoginConsent(authConfig.isSkipConsent())
                .skipLogoutConsent(authConfig.isSkipLogoutConsent())
                .useExternalConsentPage(authConfig.isUseExternalConsentPage())
                .certificate(getCertificate(serviceProvider))
                .fragment(isFragmentApp(serviceProvider))
                .enableAPIBasedAuthentication(serviceProvider.isAPIBasedAuthenticationEnabled())
                .attestationMetaData(getAttestationMetaData(serviceProvider))
                .additionalSpProperties(getSpProperties(serviceProvider));
    }

    /**
     * Retrieves the attestation metadata for an application's advanced configuration based on the provided
     * service provider.
     *
     * @param serviceProvider The service provider for which attestation metadata is required.
     * @return An instance of AdvancedApplicationConfigurationAttestationMetaData containing attestation data.
     */
    private AdvancedApplicationConfigurationAttestationMetaData getAttestationMetaData
    (ServiceProvider serviceProvider) {

        // Retrieve the client attestation metadata from the service provider.
        ClientAttestationMetaData clientAttestationMetaData = serviceProvider.getClientAttestationMetaData();

        // If the client attestation metadata is not available, create a new instance.
        if (clientAttestationMetaData == null) {
            clientAttestationMetaData = new ClientAttestationMetaData();
        }

        // Create and configure an instance of AdvancedApplicationConfigurationAttestationMetaData
        // based on the client attestation metadata.
        return new AdvancedApplicationConfigurationAttestationMetaData()
                .enableClientAttestation(clientAttestationMetaData.isAttestationEnabled())
                .androidPackageName(clientAttestationMetaData.getAndroidPackageName())
                .appleAppId(clientAttestationMetaData.getAppleAppId())
                .androidAttestationServiceCredentials(parseAndroidServiceCredentials(clientAttestationMetaData
                        .getAndroidAttestationServiceCredentials()));
    }


    private List<AdditionalSpProperty> getSpProperties(ServiceProvider serviceProvider) {

        ServiceProviderProperty[] serviceProviderProperties = serviceProvider.getSpProperties();
        ServiceProviderProperty[] updatedSpProperties = removeAndSetSpProperties(serviceProviderProperties);
        List<AdditionalSpProperty> additionalSpProperties = new ArrayList<>();
        for (ServiceProviderProperty serviceProviderProperty : updatedSpProperties) {
            AdditionalSpProperty spProperties = new AdditionalSpProperty();
            if (StringUtils.isNotBlank(serviceProviderProperty.getName())) {
                spProperties.setName(serviceProviderProperty.getName());
                spProperties.setValue(serviceProviderProperty.getValue());
            }
            if (StringUtils.isNotBlank(serviceProviderProperty.getDisplayName())) {
                spProperties.setDisplayName(serviceProviderProperty.getDisplayName());
            }
            additionalSpProperties.add(spProperties);
        }
        return additionalSpProperties;
    }

    private ServiceProviderProperty[] removeAndSetSpProperties(ServiceProviderProperty[] propertyList) {

        /* These properties are part of advanced configurations and hence removing
        them as they can't be packed as a part of additional sp properties again.*/
        List<ServiceProviderProperty> spPropertyList =
                Arrays.stream(propertyList).collect(Collectors.toList());
        spPropertyList.removeIf(property -> SKIP_CONSENT.equals(property.getName()));
        spPropertyList.removeIf(property -> SKIP_LOGOUT_CONSENT.equals(property.getName()));
        spPropertyList.removeIf(property -> USE_EXTERNAL_CONSENT_PAGE.equals(property.getName()));
        spPropertyList.removeIf(property -> USE_DOMAIN_IN_ROLES.equals(property.getName()));
        spPropertyList.removeIf(property -> USE_USER_ID_FOR_DEFAULT_SUBJECT.equals(property.getName()));
        spPropertyList.removeIf(property -> TEMPLATE_ID_SP_PROPERTY_NAME.equals(property.getName()));
        spPropertyList.removeIf(property -> IS_MANAGEMENT_APP_SP_PROPERTY_NAME.equals(property.getName()));
        spPropertyList.removeIf(property -> IS_ATTESTATION_ENABLED_PROPERTY_NAME.equals(property.getName()));
        spPropertyList.removeIf(property ->
                IS_API_BASED_AUTHENTICATION_ENABLED_PROPERTY_NAME.equals(property.getName()));
        spPropertyList.removeIf(property ->
                ANDROID_PACKAGE_NAME_PROPERTY_NAME.equals(property.getName()));
        spPropertyList.removeIf(property ->
                ANDROID_PACKAGE_NAME_PROPERTY_NAME.equals(property.getName()));
        spPropertyList.removeIf(property ->
                APPLE_APP_ID_PROPERTY_NAME.equals(property.getName()));
        spPropertyList.removeIf(property -> ALLOWED_ROLE_AUDIENCE_PROPERTY_NAME.equals(property.getName()));
        return spPropertyList.toArray(new ServiceProviderProperty[0]);
    }

    private Certificate getCertificate(ServiceProvider serviceProvider) {

        if (StringUtils.isNotBlank(serviceProvider.getCertificateContent())) {
            return new Certificate().type(TYPE_PEM).value(serviceProvider.getCertificateContent());
        } else if (StringUtils.isNotBlank(serviceProvider.getJwksUri())) {
            return new Certificate().type(TYPE_JWKS).value(serviceProvider.getJwksUri());
        }

        return null;
    }

    private boolean isFragmentApp(ServiceProvider serviceProvider) {

        return serviceProvider != null && serviceProvider.getSpProperties() != null &&
                Arrays.stream(serviceProvider.getSpProperties())
                        .filter(p -> IS_FRAGMENT_APP.equals(p.getName())).findFirst().map(
                                p -> Boolean.valueOf(p.getValue())).orElse(Boolean.FALSE);
    }

    private Claim buildClaimModel(String claimUri) {

        // TODO: will claim id and display name.
        return new Claim().uri(claimUri);
    }

    private ApplicationResponseModel.AccessEnum getAccess(String applicationName) {

        String username = ContextLoader.getUsernameFromContext();

        try {
            if (ApplicationConstants.LOCAL_SP.equals(applicationName) || (systemApplications != null
                    && systemApplications.stream().anyMatch(applicationName::equalsIgnoreCase)) ||
                    !ApplicationMgtUtil.isUserAuthorized(applicationName, username)) {
                return ApplicationResponseModel.AccessEnum.READ;
            }
        } catch (IdentityApplicationManagementException e) {
            log.error("Failed to check user authorization for the application: " + applicationName, e);
            return ApplicationResponseModel.AccessEnum.READ;
        }

        return ApplicationResponseModel.AccessEnum.WRITE;
    }

    private String getInboundKey(ServiceProvider application, String authType) {

        if (application.getInboundAuthenticationConfig() != null) {
            InboundAuthenticationRequestConfig[] authRequestConfigs = application.getInboundAuthenticationConfig()
                    .getInboundAuthenticationRequestConfigs();

            if (authRequestConfigs != null && authRequestConfigs.length > 0) {
                for (InboundAuthenticationRequestConfig authRequestConfig : authRequestConfigs) {
                    if (authRequestConfig.getInboundAuthType().equals(authType)) {
                        return authRequestConfig.getInboundAuthKey();
                    }
                }
            }
        }

        return StringUtils.EMPTY;
    }

    /**
     * Parses a JSON string representing Android service credentials and converts it to a Map.
     *
     * @param stringJSON A JSON string containing Android service credentials.
     * @return A Map representing the parsed Android service credentials,
     * or null if parsing fails or the input is blank.
     */
    private Map<String, String> parseAndroidServiceCredentials(String stringJSON) {

        Map<String, String> jsonObject;

        // Check if the input JSON string is blank
        if (StringUtils.isBlank(stringJSON)) {
            // Return null if the input is blank
            return null;
        }

        try {
            // Attempt to parse the JSON string into an instance of Map.
            jsonObject = new Gson().fromJson(stringJSON, Map.class);
        } catch (JsonSyntaxException exception) {
            // Return null if an exception occurs during parsing (e.g., due to invalid JSON syntax)
            return null;
        }
        // Return the parsed object
        return jsonObject;
    }
}
