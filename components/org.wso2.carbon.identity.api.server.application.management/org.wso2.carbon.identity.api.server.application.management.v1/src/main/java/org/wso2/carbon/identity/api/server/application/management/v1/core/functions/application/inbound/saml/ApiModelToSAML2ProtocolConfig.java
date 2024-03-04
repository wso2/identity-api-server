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

package org.wso2.carbon.identity.api.server.application.management.v1.core.functions.application.inbound.saml;

import org.wso2.carbon.identity.api.server.application.management.v1.SAML2Configuration;
import org.wso2.carbon.identity.sso.saml.dto.SAML2ProtocolConfigDTO;

import java.util.function.Function;

/**
 * Converts the SAML2Configuration model to the SAML2ProtocolConfigDTO model.
 */
public class ApiModelToSAML2ProtocolConfig implements Function<SAML2Configuration, SAML2ProtocolConfigDTO> {
    
    @Override
    public SAML2ProtocolConfigDTO apply(SAML2Configuration saml2Configuration) {
        
        SAML2ProtocolConfigDTO saml2ProtocolConfigDTO = new SAML2ProtocolConfigDTO();
        saml2ProtocolConfigDTO.setMetadataFile(saml2Configuration.getMetadataFile());
        saml2ProtocolConfigDTO.setMetadataURL(saml2Configuration.getMetadataURL());
        if(saml2Configuration.getManualConfiguration() != null) {
            saml2ProtocolConfigDTO.setManualConfiguration(new ApiModelToSAMLSSOServiceProvider().apply(
                    saml2Configuration.getManualConfiguration()));
        }
        return saml2ProtocolConfigDTO;
    }
}
