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
        saml2ProtocolConfigDTO.setManualConfiguration(new ApiModelToSAMLSSOServiceProvider().apply(
                saml2Configuration.getManualConfiguration()));
        return saml2ProtocolConfigDTO;
    }
}
