package org.wso2.carbon.identity.api.server.keystore.management.v2.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.PrivateKeyDataObject;
import org.wso2.carbon.security.keystore.KeyStoreManagementException;
import org.wso2.carbon.security.keystore.KeyStoreManagementServerException;
import org.wso2.carbon.security.keystore.service.CertData;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreManagamentDataHolder.getKeyStoreManager;

/**
 * kkkk
 */
public class KeyStoreMgtService {

    private static final Log LOG = LogFactory.getLog(KeyStoreMgtService.class);

    public PrivateKeyDataObject getPrivateKey(String alias) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            CertData privateKeyData = getKeyStoreManager().getPrivateKeyData(alias, tenantDomain);
            return new PrivateKeyDataToExternal().apply(privateKeyData);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to get the certificate with alias: " + alias + " to the keystore.");
        }
    }

    public Response uploadPrivateKey(String alias, String key) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            getKeyStoreManager().addPrivateKey(alias, key, tenantDomain);
            return Response.ok().build();
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to upload the certificate with alias: " + alias + " to the keystore.");
        }
    }

    public Response deletePrivateKey(String alias) {
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            getKeyStoreManager().deletePrivateKey(alias, tenantDomain);
            return Response.ok().build();
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to upload the certificate with alias: " + alias + " to the keystore.");
        }
    }
//    public Response getAllPrivateKeys() {
//
//        String tenantDomain = ContextLoader.getTenantDomainFromContext();
//        try {
//            List<CertData> privateKeyData = getKeyStoreManager().getAllPrivateKeys(tenantDomain);
//            return new PrivateKeysDataToExternal().apply(privateKeyData);
//        } catch (KeyStoreManagementException e) {
//            throw handleException(e,
//                    "Unable to get all the private keys from the to the keystore of the tenant: " + tenantDomain);
//        }
//    }

    private APIError handleException(KeyStoreManagementException e, String description) {

        ErrorResponse.Builder builder =
                new ErrorResponse.Builder().withCode(e.getErrorCode()).withMessage(description)
                        .withDescription(e.getMessage());
        Response.Status status;
        ErrorResponse errorResponse;
        if (e instanceof KeyStoreManagementServerException) {
            errorResponse = builder.build(LOG, e, description);
            status = Response.Status.INTERNAL_SERVER_ERROR;
        } else {
            errorResponse = builder.build(LOG, description);
            status = Response.Status.BAD_REQUEST;
        }
        return new APIError(status, errorResponse);
    }

//    private APIError handleException(KeyStoreConstants.ErrorMessage errorMessage, String data, Exception e,
//                                     Response.Status status) {
//
//        ErrorResponse.Builder builder =
//                new ErrorResponse.Builder().withCode(errorMessage.getCode())
//                        .withMessage(generateErrorMessage(errorMessage.getMessage(), data))
//                        .withDescription(e.getMessage());
//        ErrorResponse errorResponse = builder.build(LOG, e, e.getMessage());
//        return new APIError(status, errorResponse);
//    }
//
//    private APIError handleException(KeyStoreConstants.ErrorMessage errorMessage, String data, String description,
//                                     Response.Status status) {
//
//        ErrorResponse.Builder builder =
//                new ErrorResponse.Builder().withCode(errorMessage.getCode())
//                        .withMessage(generateErrorMessage(errorMessage.getMessage(), data))
//                        .withDescription(description);
//        ErrorResponse errorResponse = builder.build();
//        return new APIError(status, errorResponse);
//    }

//    private static String generateErrorMessage(String message, String data) {
//
//        if (StringUtils.isNotBlank(data)) {
//            message = String.format(message, data);
//        }
//        return message;
//    }
}
