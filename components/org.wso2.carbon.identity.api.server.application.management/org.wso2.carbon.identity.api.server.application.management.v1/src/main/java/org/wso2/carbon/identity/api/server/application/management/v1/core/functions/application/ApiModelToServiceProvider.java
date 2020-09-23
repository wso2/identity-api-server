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
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.UpdateInboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.provisioning.UpdateProvisioningConfiguration;
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
        application.setAccessUrl(applicationModel.getAccessUrl());
        application.setTemplateId(applicationModel.getTemplateId());

        addAdvancedConfigurationToApplication(application, applicationModel.getAdvancedConfigurations());
        addClaimConfigurationToApplication(application, applicationModel.getClaimConfiguration());
        addAuthenticationSequence(application, applicationModel.getAuthenticationSequence());
        addProvisioningConfiguration(application, applicationModel.getProvisioningConfigurations());
        addInboundAuthenticationProtocolsToApplication(application, applicationModel.getInboundProtocolConfiguration());

        return application;
    }

    private void addInboundAuthenticationProtocolsToApplication(ServiceProvider application,
                                                                InboundProtocols inboundProtocolsModel) {

        if (inboundProtocolsModel != null) {
            new UpdateInboundProtocols().apply(application, inboundProtocolsModel);
        }
    }

    private void addAuthenticationSequence(ServiceProvider application, AuthenticationSequence authSequenceApiModel) {

        if (authSequenceApiModel != null) {
            new UpdateAuthenticationSequence().apply(application, authSequenceApiModel);
        }
    }

    private void addProvisioningConfiguration(ServiceProvider application,
                                              ProvisioningConfiguration provisioningConfigApiModel) {

        if (provisioningConfigApiModel != null) {
            new UpdateProvisioningConfiguration().apply(application, provisioningConfigApiModel);
        }
    }

    private void addClaimConfigurationToApplication(ServiceProvider application, ClaimConfiguration claimApiModel) {

        if (claimApiModel != null) {
            new UpdateClaimConfiguration().apply(application, claimApiModel);
        }
    }

    private void addAdvancedConfigurationToApplication(ServiceProvider application,
                                                       AdvancedApplicationConfiguration advancedApplicationConfig) {

        if (advancedApplicationConfig != null) {
            new UpdateAdvancedConfigurations().apply(application, advancedApplicationConfig);
        }
    }
}
