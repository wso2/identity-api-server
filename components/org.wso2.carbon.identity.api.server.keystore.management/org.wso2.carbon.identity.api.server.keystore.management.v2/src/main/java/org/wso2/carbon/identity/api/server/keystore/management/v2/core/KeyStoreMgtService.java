/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.identity.api.server.keystore.management.v2.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.api.server.common.ContextLoader;
import org.wso2.carbon.identity.api.server.common.error.APIError;
import org.wso2.carbon.identity.api.server.common.error.ErrorResponse;
import org.wso2.carbon.identity.api.server.keystore.management.v2.function.CertificateDataToExternal;
import org.wso2.carbon.identity.api.server.keystore.management.v2.function.KeysDataToExternal;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.CertificateData;
import org.wso2.carbon.identity.api.server.keystore.management.v2.model.KeysData;
import org.wso2.carbon.security.keystore.KeyStoreManagementException;
import org.wso2.carbon.security.keystore.KeyStoreManagementServerException;
import org.wso2.carbon.security.keystore.service.KeyData;

import java.util.List;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.api.server.keystore.management.common.KeyStoreManagamentDataHolder.getKeyStoreManager;

/**
 * Core class for handling keystore service v2 APIs.
 */
public class KeyStoreMgtService {

    private static final Log LOG = LogFactory.getLog(KeyStoreMgtService.class);

    /**
     * Returns private key data for the given alias from the tenant keystore.
     *
     * @param alias Alias.
     * @return PrivateKeyDataObject.
     */
    public CertificateData getPrivateKey(String alias) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            KeyData privateKeyData = getKeyStoreManager().getPrivateKeyData(alias, tenantDomain);
            return new CertificateDataToExternal().apply(privateKeyData);
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to get the certificate with alias: " + alias + " to the keystore.");
        }
    }

    /**
     * Upload private key with the given alias to the tenant keystore.
     *
     * @param alias Alias.
     * @param key   Private Key.
     * @return Response.
     */
    public Response uploadPrivateKey(String alias, String key, String certificateChain) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            getKeyStoreManager().addPrivateKey(alias, key, certificateChain, tenantDomain);
            return Response.ok().build();
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Unable to upload the certificate with alias: " + alias + " to the keystore.");
        }
    }

    /**
     * Delete the private key with the given alias from the tenant keystore.
     *
     * @param alias Alias of the key.
     * @return Response.
     */
    public Response deletePrivateKey(String alias) {
        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            getKeyStoreManager().deletePrivateKey(alias, tenantDomain);
            return Response.ok().build();
        } catch (KeyStoreManagementException e) {
            throw handleException(e, "Error occurred while deleting the certificate with alias: " + alias +
                    " from the keystore.");
        }
    }

    /**
     * Returns data of all the private keys from the tenant keystore.
     *
     * @return List of PrivateKeysResponse
     */
    public List<KeysData> getAllPrivateKeys(String filter) {

        String tenantDomain = ContextLoader.getTenantDomainFromContext();
        try {
            List<KeyData> privateKeyData = getKeyStoreManager().getAllPrivateKeys(filter, tenantDomain);
            return new KeysDataToExternal().apply(privateKeyData);
        } catch (KeyStoreManagementException e) {
            throw handleException(e,
                    "Unable to get all the private keys from the to the keystore of the tenant: " + tenantDomain);
        }
    }

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

}
