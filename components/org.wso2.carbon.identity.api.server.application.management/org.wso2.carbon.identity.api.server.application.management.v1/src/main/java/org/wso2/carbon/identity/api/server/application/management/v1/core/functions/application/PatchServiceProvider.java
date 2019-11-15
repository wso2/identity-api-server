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
import org.wso2.carbon.identity.api.server.application.management.v1.ApplicationPatchModel;
import org.wso2.carbon.identity.api.server.application.management.v1.AuthenticationSequence;
import org.wso2.carbon.identity.api.server.application.management.v1.ClaimConfiguration;
import org.wso2.carbon.identity.api.server.application.management.v1.ProvisioningConfiguration;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.Utils.setIfNotNull;

/**
 * Partially update the provided application based on patch request.
 */
public class PatchServiceProvider implements UpdateFunction<ServiceProvider, ApplicationPatchModel> {

    @Override
    public void update(ServiceProvider serviceProvider, ApplicationPatchModel applicationPatchModel) {

        setIfNotNull(applicationPatchModel.getName(), serviceProvider::setApplicationName);
        setIfNotNull(applicationPatchModel.getDescription(), serviceProvider::setDescription);
        setIfNotNull(applicationPatchModel.getImageUrl(), serviceProvider::setImageUrl);
        setIfNotNull(applicationPatchModel.getLoginUrl(), serviceProvider::setLoginUrl);

        updateClaimConfiguration(serviceProvider, applicationPatchModel.getClaimConfiguration());
        updateAuthenticationSequence(applicationPatchModel.getAuthenticationSequence(), serviceProvider);
        updateAdvancedConfiguration(serviceProvider, applicationPatchModel.getAdvancedConfigurations());
        updateProvisioningConfiguration(applicationPatchModel.getProvisioningConfigurations(), serviceProvider);
    }

    private void updateClaimConfiguration(ServiceProvider serviceProvider, ClaimConfiguration claimConfiguration) {

        update(serviceProvider, claimConfiguration, new UpdateClaimConfiguration());
    }

    private void updateAuthenticationSequence(AuthenticationSequence authenticationSequence,
                                              ServiceProvider serviceProvider) {

        update(serviceProvider, authenticationSequence, new UpdateAuthenticationSequence());
    }

    private void updateAdvancedConfiguration(ServiceProvider serviceProvider,
                                             AdvancedApplicationConfiguration advancedConfigurations) {

        update(serviceProvider, advancedConfigurations, new UpdateAdvancedConfigurations());
    }

    private void updateProvisioningConfiguration(ProvisioningConfiguration provisioningConfigurations,
                                                 ServiceProvider serviceProvider) {

        update(serviceProvider, provisioningConfigurations, new UpdateProvisioningConfiguration());
    }

    private <T> void update(ServiceProvider application, T t, UpdateFunction<ServiceProvider, T> function) {

        if (t != null) {
            function.update(application, t);
        }
    }
}
