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
package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.UpdateFunction;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.InboundAuthenticationRequestConfig;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.InboundFunctions.rollbackInbounds;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.custom.CustomInboundFunctions.createCustomInbound;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.oauth2.OAuthInboundFunctions.createOAuthInbound;
import static org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.saml.SAMLInboundFunctions.createSAMLInbound;

/**
 * Updates the inbound authentication protocols defined by the API model in the Service Provider model.
 */
public class UpdateInboundProtocols implements UpdateFunction<ServiceProvider, InboundProtocols> {

    private static final Log log = LogFactory.getLog(UpdateInboundProtocols.class);

    @Override
    public void apply(ServiceProvider application, InboundProtocols inboundProtocols) {

        List<InboundAuthenticationRequestConfig> inbounds = new ArrayList<>();

        try {
            if (inboundProtocols.getOidc() != null) {
                inbounds.add(createOAuthInbound(application.getApplicationName(), inboundProtocols.getOidc()));
            }

            if (inboundProtocols.getSaml() != null) {
                inbounds.add(createSAMLInbound(application, inboundProtocols.getSaml()));
            }

        } catch (APIError error) {
            if (log.isDebugEnabled()) {
                log.debug("Error while adding inbound protocols for application id: "
                        + application.getApplicationResourceId() + ". Cleaning up possible partially created inbound " +
                        "configurations.");
            }
            rollbackInbounds(inbounds);
            throw error;
        }

        if (inboundProtocols.getCustom() != null) {
            inboundProtocols.getCustom().forEach(inboundConfigModel -> {
                // TODO Add validate at swagger to make sure inbound key and name are not null.
                InboundAuthenticationRequestConfig inboundRequestConfig = createCustomInbound(inboundConfigModel);
                inbounds.add(inboundRequestConfig);
            });
        }

        InboundAuthenticationConfig inboundAuthConfig = new InboundAuthenticationConfig();
        inboundAuthConfig.setInboundAuthenticationRequestConfigs(
                inbounds.toArray(new InboundAuthenticationRequestConfig[0])
        );

        application.setInboundAuthenticationConfig(inboundAuthConfig);
    }
}
