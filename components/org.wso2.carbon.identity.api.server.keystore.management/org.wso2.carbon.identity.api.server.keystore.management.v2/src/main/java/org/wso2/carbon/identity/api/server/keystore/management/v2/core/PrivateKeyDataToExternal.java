package org.wso2.carbon.identity.api.server.keystore.management.v2.core;

import org.wso2.carbon.identity.api.server.keystore.management.v2.model.PrivateKeyDataObject;
import org.wso2.carbon.security.keystore.service.CertData;

import java.util.function.Function;

/**
 * jjj
 */
public class PrivateKeyDataToExternal implements Function<CertData, PrivateKeyDataObject> {


    @Override
    public PrivateKeyDataObject apply(CertData keyData) {

        PrivateKeyDataObject response = new PrivateKeyDataObject();

        response.setAlias(keyData.getAlias());
        response.setIssurDN(keyData.getIssuerDN());
        response.setSubjectDN(keyData.getSubjectDN());
        response.setSerialNumber(Integer.valueOf(String.valueOf(keyData.getSerialNumber())));
        response.setNotAfter(keyData.getNotAfter());
        response.setNotBefore(keyData.getNotBefore());
        return response;
    }
}
