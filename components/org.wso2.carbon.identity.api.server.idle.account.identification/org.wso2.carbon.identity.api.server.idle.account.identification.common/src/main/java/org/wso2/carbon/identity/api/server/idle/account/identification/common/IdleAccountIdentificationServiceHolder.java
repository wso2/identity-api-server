package org.wso2.carbon.identity.api.server.idle.account.identification.common;

import org.wso2.carbon.identity.idle.account.identification.services.IdleAccountIdentificationService;

/**
 * Service holder class for idle account identification.
 */
public class IdleAccountIdentificationServiceHolder {

    private static IdleAccountIdentificationService idleAccountIdentificationService;

    /**
     * Get IdleAccountIdentificationService OSGi service.
     *
     * @return Idle account identification Service.
     */
    public static IdleAccountIdentificationService getIdleAccountIdentificationService() {

        return idleAccountIdentificationService;
    }

    /**
     * Set IdleAccountIdentificationService OSGi service.
     *
     * @param idleAccountIdentificationService Idle account identification Service.
     */
    public static void setIdleAccountIdentificationService(
            IdleAccountIdentificationService idleAccountIdentificationService) {

        IdleAccountIdentificationServiceHolder.idleAccountIdentificationService = idleAccountIdentificationService;
    }
}
