package org.wso2.carbon.identity.api.expired.password.identification.v1.factories;

import org.wso2.carbon.identity.api.expired.password.identification.common.PasswordExpiryServiceHolder;
import org.wso2.carbon.identity.api.expired.password.identification.v1.core.PasswordExpiredUsersManagementApiService;
import org.wso2.carbon.identity.password.expiry.services.ExpiredPasswordIdentificationService;

/**
 * Factory class for PasswordExpiredUsersManagementApiService.
 */
public class PasswordExpiredUsersManagementApiServiceFactory {

    private static final PasswordExpiredUsersManagementApiService SERVICE;

    static {
        ExpiredPasswordIdentificationService expiredPasswordIdentificationService =
                PasswordExpiryServiceHolder.getExpiredPasswordIdentificationService();

        if (expiredPasswordIdentificationService == null) {
            throw new IllegalStateException("RolePermissionManagementService is not available from OSGi context.");
        }

        SERVICE = new PasswordExpiredUsersManagementApiService(expiredPasswordIdentificationService);
    }

    /**
     * Get PasswordExpiredUsersManagementApiService service instance.
     *
     * @return PasswordExpiredUsersManagementApiService service.
     */
    public static PasswordExpiredUsersManagementApiService getExpiredPasswordIdentificationService() {
        return SERVICE;
    }
}
