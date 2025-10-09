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

package org.wso2.carbon.identity.api.server.user.credential.management.common.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialHandler;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.user.credential.management.common.UserCredentialManagementServiceDataHolder;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.dto.UserCredentialDeletionRequestDTO;
import org.wso2.carbon.identity.api.server.user.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.user.credential.management.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.application.authenticator.fido2.core.WebAuthnService;
import org.wso2.carbon.identity.application.authenticator.fido2.dto.FIDO2CredentialRegistration;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorClientException;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorServerException;
import org.wso2.carbon.identity.base.IdentityRuntimeException;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Handler for managing passkey (FIDO2/WebAuthn) credentials.
 */
public class PasskeyCredentialHandler implements UserCredentialHandler {

    private static final Log LOG = LogFactory.getLog(PasskeyCredentialHandler.class);

    private final WebAuthnService webAuthnService;

    public PasskeyCredentialHandler() {

        this.webAuthnService = UserCredentialManagementServiceDataHolder.getWebAuthnService();
    }

    @Override
    public List<UserCredentialDTO> getCredentialsForUser(String userId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving passkey credentials for user: " + userId);
        }
        try {
            String username = resolveUsernameFromUserId(userId);
            Collection<FIDO2CredentialRegistration> passkeyCredentials = webAuthnService
                    .getFIDO2DeviceMetaData(username);

            List<UserCredentialDTO> credentialDTOs = new ArrayList<>();
            for (FIDO2CredentialRegistration credential : passkeyCredentials) {
                credentialDTOs.add(mapPasskeyToCredentialDTO(credential));
            }

            if (LOG.isDebugEnabled()) {
                LOG.info("Successfully retrieved " + credentialDTOs.size() + " passkey credentials for user: "
                        + userId);
            }
            return credentialDTOs;
        } catch (FIDO2AuthenticatorServerException e) {
            throw CredentialManagementUtils.handleServerException(
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_PASSKEYS, e, userId);
        } catch (IdentityRuntimeException e) {
            throw CredentialManagementUtils.handleClientException(
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_USER_NOT_FOUND, e, userId);
        }
    }

    @Override
    public void deleteCredentialForUser(UserCredentialDeletionRequestDTO userCredentialDeletionRequest)
            throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting passkey credential for user: " + userCredentialDeletionRequest.getUserId());
        }
        try {
            String username = resolveUsernameFromUserId(userCredentialDeletionRequest.getUserId());
            webAuthnService.deregisterFIDO2Credential(userCredentialDeletionRequest.getCredentialId(), username);
            if (LOG.isDebugEnabled()) {
                LOG.info("Successfully deleted passkey credential for user: "
                        + userCredentialDeletionRequest.getUserId());
            }
        } catch (FIDO2AuthenticatorClientException e) {
            throw CredentialManagementUtils.handleClientException(
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PASSKEY_CREDENTIAL, e,
                    userCredentialDeletionRequest.getUserId());
        } catch (FIDO2AuthenticatorServerException e) {
            throw CredentialManagementUtils.handleServerException(
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PASSKEYS, e,
                    userCredentialDeletionRequest.getUserId());
        } catch (IdentityRuntimeException e) {
            throw CredentialManagementUtils.handleClientException(
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_USER_NOT_FOUND, e,
                    userCredentialDeletionRequest.getUserId());
        }
    }

    /**
     * Map FIDO2CredentialRegistration to UserCredentialDTO.
     *
     * @param credential FIDO2CredentialRegistration object.
     * @return UserCredentialDTO object.
     */
    private UserCredentialDTO mapPasskeyToCredentialDTO(FIDO2CredentialRegistration credential) {

        return new UserCredentialDTO.Builder()
            .credentialId(credential.getCredential().getCredentialId().getBase64Url())
            .displayName(credential.getDisplayName())
            .type(CredentialTypes.PASSKEY.getApiValue())
            .build();
    }

    /**
     * Get username from user ID.
     *
     * @param userId User ID.
     * @return Username.
     * @throws CredentialMgtException Error while retrieving the username.
     */
    private String resolveUsernameFromUserId(String userId) throws CredentialMgtException {

        try {
            UserRealm userRealm = PrivilegedCarbonContext.getThreadLocalCarbonContext().getUserRealm();
            AbstractUserStoreManager userStoreManager = (AbstractUserStoreManager) userRealm.getUserStoreManager();
            return userStoreManager.getUserNameFromUserID(userId);
        } catch (UserStoreException e) {
            throw CredentialManagementUtils.handleServerException(
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_USERNAME_FROM_USERID, e, userId);
        } catch (org.wso2.carbon.user.api.UserStoreException e) {
            throw CredentialManagementUtils.handleServerException(
                    UserCredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_USERNAME_FROM_USERID, e, userId);
        }
    }
}
