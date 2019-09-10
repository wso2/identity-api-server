/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.api.server.common;

import org.apache.log4j.MDC;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.governance.IdentityGovernanceService;
import org.wso2.carbon.identity.recovery.ChallengeQuestionManager;

import java.util.UUID;

/**
 * Common util class
 */
public class Util {

    /**
     * Get ChallengeQuestionManager osgi service
     *
     * @return ChallengeQuestionManager
     */
    public static ChallengeQuestionManager getChallengeQuestionManager() {
        return (ChallengeQuestionManager) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(ChallengeQuestionManager.class, null);
    }

    public static IdentityGovernanceService getIdentityGovernanceService() {
        return (IdentityGovernanceService) PrivilegedCarbonContext.getThreadLocalCarbonContext()
                .getOSGiService(IdentityGovernanceService.class, null);
    }

    /**
     * Get correlation id of current thread
     *
     * @return correlation-id
     */
    public static String getCorrelation() {
        String ref;
        if (isCorrelationIDPresent()) {
            ref = MDC.get(Constants.CORRELATION_ID_MDC).toString();
        } else {
            ref = UUID.randomUUID().toString();

        }
        return ref;
    }

    /**
     * Check whether correlation id present in the log MDC
     *
     * @return
     */
    public static boolean isCorrelationIDPresent() {
        return MDC.get(Constants.CORRELATION_ID_MDC) != null;
    }
}
