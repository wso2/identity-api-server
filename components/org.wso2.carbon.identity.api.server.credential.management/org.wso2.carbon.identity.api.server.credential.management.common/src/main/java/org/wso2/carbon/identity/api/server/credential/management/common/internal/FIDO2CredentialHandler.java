/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
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

package org.wso2.carbon.identity.api.server.credential.management.common.internal;

import org.wso2.carbon.identity.api.server.credential.management.common.AdminCredentialManagementServiceDataHolder;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtClientException;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtException;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtServerException;
import org.wso2.carbon.identity.api.server.credential.management.common.service.CredentialHandler;
import org.wso2.carbon.identity.application.authenticator.fido2.core.WebAuthnService;
import org.wso2.carbon.identity.application.authenticator.fido2.dto.FIDO2CredentialRegistration;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorClientException;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorServerException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FIDO2CredentialHandler implements CredentialHandler {

    @Override
    public List<CredentialDTO> getCredentialsForUser(String userId) throws AdminCredentialMgtException {
        try {
            WebAuthnService webAuthnService = AdminCredentialManagementServiceDataHolder.getWebAuthnService();
            Collection<FIDO2CredentialRegistration> fido2Credentials = webAuthnService.getFIDO2DeviceMetaData(userId);

            List<CredentialDTO> credentialDTOs = new ArrayList<>();

            for (FIDO2CredentialRegistration credential : fido2Credentials) {
                credentialDTOs.add(mapFIDO2ToCredentialDTO(credential));
            }

            return credentialDTOs;
        } catch (FIDO2AuthenticatorServerException e) {
            throw new AdminCredentialMgtServerException(
                    "CM-65003",
                    "Server error retrieving passkey credentials for user: " + userId,
                    "The server encountered an error while fetching passkey credentials.",
                    e
            );
        }
    }

    @Override
    public void deleteCredentialForUser(String userId, String credentialId) throws AdminCredentialMgtException {
        try {
            WebAuthnService webAuthnService = AdminCredentialManagementServiceDataHolder.getWebAuthnService();
            webAuthnService.deregisterFIDO2Credential(credentialId);
        } catch (FIDO2AuthenticatorClientException e) {
            throw new AdminCredentialMgtClientException(
                    "CM-65004",
                    "Client error deleting passkey credential: " + credentialId,
                    "The request to delete the credential was invalid.",
                    e
            );
        } catch (FIDO2AuthenticatorServerException e) {
            throw new AdminCredentialMgtServerException(
                    "CM-65005",
                    "Server error deleting passkey credential: " + credentialId,
                    "The server encountered an error while deleting the passkey credential.",
                    e
            );
        }
    }

    private CredentialDTO mapFIDO2ToCredentialDTO(FIDO2CredentialRegistration credential) {
        CredentialDTO credentialDTO = new CredentialDTO();
        if (credential.getCredential() != null && credential.getCredential().getCredentialId() != null) {
            credentialDTO.setCredentialId(credential.getCredential().getCredentialId().getBase64Url());
        }
        credentialDTO.setDisplayName(credential.getDisplayName());
        credentialDTO.setType(CredentialTypes.PASSKEY.getApiValue());

        return credentialDTO;
    }
}
