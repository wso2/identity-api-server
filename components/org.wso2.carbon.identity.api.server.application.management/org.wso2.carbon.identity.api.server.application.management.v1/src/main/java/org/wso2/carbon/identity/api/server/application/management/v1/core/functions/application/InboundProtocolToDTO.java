/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com).
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

import org.wso2.carbon.identity.api.server.application.management.v1.InboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.UpdateInboundProtocols;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.oauth2.ApiModelToOAuthConsumerApp;
import org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.saml.ApiModelToSAML2ProtocolConfig;
import org.wso2.carbon.identity.application.common.IdentityApplicationManagementClientException;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.inbound.dto.InboundProtocolsDTO;

/**
 * Converts the inbound protocols defined in the API model to the Service Provider model.
 */
public class InboundProtocolToDTO {
    
    public InboundProtocolsDTO apply(ServiceProvider serviceProvider, InboundProtocols inboundProtocols)
            throws IdentityApplicationManagementClientException {
        
        InboundProtocolsDTO inboundProtocolsDTO = new InboundProtocolsDTO();
        if (inboundProtocols.getOidc() != null) {
            inboundProtocolsDTO.addProtocolConfiguration(new ApiModelToOAuthConsumerApp().apply(
                    serviceProvider.getApplicationName(), inboundProtocols.getOidc()));
        }
        if (inboundProtocols.getSaml() != null) {
            inboundProtocolsDTO.addProtocolConfiguration(new ApiModelToSAML2ProtocolConfig().apply(
                    inboundProtocols.getSaml()));
        }
        /* If the inbound protocol is passiveSTS, WS-Trust or custom, the inbound protocol configuration is added to
         the service provider directly. The inbound protocol configuration is not added to the InboundProtocolsDTO
         because we don't have separate services to handle those inbound auth configurations. */
        new UpdateInboundProtocols().apply(serviceProvider, inboundProtocols);
        return inboundProtocolsDTO;
    }
}
