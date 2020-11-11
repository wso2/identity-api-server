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
import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationStepModel;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.AuthenticationStep;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.LocalAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.RequestPathAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.model.script.AuthenticationScriptConfig;
import org.wso2.carbon.identity.application.mgt.ApplicationConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Updates the authentication configuration defined by the API model in the Service Provider model.
 */
public class UpdateAuthenticationSequence implements UpdateFunction<ServiceProvider, AuthenticationSequence> {

    @Override
    public void apply(ServiceProvider application, AuthenticationSequence authSequenceApiModel) {

        if (authSequenceApiModel != null) {
            updateRequestPathAuthenticatorConfigs(authSequenceApiModel, application);
            // Authentication steps
            LocalAndOutboundAuthenticationConfig localAndOutboundConfig = getLocalAndOutboundConfig(application);
            updateAuthenticationSteps(authSequenceApiModel, localAndOutboundConfig);
            updateAdaptiveAuthenticationScript(authSequenceApiModel, localAndOutboundConfig);
        }
    }

    private void updateAuthenticationSteps(AuthenticationSequence authSequenceApiModel,
                                           LocalAndOutboundAuthenticationConfig localAndOutboundConfig) {

        if (isRevertToDefaultSequence(authSequenceApiModel, localAndOutboundConfig)) {
            localAndOutboundConfig.setAuthenticationType(ApplicationConstants.AUTH_TYPE_DEFAULT);
            localAndOutboundConfig.setAuthenticationSteps(new AuthenticationStep[0]);
        } else if (authSequenceApiModel.getType() != AuthenticationSequence.TypeEnum.DEFAULT) {
            AuthenticationStep[] authenticationSteps = getAuthenticationSteps(authSequenceApiModel);
            localAndOutboundConfig.setAuthenticationType(ApplicationConstants.AUTH_TYPE_FLOW);
            localAndOutboundConfig.setAuthenticationSteps(authenticationSteps);
        }
        // If the authSequenceApiModel.getType() = DEFAULT, we don't have to worry about setting authentication steps
        // and related configs.
    }

    private void updateAdaptiveAuthenticationScript(AuthenticationSequence authSequenceApiModel,
                                                    LocalAndOutboundAuthenticationConfig localAndOutboundConfig) {

        if (isRevertToDefaultSequence(authSequenceApiModel, localAndOutboundConfig)) {
            localAndOutboundConfig.setAuthenticationScriptConfig(null);
        } else if (StringUtils.isNotBlank(authSequenceApiModel.getScript())) {
            AuthenticationScriptConfig adaptiveScript = new AuthenticationScriptConfig();
            adaptiveScript.setContent(authSequenceApiModel.getScript());
            adaptiveScript.setEnabled(true);
            localAndOutboundConfig.setAuthenticationScriptConfig(adaptiveScript);
        }
    }

    private void updateRequestPathAuthenticatorConfigs(AuthenticationSequence authSequenceApiModel,
                                                       ServiceProvider application) {

        Optional.ofNullable(authSequenceApiModel.getRequestPathAuthenticators())
                .ifPresent(authenticators -> {
                    RequestPathAuthenticatorConfig[] requestPathAuthenticatorConfigs =
                            authenticators.stream()
                                    .map(this::buildRequestPathConfig)
                                    .toArray(RequestPathAuthenticatorConfig[]::new);

                    // Set request path authenticator config to application
                    application.setRequestPathAuthenticatorConfigs(requestPathAuthenticatorConfigs);
                });
    }

