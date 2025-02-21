package org.wso2.carbon.identity.api.server.registration.common;

import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.user.registration.mgt.RegistrationFlowMgtService;

/**
 * This class is used to hold the RegistrationFlowMgtService instance.
 */
public class RegistrationFlowMgtServiceHolder {

    private RegistrationFlowMgtServiceHolder() {
    }

    private static class UserRegistrationMgtServiceHolderInstance {

        static final RegistrationFlowMgtService SERVICE = (RegistrationFlowMgtService) PrivilegedCarbonContext
            .getThreadLocalCarbonContext().getOSGiService(RegistrationFlowMgtService.class, null);
    }

    public static RegistrationFlowMgtService getUserRegistrationMgtService() {

        return UserRegistrationMgtServiceHolderInstance.SERVICE;
    }
}
