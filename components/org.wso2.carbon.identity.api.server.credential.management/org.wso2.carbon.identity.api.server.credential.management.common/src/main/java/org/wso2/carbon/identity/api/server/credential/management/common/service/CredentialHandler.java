package org.wso2.carbon.identity.api.server.credential.management.common.service;

import org.wso2.carbon.identity.api.server.credential.management.common.dto.CredentialDTO;
import org.wso2.carbon.identity.api.server.credential.management.common.exception.AdminCredentialMgtException;
import java.util.List;

public interface CredentialHandler {
    /**
     * Retrieves credentials for a given user.
     */
    List<CredentialDTO> getCredentialsForUser(String userId) throws AdminCredentialMgtException;

    /**
     * Deletes a credential for a user.
     */
    void deleteCredentialForUser(String userId, String credentialId) throws AdminCredentialMgtException;
}
