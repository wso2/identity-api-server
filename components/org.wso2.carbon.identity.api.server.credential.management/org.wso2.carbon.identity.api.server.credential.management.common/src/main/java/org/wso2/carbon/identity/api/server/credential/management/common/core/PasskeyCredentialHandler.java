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

package org.wso2.carbon.identity.api.server.credential.management.common.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialHandler;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementConstants.CredentialTypes;
import org.wso2.carbon.identity.api.server.credential.management.common.CredentialManagementServiceDataHolder;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDeletionRequestDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.CredentialMgtException;
import org.wso2.carbon.identity.api.server.credential.management.common.utils.CredentialManagementUtils;
import org.wso2.carbon.identity.application.authenticator.fido2.core.WebAuthnService;
import org.wso2.carbon.identity.application.authenticator.fido2.dto.FIDO2CredentialRegistration;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorClientException;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorServerException;
import org.wso2.carbon.identity.base.IdentityRuntimeException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Handler for managing passkey (FIDO2/WebAuthn) credentials.
 */
public class PasskeyCredentialHandler implements CredentialHandler {

    private static final Log LOG = LogFactory.getLog(PasskeyCredentialHandler.class);

    private final WebAuthnService webAuthnService;

    public PasskeyCredentialHandler() {

        this.webAuthnService = CredentialManagementServiceDataHolder.getWebAuthnService();
    }

    @Override
    public List<CredentialDTO> getCredentials(String entityId) throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving passkey credentials for entity ID: " + entityId);
        }
        try {
            String username = resolveUsernameFromUserId(entityId);
            Collection<FIDO2CredentialRegistration> passkeyCredentials = webAuthnService
                    .getFIDO2DeviceMetaData(username);

            List<CredentialDTO> credentialDTOs = new ArrayList<>();
            for (FIDO2CredentialRegistration credential : passkeyCredentials) {
                credentialDTOs.add(mapPasskeyToCredentialDTO(credential));
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully retrieved " + credentialDTOs.size() + " passkey credentials for entity ID: "
                        + entityId);
            }
            return credentialDTOs;
        } catch (FIDO2AuthenticatorServerException e) {
            throw CredentialManagementUtils.handleServerException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_PASSKEYS, e, entityId);
        } catch (IdentityRuntimeException e) {
            throw CredentialManagementUtils.handleClientException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_ENTITY_NOT_FOUND, e, entityId);
        }
    }

    @Override
    public void deleteCredential(CredentialDeletionRequestDTO credentialDeletionRequest)
            throws CredentialMgtException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting passkey credential for entity ID: " + credentialDeletionRequest.getEntityId());
        }
        try {
            String username = resolveUsernameFromUserId(credentialDeletionRequest.getEntityId());
            webAuthnService.deregisterFIDO2Credential(credentialDeletionRequest.getCredentialId(), username);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully deleted passkey credential for entity ID: "
                        + credentialDeletionRequest.getEntityId());
            }
        } catch (FIDO2AuthenticatorClientException e) {
            throw CredentialManagementUtils.handleClientException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PASSKEY_CREDENTIAL, e,
                    credentialDeletionRequest.getEntityId());
        } catch (FIDO2AuthenticatorServerException e) {
            throw CredentialManagementUtils.handleServerException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_DELETE_PASSKEYS, e,
                    credentialDeletionRequest.getEntityId());
        } catch (IdentityRuntimeException e) {
            throw CredentialManagementUtils.handleClientException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_ENTITY_NOT_FOUND, e,
                    credentialDeletionRequest.getEntityId());
        }
    }

    /**
     * Map FIDO2CredentialRegistration to CredentialDTO.
     *
     * @param credential FIDO2CredentialRegistration object.
     * @return CredentialDTO object.
     */
    private CredentialDTO mapPasskeyToCredentialDTO(FIDO2CredentialRegistration credential) {

        return new CredentialDTO.Builder()
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
            RealmService userRealm = CredentialManagementServiceDataHolder.getRealmService();
            String tenantDomain = PrivilegedCarbonContext.getThreadLocalCarbonContext().getTenantDomain();
            AbstractUserStoreManager userStoreManager = (AbstractUserStoreManager) userRealm
                    .getTenantUserRealm(IdentityTenantUtil.getTenantId(tenantDomain)).getUserStoreManager();
            return userStoreManager.getUserNameFromUserID(userId) + "@" + tenantDomain;
        } catch (UserStoreException e) {
            throw CredentialManagementUtils.handleServerException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_USERNAME_FROM_USERID, e, userId);
        } catch (org.wso2.carbon.user.api.UserStoreException e) {
            throw CredentialManagementUtils.handleServerException(
                    CredentialManagementConstants.ErrorMessages.ERROR_CODE_GET_USERNAME_FROM_USERID, e, userId);
        }
    }
}