    private AuthenticationStep[] getAuthenticationSteps(AuthenticationSequence authSequenceApiModel) {

        if (CollectionUtils.isEmpty(authSequenceApiModel.getSteps())) {
            throw Utils.buildBadRequestError("Authentication steps cannot be empty for user defined " +
                    "authentication type: " + AuthenticationSequence.TypeEnum.USER_DEFINED);
        }

        // Sort the authentication steps.
        List<AuthenticationStepModel> sortedStepModelList = Optional.of(authSequenceApiModel.getSteps())
                .map(steps -> {
                    steps.sort(Comparator.comparingInt(AuthenticationStepModel::getId));
                    return steps;
                }).orElse(Collections.emptyList());

        int numSteps = sortedStepModelList.size();
        if (numSteps != sortedStepModelList.get(numSteps - 1).getId()) {
            // This means the steps are not consecutive. ie. For steps to be consecutive the largest id needs
            // to be equal to number of steps.
            throw Utils.buildBadRequestError("Step ids need to be consecutive in the authentication sequence steps.");
        }

        int subjectStepId = getSubjectStepId(authSequenceApiModel.getSubjectStepId(), numSteps);
        int attributeStepId = getSubjectStepId(authSequenceApiModel.getAttributeStepId(), numSteps);

        // We create a array of size (numSteps + 1) since step order starts from 1.
        AuthenticationStep[] authenticationSteps = new AuthenticationStep[numSteps];

        int stepOrder = 1;
        for (AuthenticationStepModel stepModel : sortedStepModelList) {
            AuthenticationStep authenticationStep = buildAuthenticationStep(stepModel);

            authenticationStep.setStepOrder(stepOrder);
            if (subjectStepId == stepOrder) {
                authenticationStep.setSubjectStep(true);
            }
            if (attributeStepId == stepOrder) {
                authenticationStep.setAttributeStep(true);
            }
            authenticationSteps[stepOrder - 1] = authenticationStep;

            stepOrder++;
        }
        return authenticationSteps;
    }

    private AuthenticationStep buildAuthenticationStep(AuthenticationStepModel stepModel) {

        AuthenticationStep authenticationStep = new AuthenticationStep();
        // iteration the options, divide in to federated and local and add the configs
        if (CollectionUtils.isEmpty(stepModel.getOptions())) {
            throw Utils.buildBadRequestError("Authentication Step options cannot be empty.");
        }

        List<LocalAuthenticatorConfig> localAuthOptions = new ArrayList<>();
        List<IdentityProvider> federatedAuthOptions = new ArrayList<>();

        stepModel.getOptions().forEach(option -> {
            // TODO : add validations to swagger so that we don't need to check inputs here.
            if (FrameworkConstants.LOCAL_IDP_NAME.equals(option.getIdp())) {
                LocalAuthenticatorConfig localAuthOption = new LocalAuthenticatorConfig();
                localAuthOption.setEnabled(true);
                localAuthOption.setName(option.getAuthenticator());
                localAuthOptions.add(localAuthOption);
            } else {
                FederatedAuthenticatorConfig federatedAuthConfig = new FederatedAuthenticatorConfig();
                federatedAuthConfig.setEnabled(true);
                federatedAuthConfig.setName(option.getAuthenticator());

                IdentityProvider federatedIdp = new IdentityProvider();
                federatedIdp.setIdentityProviderName(option.getIdp());
                federatedIdp.setFederatedAuthenticatorConfigs(new FederatedAuthenticatorConfig[]{federatedAuthConfig});
                federatedIdp.setDefaultAuthenticatorConfig(federatedAuthConfig);
                federatedAuthOptions.add(federatedIdp);
            }
        });

        authenticationStep.setLocalAuthenticatorConfigs(localAuthOptions.toArray(new LocalAuthenticatorConfig[0]));
        authenticationStep.setFederatedIdentityProviders(federatedAuthOptions.toArray(new IdentityProvider[0]));

        return authenticationStep;
    }

    private int getSubjectStepId(Integer subjectStepId, int numSteps) {

        return subjectStepId != null && subjectStepId <= numSteps ? subjectStepId : 1;
    }

    private RequestPathAuthenticatorConfig buildRequestPathConfig(String authenticator) {

        RequestPathAuthenticatorConfig requestPath = new RequestPathAuthenticatorConfig();
        requestPath.setName(authenticator);
        requestPath.setEnabled(true);
        return requestPath;
    }

    private LocalAndOutboundAuthenticationConfig getLocalAndOutboundConfig(ServiceProvider application) {

        if (application.getLocalAndOutBoundAuthenticationConfig() == null) {
            application.setLocalAndOutBoundAuthenticationConfig(new LocalAndOutboundAuthenticationConfig());
        }

        return application.getLocalAndOutBoundAuthenticationConfig();
    }

    private boolean isRevertToDefaultSequence(AuthenticationSequence authSequenceApiModel,
            LocalAndOutboundAuthenticationConfig localAndOutboundConfig) {

        String currentAuthenticationType = localAndOutboundConfig.getAuthenticationType();
        return authSequenceApiModel.getType() == AuthenticationSequence.TypeEnum.DEFAULT
                && StringUtils.isNotBlank(currentAuthenticationType)
                && !AuthenticationSequence.TypeEnum.DEFAULT.toString().equalsIgnoreCase(currentAuthenticationType);
    }
}
