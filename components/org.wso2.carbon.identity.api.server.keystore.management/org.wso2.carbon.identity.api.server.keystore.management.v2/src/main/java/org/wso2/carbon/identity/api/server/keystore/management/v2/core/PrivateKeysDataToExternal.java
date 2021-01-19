package org.wso2.carbon.identity.api.server.keystore.management.v2.core;

import org.wso2.carbon.identity.api.server.keystore.management.v2.model.PrivateKeysResponse;
import org.wso2.carbon.security.keystore.service.CertData;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.wso2.carbon.identity.api.server.common.Constants.V2_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.common.ContextLoader.buildURIForHeader;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.KEYSTORE_API_PATH_COMPONENT;
import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreConstants.KEYS_PATH_COMPONENT;

/**
 * ggg
 */
public class PrivateKeysDataToExternal implements Function<List<CertData>, List<PrivateKeysResponse>> {

    @Override
    public List<PrivateKeysResponse> apply(List<CertData> keysData) {

        List<PrivateKeysResponse> keysresponse = new ArrayList<>();
        for (CertData data : keysData) {
            PrivateKeysResponse response = new PrivateKeysResponse();
            response.setAlias(data.getAlias());
            response.setIssurDN(data.getIssuerDN());
            response.setSubjectDN(data.getSubjectDN());
            response.setNotAfter(data.getNotAfter());
            response.setPrivatekey(getKeyReferenceUrl(data.getAlias()));
            keysresponse.add(response);
        }
        return keysresponse;
    }

    private URI getKeyReferenceUrl(String alias) {

        String certificateEndPoint =
                String.format(V2_API_PATH_COMPONENT + KEYSTORE_API_PATH_COMPONENT + KEYS_PATH_COMPONENT, alias);
        return buildURIForHeader(certificateEndPoint);
    }

}
