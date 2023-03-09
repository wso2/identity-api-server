/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
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

import com.google.common.collect.Lists;
import org.wso2.carbon.identity.api.server.application.management.v1.IdpAppRoleConfig;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.application.common.model.AppRoleMappingConfig;
import org.wso2.carbon.identity.application.common.model.AuthenticationStep;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Update the Identity Provider Application Role Configurations.
 */
public class UpdateIdpAppRoleConfigurations implements UpdateFunction<ServiceProvider, List<IdpAppRoleConfig>> {

    @Override
    public void apply(ServiceProvider serviceProvider, List<IdpAppRoleConfig> idpAppRoleConfigurations) {

        if (idpAppRoleConfigurations != null) {
            updateIdpAppRoleConfigurations(serviceProvider, idpAppRoleConfigurations);
        }
    }

     private void updateIdpAppRoleConfigurations(ServiceProvider serviceProvider,
                                                 List<IdpAppRoleConfig> idpAppRoleConfigurations) {

         List<String> attributeStepFIdPs =  getAttributeStepFIdPs(serviceProvider);
         AppRoleMappingConfig[] appRoleMappingConfigs = getApplicationRoleMappingConfig(
                 attributeStepFIdPs, idpAppRoleConfigurations);
         serviceProvider.setApplicationRoleMappingConfig(appRoleMappingConfigs);
     }

    private List<String> getAttributeStepFIdPs (ServiceProvider serviceProvider) {

        AuthenticationStep attributeAuthStep = serviceProvider.getLocalAndOutBoundAuthenticationConfig().
                getAuthenticationStepForAttributes();
        IdentityProvider[] authStepFederatedIdentityProviders = null;
        if (attributeAuthStep == null) {
            for (AuthenticationStep authenticationStep : serviceProvider.getLocalAndOutBoundAuthenticationConfig().
                    getAuthenticationSteps()) {
                if (authenticationStep.isAttributeStep()) {
                        attributeAuthStep = authenticationStep;
                    break;
                }
            }
        }
        if (attributeAuthStep != null) {
            authStepFederatedIdentityProviders = attributeAuthStep.getFederatedIdentityProviders();
        }
        if (authStepFederatedIdentityProviders != null) {
            return Arrays.stream(authStepFederatedIdentityProviders).
                    map(IdentityProvider::getIdentityProviderName).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    private AppRoleMappingConfig[] getApplicationRoleMappingConfig (List<String> attributeStepFIdPs,
                                                                    List<IdpAppRoleConfig>  idpAppRoleConfigs) {

        return attributeStepFIdPs.stream().map(FIdPName -> {
            AppRoleMappingConfig appRoleMappingConfig = new AppRoleMappingConfig();
            appRoleMappingConfig.setIdPName(FIdPName);
            idpAppRoleConfigs.stream().filter(idpAppRoleConfig -> FIdPName.equals(idpAppRoleConfig.getIdp()))
                    .findFirst().ifPresent(idpAppRoleConfig -> appRoleMappingConfig.setUseAppRoleMappings(
                            idpAppRoleConfig.getUseAppRoleMappings()));
            return appRoleMappingConfig;
        }).toArray(AppRoleMappingConfig[]::new);
    }
}
